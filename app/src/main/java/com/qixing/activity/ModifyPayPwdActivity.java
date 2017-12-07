package com.qixing.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.utlis.MyTextUtils;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 * 修改支付密码页面
 */
public class ModifyPayPwdActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private EditText edit_oldpwd,edit_newpwd,edit_newpwd2;

    private TextView tv_forpwd;
    private Button btn_confrim;

    private String str_oldpwd,str_newpwd,str_newpwd2;

    private ResultBean mResultBean;

    public static Context context;
    private String isPayPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_modify_pay_pwd, null);
        setContentView(view);
        context = ModifyPayPwdActivity.this;
        isPayPwd = getIntent().getStringExtra("isPayPwd");
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        initView();
    }

    private void initView(){
        mTitleBar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("修改支付密码");
        mTitleBar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){

            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();

                AppManager.getAppManager().finishActivity();
            }


            @Override
            public void onClickRightCtv() {
                   super.onClickRightCtv();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                // 关闭软键盘
//                imm.hideSoftInputFromWindow(edit_oldpwd.getWindowToken(), 0);
//                imm.hideSoftInputFromWindow(edit_newpwd.getWindowToken(), 0);
//                imm.hideSoftInputFromWindow(edit_newpwd2.getWindowToken(), 0);
//
//                str_oldpwd = edit_oldpwd.getText().toString().trim();
//                str_newpwd = edit_newpwd.getText().toString().trim();
//                str_newpwd2 = edit_newpwd2.getText().toString().trim();
//                if(TextUtils.isEmpty(str_oldpwd)||TextUtils.isEmpty(str_newpwd)||TextUtils.isEmpty(str_newpwd2)){
//                    Toasts.show("密码不能为空");
//                }else if(!str_newpwd.equals(str_newpwd2)){
//                    Toasts.show("两次密码输入不一致");
//                }else if(str_newpwd.length()<6||str_newpwd.length()>20){
//                    Toasts.show("登录密码最低六位最高二十位");
//                }else{
//                    initDatas();
//                }
            }
        });
        edit_oldpwd = (EditText)findViewById(R.id.modify_pay_pwd_edit_oldpwd);
        edit_newpwd = (EditText)findViewById(R.id.modify_pay_pwd_edit_newpwd);
        edit_newpwd2 = (EditText)findViewById(R.id.modify_pay_pwd_edit_newpwd2);

        tv_forpwd = (TextView) findViewById(R.id.modify_pay_pwd_tv_forpwd);
        tv_forpwd.setOnClickListener(this);
        btn_confrim = (Button) findViewById(R.id.modify_pay_pwd_btn_confrim);
        btn_confrim.setOnClickListener(this);
    }


    private void initDatas(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        mAsyncHttpClient = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("uid",  MyApplication.getInstance().getUid());
        params.put("zfpwd",  edit_oldpwd.getText().toString().trim());// 原支付密码	zfpwd
        params.put("zfpwd1",  edit_newpwd.getText().toString().trim());//新密码	zfpwd1
        params.put("zfpwd2",  edit_newpwd2.getText().toString().trim());//重复新密码	zfpwd2
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_UPZFPWD;
        System.out.println("===========================修改支付密码url ====== " + url);
        System.out.println("===========================params ====== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================个人中心 支付密码修改 response ====== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
//                        MyApplication.getInstance().setPassword(edit_newpwd.getText().toString().trim());
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
                showErrorDialog(mContext);
            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.modify_pay_pwd_tv_forpwd:
                Intent intent = new Intent();
                intent.putExtra("isPayPwd",isPayPwd);
                AppManager.getAppManager().startFragmentNextActivity(mContext,ForgetPayPwdActivity.class,intent);
                break;
            case R.id.modify_pay_pwd_btn_confrim://店铺头像
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm.hideSoftInputFromWindow(edit_oldpwd.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_newpwd.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_newpwd2.getWindowToken(), 0);

                str_oldpwd = edit_oldpwd.getText().toString().trim();
                str_newpwd = edit_newpwd.getText().toString().trim();
                str_newpwd2 = edit_newpwd2.getText().toString().trim();
                if(TextUtils.isEmpty(str_oldpwd)||TextUtils.isEmpty(str_newpwd)||TextUtils.isEmpty(str_newpwd2)){
                    Toasts.show("密码不能为空");
                    break;
                }else if(str_oldpwd.length()!=6||(!MyTextUtils.isNumber(str_oldpwd))){
                    Toasts.show("支付密码为六位纯数字");
                    break;
                }else if(!str_newpwd.equals(str_newpwd2)){
                    Toasts.show("两次密码输入不一致");
                    break;
                }else{
                    initDatas();
                }
                break;
        }
    }

}
