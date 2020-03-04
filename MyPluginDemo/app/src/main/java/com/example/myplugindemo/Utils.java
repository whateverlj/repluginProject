/*******************************************************************************
 *
 * Copyright (c) Weaver Info Tech Co. Ltd
 *
 * Utils
 *
 * app.ui.Utils.java
 * TODO: File description or class description.
 *
 * @author: gao_chun
 * @since:  2014-11-6
 * @version: 1.0.0
 *
 * @changeLogs:
 *     1.0.0: First created this class.
 *
 ******************************************************************************/
package com.example.myplugindemo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * @author gao_chun
 *
 */
public class Utils {

    private final static String WORKPLACE_SDCARD_PATH="/MY_PHOTO/";

    /**
     * 产生一个随机的字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        //String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 1、判断SD卡是否存在
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**创建目录  */
    public static void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static String genProjectPath(){
        // 如果文件夹不存在则创建文件夹，并将bitmap图像文件保存
        File rootdir = Environment.getExternalStorageDirectory();
        String filePath = rootdir.getPath() + WORKPLACE_SDCARD_PATH;
        createPath(filePath);

        return filePath;
    }


    /**
     * Save image to the SD card
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap,String path,String photoName){
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
            }

            File photoFile = new File(path , photoName + ".jpg");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    compressBitmap(photoBitmap,photoFile);
//                    if (compressBitmap(photoBitmap).compress(Bitmap.CompressFormat.JPEG, 60, fileOutputStream)) {
//                        fileOutputStream.flush();
                    }
                fileOutputStream.flush();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally{
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 质量压缩
     *
     * @param image
     * @return
     * @author ping 2015-1-5 下午1:29:58
     */
    public static void  compressBitmap(Bitmap image,File photoFile) {
        //L.showlog(压缩图片);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;

        //由于数果手机像素差,这边采取不压缩
        while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 1;// 每次都减少10
        }

            try {
                FileOutputStream fos = new FileOutputStream(photoFile);
                try {
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
//
//        try {
//            isBm.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    /**
     * Check the SD card
     * @return
     */
    public static boolean checkSDCardAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
