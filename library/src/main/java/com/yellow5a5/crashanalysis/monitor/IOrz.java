package com.yellow5a5.crashanalysis.monitor;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

/**
 * Created by Yellow5A5 on 17/3/4.
 */

public abstract class IOrz {

    TextView mBindView;
    AppStateData<Float> mData;
    IStateCallback mCallback;
    static Handler sHandler = new Handler(Looper.getMainLooper());

    IOrz(int type, String title) {
        mData = new AppStateData<Float>(type, title);
    }

    public void bindView(TextView view) {
        mBindView = view;
    }

    public abstract void update();

    public AppStateData<Float> getData() {
        return mData;
    }

    public void setStateCallback(IStateCallback callback) {
        mCallback = callback;
    }

}
