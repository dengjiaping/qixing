package com.qixing.activity.webview;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.ShareBean;
import com.qixing.global.Constant;
import com.qixing.view.imagecut.ImageTools;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import cz.msebera.android.httpclient.Header;

public class MyWebActivity2 extends BaseActivity {
	private WebView webview;

    private BGATitlebar mTitleBar;
	
//	private String url = "app.qixing.com";
    private String url = "http://www.baidu.com";
    private String titleS = "";



	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.webview_layout);
        url = getIntent().getStringExtra("url");
        titleS = getIntent().getStringExtra("title");

        initView();
//		if(isNetworkConnected(this)){
//            initView();
//        }else{
//            Toast.makeText(MyWebActivity.this, "请打开网络", Toast.LENGTH_SHORT).show();
//        }

	}

	private void initView(){
        mTitleBar= (BGATitlebar)findViewById(R.id.mTitleBar);
        if(!TextUtils.isEmpty(titleS)){
            mTitleBar.setTitleText(titleS);
        }
        mTitleBar.setDelegate(new BGATitlebar.BGATitlebarDelegate() {

            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                if( webview.canGoBack()){
                    webview.goBack(); //goBack()表示返回WebView的上一页面
                }else{
                    finish();
                }
            }


            @Override
            public void onClickRightCtv() {
                super.onClickRightCtv();
            }
        });

		webview = (WebView)findViewById(R.id.webview);

        //设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDefaultTextEncodingName("utf-8");//设置字符编码
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(false); // 不可以缩放
        webview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放
//        webview.getSettings().setLoadWithOverviewMode(true);//设置Webview默认为全屏（满屏）显示
        webview.getSettings().setDisplayZoomControls(false);//不显示webview缩放按钮

        //设置Web视图  
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                // super.onPageFinished(view, url);

                System.out.println("===========================onPageFinished网页加载完成 = " );
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                System.out.println("===========================onReceivedError1 = ");
                Toast.makeText(MyWebActivity2.this, "加载失败，请稍候再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                System.out.println("===========================onReceivedError2 = ");
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                System.out.println("===========================title = " + title + "," + view.getTitle());
                if(TextUtils.isEmpty(titleS)){
                    mTitleBar.setTitleText(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    System.out.println("===========================网页加载完成 = ");
                } else {
                    // 加载中

                }

            }
        });
        //优先使用缓存
        webview.getSettings().setCacheMode(webview.getSettings().LOAD_CACHE_ELSE_NETWORK);
        //不使用缓存：
//        webview.getSettings().setCacheMode(webview.getSettings().LOAD_NO_CACHE);


        //加载需要显示的网页
        webview.loadUrl(url);
        System.out.println("===========================url = " + url);

	}





    private long mExitTime;
    //设置回退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(webview.canGoBack()){
                System.out.println("===========================webview.canGoBack() = " + url);
                webview.goBack(); //goBack()表示返回WebView的上一页面
                return true;
            }else{
                finish();
            }
        }
        return false;
    }



    // 网络状态
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
	
	/**
	 * 弹出框
	 * */
	private void showNoticeDialog() {
		
        new AlertDialog.Builder(this).setTitle("提示").setMessage("发现新版本，是否更新？")
                .setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
//                    	Intent updateIntent = new Intent(SplashActivity.this,AppUpdateService.class);
//						updateIntent.putExtra("version",versionJson.getVersion());
//						updateIntent.putExtra("downPath",versionJson.getUrl());
//						startService(updateIntent);
//						Intent mainLogin = new Intent(SplashActivity.this, MainActivity.class);
//            			SplashActivity.this.startActivity(mainLogin);
//            			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//            			SplashActivity.this.finish();
//                        dialog.dismiss();
                    }
                })
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
//                    	Intent mainLogin = new Intent(SplashActivity.this, MainActivity.class);
//            			SplashActivity.this.startActivity(mainLogin);
//            			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//            			SplashActivity.this.finish();
//                        dialog.dismiss();
                    }
                }).show();
        

    }


}
