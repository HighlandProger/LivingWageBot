package ru.rusguardian.bot.command.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.util.TelegramUtils;

@Component
@Slf4j
public class NotFoundCommand extends Command {

    private static final String NOT_FOUND_MESSAGE = "Ошибка. Команда %s не найдена. Попробуйте /start";

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        String chatId = TelegramUtils.getChatIdString(update);

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(String.format(NOT_FOUND_MESSAGE, TelegramUtils.getTextFromUpdate(update)))
                .build();


        livingWageBot.execute(sendMessage);
    }

    @Override
    protected CommandName getType() {
        return CommandName.NOT_FOUND;
    }

}
