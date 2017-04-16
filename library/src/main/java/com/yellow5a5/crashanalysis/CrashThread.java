package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.os.Looper;

/**
 * Created by Yellow5A5 on 17/4/16.
 */

public class CrashThread extends Thread{


    public CrashThread(Activity act){

    }

    @Override
    public void run() {
        Looper.prepare();
        super.run();

        Looper.loop();
    }
}
