package com.yellow5a5.crashanalysis.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Yellow5A5 on 17/5/25.
 */

public class BatteryOrz extends IOrz {
    BatteryOrz(int type, String title) {
        super(type, title);
    }

    @Override
    public void update() {

    }


    private static class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //判断它是否是为电量变化的Broadcast Action
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //电量的总刻度
                int scale = intent.getIntExtra("scale", 100);
                //把它转成百分比
                Log.d(BatteryReceiver.class.getName(), "电池电量为" + ((level * 100) / scale) + "%");
            }
        }

    }
}
