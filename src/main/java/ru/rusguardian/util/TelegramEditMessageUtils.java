package ru.rusguardian.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.rusguardian.domain.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TelegramEditMessageUtils {

    private static final String EMPTY = "EMPTY";
    private static final String ADD = "ADD";
    private static final String REMOVE = "REMOVE";
    private static final String LOW_SPACE = "_";

    private static final String EMPLOYEE = "EMPLOYEE";
    private static final String EMPLOYEE_DESCRIPTION = "Трудоспособные";
    private static final String CHILD = "CHILD";
    private static final String CHILD_DESCRIPTION = "Дети";
    private static final String RETIREE = "RETIREE";
    private static final String RETIREE_DESCRIPTION = "Пенсионеры";

    private TelegramEditMessageUtils() {
    }

    public static InlineKeyboardMarkup getFamilyCountInlineKeyboard(Chat chat) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboards = new ArrayList<>();
        keyboards.addAll(getHumanTypeKeyboard(chat, EMPLOYEE, EMPLOYEE_DESCRIPTION));
        keyboards.addAll(getHumanTypeKeyboard(chat, CHILD, CHILD_DESCRIPTION));
        keyboards.addAll(getHumanTypeKeyboard(chat, RETIREE, RETIREE_DESCRIPTION));
        keyboards.add(getResultLineKeyboard());

        inlineKeyboardMarkup.setKeyboard(keyboards);

        return inlineKeyboardMarkup;
    }

    private static List<List<InlineKeyboardButton>> getHumanTypeKeyboard(Chat chat, String humanType, String humanTypeDescription) {

        List<List<InlineKeyboardButton>> humanTypeKeyboards = new ArrayList<>();
        humanTypeKeyboards.add(getDescriptionLineKeyboard(humanTypeDescription));
        humanTypeKeyboards.add(getConfigureLineKeyboard(chat, humanType));

        return humanTypeKeyboards;
    }

    private static List<InlineKeyboardButton> getDescriptionLineKeyboard(String humanTypeDescription) {

        InlineKeyboardButton textButton = InlineKeyboardButton.builder()
                .text(humanTypeDescription)
                .callbackData(EMPTY)
                .build();

        return List.of(textButton);
    }

    private static List<InlineKeyboardButton> getConfigureLineKeyboard(Chat chat, String familyType) {

        InlineKeyboardButton removeButton = InlineKeyboardButton.builder()
                .text("-")
                .callbackData(REMOVE + LOW_SPACE + familyType)
                .build();

        InlineKeyboardButton currentInfoButton = InlineKeyboardButton.builder()
                .text(getFamilyTypeCount(chat, familyType))
                .callbackData(EMPTY)
                .build();

        InlineKeyboardButton nextButton = InlineKeyboardButton.builder()
                .text("+")
                .callbackData(ADD + LOW_SPACE + familyType)
                .build();

        return List.of(removeButton, currentInfoButton, nextButton);
    }

    private static List<InlineKeyboardButton> getResultLineKeyboard() {

        InlineKeyboardButton textButton = InlineKeyboardButton.builder()
                .text("Подтвердить")
                .callbackData("Подтвердить")
                .build();

        return List.of(textButton);
    }

    private static String getFamilyTypeCount(Chat chat, String familyType) {

        return switch (familyType) {
            case "EMPLOYEE" -> String.valueOf(chat.getEmployeeCount());
            case "CHILD" -> String.valueOf(chat.getChildCount());
            case "RETIREE" -> String.valueOf(chat.getRetireeCount());
            default -> throw new NoSuchElementException(familyType);
        };
    }
}
