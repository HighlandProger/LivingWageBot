package ru.rusguardian.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.Status;
import ru.rusguardian.service.data.ChatServiceImpl;
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
                    return CommandName.SETTING_SALARIES;
                }

                throw new NoSuchElementException();
            }
            return commandNameOptional.get();
        } catch (NoSuchElementException e) {
            log.warn("Command with name {} not found", incomeMessage);
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

    private String getCallbackQuery(Update update) {
        String data = update.getCallbackQuery().getData();
        String commandType = data.split("_")[0];
        return switch (commandType) {
            case "Подтвердить" -> "/confirmFamily";
            default -> update.getCallbackQuery().getData();
        };
    }
}

