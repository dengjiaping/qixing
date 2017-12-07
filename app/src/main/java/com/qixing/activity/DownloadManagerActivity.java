package com.qixing.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.qixing.R;
import com.qixing.adapter.DownloadManagerAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.DownloadBaseActivity;
import com.qixing.view.titlebar.BGATitlebar;

import java.util.ArrayList;
import java.util.List;

public class DownloadManagerActivity extends DownloadBaseActivity {

    private TabLayout tl;
    private ViewPager vp;
    private BGATitlebar mTitlebar;
    private DownloadManagerAdapter downloadManagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_download_manager);

//        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
    }

    @Override
    protected void initView() {
        mTitlebar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitlebar.setTitleText("下载管理");
        mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                finish();
            }
        });

        tl = (TabLayout) findViewById(R.id.tl);
        vp = (ViewPager) findViewById(R.id.vp);
    }

    @Override
    protected void initData() {
        downloadManagerAdapter = new DownloadManagerAdapter(
                getSupportFragmentManager(), this);

        List<String> strings = new ArrayList<>();
        strings.add("下载中");
        strings.add("已下载");

        downloadManagerAdapter.setData(strings);
        vp.setAdapter(downloadManagerAdapter);
        tl.setupWithViewPager(vp);
        tl.setTabsFromPagerAdapter(downloadManagerAdapter);

    }

}
