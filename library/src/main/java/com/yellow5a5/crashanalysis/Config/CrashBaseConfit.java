package com.yellow5a5.crashanalysis.Config;

/**
 * Created by Yellow5A5 on 17/4/16.
 */

public abstract class CrashBaseConfit {

    //App名字,用于文件拼接,防止多App使用此库导致的问题。
    public abstract String getAppName();

    //对外路径可配
    public abstract String getLocalPath();

    //是否开启App路径跟踪
    public abstract boolean isOpenActPath();

    //所需Crash拦截次数
    public abstract int onCrashInterruptCount();

}
