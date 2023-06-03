package ru.rusguardian.bot.command.service;

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

    private static final String EXCEPTION_MESSAGE = "Что-то пошло не так, пожалуйста, попробуйте нажать \"/start\". Или обратитесь в поддержку @Sotsialnyy_BusinessMan";

    private static final List<List<String>> replyButtonLines = new ArrayList<>();

    private static final List<String> firstLineButtons = new ArrayList<>();
    private static final List<String> secondLineButtons = new ArrayList<>();
    private static final List<String> thirdLineButtons = new ArrayList<>();
    private static final List<String> forthLineButtons = new ArrayList<>();

    static {
        firstLineButtons.add(CommandName.CATALOG.getName());
        firstLineButtons.add(CommandName.CLIENT_CHAT.getName());
        firstLineButtons.add(CommandName.STOCKS.getName());
        secondLineButtons.add(CommandName.CONTACTS.getName());
        secondLineButtons.add(CommandName.ABOUT_US.getName());
        secondLineButtons.add(CommandName.YOUTUBE_LINK.getName());
        thirdLineButtons.add(CommandName.CALCULATE_LIVING_WAGE.getName());
        forthLineButtons.add("тест на 350.000 руб. \uD83E\uDD11");

        replyButtonLines.add(firstLineButtons);
        replyButtonLines.add(secondLineButtons);
        replyButtonLines.add(thirdLineButtons);
        replyButtonLines.add(forthLineButtons);
    }

    protected abstract CommandName getType();

    protected abstract void mainExecute(Update update) throws TelegramApiException;

    protected void process(Update update) throws TelegramApiException {
        try{
            mainExecute(update);
        } catch (TelegramApiException | RuntimeException e){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(TelegramUtils.getChatId(update));
            sendMessage.setText(EXCEPTION_MESSAGE);

            livingWageBot.execute(sendMessage);
        }
    }

    protected final List<List<String>> getMainReplyButtonLines() {
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

    protected ReplyKeyboardMarkup getDefaultReplyKeyboard(Update update) {
        List<List<String>> mainReplyButtonLines = new ArrayList<>(getMainReplyButtonLines());
        if (isUserAdmin(update)) {
            mainReplyButtonLines.add(List.of("⭕️Администратору"));
        }
        return getReplyKeyboard(mainReplyButtonLines);
    }

    private boolean isUserAdmin(Update update) {
        Chat chat = chatService.findById(TelegramUtils.getChatId(update));
        return chat.isAdmin();
    }

    protected ReplyKeyboardMarkup getReplyKeyboard(List<List<String>> replyButtonLines) {

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (List<String> keyboardRow : replyButtonLines) {
            keyboardRows.add(getKeyboardRow(keyboardRow));
        }

        return ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .keyboard(keyboardRows)
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

    protected void changeUserStatus(Update update, Status status) {
        Long chatId = TelegramUtils.getChatId(update);
        chatService.updateChatStatus(chatId, status);
        log.debug("Updated status to {} for chat with id = {}", status, chatId);
    }

    protected Chat createDefaultUser(Update update) {
        Long chatId = TelegramUtils.getChatId(update);
        String username = TelegramUtils.getUsername(update);
        Chat defaultChat = new Chat(chatId, username, Status.NEW, 1, 0, 0, null, new ArrayList<>(), false);
        return chatService.create(defaultChat);
    }

}
