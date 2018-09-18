package com.din;

public class Main {

    public static void main(String[] args) {
        thread.start();
        System.out.println("线程开始启动...");
    }

    private static Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            ParseHTML.parseDayUpdateHTMLData("http://www.mzitu.com/all/");
        }
    });
}
