package com.yellow5a5.crashanalysis.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellow5a5.crashanalysis.R;

/**
 * Created by Yellow5A5 on 17/4/19.
 */

public class TvirusCenterFragment extends Fragment{

    private View mView;
    private RecyclerView mLogRecycleView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TvirusCenterFragment newInstance() {
        TvirusCenterFragment f = new TvirusCenterFragment();
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

    }
}
