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
        CrashAnalysisCenter.getInstance().registeredActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //TODO 通过Class +  方法映射Type去记录操作路径,
        //TODO 另外对外公开可输入日志的方法,让使用者能够自己输入crash产生的路径。
        //怕字符串缓存过大可以针对小字符进行缓存,超过缓存在进行读写复制。

        //TODO 加入线程ANR监听。

        //TODO 加入可配置的性能监听。

        //TODO 对用户提供一个可以看到CrashLog的Act,随时查看。
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
        CrashAnalysisCenter.getInstance().unRegisterActivity(activity);
    }
}
