package com.qixing.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jungly.gridpasswordview.GridPasswordView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.activity.webview.MyWebActivity2;
import com.qixing.adapter.LayoutAdapter;
import com.qixing.adapter.MyRebateAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.MyRebateBean;
import com.qixing.bean.PayMethodBean;
import com.qixing.bean.RebateEnterJson;
import com.qixing.global.Constant;
import com.qixing.utlis.PhoneUtils;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.MyListView;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;
import com.qixing.wxapi.WXPayEntryActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by wicep on 2015/12/23.
 * 报名
 */
public class RebateEnterActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private LinearLayout ll_promote,ll_hy;
    private ImageView img_icon;
    private EditText edit_name,edit_phone,edit_amount,edit_tjr;
    private LinearLayout ll_paymethod;
    private ImageView img_right,img_bottom;

    private LinearLayout ll_pay;
    private LinearLayout ll_yue,ll_zhifubao,ll_weixin;
    private ImageView img_yue,img_zhifubao,img_weixin;

    private boolean is_paymethod = false;
    private boolean is_yue = false,is_zhifubao = false,is_weixin = false;

    private LinearLayout ll_xieyi;
    private CheckBox checkBox_xieyi;
    private TextView tv_xieyi;

    private Button btn_confrim;

    private int p = 1;

    private String paymethod = "1"; //支付方式1支付宝 2.微信  3余额

    private RebateEnterJson mRebateEnterJson;
    private PayMethodBean mPayMethodBean;

    private String str_name,str_phone,str_money,str_yemoney,zfpwd,str_tjr;

    private Double money,yemoney;

    public static Context context;

    private String isPayPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_rebate_enter, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        context = RebateEnterActivity.this;
        isPayPwd = getIntent().getStringExtra("isPayPwd");
        initView();
        initDatas();
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("会员");
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

        ll_promote= (LinearLayout) findViewById(R.id.rebate_enter_ll_promote);
        ll_hy= (LinearLayout) findViewById(R.id.rebate_enter_ll_becomehy);
        img_icon = (ImageView) findViewById(R.id.rebate_enter_img_icon);
//        private boolean is_paymode = false;
//        private boolean is_yue = false,is_zhifubao = false,is_weixin = false;

        edit_name = (EditText) findViewById(R.id.rebate_enter_edit_name);
        edit_phone = (EditText) findViewById(R.id.rebate_enter_edit_phone);
        edit_amount = (EditText) findViewById(R.id.rebate_enter_edit_amount);
        edit_tjr= (EditText) findViewById(R.id.rebate_enter_edit_tjr);

        ll_paymethod = (LinearLayout) findViewById(R.id.rebate_enter_ll_paymethod);
        ll_paymethod.setOnClickListener(this);
        img_right = (ImageView) findViewById(R.id.rebate_enter_img_right);
        img_bottom = (ImageView) findViewById(R.id.rebate_enter_img_bottom);

        ll_pay = (LinearLayout) findViewById(R.id.rebate_enter_ll_pay);

        ll_yue = (LinearLayout) findViewById(R.id.rebate_enter_ll_yue);
        ll_yue.setOnClickListener(this);
        ll_zhifubao = (LinearLayout) findViewById(R.id.rebate_enter_ll_zhifubao);
        ll_zhifubao.setOnClickListener(this);
        ll_weixin = (LinearLayout) findViewById(R.id.rebate_enter_ll_weixin);
        ll_weixin.setOnClickListener(this);

        img_yue = (ImageView) findViewById(R.id.rebate_enter_img_yue);
        img_zhifubao = (ImageView) findViewById(R.id.rebate_enter_img_zhifubao);
        img_weixin = (ImageView) findViewById(R.id.rebate_enter_img_weixin);

        ll_xieyi = (LinearLayout) findViewById(R.id.rebate_enter_ll_xieyi);
        checkBox_xieyi = (CheckBox) findViewById(R.id.rebate_enter_checkBox_xieyi);
        checkBox_xieyi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toasts.show("您已阅读并同意注册协议");
                }

            }
        });

        tv_xieyi = (TextView) findViewById(R.id.rebate_enter_tv_xieyi);
        tv_xieyi.setOnClickListener(this);

        btn_confrim = (Button) findViewById(R.id.rebate_enter_btn_confrim);
        btn_confrim.setOnClickListener(this);
    }


    private void initDatas() {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
//        params.put("status", "2");//+"/p/"+p
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_BAOM;
        System.out.println("===========================分销返利 我要报名url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================分销返利 我要报名 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mRebateEnterJson = new Gson().fromJson(response.toString(), RebateEnterJson.class);
                    if (mRebateEnterJson.getResult().equals("1")) {
                        edit_amount.setText(mRebateEnterJson.getMoney());

                        str_money = mRebateEnterJson.getMoney();
                        str_yemoney = mRebateEnterJson.getYemoney();
                        money = Double.valueOf(str_money);
                        yemoney = Double.valueOf(str_yemoney);

                        if(!TextUtils.isEmpty(mRebateEnterJson.getBanner().getPic())){
                            if(mRebateEnterJson.getBanner().getPic().startsWith("http")){
                                ImageLoader.getInstance().displayImage(mRebateEnterJson.getBanner().getPic(),img_icon, ImageLoaderOptions.getOptions());
                            }else {
                                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+mRebateEnterJson.getBanner().getPic(),img_icon, ImageLoaderOptions.getOptions());
                            }
                        }
                    }else {
                        Toasts.show(mRebateEnterJson.getMessage());
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


    private void apply() {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();

        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("uname", edit_name.getText().toString().trim());//        用户名	uname
        params.put("uphone", edit_phone.getText().toString().trim());//        手机号	uphone
        params.put("zftype", paymethod);//        支付方式1支付宝 2.微信  3余额	zftype
        params.put("zfpwd", zfpwd);//        支付密码
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_APPLY_BAOM;
        System.out.println("===========================分销返利 提交报名url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================分销返利 我要报名 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mPayMethodBean = new Gson().fromJson(response.toString(), PayMethodBean.class);
                    if (mPayMethodBean.getResult().equals("1")) {
//                        Toasts.show(mRebateEnterJson.getMessage());
                        if("3".equals(paymethod)){
                            Toasts.show(mPayMethodBean.getMessage());
                            finish();
                        }else{
                            Toasts.show("支付跳转中....");
                            Intent intent = new Intent();
                            intent.putExtra("tag", "RebateEnterActivity");//tag 标识
                            intent.putExtra("paymethod", paymethod);//支付方式1支付宝 2.微信  3余额

                            intent.putExtra("totalprice", mPayMethodBean.getTotalprice());//订单实际价格
                            intent.putExtra("orderno", mPayMethodBean.getOrderno());//订单编号
                            intent.putExtra("url", mPayMethodBean.getUrl());//回调函数url
                            intent.putExtra("title", mPayMethodBean.getTitle());//订单标题
                            intent.putExtra("discription", mPayMethodBean.getDiscription());//订单描述
                            AppManager.getAppManager().startNextActivity(mContext, WXPayEntryActivity.class, intent);
                        }

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
            case R.id.rebate_enter_ll_paymethod:
                if(!is_paymethod){//未点击  点击显示
                    is_paymethod = true;
                    img_right.setVisibility(View.GONE);
                    img_bottom.setVisibility(View.VISIBLE);
                    ll_pay.setVisibility(View.VISIBLE);
                }else{  //已显示 点击隐藏
                    is_paymethod = false;
                    img_right.setVisibility(View.VISIBLE);
                    img_bottom.setVisibility(View.GONE);
                    ll_pay.setVisibility(View.GONE);
                }

                break;
            case R.id.rebate_enter_ll_zhifubao://1支付宝
                if(is_zhifubao){

                }else{
                    paymethod = "1";//支付方式1支付宝 2.微信  3余额
                    is_yue = false;
                    is_zhifubao = true;
                    is_weixin = false;
                    img_yue.setVisibility(View.INVISIBLE);
                    img_zhifubao.setVisibility(View.VISIBLE);
                    img_weixin.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.rebate_enter_ll_weixin://2.微信
                if(is_weixin){

                }else{
                    paymethod = "2";//支付方式1支付宝 2.微信  3余额
                    is_yue = false;
                    is_zhifubao = false;
                    is_weixin = true;
                    img_yue.setVisibility(View.INVISIBLE);
                    img_zhifubao.setVisibility(View.INVISIBLE);
                    img_weixin.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rebate_enter_ll_yue://3余额
                if(is_yue){

                }else{
                    paymethod = "3";//支付方式1支付宝 2.微信  3余额
                    is_yue = true;
                    is_zhifubao = false;
                    is_weixin = false;
                    img_yue.setVisibility(View.VISIBLE);
                    img_zhifubao.setVisibility(View.INVISIBLE);
                    img_weixin.setVisibility(View.INVISIBLE);

//                    img_yue.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_select));
//                    img_zhifubao.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_select_no));
//                    img_weixin.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_select_no));
                }

                break;
            case R.id.rebate_enter_btn_confrim:
                if(!checkBox_xieyi.isChecked()){
                    Toasts.show("请阅读并同意注册协议");
                    break;
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm.hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_phone.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_tjr.getWindowToken(),0);
                str_name = edit_name.getText().toString().trim();
                str_phone = edit_phone.getText().toString().trim();
                str_tjr=edit_tjr.getText().toString().trim();
//                str_name,str_phone,str_money,str_yemoney;
                if(TextUtils.isEmpty(str_name)){
                    Toasts.show("姓名不能为空");
                    break;
                } else if(TextUtils.isEmpty(str_phone)){
                    Toasts.show("手机号不能为空");
                    break;
                }else if(!PhoneUtils.isMobileNO(str_phone)){
                    Toasts.show("您输入的手机号码格式不正确");
                    break;
                }else if(!(is_yue||is_zhifubao||is_weixin)){
                    //is_yue = false,is_zhifubao = false,is_weixin = false;
                    Toasts.show("请选择支付方式");
                    break;
                }else if("3".equals(paymethod)){//余额支付
                    if(money>yemoney){
                        Toasts.show("余额不足，请选择其他支付方式");
                    }else{
                        if("0".equals(MyApplication.getInstance().getPref_ispaypwd())){//未设置支付密码
                            showDialog("","您尚未设置支付密码，是否前往设置？");
                        }else if("1".equals(MyApplication.getInstance().getPref_ispaypwd())){//已设置支付密码
                            showPwdDialog("请输入支付密码","报名","￥"+str_money);
                        }
                    }
                }else{
                    apply();
                }
                break;
            case R.id.rebate_enter_tv_xieyi:
                Intent intent = new Intent();
                intent.putExtra("url", Constant.BASE_URL + Constant.URL_INDEX_REG_XIEYI);
                AppManager.getAppManager().startNextActivity(RebateEnterActivity.this, MyWebActivity2.class, intent);
                break;
            default:
                break;
        }
    }

    MaterialDialog mMaterialDialog;
    /**
     * 支付密码Dialog
     * */
    private void showPwdDialog(String title,String context,String money){
        View lastView = this.getLayoutInflater().inflate(R.layout.view_dialog_edit_pwd, null);
        TextView tv_title = (TextView)lastView.findViewById(R.id.view_dialog_tv_title);
        TextView tv_content = (TextView)lastView.findViewById(R.id.view_dialog_tv_context);
        TextView tv_money = (TextView)lastView.findViewById(R.id.view_dialog_tv_money);
        final GridPasswordView mGridPasswordView = (GridPasswordView)lastView.findViewById(R.id.mGridPasswordView);
        tv_title.setText(title);
        tv_content.setText(context);
        tv_money.setText(money);
//		tv_content.setText("请把智能戒指贴向手机...");
        mMaterialDialog = new MaterialDialog(this);
        mMaterialDialog.setView(lastView);
        if (mMaterialDialog != null) {
            mMaterialDialog.setTitle(R.string.prompt)
                    .setMessage(title)
                    .setPositiveButton(
                            R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    // 关闭软键盘
                                    imm.hideSoftInputFromWindow(mGridPasswordView.getWindowToken(), 0);
                                    String edit_pwd = mGridPasswordView.getPassWord();
                                    System.out.println("===========================pwd =  " + edit_pwd);
                                    if(TextUtils.isEmpty(edit_pwd)){
                                        Toasts.show("请输入密码");
                                    }else{
                                        zfpwd = mGridPasswordView.getPassWord();
                                        apply();
                                        mMaterialDialog.dismiss();
                                    }
                                }
                            }
                    )
                    .setNegativeButton(
                            R.string.cancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                }
                            }
                    )
                    .setCanceledOnTouchOutside(false)//点击外部不可消失dialog
                    .setOnDismissListener(
                            new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }
                            }
                    )
                    .show();
        } else {

        }
    }

    /**
     * 未设置支付密码
     * */
    private void showDialog(String title,String context){
        mMaterialDialog = new MaterialDialog(this);
        if (mMaterialDialog != null) {
            mMaterialDialog.setTitle(R.string.prompt)
                    .setMessage(context)
                    .setPositiveButton(
                            R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.putExtra("isPayPwd",isPayPwd);
                                    AppManager.getAppManager().startNextActivity(mContext, ForgetPayPwdActivity.class,intent);
                                    mMaterialDialog.dismiss();
                                }
                            }
                    )
                    .setNegativeButton(
                            R.string.cancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                }
                            }
                    )
                    .setCanceledOnTouchOutside(false)//点击外部不可消失dialog
                    .setOnDismissListener(
                            new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }
                            }
                    )
                    .show();
        } else {

        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
