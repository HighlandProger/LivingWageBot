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

import static ru.rusguardian.domain.TelegramDataEnum.CONTACTS;

@Component
public class ContactsCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = CONTACTS;

    private static final List<List<String>> replyButtonLines = new ArrayList<>();
    private static final String WRITE_MESSAGE_BUTTON = "\uD83D\uDCAC️Написать";
    private static final String CALL_QUESTION_BUTTON = "\uD83D\uDCDEПозвонить";
    private static final String AVITO_BUTTON = "\uD83D\uDC8EАвито";
    private static final String SITE_BUTTON = "\uD83C\uDF10Сайт";
    private static final String MAIN_MENU_BUTTON = "\uD83C\uDFE0В главное меню";

    static {

        replyButtonLines.add(List.of(WRITE_MESSAGE_BUTTON));
        replyButtonLines.add(List.of(CALL_QUESTION_BUTTON));
        replyButtonLines.add(List.of(AVITO_BUTTON));
        replyButtonLines.add(List.of(SITE_BUTTON));
        replyButtonLines.add(List.of(MAIN_MENU_BUTTON));
    }

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.CONTACTS;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendMessage sendMessage = getSendMessageWithTelegramDataAndReplyKeyboard(update, TELEGRAM_DATA, replyButtonLines);

        livingWageBot.execute(sendMessage);
    }
}
