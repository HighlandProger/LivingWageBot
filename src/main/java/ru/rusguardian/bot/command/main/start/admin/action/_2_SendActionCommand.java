package ru.rusguardian.bot.command.main.start.admin.action;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.main.start.admin.AdminService;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.TelegramDataEnum;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.SEND_ACTION;

@Component
public class _2_SendActionCommand extends Command implements SendMessageService, AdminService {

    private static final TelegramDataEnum TELEGRAM_DATA = SEND_ACTION;

    private static final List<String> inlineButtons = new ArrayList<>();
    private static final String CONFIRM_BUTTON = "Подтвердить";
    private static final String MAIN_MENU_BUTTON = "\uD83C\uDFE0В главное меню";

    static {
        inlineButtons.add(CONFIRM_BUTTON);
        inlineButtons.add(MAIN_MENU_BUTTON);
    }

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.SEND_ACTION;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendMessage message = getSendMessageByTelegramData(update, TELEGRAM_DATA);
        livingWageBot.execute(message);

        if (isUpdateHasPhoto(update)) {
            SendPhoto sendPhoto = getSendPhotoFromUpdate(update);
            sendPhoto.setReplyMarkup(getMultipleLinedInlineKeyboard(inlineButtons));

            livingWageBot.execute(sendPhoto);
        } else if (isUpdateHasVideo(update)) {
            SendVideo sendVideo = getSendVideoFromUpdate(update);
            sendVideo.setReplyMarkup(getMultipleLinedInlineKeyboard(inlineButtons));

            livingWageBot.execute(sendVideo);
        } else {
            SendMessage sendMessage = getSimpleSendMessage(update, update.getMessage().getText());
            sendMessage.setReplyMarkup(getMultipleLinedInlineKeyboard(inlineButtons));

            livingWageBot.execute(sendMessage);
        }
    }
}
