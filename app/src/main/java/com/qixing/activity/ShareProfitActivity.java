package com.qixing.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.adapter.JFRecordAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.JFRecordBean;
import com.qixing.bean.JFRecordJson;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.view.MyListView;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

public class ShareProfitActivity extends BaseActivity {

    private BGATitlebar mTitlebar;
    private static Context context;

    private PullToRefreshScrollView mScrollView;
    private ImageView iv_share, iv_xb;
    private TextView tv_xb;
    private Button btn_duihuan;

    private TextView tv_noodler;
    private MyListView mListView;
    private List<JFRecordBean> list_jf;
    private JFRecordAdapter mRecordAdapter;
    private JFRecordJson mJFRecordJson;

    private boolean isRefresh = false;
    private int p = 1;
    private String jifen = "";

    private ResultBean mResultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_profit);

        context = ShareProfitActivity.this;
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        initView();
        initDatas();
    }

    private void initView() {
        mTitlebar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitlebar.setTitleText("分享我赚钱");
        mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate() {
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
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
        mScrollView.setMode(PullToRefreshBase.Mode.BOTH);
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
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                p = 1;
                isRefresh = true;
                initDatas();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                isRefresh = true;
                initMoreDatas();
            }
        });

        iv_share = (ImageView) findViewById(R.id.share_head_iv_pic);
        iv_xb = (ImageView) findViewById(R.id.share_head_iv_xb);
        tv_xb = (TextView) findViewById(R.id.share_head_tv_xb);
        btn_duihuan = (Button) findViewById(R.id.share_head_btn_jfdh);
        btn_duihuan.setOnClickListener(this);

        tv_noodler= (TextView) findViewById(R.id.tv_noodler);
        mListView = (MyListView) findViewById(R.id.share_profit_listView);
        mListView.setFocusable(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });

    }

    private void initDatas() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();

        RequestParams parmas = new RequestParams();
        parmas.put("uid", MyApplication.getInstance().getUid());

        String url = Constant.BASE_URL + Constant.URL_JF_RECORDLIST + "/p/" + p;
        System.out.println("=====================分享我赚钱 积分记录 页数p==========" + p);

        System.out.println("=====================分享我赚钱 积分记录 url==============" + url);

        mAsyncHttpClient.post(mContext, url, parmas, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                if (isRefresh) {
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }

                System.out.println("=======================分享我赚钱 积分纪录 response===========" + response.toString());

                if (!TextUtils.isEmpty(response.toString())) {
                    mJFRecordJson = new Gson().fromJson(response.toString(), JFRecordJson.class);
                    if (mJFRecordJson.getResult().equals("1")) {
                        jifen = mJFRecordJson.getJifen();
                        tv_xb.setText(jifen);
                        if (mJFRecordJson.getList().size() == 0 || mJFRecordJson.getList() == null || "".equals(mJFRecordJson.getList())) {
                            tv_noodler.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        } else {
                            tv_noodler.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            list_jf = mJFRecordJson.getList();
                            mRecordAdapter = new JFRecordAdapter(context, list_jf);
                            mListView.setAdapter(mRecordAdapter);
                            mRecordAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mJFRecordJson.getMessage());
                    }
                } else {
                    showErrorDialog(context);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mScrollView.onRefreshComplete();
                }
                showTimeoutDialog(context);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mScrollView.onRefreshComplete();
                }
                showErrorDialog(context);
            }
        });
    }

    private void initMoreDatas() {

        RequestParams parmas = new RequestParams();
        parmas.put("uid", MyApplication.getInstance().getUid());

        String url = Constant.BASE_URL + Constant.URL_JF_RECORDLIST + "/p/" + (p + 1);
        System.out.println("=====================分享我赚钱 积分纪录 更多 页数p==========" + (p + 1));

        System.out.println("=====================分享我赚钱 积分纪录 更多 url==============" + url);

        mAsyncHttpClient.post(mContext, url, parmas, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                if (isRefresh) {
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }

                System.out.println("=======================分享我赚钱 积分记录 更多 response===========" + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mJFRecordJson = new Gson().fromJson(response.toString(), JFRecordJson.class);
                    if (mJFRecordJson.getResult().equals("1")) {
                        if (mJFRecordJson.getList().size() == 0 || mJFRecordJson.getList() == null || "".equals(mJFRecordJson.getList())) {
                            Toasts.show("暂无更多数据");
                        } else {
                            tv_noodler.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            list_jf.addAll(mJFRecordJson.getList());
                            p = p + 1;
                            mRecordAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mJFRecordJson.getMessage());
                    }
                } else {
                    showErrorDialog(context);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mScrollView.onRefreshComplete();
                }
                showTimeoutDialog(context);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mScrollView.onRefreshComplete();
                }
                showErrorDialog(context);
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
    }

    private void showJFExchangeDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_jf_exchange, null);

        TextView tv_bili = (TextView) view.findViewById(R.id.dialog_jf_exchange_percent);
        tv_bili.setText("比例" + mJFRecordJson.getBili() + ":1");
        final EditText et_input = (EditText) view.findViewById(R.id.et_dialog_jf_exchange_input);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_dialog_jf_exchange_cancel);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_dialog_jf_exchange_confirm);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number = et_input.getText().toString();

                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(mContext);
                    mProgressDialog.setMessage("加载中...");
                }
                mProgressDialog.show();

                RequestParams params = new RequestParams();
                params.put("uid", MyApplication.getInstance().getUid());
                params.put("jifen", number);
                String jf_url = Constant.BASE_URL + Constant.URL_JF_RECHARGE_TO_QIANBAO;
                System.out.println("=====================分享我赚钱 积分兑换 jf_url==============" + jf_url);
                mAsyncHttpClient.post(mContext, jf_url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }

                        System.out.println("=======================分享我赚钱 积分兑换 response===========" + response.toString());
                        if (!TextUtils.isEmpty(response.toString())) {
                            mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                            if (mResultBean.getResult().equals("1")) {
//                                 tv_jf_count.setText(mRechargeRecordJson.getJifen());
//                                 mRecordList.addAll(mRechargeRecordJson.getList());
//                                 mRecordAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                                jifen = Integer.parseInt(jifen) - Integer.parseInt(number) + "";
                                tv_xb.setText(jifen);
                                Toasts.show(mResultBean.getMessage());
                            } else {
                                Toasts.show(mResultBean.getMessage());
                            }
                        } else {
                            dialog.dismiss();
                            showErrorDialog(context);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }
                        dialog.dismiss();
                        showErrorDialog(context);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);

                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }
                        dialog.dismiss();
                        showTimeoutDialog(context);
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.share_head_btn_jfdh://积分兑换
                showJFExchangeDialog();
                break;
        }
    }
}
