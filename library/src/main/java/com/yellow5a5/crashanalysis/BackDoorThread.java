package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.yellow5a5.crashanalysis.View.CrashInfoDialog;

/**
 * Created by Yellow5A5 on 17/4/16.
 */

class BackDoorThread extends Thread {

    private Activity mActivity;
    private Throwable mCrashException;
    private CrashListener mCrashListener;
    private onCloseInfoCallBack mOnCloseInfoCallBack;

    public interface onCloseInfoCallBack {
        void onClose();
    }

    void setCloseInfoCallBack(onCloseInfoCallBack callback){
        mOnCloseInfoCallBack = callback;
    }

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
        final String crashInfoShow = CrashInfoHelper.convertStackTraceToShow(mCrashException.getStackTrace());
        if (mActivity != null) {
            clipCrashContent(crashInfoShow);
            dialog = new CrashInfoDialog.Builder(mActivity)
                    .setCrashContent(crashInfoShow)
                    .build();
            dialog.show();
            dialog.setCrashDialogCallback(new CrashInfoDialog.CrashDialogCallback() {
                @Override
                public void onCloseBtnClick() {
                    if (mOnCloseInfoCallBack != null){
                        mOnCloseInfoCallBack.onClose();
                    }
                }

                @Override
                public void onShareBtnClick() {
                    CrashInfoHelper.shareCrashInfo(crashInfoShow, mActivity);
                }
            });
        }
        final CrashInfoDialog finalDialog = dialog;
        final String crashInfoSave = CrashInfoHelper.convertStackTraceToSave(mCrashException.getStackTrace());
        CrashInfoHelper.saveInfoLocal(crashInfoSave, new CrashInfoSaveCallBack() {
            @Override
            public void onSuccess() {
                if (finalDialog != null){
                    finalDialog.setCompleteState();
                    clipCrashContent(crashInfoSave);
                }
            }

            @Override
            public void onFailture() {
                if (finalDialog != null){
                    finalDialog.setFailtureState();
                }
            }
        });
        Log.e(CrashAnalysisCenter.class.getName(), crashInfoSave);
        if (mCrashListener != null) {
            mCrashListener.onCrash();
        }

        Looper.loop();
    }

    private void clipCrashContent(String info){
        ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", info);
        cm.setPrimaryClip(mClipData);
        Toast.makeText(mActivity, "Crash信息已复制到剪切板中。", Toast.LENGTH_LONG).show();
    }

    void cleanSelf(){
        mActivity = null;
        mCrashListener = null;
    }
}
