package com.din;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    public static void request(String imageUrl) {
        URL url = null;         // 创建一个URL对象
        BufferedReader reader = null;
        try {
            url = new URL(imageUrl);
            // 通过URL对象提出的openConnection方法创建URLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方式为"GET"
            connection.setRequestMethod("GET");
            // 超时响应时间为5秒
            connection.setConnectTimeout(5000);
            // 设置Referer，防止403请求被拒
            connection.setRequestProperty("Referer", "http://www.mzitu.com/all/");
            // 通过输入流获取图片数据
            InputStream stream = connection.getInputStream();
            // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(stream);
            // new一个文件对象用来保存图片，默认保存当前工程根目录
            PhotoUtil.savePicture(data);
            // 调用URLConnection对象提供的connect方法连接远程服务
            connection.connect();

            // 连接服务器后，就可以查询头部信息了
            Map<String, List<String>> map = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                StringBuilder builder = new StringBuilder();
                int size = values == null ? 0 : values.size();
                for (int i = 0; i < size; i++) {
                    if (i > 0) {
                        builder.append(",");
                    }
                    builder.append(values.get(i));
                }
                System.out.println(key + ": " + builder.toString());
            }
            System.out.println("--------------------------");

//            // 获取输入流，从中读取资源数据
//            InputStream inputStream = connection.getInputStream();
//            InputStreamReader streamReader = new InputStreamReader(inputStream);
//            reader = new BufferedReader(streamReader);
//			  // 遍历得到的数据
//            for (String line = null; (line = reader.readLine()) != null; ) {
//                System.out.println(line);
//            }
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

    public static void request(String imageUrl, String name) {
        URL url = null;         // 创建一个URL对象
        BufferedReader reader = null;
        try {
            url = new URL(imageUrl);
            // 通过URL对象提出的openConnection方法创建URLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方式为"GET"
            connection.setRequestMethod("GET");
            // 超时响应时间为5秒
            connection.setConnectTimeout(5000);
            // 通过输入流获取图片数据
            InputStream stream = connection.getInputStream();
            // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(stream);
            // new一个文件对象用来保存图片，默认保存当前工程根目录
            PhotoUtil.savePicture(name, data);
            // 调用URLConnection对象提供的connect方法连接远程服务
            connection.connect();

            // 连接服务器后，就可以查询头部信息了
            Map<String, List<String>> map = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                StringBuilder builder = new StringBuilder();
                int size = values == null ? 0 : values.size();
                for (int i = 0; i < size; i++) {
                    if (i > 0) {
                        builder.append(",");
                    }
                    builder.append(values.get(i));
                }
                System.out.println(key + ": " + builder.toString());
            }
            System.out.println("--------------------------");

//            // 获取输入流，从中读取资源数据
//            InputStream inputStream = connection.getInputStream();
//            InputStreamReader streamReader = new InputStreamReader(inputStream);
//            reader = new BufferedReader(streamReader);
//			  // 遍历得到的数据
//            for (String line = null; (line = reader.readLine()) != null; ) {
//                System.out.println(line);
//            }
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
}