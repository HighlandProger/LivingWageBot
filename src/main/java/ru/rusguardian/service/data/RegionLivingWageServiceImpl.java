package ru.rusguardian.service.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.repository.RegionLivingWageRepository;

import java.util.List;

@Service
@Slf4j
public class RegionLivingWageServiceImpl extends CrudService<RegionLivingWage> {

    @Autowired
    RegionLivingWageRepository regionLivingWageRepository;

    public List<RegionLivingWage> saveAll(List<RegionLivingWage> regionLivingWages) {
        return regionLivingWageRepository.saveAll(regionLivingWages);
    }
}
