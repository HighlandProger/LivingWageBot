package ru.rusguardian.bot.command.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

import static ru.rusguardian.domain.TelegramDataEnum.NOT_FOUND;

@Component
@Slf4j
public class NotFoundCommand extends Command {

    private static final TelegramDataEnum TELEGRAM_DATA = NOT_FOUND;

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        String chatId = TelegramUtils.getChatIdString(update);
        String incomeMessage = TelegramUtils.getTextFromUpdate(update);

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(String.format(TELEGRAM_DATA.getTextMessage(), incomeMessage))
                .build();


        livingWageBot.execute(sendMessage);
    }

    @Override
    protected CommandName getType() {
        return CommandName.NOT_FOUND;
    }

}
