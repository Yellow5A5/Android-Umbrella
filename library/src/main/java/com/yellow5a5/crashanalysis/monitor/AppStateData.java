package com.yellow5a5.crashanalysis.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yellow5A5 on 17/3/3.
 */

public class AppStateData<T> {

    public AppStateData(int type, String title) {
        this.type = type;
        this.title = title;
    }

    private int type;

    private String title;

    private FixedQueue<T> percentList;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FixedQueue<T> getPercentList() {
        if (percentList == null){
            percentList = new FixedQueue<T>(30);
        }
        return percentList;
    }

    public void setPercentList(FixedQueue<T> percentList) {
        this.percentList = percentList;
    }
}
