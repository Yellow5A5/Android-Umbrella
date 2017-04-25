package com.yellow5a5.sample;

import android.app.Application;

import com.yellow5a5.crashanalysis.Umbrella;
import com.yellow5a5.crashanalysis.core.CrashAnalysisLcAdapter;
import com.yellow5a5.crashanalysis.core.CrashListener;

/**
 * Created by Yellow5A5 on 17/4/17.
 */

public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new CrashAnalysisLcAdapter());
        Umbrella.getInstance().setTargetToMainThread();
        Umbrella.getInstance().setCustomCrashListener(new CrashListener() {
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
