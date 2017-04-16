package com.yellow5a5.crashanalysis.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yellow5a5.crashanalysis.R;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

public class CrashInfoDialog extends Dialog {

    private TextView mCrashContentTv;
    private Button mLeftBtn;
    private Button mRightBtn;

    public interface CrashDialogCallback{
        void onSaveBtnClick();
        void onShareBtnClick();
    }

    private CrashDialogCallback mCrashDialogCallback;

    public void setCrashDialogCallback(CrashDialogCallback l){
        mCrashDialogCallback = l;
    }

    public CrashInfoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.crash_dislay_layout, null, false);
        mCrashContentTv = (TextView) view.findViewById(R.id.dialo_crash_content_text);
        mLeftBtn = (Button) findViewById(R.id.dialog_crash_save_btn);
        mRightBtn = (Button) findViewById(R.id.dialog_crash_share_btn);
        setContentView(view);
        initListener();
        mLeftBtn.setClickable(false);
    }

    private void initListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCrashDialogCallback == null){
                    return;
                }

                if (v == mLeftBtn){
                    mCrashDialogCallback.onSaveBtnClick();

                } else if (v == mRightBtn){
                    mCrashDialogCallback.onShareBtnClick();
                }
            }
        };
        mLeftBtn.setOnClickListener(listener);
        mRightBtn.setOnClickListener(listener);
    }

    public void setCrashContent(String text){
        mCrashContentTv.setText(text);
    }

    public void setSaveingState(){
        mLeftBtn.setText("");
        mLeftBtn.setVisibility(View.GONE);
    }

    public void setCompleteState(){
        mLeftBtn.setText(R.string.dialog_crash_btn_saved_text);
        mRightBtn.setVisibility(View.VISIBLE);
    }

    public void setLeftClickable(boolean isClickable){
        mLeftBtn.setClickable(isClickable);
    }

    public void setRightClickable(boolean isClickable){
        mRightBtn.setClickable(isClickable);
    }
}
