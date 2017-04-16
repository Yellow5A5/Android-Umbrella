package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.os.Looper;

import com.yellow5a5.crashanalysis.View.CrashInfoDialog;

import java.util.LinkedList;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

public class CrashAnalysis {

    private LinkedList<Activity> mActvityList = new LinkedList<>();
    private LinkedList<CrashExceptionHandler> mExHandlerList = new LinkedList<>();

    private static class InstanceHolder {
        private static CrashAnalysis instance = new CrashAnalysis();
    }

    public static CrashAnalysis getInstance() {
        return InstanceHolder.instance;
    }

    private CrashAnalysis() {
    }

    public void setTargetToMainThread(){
        setTarget(Looper.getMainLooper().getThread());
    }
    /**
     * Exception-Listener injection for target thread.
     * @param thread target
     */
    public void setTarget(Thread thread) {
        CrashExceptionHandler exHandler = new CrashExceptionHandler(thread);
        exHandler.setActList(mActvityList);
        mExHandlerList.add(exHandler);
    }

    /**
     * Clear Exception-Listener in target thread.
     * @param thread target
     */
    public void removeTarget(Thread thread) {
        Thread.UncaughtExceptionHandler exHandler = thread.getUncaughtExceptionHandler();
        if (exHandler instanceof CrashExceptionHandler && mExHandlerList.remove(exHandler)){
            ((CrashExceptionHandler) exHandler).destory();
        }
    }

    /**
     * will show by target act.
     * @param act
     */
    public void registeredActivity(Activity act) {
        mActvityList.add(act);
    }

    /**
     * remove.
     * @param act
     */
    public void unRegisterActivity(Activity act) {
        mActvityList.remove(act);
    }

}
