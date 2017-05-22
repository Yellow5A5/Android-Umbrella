package com.yellow5a5.crashanalysis.monitor;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yellow5A5 on 17/3/7.
 */

public class MonitorRack {

    private Context mContext;
    private View mMonitorWindow;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private WindowManager mWindowManager;
    private LayoutInflater mInflater;
    private DisplayMetrics metrics = new DisplayMetrics();

    private List<View> mViewList = new ArrayList<>();
    private AppStateOwner mQueue = new AppStateOwner();
    private List<AppStateData> mListData = new ArrayList<>();
    private MonitorConfig mConfig;

    private Handler mHandle = new Handler();

    public MonitorRack(Context context) {
        mContext = context;
        createAMonitorView();
    }

    void createAMonitorView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
//        mMonitorWindow = inflater.inflate(R.layout.window_monitor_layout, null);
        mInflater = LayoutInflater.from(mContext);

        //Window Install.
        initLayoutParams();

        //Protecting.(if config is null)
        setConfig(mConfig);

        //Orz Function Setup.
        initOrzByConfig();

        //DetailView Install.
//        installDetailPager();

        //Start update.
        mHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandle.postDelayed(this, mConfig.getTimeBlock());
            }
        }, mConfig.getTimeBlock());
    }

    void setConfig(MonitorConfig config) {
        if (config != null) {
            mConfig = config;
        } else {
            mConfig = new MonitorConfig().defaultConfig();
        }
    }

    public void startNotify() {
        mQueue.start(mConfig.getTimeBlock());
    }

    private void initLayoutParams() {
        mWindowLayoutParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) (mContext.getApplicationContext()).getSystemService(mContext.getApplicationContext().WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        //设置window type
        mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        mWindowLayoutParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        mWindowLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mWindowLayoutParams.x = 0;
        mWindowLayoutParams.y = 0;

        //设置悬浮窗口长宽数据
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private void initOrzByConfig() {
        for (Integer type : mConfig.getOrzStateList()) {
            switch (type) {
                case Constant.TYPE_CPU:
                    CpuOrz cpuObs = new CpuOrz(Constant.TYPE_CPU, "CPU");
                    mQueue.registerObserver(cpuObs);
                    mListData.add(cpuObs.getData());
                    break;
                case Constant.TYPE_MEMORY:
                    MemOrz memObs = new MemOrz(Constant.TYPE_MEMORY, "MEM");
                    mQueue.registerObserver(memObs);
                    mListData.add(memObs.getData());
                    break;
                case Constant.TYPE_FPS:
                    FpsOrz fpsOrz = new FpsOrz(Constant.TYPE_FPS, "FPS");
                    fpsOrz.injectHandler(mHandle);
                    mQueue.registerObserver(fpsOrz);
                    mListData.add(fpsOrz.getData());
                    break;
            }
        }
    }

    private void installDetailPager() {
        mWindowManager.addView(mMonitorWindow, mWindowLayoutParams);
        mMonitorWindow.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }
}
