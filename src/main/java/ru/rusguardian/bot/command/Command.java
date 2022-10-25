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
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.Status;
import ru.rusguardian.domain.TelegramDataEnum;
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

    private static final List<List<String>> replyButtonLines = new ArrayList<>();

    private static final List<String> firstLineButtons = new ArrayList<>();
    private static final List<String> secondLineButtons = new ArrayList<>();
    private static final List<String> thirdLineButtons = new ArrayList<>();
    private static final String CATALOG_BUTTON = "\uD83D\uDECDКаталог";
    private static final String BASKET_BUTTON = "\uD83D\uDED2Корзина";
    private static final String STOCKS_BUTTON = "\uD83C\uDF81Акции";
    private static final String CONTACTS_BUTTON = "Контакты";
    private static final String ABOUT_US_BUTTON = "О нас";
    private static final String YOUTUBE_BUTTON = "\uD83D\uDCF9You Tube";
    private static final String CALCULATE_BUTTON = "\uD83D\uDCA1Посчитать прожиточный минимум";

    static {
        firstLineButtons.add(CATALOG_BUTTON);
        firstLineButtons.add(BASKET_BUTTON);
        firstLineButtons.add(STOCKS_BUTTON);
        secondLineButtons.add(CONTACTS_BUTTON);
        secondLineButtons.add(ABOUT_US_BUTTON);
        secondLineButtons.add(YOUTUBE_BUTTON);
        thirdLineButtons.add(CALCULATE_BUTTON);

        replyButtonLines.add(firstLineButtons);
        replyButtonLines.add(secondLineButtons);
        replyButtonLines.add(thirdLineButtons);
    }

    protected abstract CommandName getType();

    protected abstract void mainExecute(Update update) throws TelegramApiException;

    protected List<List<String>> getMainReplyButtonLines() {
        return replyButtonLines;
    }

    protected List<List<String>> getMainMenuReplyButtonLine() {
        return replyButtonLines;
    }

    protected InlineKeyboardMarkup getOneLinedKeyboard(Map<String, String> commandButtonsMap) {

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

    protected ReplyKeyboardMarkup getReplyKeyboard(List<List<String>> replyButtonLines) {

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (List<String> keyboardRow : replyButtonLines) {
            keyboardRows.add(getKeyboardRow(keyboardRow));
        }

        return ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .keyboard(keyboardRows)
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

    protected InlineKeyboardMarkup getMultipleLinedInlineKeyboard(List<String> buttonLines) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(getInlineButtons(buttonLines));

        return inlineKeyboardMarkup;
    }

    private List<List<InlineKeyboardButton>> getInlineButtons(List<String> buttonLines) {
        List<List<InlineKeyboardButton>> nameButtons = new ArrayList<>();

        for (String regionName : buttonLines) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(regionName);
            button.setCallbackData(regionName);

            nameButtons.add(List.of(button));
        }

        return nameButtons;
    }

    private KeyboardRow getKeyboardRow(List<String> replyButtons) {

        KeyboardRow keyboardRow = new KeyboardRow();
        for (String button : replyButtons) {
            keyboardRow.add(button);
        }

        return keyboardRow;
    }

    protected SendMessage getSendMessageByTelegramData(Update update, TelegramDataEnum telegramDataEnum) {
        String message = telegramDataEnum.getTextMessage();
        return getSimpleSendMessage(update, message);
    }

    protected SendMessage getSimpleSendMessage(Update update, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(TelegramUtils.getChatId(update));
        sendMessage.setText(message);

        return sendMessage;
    }

    protected SendMessage getSendMessageWithReplyKeyboard(Update update, String message, List<List<String>> replyButtonLines) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(TelegramUtils.getChatId(update));
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(getReplyKeyboard(replyButtonLines));

        return sendMessage;
    }

    protected SendMessage getSendMessageWithTelegramDataAndReplyKeyboard(Update update, TelegramDataEnum telegramDataEnum, List<List<String>> replyButtonLines) {
        SendMessage sendMessage = getSendMessageByTelegramData(update, telegramDataEnum);
        sendMessage.setReplyMarkup(getReplyKeyboard(replyButtonLines));

        return sendMessage;
    }

    protected SendMessage getSendMessageWithTelegramDataAndInlineKeyboard(Update update, TelegramDataEnum telegramDataEnum, List<String> inlineButtonLines) {
        SendMessage sendMessage = getSendMessageByTelegramData(update, telegramDataEnum);
        sendMessage.setReplyMarkup(getMultipleLinedInlineKeyboard(inlineButtonLines));

        return sendMessage;
    }

    protected void changeUserStatus(Update update, Status status) {
        Long chatId = TelegramUtils.getChatId(update);
        chatService.updateChatStatus(chatId, status);
        log.debug("Updated status to {} for chat with id = {}", status, chatId);
    }

    protected void createDefaultUser(Update update) {
        Long chatId = TelegramUtils.getChatId(update);
        String username = TelegramUtils.getUsername(update);
        Chat defaultChat = new Chat(chatId, username, Status.NEW, 1, 0, 0, null, new ArrayList<>());
        chatService.create(defaultChat);
    }

}
