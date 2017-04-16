package com.yellow5a5.crashanalysis;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
                root = new File(Environment.getExternalStorageDirectory(), "LocalCrashInfo");
            } else {
                root = new File(path);
            }
            if (!root.exists()) {
                root.mkdirs();
            }
            String currentTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File gpxfile = new File(root, currentTime + ".txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            if (callback != null){
                callback.onFailture();
            }
            e.printStackTrace();
        }
        if (callback != null){
            callback.onSuccess();
        }
    }

    public static String convertStackTrace(StackTraceElement[] list){
        if (list == null){
            return "数据栈为空。";
        }
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement element: list) {
            builder.append(element.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
