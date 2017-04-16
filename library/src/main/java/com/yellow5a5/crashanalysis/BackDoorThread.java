package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.os.Looper;
import android.util.Log;

import com.yellow5a5.crashanalysis.View.CrashInfoDialog;

/**
 * Created by Yellow5A5 on 17/4/16.
 */

class BackDoorThread extends Thread {

    private Activity mActivity;
    private String mCrashContent;
    private CrashListener mCrashListener;

    void setCrashListener(CrashListener l) {
        mCrashListener = l;
    }

    BackDoorThread(Activity act) {
        mActivity = act;
    }

    public void setCrashContent(String content) {
        mCrashContent = content;
    }

    @Override
    public void run() {
        Log.e(BackDoorThread.class.getName(), "run: 444");
        Looper.prepare();
        CrashInfoDialog dialog = null;
        Log.e(BackDoorThread.class.getName(), "run: 255552");
        if (mActivity != null) {
            dialog = new CrashInfoDialog.Builder(mActivity)
                    .setCrashContent(mCrashContent)
                    .build();
            dialog.show();
            Log.e(BackDoorThread.class.getName(), "run: 255552");
        }
        Log.e(BackDoorThread.class.getName(), "run: 22");
        final CrashInfoDialog finalDialog = dialog;
        CrashInfoHelper.saveInfoLocal(mCrashContent, new CrashInfoSaveCallBack() {
            @Override
            public void onSuccess() {
                if (finalDialog != null){
                    finalDialog.setCompleteState();
                }
                Log.e(BackDoorThread.class.getName(), "run: 11");
            }

            @Override
            public void onFailture() {

            }
        });
        Log.e(BackDoorThread.class.getName(), "run: bbb");
        if (mCrashListener != null) {
            mCrashListener.onCallBack();
        }

        Looper.loop();
    }

    void cleanSelf(){
        mActivity = null;
        mCrashListener = null;
    }
}
