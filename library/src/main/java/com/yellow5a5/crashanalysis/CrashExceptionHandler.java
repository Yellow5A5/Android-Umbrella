package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.os.Looper;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread mTargetThread;
    private LinkedList<Activity> mActvityList;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private BackDoorThread mBackDoorThread;
    private CrashListener mCrashListener;

    CrashExceptionHandler(Thread thread) {
        mTargetThread = thread;
        if (mTargetThread == null) {
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        } else {
            //系统默认的线程捕抓处理是否要过滤掉.
            //1、过滤:
            //mDefaultHandler = (thread.getUncaughtExceptionHandler() == thread.getThreadGroup()
            //      ? null : thread.getUncaughtExceptionHandler());
            //2、不过滤:
            mDefaultHandler = thread.getUncaughtExceptionHandler();

            mTargetThread.setUncaughtExceptionHandler(this);
        }
    }

    void setActList(LinkedList<Activity> list) {
        mActvityList = list;
    }

    void setCrashListener(CrashListener l) {
        mCrashListener = l;
    }

    void destory() {
        if (mTargetThread == null) {
            Thread.setDefaultUncaughtExceptionHandler(mDefaultHandler);
        } else {
            mTargetThread.setUncaughtExceptionHandler(mDefaultHandler);
        }
        if (mBackDoorThread != null) {
            mBackDoorThread.cleanSelf();
        }
        mTargetThread = null;
        mDefaultHandler = null;
        mActvityList = null;
        mBackDoorThread = null;
        mCrashListener = null;

    }

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        Activity act = mActvityList.isEmpty() ? CrashInfoHelper.getForegroundActivity() : mActvityList.getLast();
        mBackDoorThread = new BackDoorThread(act);
        mBackDoorThread.setCrashListener(mCrashListener);
        mBackDoorThread.setCrashException(e);
        mBackDoorThread.setCloseInfoCallBack(new BackDoorThread.onCloseInfoCallBack() {
            @Override
            public void onClose() {
                if (t.getId() != Looper.getMainLooper().getThread().getId()){
                    return;
                }
                for (Activity act: mActvityList) {
                    if (act != null){
                        act.finish();
                    }
                }
                //PLACE ONE
                //原来的CrashHandler处理可以放在这,或者放在PLACE TWO,主要看开发者的需要.
                if (mDefaultHandler != null) {
                    mDefaultHandler.uncaughtException(t, e);
                }
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        });
        mBackDoorThread.start();
        //PLACE TWO
    }
}
