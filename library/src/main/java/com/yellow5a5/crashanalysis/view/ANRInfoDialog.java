package com.yellow5a5.crashanalysis.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yellow5a5.crashanalysis.R;

/**
 * Created by Yellow5A5 on 17/4/15.
 */

public class ANRInfoDialog extends Dialog {

    private TextView mTitleTv;
    private TextView mAnrContentTv;
    private Button mLeftBtn;

    public ANRInfoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void createViewInner(View view) {
        setContentView(view);
        mTitleTv = (TextView) findViewById(R.id.dialog_anr_title);
        mAnrContentTv = (TextView) view.findViewById(R.id.dialog_anr_content_text);
        mLeftBtn = (Button) view.findViewById(R.id.dialog_anr_wait_btn);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
        mLeftBtn.setOnClickListener(listener);
        mAnrContentTv.setHorizontallyScrolling(true);
        mAnrContentTv.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public static class Builder {

        private Context context;
        private ANRInfoDialog dialog;
        private String anrContent;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setAnrContent(String content) {
            anrContent = content;
            return this;
        }

        public ANRInfoDialog build() {
            dialog = new ANRInfoDialog(context);
            dialog.setCanceledOnTouchOutside(false);
            setupView();
            return dialog;
        }

        private void setupView() {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_anr_dislay_layout, null, false);
            TextView anrContentTv = (TextView) view.findViewById(R.id.dialog_anr_content_text);
            if (anrContent != null) {
                anrContentTv.setText(anrContent);
            }
            dialog.createViewInner(view);

        }

    }
}
