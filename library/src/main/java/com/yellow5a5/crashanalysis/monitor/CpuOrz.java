package com.yellow5a5.crashanalysis.monitor;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Yellow5A5 on 17/3/4.
 */

public class CpuOrz extends IOrz {

    public CpuOrz(int type, String title) {
        super(type, title);
    }

    @Override
    public void update() {
        int percent = getProcessCpuRate();
        mData.getPercentList().push((float) percent);
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mBindView != null) {
                    mBindView.setText(String.valueOf(mData.getPercentList().get() + "%"));
                }
            }
        });
    }

    /** get CPU rate
     * @return
     */
    public static int getProcessCpuRate() {

        StringBuilder tv = new StringBuilder();
        int rate = 0;

        try {
            String Result;
            Process p;
            p = Runtime.getRuntime().exec("top -n 1");

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((Result = br.readLine()) != null) {
                Log.d(CpuOrz.class.getName(), Result);
                if (Result.trim().length() < 1) {
                    continue;
                } else {
                    String[] CPUusr = Result.split("%");
                    String[] CPUusage = CPUusr[0].split("User");
                    String[] SYSusage = CPUusr[1].split("System");
                    rate = Integer.parseInt(CPUusage[1].trim()) + Integer.parseInt(SYSusage[1].trim());
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rate;
    }
}
