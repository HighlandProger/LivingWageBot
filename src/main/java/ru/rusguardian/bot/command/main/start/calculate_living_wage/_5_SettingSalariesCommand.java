package ru.rusguardian.bot.command.main.start.calculate_living_wage;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.domain.Status;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.DateUtils;
import ru.rusguardian.util.TelegramUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.rusguardian.domain.TelegramDataEnum.*;

@Component
public class _5_SettingSalariesCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum MONTH_SALARY_TELEGRAM_DATA = MONTH_SALARY_QUESTION;
    private static final TelegramDataEnum SUCCESS_TELEGRAM_DATA = DATA_ACCEPTED;
    private static final TelegramDataEnum ERROR_TELEGRAM_DATA = DATA_DECLINED;

    private static final Map<String, String> commandButtonsMap = new HashMap<>();

    static {
        commandButtonsMap.put("/resultCommand", "Посмотреть результат");
    }

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.SETTING_SALARIES_QUESTION;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        Chat chat = chatService.findById(TelegramUtils.getChatId(update));
        List<Integer> salaries = chat.getSalaries();

        try {
            addSalaryAnswer(update, chat, salaries);
        } catch (NumberFormatException e) {
            SendMessage sendMessage = getDeclinedSendMessage(update);
            livingWageBot.execute(sendMessage);
        }

        SendMessage sendMessage = getMontSalaryQuestionSendMessage(update, chat);

        if (salaries.size() == 3) {
            sendMessage = getExecutedSendMessage(update);
            chatService.updateChatStatus(chat.getId(), Status.EXECUTED);
        }

        livingWageBot.execute(sendMessage);
    }

    private SendMessage getMontSalaryQuestionSendMessage(Update update, Chat chat) {
        String monthSalaryQuestion = getMonthSalaryQuestion(chat);
        return getSimpleSendMessage(update, monthSalaryQuestion);
    }


    private SendMessage getExecutedSendMessage(Update update) {
        String message = SUCCESS_TELEGRAM_DATA.getTextMessage();
        SendMessage sendMessage = getSimpleSendMessage(update, message);
        sendMessage.setReplyMarkup(getOneLinedKeyboard(commandButtonsMap));

        return sendMessage;
    }

    private SendMessage getDeclinedSendMessage(Update update) {
        String message = ERROR_TELEGRAM_DATA.getTextMessage();

        return getSimpleSendMessage(update, message);
    }

    private void addSalaryAnswer(Update update, Chat chat, List<Integer> salaryAnswers) {
        int salaryAnswer = Integer.parseInt(TelegramUtils.getTextFromUpdate(update));
        salaryAnswers.add(salaryAnswer);
        chat.setSalaries(salaryAnswers);
        chatService.updateChat(chat);
    }

    protected String getMonthSalaryQuestion(Chat chat) {

        String messagePattern = MONTH_SALARY_TELEGRAM_DATA.getTextMessage();
        String parsedMonthAndYear = getParsedMonthAndYear(chat);

        return String.format(messagePattern, parsedMonthAndYear);
    }

    private String getParsedMonthAndYear(Chat chat) {
        int monthsToRoll = 3 - chat.getSalaries().size();
        return DateUtils.getRolledFormattedDate(monthsToRoll);
    }

    protected int getLivingWagesSum(Chat chat) {

        RegionLivingWage regionLivingWage = chat.getRegionLivingWage();
        int employeesSum = chat.getEmployeeCount() * regionLivingWage.getEmployeeLivingWage();
        int childrenSum = chat.getChildCount() * regionLivingWage.getChildLivingWage();
        int retireeSum = chat.getRetireeCount() * regionLivingWage.getRetireeLivingWage();

        return employeesSum + childrenSum + retireeSum;
    }

}
