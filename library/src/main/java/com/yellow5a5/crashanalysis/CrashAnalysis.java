package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.os.Looper;
import android.widget.Toast;

import com.yellow5a5.crashanalysis.View.CrashDisplayDialog;

import java.util.LinkedList;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

public class CrashAnalysis implements Thread.UncaughtExceptionHandler {

    private static class InstanceHolder {
        private static CrashAnalysis instance = new CrashAnalysis();
    }
    public static CrashAnalysis getInstance() {
        return InstanceHolder.instance;
    }

    private CrashExceptionHandler mCrashHandler;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private LinkedList<Activity> mActvityList = new LinkedList<>();

    public void init(){
        if (mCrashHandler == null){
            Looper.getMainLooper().getThread().setUncaughtExceptionHandler(this);
        }
    }

    public void registeredActivity(Activity act){
        mActvityList.add(act);
    }

    public void unRegisterActivity(Activity act){
        mActvityList.remove(act);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (mDefaultHandler != null){
            mDefaultHandler.uncaughtException(t,e);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                //TODO 存储Crash信息。


                if (mActvityList.getLast() == null){
                    return;
                }
                CrashDisplayDialog dialog = new CrashDisplayDialog(mActvityList.getLast());

                dialog.show();

                Looper.loop();
            }
        }).start();

    }



}
