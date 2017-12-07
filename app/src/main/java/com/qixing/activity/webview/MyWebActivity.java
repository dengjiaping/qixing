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

public class MyWebActivity extends BaseActivity {
    private WebView webview;

    private BGATitlebar mTitleBar;

    //	private String url = "app.qixing.com";
    private String url = "http://www.baidu.com";
    private String titleS = "";

    private String spid;
    private String kind;
    private String type;

    private ShareBean mShareBean;

    /**
     * umeng
     */
    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.webview_layout);
        url = getIntent().getStringExtra("url");
        titleS = getIntent().getStringExtra("title");

        int pro_idIndex = url.indexOf("/id/");
        spid = (String) url.subSequence(pro_idIndex + "/id/".length(), url.length());
        System.out.println("===========================订单页面 spid = " + spid);
        kind = getIntent().getStringExtra("kind");

        if ("1".equals(kind)) {
            type = "5";
        } else if ("2".equals(kind)) {
            type = "3";
        }

//        if (!("1".equals(MyApplication.getInstance().getFirst_enter())) && !(spid.equals(MyApplication.getInstance().getInfo_new()))) {
//            MyApplication.getInstance().setFirst_enter("1");
//            MyApplication.getInstance().setInfo_new(spid);
//        }

        initView();
        initUMeng();
//		if(isNetworkConnected(this)){
//            initView();
//        }else{
//            Toast.makeText(MyWebActivity.this, "请打开网络", Toast.LENGTH_SHORT).show();
//        }

    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        if (!TextUtils.isEmpty(titleS)) {
            mTitleBar.setTitleText(titleS);
        }
        mTitleBar.setRightDrawable(getResources().getDrawable(R.drawable.icon_share2));
        mTitleBar.setDelegate(new BGATitlebar.BGATitlebarDelegate() {

            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                if (webview.canGoBack()) {
                    webview.goBack(); //goBack()表示返回WebView的上一页面
                } else {
                    finish();
                }
            }


            @Override
            public void onClickRightCtv() {
                super.onClickRightCtv();
                shareNum();
            }
        });

        webview = (WebView) findViewById(R.id.webview);

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
        webview.setWebViewClient(new WebViewClient() {

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
                Toast.makeText(MyWebActivity.this, "加载失败，请稍候再试", Toast.LENGTH_SHORT).show();
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
                if (TextUtils.isEmpty(titleS)) {
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
//        webview.getSettings().setCacheMode(webview.getSettings().LOAD_CACHE_ELSE_NETWORK);
        //不使用缓存：
        webview.getSettings().setCacheMode(webview.getSettings().LOAD_NO_CACHE);

        //加载需要显示的网页
        webview.loadUrl(url);
        System.out.println("===========================url = " + url);

    }


    private void initUMeng() {

        mShareListener = new CustomShareListener(MyWebActivity.this);

                /*无自定按钮的分享面板*/
//        mShareAction = new ShareAction(TestActivity.this).setDisplayList(
//                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
//                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
//                SHARE_MEDIA.ALIPAY, SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
//                SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL, SHARE_MEDIA.YNOTE,
//                SHARE_MEDIA.EVERNOTE, SHARE_MEDIA.LAIWANG, SHARE_MEDIA.LAIWANG_DYNAMIC,
//                SHARE_MEDIA.LINKEDIN, SHARE_MEDIA.YIXIN, SHARE_MEDIA.YIXIN_CIRCLE,
//                SHARE_MEDIA.TENCENT, SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.TWITTER,
//                SHARE_MEDIA.WHATSAPP, SHARE_MEDIA.GOOGLEPLUS, SHARE_MEDIA.LINE,
//                SHARE_MEDIA.INSTAGRAM, SHARE_MEDIA.KAKAO, SHARE_MEDIA.PINTEREST,
//                SHARE_MEDIA.POCKET, SHARE_MEDIA.TUMBLR, SHARE_MEDIA.FLICKR,
//                SHARE_MEDIA.FOURSQUARE, SHARE_MEDIA.MORE)
//                .withText(Defaultcontent.text + "来自友盟自定义分享面板")
//                .setCallback(mShareListener);


//        String shareUrl = Constant.BASE_URL+Constant.URL_USERAPI_FXZBXX+"/"+spid;
//        mShareAction = new ShareAction(MyWebActivity.this).setDisplayList(
//                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                .withTitle("七星时代")
//                .withTargetUrl(shareUrl)
//                .withText("来自七星时代的分享")
//                .setCallback(mShareListener);
//        System.out.println("=========================== 分享连接 ====" + shareUrl);
    }

    private class CustomShareListener implements UMShareListener {

        private WeakReference<MyWebActivity> mActivity;

        private CustomShareListener(MyWebActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toasts.show("收藏成功");
                System.out.println("======================" + platform + "===== 分享 收藏成功啦  ");
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST
                        && platform != SHARE_MEDIA.LINKEDIN
                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    shareSuccess();
                    Toast.makeText(mActivity.get(), " 分享成功", Toast.LENGTH_SHORT).show();
                    System.out.println("=====================" + platform + "====== 分享 分享成功啦  ");

                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST
                    && platform != SHARE_MEDIA.LINKEDIN
                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), " 分享失败", Toast.LENGTH_SHORT).show();
                System.out.println("=====================" + platform + "====== 分享 分享失败啦  ");
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                    System.out.println("====================" + platform + "======= 分享 throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), " 分享取消", Toast.LENGTH_SHORT).show();
            System.out.println("=====================" + platform + "====== 分享 分享取消了  ");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private long mExitTime;

    //设置回退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (webview.canGoBack()) {
                System.out.println("===========================webview.canGoBack() = " + url);
                webview.goBack(); //goBack()表示返回WebView的上一页面
                return true;
            } else {
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
     */
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


    private void shareNum() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
//            mProgressDialog.show();
        }
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("spid", spid);
        params.put("type", type);//type  1视频   2直播  3资讯 4干货 5营销

        final String url = Constant.BASE_URL + Constant.URL_INDEX_SHARE_NUM;
        System.out.println("===========================用户观看视频页 分享成功前转发量 url ======= " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================用户观看视频页 分享成功前转发量 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mShareBean = new Gson().fromJson(response.toString(), ShareBean.class);
                    if (mShareBean.getResult().equals("1")) {
//                        tv_sharenum.setText(mShareBean.getSharenum());

                        try {
                            downloadFile(Constant.BASE_URL_IMG + mShareBean.getPic());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        Toasts.show(mResultBean.getMessage());
                    } else {
//                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
//                    showErrorDialog(MyWebActivity.this);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
//                showTimeoutDialog(MyWebActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
//                showErrorDialog(MyWebActivity.this);
            }
        });
    }


    //分享成功后调用
    private void shareSuccess() {
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("spid", spid);
        params.put("type", type);//type  1视频   2直播 3资讯 4 干货 5营销
        final String url = Constant.BASE_URL + Constant.URL_SHARE_SUCCESS;
        System.out.println("===========================用户观看视频页 分享成功后转发量 url ======= " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                System.out.println("===========================用户观看视频页 分享成功后转发量 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mShareBean = new Gson().fromJson(response.toString(), ShareBean.class);
                    if (mShareBean.getResult().equals("1")) {
//                        tv_sharenum.setText(mShareBean.getSharenum());
                        System.out.println("=================分享成功后的提示信息 message ==========" + mShareBean.getMessage());
//                        Toasts.show(mResultBean.getMessage());
                    } else {
//                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
//                    showErrorDialog(PlayVideoActivity.this);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                if (mProgressDialog != null) {
//                    mProgressDialog.dismiss();
//                }
//                showTimeoutDialog(PlayVideoActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
//                if (mProgressDialog != null) {
//                    mProgressDialog.dismiss();
//                }
//                showErrorDialog(PlayVideoActivity.this);
            }
        });
    }

    private Bitmap bitmap;
    private String img_base64;

    /**
     * @param file_url 要下载的文件URL
     * @throws Exception
     */
    public void downloadFile(String file_url) throws Exception {

        // 指定文件类型
        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
        // 获取二进制数据如图片和其他文件
        mAsyncHttpClient.get(file_url, new BinaryHttpResponseHandler(allowedContentTypes) {

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
                    System.out.println("===========下载成功 头像路径是" + tempPath);
                    bitmap = ImageTools.createThumbnail(tempPath, 200, 200);
                    img_base64 = ImageTools.bitmapToBase64(bitmap);

                    String shareUrl = Constant.BASE_URL + Constant.URL_USERAPI_FXZBXX + "/id/" + spid + "/type/" + type;//type 1视频 2 直播 3资讯 4干货 5营销
//                    UMImage image = new UMImage(MyWebActivity.this, Constant.BASE_URL_IMG+mShareBean.getPic());//网络图片
                    UMImage image = new UMImage(MyWebActivity.this, file);//本地文件
                    mShareAction = new ShareAction(MyWebActivity.this).setDisplayList(
                            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                            .withTitle(mShareBean.getTitle())
                            .withTargetUrl(url+"/is_fx/1")
                            .withMedia(image)
                            .withText(mShareBean.getContent())
                            .setCallback(mShareListener);
                    System.out.println("=========================== 分享连接 ====" + url);
                    System.out.println("=========================== 分享图片连接 ====" + Constant.BASE_URL_IMG + mShareBean.getPic());
                    mShareAction.open();

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

}
