# [Mzitu.com](http://www.mzitu.com/)

### Java爬虫

#### 一、分析网站结构得知，每日更新页面存在所有图片链接，遍历年份节点，取得日期下的图片页面所在的链接
```
    public static void parseDayUpdateHTMLData(String URL) {
        Elements elements = getElements(URL, "div.all", "ul.archives");
        System.out.println("获取的总年份数据为:" + elements.size());
        for (int i = 0; i < elements.size(); i++) {
            System.out.println("正在获取第 " + count + " 年的数据");
            Elements nextElements = elements.get(i).select("a");
            addList(nextElements);
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
            parseContentHTMLData(title, url);
            System.out.println("第 " + j + " 条数据图片保存完成...");
        }
    }
```

#### 二、图片是分页显示的，一页显示一张图片，在进入图片链接之后，获取底部的页数显示作为该帖子下图片的个数
```
    public static void parseContentHTMLData(String title, String URL) {
        Elements elements = getElements(URL, "div.pagenavi", "span");
        // 分析网页的内容
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

    private static int getPosition(Elements elements, int last) {
        int pages = Integer.valueOf(elements.get(elements.size() - last).text());
        return pages;
    }
```
