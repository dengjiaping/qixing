package com.qixing.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.adapter.DialogGiftAdapter;
import com.qixing.adapter.DialogRechargeAdapter;
import com.qixing.adapter.MyRebateAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.GiftBean;
import com.qixing.bean.MyRebateBean;
import com.qixing.bean.RechargeBean;
import com.qixing.bean.RechargeJson;
import com.qixing.global.Constant;
import com.qixing.qxlive.gift.CustomRoundView;
import com.qixing.qxlive.gift.GiftDateUtlis;
import com.qixing.qxlive.gift.MagicTextView;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.MyGridView;
import com.qixing.view.MyListView;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 */
public class TestViewPagerActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private TextView tv_content, tv_phone;
    private Button btn_call;

    private int p = 1;

    private String state = "1";

    private MyListView mListView;

    private List<MyRebateBean> list;
    private MyRebateAdapter mMyRebateAdapter;

//    private JoinBean mJson;


    private TextView tvSendone;
    private TextView tvSendtwo;
    private TextView tvSendthree;
    private TextView tvSendfor;


    //选项卡
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private TextView videoLayout;
    private TextView musicLayout;
    // 滚动条图片
    private ImageView scrollbar;
    // 滚动条初始偏移量
    private int offset = 0;
    // 当前页编号
    private int currIndex = 0;
    // 滚动条宽度
    private int bmpW;
    //一倍滚动量
    private int one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_test_viewpager, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        initView();
//        initDatas();
        initTestDate();
        initViewPage();
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("测试");
        mTitleBar.setDelegate(new BGATitlebar.BGATitlebarDelegate() {

            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }


            @Override
            public void onClickRightCtv() {
                super.onClickRightCtv();
            }
        });

        tv_content = (TextView) findViewById(R.id.join_tv_content);
        tv_phone = (TextView) findViewById(R.id.join_tv_phone);
        btn_call = (Button) findViewById(R.id.join_btn_call);
        btn_call.setOnClickListener(this);

        mListView = (MyListView)findViewById(R.id.mListView);
        mListView.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }

    private void initViewPage(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater =getLayoutInflater();
        View view1 = inflater.inflate(R.layout.layout_qxlive_see2_chat, null);
        View view2 = inflater.inflate(R.layout.layout_qxlive_see2_rank, null);
        videoLayout = (TextView)findViewById(R.id.videoLayout);
        musicLayout = (TextView)findViewById(R.id.musicLayout);
        scrollbar = (ImageView)findViewById(R.id.scrollbar);
        videoLayout.setOnClickListener(this);
        musicLayout.setOnClickListener(this);
        pageview =new ArrayList<View>();
        //添加想要切换的界面
        pageview.add(view1);
        pageview.add(view2);
        //数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter(){

            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0==arg1;
            }
            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageview.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1){
                ((ViewPager)arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }
        };
        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        //设置viewPager的初始界面为第一个界面
        viewPager.setCurrentItem(0);
        //添加切换界面的监听器
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        // 获取滚动条的宽度
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.icon_info_close).getWidth();
        //为了获取屏幕宽度，新建一个DisplayMetrics对象
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //将当前窗口的一些信息放在DisplayMetrics类中
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //得到屏幕的宽度
        int screenW = displayMetrics.widthPixels;
        //设置滚动条imgview的宽度
        ViewGroup.LayoutParams para = scrollbar.getLayoutParams();
        para.width = screenW / 2;
        scrollbar.setLayoutParams(para);

        //计算出滚动条初始的偏移量
//        offset = (screenW / 2 - bmpW) / 2;
        offset = 0;
        //计算出切换一个界面时，滚动条的位移量
//        one = offset * 2 + bmpW;
        one = screenW / 2;

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        //将滚动条的初始位置设置成与左边界间隔一个offset
        scrollbar.setImageMatrix(matrix);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/
                    animation = new TranslateAnimation(one, 0, 0, 0);

                    videoLayout.setTextColor(getResources().getColor(R.color.color_bottom_select));
                    musicLayout.setTextColor(getResources().getColor(R.color.black));
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    videoLayout.setTextColor(getResources().getColor(R.color.black));
                    musicLayout.setTextColor(getResources().getColor(R.color.color_bottom_select));
                    break;
            }
            //arg0为切换到的页的编码
            currIndex = arg0;
            // 将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            //滚动条开始动画
            scrollbar.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private void initDatas() {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
//        params.put("uid", MyApplication.getInstance().getUid());
//        params.put("status", "2");//+"/p/"+p
//        final String url = Constant.BASE_URL + Constant.URL_USERAPI_DOU_MONEY;
        params.put("phone", "18612523501");
        final String url = "http://app.qexic.com/app.php/User/ajaxphone/type/1";
        //http://app.qexic.com/index.php/User/ajaxphone/type/1/phone/18612523501
        System.out.println("===========================个人中心 测试 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================个人中心 测试 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {

                } else {
                    showErrorDialog(mContext);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.join_btn_call:
//                showGiftDialog();
                initDatas();
                break;
            case R.id.tvSendone:/*礼物1*/
//                showGift("Johnny1");
                break;
            case R.id.videoLayout:
                //点击"视频“时切换到第一页
                viewPager.setCurrentItem(0);
                break;
            case R.id.musicLayout:
                //点击“音乐”时切换的第二页
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    private void initTestDate(){
        list = new ArrayList<MyRebateBean>();
        for(int i=0 ; i<15;i++){
            MyRebateBean mMyRebateBean = new MyRebateBean();
            mMyRebateBean.setNum(i+1+"");
            mMyRebateBean.setTime("2016-12-07");
            mMyRebateBean.setName("七星时代");
            mMyRebateBean.setLv("普通会员");
            list.add(mMyRebateBean);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
