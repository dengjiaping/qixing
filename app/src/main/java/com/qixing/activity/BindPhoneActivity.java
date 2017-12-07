package com.qixing.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.bean.RegistBean;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.TimeButton;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 * 绑定账号
 */
public class BindPhoneActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private EditText edit_phone,edit_code;
    private TextView tv_context;
    private TimeButton btn_time;

    private String state = "1";

    private ResultBean mResultBean;
//    private JoinBean mJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_bind_phone, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        initView();
        initDatas();
        btn_time.onCreate(savedInstanceState);
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("绑定账号");
        mTitleBar.setRightText("保存");
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
        edit_phone =(EditText)findViewById(R.id.bind_phone_edit_phone);
        edit_code = (EditText)findViewById(R.id.bind_phone_edit_code);
        tv_context = (TextView)findViewById(R.id.bind_phone_tv_context);
        btn_time = (TimeButton)findViewById(R.id.bind_phone_btn_time);
        btn_time.setOnClickListener(this);

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
        params.put("zc", "1");
        final String url = Constant.BASE_URL;//+Constant.URL_USERAPI_FDX
        System.out.println("===========================修改手机获取手机验证码url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================修改手机获取手机验证码response ===== " + response);
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
        switch (v.getId()) {
            case R.id.bind_phone_btn_time:

                break;
            case R.id.bind_account_ll_phone:

                break;
            default:
                break;
        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
