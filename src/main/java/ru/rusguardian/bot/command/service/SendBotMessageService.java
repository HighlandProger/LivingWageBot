package ru.rusguardian.bot.command.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.Status;
import ru.rusguardian.service.data.ChatServiceImpl;
import ru.rusguardian.service.data.exception.EntityNotFoundException;
import ru.rusguardian.util.TelegramUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SendBotMessageService {

    private final Map<CommandName, Command> map;

    @Autowired
    private ChatServiceImpl chatService;

    public SendBotMessageService(List<Command> commands) {
        this.map = commands.stream().collect(Collectors.toMap(Command::getType, Function.identity()));
    }

    public void sendMessage(Update update) {

        log.debug(update.toString());
        String incomeMessage = "";

        if (update.hasMessage()) {
            incomeMessage = update.getMessage().getText();
        }

        if (update.hasCallbackQuery()) {
            incomeMessage = getCallbackQuery(update);
        }

        CommandName commandName = getCommandName(incomeMessage, update);

        Command command = map.get(commandName);

        try {
            command.mainExecute(update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private CommandName getCommandName(String incomeMessage, Update update) {

        Optional<CommandName> commandNameOptional;
        try {
            commandNameOptional = Arrays.stream(CommandName.values()).filter(e -> e.getName().equals(incomeMessage)).findFirst();
            if (commandNameOptional.isEmpty()) {
                Chat chat = chatService.findById(TelegramUtils.getChatId(update));
                if (isStatusSettingRegion(chat)) {
                    return CommandName.REGION_NAME;
                }
                if (isStatusSettingFamily(chat) && update.hasCallbackQuery()) {
                    return CommandName.CHANGE_FAMILY;
                }
                if (isStatusSettingSalaries(chat)) {
                    return CommandName.SETTING_SALARIES_QUESTION;
                }
                if (isStatusWritingMessage(chat)) {
                    return CommandName.CLIENT_MESSAGE;
                }
                if (isStatusAdminSettingAction(chat) && update.hasCallbackQuery()) {
                    if (update.getCallbackQuery().getData().equals("Подтвердить")) {
                        return CommandName.CONFIRM_ACTION;
                    }
                    if (update.getCallbackQuery().getData().equals("В главное меню")) {
                        return CommandName.MAIN_MENU;
                    }
                }
                if (isStatusAdminSettingAction(chat)) {
                    return CommandName.SEND_ACTION;
                }
                if (isStatusAdminSettingAboutUs(chat)) {
                    return CommandName.SEND_ABOUT_US;
                }
                throw new NoSuchElementException();
            }
            return commandNameOptional.get();
        } catch (NoSuchElementException e) {
            log.warn("Command with name {} not found", incomeMessage);
        } catch (EntityNotFoundException e) {
            log.info(e.getMessage());
        }

        return CommandName.NOT_FOUND;
    }

    private boolean isStatusSettingRegion(Chat chat) {
        return chat.getStatus() == Status.SETTING_REGION;
    }

    private boolean isStatusSettingFamily(Chat chat) {
        return chat.getStatus() == Status.SETTING_FAMILY;
    }

    private boolean isStatusSettingSalaries(Chat chat) {
        return chat.getStatus() == Status.SETTING_SALARIES;
    }

    private boolean isStatusWritingMessage(Chat chat) {
        return chat.getStatus() == Status.WRITING_MESSAGE;
    }

    private boolean isStatusAdminSettingAction(Chat chat) {
        return chat.getStatus() == Status.ADMIN_SETTING_ACTION;
    }

    private boolean isStatusAdminSettingAboutUs(Chat chat) {
        return chat.getStatus() == Status.ADMIN_SETTING_ABOUT_US;
    }

    private String getCallbackQuery(Update update) {
        return update.getCallbackQuery().getData();
    }
}

