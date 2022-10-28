package ru.rusguardian.bot.command.main.start.contacts.write_question;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.Status;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

import static ru.rusguardian.domain.TelegramDataEnum.CLIENT_MESSAGE;
import static ru.rusguardian.domain.TelegramDataEnum.OWNER_MESSAGE;

@Component
@Slf4j
public class ClientMessageCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = CLIENT_MESSAGE;
    private static final TelegramDataEnum TELEGRAM_DATA2 = OWNER_MESSAGE;

    @Value("${telegram.owner.chat.id}")
    private String ownerChatId;

    @Value("${telegram.owner.chat.id.2}")
    private String ownerChatId2;

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.CLIENT_MESSAGE;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        chatService.updateChatStatus(TelegramUtils.getChatId(update), Status.EXECUTED);
        SendMessage clientSendMessage = getSendMessageByTelegramData(update, TELEGRAM_DATA);

        noticeOwners(update);

        livingWageBot.execute(clientSendMessage);
    }

    private void noticeOwners(Update update) throws TelegramApiException {
        SendMessage ownerSendMessage = getOwnerSendMessage(update, ownerChatId);
        SendMessage ownerSendMessage2 = getOwnerSendMessage(update, ownerChatId2);

        log.info("Notice owner with chatId: {}", ownerChatId);
        livingWageBot.execute(ownerSendMessage);
        log.info("Notice owner with chatId: {}", ownerChatId2);
        livingWageBot.execute(ownerSendMessage2);
    }

    private SendMessage getOwnerSendMessage(Update update, String ownerChatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(ownerChatId);
        sendMessage.setText(getMessageToOwner(update));

        return sendMessage;
    }

    private String getMessageToOwner(Update update) {
        String messagePattern = TELEGRAM_DATA2.getTextMessage();

        String client = update.getMessage().getFrom().getUserName();
        String clientMessage = TelegramUtils.getTextFromUpdate(update);

        return String.format(messagePattern, client, clientMessage);
    }
}
