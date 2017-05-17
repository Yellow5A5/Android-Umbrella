package com.yellow5a5.crashanalysis.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.yellow5a5.crashanalysis.Umbrella;

/**
 * Created by Yellow5A5 on 17/5/17.
 */

public class WatchDog extends Thread{

    private Handler mDogHander;
    private Handler mUIHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == ANR_MESSAGE_TYEP_FLAG){
                Log.d(Umbrella.class.getName(), "UI run: " + isRunning);
            }
            return false;
        }
    });

    private boolean isRunning = false;

    private int ANR_MESSAGE_TYEP_FLAG = 0xFFABCD;
    private int ANR_CHECKER_TIME_INTERVAL = 3000;

    private final Runnable dogTask = new Runnable() {
        @Override
        public void run() {
            if (mUIHandler.hasMessages(ANR_MESSAGE_TYEP_FLAG)){
                mUIHandler.removeMessages(ANR_MESSAGE_TYEP_FLAG);
                Log.d(Umbrella.class.getName(), "run: App Not Response - 3000ms");
                StackTraceElement[] list = Looper.getMainLooper().getThread().getStackTrace();
                final String path = Umbrella.getInstance().getCrashConfig().getANRFilePath();
                final String appName = Umbrella.getInstance().getCrashConfig().getAppName();
                CrashInfoHelper.saveInfoLocal(path, appName, CrashInfoHelper.getStackTraceAsString(list), new CrashInfoSaveCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailture() {

                    }
                });
            } else {
                isRunning = false;
                Log.d(Umbrella.class.getName(), "Dog run: " + isRunning);
            }
            mUIHandler.sendEmptyMessageDelayed(ANR_MESSAGE_TYEP_FLAG, 0);
            //FIXME 细分监测 否则在临界状态下最多会为设定时间的2倍。
            mDogHander.postDelayed(this, ANR_CHECKER_TIME_INTERVAL);
        }
    };

    public WatchDog(int interval_time){
        ANR_CHECKER_TIME_INTERVAL = interval_time;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        if (mUIHandler == null){
            mUIHandler = new Handler(Looper.getMainLooper());
        }
        mUIHandler.sendEmptyMessageDelayed(ANR_MESSAGE_TYEP_FLAG, 0);
        if (mDogHander == null){
            mDogHander = new Handler(Looper.myLooper());
        }
        mDogHander.postDelayed(dogTask,ANR_CHECKER_TIME_INTERVAL + 100);
        Looper.loop();
    }
}
