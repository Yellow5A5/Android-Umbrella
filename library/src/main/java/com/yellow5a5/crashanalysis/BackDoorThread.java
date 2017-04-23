package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.yellow5a5.crashanalysis.view.CrashInfoDialog;

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
        void onRestart();
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
        final String crashInfoShow = CrashInfoHelper.convertExceptionToStringShow(mCrashException);
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

                @Override
                public void onRestartBtnClick() {
                    if (mOnCloseInfoCallBack != null){
                        mOnCloseInfoCallBack.onRestart();
                    }
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            while (true){
//                                Looper.loop();
//                            }
//                        }
//                    });
                }
            });
        }
        final CrashInfoDialog finalDialog = dialog;
        final String crashInfoSave = CrashInfoHelper.convertExceptionToStringSave(mCrashException);
        final String path = CrashAnalysisCenter.getInstance().getCrashConfig().getLocalPath();
        final String appName = CrashAnalysisCenter.getInstance().getCrashConfig().getAppName();
        CrashInfoHelper.saveInfoLocal(path, appName, crashInfoSave, new CrashInfoSaveCallBack() {
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
