package com.yellow5a5.crashanalysis.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellow5a5.crashanalysis.adapter.CrashLogAdapter;
import com.yellow5a5.crashanalysis.Umbrella;
import com.yellow5a5.crashanalysis.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Created by Yellow5A5 on 17/4/19.
 */

public class LogCenterFragment extends Fragment {

    private View mView;
    private RecyclerView mLogRecycleView;
    private CrashLogAdapter mCrashLogAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static LogCenterFragment newInstance() {
        LogCenterFragment f = new LogCenterFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tvirus_center, container, false);
        initView();
        initListener();
        initData();
        return mView;
    }

    private void initView() {
        mLogRecycleView = (RecyclerView) mView.findViewById(R.id.crash_log_recycler_view);
    }

    private void initListener() {

    }

    private void initData() {
        File logDir = new File(Umbrella.getInstance().getCrashConfig().getLocalPath());
        if (!logDir.exists() || !logDir.isDirectory()) {
            return;
        }
        File[] fileList = logDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String appName = Umbrella.getInstance().getCrashConfig().getAppName();
                if (TextUtils.isEmpty(appName) || pathname.toString().contains(appName)){
                return true;
                } else {
                    return false;
                }
            }
        });


        ArrayList<String> data = new ArrayList<>();

        for (File item : fileList) {
            data.add(item.getName());
        }
        mCrashLogAdapter = new CrashLogAdapter(getActivity());
        mLogRecycleView.setItemAnimator(new DefaultItemAnimator());
        mLogRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLogRecycleView.setAdapter(mCrashLogAdapter);
        mCrashLogAdapter.setData(data);
    }
}
