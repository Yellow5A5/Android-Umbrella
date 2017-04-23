package com.yellow5a5.sample;

import android.app.Application;
import android.os.Looper;

import com.yellow5a5.crashanalysis.CrashAnalysisCenter;
import com.yellow5a5.crashanalysis.CrashAnalysisLcAdapter;
import com.yellow5a5.crashanalysis.CrashListener;

/**
 * Created by Yellow5A5 on 17/4/17.
 */

public class DemoApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new CrashAnalysisLcAdapter());
        CrashAnalysisCenter.getInstance().setTargetToMainThread();
        CrashAnalysisCenter.getInstance().setCustomCrashListener(new CrashListener() {
            @Override
            public void onCrash() {
                //TODO...
            }
        });
//        getMainLooper().getThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                while (true){
//                    Looper.loop();
//                }
//            }
//        });
    }
}
