package com.din;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParseHTML {

    public void parse() {
        try {
            Document document = Jsoup.connect("").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseContentHTMLData(String URL) {
        Elements elements = getElements(URL, "div.pagenavi", "span");     // 分析网页的内容
        if (elements.size() >= 2) {
            int pages = getPosition(elements, 2);
            for (int i = 1; i <= pages; i++) {
                try {
                    Document pageDocument = Jsoup.connect(URL + "/" + i).get();
                    Element element = pageDocument.select("div.main-image").select("img").first();
                    getElements(URL, "div.main-image", "span");
                    Elements information = pageDocument.select("div.main-meta").select("span");
                    String tyle = information.get(0).text() + "   ";
                    String time = information.get(1).text() + "   ";
                    String count = information.get(2).text() + "   ";
                    String url = element.attr("src").toString();
                    try {
                        Thread.sleep(2000);
                        HttpUtil.request(url);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Elements getElements(String URL, String cssQuery, String query) {
        try {
            Document document = Jsoup.connect(URL).get();       // 通过Jsoup链接到一个网址，获取到整个document
            Elements elements = document.select(cssQuery).select(query);     // 分析网页的内容
            return elements;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getPosition(Elements elements, int last) {
        int pages = Integer.valueOf(elements.get(elements.size() - last).text());
        return pages;
    }
}