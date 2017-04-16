package com.yellow5a5.crashanalysis;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Yellow5A5 on 17/4/16.
 */

public class CrashInfoHelper {

    public static void saveInfoLocal(String content, CrashInfoSaveCallBack callback){
        saveInfoLocal("", content, callback);
    }

    public static void saveInfoLocal(String path, String content, CrashInfoSaveCallBack callback){
        if (TextUtils.isEmpty(content)){
            callback.onFailture();
            return;
        }
        try {
            File root = null;
            if (TextUtils.isEmpty(path)){
                root = new File(Environment.getExternalStorageDirectory(), "localCrashInfo");
            } else {
                root = new File(path);
            }
            if (!root.exists()) {
                root.mkdirs();
            }
            String currentTime = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
            File gpxfile = new File(root, currentTime + ".txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            if (callback != null){
                callback.onFailture();
            }
            e.printStackTrace();
        }
        if (callback != null){
            callback.onSuccess();
        }
    }

    public static final String CRASH_CONTENT_TOP_INTERVAL = "<<=======================";
    public static final String CRASH_CONTENT_BOTTOM_INTERVAL = "======================>>";
    public static final String CRASH_CONTENT_SPACE_LEFT_INTERVAL = "   |  ";
    public static final String CRASH_CONTENT_SPACE_RIGHT_INTERVAL = "   |  ";
    public static final String CRASH_CONTENT_ITEM_INTERVAL = "——————————————————————————————————————————————————————————————";

    public static String convertStackTraceToShow(StackTraceElement[] list){
        if (list == null){
            return "数据栈为空。";
        }
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement element: list) {
            builder.append(element.toString());
            builder.append("\n");
        }
        builder.append("\n");
        return builder.toString();
    }

    public static String convertStackTraceToSave(StackTraceElement[] list){
        if (list == null){
            return "数据栈为空。";
        }
        StringBuilder builder = new StringBuilder();
        String currentTime = new SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(Calendar.getInstance().getTime());
        builder.append(CRASH_CONTENT_TOP_INTERVAL);
        builder.append(currentTime);
        builder.append(CRASH_CONTENT_BOTTOM_INTERVAL);
        builder.append("\n");
        for (StackTraceElement element: list) {
            builder.append(CRASH_CONTENT_SPACE_LEFT_INTERVAL);
            builder.append(element.toString());
            builder.append("\n");
        }
        builder.append(CRASH_CONTENT_ITEM_INTERVAL);
        builder.append("\n");
        return builder.toString();
    }
}
