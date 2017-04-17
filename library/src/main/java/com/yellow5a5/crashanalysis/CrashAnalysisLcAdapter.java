package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by Yellow5A5 on 17/4/17.
 */

public class CrashAnalysisLcAdapter implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        CrashAnalysis.getInstance().registeredActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        CrashAnalysis.getInstance().unRegisterActivity(activity);
    }
}
