package com.qixing.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.adapter.RechargeRecordAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.JFRecordJson;
import com.qixing.bean.RechargeRecordBean;
import com.qixing.bean.RechargeRecordJson;
import com.qixing.global.Constant;
import com.qixing.view.pulltorefreshswipemenu.PullToRefreshSwipeMenuListView;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

public class CashRecordActivity extends BaseActivity {

    private BGATitlebar mTitlebar;
    private PullToRefreshListView mListView_CashRecord;

    private List<RechargeRecordBean> mRecordList;
    private RechargeRecordAdapter mRecordAdapter;

    private RechargeRecordJson mRecordJson;

    private int p=1;
    private boolean isRefresh=false;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view=getLayoutInflater().inflate(R.layout.activity_cash_record,null);
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

        RequestParams params=new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());


        String url= Constant.BASE_URL+Constant.URL_CZ_RECORDLIST+"/p/"+p;
        System.out.println("=====================消费记录 页数p=========="+p);

        System.out.println("========================消费记录 url=============="+url);

        mAsyncHttpClient.post(mContext,url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                if(isRefresh){
                    isRefresh=false;
                    mListView_CashRecord.onRefreshComplete();
                }
                System.out.println("=======================消费记录 response==========="+response.toString());

                if(!TextUtils.isEmpty(response.toString())){
                    mRecordJson=new Gson().fromJson(response.toString(),RechargeRecordJson.class);
                    if(mRecordJson.getResult().equals("1")){
                        if (mRecordJson.getList() == null || mRecordJson.getList().size() == 0 || "".equals(mRecordJson.getList())){

                        }else {
                            mRecordList = mRecordJson.getList();
                            mRecordAdapter = new RechargeRecordAdapter(CashRecordActivity.this, mRecordList);
                            mListView_CashRecord.setAdapter(mRecordAdapter);
                            mRecordAdapter.notifyDataSetChanged();
                        }
                    }else{
                        Toasts.show(mRecordJson.getMessage());
                    }
                }else{
                    showErrorDialog(CashRecordActivity.this);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mListView_CashRecord.onRefreshComplete();
                }
                showErrorDialog(CashRecordActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mListView_CashRecord.onRefreshComplete();
                }
                showTimeoutDialog(CashRecordActivity.this);
            }
        });
    }

    private void initMoreData(){
        RequestParams params=new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());


        String url= Constant.BASE_URL+Constant.URL_CZ_RECORDLIST+"/p/"+(p+1);
        System.out.println("=====================消费记录 上拉加载 页数p=========="+(p+1));

        System.out.println("========================消费记录 url=============="+url);

        mAsyncHttpClient.post(mContext,url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                if(isRefresh){
                    isRefresh=false;
                    mListView_CashRecord.onRefreshComplete();
                }
                System.out.println("=======================消费记录 response==========="+response.toString());

                if(!TextUtils.isEmpty(response.toString())){
                    mRecordJson=new Gson().fromJson(response.toString(),RechargeRecordJson.class);
                    if(mRecordJson.getResult().equals("1")){
                        if(mRecordJson.getList().size()==0||mRecordJson.getList()==null||"".equals(mRecordJson.getList())){
                            Toasts.show("暂无更多数据");
                        }else{
                            mRecordList.addAll(mRecordJson.getList());
                            p=p+1;
                            mRecordAdapter.notifyDataSetChanged();
                        }
                    }else{
                        Toasts.show(mRecordJson.getMessage());
                    }
                }else{
                    showErrorDialog(CashRecordActivity.this);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mListView_CashRecord.onRefreshComplete();
                }
                showErrorDialog(CashRecordActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mListView_CashRecord.onRefreshComplete();
                }
                showTimeoutDialog(CashRecordActivity.this);
            }
        });
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

    private void initView() {
        mTitlebar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitlebar.setTitleText("消费记录");
        mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }
        });

        mListView_CashRecord= (PullToRefreshListView) findViewById(R.id.listview_activity_cash_record);
        mListView_CashRecord.setMode(PullToRefreshBase.Mode.BOTH);
        init(mListView_CashRecord);

        mListView_CashRecord.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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

    @Override
    protected void onResume() {
        super.onResume();
        isRefresh=true;
        initData();
    }
}
