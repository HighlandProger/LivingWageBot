package ru.rusguardian.bot.command.main.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.Status;
import ru.rusguardian.util.TelegramUtils;

@Component
public class _5_ConfirmFamilyCommand extends Command {

    private static final String SALARY_QUESTION_TELEGRAM_DATA = "SALARY_QUESTION";

    @Autowired
    private _6_SettingSalariesCommand settingSalariesCommand;

    @Override
    protected CommandName getType() {
        return CommandName.CONFIRM_FAMILY;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        Chat chat = chatService.findById(TelegramUtils.getChatId(update));

        livingWageBot.execute(getFirstSendMessage(update, chat));
        livingWageBot.execute(getSecondSendMessage(update, chat));

        chatService.updateChatStatus(chat.getId(), Status.SETTING_SALARIES);
    }

    private SendMessage getFirstSendMessage(Update update, Chat chat) {
        int chatLivingWage = settingSalariesCommand.getLivingWagesSum(chat);
        String salaryQuestionMessage = telegramDataService.getTelegramDataByName(SALARY_QUESTION_TELEGRAM_DATA).getTextMessage();
        SendMessage sendMessage = getSimpleSendMessage(update, String.format(salaryQuestionMessage, chatLivingWage));
        sendMessage.enableHtml(true);
        return sendMessage;
    }

    private SendMessage getSecondSendMessage(Update update, Chat chat) {
        return getSimpleSendMessage(update, settingSalariesCommand.getMonthSalaryQuestion(chat));
    }

}
