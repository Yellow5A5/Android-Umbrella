package com.yellow5a5.crashanalysis.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yellow5A5 on 17/3/3.
 */

public class AppStateData {

    public AppStateData(int type, String title) {
        this.type = type;
        this.title = title;
    }

    private int type;

    private String title;

    private List<Integer> percentList;

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

    public List<Integer> getPercentList() {
        if (percentList == null){
            percentList = new ArrayList<>();
        }
        return percentList;
    }

    public void setPercentList(List<Integer> percentList) {
        this.percentList = percentList;
    }
}
