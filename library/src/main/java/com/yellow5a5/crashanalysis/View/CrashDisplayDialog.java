package com.yellow5a5.crashanalysis.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yellow5a5.crashanalysis.R;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

public class CrashDisplayDialog extends Dialog {

    private TextView mCrashContentTv;

    public CrashDisplayDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.crash_dislay_layout, null, false);
        mCrashContentTv = (TextView) view.findViewById(R.id.dialo_crash_content_text);
        setContentView(view);
    }

    public void hideTv(){
        mCrashContentTv.setVisibility(View.GONE);
    }

    public void setCrashContent(String content){
        mCrashContentTv.setText(content);
    }
}
