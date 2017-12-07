package com.qixing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.qixing.R;
import com.qixing.adapter.MyRebateAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.bean.MyRebateBean;
import com.qixing.view.MyListView;
import com.qixing.view.titlebar.BGATitlebar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wicep on 2015/12/23.
 * 绑定账号
 */
public class BindAccountActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private LinearLayout ll_phone,ll_wx,ll_zfb;
    private TextView tv_phone, tv_wxname, tv_zfbname;

    private String state = "1";


//    private JoinBean mJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_bind_account, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        initView();
        initDatas();
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("绑定账号");
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
//        private LinearLayout ll_phone,ll_wx,ll_zfb;
//        private TextView tv_phone, tv_wxname, tv_zfbname;
        ll_phone = (LinearLayout) findViewById(R.id.bind_account_ll_phone);
        ll_phone.setOnClickListener(this);
        ll_wx = (LinearLayout) findViewById(R.id.bind_account_ll_wx);
        ll_wx.setOnClickListener(this);
        ll_zfb = (LinearLayout) findViewById(R.id.bind_account_ll_zfb);
        ll_zfb.setOnClickListener(this);

        tv_phone = (TextView) findViewById(R.id.bind_account_tv_phone);
        tv_wxname = (TextView) findViewById(R.id.bind_account_tv_wxname);
        tv_zfbname = (TextView) findViewById(R.id.bind_account_tv_zfbname);

    }


    private void initDatas() {
//        initTestData();
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(mContext);
//            mProgressDialog.setMessage("加载中...");
//            mProgressDialog.show();
//        }
//        final RequestParams params = new RequestParams();
////        params.put("uid", MyApplication.getInstance().getUid());
////        params.put("status", "2");//+"/p/"+p
//        final String url = Constant.BASE_URL + Constant.URL_APP_JMKY;
//        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                if (mProgressDialog != null) {
//                    mProgressDialog.dismiss();
//                }
//                System.out.println("===========================url ===== " + url);
//                System.out.println("===========================params ===== " + params.toString());
//                System.out.println("===========================个人中心 加盟快优 response ===== " + response.toString());
//                if (!TextUtils.isEmpty(response.toString())) {
//                    mJson = new Gson().fromJson(response.toString(), JoinBean.class);
//                    if (mJson.getResult().equals("1")) {
//                        tv_content.setText(mJson.getLists().getContent() + "");
//                        tv_phone.setText("加盟热线:"+mJson.getLists().getIphone() + "");
//                    } else {
//                        Toasts.show(mJson.getMessage());
//                    }
//                } else {
//                    showErrorDialog(mContext);
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                if (mProgressDialog != null) {
//                    mProgressDialog.dismiss();
//                }
//                showErrorDialog(mContext);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.bind_account_ll_phone:
                intent = new Intent();
//                intent.putExtra("url" ,Constant.BASE_URL+Constant.URL_USERAPI_POLICY);
                AppManager.getAppManager().startNextActivity(mContext, BindPhoneActivity.class,intent);
                break;
            default:
                break;
        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
