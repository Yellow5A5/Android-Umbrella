package com.yellow5a5.crashanalysis.monitor;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;

/**
 * Created by Yellow5A5 on 17/3/6.
 * FpsOrz is special.
 */

public class FpsOrz extends IOrz {

    private boolean isStartMonitoring;


    public FpsOrz(int type, String title) {
        super(type, title);
    }

    @Override
    public void update() {
        if (!isStartMonitoring) {
            isStartMonitoring = true;
            setFrameCallback();
        }
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mBindView != null && mData.getPercentList().get() != null) {
                    mBindView.setText(String.valueOf((mData.getPercentList().get().intValue())));
                }
            }
        });
    }

    private void setFrameCallback() {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(FpsOrz.class.getName(), "setFrameCallback: ");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
                        @Override
                        public void doFrame(long frameTimeNanos) {
                            if (!FpsMonitor.getInstance().isTimeToReset(frameTimeNanos)) {
                                FpsMonitor.getInstance().calculateRemainTime(frameTimeNanos);
                            } else {
                                Log.d(FpsOrz.class.getName(), "doFrame: " + FpsMonitor.getInstance().getFps());
                                mData.getPercentList().push((float) FpsMonitor.getInstance().getFps());
                                FpsMonitor.getInstance().resetFps();
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                Choreographer.getInstance().postFrameCallback(this);
                            }
                        }
                    });
                }
            }
        });
    }

    private static class FpsMonitor {

        private static FpsMonitor sInstance = new FpsMonitor();
        private static final long TIME_BLOCK = 1000000000L;

        private int fps = 60;
        private long subTime;
        private long preTime;

        static FpsMonitor getInstance() {
            return sInstance;
        }

        void calculateRemainTime(long remainTime) {
            subTime = TIME_BLOCK - (remainTime - preTime);
            fps++;
        }

        boolean isTimeToReset(long remainTime) {
            if (subTime <= 0) {
                subTime = TIME_BLOCK;
                preTime = remainTime;
                return true;
            } else {
                return false;
            }
        }

        int getFps() {
            return fps;
        }

        void resetFps() {
            fps = 0;
        }
    }
}
