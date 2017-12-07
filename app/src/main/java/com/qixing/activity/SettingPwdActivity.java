package com.qixing.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.RegistBean;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.utlis.PhoneUtils;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.TimeButton;
import com.qixing.widget.Toasts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 * 修改密码页面
 */
public class SettingPwdActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private EditText edit_phone,edit_newpwd,edit_code;
    private TimeButton btn_time;

    private String str_phone,str_newpwd,str_code;

    private ResultBean mResultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_setting_pwd, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        initView();

        btn_time.onCreate(savedInstanceState);
    }

    private void initView(){
        mTitleBar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("修改密码");
        mTitleBar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){

            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();

                AppManager.getAppManager().finishActivity();
            }


            @Override
            public void onClickRightCtv() {
                   super.onClickRightCtv();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm.hideSoftInputFromWindow(edit_phone.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_code.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_newpwd.getWindowToken(), 0);

                str_phone = edit_phone.getText().toString().trim();
                str_newpwd = edit_newpwd.getText().toString().trim();
                str_code = edit_code.getText().toString().trim();
                if(TextUtils.isEmpty(str_newpwd)){
                    Toasts.show("密码不能为空");
                }else if(TextUtils.isEmpty(str_code)){
                    Toasts.show("验证码不能为空");
                }else if(str_newpwd.length()<6||str_newpwd.length()>20){
                    Toasts.show("登录密码最低六位最高二十位");
                }else{
                    initDatas();
                }
            }
        });
        edit_phone = (EditText)findViewById(R.id.setting_pwd_edit_phone);
        btn_time = (TimeButton)findViewById(R.id.setting_pwd_btn_time);
        btn_time.setOnClickListener(this);
        edit_code = (EditText)findViewById(R.id.setting_pwd_edit_code);
        edit_newpwd = (EditText)findViewById(R.id.setting_pwd_edit_newpwd);
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
        params.put("wjmm", "1");
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_FDX;//
        System.out.println("===========================修改密码 获取手机验证码url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================response ===== " + response);
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        Toasts.show("验证码已发送至您手机！");
                        btn_time.StartTimer();
                    } else  {
                        Toasts.show(mResultBean.getMessage() + "");
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
    private void initDatas(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();
        final RequestParams params = new RequestParams();
        params.put("uid",  MyApplication.getInstance().getUid());
        params.put("phone",  edit_phone.getText().toString().trim());
        params.put("code",  edit_code.getText().toString().trim());
        params.put("pwd",  edit_newpwd.getText().toString().trim());
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_FORGPWD;//
        System.out.println("===========================修改登录密码url ====== " + url);
        System.out.println("===========================params ====== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================修改登录密码 response ====== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        MyApplication.getInstance().setPassword(edit_newpwd.getText().toString().trim());
                        AppManager.getAppManager().finishActivity();
                        Toasts.show(mResultBean.getMessage());
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
                showTimeoutDialog(mContext);
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
            case R.id.setting_pwd_btn_time:
                InputMethodManager immTime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘edit_name,edit_phone,edit_code,edit_pwd,edit_pwd2;
                immTime.hideSoftInputFromWindow(edit_phone.getWindowToken(), 0);
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

}
