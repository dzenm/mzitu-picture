package com.din;

public class Main {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ParseHTML.parseDayUpdateHTMLData("http://www.mzitu.com/all/");
            }
        }).start();
    }
}