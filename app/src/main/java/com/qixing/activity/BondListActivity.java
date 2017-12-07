package com.qixing.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.adapter.LiveGiftAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.LiveGiftBean;
import com.qixing.bean.LiveGiftJson;
import com.qixing.global.Constant;
import com.qixing.view.CircleImageView;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class BondListActivity extends BaseActivity {

    private BGATitlebar mTitlebar;

    private LinearLayout ll_first, ll_second, ll_third;
    private CircleImageView img_first, img_second, img_third;
    private TextView tv_name, tv_name2, tv_name3, tv_devote, tv_devote2, tv_devote3;
    private View view_line;

    private ListView mListView;
    private LiveGiftAdapter mGiftAdapter;
    private LiveGiftJson mGiftJson;
    private List<LiveGiftBean> mList;
    private List<LiveGiftBean> list_devote;

    private String liveId="";
    private static Context context;

    private int p=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bond_list);

        context = BondListActivity.this;
        initStatusbar(context, R.color.color_titlebar_default);

        liveId=getIntent().getStringExtra("zbid");

        initView();
        initDatas();
    }

    private void initView() {
        mTitlebar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitlebar.setTitleText("魅力值排行榜");
        mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate() {
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }
        });

        ll_first = (LinearLayout) findViewById(R.id.bond_list_ll_first);
        ll_first.setVisibility(View.VISIBLE);
        img_first = (CircleImageView) findViewById(R.id.bond_list_first_img);
        tv_name = (TextView) findViewById(R.id.bond_list_first_tv_name);
        tv_devote = (TextView) findViewById(R.id.bond_list_first_tv_devote);

        ll_second = (LinearLayout) findViewById(R.id.bond_list_ll_second);
        ll_second.setVisibility(View.VISIBLE);
        img_second = (CircleImageView) findViewById(R.id.bond_list_second_img);
        tv_name2 = (TextView) findViewById(R.id.bond_list_second_tv_name);
        tv_devote2 = (TextView) findViewById(R.id.bond_list_second_tv_devote);

        ll_third = (LinearLayout) findViewById(R.id.bond_list_ll_third);
        ll_third.setVisibility(View.VISIBLE);
        img_third = (CircleImageView) findViewById(R.id.bond_list_third_img);
        tv_name3 = (TextView) findViewById(R.id.bond_list_third_tv_name);
        tv_devote3 = (TextView) findViewById(R.id.bond_list_third_tv_devote);

        view_line=(View) findViewById(R.id.bond_list_view);
        view_line.setVisibility(View.VISIBLE);
        mListView = (ListView) findViewById(R.id.bond_list_mListView);
        list_devote=new ArrayList<>();
    }

    private void initDatas() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }

        RequestParams params=new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("zbid",liveId);
        params.put("p",p);

        String url= Constant.BASE_URL+Constant.URL_USERAPI_LIVEGIFTRANK;

        System.out.println("===================================直播礼物排行 params============"+params.toString());
        System.out.println("===================================直播礼物排行 url==============="+url);

        mAsyncHttpClient.post(context,url,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog!=null){
                    mProgressDialog.dismiss();
                }

                System.out.println("===================================直播礼物排行 response==========="+response.toString());
                if (!TextUtils.isEmpty(response.toString())){
                    mGiftJson=new Gson().fromJson(response.toString(),LiveGiftJson.class);
                    if ("1".equals(mGiftJson.getResult())){
                        if (mGiftJson.getList().size()==0||mGiftJson.getList()==null||"".equals(mGiftJson.getList())){
                            ll_first.setVisibility(View.VISIBLE);
                            ll_second.setVisibility(View.VISIBLE);
                            ll_third.setVisibility(View.VISIBLE);
                            view_line.setVisibility(View.GONE);
                            mListView.setVisibility(View.GONE);
                        }else{
                            if (mGiftJson.getList().size()<=3){
                                for (int i = 0; i <mGiftJson.getList().size() ; i++) {
                                    switch (mGiftJson.getList().size()){
                                        case 1:
                                            ll_first.setVisibility(View.VISIBLE);
                                            ll_second.setVisibility(View.VISIBLE);
                                            ll_third.setVisibility(View.VISIBLE);
                                            view_line.setVisibility(View.GONE);
                                            mListView.setVisibility(View.GONE);
                                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+ mGiftJson.getList().get(0).getPic(),img_first);
                                            tv_name.setText(mGiftJson.getList().get(0).getUname());
                                            tv_devote.setText("魅力  "+mGiftJson.getList().get(0).getAlljb());
                                            break;
                                        case 2:
                                            ll_first.setVisibility(View.VISIBLE);
                                            ll_second.setVisibility(View.VISIBLE);
                                            ll_third.setVisibility(View.VISIBLE);
                                            view_line.setVisibility(View.GONE);
                                            mListView.setVisibility(View.GONE);
                                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+ mGiftJson.getList().get(0).getPic(),img_first);
                                            tv_name.setText(mGiftJson.getList().get(0).getUname());
                                            tv_devote.setText("魅力  "+mGiftJson.getList().get(0).getAlljb());

                                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+ mGiftJson.getList().get(1).getPic(),img_second);
                                            tv_name2.setText(mGiftJson.getList().get(1).getUname());
                                            tv_devote2.setText("魅力  "+mGiftJson.getList().get(1).getAlljb());
                                            break;
                                        case 3:
                                            ll_first.setVisibility(View.VISIBLE);
                                            ll_second.setVisibility(View.VISIBLE);
                                            ll_third.setVisibility(View.VISIBLE);
                                            view_line.setVisibility(View.GONE);
                                            mListView.setVisibility(View.GONE);
                                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+ mGiftJson.getList().get(0).getPic(),img_first);
                                            tv_name.setText(mGiftJson.getList().get(0).getUname());
                                            tv_devote.setText("魅力  "+mGiftJson.getList().get(0).getAlljb());

                                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+ mGiftJson.getList().get(1).getPic(),img_second);
                                            tv_name2.setText(mGiftJson.getList().get(1).getUname());
                                            tv_devote2.setText("魅力  "+mGiftJson.getList().get(1).getAlljb());

                                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+ mGiftJson.getList().get(2).getPic(),img_third);
                                            tv_name3.setText(mGiftJson.getList().get(2).getUname());
                                            tv_devote3.setText("魅力  "+mGiftJson.getList().get(2).getAlljb());
                                            break;
                                    }
                                }
                            }else{
                                ll_first.setVisibility(View.VISIBLE);
                                ll_second.setVisibility(View.VISIBLE);
                                ll_third.setVisibility(View.VISIBLE);
                                view_line.setVisibility(View.VISIBLE);
                                mListView.setVisibility(View.VISIBLE);
                                mList=mGiftJson.getList();
                                for (int i = 0; i <3 ; i++) {
                                    switch (i){
                                        case 0:
                                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+ mList.get(0).getPic(),img_first);
                                            tv_name.setText(mList.get(0).getUname());
                                            tv_devote.setText("魅力  "+mList.get(0).getAlljb());
                                            break;
                                        case 1:
                                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+ mList.get(1).getPic(),img_second);
                                            tv_name2.setText(mList.get(1).getUname());
                                            tv_devote2.setText("魅力  "+mList.get(1).getAlljb());
                                            break;
                                        case 2:
                                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+ mList.get(2).getPic(),img_third);
                                            tv_name3.setText(mList.get(2).getUname());
                                            tv_devote3.setText("魅力  "+mList.get(2).getAlljb());
                                            break;
                                    }
                                }

                                for (int i = 3; i <mList.size() ; i++) {
                                    list_devote.add(mList.get(i));
                                }
                                mGiftAdapter=new LiveGiftAdapter(context,list_devote);
                                mListView.setAdapter(mGiftAdapter);
                            }
                        }
                    }else{
                        Toasts.show(mGiftJson.getMessage());
                    }
                }else{
                    showErrorDialog(context);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog!=null){
                    mProgressDialog.dismiss();
                }
                showErrorDialog(context);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("=================================直播礼物排行 throwable,responseString=========="+responseString);
                if (mProgressDialog!=null){
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(context);
            }
        });

    }

    private void initMoreDatas() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
