package com.din;

public class Main {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始解析数据...");
                ParseHTML.parseDayUpdateHTMLData("http://www.mzitu.com/all/");
            }
        }).start();
        System.out.println("线程开始启动...");
    }
}