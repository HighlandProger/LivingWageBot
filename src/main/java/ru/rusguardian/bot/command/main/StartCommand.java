package ru.rusguardian.bot.command.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Status;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.service.data.exception.EntityNotFoundException;
import ru.rusguardian.util.TelegramUtils;

import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.WELCOME;

@Component
@Slf4j
public class StartCommand extends Command {

    private static final TelegramDataEnum TELEGRAM_DATA = WELCOME;

    @Override
    protected CommandName getType() {
        return CommandName.START;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        createChatIfNotExists(update);

        SendMessage sendMessage = getSendMessage(update);

        livingWageBot.execute(sendMessage);
    }

    private void createChatIfNotExists(Update update) {

        Long chatId = TelegramUtils.getChatId(update);
        try {
            chatService.findById(chatId);
        } catch (EntityNotFoundException e) {
            createDefaultUser(update);
        } finally {
            changeUserStatus(update, Status.NEW);
        }
    }

    private SendMessage getSendMessage(Update update) {
        String callbackMessage = getCallbackMessage(update);
        List<List<String>> replyButtonLines = getMainReplyButtonLines();
        SendMessage sendMessage = getSendMessageWithReplyKeyboard(update, callbackMessage, replyButtonLines);
        ReplyKeyboardMarkup replyKeyboardMarkup = (ReplyKeyboardMarkup) sendMessage.getReplyMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return sendMessage;
    }

    private String getCallbackMessage(Update update) {
        String userFirstName = update.getMessage().getFrom().getFirstName();
        String patternMessage = TELEGRAM_DATA.getTextMessage();
        return String.format(patternMessage, userFirstName);
    }

}
