package ru.rusguardian.service.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.Status;
import ru.rusguardian.repository.ChatRepository;
import ru.rusguardian.service.data.exception.EntityNotFoundException;

import java.util.Optional;

@Service
@Slf4j
public class ChatServiceImpl extends CrudService<Chat> {

    @Autowired
    ChatRepository chatRepository;

    @Override
    public Chat findById(Long chatId) {
        log.debug("Searching for chat with telegram id = {}", chatId);
        Optional<Chat> entity = chatRepository.findById(chatId);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Chat with telegram id = " + chatId + " not exists");
        }
        return entity.get();
    }

    public void updateChatStatus(Long chatId, Status status) {
        log.debug("Updating chat with id = {} to status {}", chatId, status.name());
        chatRepository.updateUserStatus(chatId, status.name());
    }

    public void updateChat(Chat chat) {
        log.debug("Updating chat {}", chat);
        chatRepository.save(chat);
    }
}
