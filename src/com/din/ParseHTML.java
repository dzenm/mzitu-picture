package com.din;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseHTML {

    /**
     * 链接到网址获取数据
     *
     * @param URL
     * @param cssQuery
     * @param query
     * @return
     */
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

    /**
     * 解析HTML页面
     * @param URL
     */
    public static void parseDayUpdateHTMLData(String URL) {
        Elements elements = getElements(URL, "div.all", "ul.archives");
        for (int i = 0; i < elements.size(); i++) {
            Elements nextElements = elements.get(i).select("a");
            addList(nextElements);
        }
    }

    /**
     * 添加到List
     *
     * @param nextElements
     */
    private static void addList(Elements nextElements) {
        for (int j = 0; j < nextElements.size(); j++) {
            String title = nextElements.get(j).text().toString();
            String url = nextElements.get(j).attr("href").toString();
            parseContentHTMLData(title, url);
        }
    }
    /**
     * 单张图片显示
     * @param title
     * @param URL
     */
    public static void parseContentHTMLData(String title, String URL) {
        Elements elements = getElements(URL, "div.pagenavi", "span");     // 分析网页的内容
        if (elements.size() >= 2) {
            int pages = getPosition(elements, 2);
            for (int i = 1; i <= pages; i++) {
                try {
                    Document pageDocument = Jsoup.connect(URL + "/" + i).get();
                    Element element = pageDocument.select("div.main-image").select("img").first();
                    getElements(URL, "div.main-image", "span");
                    String url = element.attr("src").toString();
                    HttpUtil.request(url, title);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从第一个页面获取该系列的网页总数
     *
     * @param elements
     * @param last
     * @return
     */
    private static int getPosition(Elements elements, int last) {
        int pages = Integer.valueOf(elements.get(elements.size() - last).text());
        return pages;
    }
}