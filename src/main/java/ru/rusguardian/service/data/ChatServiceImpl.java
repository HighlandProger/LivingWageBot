package ru.rusguardian.service.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.Status;
import ru.rusguardian.repository.ChatRepository;
import ru.rusguardian.service.data.exception.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatServiceImpl {

    @Autowired
    private ChatRepository chatRepository;

    private Map<Long, Chat> chats = new HashMap<>();

    //Once in a day 8640000
    @Scheduled(fixedDelay = 8640000, initialDelay = 0)
    private void initDataFromDb() {
        log.info("Updating chats");
        chats = chatRepository.findAll().stream().collect(Collectors.toMap(Chat::getId, Function.identity()));
    }

    public Chat create(Chat chat) {
        chats.put(chat.getId(), chat);
        chatRepository.save(chat);
        return chat;
    }

    public Chat findById(Long chatId) {
        log.debug("Searching for chat with telegram id = {}", chatId);
        Optional<Chat> entity;
        try {
            entity = Optional.of(chats.get(chatId));
        } catch (NullPointerException e) {
            log.info("Chat with id {} not found in map", chatId);
            throw new EntityNotFoundException(e.getMessage());
        }
        return entity.get();
    }

    public void updateChatStatus(Long chatId, Status status) {
        log.debug("Updating chat with id = {} to status {}", chatId, status.name());
        Chat chat = findById(chatId);
        chat.setStatus(status);
        chatRepository.updateUserStatus(chatId, status.name());
    }

    public void updateChat(Chat chat) {
        log.debug("Updating chat {}", chat);
        chatRepository.save(chat);
        chats.put(chat.getId(), chat);
    }

    public List<Chat> getAll() {
        return (List<Chat>) chats.values();
    }

}
