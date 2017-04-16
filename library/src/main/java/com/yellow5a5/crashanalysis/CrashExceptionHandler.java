package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.graphics.Point;
import android.os.Looper;

import com.yellow5a5.crashanalysis.View.CrashInfoDialog;

import java.util.LinkedList;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {


    private boolean isInit = false;
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
        mDefaultHandler = thread.getUncaughtExceptionHandler();
        mTargetThread.setUncaughtExceptionHandler(this);
        isInit = true;
    }

    void setActList(LinkedList<Activity> list) {
        mActvityList = list;
    }

    public void setCrashListener(CrashListener l) {
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

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        }
        mBackDoorThread = new BackDoorThread(mActvityList.getLast());
        mBackDoorThread.setCrashListener(mCrashListener);
        mBackDoorThread.start();
    }
}
