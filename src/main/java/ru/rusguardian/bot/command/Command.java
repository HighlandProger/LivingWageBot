package ru.rusguardian.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.LivingWageBot;
import ru.rusguardian.domain.Status;
import ru.rusguardian.service.data.ChatServiceImpl;
import ru.rusguardian.service.data.RegionLivingWageServiceImpl;
import ru.rusguardian.service.data.TelegramDataServiceImpl;
import ru.rusguardian.util.TelegramUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class Command {

    @Autowired
    protected LivingWageBot livingWageBot;

    @Autowired
    protected ChatServiceImpl chatService;

    @Autowired
    protected RegionLivingWageServiceImpl regionLivingWageService;

    @Autowired
    protected TelegramDataServiceImpl telegramDataService;

    protected abstract void mainExecute(Update update) throws TelegramApiException;

    protected abstract CommandName getType();

    protected InlineKeyboardMarkup getInlineKeyboard(Map<String, String> commandButtonsMap) {

        List<InlineKeyboardButton> inlineButtons = new ArrayList<>();
        for (Map.Entry<String, String> entry : commandButtonsMap.entrySet()) {
            InlineKeyboardButton keyboardButton = InlineKeyboardButton.builder()
                    .text(entry.getValue())
                    .callbackData(entry.getKey())
                    .build();

            inlineButtons.add(keyboardButton);
        }

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(inlineButtons))
                .build();
    }

    protected ReplyKeyboardMarkup getOneRowReplyKeyboard(List<String> replyButtons) {

        return ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .keyboard(List.of(getKeyboardRow(replyButtons)))
                .build();
    }

    protected InlineKeyboardMarkup getPossibleRegionsInlineKeyboard(List<String> regionNames) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(getRegionNameInlineButtons(regionNames));

        return inlineKeyboardMarkup;
    }

    private List<List<InlineKeyboardButton>> getRegionNameInlineButtons(List<String> regionNames) {
        List<List<InlineKeyboardButton>> nameButtons = new ArrayList<>();

        for (String regionName : regionNames) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(regionName);
            button.setCallbackData(regionName);

            nameButtons.add(List.of(button));
        }

        return nameButtons;
    }

    protected ReplyKeyboardMarkup getPossibleRegionsKeyboard(List<String> regionNames) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        for (String regionName : regionNames) {
            if (keyboardRow.size() == 3) {
                keyboardRows.add(new KeyboardRow(keyboardRow));
                keyboardRow.clear();
            }
            keyboardRow.add(regionName);
        }

        if (!keyboardRow.isEmpty()) {
            keyboardRows.add(keyboardRow);
        }

        return ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .keyboard(keyboardRows)
                .build();
    }

    private KeyboardRow getKeyboardRow(List<String> replyButtons) {

        KeyboardRow keyboardRow = new KeyboardRow();
        for (String button : replyButtons) {
            keyboardRow.add(button);
        }

        return keyboardRow;
    }

    protected SendMessage getSimpleSendMessage(Update update, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(TelegramUtils.getChatId(update));
        sendMessage.setText(message);

        return sendMessage;
    }

    protected SendMessage getSendMessageWithInlineKeyboard(Update update, String message, Map<String, String> buttonsMap) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(TelegramUtils.getChatId(update));
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(getInlineKeyboard(buttonsMap));

        return sendMessage;
    }

    protected void changeUserStatus(Update update, Status status) {
        Long chatId = TelegramUtils.getChatId(update);
        chatService.updateChatStatus(chatId, status);
        log.debug("Updated status to {} for chat with id = {}", status, chatId);
    }

}
