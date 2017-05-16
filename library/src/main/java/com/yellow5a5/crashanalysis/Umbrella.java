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

import java.util.LinkedList;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

public class Umbrella {

    private Application mApp;
    private Thread mWatchDog;
    private LinkedList<Activity> mActvityList = new LinkedList<>();
    private LinkedList<CrashExceptionHandler> mExHandlerList = new LinkedList<>();
    private CrashListener mCrashListener;
    private CrashBaseConfit mCrashConfig;

    private int UI_ANR_TIMER_CHECKER = 3000;
    private Handler dogHander;
    private Handler mUIHandler = new Handler(Looper.myLooper());
    private boolean isRunning = false;

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

    public Umbrella openANRWatchDog() {
        if (mWatchDog != null){
            return getInstance();
        }

        final Runnable uiTask = new Runnable() {
            @Override
            public void run() {
                isRunning = true;
                Log.e(Umbrella.class.getName(), "UI run: " + isRunning);
                mUIHandler.postDelayed(this, UI_ANR_TIMER_CHECKER);
            }
        };
        mUIHandler.postDelayed(uiTask, UI_ANR_TIMER_CHECKER);

        final Runnable dogTask = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    isRunning = false;
                    Log.e(Umbrella.class.getName(), "Dog run: " + isRunning);
                } else {
                    Log.e(Umbrella.class.getName(), "run: App Not Response - 3000ms");
                    StackTraceElement[] list = Looper.getMainLooper().getThread().getStackTrace();
                    final String path = Umbrella.getInstance().getCrashConfig().getANRFilePath();
                    final String appName = Umbrella.getInstance().getCrashConfig().getAppName();
                    CrashInfoHelper.saveInfoLocal(path, appName, CrashInfoHelper.getStackTraceAsString(list), new CrashInfoSaveCallBack() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailture() {

                        }
                    });
                }
                dogHander.postDelayed(this, UI_ANR_TIMER_CHECKER);
            }
        };
        mUIHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWatchDog = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        if (dogHander == null){
                            dogHander = new Handler(Looper.myLooper());
                        }
                        dogHander.post(dogTask);
                        Looper.loop();
                    }
                });
                mWatchDog.start();
            }
        }, UI_ANR_TIMER_CHECKER + 100);
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
}
