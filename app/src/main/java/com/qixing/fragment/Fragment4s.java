package com.qixing.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qixing.R;
import com.qixing.fragment.customer.PagerSlidingTabStrip;
import com.qixing.fragment.subfragment.SubFragment1;
import com.qixing.fragment.subfragment.SubFragment2;
import com.qixing.fragment.subfragment.SubFragment3;
import com.qixing.widget.Toasts;

public class Fragment4s extends Fragment {

    private SubFragment1 subFragment1;
    private SubFragment2 subFragment2;
    private SubFragment3 subFragment3;
    /**
     * PagerSlidingTabStrip的实例
     */
    private PagerSlidingTabStrip tabs;

    /**
     * 获取当前屏幕的密度
     */
    private DisplayMetrics dm;
    private int tag=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {// 在前面执行

        super.onCreate(savedInstanceState);
        // 获取参数
        Bundle bundle = getArguments();
        if (null != bundle) {
            //
            tag=bundle.getInt("tag",0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Toasts.show("FragmentMessage==onCreateView");
        View view = inflater.inflate(R.layout.fragment4s, null);

        initView(view);

        return view;
    }

    private void initView(View view) {

        dm = getResources().getDisplayMetrics();
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        tabs.setViewPager(pager);
        setTabsValue();

        pager.setCurrentItem(tag);
        System.out.println("================================Fragment4s tag==========="+tag);
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {

//        pstsIndicatorColor Color of the sliding indicator  滑动条的颜色
//        pstsUnderlineColor Color of the full-width line on the bottom of the view  滑动条所在的那个全宽线的颜色
//        pstsDividerColor Color of the dividers between tabs   每个标签的分割线的颜色
//        pstsIndicatorHeightHeight of the sliding indicator       滑动条的高度
//        pstsUnderlineHeight Height of the full-width line on the bottom of the view    滑动条所在的那个全宽线的高度
//        pstsDividerPadding Top and bottom padding of the dividers   分割线底部和顶部的填充宽度
//        pstsTabPaddingLeftRight Left and right padding of each tab   每个标签左右填充宽度
//        pstsScrollOffset Scroll offset of the selected tab
//        pstsTabBackground Background drawable of each tab, should be a StateListDrawable  每个标签的背景，应该是一个StateListDrawable
//        pstsShouldExpand If set to true, each tab is given the same weight, default false   如果设置为true，每个标签是相同的控件，均匀平分整个屏幕，默认是false
//        pstsTextAllCaps If true, all tab titles will be upper case, default true   如果为true，所有标签都是大写字母，默认为true

        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // tabs.setDividerColor(Color.BLACK);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, dm));// 4
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm)); // 16
        // 设置Tab Indicator的颜色       设置Tab底部线的颜色
//        tabs.setIndicatorColor(Color.parseColor("#45c01a"));// #45c01a
        tabs.setIndicatorColor(getResources().getColor(R.color.white_F));// #45c01a
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(getResources().getColor(R.color.white_F));// #45c01a
        tabs.setTextColor(getResources().getColor(R.color.white_F));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }

    // FragmentPagerAdapter FragmentStatePagerAdapter //不能用FragmentPagerAdapter


    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        private final String[] titles = {"七星资讯", "七星干货", "七星营销"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    if (null == subFragment1) {
                        subFragment1 = new SubFragment1();
                    }

                    return subFragment1;

                case 1:

                    if (null == subFragment2) {
                        subFragment2 = new SubFragment2();
                    }

                    return subFragment2;
                case 2:
                    if (null == subFragment3) {
                        subFragment3 = new SubFragment3();
                    }
                    return subFragment3;
                default:
                    return null;
            }
        }

    }
}
