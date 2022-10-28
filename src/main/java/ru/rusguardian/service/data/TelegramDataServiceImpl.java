package ru.rusguardian.service.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rusguardian.domain.TelegramDataDto;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.repository.TelegramDataDtoRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class TelegramDataServiceImpl {

    private final List<TelegramDataDto> telegramDataList = new ArrayList<>();
    @Autowired
    TelegramDataDtoRepository telegramDataRepository;

    @PostConstruct
    private void initData() {
        telegramDataList.addAll(telegramDataRepository.findAll());
    }

    public TelegramDataDto getTelegramDataByName(String name) {
        Optional<TelegramDataDto> telegramDataOptional = telegramDataList.stream().filter(s -> s.getName().equals(name)).findFirst();
        if (telegramDataOptional.isEmpty()) {
            log.error("Telegram data with name {} not found", name);
            throw new NoSuchElementException();
        }
        return telegramDataOptional.get();
    }

    public void updateTelegramData(TelegramDataDto telegramData) {
        TelegramDataEnum telegramDataEnum = TelegramDataEnum.valueOf(telegramData.getName());
        telegramDataEnum.setByTelegramDataDto(telegramData);

        telegramDataRepository.update(
                telegramData.getName(),
                telegramData.getTextMessage(),
                telegramData.getPhotoId(),
                telegramData.getStickerId(),
                telegramData.getVideoId());
    }

    public List<TelegramDataDto> getTelegramData() {
        return this.telegramDataList;
    }

}
