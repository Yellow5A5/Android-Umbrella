package com.yellow5a5.crashanalysis.core;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.yellow5a5.crashanalysis.Umbrella;
import com.yellow5a5.crashanalysis.view.ANRInfoDialog;

/**
 * Created by Yellow5A5 on 17/5/17.
 */

public class WatchDog extends Thread{

    private static final int ANR_MESSAGE_TYEP_FLAG = 0xFFABCD;
    private static final int BLOCK_PRECISION_L = 500;
    private static final int BLOCK_PRECISION_M = 200;
    private static final int BLOCK_PRECISION_S = 50;

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
    private boolean isAnrDialogShown = false;

    //block times.
    private int mPrecisionBlockTimes = 0;
    //custom block-time to show dialog.
    private int mBolckInterval = 3000;
    //calculation accuracy.
    private int mBlockPrecision = 50;

    private final Runnable dogTask = new Runnable() {
        @Override
        public void run() {
            if (mUIHandler.hasMessages(ANR_MESSAGE_TYEP_FLAG)){
                mUIHandler.removeMessages(ANR_MESSAGE_TYEP_FLAG);
                mPrecisionBlockTimes++;
                Log.e(WatchDog.class.getName(), "dog run: " + isRunning);
                if (mPrecisionBlockTimes * mBlockPrecision > mBolckInterval){
                    StackTraceElement[] list = Looper.getMainLooper().getThread().getStackTrace();
                    String anrInfo = CrashInfoHelper.getStackTraceAsString(list);
                    Log.e(WatchDog.class.getName(), "ANR-INFO: " + anrInfo);
                    Activity topActivity = CrashInfoHelper.getForegroundActivity();
                    if (topActivity != null && !isAnrDialogShown) {
                        ANRInfoDialog dialog = new ANRInfoDialog.Builder(topActivity)
                                .setAnrContent(anrInfo)
                                .build();
                        dialog.show();
                        isAnrDialogShown = true;
                    }

                    //TODO ANR信息是否要存储。
//                final String path = Umbrella.getInstance().getCrashConfig().getANRFilePath();
//                final String appName = Umbrella.getInstance().getCrashConfig().getAppName();
//                CrashInfoHelper.saveInfoLocal(path, appName, CrashInfoHelper.getStackTraceAsString(list), new CrashInfoSaveCallBack() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onFailture() {
//
//                    }
//                });
                }
            } else {
                Log.e(WatchDog.class.getName(), "dog run: " + isRunning);
                mPrecisionBlockTimes = 0;
                isRunning = false;
            }
            mUIHandler.sendEmptyMessageDelayed(ANR_MESSAGE_TYEP_FLAG, 0);
            mDogHander.postDelayed(this, mBlockPrecision);
        }
    };

    public WatchDog(int interval){
        if(interval < BLOCK_PRECISION_S){
            mBlockPrecision = BLOCK_PRECISION_S;
        }else if (interval < BLOCK_PRECISION_M){
            mBlockPrecision = BLOCK_PRECISION_M;
        }else if (interval >= BLOCK_PRECISION_M){
            mBlockPrecision = BLOCK_PRECISION_L;
        }
        mPrecisionBlockTimes = interval / mBlockPrecision;
        mBolckInterval = interval;
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
        mDogHander.postDelayed(dogTask, mPrecisionBlockTimes + 100);
        Looper.loop();
    }
}
