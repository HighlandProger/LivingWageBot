package ru.rusguardian.bot;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rusguardian.bot.command.SendBotMessageService;

@Component
@Log4j2
public class LivingWageBot extends TelegramLongPollingBot {

    @Autowired
    SendBotMessageService sendBotMessageService;
    @Value("${bot.username}")
    private String username;
    @Value("${bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        sendBotMessageService.sendMessage(update);
    }

}
