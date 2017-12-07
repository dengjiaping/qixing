package com.qixing.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

public class JFAmountActivity extends BaseActivity {

    private BGATitlebar mTitlebar;

    private TextView tv_jf_count;
    private Button btn_jf_exchange;
    private PullToRefreshListView mListView_JF;

    private JFRecordAdapter mRecordAdapter;
    private List<JFRecordBean> mRecordList;

    private JFRecordJson mRechargeRecordJson;
    private ResultBean mResultBean;

    private String jifen;

    private ProgressDialog mProgressDialog;

    private int p=1;
    private boolean isRefresh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_jfamount, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        initView();
        initData();
    }

    private void initData() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();

        RequestParams parmas = new RequestParams();
        parmas.put("uid", MyApplication.getInstance().getUid());

        String url = Constant.BASE_URL + Constant.URL_JF_RECORDLIST+"/p/"+p;
        System.out.println("=====================积分纪录 页数p=========="+p);

        System.out.println("=====================积分记录 url==============" + url);

        mAsyncHttpClient.post(mContext, url, parmas, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                if(isRefresh){
                    isRefresh=false;
                    mListView_JF.onRefreshComplete();
                }

                System.out.println("=======================积分记录 response===========" + response.toString());

                if (!TextUtils.isEmpty(response.toString())) {
                    mRechargeRecordJson = new Gson().fromJson(response.toString(), JFRecordJson.class);
                    if (mRechargeRecordJson.getResult().equals("1")) {
                        jifen=mRechargeRecordJson.getJifen();
                        tv_jf_count.setText(jifen);
                        mRecordList=mRechargeRecordJson.getList();
                        mRecordAdapter = new JFRecordAdapter(JFAmountActivity.this, mRecordList);
                        mListView_JF.setAdapter(mRecordAdapter);
                        mRecordAdapter.notifyDataSetChanged();
                    } else {
                        Toasts.show(mRechargeRecordJson.getMessage());
                    }
                } else {
                    showErrorDialog(JFAmountActivity.this);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mListView_JF.onRefreshComplete();
                }
                showTimeoutDialog(JFAmountActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mListView_JF.onRefreshComplete();
                }
                showErrorDialog(JFAmountActivity.this);
            }
        });
    }

    private void initView() {
        mTitlebar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitlebar.setTitleText("积分记录");
        mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate() {
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }
        });

        tv_jf_count = (TextView) findViewById(R.id.tv_jf_amount_total_count);
        btn_jf_exchange = (Button) findViewById(R.id.btn_jf_amount_exchange);
        btn_jf_exchange.setOnClickListener(this);

        mListView_JF = (PullToRefreshListView) findViewById(R.id.mylv_jf_amount_list);
        mListView_JF.setMode(PullToRefreshBase.Mode.BOTH);
        init(mListView_JF);

        mListView_JF.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                p=1;
                isRefresh=true;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh=true;
                initMoreData();
            }
        });

    }

    private void initMoreData() {
        RequestParams parmas = new RequestParams();
        parmas.put("uid", MyApplication.getInstance().getUid());

        String url = Constant.BASE_URL + Constant.URL_JF_RECORDLIST+"/p/"+(p+1);
        System.out.println("=====================积分记录 上拉加载 页数p=========="+(p+1));

        System.out.println("=====================积分记录 url==============" + url);

        mAsyncHttpClient.post(mContext, url, parmas, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                if(isRefresh){
                    isRefresh=false;
                    mListView_JF.onRefreshComplete();
                }

                System.out.println("=======================积分记录 response===========" + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mRechargeRecordJson = new Gson().fromJson(response.toString(), JFRecordJson.class);
                    if (mRechargeRecordJson.getResult().equals("1")) {
                        if(mRechargeRecordJson.getList().size()==0||mRechargeRecordJson.getList()==null||"".equals(mRechargeRecordJson.getList())){
                            Toasts.show("暂无更多数据");
                        }else {
                            mRecordList.addAll(mRechargeRecordJson.getList());
                            p=p+1;
                            mRecordAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mRechargeRecordJson.getMessage());
                    }
                } else {
                    showErrorDialog(JFAmountActivity.this);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mListView_JF.onRefreshComplete();
                }
                showTimeoutDialog(JFAmountActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mListView_JF.onRefreshComplete();
                }
                showErrorDialog(JFAmountActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_jf_amount_exchange:
                showJFExchangeDialog();
                break;
            default:
                break;
        }
    }

    private void init(PullToRefreshListView listView) {
        ILoadingLayout startLabels = listView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = listView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

    }

    private void showJFExchangeDialog() {
        View view=getLayoutInflater().inflate(R.layout.dialog_jf_exchange,null);

        TextView tv_bili= (TextView) view.findViewById(R.id.dialog_jf_exchange_percent);
        tv_bili.setText("比例"+mRechargeRecordJson.getBili()+":1");
        final EditText et_input= (EditText) view.findViewById(R.id.et_dialog_jf_exchange_input);
        Button btn_cancel= (Button) view.findViewById(R.id.btn_dialog_jf_exchange_cancel);
        Button btn_confirm= (Button) view.findViewById(R.id.btn_dialog_jf_exchange_confirm);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
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
                System.out.println("=====================积分记录 积分兑换 jf_url==============" + jf_url);
                mAsyncHttpClient.post(mContext, jf_url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }

                        System.out.println("=======================积分记录 response===========" + response.toString());
                        if (!TextUtils.isEmpty(response.toString())) {
                            mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                            if (mResultBean.getResult().equals("1")) {
//                                 tv_jf_count.setText(mRechargeRecordJson.getJifen());
//                                 mRecordList.addAll(mRechargeRecordJson.getList());
//                                 mRecordAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                                jifen=Integer.parseInt(jifen)-Integer.parseInt(number)+"";
                                tv_jf_count.setText(jifen);
                                Toasts.show(mResultBean.getMessage());
                            } else {
                                Toasts.show(mResultBean.getMessage());
                            }
                        } else {
                            dialog.dismiss();
                            showErrorDialog(JFAmountActivity.this);
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
                        showErrorDialog(JFAmountActivity.this);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);

                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }
                        dialog.dismiss();
                        showTimeoutDialog(JFAmountActivity.this);
                    }
                });
            }
        });

    }
}
