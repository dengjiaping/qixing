package com.qixing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.qixing.adapter.MyRebateAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.MyRebateBean;
import com.qixing.bean.MyRebateJson;
import com.qixing.bean.VIPJson;
import com.qixing.global.Constant;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.CircleImageView;
import com.qixing.view.MyListView;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 * 会员权益
 */
public class MyRebateActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private PullToRefreshScrollView mScrollView;
    private TextView tv_hy;

    private VIPJson mVipJson;

    private String state = "1";
    private String dengji = "";
    private boolean isRefresh = false;
    private String isPayPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_my_rebate, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        dengji = getIntent().getStringExtra("dengji");
        isPayPwd = getIntent().getStringExtra("isPayPwd");
        initView();
        initDatas();
//        initTestDate();
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("会员权益");
//        mTitleBar.setRightText("报名");
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
        mScrollView = (PullToRefreshScrollView) findViewById(R.id.mScrollview);
		/*
         * Mode.BOTH：同时支持上拉下拉
         * Mode.PULL_FROM_START：只支持下拉Pulling Down
         * Mode.PULL_FROM_END：只支持上拉Pulling Up
         */
        /*
         * 如果Mode设置成Mode.BOTH，需要设置刷新Listener为OnRefreshListener2，并实现onPullDownToRefresh()、onPullUpToRefresh()两个方法。
         * 如果Mode设置成Mode.PULL_FROM_START或Mode.PULL_FROM_END，需要设置刷新Listener为OnRefreshListener，同时实现onRefresh()方法。
         * 当然也可以设置为OnRefreshListener2，但是Mode.PULL_FROM_START的时候只调用onPullDownToRefresh()方法，
         * Mode.PULL_FROM的时候只调用onPullUpToRefresh()方法.
         */
        mScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        init(mScrollView);
		         /*
         * setOnRefreshListener(OnRefreshListener listener):设置刷新监听器；
         * setOnLastItemVisibleListener(OnLastItemVisibleListener listener):设置是否到底部监听器；
         * setOnPullEventListener(OnPullEventListener listener);设置事件监听器；
         * onRefreshComplete()：设置刷新完成
         */
        /*
         * pulltorefresh.setOnScrollListener()
         */
        // SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
        // SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
        // SCROLL_STATE_IDLE(0) 停止滚动
        /*
         * setOnLastItemVisibleListener
         * 当用户拉到底时调用
         */
        /*
         * setOnTouchListener是监控从点下鼠标 （可能拖动鼠标）到放开鼠标（鼠标可以换成手指）的整个过程 ，他的回调函数是onTouchEvent（MotionEvent event）,
         * 然后通过判断event.getAction()是MotionEvent.ACTION_UP还是ACTION_DOWN还是ACTION_MOVE分别作不同行为。
         * setOnClickListener的监控时间只监控到手指ACTION_DOWN时发生的行为
         */
//        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
//            @Override
//            public void onRefresh(PullToRefreshBase refreshView) {
//                isRefresh = true;
//                initDatas();
//            }
//        });
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
//                p = 1;
                isRefresh = true;
                initDatas();
            }


            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
//                isRefresh = true;
//                initMoreDatas();
            }

        });

        tv_hy= (TextView) findViewById(R.id.my_rebate_tv_hyqy);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private void initDatas() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();
        final RequestParams params = new RequestParams();
//        params.put("status", "2");//+"/p/"+p
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_VIPQY;
        System.out.println("===========================个人中心 会员权益url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if(isRefresh){
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }
                System.out.println("===========================个人中心 会员权益 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mVipJson = new Gson().fromJson(response.toString(), VIPJson.class);
                    if (mVipJson.getResult().equals("1")) {
                        tv_hy.setText(mVipJson.getWzinfo());

                    } else {
                        Toasts.show(mVipJson.getMessage());
                    }
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
                if(isRefresh){
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }
                showTimeoutDialog(mContext);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if(isRefresh){
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }
                showErrorDialog(mContext);
            }
        });
    }

    private void init(PullToRefreshScrollView mListView) {
        ILoadingLayout startLabels = mListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = mListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

//      // 设置下拉刷新文本
//      pullToRefresh.getLoadingLayoutProxy(false, true)
//              .setPullLabel("上拉刷新...");
//      pullToRefresh.getLoadingLayoutProxy(false, true).setReleaseLabel(
//              "放开刷新...");
//      pullToRefresh.getLoadingLayoutProxy(false, true).setRefreshingLabel(
//              "正在加载...");
//      // 设置上拉刷新文本
//      pullToRefresh.getLoadingLayoutProxy(true, false)
//              .setPullLabel("下拉刷新...");
//      pullToRefresh.getLoadingLayoutProxy(true, false).setReleaseLabel(
//              "放开刷新...");
//      pullToRefresh.getLoadingLayoutProxy(true, false).setRefreshingLabel(
//              "正在加载...");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            default:
                break;
        }
    }
    private void  initSx(){
//        tv_jxs1.setTextColor(getResources().getColor(R.color.black));
//        view1.setVisibility(View.GONE);
//        tv_jxs2.setTextColor(getResources().getColor(R.color.black));
//        view2.setVisibility(View.GONE);
//        tv_jxs3.setTextColor(getResources().getColor(R.color.black));
//        view3.setVisibility(View.GONE);
    }

    private void initTestDate(){
//        list = new ArrayList<MyRebateBean>();
        for(int i=0 ; i<15;i++){
            MyRebateBean mMyRebateBean = new MyRebateBean();
            mMyRebateBean.setNum(i+1+"");
            mMyRebateBean.setTime("2016-12-07");
            mMyRebateBean.setName("七星时代");
            mMyRebateBean.setLv("普通会员");
//            list.add(mMyRebateBean);
        }
//        mMyRebateAdapter = new MyRebateAdapter(mContext,list);
//        mListView.setAdapter(mMyRebateAdapter);
//        mMyRebateAdapter.notifyDataSetChanged();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
