package com.yellow5a5.crashanalysis.monitor;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.yellow5a5.crashanalysis.Umbrella;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Yellow5A5 on 17/3/4.
 */

public class AppStateOwner implements Subject {

    private Thread mMonitorThread;
    private Handler mMonitorHandler;
    private List<IOrz> mObsList = new ArrayList<>();
    private List<AppStateData> mDataList = new ArrayList<>();

    @Override
    public void registerObserver(IOrz obs) {
        mObsList.add(obs);
        if (obs.getData() != null){
            mDataList.add(obs.getData());
        }
    }

    @Override
    public void removeObserver(IOrz obs) {
        if (mObsList.contains(obs)){
            mObsList.remove(obs);
        }
    }

    @Override
    public void notifyObservers() {
        for (IOrz obs: mObsList) {
            if (mDataList.contains(obs.getData())){
                obs.update();
            }
        }
    }

    public void start(final int timeBlock){
        final Runnable updateTask =new Runnable() {
            @Override
            public void run() {
                notifyObservers();
                mMonitorHandler.postDelayed(this, timeBlock);
            }
        };
        mMonitorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mMonitorHandler = new Handler(Looper.myLooper());
                mMonitorHandler.postDelayed(updateTask, timeBlock);
                Looper.loop();
            }
        });
        mMonitorThread.start();
    }
}
