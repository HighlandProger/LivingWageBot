package ru.rusguardian.service.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.util.FileUtils;

import javax.annotation.PostConstruct;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RegionLivingWageServiceImpl {

    private static final String DATA_FILE_PATH = "/data/region_living_wage_data.txt";
    private final List<RegionLivingWage> regionLivingWages = new ArrayList<>();

    @PostConstruct
    private void initData() {
        String[] lines = FileUtils.getTextFromResourcesFile(DATA_FILE_PATH).split("\n");

        for (String line : lines) {
            String[] params = line.split(",");
            RegionLivingWage regionLivingWage = new RegionLivingWage();
            regionLivingWage.setRegionName(params[0]);
            regionLivingWage.setEmployeeLivingWage(Integer.parseInt(params[1]));
            regionLivingWage.setRetireeLivingWage(Integer.parseInt(params[2]));
            regionLivingWage.setChildLivingWage(Integer.parseInt(params[3]));

            regionLivingWages.add(regionLivingWage);
        }
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
}
