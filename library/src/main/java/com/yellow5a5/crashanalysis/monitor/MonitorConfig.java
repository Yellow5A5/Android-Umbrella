package com.yellow5a5.crashanalysis.monitor;

import java.util.ArrayList;

/**
 * Created by Yellow5A5 on 17/3/7.
 */

public class MonitorConfig {

    //default block is 1s.
    private int timeBlock = 1000;

    private ArrayList<Integer> orzState = new ArrayList<>();

    public MonitorConfig addOrz(int type){
        orzState.add(type);
        return this;
    }

    public MonitorConfig openCpuOrz(){
        orzState.add(Constant.TYPE_CPU);
        return this;
    }


    public MonitorConfig openFpsOrz(){
        orzState.add(Constant.TYPE_MEMORY);
        return this;
    }


    public MonitorConfig openMemoryOrz(){
        orzState.add(Constant.TYPE_FPS);
        return this;
    }

    public MonitorConfig setTimeBlock(int timeBlock){
        this.timeBlock = timeBlock;
        return this;
    }

    public MonitorConfig defaultConfig(){
        orzState.add(Constant.TYPE_CPU);
        orzState.add(Constant.TYPE_MEMORY);
        orzState.add(Constant.TYPE_FPS);
        timeBlock = 1000;
        return this;
    }

    public int getTimeBlock() {
        return timeBlock;
    }

    public ArrayList<Integer> getOrzStateList(){
        return orzState;
    }
}
