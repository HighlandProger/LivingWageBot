package ru.rusguardian.service.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GetLivingWageHtmlService {

    public Document getHtmlDoc() {
        Document document = null;
        try {
            document = Jsoup.connect("http://potrebkor.ru/prozhitochnyi-minimum.html").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return document;
    }

}
