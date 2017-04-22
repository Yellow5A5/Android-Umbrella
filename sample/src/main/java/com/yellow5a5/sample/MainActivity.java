package com.yellow5a5.sample;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yellow5a5.crashanalysis.CrashAnalysisCenter;

public class MainActivity extends AppCompatActivity {

    private Button mBtn1;
    private Button mBtn2;

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                mBtn1 = (Button) findViewById(R.id.demo_btn1);
                mBtn2 = (Button) findViewById(R.id.demo_btn2);

        initListener();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(this, 2000);
                Toast.makeText(MainActivity.this, flag++ + "", Toast.LENGTH_SHORT).show();
//                throw new ArrayIndexOutOfBoundsException();
            }
        }, 2000);
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
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }
}
