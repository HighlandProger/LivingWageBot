package ru.rusguardian.bot.command.main.start;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${telegram.url.preparedBP}")
    private String preparedBusinessPlansURL;

    @Value("${telegram.url.individualBP}")
    private String individualBusinessPlansURL;

    @Value("${telegram.url.consultation}")
    private String consultationURL;

    private static final List<String> buttonLines = new ArrayList<>();
    private static final String PREPARED_BUSINESS_PLAN_BUTTON = CommandName.PREPARED_BUSINESS_PLAN.getName();
    private static final String INDIVIDUAL_BUSINESS_PLAN_BUTTON = CommandName.INDIVIDUAL_BUSINESS_PLAN.getName();
    private static final String CONSULTATION_BUTTON = CommandName.CONSULTATION.getName();

    static {
        buttonLines.add(PREPARED_BUSINESS_PLAN_BUTTON);
        buttonLines.add(INDIVIDUAL_BUSINESS_PLAN_BUTTON);
        buttonLines.add(CONSULTATION_BUTTON);
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
        setWebAppsForButtons(sendMessage);

        livingWageBot.execute(sendMessage);
    }

    private void setWebAppsForButtons(SendMessage sendMessage){
        ReplyKeyboard keyboard = sendMessage.getReplyMarkup();
        InlineKeyboardMarkup markup = (InlineKeyboardMarkup) keyboard;

        setWebAppForPreparedPlansButton(markup);
        setWebAppForIndividualPlansButton(markup);
        setWebAppForConsultationButton(markup);
    }

    private void setWebAppForPreparedPlansButton(InlineKeyboardMarkup markup){
        InlineKeyboardButton preparedPlansButton = markup.getKeyboard().get(0).get(0);
        preparedPlansButton.setCallbackData(null);
        preparedPlansButton.setWebApp(new WebAppInfo(preparedBusinessPlansURL));
    }

    private void setWebAppForIndividualPlansButton(InlineKeyboardMarkup markup){
        InlineKeyboardButton preparedPlansButton = markup.getKeyboard().get(1).get(0);
        preparedPlansButton.setCallbackData(null);
        preparedPlansButton.setWebApp(new WebAppInfo(individualBusinessPlansURL));
    }

    private void setWebAppForConsultationButton(InlineKeyboardMarkup markup){
        InlineKeyboardButton preparedPlansButton = markup.getKeyboard().get(2).get(0);
        preparedPlansButton.setCallbackData(null);
        preparedPlansButton.setWebApp(new WebAppInfo(consultationURL));
    }


}
