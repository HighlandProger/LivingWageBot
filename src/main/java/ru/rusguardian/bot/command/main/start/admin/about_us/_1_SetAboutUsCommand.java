package ru.rusguardian.bot.command.main.start.admin.about_us;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.Status;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.NOT_FOUND;
import static ru.rusguardian.domain.TelegramDataEnum.SET_ABOUT_US;

@Component
public class _1_SetAboutUsCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = SET_ABOUT_US;
    private static final TelegramDataEnum NOT_FOUND_TELEGRAM_DATA = NOT_FOUND;

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
        return CommandName.SET_ABOUT_US;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        Chat chat = chatService.findById(TelegramUtils.getChatId(update));
        SendMessage sendMessage;
        if (!chat.isAdmin()) {
            sendMessage = getSendMessageByTelegramData(update, NOT_FOUND_TELEGRAM_DATA);
            livingWageBot.execute(sendMessage);
        } else {
            sendMessage = getSendMessageWithTelegramDataAndReplyKeyboard(update, TELEGRAM_DATA, replyButtonLines);
            livingWageBot.execute(sendMessage);
            chatService.updateChatStatus(chat.getId(), Status.ADMIN_SETTING_ABOUT_US);
        }

    }
}
