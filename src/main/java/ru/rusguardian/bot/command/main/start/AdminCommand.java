package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.TelegramDataEnum;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.ADMIN;

@Component
public class AdminCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = ADMIN;

    private static final List<List<String>> replyButtonLines = new ArrayList<>();
    private static final List<String> firstLineButtons = new ArrayList<>();
    private static final List<String> secondLineButtons = new ArrayList<>();

    private static final String SEND_ACTION_BUTTON = "Отправить акцию";
    private static final String CHANGE_ABOUT_US_BUTTON = "Изменить \"О нас\"";
    private static final String MAIN_MENU_BUTTON = "\uD83C\uDFE0В главное меню";

    static {
        firstLineButtons.add(SEND_ACTION_BUTTON);
        firstLineButtons.add(CHANGE_ABOUT_US_BUTTON);
        secondLineButtons.add(MAIN_MENU_BUTTON);

        replyButtonLines.add(firstLineButtons);
        replyButtonLines.add(secondLineButtons);
    }

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.ADMIN;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendMessage sendMessage = getSendMessageWithTelegramDataAndReplyKeyboard(update, TELEGRAM_DATA, replyButtonLines);

        livingWageBot.execute(sendMessage);
    }
}
