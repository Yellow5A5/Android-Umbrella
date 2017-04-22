package com.yellow5a5.crashanalysis.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yellow5a5.crashanalysis.R;

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
        return new LogViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_log_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mData == null || mData.size() < position){
            return;
        }
        LogViewHolder logHolder = (LogViewHolder) holder;
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

        private ImageView imageView;
        private TextView dateTimeView;

        private LogViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.adapter_log_icon_iv);
            dateTimeView = (TextView) itemView.findViewById(R.id.adapter_log_date_time_text);
        }
    }
}
