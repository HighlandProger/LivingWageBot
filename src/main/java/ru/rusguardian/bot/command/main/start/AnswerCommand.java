package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Status;
import ru.rusguardian.util.TelegramUtils;

@Component
public class AnswerCommand extends Command {

    private static final String SUCCESS_MESSAGE = "Отлично! Вы подходите под данную программу. Чтобы узнать больше, перейдите по ссылке ...";
    private static final String NO_SUCCESS_MESSAGE = "Извините, но вы не подходите под данную программу...";
    private static final String WRONG_ANSWER = "Не понял Вас. Пожалуйста введите \"Да\" или \"Нет\"";

    @Override
    protected CommandName getType() {
        return CommandName.ANSWER;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        String resultMessage = getResultMessage(update);
        SendMessage sendMessage = getSimpleSendMessage(update, resultMessage);

        livingWageBot.execute(sendMessage);
        if (!resultMessage.equals(WRONG_ANSWER)) {
            chatService.updateChatStatus(TelegramUtils.getChatId(update), Status.EXECUTED);
        }
    }

    private String getResultMessage(Update update) {
        String userMessage = TelegramUtils.getTextFromUpdate(update);
        return switch (userMessage) {
            case "Да" -> NO_SUCCESS_MESSAGE;
            case "Нет" -> SUCCESS_MESSAGE;
            default -> WRONG_ANSWER;
        };
    }
}
