package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.yellow5a5.crashanalysis.config.CrashBaseConfit;
import com.yellow5a5.crashanalysis.config.DefaultUmbrellaConfig;
import com.yellow5a5.crashanalysis.core.CrashInfoHelper;
import com.yellow5a5.crashanalysis.core.CrashInfoSaveCallBack;
import com.yellow5a5.crashanalysis.core.DefaultTrackPageAdapter;
import com.yellow5a5.crashanalysis.core.CrashExceptionHandler;
import com.yellow5a5.crashanalysis.core.CrashListener;
import com.yellow5a5.crashanalysis.core.IEncryptionCallback;
import com.yellow5a5.crashanalysis.core.WatchDog;
import com.yellow5a5.crashanalysis.monitor.MonitorRack;

import java.util.LinkedList;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

public class Umbrella {

    private Application mApp;
    private WatchDog mWatchDog;
    private MonitorRack mMonitorRack;
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

    public Umbrella inject(Application app){
        mApp = app;
        return getInstance();
    }

    public Umbrella openMonitor(){
        if (mMonitorRack == null && mApp != null){
            mMonitorRack = new MonitorRack(mApp);
            mMonitorRack.startNotify();
        }
        return getInstance();
    }

    public Umbrella setCustomCrashListener(CrashListener listener) {
        mCrashListener = listener;
        for (CrashExceptionHandler item : mExHandlerList) {
            item.setCrashListener(mCrashListener);
        }
        return getInstance();
    }

    public Umbrella setTargetToMainThread() {
        setTarget(Looper.getMainLooper().getThread());
        return getInstance();
    }

    public Umbrella setTargetToDefaultThread(){
        setTarget(null);
        return getInstance();
    }

    public void removeTargetToMainThread() {
        removeTarget(Looper.getMainLooper().getThread());
    }

    /**
     * 开启默认的事件跟踪.
     * @return
     */
    public Umbrella trackingPageEvent(){
        if (mApp != null){
            mApp.registerActivityLifecycleCallbacks(new DefaultTrackPageAdapter());
        }
        return getInstance();
    }

    /**
     * //TODO 传入自定义页面事件跟踪.
     * @param ptp
     * @return
     */
    public Umbrella trackingPageEvent(/*FIXME*/String ptp){
        if (mApp != null){
            mApp.registerActivityLifecycleCallbacks(new DefaultTrackPageAdapter());
        }
        return getInstance();
    }

    public Umbrella openANRWatchDog(int interval) {
        if (mWatchDog == null){
            mWatchDog = new WatchDog(interval);
            mWatchDog.start();
        }
        return getInstance();
    }


    /**
     * Exception-Listener injection for target thread.
     *
     * @param thread target
     */
    public Umbrella setTarget(Thread thread) {
        CrashExceptionHandler exHandler = new CrashExceptionHandler(thread);
        if (mCrashListener != null){
            exHandler.setCrashListener(mCrashListener);
        }
        exHandler.setActList(mActvityList);
        mExHandlerList.add(exHandler);
        return getInstance();
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

    public Umbrella setCrashConfig(CrashBaseConfit mCrashConfig) {
        this.mCrashConfig = mCrashConfig;
        return getInstance();
    }

    private IEncryptionCallback mIEncryptionCallback;

    public Umbrella setEncryptionMethod(IEncryptionCallback method){
        mIEncryptionCallback = method;
        return getInstance();
    }

    public String encryptString(String origin){
        if (mIEncryptionCallback != null){
            return mIEncryptionCallback.onEncryptionAlgorithm(origin);
         } else {
            return origin;
        }
    }

    public String decryptString(String cipher){
        if (mIEncryptionCallback != null){
            return mIEncryptionCallback.onDecryptionAalgorithm(cipher);
        } else {
            return cipher;
        }
    }
}
