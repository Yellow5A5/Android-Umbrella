package com.yellow5a5.crashanalysis;

import android.text.TextUtils;

/**
 * Created by Yellow5A5 on 17/4/16.
 */

public class CrashInfoHelper {

    public static void saveInfoLocal(){
        saveInfoLocal("");
    }

    public static void saveInfoLocal(String path){
        //TODO

        if(TextUtils.isEmpty(path)){

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
