package com.qixing.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 *
 */
public class SignatureActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private EditText edit_name;
    private TextView tv_num;

    private String str_Signature;

    private ResultBean mResultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_feedback, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        initView();
//        initDatas();
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("个性签名");
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
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm.hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
                str_Signature = edit_name.getText().toString().trim();
//                if(TextUtils.isEmpty(str_nickname)){
//                    Toasts.show("请输入您的新昵称");
//                }else{
//                }
                initDatas(str_Signature);
            }
        });

        edit_name = (EditText) findViewById(R.id.feedback_edit_name);
        tv_num = (TextView) findViewById(R.id.feedback_tv_num);
        edit_name.addTextChangedListener(new EditChangedListener());
    }

    class EditChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private int editStart;//光标开始位置
        private int editEnd;//光标结束位置
        private final int charMaxNum = 120;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            if (DEBUG)
//                Log.i(TAG, "输入文本之前的状态");
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (DEBUG)
//                Log.i(TAG, "输入文字中的状态，count是一次性输入字符数");
//            mTvAvailableCharNum.setText("还能输入" + (charMaxNum - s.length()) + "字符");
            tv_num.setText("" + (charMaxNum - s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {
//            if (DEBUG)
//                Log.i(TAG, "输入文字后的状态");
            /**
             *  得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制
             **/
            editStart = edit_name.getSelectionStart();
            editEnd = edit_name.getSelectionEnd();
            if (temp.length() > charMaxNum) {
                Toast.makeText(getApplicationContext(), "你输入的字数已经超过了限制！", Toast.LENGTH_LONG).show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                edit_name.setText(s);
                edit_name.setSelection(tempSelection);
            }

        }
    };


    private void initDatas(String str_Signature) {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("qianm", str_Signature);//+"/p/"+p
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_UPQIANM;
        System.out.println("===========================个人中心 个性签名 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================个人中心 个性签名 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        MyApplication.getInstance().setUname(edit_name.getText().toString().trim());
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", edit_name.getText().toString().trim());
                        //设置返回数据
                        SignatureActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        SignatureActivity.this.finish();
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
            case R.id.join_btn_call:
                break;
            default:
                break;
        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
