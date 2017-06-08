package com.yellow5a5.crashanalysis.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.text.TextUtils;

import com.yellow5a5.crashanalysis.Umbrella;
import com.yellow5a5.crashanalysis.config.DefaultUmbrellaConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Yellow5A5 on 17/4/16.
 */

public class CrashInfoHelper {

    public static final String CRASH_CONTENT_TOP_INTERVAL = "<<=======================";
    public static final String CRASH_CONTENT_BOTTOM_INTERVAL = "======================>>";
    public static final String CRASH_CONTENT_SPACE_LEFT_INTERVAL = "   |  ";
    public static final String CRASH_CONTENT_SPACE_RIGHT_INTERVAL = "   |  ";
    public static final String CRASH_CONTENT_ITEM_INTERVAL = "——————————————————————————————————————————————————————————————";

    public static void saveInfoLocal(String path, String prefixFileName,String content, CrashInfoSaveCallBack callback) {
        if (TextUtils.isEmpty(content)) {
            callback.onFailture();
            return;
        }
        try {
            File root = null;
            if (TextUtils.isEmpty(path)) {
                root = new File(Environment.getExternalStorageDirectory(), DefaultUmbrellaConfig.CRASH_INFO_DEFAULT_PATH);
            } else {
                root = new File(path);
            }
            if (!root.exists()) {
                root.mkdirs();
            }
            String currentTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            if (!TextUtils.isEmpty(prefixFileName)){
                currentTime = prefixFileName + currentTime;
            }
            File gpxfile = new File(root, currentTime + ".txt");
            FileWriter writer = new FileWriter(gpxfile, true);
            content = Umbrella.getInstance().encryptString(content);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            if (callback != null) {
                callback.onFailture();
            }
            e.printStackTrace();
        }
        if (callback != null) {
            callback.onSuccess();
        }
    }



    public static String convertExceptionToStringShow(Throwable throwable) {
        if (throwable == null) {
            return "数据栈为空。";
        }
        StackTraceElement[] list = throwable.getStackTrace();
        StringBuilder builder = new StringBuilder();
        builder.append(throwable.toString());
        builder.append("\n");
        for (StackTraceElement element : list) {
            builder.append(element.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public static String convertExceptionToStringSave(Throwable throwable) {
        if (throwable == null) {
            return "数据栈为空。";
        }
        StackTraceElement[] list = throwable.getStackTrace();
        StringBuilder builder = new StringBuilder();
        String currentTime = new SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(Calendar.getInstance().getTime());
        builder.append(CRASH_CONTENT_TOP_INTERVAL);
        builder.append(currentTime);
        builder.append(CRASH_CONTENT_BOTTOM_INTERVAL);
        builder.append("\n");
        builder.append(CRASH_CONTENT_SPACE_LEFT_INTERVAL);
        builder.append(throwable.toString());
        builder.append("\n");
        for (StackTraceElement element : list) {
            builder.append(CRASH_CONTENT_SPACE_LEFT_INTERVAL);
            builder.append(element.toString());
            builder.append("\n");
        }
        builder.append(CRASH_CONTENT_ITEM_INTERVAL);
        builder.append("\n");
        return builder.toString();
    }

    public static String getStackTraceAsString(StackTraceElement[] list) {
//        String trace = "";
//        StringWriter stringWriter = new StringWriter();
//        throwable.printStackTrace(new PrintWriter(stringWriter));
//        return stringWriter.toString();
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement element : list) {
            builder.append(element.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public static ArrayList<String> getTextFromFile(String fileName) {
        String realPath = Umbrella.getInstance().getCrashConfig().getCrashFilePath() + File.separator + fileName;
        ArrayList<String> list = new ArrayList<>();
        String line = null;
        try {
            FileReader fileReader =
                    new FileReader(realPath);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                line = Umbrella.getInstance().decryptString(line);
                list.add(line);
                System.out.println(line);
            }
            bufferedReader.close();
            return list;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static void shareCrashInfo(String content, Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(shareIntent);
    }

    public static Activity getForegroundActivity() {
        try {
            Class activityThreadClass = null;
            activityThreadClass = Class.forName("android.app.ActivityThread");

            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);

            Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
            if (activities == null)
                return null;

            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
