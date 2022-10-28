package ru.rusguardian.bot.command.main.start.calculate_living_wage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.Status;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

import static ru.rusguardian.domain.TelegramDataEnum.SALARY_QUESTION;

@Component
public class _4_ConfirmFamilyCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = SALARY_QUESTION;

    @Autowired
    private _5_SettingSalariesCommand settingSalariesCommand;

    @Override
    public Command getCommand() {
        return this;
    }

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
        String salaryQuestionMessage = TELEGRAM_DATA.getTextMessage();
        SendMessage sendMessage = getSimpleSendMessage(update, String.format(salaryQuestionMessage, chatLivingWage));
        sendMessage.enableHtml(true);
        return sendMessage;
    }

    private SendMessage getSecondSendMessage(Update update, Chat chat) {
        return getSimpleSendMessage(update, settingSalariesCommand.getMonthSalaryQuestion(chat));
    }

}
