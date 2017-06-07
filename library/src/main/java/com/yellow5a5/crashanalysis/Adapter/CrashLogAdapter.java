package com.yellow5a5.crashanalysis.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yellow5a5.crashanalysis.R;
import com.yellow5a5.crashanalysis.activity.CrashDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yellow5A5 on 17/4/22.
 */

public class CrashLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;

    private List<String> mData = new ArrayList<>();

    public CrashLogAdapter(Context context){
        mContext = context;
    }

    public void setData(List<String> data){
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_log_item_view, parent, false);
        final LogViewHolder holder = new LogViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.position < mData.size()){
                    Bundle bundle = new Bundle();
                    bundle.putString("PATH_DETAIL", mData.get(holder.position));
                    Intent intent = new Intent(mContext, CrashDetailActivity.class);
                    intent.putExtras(bundle);
                    if (mContext instanceof Activity){
                        mContext.startActivity(intent);
                    }
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mData == null || mData.size() < position){
            return;
        }
        LogViewHolder logHolder = (LogViewHolder) holder;
        logHolder.position = position;
        logHolder.dateTimeView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if (mData != null){
            return mData.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder{

        int position;
        ImageView imageView;
        TextView dateTimeView;

        private LogViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.adapter_log_icon_iv);
            dateTimeView = (TextView) itemView.findViewById(R.id.adapter_log_date_time_text);
        }
    }
}
