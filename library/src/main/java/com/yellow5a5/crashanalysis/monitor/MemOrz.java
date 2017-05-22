package com.yellow5a5.crashanalysis.monitor;

/**
 * Created by Yellow5A5 on 17/3/5.
 */

public class MemOrz extends IOrz {


    public MemOrz(int type, String title) {
        super(type, title);
    }

    @Override
    public void update() {
//        ActivityManager mActivityManager = (ActivityManager) App.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
//        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
//        mActivityManager.getMemoryInfo(outInfo);
//        outInfo...
        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long freeMemInMB = runtime.freeMemory() / 1048576L;
        final long maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
        mData.getPercentList().add((int) (freeMemInMB * 100 / maxHeapSizeInMB));
//        Log.e(MemoryObserver.class.getName(), "update: " + freeMemInMB + " " + maxHeapSizeInMB);
    }
}
