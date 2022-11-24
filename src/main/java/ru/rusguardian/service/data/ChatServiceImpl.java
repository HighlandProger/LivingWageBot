package ru.rusguardian.service.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.Status;
import ru.rusguardian.repository.ChatRepository;
import ru.rusguardian.service.data.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChatServiceImpl {

    @Autowired
    private ChatRepository chatRepository;

    private List<Chat> chats = new ArrayList<>();

    //Once in a day 8640000
    @Scheduled(fixedDelay = 8640000, initialDelay = 0)
    private void initDataFromDb() {
        log.info("Updating chats");
        chats = chatRepository.findAll();
    }

    public Chat create(Chat chat) {
        chats.add(chat);
        chatRepository.save(chat);
        return chat;
    }

    public Chat findById(Long chatId) {
        log.debug("Searching for chat with telegram id = {}", chatId);
        Optional<Chat> entity = chats.stream().filter(s -> s.getId().equals(chatId)).findFirst();
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Chat with telegram id = " + chatId + " not exists");
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
        Chat oldChat = findById(chat.getId());
        chatRepository.save(chat);
        chats.remove(oldChat);
        chats.add(chat);
    }

    public List<Chat> getAll() {
        return chats;
    }

}
