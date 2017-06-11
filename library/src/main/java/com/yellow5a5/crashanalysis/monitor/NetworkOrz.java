package com.yellow5a5.crashanalysis.monitor;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Yellow5A5 on 17/5/30.
 */

public class NetworkOrz extends IOrz {

    private long mPreTotalBytes;
    private long mPreTime;
    private long mIntervalTime;

    NetworkOrz(int type, String title) {
        super(type, title);
    }

    @Override
    public void update() {
        if (mPreTotalBytes == 0) {
            mPreTotalBytes = TrafficStats.getTotalRxBytes();
            return;
        }
        if (mPreTime == 0){
            mPreTime = System.currentTimeMillis();
            return;
        } else {
            long current = System.currentTimeMillis();
            mIntervalTime = current - mPreTime;
            mPreTime = current;
        }
        long currentBytes = TrafficStats.getTotalRxBytes();
        long diffBytes = (currentBytes - mPreTotalBytes);
        float speedKb = (diffBytes >> 10)/ ((float)mIntervalTime) * 1000;

        BigDecimal bd = new BigDecimal(speedKb);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        speedKb = bd.floatValue();

//        Log.d(NetworkOrz.class.getName(), "update: mIntervalTime " + mIntervalTime);
//        Log.d(NetworkOrz.class.getName(), "update: diffBytes " + diffBytes);
//        Log.d(NetworkOrz.class.getName(), "update: speedKb  " + speedKb);
        mPreTotalBytes = currentBytes;
        mData.getPercentList().push(speedKb);

        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mBindView != null){
                    mBindView.setText(String.valueOf(mData.getPercentList().get()) + "K/s");
                }
            }
        });
    }
}
