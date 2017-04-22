package com.yellow5a5.crashanalysis.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

    private TextView mTitleTv;
    private TextView mCrashContentTv;
    private Button mLeftBtn;
    private Button mShareBtn;
    private ImageView mCloseV;


    public interface CrashDialogCallback {
        void onCloseBtnClick();

        void onShareBtnClick();
    }

    private CrashDialogCallback mCrashDialogCallback;

    public void setCrashDialogCallback(CrashDialogCallback l) {
        mCrashDialogCallback = l;
    }

    private CrashInfoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void createViewInner(View view) {
        setContentView(view);
        mTitleTv = (TextView) findViewById(R.id.dialog_crash_title);
        mCrashContentTv = (TextView) view.findViewById(R.id.dialog_crash_content_text);
        mLeftBtn = (Button) view.findViewById(R.id.dialog_crash_save_btn);
        mShareBtn = (Button) view.findViewById(R.id.dialog_crash_share_btn);
        mCloseV = (ImageView) view.findViewById(R.id.dialog_crash_close_iv);
        mShareBtn.setVisibility(View.VISIBLE);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCrashDialogCallback == null) {
                    return;
                }
                if (v == mShareBtn) {
                    mCrashDialogCallback.onShareBtnClick();
                } else if (v == mCloseV) {
                    mCrashDialogCallback.onCloseBtnClick();
                    dismiss();
                }
            }
        };
        mLeftBtn.setOnClickListener(listener);
        mShareBtn.setOnClickListener(listener);
        mCloseV.setOnClickListener(listener);
        mCrashContentTv.setHorizontallyScrolling(true);
        mCrashContentTv.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void setCrashContent(String text) {
        mCrashContentTv.setText(text);
    }

    public void setSaveingState() {
        mLeftBtn.setText("");
        mLeftBtn.setVisibility(View.GONE);
    }

    public void setCompleteState() {
        mTitleTv.setText(R.string.dialog_crash_success_title);
//        mLeftBtn.setText(R.string.dialog_crash_btn_saved_text);
//        mLeftBtn.setVisibility(View.VISIBLE);
//        mShareBtn.setVisibility(View.VISIBLE);
//        Toast.makeText(getContext(), R.string.dialog_crash_btn_saved_text, Toast.LENGTH_LONG).show();

    }

    public void setFailtureState() {
        mTitleTv.setText(R.string.dialog_crash_failture_title);
//        mLeftBtn.setText(R.string.dialog_crash_btn_save_fail_text);
//        mLeftBtn.setVisibility(View.VISIBLE);
//        mShareBtn.setVisibility(View.VISIBLE);
//        Toast.makeText(getContext(), R.string.dialog_crash_btn_save_fail_text, Toast.LENGTH_LONG).show();
    }

    public void setLeftClickable(boolean isClickable) {
        mLeftBtn.setClickable(isClickable);
    }

    public void setRightClickable(boolean isClickable) {
        mShareBtn.setClickable(isClickable);
    }

    public static class Builder {

        private Context context;
        private CrashInfoDialog dialog;
        private String crashContent;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setCrashContent(String content) {
            crashContent = content;
            return this;
        }

        public CrashInfoDialog build() {
            dialog = new CrashInfoDialog(context);
            dialog.setCanceledOnTouchOutside(false);
            setupView();
            return dialog;
        }

        private void setupView() {
            View view = LayoutInflater.from(context).inflate(R.layout.crash_dislay_layout, null, false);
            TextView crashContentTv = (TextView) view.findViewById(R.id.dialog_crash_content_text);
            final Button saveBtn = (Button) view.findViewById(R.id.dialog_crash_save_btn);
            final Button shareBtn = (Button) view.findViewById(R.id.dialog_crash_share_btn);
            saveBtn.setClickable(false);
            if (crashContent != null) {
                crashContentTv.setText(crashContent);
            }
            dialog.createViewInner(view);

        }

    }
}
