package ru.rusguardian.bot.command.main.start.contacts;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.TelegramDataEnum;

import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.CALL_QUESTION;

@Component
public class CallQuestionCommand extends Command {

    private static final TelegramDataEnum TELEGRAM_DATA = CALL_QUESTION;

    @Override
    protected CommandName getType() {
        return CommandName.CALL_QUESTION;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        List<List<String>> replyButtonLines = getMainMenuReplyButtonLine();
        SendMessage sendMessage = getSendMessageWithTelegramDataAndReplyKeyboard(update, TELEGRAM_DATA, replyButtonLines);
        livingWageBot.execute(sendMessage);
    }
}
