package ru.rusguardian.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.service.data.RegionLivingWageServiceImpl;
import ru.rusguardian.service.web.GetLivingWageHtmlService;
import ru.rusguardian.util.HtmlUtils;

import java.util.List;

@Service
@Slf4j
public class WebToDataServiceImpl {

    @Autowired
    private GetLivingWageHtmlService htmlService;

    @Autowired
    private RegionLivingWageServiceImpl dataService;

    //    @Scheduled(fixedDelay = 600000, initialDelay = 1)
    public void updateData() {
        log.info("Updating data");
        Document document = htmlService.getHtmlDoc();
        List<RegionLivingWage> regionLivingWages = HtmlUtils.getRegionsLivingWages(document);
        dataService.saveAll(regionLivingWages);
    }
}
