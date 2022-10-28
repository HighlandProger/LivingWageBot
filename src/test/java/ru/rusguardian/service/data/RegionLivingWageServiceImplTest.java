package ru.rusguardian.service.data;

import org.junit.jupiter.api.Test;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegionLivingWageServiceImplTest {

    private static final String DATA_FILE_PATH = "/data/region_living_wage_data.txt";
    private final File dataFile = FileUtils.getFileFromResources(DATA_FILE_PATH);
    private final List<RegionLivingWage> regionLivingWages = new ArrayList<>();

    @Test
    void initData() {
        int regionsCount = 85;
        String[] lines = FileUtils.getTextFromFile(dataFile).split("\n");

        for (String line : lines) {
            String[] params = line.split(",");
            RegionLivingWage regionLivingWage = new RegionLivingWage();
            regionLivingWage.setRegionName(params[0]);
            regionLivingWage.setEmployeeLivingWage(Integer.parseInt(params[1]));
            regionLivingWage.setRetireeLivingWage(Integer.parseInt(params[2]));
            regionLivingWage.setChildLivingWage(Integer.parseInt(params[3]));

            regionLivingWages.add(regionLivingWage);
        }

        assertEquals(regionLivingWages.size(), regionsCount);
    }
}