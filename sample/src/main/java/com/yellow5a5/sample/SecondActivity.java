package com.yellow5a5.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.yellow5a5.crashanalysis.Adapter.CrashLogAdapter;
import com.yellow5a5.crashanalysis.CrashAnalysisCenter;
import com.yellow5a5.crashanalysis.activity.UmbrellaCorporationActivity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                throw new NumberFormatException();
                Intent intent = new Intent(SecondActivity.this, UmbrellaCorporationActivity.class);
                startActivity(intent);
            }
        });


        File logDir = new File(CrashAnalysisCenter.getInstance().getCrashConfig().getLocalPath());
        if (!logDir.exists() || !logDir.isDirectory()){
            return;
        }
        File[] fileList = logDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String appName = CrashAnalysisCenter.getInstance().getCrashConfig().getAppName();
                if (TextUtils.isEmpty(appName) || pathname.toString().contains(appName)){
                    return true;
                } else {
                    return false;
                }
            }
        });

        ArrayList<String> data = new ArrayList<>();
        for (File item : fileList){
            data.add(item.getName());
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.test_recycle);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CrashLogAdapter adapter = new CrashLogAdapter(this);
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
