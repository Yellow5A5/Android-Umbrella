package com.yellow5a5.crashanalysis.monitor;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yellow5a5.crashanalysis.R;

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
    private TextView mCpuTv;
    private TextView mRamTv;
    private TextView mFpsTv;
    private TextView mNetworkTv;

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
        mMonitorWindow = inflater.inflate(R.layout.window_monitor_layout, null);
        mFpsTv = (TextView) mMonitorWindow.findViewById(R.id.window_monitor_fps_tv);
        mCpuTv = (TextView) mMonitorWindow.findViewById(R.id.window_monitor_cpu_tv);
        mRamTv = (TextView) mMonitorWindow.findViewById(R.id.window_monitor_ram_tv);
        mNetworkTv = (TextView) mMonitorWindow.findViewById(R.id.window_monitor_newwork_tv);
        mInflater = LayoutInflater.from(mContext);


        //Window Install.
        initLayoutParams();

        //Protecting.(if config is null)
        setConfig(mConfig);

        //Orz Function Setup.
        initOrzByConfig();

        //DetailView Install.
        installDetailPager();

        //Start update.
//        mHandle.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mHandle.postDelayed(this, mConfig.getTimeBlock());
//            }
//        }, mConfig.getTimeBlock());
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
        mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mWindowLayoutParams.format = PixelFormat.RGBA_8888;
        mWindowLayoutParams.flags = // Allows the view to be on top of the StatusBar
                // Keeps the button presses from going to the background window
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        // Enables the notification to recieve touch events
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        // Draws over status bar
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        mWindowLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowLayoutParams.x = 0;
        mWindowLayoutParams.y = 0;
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

    }

    private void initOrzByConfig() {
        for (Integer type : mConfig.getOrzStateList()) {
            switch (type) {
                case Constant.TYPE_CPU:
                    CpuOrz cpuObs = new CpuOrz(Constant.TYPE_CPU, "CPU");
                    cpuObs.bindView(mCpuTv);
                    mQueue.registerObserver(cpuObs);
                    mListData.add(cpuObs.getData());
                    break;
                case Constant.TYPE_MEMORY:
                    MemOrz memObs = new MemOrz(Constant.TYPE_MEMORY, "MEM");
                    memObs.bindView(mRamTv);
                    mQueue.registerObserver(memObs);
                    mListData.add(memObs.getData());
                    break;
                case Constant.TYPE_FPS:
                    FpsOrz fpsOrz = new FpsOrz(Constant.TYPE_FPS, "FPS");
                    fpsOrz.bindView(mFpsTv);
                    mQueue.registerObserver(fpsOrz);
                    mListData.add(fpsOrz.getData());
                    break;
                case Constant.TYPE_NETWORK:
                    NetworkOrz networkOrz = new NetworkOrz(Constant.TYPE_NETWORK, "SPEED");
                    mQueue.registerObserver(networkOrz);
                    networkOrz.bindView(mNetworkTv);
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
