package com.qixing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

/**
 * Created by wicep on 2015/12/23.
 * 账户余额
 */
public class MyAmountActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private TextView tv_bonus;
    private Button btn_confirm,btn_cash,btn_jf,btn_cash_record;
    private TextView tv_sell;

    private LinearLayout ll_sell;
    private String money,dls_money;
    private String isPayPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_my_amount, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        money = getIntent().getStringExtra("money");
        isPayPwd = getIntent().getStringExtra("isPayPwd");
        initView();
    }

    private void initView(){
        mTitleBar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("钱包");
        mTitleBar.setRightText("支付设置");
        mTitleBar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){

            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }


            @Override
            public void onClickRightCtv() {
                   super.onClickRightCtv();
                Intent intent = new Intent();
                intent.putExtra("isPayPwd",isPayPwd);
                if("0".equals(MyApplication.getInstance().getPref_ispaypwd())){//未设置支付密码
                    AppManager.getAppManager().startNextActivity(mContext, ForgetPayPwdActivity.class,intent);
                }else if("1".equals(MyApplication.getInstance().getPref_ispaypwd())){//已设置支付密码
                    AppManager.getAppManager().startNextActivity(mContext, ModifyPayPwdActivity.class,intent);
                }
            }
        });
        tv_bonus = (TextView)findViewById(R.id.my_amount_tv_bonus);
        tv_bonus.setText("￥"+money);
        btn_confirm = (Button)findViewById(R.id.my_amount_btn_confirm);
        btn_confirm.setOnClickListener(this);
        btn_cash = (Button)findViewById(R.id.my_amount_btn_cash);
        btn_cash.setOnClickListener(this);
        btn_jf= (Button) findViewById(R.id.my_amount_btn_jf_record);
        btn_jf.setOnClickListener(this);
        btn_cash_record= (Button) findViewById(R.id.my_amount_btn_cash_record);
        btn_cash_record.setOnClickListener(this);
    }


    private void initDatas(){

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()){
            case R.id.my_amount_btn_confirm://
                intent = new Intent();
                intent.putExtra("tag", "MyAmountActivity");//
                intent.putExtra("sjprice", "");//订单实际价格
                AppManager.getAppManager().startFragmentNextActivity(mContext,RechargeActivity.class,intent);
                break;
            case R.id.my_amount_btn_cash://提现  类型（1商城 2返利）	types
                intent = new Intent();
                intent.putExtra("tag", "MyAmountActivity");//
                intent.putExtra("type", "1");
                intent.putExtra("money", money);
                AppManager.getAppManager().startFragmentNextActivity(mContext,CashConfirmActivity.class,intent);
                break;
            case R.id.my_amount_btn_jf_record:
                AppManager.getAppManager().startFragmentNextActivity(mContext,JFAmountActivity.class);
                break;
            case R.id.my_amount_btn_cash_record:
                AppManager.getAppManager().startFragmentNextActivity(mContext,CashRecordActivity.class);
                break;
        }
    }
}
