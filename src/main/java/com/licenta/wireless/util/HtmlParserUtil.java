package com.licenta.wireless.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlParserUtil {

    public static void parseHtml(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Document doc = Jsoup.parse(content);



            //  extragerea titlurilor din document
            Elements titles = doc.select("title");
            for (Element title : titles) {
                System.out.println("Titlu: " + title.text());
            }



            System.out.println("Procesare fișier HTML completată.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

