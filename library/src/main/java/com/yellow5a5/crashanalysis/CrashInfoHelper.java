package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;

import com.yellow5a5.crashanalysis.config.DefaultUmbrellaConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Yellow5A5 on 17/4/16.
 */

class CrashInfoHelper {

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
            String currentTime = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
            if (!TextUtils.isEmpty(prefixFileName)){
                currentTime = prefixFileName + currentTime;
            }
            File gpxfile = new File(root, currentTime + ".txt");
            FileWriter writer = new FileWriter(gpxfile, true);
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

    public static final String CRASH_CONTENT_TOP_INTERVAL = "<<=======================";
    public static final String CRASH_CONTENT_BOTTOM_INTERVAL = "======================>>";
    public static final String CRASH_CONTENT_SPACE_LEFT_INTERVAL = "   |  ";
    public static final String CRASH_CONTENT_SPACE_RIGHT_INTERVAL = "   |  ";
    public static final String CRASH_CONTENT_ITEM_INTERVAL = "——————————————————————————————————————————————————————————————";

    public static String convertExceptionToStringShow(Throwable throwable) {
        if (throwable == null) {
            return "数据栈为空。";
        }
        return getStackTraceAsString(throwable);
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

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
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
}
