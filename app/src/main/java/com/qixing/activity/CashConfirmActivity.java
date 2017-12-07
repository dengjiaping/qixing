package com.qixing.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.jungly.gridpasswordview.GridPasswordView;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.ResultBean;
import com.qixing.bean.ReviseBankJson;
import com.qixing.global.Constant;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by wicep on 2015/12/23.
 * 提现
 */
public class CashConfirmActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private Button btn_confirm;
    private EditText edit_sum, edit_amount, edit_paypwd;
    private LinearLayout ll_bank;
    private TextView tv_kfphone;

    private TextView tv_bankname, tv_bankno;
    private String strNamebank, strBankID;
    private ResultBean mResultBean;

    private String type = "";
    private String money = "";

    public static final int RESULT_OK = -1;
    private static final int REQUEST_BANK = 1;
//    private JoinBean mJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_cash_confirm, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        type = getIntent().getStringExtra("type");
        money = getIntent().getStringExtra("money");
        System.out.println("===========================提现 type ===== " + type);
        initView();
//        initDatas();
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("提现");
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

        edit_sum = (EditText) findViewById(R.id.cash_confirm_edit_sum);
        edit_sum.setText("￥" + money);
        edit_amount = (EditText) findViewById(R.id.cash_confirm_edit_amount);
        edit_paypwd = (EditText) findViewById(R.id.cash_edit_paypwd);
        ll_bank = (LinearLayout) findViewById(R.id.cash_confirm_ll_bank);
        ll_bank.setOnClickListener(this);
        tv_bankname = (TextView) findViewById(R.id.cash_confirm_tv_bankname);
        tv_bankno = (TextView) findViewById(R.id.cash_confirm_tv_bankno);
        tv_kfphone = (TextView) findViewById(R.id.cash_confirm_tv_kfphone);
        tv_kfphone.setText(MyApplication.getInstance().getKf_phone());
        tv_kfphone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_kfphone.getPaint().setAntiAlias(true);//设置抗锯齿

        tv_kfphone.setOnClickListener(this);

        btn_confirm = (Button) findViewById(R.id.cash_confirm_btn_confirm);
        btn_confirm.setOnClickListener(this);
    }


    private void cash(String edit_pwd) {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("type", type);//        类型	type(1账户余额   2分销返利)
        params.put("bankid", strBankID);//        银行卡id	bankid
        params.put("money", edit_amount.getText().toString().trim());//        提现金额	money
        params.put("zfpwd", edit_pwd);//        支付密码	zfpwd
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_APPLAY_TX;//
        System.out.println("=========================== 提现url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("=========================== 提现 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
//                        ((Activity)CashActivity.context).finish();
                        finish();
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
        Intent intent;
        switch (v.getId()) {
            case R.id.cash_confirm_ll_bank:
                intent = new Intent(mContext, MyBankActivity.class);
                startActivityForResult(intent, REQUEST_BANK);
                break;
            case R.id.cash_confirm_btn_confirm:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm.hideSoftInputFromWindow(edit_amount.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edit_paypwd.getWindowToken(), 0);
                String str_amount = edit_amount.getText().toString().trim();
                String str_paypwd = edit_paypwd.getText().toString().trim();
                if (TextUtils.isEmpty(str_amount)) {
                    Toasts.show("请填写提现金额");
                } else if (TextUtils.isEmpty(strBankID)) {
                    Toasts.show("请选择提现帐号");
                } else {
                    showPwdDialog("请输入支付密码", "提现", "￥" + str_amount);
                }
                break;
            case R.id.cash_confirm_tv_kfphone:
                intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tv_kfphone.getText().toString().trim()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    MaterialDialog mMaterialDialog;

    /**
     * 支付密码Dialog
     */
    private void showPwdDialog(String title, String context, String money) {
        View lastView = this.getLayoutInflater().inflate(R.layout.view_dialog_edit_pwd, null);
        TextView tv_title = (TextView) lastView.findViewById(R.id.view_dialog_tv_title);
        TextView tv_content = (TextView) lastView.findViewById(R.id.view_dialog_tv_context);
        TextView tv_money = (TextView) lastView.findViewById(R.id.view_dialog_tv_money);
        final GridPasswordView mGridPasswordView = (GridPasswordView) lastView.findViewById(R.id.mGridPasswordView);
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
                                    if (TextUtils.isEmpty(edit_pwd)) {
                                        Toasts.show("请输入密码");
                                    } else {
                                        cash(edit_pwd);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_BANK://修改头像
                if (resultCode == RESULT_OK) {
                    String result = data.getExtras().getString("result");//得到Activity 关闭后返回的数据
                    System.out.println("===========================REQUEST_BANK  result = " + result);
                    ReviseBankJson.Bank mBank = new Gson().fromJson(result, ReviseBankJson.Bank.class);
                    String str = mBank.getCard_num();
                    System.out.println("===========================银行卡号 = " + str);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.length() < 4) {
//                            System.out.println("===========================不够四位四位 = " + str);
                            tv_bankno.setText("尾号：" + str);
                        } else {
//                            System.out.println("===========================后四位 = " + str.substring(str.length() - 4, str.length()));
                            tv_bankno.setText("尾号：" + str.substring(str.length() - 4, str.length()));
                        }
                    }
                    tv_bankname.setText(mBank.getName());
                    strBankID = mBank.getBankid();
                }
                break;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
