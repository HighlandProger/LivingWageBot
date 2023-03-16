package ru.rusguardian.bot.command.main.start.calculate_living_wage;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.NOT_SUCCESS_CLIENT;
import static ru.rusguardian.domain.TelegramDataEnum.SUCCESS_CLIENT;

@Component
public class _6_ResultCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum SUCCESS_TELEGRAM_DATA = SUCCESS_CLIENT;
    private static final TelegramDataEnum NOT_SUCCESS_TELEGRAM_DATA = NOT_SUCCESS_CLIENT;

    private static final List<String> buttonLines = new ArrayList<>();
    private static final String BACK_TO_MAIN_MENU_BUTTON = "\uD83C\uDFE0В главное меню";

    static {
        buttonLines.add(BACK_TO_MAIN_MENU_BUTTON);
    }

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.RESULT;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        Chat chat = chatService.findById(TelegramUtils.getChatId(update));

        SendMessage sendMessage = getSuccessSendMessage(update);

        if (!isSuitable(chat)) {
            sendMessage = getNotSuccessSendMessage(update);
        }
        sendMessage.setReplyMarkup(getMultipleLinedInlineKeyboard(buttonLines));

        livingWageBot.execute(sendMessage);
    }

    private SendMessage getSuccessSendMessage(Update update) {
        String message = SUCCESS_TELEGRAM_DATA.getTextMessage();
        return getSimpleSendMessage(update, message);
    }

    private SendMessage getNotSuccessSendMessage(Update update) {
        String message = NOT_SUCCESS_TELEGRAM_DATA.getTextMessage();
        return getSimpleSendMessage(update, message);
    }

    private boolean isSuitable(Chat chat) {
        int livingWageFor3Months = getLivingWageFor1Month(chat) * 3;
        int threeMonthsSalary = chat.getSalaries().stream().mapToInt(Integer::intValue).sum();
        return livingWageFor3Months > threeMonthsSalary;
    }

    private int getLivingWageFor1Month(Chat chat) {

        RegionLivingWage regionLivingWage = chat.getRegionLivingWage();
        int humansInFamily = chat.getEmployeeCount() + chat.getRetireeCount() + chat.getChildCount();

        return humansInFamily * regionLivingWage.getHumanLivingWage();
    }
}
