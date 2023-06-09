package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.*;
import ru.rusguardian.domain.TelegramDataEnum;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.STOCKS;

@Component
public class StocksCommand extends Command implements SendMessageService, SendPhotoService, SendVideoService {

    private static final TelegramDataEnum TELEGRAM_DATA = STOCKS;

    private static final List<List<String>> replyButtonLines = new ArrayList<>();
    private static final List<String> firstLineButtons = new ArrayList<>();
    private static final List<String> secondLineButtons = new ArrayList<>();
    private static final String ALL_STOCKS_BUTTON = CommandName.ALL_STOCKS.getName();
    private static final String MAIN_MENU_BUTTON = CommandName.MAIN_MENU.getName();

    static {
        firstLineButtons.add(ALL_STOCKS_BUTTON);
        secondLineButtons.add(MAIN_MENU_BUTTON);

        replyButtonLines.add(firstLineButtons);
        replyButtonLines.add(secondLineButtons);
    }

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.STOCKS;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        if(isStocksWithPhoto()){
            SendPhoto sendPhoto = getSendPhotoByTelegramData(update, TELEGRAM_DATA);
            livingWageBot.execute(sendPhoto);
        }
        else if(isStocksWithVideo()){
            SendVideo sendVideo = getSendVideoByTelegramData(update, TELEGRAM_DATA);
            livingWageBot.execute(sendVideo);
        }
        else {
            SendMessage sendMessage = getSendMessageWithTelegramDataAndReplyKeyboard(update, TELEGRAM_DATA, replyButtonLines);
            livingWageBot.execute(sendMessage);
        }
    }

    private boolean isStocksWithPhoto(){
        return TELEGRAM_DATA.getPhotoId() != null;
    }

    private boolean isStocksWithVideo(){
        return TELEGRAM_DATA.getVideoId() != null;
    }
}
