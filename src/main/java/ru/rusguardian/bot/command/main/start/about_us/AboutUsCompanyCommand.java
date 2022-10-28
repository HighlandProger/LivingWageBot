package ru.rusguardian.bot.command.main.start.about_us;

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

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.ABOUT_US_COMPANY;

@Component
public class AboutUsCompanyCommand extends Command implements SendPhotoService, SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = ABOUT_US_COMPANY;

    private static final List<List<String>> replyButtonLines = new ArrayList<>();
    private static final List<String> firstLineButtons = new ArrayList<>();
    private static final String MAIN_MENU_BUTTON = "\uD83C\uDFE0В главное меню";

    static {
        firstLineButtons.add(MAIN_MENU_BUTTON);

        replyButtonLines.add(firstLineButtons);
    }

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.ABOUT_US_COMPANY;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendPhoto sendPhoto = getSendPhotoByTelegramData(update, TELEGRAM_DATA);
        sendPhoto.setCaption("");
        sendPhoto.setReplyMarkup(getReplyKeyboard(replyButtonLines));

        SendMessage sendMessage = getSendMessageByTelegramData(update, TELEGRAM_DATA);

        livingWageBot.execute(sendPhoto);
        livingWageBot.execute(sendMessage);

    }
}
