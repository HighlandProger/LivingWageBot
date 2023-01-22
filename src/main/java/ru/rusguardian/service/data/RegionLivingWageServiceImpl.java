package ru.rusguardian.service.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.repository.RegionLivingWageRepository;

import javax.annotation.PostConstruct;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RegionLivingWageServiceImpl {

    private final List<RegionLivingWage> regionLivingWages = new ArrayList<>();
    @Autowired
    RegionLivingWageRepository regionLivingWageRepository;

    @PostConstruct
    private void initData() {
        regionLivingWages.addAll(regionLivingWageRepository.findAll());
    }

    public List<RegionLivingWage> getRegionLivingWages() {
        return regionLivingWages;
    }

    public RegionLivingWage getRegionLivingWageByName(String regionName) throws NoSuchObjectException {
        Optional<RegionLivingWage> regionLivingWageOptional = regionLivingWages.stream()
                .filter(s -> s.getRegionName().equals(regionName)).findFirst();
        if (regionLivingWageOptional.isEmpty()) {
            log.info("Region with name {} is not present", regionName);
            throw new NoSuchObjectException(regionName);
        }
        return regionLivingWageOptional.get();
    }

    public List<RegionLivingWage> saveAll(List<RegionLivingWage> regionLivingWages) {
        return regionLivingWageRepository.saveAll(regionLivingWages);
    }
}
