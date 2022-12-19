package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.bot.command.service.SendPhotoService;
import ru.rusguardian.domain.TelegramDataEnum;

import static ru.rusguardian.domain.TelegramDataEnum.CLIENT_CHAT;
import static ru.rusguardian.domain.TelegramDataEnum.YOU_TUBE_LINK;

@Component
public class ClientChatCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = CLIENT_CHAT;

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.CLIENT_CHAT;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendMessage sendMessage = getSendMessageByTelegramData(update, TELEGRAM_DATA);
        sendMessage.disableWebPagePreview();

        livingWageBot.execute(sendMessage);
    }
}
