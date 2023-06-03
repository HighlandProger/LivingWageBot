package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.TelegramDataEnum;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.CATALOG;

@Component
public class CatalogCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = CATALOG;

    private static final List<String> buttonLines = new ArrayList<>();
    private static final String PREPARED_BUSINESS_PLAN_BUTTON = "Готовый Бизнес-План";
    private static final String INDIVIDUAL_BUSINESS_PLAN_BUTTON = "Индивидуальный Бизнес-План";

    static {
        buttonLines.add(PREPARED_BUSINESS_PLAN_BUTTON);
        buttonLines.add(INDIVIDUAL_BUSINESS_PLAN_BUTTON);
    }

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.CATALOG;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendMessage sendMessage = getSendMessageWithTelegramDataAndInlineKeyboard(update, TELEGRAM_DATA, buttonLines);
        setWebAppForCatalogButton(sendMessage);

        livingWageBot.execute(sendMessage);
    }

    private void setWebAppForCatalogButton(SendMessage sendMessage) {
        WebAppInfo webAppInfo = new WebAppInfo("https://biznes-plan-russia.ru/catalog");
        ReplyKeyboard keyboard = sendMessage.getReplyMarkup();
        InlineKeyboardMarkup markup = (InlineKeyboardMarkup) keyboard;

        InlineKeyboardButton catalogButton = markup.getKeyboard().get(0).get(0);
        catalogButton.setCallbackData(null);
        catalogButton.setWebApp(webAppInfo);
    }

}
