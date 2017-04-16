package com.yellow5a5.crashanalysis.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yellow5a5.crashanalysis.R;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

public class CrashInfoDialog extends Dialog {

    private TextView mCrashContentTv;
    private Button mLeftBtn;
    private Button mRightBtn;
    private ImageView mCloseV;

    public interface CrashDialogCallback{
        void onSaveBtnClick();
        void onShareBtnClick();
    }

    private CrashInfoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void createViewInner(View view){
        setContentView(view);
        mCrashContentTv = (JustifyTextView) view.findViewById(R.id.dialo_crash_content_text);
        mLeftBtn = (Button) findViewById(R.id.dialog_crash_save_btn);
        mRightBtn = (Button) findViewById(R.id.dialog_crash_share_btn);
        mCloseV = (ImageView) findViewById(R.id.dialog_crash_close_iv);
        mCloseV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
        mLeftBtn.setVisibility(View.VISIBLE);
        mRightBtn.setVisibility(View.VISIBLE);
    }

    public void setLeftClickable(boolean isClickable){
        mLeftBtn.setClickable(isClickable);
    }

    public void setRightClickable(boolean isClickable){
        mRightBtn.setClickable(isClickable);
    }

    public static class Builder{

        private Context context;
        private CrashInfoDialog dialog;
        private String crashContent;

        private CrashDialogCallback mCrashDialogCallback;

        public void setCrashDialogCallback(CrashDialogCallback l){
            mCrashDialogCallback = l;
        }

        public Builder(Context context){
            this.context = context;
        }

        public Builder setCrashContent(String content){
            crashContent = content;
            return this;
        }

        public CrashInfoDialog build(){
            dialog = new CrashInfoDialog(context);
            dialog.setCanceledOnTouchOutside(false);
            setupView();
            return dialog;
        }

        private void setupView() {
            View view = LayoutInflater.from(context).inflate(R.layout.crash_dislay_layout, null, false);
            TextView crashContentTv = (TextView) view.findViewById(R.id.dialo_crash_content_text);
            final Button saveBtn = (Button) view.findViewById(R.id.dialog_crash_save_btn);
            final Button shareBtn = (Button) view.findViewById(R.id.dialog_crash_share_btn);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCrashDialogCallback == null){
                        return;
                    }
                    if (v == saveBtn){
                        mCrashDialogCallback.onSaveBtnClick();

                    } else if (v == shareBtn){
                        mCrashDialogCallback.onShareBtnClick();
                    }
                }
            };
            //TODO
            saveBtn.setOnClickListener(listener);
            shareBtn.setOnClickListener(listener);
            saveBtn.setClickable(false);
            if (crashContent != null){
                crashContentTv.setText(crashContent);
            }
            dialog.createViewInner(view);

        }

    }
}
