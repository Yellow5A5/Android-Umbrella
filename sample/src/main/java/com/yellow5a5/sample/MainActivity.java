package com.yellow5a5.sample;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ApplicationErrorReport;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yellow5a5.crashanalysis.View.CrashDisplayDialog;

public class MainActivity extends AppCompatActivity {

    private Button mBtn1;

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                mBtn1 = (Button) findViewById(R.id.demo_btn1);

        initListener();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(this, 2000);
//                throw new ArrayIndexOutOfBoundsException();
                Toast.makeText(MainActivity.this, flag++ + "", Toast.LENGTH_SHORT).show();
            }
        }, 2000);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        CrashDisplayDialog dialog = new CrashDisplayDialog();

                        dialog.show();

                        Looper.loop();
                    }
                }).start();
            }
        });
    }

    private void initListener() {
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new ArrayIndexOutOfBoundsException();
            }
        });
    }
}
