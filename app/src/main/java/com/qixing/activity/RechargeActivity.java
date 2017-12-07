package com.qixing.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jungly.gridpasswordview.GridPasswordView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.PayMethodBean;
import com.qixing.bean.RebateEnterJson;
import com.qixing.global.Constant;
import com.qixing.utlis.PhoneUtils;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;
import com.qixing.wxapi.WXPayEntryActivity;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by wicep on 2015/12/23.
 * 充值
 */
public class RechargeActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private EditText edit_amount;
    private LinearLayout ll_paymethod;

    private LinearLayout ll_pay;
    private LinearLayout ll_zhifubao,ll_weixin;
    private ImageView img_zhifubao,img_weixin;

    private boolean is_paymethod = false;
    private boolean is_zhifubao = false,is_weixin = false;

    private Button btn_confrim;

    private int p = 1;

    private String paymethod = "1"; //支付方式1支付宝 2.微信  3余额

    private PayMethodBean mPayMethodBean;

    private String str_amount;

    private Double money,yemoney;

    public static Context context;

    private int price=0;
    private String num="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_recharge, null);
        setContentView(view);

        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        context = RechargeActivity.this;

        price=getIntent().getIntExtra("price",0);
        num=getIntent().getStringExtra("num");

        initView();
//        initDatas();
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("充值");
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

        edit_amount = (EditText) findViewById(R.id.recharge_edit_amount);
        if (price!=0){
            edit_amount.setText(price+"");
        }

        ll_paymethod = (LinearLayout) findViewById(R.id.recharge_ll_paymethod);
        ll_paymethod.setOnClickListener(this);

        ll_pay = (LinearLayout) findViewById(R.id.recharge_ll_pay);

        ll_zhifubao = (LinearLayout) findViewById(R.id.recharge_ll_zhifubao);
        ll_zhifubao.setOnClickListener(this);
        ll_weixin = (LinearLayout) findViewById(R.id.recharge_ll_weixin);
        ll_weixin.setOnClickListener(this);

        img_zhifubao = (ImageView) findViewById(R.id.recharge_img_zhifubao);
        img_weixin = (ImageView) findViewById(R.id.recharge_img_weixin);

        btn_confrim = (Button) findViewById(R.id.recharge_btn_confrim);
        btn_confrim.setOnClickListener(this);
    }

    private void apply() {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();

        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("num",num);
        params.put("money", edit_amount.getText().toString().trim());//钱	money
        params.put("buytype","1");
//        final String url = Constant.BASE_URL + Constant.URL_USERAPI_CZMONEY;
        final String url=Constant.BASE_URL+Constant.URL_USERAPI_BUY_DOU;
        System.out.println("===========================充值 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================充值 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mPayMethodBean = new Gson().fromJson(response.toString(), PayMethodBean.class);
                    if (mPayMethodBean.getResult().equals("1")) {
                        Toasts.show("支付跳转中....");
                        Intent intent = new Intent();
                        intent.putExtra("tag", "RechargeActivity");//tag 标识
                        intent.putExtra("paymethod", paymethod);//支付方式1支付宝 2.微信  3余额

                        intent.putExtra("totalprice", mPayMethodBean.getMoney());//订单实际价格
                        intent.putExtra("orderno", mPayMethodBean.getId());//订单编号
                        intent.putExtra("url", mPayMethodBean.getUrl());//回调函数url
                        intent.putExtra("title", mPayMethodBean.getTitle());//订单标题
                        intent.putExtra("discription", mPayMethodBean.getDiscription());//订单描述
                        AppManager.getAppManager().startNextActivity(mContext, WXPayEntryActivity.class, intent);
                    } else {
                        Toasts.show(mPayMethodBean.getMessage());
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
                showTimeoutDialog(mContext);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.recharge_ll_zhifubao://1支付宝
                if(is_zhifubao){

                }else{
                    paymethod = "1";//支付方式1支付宝 2.微信  3余额
                    is_zhifubao = true;
                    is_weixin = false;
                    img_zhifubao.setVisibility(View.VISIBLE);
                    img_weixin.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.recharge_ll_weixin://2.微信
                if(is_weixin){

                }else{
                    paymethod = "2";//支付方式1支付宝 2.微信  3余额
                    is_zhifubao = false;
                    is_weixin = true;
                    img_zhifubao.setVisibility(View.INVISIBLE);
                    img_weixin.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.recharge_btn_confrim:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm.hideSoftInputFromWindow(edit_amount.getWindowToken(), 0);
                str_amount = edit_amount.getText().toString().trim();
//                str_name,str_phone,str_money,str_yemoney;
                if(TextUtils.isEmpty(str_amount)){
                    Toasts.show("充值金额不能为空");
                    break;
                }else if(!(is_zhifubao||is_weixin)){
                    Toasts.show("请选择支付方式");
                    break;
                }else{
                    apply();
                }
                break;
            default:
                break;
        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
