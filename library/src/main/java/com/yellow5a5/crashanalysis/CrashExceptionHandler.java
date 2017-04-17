package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.os.Looper;

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

    CrashExceptionHandler() {
        this(Looper.getMainLooper().getThread());
    }

    CrashExceptionHandler(Thread thread) {
        mTargetThread = thread;
        mDefaultHandler = (thread.getUncaughtExceptionHandler() == thread.getThreadGroup()
                ? null : thread.getUncaughtExceptionHandler());
        mTargetThread.setUncaughtExceptionHandler(this);
    }

    void setActList(LinkedList<Activity> list) {
        mActvityList = list;
    }

    void setCrashListener(CrashListener l) {
        mCrashListener = l;
    }

    void destory() {
        if (mTargetThread != null) {
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
    public void uncaughtException(Thread t, Throwable e) {
        Activity act = mActvityList.isEmpty() ? CrashInfoHelper.getForegroundActivity() : mActvityList.getLast();
        mBackDoorThread = new BackDoorThread(act);
        mBackDoorThread.setCrashListener(mCrashListener);
        mBackDoorThread.setCrashException(e);
        mBackDoorThread.setCloseInfoCallBack(new BackDoorThread.onCloseInfoCallBack() {
            @Override
            public void onClose() {
                for (Activity act: mActvityList) {
                    if (act != null){
                        act.finish();
                    }
                }
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        mBackDoorThread.start();
        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        }
    }
}
