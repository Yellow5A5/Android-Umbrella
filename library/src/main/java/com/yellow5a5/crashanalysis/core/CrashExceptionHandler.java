package com.yellow5a5.crashanalysis.core;

import android.app.Activity;
import android.os.Looper;

import com.yellow5a5.crashanalysis.Umbrella;

import java.util.LinkedList;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread mTargetThread;
    private LinkedList<Activity> mActvityList;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private BackDoorThread mBackDoorThread;
    private CrashListener mCrashListener;

    private boolean isNeedRestartApp = false;

    public CrashExceptionHandler(Thread thread) {
        mTargetThread = thread;
        if (mTargetThread == null) {
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        } else {
            //系统默认的线程捕抓处理是否要过滤掉.
            //1、过滤:
            mDefaultHandler = (thread.getUncaughtExceptionHandler() == thread.getThreadGroup()
                  ? null : thread.getUncaughtExceptionHandler());
            //2、不过滤:
//            mDefaultHandler = thread.getUncaughtExceptionHandler();

            mTargetThread.setUncaughtExceptionHandler(this);
        }
    }

    public void setActList(LinkedList<Activity> list) {
        mActvityList = list;
    }

    public void setCrashListener(CrashListener l) {
        mCrashListener = l;
    }

    public void destory() {
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
        Umbrella.getInstance().setCrashNowFlag(true);
        isNeedRestartApp = false;
        Activity act = mActvityList.isEmpty() ? CrashInfoHelper.getForegroundActivity() : mActvityList.getLast();
        mBackDoorThread = new BackDoorThread(act);
        mBackDoorThread.setCrashListener(mCrashListener);
        mBackDoorThread.setCrashException(e);
        mBackDoorThread.setCloseInfoCallBack(new BackDoorThread.onCloseInfoCallBack() {
            @Override
            public void onClose() {
                if (t.getId() != Looper.getMainLooper().getThread().getId()) {
                    return;
                }
                for (Activity act : mActvityList) {
                    if (act != null) {
                        act.finish();
                    }
                }
                //PLACE ONE
                //原来的CrashHandler处理或可以放在这,或放在PLACE TWO,主要看开发者的需要.
                if (mDefaultHandler != null) {
                    mDefaultHandler.uncaughtException(t, e);
                }
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);

            }

            @Override
            public void onRestart() {
                if(Umbrella.getInstance().getCrashConfig().isNeedRestartApp()){
                    Umbrella.getInstance().setCrashNowFlag(false);
                    isNeedRestartApp = true;
                }

            }
        });
        mBackDoorThread.start();
        //PLACE TWO

        //正常来说,只有主线程才需要重启
        if (t == Looper.getMainLooper().getThread()){
            while (true){
                if (isNeedRestartApp) {
                    try {
                        Looper.loop();
                    }catch (Exception ex){
                        uncaughtException(t,ex);
                    }
                }
            }
        }
    }
}
