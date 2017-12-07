package com.qixing.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.RegistBean;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.utlis.MyTextUtils;
import com.qixing.utlis.PhoneUtils;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.TimeButton;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 * 忘记支付密码
 */
public class ForgetPayPwdActivity extends BaseActivity {

    private BGATitlebar mTitleBar;
    private TimeButton btn_time;

    private EditText edit_phone,edit_code,edit_pay_pwd;
    private Button btn_confrim;

    private String str_phone,str_code,str_pay_pwd,str_pay_pwd2;

    private ResultBean mResultBean;
    private RegistBean mRegistBean;

    private String isPayPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_forget_pay_pwd, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        isPayPwd = getIntent().getStringExtra("isPayPwd");
        initView();
        initDatas();
        btn_time.onCreate(savedInstanceState);
    }

    private void initView(){
        mTitleBar= (BGATitlebar) findViewById(R.id.mTitleBar);
        if("0".equals(MyApplication.getInstance().getPref_ispaypwd())){//未设置支付密码
            mTitleBar.setTitleText("设置支付密码");
        }else if("1".equals(MyApplication.getInstance().getPref_ispaypwd())){//已设置支付密码
            mTitleBar.setTitleText("找回支付密码");
        }
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

        edit_phone = (EditText)findViewById(R.id.forget_pay_pwd_edit_phone);
        edit_code = (EditText)findViewById(R.id.forget_pay_pwd_edit_code);
        edit_pay_pwd = (EditText)findViewById(R.id.forget_pay_pwd_edit_pwd);
        btn_time = (TimeButton)findViewById(R.id.forget_pay_pwd_btn_time);
        btn_time.setOnClickListener(this);
        btn_confrim = (Button)findViewById(R.id.forget_pay_pwd_btn_confrim);
        btn_confrim.setOnClickListener(this);

    }


    private void initDatas(){
//        getPhoneCode();
    }

    /**
     * 获取验证码
     * */
    private void getPhoneCode(){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();
        final RequestParams params = new RequestParams();
        params.put("phone", edit_phone.getText().toString());
        params.put("wjmm", "1");//忘记密码时	wjmm
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_FDX;//
        System.out.println("===========================找回支付密码 发送手机验证码url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================找回支付密码 发送手机验证码response ===== " + response);
                if (!TextUtils.isEmpty(response.toString())) {
                    mRegistBean = new Gson().fromJson(response.toString(), RegistBean.class);
                    if (mRegistBean.getResult().equals("1")) {
                        Toasts.show("验证码已发送至您手机！");
                        btn_time.StartTimer();
                    } else  {
                        Toasts.show(mRegistBean.getMessage() + "");
                    }
                } else {
                    showErrorDialog(mContext);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toasts.show(R.string.service_wrong);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
            }
        });

    }

    /**
     * 注册
     * */
    private void regist(){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();

//        手机号	phone
//        手机验证码	code
//        新密码	zfpwd
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("phone", edit_phone.getText().toString().trim());
        params.put("code", edit_code.getText().toString().trim());
        params.put("zfpwd", edit_pay_pwd.getText().toString().trim());
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_FORGZFPWD;//
        System.out.println("===========================找回支付密码 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================找回支付密码 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        Toasts.show(mResultBean.getMessage());
//                        AppManager.getAppManager().startNextActivity(LoginActivity.class);
                        if("0".equals(MyApplication.getInstance().getPref_ispaypwd())){//未设置支付密码
                            MyApplication.getInstance().setPref_ispaypwd("1");
                        }else if("1".equals(MyApplication.getInstance().getPref_ispaypwd())){//已设置支付密码
                            ((Activity)ModifyPayPwdActivity.context).finish();
                        }
                        ForgetPayPwdActivity.this.finish();
                    } else {
                        Toasts.show(mResultBean.getMessage());
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
                showErrorDialog(mContext);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
            }
        });
    }




    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.forget_pay_pwd_btn_confrim:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm.hideSoftInputFromWindow(edit_phone.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_code.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_pay_pwd.getWindowToken(), 0);
                str_phone = edit_phone.getText().toString().trim();
                str_code = edit_code.getText().toString().trim();
                str_pay_pwd = edit_pay_pwd.getText().toString().trim();
                if(TextUtils.isEmpty(str_code)){
                    Toasts.show("验证码不能为空");
                    break;
                }else if(TextUtils.isEmpty(str_phone)){
                    Toasts.show("手机号不能为空");
                    break;
                }else if(!PhoneUtils.isMobileNO(str_phone)){
                    Toasts.show("您输入的手机号码格式不正确");
                    break;
                }else if(TextUtils.isEmpty(str_pay_pwd)){
                    Toasts.show("请设置支付密码");
                    break;
                }else if(str_pay_pwd.length()!=6||(!MyTextUtils.isNumber(str_phone))){
                    Toasts.show("支付密码为六位纯数字");
                    break;
                }else{
                    regist();
                }
                break;
            case R.id.forget_pay_pwd_btn_time:
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm1.hideSoftInputFromWindow(edit_phone.getWindowToken(), 0);
                str_phone = edit_phone.getText().toString().trim();
                if(TextUtils.isEmpty(str_phone)){
                    Toasts.show("手机号不能为空");
                }else if(!PhoneUtils.isMobileNO(str_phone)){
                    Toasts.show("您输入的手机号码格式不正确");
                    break;
                }else{
                    getPhoneCode();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        btn_time.onDestroy();
    }
}
