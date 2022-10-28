package ru.rusguardian.bot.command.main.start.admin.about_us;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.domain.TelegramDataEnum;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.SEND_ABOUT_US;

@Component
public class _2_SendAboutUsCommand extends Command {

    private static final TelegramDataEnum TELEGRAM_DATA = SEND_ABOUT_US;

    private static final List<String> inlineButtons = new ArrayList<>();
    private static final String CONFIRM_BUTTON = "Подтвердить";
    private static final String MAIN_MENU_BUTTON = "\uD83C\uDFE0В главное меню";

    static {
        inlineButtons.add(CONFIRM_BUTTON);
        inlineButtons.add(MAIN_MENU_BUTTON);
    }

    @Override
    protected CommandName getType() {
        return CommandName.SEND_ABOUT_US;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {


        if (isUpdateHasPhoto(update)) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setPhoto(new InputFile(update.getMessage().getPhoto().get(0).getFileId()));
            sendPhoto.setCaption(update.getMessage().getText());
            sendPhoto.setReplyMarkup(getMultipleLinedInlineKeyboard(inlineButtons));

            livingWageBot.execute(sendPhoto);
        } else if (isUpdateHasVideo(update)) {
            SendVideo sendVideo = new SendVideo();
            sendVideo.setCaption(update.getMessage().getVideo().getFileId());
            sendVideo.setCaption(update.getMessage().getCaption());
            sendVideo.setReplyMarkup(getMultipleLinedInlineKeyboard(inlineButtons));

            livingWageBot.execute(sendVideo);
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(update.getMessage().getText());
            sendMessage.setReplyMarkup(getMultipleLinedInlineKeyboard(inlineButtons));

            livingWageBot.execute(sendMessage);
        }
    }

    private boolean isUpdateHasPhoto(Update update) {
        return update.getMessage().hasPhoto();
    }

    private boolean isUpdateHasVideo(Update update) {
        return update.getMessage().hasVideo();
    }
}
