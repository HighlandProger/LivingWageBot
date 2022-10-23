package ru.rusguardian.bot.command.main.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.util.TelegramEditMessageUtils;
import ru.rusguardian.util.TelegramUtils;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class _4_ChangeFamilyCommand extends Command {

    private static final String NEGATIVE_COUNT_TELEGRAM_DATA = "NEGATIVE_COUNT_TELEGRAM_DATA";

    @Override
    protected CommandName getType() {
        return CommandName.CHANGE_FAMILY;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        Chat chat = chatService.findById(TelegramUtils.getChatId(update));

        try {
            updateChatFamilyCount(chat, update);
            EditMessageText editMessageText = getUpdatedInlineKeyboardMessage(update, chat);
            livingWageBot.execute(editMessageText);
        } catch (IndexOutOfBoundsException e) {
            AnswerCallbackQuery answerCallbackQuery = getAttentionCallbackQuery(update, e);
            livingWageBot.execute(answerCallbackQuery);
        } catch (NoSuchElementException e) {
            log.error("Unknown input data {}", e.getMessage());
        }
    }

    private EditMessageText getUpdatedInlineKeyboardMessage(Update update, Chat chat) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(TelegramUtils.getChatIdString(update));
        editMessageText.setMessageId(TelegramUtils.getMessageId(update));
        editMessageText.setText(update.getCallbackQuery().getMessage().getText());
        editMessageText.setReplyMarkup(TelegramEditMessageUtils.getFamilyCountInlineKeyboard(chat));

        return editMessageText;
    }

    private AnswerCallbackQuery getAttentionCallbackQuery(Update update, Exception e) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(TelegramUtils.getCallbackQueryId(update));
        answerCallbackQuery.setText(e.getMessage());

        return answerCallbackQuery;
    }

    private void updateChatFamilyCount(Chat chat, Update update) {

        String data = update.getCallbackQuery().getData();
        String mathOperation = data.split("_")[0];
        switch (mathOperation) {
            case "ADD" -> addHumanByType(chat, update);
            case "REMOVE" -> removeHumanByType(chat, update);
            default -> throw new NoSuchElementException(data);
        }
        chatService.updateChat(chat);
    }

    private void addHumanByType(Chat chat, Update update) {
        String data = update.getCallbackQuery().getData();
        String humanType = data.split("_")[1];
        switch (humanType) {
            case "EMPLOYEE" -> chat.setEmployeeCount(chat.getEmployeeCount() + 1);
            case "CHILD" -> chat.setChildCount(chat.getChildCount() + 1);
            case "RETIREE" -> chat.setRetireeCount(chat.getRetireeCount() + 1);
            default -> throw new NoSuchElementException(data);
        }
    }

    private void removeHumanByType(Chat chat, Update update) {
        String data = update.getCallbackQuery().getData();
        String humanType = data.split("_")[1];
        switch (humanType) {
            case "EMPLOYEE" -> chat.setEmployeeCount(removeWithCheck(chat.getEmployeeCount()));
            case "CHILD" -> chat.setChildCount(removeWithCheck(chat.getChildCount()));
            case "RETIREE" -> chat.setRetireeCount(removeWithCheck(chat.getRetireeCount()));
            default -> throw new NoSuchElementException(data);
        }
    }

    private int removeWithCheck(int count) {
        if (count == 0) {
            String negativeCountAttentionMessage = telegramDataService.getTelegramDataByName(NEGATIVE_COUNT_TELEGRAM_DATA).getTextMessage();
            throw new IndexOutOfBoundsException(negativeCountAttentionMessage);
        }
        return --count;
    }
}
