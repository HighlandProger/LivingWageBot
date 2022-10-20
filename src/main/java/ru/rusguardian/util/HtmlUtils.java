package ru.rusguardian.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.rusguardian.domain.RegionLivingWage;

import java.util.ArrayList;
import java.util.List;

public class HtmlUtils {

    private HtmlUtils() {
    }

    public static List<RegionLivingWage> getRegionsLivingWages(Document document) {
        List<RegionLivingWage> regionLivingWages = new ArrayList<>();

        Elements table = document.select("table");
        List<Element> rows = table.select("tr").stream().skip(1).toList();

        for (Element row : rows) {
            Elements cells = row.select("td");
            regionLivingWages.add(RegionLivingWage.builder()
                    .regionName(cells.get(0).text())
                    .humanLivingWage(Integer.parseInt(cells.get(1).text().replaceAll("\\s", "")))
                    .employeeLivingWage(Integer.parseInt(cells.get(2).text().replaceAll("\\s", "")))
                    .retireeLivingWage(Integer.parseInt(cells.get(3).text().replaceAll("\\s", "")))
                    .childLivingWage(Integer.parseInt(cells.get(4).text().replaceAll("\\s", "")))
                    .build());
        }
        return regionLivingWages;
    }
}
