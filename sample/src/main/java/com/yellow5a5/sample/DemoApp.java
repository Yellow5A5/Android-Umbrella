package com.yellow5a5.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.yellow5a5.crashanalysis.CrashAnalysis;
import com.yellow5a5.crashanalysis.CrashAnalysisLcAdapter;
import com.yellow5a5.crashanalysis.CrashListener;

/**
 * Created by Yellow5A5 on 17/4/17.
 */

public class DemoApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO 还可以加入路径记录!
        registerActivityLifecycleCallbacks(new CrashAnalysisLcAdapter());
        CrashAnalysis.getInstance().setTargetToMainThread();
        CrashAnalysis.getInstance().setCustomCrashListener(new CrashListener() {
            @Override
            public void onCrash() {
                //TODO...
            }
        });
    }
}
