package com.yellow5a5.crashanalysis.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.yellow5a5.crashanalysis.R;
import com.yellow5a5.crashanalysis.Umbrella;
import com.yellow5a5.crashanalysis.core.CrashInfoHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Yellow5A5 on 17/4/25.
 */

public class LogDetailActivity extends AppCompatActivity {


    private TextView logdetailexceptiontip;
    private TextView logdetailexceptiontext;
    private TextView logdetailclasstip;
    private TextView logdetailclasstext;
    private TextView logdetailmethodtip;
    private TextView logdetailmethodtext;
    private TextView logdetaillinetip;
    private TextView logdetaillinetext;

    private String mPath;
    private ArrayList<String> mContent;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_detail_layout);
        initView();
        resolveIntent();
        resolveFile();
    }

    private void resolveFile() {
        if (TextUtils.isEmpty(mPath)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mContent = CrashInfoHelper.getTextFromFile(mPath);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setContent(mContent);
                    }
                });
            }
        }).start();
    }

    private void initView() {
        this.logdetaillinetext = (TextView) findViewById(R.id.log_detail_line_text);
        this.logdetaillinetip = (TextView) findViewById(R.id.log_detail_line_tip);
        this.logdetailmethodtext = (TextView) findViewById(R.id.log_detail_method_text);
        this.logdetailmethodtip = (TextView) findViewById(R.id.log_detail_method_tip);
        this.logdetailclasstext = (TextView) findViewById(R.id.log_detail_class_text);
        this.logdetailclasstip = (TextView) findViewById(R.id.log_detail_class_tip);
        this.logdetailexceptiontext = (TextView) findViewById(R.id.log_detail_exception_text);
        this.logdetailexceptiontip = (TextView) findViewById(R.id.log_detail_exception_tip);
    }

    private void resolveIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPath = bundle.getString("PATH_DETAIL");
        }
    }

    private void setContent(ArrayList<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        String exception = list.get(0);
        String position = list.get(1).substring(0, list.get(1).indexOf("$"));
        String methodLine = "(行数" + list.get(1).substring(list.get(1).indexOf(":"), list.get(1).length());
        String method = list.get(1).replace(position,"");
        method = method.substring(method.indexOf("."), method.indexOf("("));
        position = (position + methodLine).replace("at ", "");
        logdetailexceptiontext.setText(exception);
        logdetailmethodtext.setText(method);
        logdetailclasstext.setText(position);
        StringBuilder builder = new StringBuilder();
        for (String line : list) {
            builder.append(line);
            builder.append("\n");
        }
        logdetaillinetext.setText(builder);

    }
}
