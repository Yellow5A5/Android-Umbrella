package com.yellow5a5.crashanalysis.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Yellow5A5 on 17/4/22.
 */

public class DefaultUmbrellaConfig extends CrashBaseConfit{

    public static final String CRASH_INFO_DEFAULT_PATH = "localCrashInfo";
    public static final String ANR_INFO_DEFAULT_PATH = "localANRInfo";

    @Override
    public String getAppName() {
        return "Default_";
    }

    @Override
    public String getCrashFilePath() {
        return Environment.getExternalStorageDirectory() + File.separator + CRASH_INFO_DEFAULT_PATH;
    }

    @Override
    public String getANRFilePath() {
        return Environment.getExternalStorageDirectory() + File.separator + ANR_INFO_DEFAULT_PATH;
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

    @Override
    public boolean isOpenUmbrella() {
        return true;
    }

    @Override
    public boolean isNeedANRCatch() {
        return true;
    }
}
