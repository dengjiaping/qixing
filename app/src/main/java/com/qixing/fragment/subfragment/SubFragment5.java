package com.qixing.fragment.subfragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.adapter.BillRechargeAdapter;
import com.qixing.adapter.XBConsumeAdapter;
import com.qixing.app.MyApplication;
import com.qixing.bean.BillRechargeBean;
import com.qixing.bean.BillRechargeJson;
import com.qixing.bean.XBConsumeJson;
import com.qixing.fragment.BaseFragment;
import com.qixing.global.Constant;
import com.qixing.widget.Toasts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lenovo on 2017/10/23.
 */
public class SubFragment5 extends BaseFragment {

    private TextView tv_noodler;
    private ImageView iv_noodler;
    private PullToRefreshListView mListView;

    private int p = 1;
    private boolean isRefresh = false;

    private BillRechargeJson mBillRechargeJson;
    private List<BillRechargeBean> list_recharge;
    private BillRechargeAdapter mRechargeAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_sub5);

        iv_noodler = getViewById(R.id.iv_noodler);
        tv_noodler = getViewById(R.id.tv_noodler);
        mListView = getViewById(R.id.mListView);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        init(mListView);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                p = 1;
                isRefresh = true;
                initDatas();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                initMoreDatas();
            }
        });

        initDatas();
    }

    private void initDatas() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.dismiss();
        }

        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("p", p);
        String url = Constant.BASE_URL + Constant.URL_USERAPI_RECHARGERECORD;//星币充值记录
        System.out.println("================================我的账单 充值记录 params===========" + params.toString());
        System.out.println("================================我的账单 充值记录 url===========" + url);

        mAsyncHttpClient.post(getActivity(), url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (isRefresh) {
                    isRefresh = false;
                    mListView.onRefreshComplete();
                }
                System.out.println("============================我的账单 充值记录 response==========" + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mBillRechargeJson = new Gson().fromJson(response.toString(), BillRechargeJson.class);
                    if ("1".equals(mBillRechargeJson.getResult())) {
                        if (mBillRechargeJson.getList().size() == 0 || mBillRechargeJson.getList() == null || "".equals(mBillRechargeJson.getList())) {
                            iv_noodler.setVisibility(View.VISIBLE);
                            tv_noodler.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        } else {
                            iv_noodler.setVisibility(View.GONE);
                            tv_noodler.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            list_recharge = mBillRechargeJson.getList();
                            mRechargeAdapter = new BillRechargeAdapter(getActivity(), list_recharge);
                            mListView.setAdapter(mRechargeAdapter);
                        }
                    } else if ("2".equals(mBillRechargeJson.getResult())) {
                        iv_noodler.setVisibility(View.VISIBLE);
                        tv_noodler.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    } else {
                        Toasts.show(mBillRechargeJson.getMessage());
                    }
                } else {
                    showErrorDialog(getActivity());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (isRefresh) {
                    isRefresh = false;
                    mListView.onRefreshComplete();
                }
                showErrorDialog(getActivity());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("================================throwable,responseString===========" + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(getActivity());
            }
        });
    }

    private void initMoreDatas() {

        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("p", p + 1);
        String url = Constant.BASE_URL + Constant.URL_USERAPI_RECHARGERECORD;//星币充值记录
        System.out.println("================================我的账单 充值记录 更多params===========" + params.toString());
        System.out.println("================================我的账单 充值记录 更多url===========" + url);

        mAsyncHttpClient.post(getActivity(), url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (isRefresh) {
                    isRefresh = false;
                    mListView.onRefreshComplete();
                }
                System.out.println("============================我的账单 充值记录 更多response==========" + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mBillRechargeJson = new Gson().fromJson(response.toString(), BillRechargeJson.class);
                    if ("1".equals(mBillRechargeJson.getResult())) {
                        if (mBillRechargeJson.getList().size() == 0 || mBillRechargeJson.getList() == null || "".equals(mBillRechargeJson.getList())) {
                            Toasts.show("暂无更多数据");
                        } else {
                            iv_noodler.setVisibility(View.GONE);
                            tv_noodler.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            p = p + 1;
                            list_recharge.addAll(mBillRechargeJson.getList());
                            mRechargeAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mBillRechargeJson.getMessage());
                    }
                } else {
                    showErrorDialog(getActivity());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (isRefresh) {
                    isRefresh = false;
                    mListView.onRefreshComplete();
                }
                showErrorDialog(getActivity());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("============================throwable,responseString========" + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(getActivity());
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

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    public void onResume() {
        super.onResume();
        initDatas();
    }
}
