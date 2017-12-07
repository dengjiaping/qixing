package com.qixing.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.fragment.subfragment.SubFragment3;
import com.qixing.fragment.subfragment.SubFragment4;
import com.qixing.fragment.subfragment.SubFragment5;
import com.qixing.view.titlebar.BGATitlebar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MyBillActivity extends AppCompatActivity {

    private BGATitlebar mTitlebar;

    private static Context context;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private String[] titles={"充值记录","送出礼物"};
    private MyAdapter myAdapter;

    private ViewGroup view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bill);

        context=MyBillActivity.this;
        initStatusbar(context,R.color.color_titlebar_default);

        initView();
    }

    private void initView(){
        mTitlebar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitlebar.setTitleText("我的账单");
        mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                finish();
            }
        });

        mTabLayout= (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager= (ViewPager) findViewById(R.id.viewPager);

        fragments=new ArrayList<>();
        fragments.add(new SubFragment5());
        fragments.add(new SubFragment4());
        myAdapter=new MyAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(myAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private void initStatusbar(Context context, int statusbar_bg) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window window = ((Activity) context).getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            view = (ViewGroup) ((Activity) context).getWindow().getDecorView();
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight());

            TextView textView = new TextView(this);
            textView.setBackgroundResource(statusbar_bg);
            textView.setLayoutParams(lParams);
            view.addView(textView);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
