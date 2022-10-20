package ru.rusguardian.service.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.domain.TelegramData;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppDataService {

    private List<RegionLivingWage> regionLivingWages = new ArrayList<>();
    private List<TelegramData> telegramData = new ArrayList<>();

    @Autowired
    private RegionLivingWageServiceImpl regionLivingWageService;

    @Autowired
    private TelegramDataServiceImpl telegramDataService;

    @PostConstruct
    private void initData() {
        this.regionLivingWages = regionLivingWageService.getAll();
        this.telegramData = telegramDataService.getAll();
    }

    public Optional<RegionLivingWage> getRegionLivingWageByName(String regionName) {
        return regionLivingWages.stream().filter(s -> s.getRegionName().equals(regionName)).findFirst();
    }

    public Optional<TelegramData> getTelegramDataByName(String name) {
        return telegramData.stream().filter(s -> s.getName().equals(name)).findFirst();
    }

    public List<RegionLivingWage> getRegionLivingWages() {
        return this.regionLivingWages;
    }

    public List<TelegramData> getTelegramData() {
        return this.telegramData;
    }
}
