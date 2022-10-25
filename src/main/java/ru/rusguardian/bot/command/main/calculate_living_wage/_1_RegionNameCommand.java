package ru.rusguardian.bot.command.main.calculate_living_wage;

import info.debatty.java.stringsimilarity.RatcliffObershelp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.bot.command.main.start.CalculateLivingWageCommand;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static ru.rusguardian.domain.Status.SETTING_FAMILY;
import static ru.rusguardian.domain.TelegramDataEnum.ERROR_REGION_NAME;
import static ru.rusguardian.domain.TelegramDataEnum.SUCCESS_REGION_NAME;

@Component
@Slf4j
public class _1_RegionNameCommand extends Command {

    private static final TelegramDataEnum SUCCESS_TELEGRAM_DATA = SUCCESS_REGION_NAME;
    private static final TelegramDataEnum ERROR_TELEGRAM_DATA = ERROR_REGION_NAME;

    @Autowired
    private CalculateLivingWageCommand chooseRegionCommand;

    @Autowired
    private _2_DefaultFamilyCommand defaultFamilyCommand;

    @Override
    protected CommandName getType() {
        return CommandName.REGION_NAME;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        String regionName = TelegramUtils.getTextFromUpdate(update);
        try {
            RegionLivingWage regionLivingWage = regionLivingWageService.getRegionLivingWageByName(regionName);
            nextStep(update, regionLivingWage);
        } catch (NoSuchObjectException e) {
            previousStep(update, regionName);
        }

    }

    private void nextStep(Update update, RegionLivingWage regionLivingWage) throws TelegramApiException {

        updateChat(update, regionLivingWage);

        livingWageBot.execute(getSuccessSendMessage(update));

        defaultFamilyCommand.mainExecute(update);
    }

    private void previousStep(Update update, String wrongRegionName) throws TelegramApiException {

        livingWageBot.execute(getErrorSendMessage(update, wrongRegionName));

        chooseRegionCommand.mainExecute(update);
    }

    private void updateChat(Update update, RegionLivingWage regionLivingWage) {
        Chat chat = chatService.findById(TelegramUtils.getChatId(update));
        chat.setRegionLivingWage(regionLivingWage);
        chat.setStatus(SETTING_FAMILY);
        chatService.updateChat(chat);
    }

    private List<String> getPossibleRegionNames(String wrongRegionName) {
        List<String> possibleRegionNames = new ArrayList<>();
        RatcliffObershelp metric = new RatcliffObershelp();
        for (RegionLivingWage regionLivingWage : regionLivingWageService.getRegionLivingWages()) {
            String regionName = regionLivingWage.getRegionName();
            double similarityPercent = metric.similarity(regionName.toUpperCase(Locale.ROOT), wrongRegionName.toUpperCase(Locale.ROOT));
            if (similarityPercent > 0.3) {
                possibleRegionNames.add(regionName);
            }
        }
        return possibleRegionNames;
    }

    private SendMessage getSuccessSendMessage(Update update) {
        String message = SUCCESS_TELEGRAM_DATA.getTextMessage();
        return getSimpleSendMessage(update, message);
    }

    private SendMessage getErrorSendMessage(Update update, String wrongRegionName) {
        String message = ERROR_TELEGRAM_DATA.getTextMessage();

        SendMessage sendMessage = getSimpleSendMessage(update, message);

        List<String> possibleRegionNames = getPossibleRegionNames(wrongRegionName);
        InlineKeyboardMarkup inlineKeyboardMarkup = getMultipleLinedInlineKeyboard(possibleRegionNames);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }
}
