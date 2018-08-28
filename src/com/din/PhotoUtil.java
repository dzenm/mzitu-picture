package com.din;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoUtil {

    public static void savePicture(byte[] data) {
        long name = System.currentTimeMillis();     // 时间戳作为图片名字
        String direct = createDir("picture");       // 图片存在单独的一个文件夹下
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

    public static void savePicture(String name, byte[] data) {
        String direct = createDir("picture");       // 图片存在单独的一个文件夹下
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

    /**
     * 创建文件夹
     * @param dir
     * @return
     */
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
        direct.mkdir();
        return dir;
    }
}