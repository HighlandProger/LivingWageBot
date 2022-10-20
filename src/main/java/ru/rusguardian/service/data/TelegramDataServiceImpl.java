package ru.rusguardian.service.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rusguardian.domain.TelegramData;
import ru.rusguardian.repository.TelegramDataRepository;
import ru.rusguardian.service.data.exception.EntityNotFoundException;

import java.util.Optional;

@Service
@Slf4j
public class TelegramDataServiceImpl extends CrudService<TelegramData> {

    @Autowired
    TelegramDataRepository telegramDataRepository;

    public TelegramData findByName(String name) {
        log.debug("Searching for telegram data with name = {}", name);
        Optional<TelegramData> entity = telegramDataRepository.findByName(name);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Telegram data with name = " + name + " not exists");
        }
        return entity.get();
    }
}
