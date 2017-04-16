package com.yellow5a5.crashanalysis;

import android.app.Activity;
import android.os.Looper;

import com.yellow5a5.crashanalysis.View.CrashInfoDialog;

import java.util.LinkedList;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

public class CrashAnalysis implements Thread.UncaughtExceptionHandler {

    private static class InstanceHolder {
        private static CrashAnalysis instance = new CrashAnalysis();
    }
    public static CrashAnalysis getInstance() {
        return InstanceHolder.instance;
    }

    private CrashExceptionHandler mCrashHandler;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private LinkedList<Activity> mActvityList = new LinkedList<>();

    public void init(){
        if (mCrashHandler == null){
            Looper.getMainLooper().getThread().setUncaughtExceptionHandler(this);
        }
    }

    public void registeredActivity(Activity act){
        mActvityList.add(act);
    }

    public void unRegisterActivity(Activity act){
        mActvityList.remove(act);
    }


    @Override
    public void uncaughtException(Thread t, final Throwable e) {
        if (mDefaultHandler != null){
            mDefaultHandler.uncaughtException(t,e);
        }

    }



    public static class CrashThread extends Thread{

        private Activity mActivity;
        private String mCrashContent;
        private CrashListener mCrashListener;

        public interface CrashListener{
            void onCallBack();
        }

        public void setCrashListener(CrashListener l){
            mCrashListener = l;
        }


        public CrashThread(Activity act){
            mActivity = act;
        }

        public void setCrashContent(String content){
            mCrashContent = content;
        }

        @Override
        public void run() {
            Looper.prepare();

            CrashInfoHelper.saveInfoLocal();//TODO 要回调保存成功出来。
            //展示Crash窗
            showCrashDialog(mCrashContent);
            if (mCrashListener != null){
                mCrashListener.onCallBack();
            }
            super.run();
            Looper.loop();
        }

        private void showCrashDialog(String content) {
            if (mActivity != null){
                CrashInfoDialog dialog = new CrashInfoDialog(mActivity);
                dialog.setCrashContent(content);
                dialog.show();
            }
        }
    }

}
