package com.yellow5a5.crashanalysis.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yellow5a5.crashanalysis.R;
import com.yellow5a5.crashanalysis.Adapter.OnPageChangeAdapter;
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
        initView();
        initListener();

        data.add(LogCenterFragment.newInstance());
        data.add(LogCenterFragment.newInstance());
    }

    private void initView() {
        mViewPage = (ViewPager) findViewById(R.id.umbrella_viewpager_container);
        LinearLayout mCrashLogLl = (LinearLayout) findViewById(R.id.umbrella_bottom_log_btn);
        LinearLayout mSettingLl = (LinearLayout) findViewById(R.id.umbrella_bottom_settind_btn);
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
