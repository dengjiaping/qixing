package com.qixing.welcome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.MainActivity;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.RegistBean;
import com.qixing.global.Constant;
import com.qixing.utlis.PhoneUtils;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.TimeButton;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 * 注册
 */
public class ForgetPwdActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private EditText edit_name,edit_phone,edit_code,edit_pwd,edit_pwd2,edit_paypwd,edit_tjphone;
    private TimeButton btn_time;
    private Button btn_confrim;

    private String str_name,str_phone,str_code,str_pwd,str_pwd2,str_paypwd,str_tjphone;
    private CheckBox cb;
    private TextView tv_xieyi;
    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;

    private RegistBean mRegistBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_forget_pwd, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        initView();
//        initDatas();
        btn_time.onCreate(savedInstanceState);
    }

    private void initView(){
        mTitleBar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("忘记密码");
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

        edit_name = (EditText)findViewById(R.id.forget_pwd_edit_name);
        edit_phone =(EditText)findViewById(R.id.forget_pwd_edit_phone);
        edit_code =(EditText)findViewById(R.id.forget_pwd_edit_code);
        edit_pwd = (EditText)findViewById(R.id.forget_pwd_edit_pwd);
        edit_pwd2 = (EditText)findViewById(R.id.forget_pwd_edit_pwd2);
        edit_paypwd = (EditText)findViewById(R.id.forget_pwd_edit_paypwd);
        edit_tjphone = (EditText)findViewById(R.id.forget_pwd_edit_tjphone);
        btn_time = (TimeButton)findViewById(R.id.forget_pwd_btn_time);
        btn_time.setOnClickListener(this);
        btn_confrim = (Button)findViewById(R.id.forget_pwd_btn_confrim);
        btn_confrim.setOnClickListener(this);

        cb = (CheckBox) findViewById(R.id.forget_pwd_cb);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    Toasts.show("您已阅读并同意注册协议！");
                }
            }
        });
        tv_xieyi = (TextView) findViewById(R.id.forget_pwd_tv_xieyi);
        tv_xieyi.setOnClickListener(this);
    }


    private void initDatas(){

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
        System.out.println("===========================忘记密码获取手机验证码url ===== " + url);
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
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
            }
        });

    }

    private void forgetPwd(){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();
//        手机号	phone
//        手机验证码	code
//        新密码	pwd
//edit_name,edit_phone,edit_code,edit_pwd,edit_pwd2,edit_paypwd;
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("phone", edit_phone.getText().toString().trim());
        params.put("code", edit_code.getText().toString().trim());
        params.put("pwd", edit_pwd.getText().toString().trim());
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_FORGPWD;//
        System.out.println("===========================忘记登录密码 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================忘记登录密码response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mRegistBean = new Gson().fromJson(response.toString(), RegistBean.class);
                    if (mRegistBean.getResult().equals("1")) {
                        Toasts.show(mRegistBean.getMessage());
//                        MyApplication.getInstance().setUid(mRegistBean.getUid());
//                        MyApplication.getInstance().setUname(mRegistBean.getUname());
//                        MyApplication.getInstance().setIsLogining("1");
//                        Intent intent = new Intent();
//                        intent.putExtra("vp", 3);
//                        intent.putExtra("isRegist", "1");
//                        intent.putExtra("tishi", mRegistBean.getTishi());
//                        AppManager.getAppManager().startNextActivity(mContext, MainActivity.class, intent);
                        AppManager.getAppManager().startNextActivity(mContext, LoginActivity.class);

                        ForgetPwdActivity.this.finish();
                    } else  {
                        Toasts.show(mRegistBean.getMessage());
                    }
                } else {
                    mAlertDialog = new AlertDialog.Builder(mContext)
                            .setTitle(R.string.dialog_prompt)
                            .setMessage(R.string.dialog_wrong)
                            .setPositiveButton(R.string.dialog_ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialoginterface, int i) {
                                            mAlertDialog.dismiss();
//                                        finish();
                                        }
                                    }).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mAlertDialog = new AlertDialog.Builder(mContext)
                        .setTitle(R.string.dialog_prompt)
                        .setMessage(R.string.dialog_wrong)
                        .setPositiveButton(R.string.dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        mAlertDialog.dismiss();
//                                        finish();
                                    }
                                }).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
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
            case R.id.forget_pwd_btn_time:
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
            case R.id.forget_pwd_btn_confrim:
//                if(!cb.isChecked()){
//                    Toasts.show("请阅读并同意注册协议！");
//                    break;
//                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘edit_name,edit_phone,edit_code,edit_pwd,edit_pwd2;
                imm.hideSoftInputFromWindow(edit_phone.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_code.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_pwd.getWindowToken(), 0);
                str_phone = edit_phone.getText().toString().trim();
                str_code = edit_code.getText().toString().trim();
                str_pwd = edit_pwd.getText().toString().trim();
                if(TextUtils.isEmpty(str_phone)){
                    Toasts.show("手机号不能为空");
                    break;
                }else if(!PhoneUtils.isMobileNO(str_phone)){
                    Toasts.show("您输入的手机号码格式不正确");
                    break;
                }else if(TextUtils.isEmpty(str_code)){
                    Toasts.show("手机验证码不能为空");
                    break;
                }else if(TextUtils.isEmpty(str_pwd)){
                    Toasts.show("密码不能为空");
                    break;
                }else if(str_pwd.length()<6||str_pwd.length()>20){
                    Toasts.show("登录密码最低六位最高二十位");
                    break;
                }
//                else if(!str_pwd.equals(str_pwd2)){
//                    System.out.println("===========================str_pwd ="+str_pwd+"===str_pwd2 = " + str_pwd2);
//                    Toasts.show("两次密码输入不一致");
//                    break;
//                }else if(TextUtils.isEmpty(str_paypwd)){
//                    Toasts.show("支付密码不能为空");
//                    break;
//                }else if(str_paypwd.length()<6||str_paypwd.length()>20){
//                    Toasts.show("支付密码最低六位最高二十位");
//                    break;
//                }
                else{
                    forgetPwd();
//                    AppManager.getAppManager().startNextActivity(this, MainActivity.class);
//                    finish();
                }
                break;
            case R.id.forget_pwd_tv_xieyi:
//                Intent intent = new Intent();
//                intent.putExtra("url", Constant.URL_BASE_SHOP+Constant.URL_WAP_XIEYI+"/type/1");
//                AppManager.getAppManager().startNextActivity(XieyiWebActivity.class, intent);
//                mAlertDialog = new AlertDialog.Builder(mContext)
//                        .setTitle(R.string.dialog_prompt)
//                        .setMessage(R.string.dialog_wrong)
//                        .setPositiveButton(R.string.dialog_ok,
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(
//                                                DialogInterface dialoginterface, int i) {
//                                            mAlertDialog.dismiss();
//                                            //finish();
//                                        }
//                                    }).show();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        btn_time.onDestroy();
    }
}
