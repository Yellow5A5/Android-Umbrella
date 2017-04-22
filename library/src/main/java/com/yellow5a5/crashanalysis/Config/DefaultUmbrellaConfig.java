package com.yellow5a5.crashanalysis.Config;

import android.os.Environment;

import com.yellow5a5.crashanalysis.Config.CrashBaseConfit;

/**
 * Created by Yellow5A5 on 17/4/22.
 */

public class DefaultUmbrellaConfig extends CrashBaseConfit{

    public static final String CRASH_INFO_DEFAULT_PATH = "localCrashInfo";

    @Override
    public String getAppName() {
        return "";
    }

    @Override
    public String getLocalPath() {
        return Environment.getExternalStorageDirectory() + CRASH_INFO_DEFAULT_PATH;
    }

    @Override
    public boolean isOpenActPath() {
        return false;
    }

    @Override
    public int onCrashInterruptCount() {
        return 0;
    }
}
