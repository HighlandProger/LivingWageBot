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

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.ABOUT_US;

@Component
public class AboutUsCommand extends Command implements SendMessageService, SendPhotoService {

    private static final TelegramDataEnum TELEGRAM_DATA = ABOUT_US;

    private static final List<List<String>> replyButtonLines = new ArrayList<>();
    private static final List<String> firstLineButtons = new ArrayList<>();
    private static final List<String> secondLineButtons = new ArrayList<>();

    private static final String REQUISITES_BUTTON = "\uD83D\uDCCBРеквизиты";
    private static final String MAIN_MENU_BUTTON = "\uD83C\uDFE0В главное меню";

    static {
        firstLineButtons.add(REQUISITES_BUTTON);
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
        return CommandName.ABOUT_US;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendPhoto sendPhoto = getSendPhotoByTelegramData(update, TELEGRAM_DATA);
        sendPhoto.setCaption("");
        sendPhoto.setReplyMarkup(getReplyKeyboard(replyButtonLines));

        SendMessage sendMessage = getSendMessageByTelegramData(update, TELEGRAM_DATA);
        sendMessage.disableWebPagePreview();

        livingWageBot.execute(sendPhoto);
        livingWageBot.execute(sendMessage);
    }
}
