package com.qixing.welcome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.qixing.MainActivity;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.LoginBean;
import com.qixing.bean.UserInfoBean;
import com.qixing.bean.WXUserInfoBean;
import com.qixing.bean.ZFBUserInfoBean;
import com.qixing.global.Constant;
import com.qixing.utlis.DateUtils;
import com.qixing.view.imagecut.ImageTools;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.wxapi.WXEntryActivity;
import com.qixing.wxapi.alipay.H5PayDemoActivity;
import com.qixing.wxapi.alipay.PayResult;
import com.qixing.wxapi.alipay.SignUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 * 登录
 */
public class LoginActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private EditText edit_name,edit_pwd;
    private Button btn_login;
    private TextView tv_forpwd,tv_regist;

    private LinearLayout ll_wxlogin,ll_zfblogin,ll_qqlogin;

    private String str_name,str_pwd;

    private LoginBean mLoginBean;

    private ProgressDialog mProgressLoginDialog;

    private TextView tv_info;

    private String LOGIN_TYPE;
    public WXUserInfoBean mWXUserInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_login, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        initView();
        initDatas();
    }

    private void initView(){
        mTitleBar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("登录");
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

        edit_name = (EditText)findViewById(R.id.login_edit_name);
        edit_pwd = (EditText)findViewById(R.id.login_edit_pwd);
        btn_login = (Button)findViewById(R.id.login_btn_login);
        btn_login.setOnClickListener(this);
        tv_forpwd = (TextView)findViewById(R.id.login_tv_forpwd);
        tv_forpwd.setOnClickListener(this);
        tv_regist = (TextView)findViewById(R.id.login_tv_regist);
        tv_regist.setOnClickListener(this);

        ll_wxlogin = (LinearLayout)findViewById(R.id.login_ll_wxlogin);
        ll_wxlogin.setOnClickListener(this);
        ll_wxlogin.setVisibility(View.VISIBLE);

        ll_zfblogin = (LinearLayout)findViewById(R.id.login_ll_zfblogin);
        ll_zfblogin.setOnClickListener(this);
        ll_zfblogin.setVisibility(View.VISIBLE );

        ll_qqlogin = (LinearLayout)findViewById(R.id.login_ll_qqlogin);
        ll_qqlogin.setOnClickListener(this);
        ll_qqlogin.setVisibility(View.VISIBLE);

        tv_info = (TextView)findViewById(R.id.login_tv_info);
        tv_info.setVisibility(View.GONE);

    }

    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().AppExit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mProgressLoginDialog !=null){
            mProgressLoginDialog.dismiss();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//        if(!TextUtils.isEmpty(WXEntryActivity.IS_WX_LOGIN)){
//            Toasts.show("授权成功");
//            if("1".equals(WXEntryActivity.IS_WX_LOGIN)){//1:未绑定，绑定手机号
////                Intent intentUserinfo = new Intent();
////                intentUserinfo.putExtra("tag","WX");
////                AppManager.getAppManager().startNextActivity(this, ComplementUserinfoActivity.class,intentUserinfo);
//                MyApplication.getInstance().setLogin_type("WX");
//                AppManager.getAppManager().startNextActivity(mContext, MainActivity.class);
//                LoginActivity.this.finish();
//            }else if ("3".equals(WXEntryActivity.IS_WX_LOGIN)){//3:已绑定，可直接登录
//                MyApplication.getInstance().setIsLogining("1");
////                JPushInterface.setAliasAndTags(LoginActivity.this, MyApplication.getInstance().getUid(), null, new TagAliasCallback() {
////                    @Override
////                    public void gotResult(int i, String s, Set<String> set) {
////
////                    }
////                });
//                AppManager.getAppManager().startNextActivity(mContext, MainActivity.class);
//                LoginActivity.this.finish();
//                finish();
//            }
//        }
    }

    private void initDatas(){

    }

    private void login(){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();
        RequestParams params = new RequestParams();
        params.put("uname", edit_name.getText().toString().trim());
        params.put("password", edit_pwd.getText().toString().trim());
        final String url = Constant.BASE_URL+ Constant.URL_USERAPI_LOGIN;//
        System.out.println("===========================登录 url = " + url);
        System.out.println("===========================登录 params = " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {//18600277088
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================登录 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mLoginBean = new Gson().fromJson(response.toString(), LoginBean.class);
                    if (mLoginBean.getResult().equals("1")) {
                        MyApplication.getInstance().setUid(mLoginBean.getUid());
                        MyApplication.getInstance().setU_tel(mLoginBean.getU_phone());
                        MyApplication.getInstance().setNickname(mLoginBean.getUname());
                        MyApplication.getInstance().setUser_Head(mLoginBean.getTxurl());
                        MyApplication.getInstance().setQianm(mLoginBean.getQianm());
                        MyApplication.getInstance().setU_djpic(mLoginBean.getDjpic());
                        MyApplication.getInstance().setVip_dj(mLoginBean.getDengji());
                        if("男".equals(mLoginBean.getSex())){
                            MyApplication.getInstance().setSex("1");
                        }else{
                            MyApplication.getInstance().setSex("2");
                        }
                        MyApplication.getInstance().setIsLogining("1");
                        JPushInterface.resumePush(mContext);
                        JPushInterface.setAliasAndTags(LoginActivity.this, MyApplication.getInstance().getUid(), null, new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {

                            }
                        });
                        AppManager.getAppManager().startNextActivity(mContext, MainActivity.class);
                        LoginActivity.this.finish();
                        Toasts.show(mLoginBean.getMessage());
                    } else  {
                        Toasts.show(mLoginBean.getMessage());
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
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
            }
        });
    }

    private void getAccess_token(String singInfo){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();
        RequestParams params = new RequestParams();
        params.put("url", singInfo);
        final String url = Constant.BASE_URL+ Constant.URL_USERAPI_ZFB_TOKEN;//
        System.out.println("===========================支付宝登录获取access_token url = " + url);
        System.out.println("===========================支付宝登录获取access_token params = " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {//18600277088
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================支付宝登录获取access_token response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mUserInfoBean = new Gson().fromJson(response.toString(), UserInfoBean.class);
                    if (mUserInfoBean.getResult().equals("1")) {
//                        getUserInfoTest(mUserInfoBean.getList());
//                        String times = DateUtils.getNowTime();
//                        String singInfo = getSignInfo2(mUserInfoBean.getList(), times);
                        getUserInfo2(mUserInfoBean.getList());

//                        String times = DateUtils.getNowTime();
//                        String singInfo = getSignInfo2(access_token,times);//getSignInfo2(access_token,times);
//                        System.out.println("===========================支付宝登录获取用户信息 url = https://openapi.alipay.com/gateway.do?" + singInfo);
                    } else  {
                        Toasts.show(mLoginBean.getMessage());
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
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
            }
        });
    }

    //alipay.user.userinfo.share

    private void getUserInfo2(String access_token){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();
        RequestParams params = new RequestParams();
        String times = DateUtils.getNowTime();
        String singInfo = getSignInfo2(access_token,times);//getSignInfo2(access_token,times);
        params.put("url", "https://openapi.alipay.com/gateway.do?"+singInfo);
        params.put("status", "2");
        final String url = Constant.BASE_URL+ Constant.URL_USERAPI_THIRD_PARTY;//
        System.out.println("===========================支付宝登录获取用户信息 url = " + url);
        System.out.println("===========================支付宝登录获取用户信息 params = " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {//18600277088
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================支付宝登录获取用户信息 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mZFBUserInfoBean = new Gson().fromJson(response.toString(), ZFBUserInfoBean.class);
                    if ("".equals(mZFBUserInfoBean.getAlipay_user_info_share_response())||mZFBUserInfoBean.getAlipay_user_info_share_response()==null) {
                        Toasts.show("授权失败");
                    } else  {
                        if(TextUtils.isEmpty(mZFBUserInfoBean.getAlipay_user_info_share_response().getAvatar())){
                            img_base64 = "";
                            zfblast();
                        }else{
                            try {
                                downloadFile(mZFBUserInfoBean.getAlipay_user_info_share_response().getAvatar(),"ZFB");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
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
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
            }
        });
    }

    private void zfblast(){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();
        RequestParams params = new RequestParams();
//        支付宝id	zfb_id
//        昵称	nickname
//        性别	sex
//        头像	pic
        params.put("zfb_id", mZFBUserInfoBean.getAlipay_user_info_share_response().getUser_id());
        params.put("nickname", mZFBUserInfoBean.getAlipay_user_info_share_response().getNick_name());
        String sex;
        if(TextUtils.isEmpty(mZFBUserInfoBean.getAlipay_user_info_share_response().getGender())){
            sex = "";
        }else{
            sex = mZFBUserInfoBean.getAlipay_user_info_share_response().getGender()=="m"?"1":"2";
        }
        params.put("sex", sex);
        params.put("pic", img_base64);

        final String url = Constant.BASE_URL + Constant.URL_USERAPI_ZFBLAST;//
        System.out.println("===========================支付宝登录写入数据库 url = " + url);
        System.out.println("===========================支付宝登录写入数据库 params = " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {//18600277088
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================支付宝登录获写入数据库 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mUserInfoBean = new Gson().fromJson(response.toString(), UserInfoBean.class);
                    if ("1".equals(mUserInfoBean.getResult())) {//1:未绑定，绑定手机号
                        Toasts.show("授权成功");
//                        Intent intentUserinfo = new Intent();
//                        intentUserinfo.putExtra("tag","ZFB");
//                        AppManager.getAppManager().startNextActivity(mContext, ComplementUserinfoActivity.class, intentUserinfo);
                        MyApplication.getInstance().setIsLogining("1");
                        MyApplication.getInstance().setUid(mUserInfoBean.getUid());
                        MyApplication.getInstance().setNickname(mUserInfoBean.getUname());
                        MyApplication.getInstance().setUser_Head(mUserInfoBean.getTxurl());
//						Toasts.show("授权成功");
                        MyApplication.getInstance().setLogin_type(LOGIN_TYPE);
                        MyApplication.getInstance().setLogin_type("ZFB");
                        JPushInterface.resumePush(mContext);
                        JPushInterface.setAliasAndTags(LoginActivity.this, MyApplication.getInstance().getUid(), null, new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {

                            }
                        });
                        AppManager.getAppManager().startNextActivity(mContext, MainActivity.class);
                        finish();
                    } else if("3".equals(mUserInfoBean.getResult())){//3:已绑定，可直接登录
                        Toasts.show("授权成功");
                        MyApplication.getInstance().setUid(mUserInfoBean.getUid());
                        MyApplication.getInstance().setNickname(mUserInfoBean.getUname());
                        MyApplication.getInstance().setU_tel(mUserInfoBean.getU_phone());
                        MyApplication.getInstance().setUser_Head(mUserInfoBean.getTxurl());
                        MyApplication.getInstance().setIsLogining("1");
                        JPushInterface.resumePush(mContext);
                        JPushInterface.setAliasAndTags(LoginActivity.this, MyApplication.getInstance().getUid(), null, new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {

                            }
                        });
                        AppManager.getAppManager().startNextActivity(mContext, MainActivity.class);
                        finish();
                    } else  {
                        Toasts.show(mUserInfoBean.getResult());
                    }
                } else {
                    Toasts.show("授权失败");
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
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
            }
        });
    }


    public String getSignInfo2(String access_token,String times){
        String authInfo = getAuthInfo2(Constant.APP_ID_ZFB,access_token,times);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(authInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的登录授权信息
         */
        final String loginInfo = authInfo + "&sign=" + sign;// + "\"&" + getSignType()

        System.out.println("=========================== 支付宝第二次访问 loginInfo = " + loginInfo);
        final String loginInfoRep = loginInfo.toString().replace(" ","%20");
        System.out.println("=========================== 支付宝第二次访问替换空格 loginInfoRep = " + loginInfoRep);
        System.out.println("=========================== 支付宝第二次访问完整url = https://openapi.alipay.com/gateway.do?" + loginInfo);
        return loginInfoRep;
    }


    /**
     * create the order info. 创建订单信息
     *
     */
    private String getAuthInfo2(String app_id,String access_token,String times) {

//        params.put("app_id", Constant.APP_ID_ZFB);
//        params.put("method", "alipay.user.userinfo.share");
//        params.put("charset", "utf-8");
//        params.put("sign_type", "RSA");
//        params.put("timestamp", DateUtils.getNowTime());
//        params.put("version", "1.0");
//        params.put("auth_token", access_token);
        //服务对应的名称，常量值为com.alipay.account.auth
        String authInfo = "";//apiname=com.alipay.account.auth

        //  支付宝分配给开发者的应用ID
        authInfo += "app_id=" + app_id ;

        //
        authInfo += "&auth_token=" + access_token;

        //
        authInfo += "&charset=GBK";//utf-8 GBK

        //
//        authInfo += "&method=alipay.user.userinfo.share";
        authInfo += "&method=alipay.user.info.share";

        // 	签名的类型，常量值为RSA，暂不支持其他类型签名
        authInfo += "&sign_type=RSA";

        //
        authInfo += "&timestamp=" + times;

        // 	签名的类型，常量值为RSA，暂不支持其他类型签名
        authInfo += "&version=1.0";

//        // 	整个授权参数信息的签名，即此行以上参数key和value通过&拼接的字符串的签名值，对此字符串签名后需做URL编码
//        authInfo += "&sign=fMcp4GtiM6rxSIeFnJCVePJKV43eXrUP86CQgiLhDHH2u%2FdN75eEvmywc2ulkm7qKRetkU9fbVZtJIqFdMJcJ9Yp%2BJI%2FF%2FpESafFR6rB2fRjiQQLGXvxmDGVMjPSxHxVtIqpZy5FDoKUSjQ2%2FILDKpu3%2F%2BtAtm2jRw1rUoMhgt0%3D";

        return authInfo;
    }


        // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    private boolean checkWX(){
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
        api.registerApp(Constant.APP_ID);
        if(api!=null){
        }
        if( !api.isWXAppInstalled()){
            Toast.makeText(this, "请先安装微信应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( !api.isWXAppSupportAPI() ){
            Toast.makeText(this, "请先更新微信应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.login_btn_login:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm.hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_pwd.getWindowToken(), 0);
                str_name = edit_name.getText().toString().trim();
                str_pwd = edit_name.getText().toString().trim();
                if(TextUtils.isEmpty(str_name)||TextUtils.isEmpty(str_pwd)){
                    Toasts.show("用户名或密码不能为空");
                }else{
                    login();
//                    MyApplication.getInstance().setIsLogining("1");
//                    LoginActivity.this.finish();
                }
//                AppManager.getAppManager().startNextActivity(this, MainActivity.class);
//                finish();
                break;
            case R.id.login_tv_forpwd:
                AppManager.getAppManager().startNextActivity(this, ForgetPwdActivity.class);
//                this.finish();
                break;
            case R.id.login_tv_regist:
                AppManager.getAppManager().startNextActivity(this, RegistActivity.class);
//                this.finish();
                break;
            case R.id.login_ll_wxlogin://第三方登录 微信登录
//                if(mProgressLoginDialog == null){
//                    mProgressLoginDialog = new ProgressDialog(mContext);
//                }
//                mProgressLoginDialog.setMessage("微信跳转中...");
//                mProgressLoginDialog.show();
//                Toasts.show("暂未开放");
                Toasts.show("微信跳转中...");
                checkWX();
            // send oauth request
//                final SendAuth.Req req = new SendAuth.Req();
//                req.scope = "snsapi_userinfo";
//                req.state = "wechat_sdk_demo_test";
//                api.sendReq(req);
                LOGIN_TYPE = "WX";
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this,SHARE_MEDIA.WEIXIN,authListener);

                break;
            case R.id.login_ll_qqlogin://第三方登录 QQ登录
                Toasts.show("QQ跳转中...");
                LOGIN_TYPE = "QQ";
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this,SHARE_MEDIA.QQ,authListener);
                break;
            case R.id.login_ll_zfblogin://第三方登录 支付宝登录
//                Toasts.show("暂未开放");
//                APP_ID 	开放平台应用的APPID，详见创建应用并获取APPID  APP_ID_ZFB
//                APP_PRIVATE_KEY 	开发者应用私钥，详见配置密钥  RSA_PRIVATE
//                CHARSET 	请求和签名使用的字符编码格式，支持GBK和UTF-8  CHARSET
//                ALIPAY_PUBLIC_KEY 	支付宝公钥，详见配置密钥  RSA_PUBLIC
//                alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",Constant.APP_ID_ZFB,Constant.RSA_PRIVATE,"json",Constant.CHARSET,Constant.RSA_PUBLIC);
                LOGIN_TYPE = "ZFB";
                authZFB();
                break;
            default:
                break;
        }
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            System.out.println("=========================== UMeng登陆 platform = " + platform.toString());
            String temp = "";
            mWXUserInfoBean = new WXUserInfoBean();
            for (String key : data.keySet()) {
                temp= temp+key +" : "+data.get(key)+"\n";
                if("openid".equals(key)){
                    mWXUserInfoBean.setOpenid(data.get(key));
                }
                if("screen_name".equals(key)){
                    mWXUserInfoBean.setNickname(data.get(key).toString().trim());
                }
                if("gender".equals(key)){
                    mWXUserInfoBean.setSex(data.get(key));
                }
                if("profile_image_url".equals(key)){
                    mWXUserInfoBean.setHeadimgurl(data.get(key));
                }
                if("unionid".equals(key)){
                    mWXUserInfoBean.setUnionid(data.get(key));
                }
//                params.put("openid", mWXUserInfoBean.getOpenid());//		微信id	openid
//                params.put("nickname", mWXUserInfoBean.getNickname());//		昵称	nickname
//                params.put("sex", mWXUserInfoBean.getSex());//		性别	sex
//                params.put("pic", img_base64);//		头像	pic
//                params.put("unionid", mWXUserInfoBean.getUnionid());//		unionid	unionid
            }
//            tv_info.setText(temp);
            String str = new Gson().toJson(mWXUserInfoBean);
            System.out.println("=========================== UMeng 登陆 LOGIN_TYPE = "+LOGIN_TYPE+", str = " + str.toString());
            try {
                downloadFile(mWXUserInfoBean.getHeadimgurl(),LOGIN_TYPE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            tv_info.setText("错误"+t.getMessage());
            Toasts.show("授权失败，请稍后再试");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toasts.show("授权已取消");
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public String getSignInfo(){
        String authInfo = getAuthInfo1(Constant.APP_ID_ZFB);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(authInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的登录授权信息
         */
        final String loginInfo = authInfo + "&sign=" + sign;// + "\"&" + getSignType()

        System.out.println("=========================== 支付宝第一次访问 loginInfo = " + loginInfo);
        final String loginInfoRep = loginInfo.toString().replace(" ","%20");
        System.out.println("=========================== 支付宝第一次访问替换空格 loginInfoRep = " + loginInfoRep);
        return loginInfoRep;
    }

    //会员授权码 auth_code作为换取access_token的票据，auth_code只能使用一次，一天未被使用自动过期。
    private String auth_code;
    private String alipay_open_id;
    //支付宝会员ID
    private String user_id;
    private String result_code;
    private String success;
    //访问令牌。通过该令牌调用需要用户信息授权的接口，如alipay.user.userinfo.share
    private String access_token;

    private UserInfoBean mUserInfoBean;
    private ZFBUserInfoBean mZFBUserInfoBean;
    private Bitmap bitmap;
    private String img_base64;


    /**
     * 方法名称:transStringToMap
     * 传入参数:mapString 形如 username'chenziwen^password'1234
     * 返回值:Map
     */
    public static Map transStringToMap(String mapString){

        Map map = new HashMap();

        StringTokenizer items;

        for(StringTokenizer entrys = new StringTokenizer(mapString, "&");entrys.hasMoreTokens();

            map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))

            items = new StringTokenizer(entrys.nextToken(), "=");

        return map;

    }

    private void getUserInfoZFB(){
        // 必须异步调用
//        Thread access_tokenThread = new Thread(access_tokenRunnable);
//        access_tokenThread.start();

    }


    /*
 * *********************************************************支付宝 Start*************************************************************************
 * */
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    System.out.println("=========================== 支付宝 resultInfo =  " + resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                        success=true&auth_code=5760a22fcd0c4515aa5f5b996f88VX19&result_code=200&alipay_open_id=20880072593677144333703431910119&user_id=2088602154370190
                        Map myInfo = transStringToMap(resultInfo);
                        success = (String) myInfo.get("success");
                        //会员授权码 auth_code作为换取access_token的票据，auth_code只能使用一次，一天未被使用自动过期。
                        auth_code = (String) myInfo.get("auth_code");
                        result_code = (String) myInfo.get("result_code");
                        alipay_open_id = (String) myInfo.get("alipay_open_id");
                        //支付宝会员ID
                        user_id = (String) myInfo.get("user_id");
                        System.out.println("=========================== 支付宝 登录成功  success = " + success + ",auth_code = " + auth_code + ",result_code = " + result_code + ",alipay_open_id = " + alipay_open_id + ",user_id = " + user_id);
                        //"https://openapi.alipay.com/gateway.do",Constant.APP_ID_ZFB,Constant.RSA_PRIVATE,"json",Constant.CHARSET,Constant.RSA_PUBLIC
//                        getUserInfoZFB();
                        String interfaceurl = "https://openapi.alipay.com/gateway.do?";
                        String singInfo = getSignInfo();
                        getAccess_token(interfaceurl+singInfo);

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(LoginActivity.this, "授权结果确认中", Toast.LENGTH_SHORT).show();
//                            btn_confirm.setEnabled(true);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            btn_login.setEnabled(true);
                        }
                    }
                    break;
                case Constant.SDK_AUTH_FLAG:
                    String access_token = (String) msg.obj;
                    System.out.println("=========================== 支付宝mHandler access_token =  " + access_token);
//                    new Thread(userinfoRunnable).start();
                    break;
                case Constant.SDK_USERINFO_FLAG:
                    String userinfo = (String) msg.obj;
                    System.out.println("=========================== 支付宝mHandler Userinfo =  " + userinfo);
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * create the order info. 创建订单信息
     *
     */
    private String getAuthInfo1(String app_id) {

        //https://openapi.alipay.com/gateway.do?
        // app_id=2016062801561844
        // &charset=UTF-8
        // &code=ad91915a37d84fc2a28abf97cfe5PX40
        // &grant_type=authorization_code
        // &method=alipay.system.oauth.token
        // &sign=VhVQ8onwPXBVaEvy9m4LYQlgsjB%2FTe47fEb1MoF6rDux3XftNWaAI3zEAh7BJZX49u6ik4eWGx7eBxvDPploXQQitPkZ2OLQEKH5Qcyi8sZ%2FXDsOkVS0%2B%2B7vnXTgTk2J3SOZ9AXlDtCHlTamu%2BV5esDAts3eYeDSvfn7F4Y3DLg%3D
        // &sign_type=RSA
        // &timestamp=2016-09-03%2017:04:36
        // &version=1.0
        //服务对应的名称，常量值为com.alipay.account.auth
        String authInfo = "apiname=com.alipay.account.auth";

        //  支付宝分配给开发者的应用ID
        authInfo += "&app_id=" + app_id ;

        //
        authInfo += "&charset=UTF-8";

        //
        authInfo += "&code=" + auth_code;

        //
        authInfo += "&grant_type=authorization_code";

        //
        authInfo += "&method=alipay.system.oauth.token";

        // 	签名的类型，常量值为RSA，暂不支持其他类型签名
        authInfo += "&sign_type=RSA";

        //
        authInfo += "&timestamp=" + DateUtils.getNowTime();

        // 	签名的类型，常量值为RSA，暂不支持其他类型签名
        authInfo += "&version=1.0";

//        // 	整个授权参数信息的签名，即此行以上参数key和value通过&拼接的字符串的签名值，对此字符串签名后需做URL编码
//        authInfo += "&sign=fMcp4GtiM6rxSIeFnJCVePJKV43eXrUP86CQgiLhDHH2u%2FdN75eEvmywc2ulkm7qKRetkU9fbVZtJIqFdMJcJ9Yp%2BJI%2FF%2FpESafFR6rB2fRjiQQLGXvxmDGVMjPSxHxVtIqpZy5FDoKUSjQ2%2FILDKpu3%2F%2BtAtm2jRw1rUoMhgt0%3D";

        return authInfo;
    }

    /**
     * call alipay sdk pay. 调用SDK授权
     */
    private void authZFB(){
        if (TextUtils.isEmpty(Constant.PARTNER) || TextUtils.isEmpty(Constant.RSA_PRIVATE) || TextUtils.isEmpty(Constant.SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
//        if(mProgressLoginDialog == null){
//            mProgressLoginDialog = new ProgressDialog(mContext);
//        }
//        mProgressLoginDialog.setMessage("支付宝跳转中...");
//        mProgressLoginDialog.show();

        Toasts.show("支付宝跳转中...");

        String authInfo = getAuthInfo(Constant.APP_ID_ZFB,Constant.PARTNER);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(authInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的登录授权信息
         */
        final String loginInfo = authInfo + "&sign=" + sign;// + "\"&" + getSignType()

        System.out.println("=========================== 支付宝 loginInfo = " + loginInfo);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask aliAuth = new AuthTask(LoginActivity.this);
                // 调用登录接口，获取登录结果
                String result = aliAuth.auth(loginInfo, true);
                Message msg = new Message();
                msg.what = Constant.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(this, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.meituan.com";
        // url可以是一号店或者美团等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);

    }

    /**
     * create the order info. 创建订单信息
     *
     */
    private String getAuthInfo(String app_id, String pid) {

//        String authInfo = "apiname=com.alipay.account.auth" +
//                "&app_id=xxxxx" +
//                "&app_name=mc" +
//                "&auth_type=AUTHACCOUNT" +
//                "&biz_type=openservice" +
//                "&method=alipay.open.auth.sdk.code.get" +
//                "&pid=xxxxx" +
//                "&product_id=APP_FAST_LOGIN" +
//                "&scope=kuaijie" +
//                "&sign_type=RSA" +
//                "&target_id=20141225xxxx" +
//                "&sign=fMcp4GtiM6rxSIeFnJCVePJKV43eXrUP86CQgiLhDHH2u%2FdN75eEvmywc2ulkm7qKRetkU9fbVZtJIqFdMJcJ9Yp%2BJI%2FF%2FpESafFR6rB2fRjiQQLGXvxmDGVMjPSxHxVtIqpZy5FDoKUSjQ2%2FILDKpu3%2F%2BtAtm2jRw1rUoMhgt0%3D";

        //服务对应的名称，常量值为com.alipay.account.auth
        String authInfo = "apiname=com.alipay.account.auth";

        //  支付宝分配给开发者的应用ID
        authInfo += "&app_id=" + app_id ;

        //  调用来源方的标识，常量值为mc
        authInfo += "&app_name=mc";

        // 	标识授权类型，取值范围：AUTHACCOUNT代表授权； LOGIN代表登录
        authInfo += "&auth_type=LOGIN";

        //调用业务的类型，常量值为openservice
        authInfo += "&biz_type=openservice";

        // 	接口名称，常量值为alipay.open.auth.sdk.code.get
        authInfo += "&method=alipay.open.auth.sdk.code.get";

        // 	签约的支付宝账号对应的支付宝唯一用户号，以2088开头的16位纯数字组成
        authInfo += "&pid="+ pid ;

        // 	产品码，常量值为APP_FAST_LOGIN
        authInfo += "&product_id=APP_FAST_LOGIN";

        // 	授权范围，常量值为kuaijie
        authInfo += "&scope=kuaijie";

        // 	签名的类型，常量值为RSA，暂不支持其他类型签名
        authInfo += "&sign_type=RSA";

        // 	商户标识该次用户授权请求的ID，该值在商户端应保持唯一
        authInfo += "&target_id=20141225xxxx";

//        // 	整个授权参数信息的签名，即此行以上参数key和value通过&拼接的字符串的签名值，对此字符串签名后需做URL编码
//        authInfo += "&sign=fMcp4GtiM6rxSIeFnJCVePJKV43eXrUP86CQgiLhDHH2u%2FdN75eEvmywc2ulkm7qKRetkU9fbVZtJIqFdMJcJ9Yp%2BJI%2FF%2FpESafFR6rB2fRjiQQLGXvxmDGVMjPSxHxVtIqpZy5FDoKUSjQ2%2FILDKpu3%2F%2BtAtm2jRw1rUoMhgt0%3D";

        return authInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, Constant.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
    /**
     * **********************************************************支付宝支付 end*************************************************************************
     * */



    /**
     * @param url
     *            要下载的文件URL
     * @throws Exception
     */
    public void downloadFile(String url, final String type) throws Exception {

        // 指定文件类型
        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
        // 获取二进制数据如图片和其他文件
        mAsyncHttpClient.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] binaryData) {
                String tempPath = Environment.getExternalStorageDirectory()
                        .getPath() + "/temp.jpg";
                // TODO Auto-generated method stub
                // 下载成功后需要做的工作
//                progress.setProgress(0);
                //
                Log.e("binaryData:", "共下载了：" + binaryData.length);
                //
                Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,
                        binaryData.length);

                File file = new File(tempPath);
                // 压缩格式
                Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                // 压缩比例
                int quality = 100;
                try {
                    // 若存在则删除
                    if (file.exists())
                        file.delete();
                    // 创建文件
                    file.createNewFile();
                    //
                    OutputStream stream = new FileOutputStream(file);
                    // 压缩输出
                    bmp.compress(format, quality, stream);
                    // 关闭
                    stream.close();
                    //
                    System.out.println("===========下载成功 头像路径是"+tempPath);
                    bitmap= ImageTools.createThumbnail(tempPath, 200, 200);
                    img_base64=ImageTools.bitmapToBase64(bitmap);
                    if("ZFB".equals(type)){
                        zfblast();
                    }else if("WX".equals(type)){
                        wxUserInfoToServer("1");
                    }else if("QQ".equals(type)){
                        wxUserInfoToServer("3");
                    }

//                    Toast.makeText(mContext, "下载成功\n" + tempPath,Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, "下载失败", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                // TODO Auto-generated method stub
                super.onProgress(bytesWritten, totalSize);
                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                // 下载进度显示
//                progress.setProgress(count);
                Log.e("下载 Progress>>>>>", bytesWritten + " / " + totalSize);

            }

            @Override
            public void onRetry(int retryNo) {
                // TODO Auto-generated method stub
                super.onRetry(retryNo);
                // 返回重试次数
            }

        });
    }


    public static String IS_WX_LOGIN = "";


	private void wxUserInfoToServer(String status){
		if(mProgressDialog == null){
			mProgressDialog = new ProgressDialog(this);
		}
		mProgressDialog.setMessage("加载中...");
		mProgressDialog.show();
		RequestParams params = new RequestParams();
		params.put("status", status);//		类型	status  1微信  3 QQ
		params.put("openid", mWXUserInfoBean.getOpenid());//		微信id	openid
		params.put("nickname", mWXUserInfoBean.getNickname());//		昵称	nickname
		params.put("sex", mWXUserInfoBean.getSex());//		性别	sex
		params.put("pic", img_base64);//		头像	pic
		params.put("unionid", mWXUserInfoBean.getUnionid());//		unionid	unionid
		final String url = Constant.BASE_URL+ Constant.URL_USERAPI_THIRD_PARTY;//
		System.out.println("===========================WX第三方登录 url = " + url);
		System.out.println("===========================WX第三方登录 params = " + params.toString());
//		mAsyncHttpClient = new AsyncHttpClient();
		mAsyncHttpClient.post(LoginActivity.this, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}

				System.out.println("===========================WX第三方登录 response = " + response.toString());
				if (!TextUtils.isEmpty(response.toString())) {
					mUserInfoBean = new Gson().fromJson(response.toString(), UserInfoBean.class);
					if ("1".equals(mUserInfoBean.getResult())) {//1:未绑定，绑定手机号
						IS_WX_LOGIN = "1";
						MyApplication.getInstance().setUid(mUserInfoBean.getUid());
                        MyApplication.getInstance().setNickname(mUserInfoBean.getUname());
                        MyApplication.getInstance().setUser_Head(mUserInfoBean.getTxurl());
//						Toasts.show("授权成功");
                        MyApplication.getInstance().setLogin_type(LOGIN_TYPE);
                        MyApplication.getInstance().setIsLogining("1");
                        JPushInterface.resumePush(mContext);
                        JPushInterface.setAliasAndTags(LoginActivity.this, MyApplication.getInstance().getUid(), null, new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {

                            }
                        });
                        AppManager.getAppManager().startNextActivity(mContext, MainActivity.class);
                        LoginActivity.this.finish();
					} else if("3".equals(mUserInfoBean.getResult())){//3:已绑定，可直接登录
						IS_WX_LOGIN = "3";
						MyApplication.getInstance().setUid(mUserInfoBean.getUid());
						MyApplication.getInstance().setNickname(mUserInfoBean.getUname());
						MyApplication.getInstance().setU_tel(mUserInfoBean.getU_phone());
						MyApplication.getInstance().setUser_Head(mUserInfoBean.getTxurl());
                        MyApplication.getInstance().setIsLogining("1");
                        JPushInterface.resumePush(mContext);
                        JPushInterface.setAliasAndTags(LoginActivity.this, MyApplication.getInstance().getUid(), null, new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {

                            }
                        });
                        AppManager.getAppManager().startNextActivity(mContext, MainActivity.class);
                        LoginActivity.this.finish();
                        finish();
					} else  {
						Toasts.show(mUserInfoBean.getResult());
                    }
                } else {
					Toasts.show("授权失败");
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				Toasts.show("授权超时");
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				System.out.println("===========================throwable ,responseString =  " + responseString.toString());
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				Toasts.show("授权失败");
			}
		});
	}
}
