package com.yellow5a5.sample;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yellow5a5.crashanalysis.activity.UmbrellaCorporationActivity;

public class MainActivity extends AppCompatActivity {

    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn1 = (Button) findViewById(R.id.demo_btn1);
        mBtn2 = (Button) findViewById(R.id.demo_btn2);
        mBtn3 = (Button) findViewById(R.id.demo_btn3);

        Log.e(MainActivity.class.getName(), "onCreate: MainActivity");
        initListener();

    }

    private void initListener() {
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Thread subThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                        throw new ArrayIndexOutOfBoundsException();
//                    }
//                });
//                CrashAnalysisCenter.getInstance().setTarget(subThread);
//                subThread.start();
            }
        });

        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, UmbrellaCorporationActivity.class);
                startActivity(intent);
            }
        });
    }
}
