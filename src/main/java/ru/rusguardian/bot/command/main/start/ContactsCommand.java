package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.TelegramDataEnum;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.CONTACTS;

@Component
public class ContactsCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = CONTACTS;

    private static final List<KeyboardButton> simpleButtonLines = new ArrayList<>();
    private static final KeyboardButton WRITE_MESSAGE_BUTTON = new KeyboardButton("\uD83D\uDCAC️Написать");
    private static final KeyboardButton CALL_QUESTION_BUTTON = new KeyboardButton("\uD83D\uDCDEПозвонить");
    private static final KeyboardButton SITE_BUTTON = new KeyboardButton("\uD83C\uDF10Сайт");
    private static final KeyboardButton MAIN_MENU_BUTTON = new KeyboardButton("\uD83C\uDFE0В главное меню");

    static {
        setSiteWebAppInfo(SITE_BUTTON);

        simpleButtonLines.add(WRITE_MESSAGE_BUTTON);
        simpleButtonLines.add(CALL_QUESTION_BUTTON);
        simpleButtonLines.add(SITE_BUTTON);
        simpleButtonLines.add(MAIN_MENU_BUTTON);
    }

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.CONTACTS;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendMessage sendMessage = getSendMessageByTelegramData(update, TELEGRAM_DATA);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = getKeyboardRows();
        setSiteWebAppInfo(rows.get(2).get(0));
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);

        livingWageBot.execute(sendMessage);
    }

    private List<KeyboardRow> getKeyboardRows(){
        List<KeyboardRow> rows = new ArrayList<>();
        for (KeyboardButton button : simpleButtonLines){
            KeyboardRow row = new KeyboardRow();
            row.add(button);
            rows.add(row);
        }
        return rows;
    }

    private static void setSiteWebAppInfo(KeyboardButton button){
        WebAppInfo webApp = new WebAppInfo();
        webApp.setUrl("https://biznes-plan-russia.ru");
        button.setWebApp(webApp);
    }
}
