package com.yellow5a5.crashanalysis.config;

/**
 * Created by Yellow5A5 on 17/4/16.
 */

public abstract class CrashBaseConfit {

    //App名字,用于文件拼接,防止多App使用此库导致的问题。
    public abstract String getAppName();

    //Crash信息存放路径
    public abstract String getCrashFilePath();

    //ANR信息存放路径
    public abstract String getANRFilePath();

    //是否开启App路径跟踪
    public abstract boolean isOpenActPath();

    //是否提供App重启功能
    public abstract boolean isNeedRestartApp();

    //所需Crash拦截次数
    public abstract int onCrashInterruptCount();

    //是否提供查看保护伞信息的入口
    public abstract boolean isOpenUmbrella();

    public abstract boolean isNeedANRCatch();


    //TODO Native Crash捕抓。

    //TODO ANR 捕抓
}
