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
import com.qixing.adapter.XBConsumeAdapter;
import com.qixing.app.MyApplication;
import com.qixing.bean.XBConsumeBean;
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
public class SubFragment4 extends BaseFragment {

    private ImageView iv_noodler;
    private TextView tv_noodler;
    private PullToRefreshListView mListView;

    private int p = 1;
    private boolean isRefresh = false;

    private XBConsumeJson mXBConsumeJson;
    private List<XBConsumeBean> list_consume;
    private XBConsumeAdapter mConsumeAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_sub4);

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
        String url = Constant.BASE_URL + Constant.URL_USERAPI_XBXFRECORD;//星币消费记录
        System.out.println("================================我的账单 送出礼物 params===========" + params.toString());
        System.out.println("================================我的账单 送出礼物 url===========" + url);

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
                System.out.println("============================我的账单 送出礼物 response==========" + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mXBConsumeJson = new Gson().fromJson(response.toString(), XBConsumeJson.class);
                    if ("1".equals(mXBConsumeJson.getResult())) {
                        if (mXBConsumeJson.getList().size() == 0 || mXBConsumeJson.getList() == null || "".equals(mXBConsumeJson.getList())) {
                            iv_noodler.setVisibility(View.VISIBLE);
                            tv_noodler.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        } else {
                            iv_noodler.setVisibility(View.GONE);
                            tv_noodler.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            list_consume = mXBConsumeJson.getList();
                            mConsumeAdapter = new XBConsumeAdapter(getActivity(), list_consume);
                            mListView.setAdapter(mConsumeAdapter);
                        }
                    } else if ("2".equals(mXBConsumeJson.getResult())) {
                        iv_noodler.setVisibility(View.VISIBLE);
                        tv_noodler.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    } else {
                        Toasts.show(mXBConsumeJson.getMessage());
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
        String url = Constant.BASE_URL + Constant.URL_USERAPI_XBXFRECORD;//星币消费记录
        System.out.println("================================我的账单 送出礼物 更多params===========" + params.toString());
        System.out.println("================================我的账单 送出礼物 更多url===========" + url);

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
                System.out.println("============================我的账单 送出礼物 更多response==========" + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mXBConsumeJson = new Gson().fromJson(response.toString(), XBConsumeJson.class);
                    if ("1".equals(mXBConsumeJson.getResult())) {
                        if (mXBConsumeJson.getList().size() == 0 || mXBConsumeJson.getList() == null || "".equals(mXBConsumeJson.getList())) {
                            Toasts.show("暂无更多数据");
                        } else {
                            iv_noodler.setVisibility(View.GONE);
                            tv_noodler.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            p = p + 1;
                            list_consume.addAll(mXBConsumeJson.getList());
                            mConsumeAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mXBConsumeJson.getMessage());
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
