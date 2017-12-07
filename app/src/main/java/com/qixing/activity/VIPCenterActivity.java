package com.qixing.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.view.titlebar.BGATitlebar;

public class VIPCenterActivity extends BaseActivity {

    private BGATitlebar mTitlebar;

    private static Context context;
    private Button btn_kf,btn_confirm;
    private ImageView iv_vip;
    private TextView tv_vip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vipcenter);

        context=VIPCenterActivity.this;
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        initView();
        initDatas();
    }

    private void initView(){
        mTitlebar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitlebar.setTitleText("会员中心");
        mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }
        });

        iv_vip= (ImageView) findViewById(R.id.vip_center_iv_biaoshi);
        tv_vip= (TextView) findViewById(R.id.vip_center_tv_intro);
        btn_kf= (Button) findViewById(R.id.vip_center_btn_kaitong);
        btn_kf.setOnClickListener(this);
        btn_confirm= (Button) findViewById(R.id.vip_center_btn_vip_kaitong);
        btn_confirm.setOnClickListener(this);
    }

    private void initDatas(){
        if (!TextUtils.isEmpty(MyApplication.getInstance().getVip_dj())){
            if ("1".equals(MyApplication.getInstance().getVip_dj())){
                btn_confirm.setVisibility(View.GONE);
                btn_kf.setVisibility(View.GONE);
                iv_vip.setImageDrawable(getResources().getDrawable(R.drawable.icon_vip_show));
                tv_vip.setText("会员标识已点亮");
            }else{
                btn_confirm.setVisibility(View.VISIBLE);
                btn_kf.setVisibility(View.VISIBLE);
                iv_vip.setImageDrawable(getResources().getDrawable(R.drawable.icon_not_vip));
                tv_vip.setText("会员标识未点亮");
            }
        }else{
            btn_confirm.setVisibility(View.VISIBLE);
            btn_kf.setVisibility(View.VISIBLE);
            iv_vip.setImageDrawable(getResources().getDrawable(R.drawable.icon_not_vip));
            tv_vip.setText("会员标识未点亮");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.vip_center_btn_kaitong:
                AppManager.getAppManager().startNextActivity(context,RebateEnterActivity.class);
                break;
            case R.id.vip_center_btn_vip_kaitong:
                AppManager.getAppManager().startNextActivity(context,RebateEnterActivity.class);
                break;
        }
    }
}
