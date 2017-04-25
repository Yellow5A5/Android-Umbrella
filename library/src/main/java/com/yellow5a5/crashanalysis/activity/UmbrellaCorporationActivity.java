package com.yellow5a5.crashanalysis.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yellow5a5.crashanalysis.R;
import com.yellow5a5.crashanalysis.adapter.OnPageChangeAdapter;
import com.yellow5a5.crashanalysis.fragment.LogCenterFragment;

import java.util.ArrayList;

public class UmbrellaCorporationActivity extends AppCompatActivity {

    private LinearLayout mCrashLogLl;
    private LinearLayout mSettingLl;
    private ViewPager mViewPage;

    private ArrayList<Fragment> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umbrella_corporation);
        data.add(LogCenterFragment.newInstance());
        data.add(LogCenterFragment.newInstance());
        initView();
        initListener();

    }

    private void initView() {
        mViewPage = (ViewPager) findViewById(R.id.umbrella_viewpager_container);
        mCrashLogLl = (LinearLayout) findViewById(R.id.umbrella_bottom_log_btn);
        mSettingLl = (LinearLayout) findViewById(R.id.umbrella_bottom_settind_btn);
    }

    private void initListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mCrashLogLl) {
                    mViewPage.setCurrentItem(0);
                } else if (v == mSettingLl) {
                    mViewPage.setCurrentItem(1);
                }
            }
        };
        mCrashLogLl.setOnClickListener(listener);
        mSettingLl.setOnClickListener(listener);

        mViewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return data.get(position);
            }

            @Override
            public int getCount() {
                return data.size();
            }
        });

        mViewPage.addOnPageChangeListener(new OnPageChangeAdapter(0) {

            @Override
            public void onPageSelected(int lastposition, int position) {

            }
        });

    }
}
