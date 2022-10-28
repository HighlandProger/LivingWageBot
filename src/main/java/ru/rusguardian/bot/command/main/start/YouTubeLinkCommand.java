package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendPhotoService;
import ru.rusguardian.domain.TelegramDataEnum;

import static ru.rusguardian.domain.TelegramDataEnum.YOU_TUBE_LINK;

@Component
public class YouTubeLinkCommand extends Command implements SendPhotoService {

    private static final TelegramDataEnum TELEGRAM_DATA = YOU_TUBE_LINK;

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.YOUTUBE_LINK;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendPhoto sendPhoto = getDefaultSendPhotoWithReplyKeyboard(update, TELEGRAM_DATA);

        livingWageBot.execute(sendPhoto);
    }
}
