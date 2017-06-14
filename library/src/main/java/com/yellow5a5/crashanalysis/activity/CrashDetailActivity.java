package com.yellow5a5.crashanalysis.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.yellow5a5.crashanalysis.R;
import com.yellow5a5.crashanalysis.core.CrashInfoHelper;

import java.util.ArrayList;

/**
 * Created by Yellow5A5 on 17/4/25.
 */

public class CrashDetailActivity extends Activity {


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
        logdetailexceptiontext.setText(exception);

        if (list.size() <= 1){
            return;
        }
        String classPosition = pullAwayClassPosition(list.get(1));
        String method = pullAwayMethod(list.get(1));
        logdetailclasstext.setText(classPosition);
        logdetailmethodtext.setText(method);
        StringBuilder builder = new StringBuilder();
        for (String line : list) {
            builder.append(line);
            builder.append("\n");
        }
        logdetaillinetext.setText(builder);
    }

    private boolean isContainMethodSymbol(String ex){
        if (!TextUtils.isEmpty(ex) && ex.contains("$")){
            return true;
        } else {
            return false;
        }
    }

    private String pullAwayMethod(String ex){
        if(isContainMethodSymbol(ex)){
            String method = ex.substring(ex.indexOf("$"), ex.length());
            if (method.contains(".") && method.contains("(")){
                method = method.substring(method.indexOf("."), method.indexOf("("));
                return method;
            }
        }
        return "";
    }

    private String pullAwayClassPosition(String ex) {
        String position = "";
        if (isContainMethodSymbol(ex)) {
            position = ex.substring(0, ex.indexOf("$"));
        } else if(ex.contains("(")) {
            position = ex.substring(0, ex.indexOf("("));
        }
        //移除at 前缀
        if (position.startsWith("\tat ")){
            position = position.replace("\tat ", "");
        }
        if (ex.contains(":")){
            String methodLine = "(行数" + ex.substring(ex.indexOf(":"), ex.length());
            position += methodLine;
        }
        return position;
    }
}
