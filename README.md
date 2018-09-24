# 用Java 爬 Mzitu.com


<br /><br />

## 首先需要导入Jsoup，这是 [Jsoup下载地址](https://jsoup.org/download)

<br />

## &emsp;&emsp;一、打开 [网址](http://www.mzitu.com/)，按F12(Chrome) 或点击右上角的 菜单/更多工具/开发者工具。打开后是HTML页面，然后分析网站结构得知，[每日更新](http://www.mzitu.com/all/) 是所有更新的图片帖子链接。可以遍历年份节点，取得日期下的图片页面所在的链接---(获取所有图片帖子的链接和标题)

<br />

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

<br />

## &emsp;&emsp;二、图片是分页显示的，一页显示一张图片，在进入图片链接之后，可以获取底部的页数知道图片的数据---(获取每条帖子下的所有图片url)

<br />

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

#### 有些页面会出现url链接错误，然后jsoup就会解析失败，抛出异常。判断获取的url包含换行字段来判断是否存在url异常

```
    if (url.contains("\n") || url.contains("</p>")) {
        continue;
    }
```

<br />

## &emsp;&emsp;三、下载图片，创建一个URL对象，设置请求的参数，将输入流转化为字节流

<br />

```
    public static void request(String imageUrl, String name) {
        URL url = null;         // 创建一个URL对象
        BufferedReader reader = null;
        try {
            url = new URL(imageUrl);
            // 通过URL对象提出的openConnection方法创建URLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方式为"GET"
            connection.setRequestMethod("GET");
            // 设置Referer，防止403请求被拒
            connection.setRequestProperty("Referer", "http://www.mzitu.com/all/");
            // 超时响应时间为60秒
            connection.setConnectTimeout(60000);
            // 通过输入流获取图片数据
            InputStream stream = connection.getInputStream();
            // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(stream);
            // new一个文件对象用来保存图片，默认保存当前工程根目录
            PhotoUtil.savePicture(name, data);
            // 调用URLConnection对象提供的connect方法连接远程服务
            connection.connect();
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] readInputStream (InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        int len = 0;
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕，使用一个输入流从buffer里把数据读取出来
        while ((len = inputStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outputStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inputStream.close();
        // 把outStream里的数据写入内存
        return outputStream.toByteArray();
    }
```

<br />

* 跨域问题，需要设置请求头，否则会出现403访问被拒
> 设置Referer，防止403请求被拒
> connection.setRequestProperty("Referer", "http://www.mzitu.com/all/");  

<br />  

## &emsp;&emsp;四、保存图片，根据图片所在的帖子标题创建新的文件夹，如果文件夹存在，返回文件的相对路径，创建输出流将字节流转化为图片文件

<br />    

```
    public static void savePicture(String dir, byte[] data) {
        long name = System.currentTimeMillis();     // 时间戳作为图片名字
        String direct = createDir("picture/" + dir);       // 图片存在单独的一个文件夹下
        File file = new File(direct + "/" + name + ".png");
        FileOutputStream outputStream = null;
        try {
            // 创建新文件
            file.createNewFile();
            // 创建输出流
            outputStream = new FileOutputStream(file);
            if (outputStream != null) {
                // 写入数据
                outputStream.write(data);
                // 关闭输出流
                outputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String createDir(String dir) {
        File direct = new File(dir);
        if (direct.exists()) {
            try {
                // 文件夹存在就获取该文件夹的相对路径
                return direct.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        direct.mkdirs();
        return dir;
    }
 ```  
 
<br /><br />
该网址有4000多条数据，12G的图片
[image](https://github.com/freedomeden/mzitu-picture/blob/master/picture.png)
<br /><br />

