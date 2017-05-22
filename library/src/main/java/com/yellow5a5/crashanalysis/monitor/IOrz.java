package com.yellow5a5.crashanalysis.monitor;

/**
 * Created by Yellow5A5 on 17/3/4.
 */

public abstract class IOrz {

    AppStateData mData;

    IStateCallback mCallback;

    IOrz(int type, String title) {
        mData = new AppStateData(type, title);
    }

    public abstract void update();

        public AppStateData getData() {
        return mData;
    }

    public void setStateCallback(IStateCallback callback) {
        mCallback = callback;
    }

}
