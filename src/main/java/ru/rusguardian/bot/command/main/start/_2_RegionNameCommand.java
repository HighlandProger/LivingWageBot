package ru.rusguardian.bot.command.main.start;

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
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.domain.Status;
import ru.rusguardian.util.TelegramUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@Slf4j
public class _2_RegionNameCommand extends Command {

    private static final String SUCCESS_MESSAGE = "Отлично!";
    private static final String ERROR_MESSAGE = "Извините. Не могу распознать введенный Вами регион." +
            " Попробуйте найти его из выпадающего списка или введите снова.";

    @Autowired
    private _1_ChooseRegionCommand chooseRegionCommand;

    @Autowired
    private _3_DefaultFamilyCommand defaultFamilyCommand;

    @Override
    protected CommandName getType() {
        return CommandName.REGION_NAME;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        String regionName = TelegramUtils.getTextFromUpdate(update);
        Optional<RegionLivingWage> regionLivingWageOptional = appDataService.getRegionLivingWageByName(regionName);
        if (regionLivingWageOptional.isPresent()) {
            nextStep(update, regionLivingWageOptional.get());
        } else {
            previousStep(update, regionName);
        }

    }

    private void nextStep(Update update, RegionLivingWage regionLivingWage) {
        try {
            SendMessage sendMessage = getSimpleSendMessage(update, SUCCESS_MESSAGE);
            Chat chat = chatService.findById(TelegramUtils.getChatId(update));
            chat.setRegionLivingWage(regionLivingWage);
            chatService.updateChat(chat);
            livingWageBot.execute(sendMessage);
            defaultFamilyCommand.mainExecute(update);
            changeUserStatus(update, Status.SETTING_FAMILY);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void previousStep(Update update, String wrongRegionName) {
        try {
            SendMessage sendMessage = getSimpleSendMessage(update, ERROR_MESSAGE);
            List<String> possibleRegionNames = getPossibleRegionNames(wrongRegionName);
            InlineKeyboardMarkup inlineKeyboardMarkup = getPossibleRegionsInlineKeyboard(possibleRegionNames);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            livingWageBot.execute(sendMessage);
            chooseRegionCommand.mainExecute(update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private List<String> getPossibleRegionNames(String wrongRegionName) {
        List<String> possibleRegionNames = new ArrayList<>();
        RatcliffObershelp metric = new RatcliffObershelp();
        for (RegionLivingWage regionLivingWage : appDataService.getRegionLivingWages()) {
            String regionName = regionLivingWage.getRegionName();
            double similarityPercent = metric.similarity(regionName.toUpperCase(Locale.ROOT), wrongRegionName.toUpperCase(Locale.ROOT));
            if (similarityPercent > 0.5) {
                possibleRegionNames.add(regionName);
            }
        }
        return possibleRegionNames;
    }
}
