package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.os.Looper;

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
        Looper.prepare();
        CrashInfoDialog dialog = null;
        if (mActivity != null) {
            dialog = new CrashInfoDialog(mActivity);
            dialog.setCrashContent(mCrashContent);
            dialog.show();
        }
        final CrashInfoDialog finalDialog = dialog;
        CrashInfoHelper.saveInfoLocal(mCrashContent, new CrashInfoSaveCallBack() {
            @Override
            public void onSuccess() {
                if (finalDialog != null){
                    finalDialog.setCompleteState();
                }
            }

            @Override
            public void onFailture() {

            }
        });

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
