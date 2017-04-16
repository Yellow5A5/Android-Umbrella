package com.yellow5a5.sample;

import android.app.ApplicationErrorReport;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yellow5a5.crashanalysis.CrashAnalysis;
import com.yellow5a5.crashanalysis.CrashInfoHelper;
import com.yellow5a5.crashanalysis.View.CrashInfoDialog;

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

        CrashAnalysis.getInstance().registeredActivity(this);
        CrashAnalysis.getInstance().setTargetToMainThread();
//        Looper.getMainLooper().getThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(final Thread t, final Throwable e) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Looper.prepare();
//                        CrashInfoDialog dialog = new CrashInfoDialog(MainActivity.this);
//                        dialog.show();
//                        Looper.loop();
//                    }
//                }).start();
//            }
//        });

    }

    private void initListener() {
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new ArrayIndexOutOfBoundsException();
//                CrashInfoDialog dialog = new CrashInfoDialog.Builder(MainActivity.this)
//                        .setCrashContent("TSUSUSUUSUASKDSADLASJDLSDA")
//                        .build();
//                dialog.show();
            }
        });
    }
}
