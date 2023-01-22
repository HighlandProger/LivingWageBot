package ru.rusguardian.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.rusguardian.domain.RegionLivingWage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public static List<RegionLivingWage> getRegionsLivingWages2(Document document) {
        List<RegionLivingWage> regionLivingWages = new ArrayList<>();

        Elements table = document.select("table");
        List<Element> rows = table.select("tr").stream().skip(1).toList();

        rows.stream()
                .limit(91)
                .forEach(s -> {
                    Elements cells = s.select("td");
                    regionLivingWages.add(RegionLivingWage.builder()
                                    .id(null)
                                    .regionName(cells.get(0).text())
                                    .humanLivingWage(Integer.parseInt(cells.get(2).text().replaceAll("\\s", "").split(",")[0]))
                                    .employeeLivingWage(Integer.parseInt(cells.get(3).text().replaceAll("\\s", "").split(",")[0]))
                                    .retireeLivingWage(Integer.parseInt(cells.get(4).text().replaceAll("\\s", "").split(",")[0]))
                                    .childLivingWage(Integer.parseInt(cells.get(5).text().replaceAll("\\s", "").split(",")[0]))
                            .build());
                });

        return regionLivingWages.stream().filter(distinctByKey(RegionLivingWage::getRegionName)).toList();

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
