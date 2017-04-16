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
    private Throwable mCrashException;
    private CrashListener mCrashListener;

    void setCrashListener(CrashListener l) {
        mCrashListener = l;
    }

    BackDoorThread(Activity act) {
        mActivity = act;
    }

    public void setCrashException(Throwable throwable) {
        mCrashException = throwable;
    }

    @Override
    public void run() {
        Looper.prepare();
        CrashInfoDialog dialog = null;
        String crashInfoShow = CrashInfoHelper.convertStackTraceToShow(mCrashException.getStackTrace());
        Log.e(BackDoorThread.class.getName(), "run: " + crashInfoShow);
        String crashInfoSave = CrashInfoHelper.convertStackTraceToSave(mCrashException.getStackTrace());
        Log.e(BackDoorThread.class.getName(), "run: " + crashInfoSave);
        if (mActivity != null) {
            dialog = new CrashInfoDialog.Builder(mActivity)
                    .setCrashContent(crashInfoShow)
                    .build();
            dialog.show();
        }
        final CrashInfoDialog finalDialog = dialog;
        CrashInfoHelper.saveInfoLocal(crashInfoSave, new CrashInfoSaveCallBack() {
            @Override
            public void onSuccess() {
                if (finalDialog != null){
                    finalDialog.setCompleteState();
                    CrashInfoDialog
                }
            }

            @Override
            public void onFailture() {
                if (finalDialog != null){
                    finalDialog.setFailtureState();
                }
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
