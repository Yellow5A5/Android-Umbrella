package com.yellow5a5.crashanalysis.monitor;

import android.util.Log;

/**
 * Created by Yellow5A5 on 17/3/5.
 */

public class MemOrz extends IOrz {


    public MemOrz(int type, String title) {
        super(type, title);
    }

    @Override
    public void update() {
        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long freeMemInMB = runtime.freeMemory() / 1048576L;
        final long maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
        mData.getPercentList().push((float) (freeMemInMB * 100 / maxHeapSizeInMB));
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mBindView != null) {
                    mBindView.setText(String.valueOf(usedMemInMB) + "M");
                }
            }
        });
    }
}
