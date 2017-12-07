package com.qixing.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.activity.webview.RechargeProtocolActivity;
import com.qixing.adapter.QXliveHeadingAdapter;
import com.qixing.adapter.XBRechargeAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.InitJson;
import com.qixing.bean.QXLiveInfoJson;
import com.qixing.bean.ResultBean;
import com.qixing.bean.XBRechargeBean;
import com.qixing.global.Constant;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.MyGridView;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lenovo on 2017/10/20.
 */
public class MyWalletActivity extends BaseActivity {

    private BGATitlebar mTitlebar;

    private static Context context;

    private LinearLayout ll_chongzhi, ll_tixian, ll_zhangdan;

    //充值
    private LinearLayout wallet_ll_chongzhi;
    private TextView tv_xb, tv_recharge_xieyi;
    private MyGridView myGridView;
    private Button btn_recharge;
    private CheckBox cb_recharge;

    //提现
    private LinearLayout wallet_ll_tixian;
    private TextView tv_jf_present, tv_jf_keti, tv_tixian_xieyi;
    private Button btn_tixian;
    private CheckBox cb_tixian;

    private InitJson mInitJson;

    private List<XBRechargeBean> list_xb;
    private XBRechargeAdapter mRechargeAdapter;
    private int select = -1;

    private String str_xb;
    private int price = 0;

    private String isPayPwd,money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        context = MyWalletActivity.this;
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        isPayPwd = getIntent().getStringExtra("isPayPwd");
        money=getIntent().getStringExtra("money");

        initView();
        initDatas();
    }

    private void initView() {

        mTitlebar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitlebar.setTitleText("钱包");
        mTitlebar.setRightText("支付设置");
        mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate() {
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }

            @Override
            public void onClickRightCtv() {
                super.onClickRightCtv();
                Intent intent = new Intent();
                intent.putExtra("isPayPwd",isPayPwd);
                if("0".equals(MyApplication.getInstance().getPref_ispaypwd())){//未设置支付密码
                    AppManager.getAppManager().startNextActivity(mContext, ForgetPayPwdActivity.class,intent);
                }else if("1".equals(MyApplication.getInstance().getPref_ispaypwd())){//已设置支付密码
                    AppManager.getAppManager().startNextActivity(mContext, ModifyPayPwdActivity.class,intent);
                }
            }
        });

        ll_chongzhi = (LinearLayout) findViewById(R.id.my_wallet_head_ll_chongzhi);
        ll_chongzhi.setOnClickListener(this);
        ll_tixian = (LinearLayout) findViewById(R.id.my_wallet_head_ll_tixian);
        ll_tixian.setOnClickListener(this);
        ll_zhangdan = (LinearLayout) findViewById(R.id.my_wallet_head_ll_zhangdan);
        ll_zhangdan.setOnClickListener(this);

        //充值
        wallet_ll_chongzhi = (LinearLayout) findViewById(R.id.my_wallet_ll_chongzhi);
        tv_xb = (TextView) findViewById(R.id.my_wallet_chongzhi_tv_xb);
        cb_recharge = (CheckBox) findViewById(R.id.my_wallet_chongzhi_cb_xieyi);
        cb_recharge.setChecked(true);
        cb_recharge.setOnClickListener(this);
        btn_recharge = (Button) findViewById(R.id.my_wallet_chongzhi_btn_recharge);
        btn_recharge.setOnClickListener(this);
        tv_recharge_xieyi= (TextView) findViewById(R.id.my_wallet_chongzhi_tv_xieyi);
        tv_recharge_xieyi.setOnClickListener(this);
        myGridView = (MyGridView) findViewById(R.id.my_wallet_chongzhi_gv_xb);


        //提现
        wallet_ll_tixian = (LinearLayout) findViewById(R.id.my_wallet_ll_tixian);
        tv_jf_present = (TextView) findViewById(R.id.my_wallet_tixian_tv_present_jf);
        tv_tixian_xieyi = (TextView) findViewById(R.id.my_wallet_tixian_tv_xieyi);
        tv_tixian_xieyi.setOnClickListener(this);
        cb_tixian = (CheckBox) findViewById(R.id.my_wallet_tixian_cb_xieyi);
        cb_tixian.setChecked(true);
        cb_tixian.setOnClickListener(this);
        btn_tixian = (Button) findViewById(R.id.my_wallet_tixian_btn_shouyi);
        btn_tixian.setOnClickListener(this);

        initXBList();

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                for (int i = 0; i < list_xb.size(); i++) {
                    if (i == position) {
                        list_xb.get(i).setSelect("1");
                        if (list_xb.get(i).getPrice().contains(",")) {
                            int index = list_xb.indexOf(",");
                            String money = list_xb.get(i).getPrice();
                            String rmb = money.replace(",", "");
                            price = Integer.parseInt(rmb);
                            str_xb = list_xb.get(i).getXb();
                        } else {
                            double rmb = Double.parseDouble(list_xb.get(i).getPrice());
                            price = (int) rmb;
                        }
                    } else {
                        list_xb.get(i).setSelect("0");
                    }
                    select = position;
                }
                mRechargeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initDatas() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_SEL_DOU;//
        System.out.println("===========================钱包页面 星币个数url = " + url);
        System.out.println("===========================钱包页面 星币个数params = " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================钱包页面 星币个数 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mInitJson = new Gson().fromJson(response.toString(), InitJson.class);
                    if (mInitJson.getResult().equals("1")) {
                        tv_xb.setText(mInitJson.getDoudou());
                    } else {
                        Toasts.show(mInitJson.getMessage());
                    }
                } else {
                    showErrorDialog(context);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(context);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(context);
            }
        });
    }

    private void initXBList() {
        String[] xb = {"180", "880", "1680", "5880", "9980", "19980"};
        String[] rmb = {"18.00", "88.00", "168.00", "588.00", "998.00", "1,998"};
        list_xb = new ArrayList<>();
        mRechargeAdapter = new XBRechargeAdapter(context, list_xb);
        myGridView.setAdapter(mRechargeAdapter);
        for (int i = 0; i < 6; i++) {
            XBRechargeBean xbBean = new XBRechargeBean();
            xbBean.setXb(xb[i]);
            xbBean.setPrice(rmb[i]);
            list_xb.add(xbBean);
        }
        mRechargeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.my_wallet_head_ll_chongzhi://充值
                wallet_ll_tixian.setVisibility(View.GONE);
                wallet_ll_chongzhi.setVisibility(View.VISIBLE);
                initDatas();
                break;
            case R.id.my_wallet_head_ll_tixian://提现
                wallet_ll_chongzhi.setVisibility(View.GONE);
                wallet_ll_tixian.setVisibility(View.VISIBLE);
                if (Double.parseDouble(money)>0) {
                    tv_jf_present.setText(money);
                }else{

                }
                break;
            case R.id.my_wallet_head_ll_zhangdan://我的账单
                intent = new Intent(context, MyBillActivity.class);
                startActivity(intent);
                break;
            case R.id.my_wallet_chongzhi_btn_recharge://立即充值
                if (price != 0) {
                    intent = new Intent();
                    intent.putExtra("price", price);
                    intent.putExtra("num", str_xb);
                    AppManager.getAppManager().startNextActivity(context, RechargeActivity.class, intent);
                } else {
                    Toasts.show("请您先选择套餐后充值!");
                }
                break;
            case R.id.my_wallet_tixian_btn_shouyi://兑换收益
                intent = new Intent();
                intent.putExtra("tag", "MyWalletActivity");//
                intent.putExtra("type", "1");
                intent.putExtra("money", money);
                AppManager.getAppManager().startFragmentNextActivity(mContext,CashConfirmActivity.class,intent);
                break;
            case R.id.my_wallet_tixian_tv_xieyi://提现协议
                AppManager.getAppManager().startNextActivity(context, RechargeProtocolActivity.class);
                break;
            case R.id.my_wallet_chongzhi_tv_xieyi://充值协议
                AppManager.getAppManager().startNextActivity(context, RechargeProtocolActivity.class);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
