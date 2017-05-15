package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.os.Looper;

import com.yellow5a5.crashanalysis.config.CrashBaseConfit;
import com.yellow5a5.crashanalysis.config.DefaultUmbrellaConfig;
import com.yellow5a5.crashanalysis.core.CrashExceptionHandler;
import com.yellow5a5.crashanalysis.core.CrashListener;

import java.util.LinkedList;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

public class Umbrella {

    private LinkedList<Activity> mActvityList = new LinkedList<>();
    private LinkedList<CrashExceptionHandler> mExHandlerList = new LinkedList<>();
    private CrashListener mCrashListener;
    private CrashBaseConfit mCrashConfig;

    private static class InstanceHolder {
        private static Umbrella instance = new Umbrella();
    }

    public static Umbrella getInstance() {
        return InstanceHolder.instance;
    }

    private Umbrella() {
    }

    public void setCustomCrashListener(CrashListener listener) {
        mCrashListener = listener;
        for (CrashExceptionHandler item : mExHandlerList) {
            item.setCrashListener(mCrashListener);
        }
    }

    public void setTargetToMainThread() {
        setTarget(Looper.getMainLooper().getThread());
    }

    public void setTargetToDefaultThread(){
        setTarget(null);
    }

    public void setTargetCurrentThread(){
        setTarget(Thread.currentThread());
    }

    public void removeTargetToMainThread() {
        removeTarget(Looper.getMainLooper().getThread());
    }

    /**
     * Exception-Listener injection for target thread.
     *
     * @param thread target
     */
    public void setTarget(Thread thread) {
        CrashExceptionHandler exHandler = new CrashExceptionHandler(thread);
        if (mCrashListener != null){
            exHandler.setCrashListener(mCrashListener);
        }
        exHandler.setActList(mActvityList);
        mExHandlerList.add(exHandler);
    }

    /**
     * Clear Exception-Listener in target thread.
     *
     * @param thread target
     */
    public void removeTarget(Thread thread) {
        Thread.UncaughtExceptionHandler exHandler = thread.getUncaughtExceptionHandler();
        if (exHandler instanceof CrashExceptionHandler && mExHandlerList.remove(exHandler)) {
            ((CrashExceptionHandler) exHandler).destory();
        }
    }

    /**
     * will show in target act.
     *
     * @param act
     */
    public void registeredActivity(Activity act) {
        mActvityList.add(act);
    }

    /**
     * remove.
     *
     * @param act
     */
    public void unRegisterActivity(Activity act) {
        mActvityList.remove(act);
    }

    public CrashBaseConfit getCrashConfig() {
        if (mCrashConfig == null){
            mCrashConfig = new DefaultUmbrellaConfig();
        }
        return mCrashConfig;
    }

    public void setCrashConfig(CrashBaseConfit mCrashConfig) {
        this.mCrashConfig = mCrashConfig;
    }
}