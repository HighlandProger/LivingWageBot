package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.domain.Status;
import ru.rusguardian.util.DateUtils;
import ru.rusguardian.util.TelegramUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class _6_SettingSalariesCommand extends Command {

    private static final String MONTH_SALARY_QUESTION_TELEGRAM_DATA = "MONTH_SALARY_QUESTION";
    private static final String DATA_ACCEPTED_TELEGRAM_DATA = "DATA_ACCEPTED";
    private static final String DATA_DECLINED_TELEGRAM_DATA = "DATA_DECLINED";

    private static final Map<String, String> commandButtonsMap = new HashMap<>();

    static {
        commandButtonsMap.put("/resultCommand", "Посмотреть результат");
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
        } catch (NumberFormatException e){
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
        String message = telegramDataService.getTelegramDataByName(DATA_ACCEPTED_TELEGRAM_DATA).getTextMessage();
        SendMessage sendMessage = getSimpleSendMessage(update, message);
        sendMessage.setReplyMarkup(getInlineKeyboard(commandButtonsMap));

        return sendMessage;
    }

    private SendMessage getDeclinedSendMessage(Update update){
        String message = telegramDataService.getTelegramDataByName(DATA_DECLINED_TELEGRAM_DATA).getTextMessage();

        return getSimpleSendMessage(update, message);
    }

    private void addSalaryAnswer(Update update, Chat chat, List<Integer> salaryAnswers) {
        int salaryAnswer = Integer.parseInt(TelegramUtils.getTextFromUpdate(update));
        salaryAnswers.add(salaryAnswer);
        chat.setSalaries(salaryAnswers);
        chatService.updateChat(chat);
    }

    protected String getMonthSalaryQuestion(Chat chat) {

        String messagePattern = telegramDataService.getTelegramDataByName(MONTH_SALARY_QUESTION_TELEGRAM_DATA).getTextMessage();
        String parsedMonthAndYear = getParsedMonthAndYear(chat);

        return String.format(messagePattern, parsedMonthAndYear);
    }

    private String getParsedMonthAndYear(Chat chat) {
        List<Integer> clientSalaries = chat.getSalaries();
        int monthsToRoll = 3 - clientSalaries.size();
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
