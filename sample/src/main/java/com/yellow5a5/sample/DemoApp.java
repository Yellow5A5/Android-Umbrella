package com.yellow5a5.sample;

import android.app.Application;

import com.yellow5a5.crashanalysis.Umbrella;
import com.yellow5a5.crashanalysis.core.CrashListener;

/**
 * Created by Yellow5A5 on 17/4/17.
 */

public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Umbrella.getInstance()
                .inject(this)
                .openANRWatchDog(3000)
                .trackingPageEvent()
                .setTargetToMainThread();
        Umbrella.getInstance().setCustomCrashListener(new CrashListener() {
            @Override
            public void onCrash() {
                //TODO...
            }
        });

//        CustomActivityOnCrash.install(this);


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
