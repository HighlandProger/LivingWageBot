package ru.rusguardian.bot.command.main.start.contacts;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Status;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

import static ru.rusguardian.domain.TelegramDataEnum.WRITE_QUESTION;

@Component
public class WriteQuestionCommand extends Command {

    private static final TelegramDataEnum TELEGRAM_DATA = WRITE_QUESTION;

    @Override
    protected CommandName getType() {
        return CommandName.WRITE_QUESTION;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        chatService.updateChatStatus(TelegramUtils.getChatId(update), Status.WRITING_MESSAGE);
        SendMessage sendMessage = getSendMessageByTelegramData(update, TELEGRAM_DATA);
        livingWageBot.execute(sendMessage);
    }
}
