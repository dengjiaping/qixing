package com.qixing.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.view.titlebar.BGATitlebar;

public class MessageDetailActivity extends BaseActivity {

    private static Context context;

    private BGATitlebar mTitlebar;

    private ProgressBar mProgressBar;
    private BridgeWebView mWebView;

    private String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=getLayoutInflater().inflate(R.layout.activity_message_detail,null);
        setContentView(view);

        context=MessageDetailActivity.this;
        initStatusbar(context,R.color.color_titlebar_default);

        url=getIntent().getStringExtra("url");
        initView();
    }

    private void initView(){
        mTitlebar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitlebar.setTitleText("消息详情");
        mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }
        });

        mProgressBar= (ProgressBar) findViewById(R.id.webview_pb);

        mWebView= (BridgeWebView) findViewById(R.id.mWebView);

        //设置WebView属性，能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");//设置字符编码
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(false); // 不可以缩放
        mWebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放
//        webview.getSettings().setLoadWithOverviewMode(true);//设置Webview默认为全屏（满屏）显示
        mWebView.getSettings().setDisplayZoomControls(false);//不显示webview缩放按钮

        //设置Web视图
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                // super.onPageFinished(view, url);

                System.out.println("===========================onPageFinished网页加载完成 = ");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                System.out.println("===========================onReceivedError1 = ");
                Toast.makeText(context, "加载失败，请稍候再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                System.out.println("===========================onReceivedError2 = ");
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                System.out.println("===========================title = " + title + "," + view.getTitle());
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    System.out.println("===========================网页加载完成 = "+newProgress);
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    // 加载中
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }

            }
        });
        //优先使用缓存
//        webview.getSettings().setCacheMode(webview.getSettings().LOAD_CACHE_ELSE_NETWORK);
        //不使用缓存：
        mWebView.getSettings().setCacheMode(mWebView.getSettings().LOAD_NO_CACHE);

        //加载需要显示的网页
        mWebView.loadUrl(url);
        System.out.println("===========================url = " + url);
    }

    //设置回退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mWebView.canGoBack()) {
                System.out.println("===========================webview.canGoBack() = " + url);
                mWebView.goBack(); //goBack()表示返回WebView的上一页面
                return true;
            } else {
                finish();
            }
        }
        return false;
    }
}
