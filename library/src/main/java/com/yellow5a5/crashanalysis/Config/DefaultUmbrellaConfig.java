package com.yellow5a5.crashanalysis.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Yellow5A5 on 17/4/22.
 */

public class DefaultUmbrellaConfig extends CrashBaseConfit{

    public static final String CRASH_INFO_DEFAULT_PATH = "localCrashInfo";

    @Override
    public String getAppName() {
        return "Default_";
    }

    @Override
    public String getLocalPath() {
        return Environment.getExternalStorageDirectory() + File.separator + CRASH_INFO_DEFAULT_PATH;
    }

    @Override
    public boolean isOpenActPath() {
        return false;
    }

    @Override
    public boolean isNeedRestartApp() {
        return true;
    }

    @Override
    public int onCrashInterruptCount() {
        return 0;
    }
}
