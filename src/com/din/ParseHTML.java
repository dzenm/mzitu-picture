package com.din;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParseHTML {

    private static int count = 0;

    /**
     * 链接到网址获取数据
     *
     * @param baseUrl
     * @param cssQuery
     * @param query
     * @return
     */
    private static Elements getElements(String baseUrl, String cssQuery, String query) {
        try {
            Document document = Jsoup.connect(baseUrl).get();       // 通过Jsoup链接到一个网址，获取到整个document
            Elements elements = document.select(cssQuery).select(query);     // 分析网页的内容
            return elements;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析HTML页面
     *
     * @param baseUrl
     */
    public static void parseDayUpdateHTMLData(String baseUrl) {
        Elements elements = getElements(baseUrl, "div.all", "ul.archives");
        System.out.println("获取的总年份数据为:" + elements.size());
        for (int i = 0; i < elements.size(); i++) {
            System.out.println("正在获取第 " + count + " 年的数据");
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
        System.out.println("获取的该年的总数据为：" + nextElements.size());
        try {
            System.out.println("即将开始下载图片...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < nextElements.size(); j++) {
            System.out.println("获取第 " + j + " 条的数据成功");
            String title = nextElements.get(j).text().toString();
            String url = nextElements.get(j).attr("href").toString();
            System.out.println("正在下载 " + j + " 条的数据...");
            System.out.println("标题为: " + title);
            parseContentHTMLData(title, url);
            System.out.println("第 " + j + " 条数据图片保存完成...");
        }
    }

    /**
     * 单张图片显示
     *
     * @param title
     * @param baseUrl
     */
    public static void parseContentHTMLData(String title, String baseUrl) {
        Elements elements = getElements(baseUrl, "div.pagenavi", "span");
        // 分析网页的内容
        if (elements.size() >= 2) {
            int pages = getPosition(elements, 2);
            for (int i = 1; i <= pages; i++) {
                try {
                    Document pageDocument = Jsoup.connect(baseUrl + "/" + i).get();
                    String url = pageDocument.select("div.main-image").select("img").attr("src");
                    if (url.contains("\n") || url.contains("</p>")) {
                        continue;
                    }
                    HttpUtil.request(url, title);
                    System.out.println("图片网址: " + url);
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
