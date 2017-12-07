package com.qixing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lzy.okhttputils.https.HttpsUtils;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.ResultBean;
import com.qixing.bean.ShareBean;
import com.qixing.global.Constant;
import com.qixing.utlis.DateUtils;
import com.qixing.view.imagecut.ImageTools;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.http.Url;

/**
 * Created by wicep on 2015/12/23.
 * 播放视频
 */
public class PlayVideoActivity extends BaseActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, View.OnTouchListener, View.OnClickListener {

    /**
     * View播放
     */
    private VideoView videoView;

    /**
     * 加载预览进度条
     */
    private ProgressBar progressBar;

    /**
     * 设置view播放控制条
     */
    private MediaController mediaController;

    /**
     * 标记当视频暂停时播放位置
     */
    private int intPositionWhenPause = -1;

    /**
     * 设置窗口模式下的videoview的宽度
     */
    private int videoWidth;

    /**
     * 设置窗口模式下videoview的高度
     */
    private int videoHeight;

    private RelativeLayout rl_video;

    private String url_video = "";
    private String pic_video="";
    private String name_video="";
    private String isgz = "";
    private String spid = "";

    private BGATitlebar mTitleBar;
    private Button btn_layout;
    private ImageView img_layout;
    private boolean isFull = false;

    private int myVideoHeight, myVideoWidth;

    private Button btn_collection;
    private LinearLayout ll_share;
    private ImageView img_collection, img_share, img_download;
    private TextView tv_seenum, tv_sharenum;

    private TextView tv_context;
    private String sp_nr;

    private String seenum, sharenum;

    private ResultBean mResultBean;

    private ShareBean mShareBean;


    /**
     * umeng
     */
    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        initStatusbar(this, R.color.black);
        url_video = getIntent().getStringExtra("path_video");
        System.out.println("=============================url_video======"+url_video);
        pic_video=getIntent().getStringExtra("pic_video");
        name_video=getIntent().getStringExtra("name_video");
        System.out.println("=============================pic_video======"+pic_video);
        System.out.println("=============================name_video======"+name_video);
        isgz = getIntent().getStringExtra("isgz");
        spid = getIntent().getStringExtra("spid");
        sp_nr = getIntent().getStringExtra("sp_nr");
        seenum = getIntent().getStringExtra("seenum");
        sharenum = getIntent().getStringExtra("sharenum");

//        if(!("1".equals(MyApplication.getInstance().getFirst_enter()))&&!(spid.equals(MyApplication.getInstance().getInfo_new()))) {
//            MyApplication.getInstance().setInfo_new(spid);
//        }   MyApplication.getInstance().setFirst_enter("1");
//

        if (TextUtils.isEmpty(url_video)) {
            url_video = "http://baobab.wdjcdn.com/1455782903700jy.mp4";
        }

//        url_video = "http://flv2.bn.netease.com/videolib3/1612/18/wMmsD4452/SD/movie_index.m3u8";
//        url_video = "http://baobab.wdjcdn.com/1455782903700jy.mp4";
        System.out.println("===========================播放视频页面 视频地址url_video = " + url_video);
        initView();
        initVideoView();
        initWebView();
        initUMeng();
//        Uri uri=Uri.parse(url_video);
//        Intent intent=new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(uri,"video/*");
//        startActivity(intent);

        if ("0".equals(isgz)) {
//            btn_collection.setVisibility(View.VISIBLE);
            img_collection.setImageResource(R.drawable.icon_playvideo_nocollection);
            img_collection.setTag(R.drawable.icon_playvideo_nocollection);
        } else {
//            btn_collection.setVisibility(View.GONE);
            img_collection.setImageResource(R.drawable.icon_playvideo_collection);
            img_collection.setTag(R.drawable.icon_playvideo_collection);
        }
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("播放视频");
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

        btn_collection = (Button) findViewById(R.id.play_video_btn_collection);
        btn_collection.setOnClickListener(this);
//        private ImageView img_collection,img_share;
//        private TextView tv_seenum,tv_sharenum;
        img_collection = (ImageView) findViewById(R.id.play_video_img_collection);
        img_collection.setOnClickListener(this);
        img_share = (ImageView) findViewById(R.id.play_video_img_share);
        ll_share = (LinearLayout) findViewById(R.id.play_video_ll_share);
        ll_share.setOnClickListener(this);
        tv_seenum = (TextView) findViewById(R.id.play_video_tv_seenum);
        tv_seenum.setText("播放：" + seenum + "次");
        tv_sharenum = (TextView) findViewById(R.id.play_video_tv_sharenum);
        tv_sharenum.setText(sharenum);

        tv_context = (TextView) findViewById(R.id.play_video_tv_context);
        tv_context.setText(sp_nr);

        img_download = (ImageView) findViewById(R.id.play_video_img_download);
        img_download.setOnClickListener(this);
    }

    private void initWebView() {
    }

    /**
     * 初始化videoview播放
     */
    public void initVideoView() {
        //初始化播放器父布局
        rl_video = (RelativeLayout) findViewById(R.id.play_video_rl_video);

        //初始化进度条
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //初始化VideoView
        videoView = (VideoView) findViewById(R.id.videoView);
        //初始化videoview控制条
        mediaController = new MediaController(this);
        //设置videoview的控制条
        videoView.setMediaController(mediaController);
        //设置显示控制条
        mediaController.show(0);
        //设置播放完成以后监听
        videoView.setOnCompletionListener(this);
        //设置发生错误监听，如果不设置videoview会向用户提示发生错误
        videoView.setOnErrorListener(this);
        videoView.setOnPreparedListener(this);
        //设置videoView的点击监听
        videoView.setOnTouchListener(this);
        //设置在视频文件在加载完毕以后的回调函数
        //设置网络视频路径
        Uri uri = Uri.parse(url_video);
        videoView.setVideoURI(uri);

        btn_layout = (Button) findViewById(R.id.play_video_btn_layout);
        btn_layout.setOnClickListener(this);

        img_layout = (ImageView) findViewById(R.id.play_video_img_layout);
        img_layout.setOnClickListener(this);
        isFull = false;

        ViewTreeObserver vto2 = rl_video.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl_video.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                myVideoHeight = rl_video.getHeight();
                myVideoWidth = rl_video.getWidth();
                System.out.println("===========================视频播放器高宽 ======= " + rl_video.getHeight() + "," + rl_video.getWidth());
            }
        });

        //设置为窗口模式播放
//        setVideoViewLayoutParams(2);

    }

    private void initUMeng() {

        mShareListener = new CustomShareListener(PlayVideoActivity.this);

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
//        mShareAction = new ShareAction(PlayVideoActivity.this).setDisplayList(
//                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                .withTitle("七星时代")
//                .withTargetUrl(shareUrl)
//                .withText("来自七星时代的分享")
//                .setCallback(mShareListener);
//        System.out.println("=========================== 分享连接 ====" + shareUrl);
    }

    private class CustomShareListener implements UMShareListener {

        private WeakReference<PlayVideoActivity> mActivity;

        private CustomShareListener(PlayVideoActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), " 收藏成功", Toast.LENGTH_SHORT).show();
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
//                shareNum();//分享量增加
                tv_sharenum.setText(mShareBean.getSharenum());
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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.play_video_btn_layout:
                if (isFull) {
                    isFull = false;
                    setVideoViewLayoutParams(2);
                } else {
                    isFull = true;
                    setVideoViewLayoutParams(1);
                }
                break;
            case R.id.play_video_img_layout:
                if (isFull) {
                    isFull = false;
                    setVideoViewLayoutParams(2);
                } else {
                    isFull = true;
                    setVideoViewLayoutParams(1);
                }
                break;
            case R.id.play_video_btn_collection:
//                collection();
                break;
            case R.id.play_video_img_collection:
                if (Integer.parseInt(img_collection.getTag().toString()) == R.drawable.icon_playvideo_collection) {
                    Toasts.show("您已收藏该视频");
                } else {
                    collection();
                }
                break;
            case R.id.play_video_ll_share:
                shareNum();//分享量增加
//                mShareAction.open();
                break;
            case R.id.play_video_img_download:
                intent=new Intent();
                intent.putExtra("name",name_video);
                intent.putExtra("pic",Constant.BASE_URL_IMG+pic_video);
                intent.putExtra("url",url_video);
                finish();
                AppManager.getAppManager().startNextActivity(PlayVideoActivity.this,DownloadListActivity.class,intent);
//                showDownloadDialog();
                break;
        }
    }

    private void collection() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("spid", spid);
        final String url = Constant.BASE_URL + Constant.URL_INDEX_FAV_SP;
        System.out.println("===========================用户观看视频页 收藏 url ======= " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================用户观看视频页 收藏 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        img_collection.setImageResource(R.drawable.icon_playvideo_collection);
                        img_collection.setTag(R.drawable.icon_playvideo_collection);
                        Toasts.show(mResultBean.getMessage());
                    } else {
                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
                    showErrorDialog(PlayVideoActivity.this);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(PlayVideoActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(PlayVideoActivity.this);
            }
        });
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
        params.put("type", "1");//type  1视频   2直播
        final String url = Constant.BASE_URL + Constant.URL_INDEX_SHARE_NUM;
        System.out.println("===========================用户观看视频页  url ======= " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================用户观看视频页  response ======= " + response.toString());
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
//                    showErrorDialog(PlayVideoActivity.this);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
//                showTimeoutDialog(PlayVideoActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
//                showErrorDialog(PlayVideoActivity.this);
            }
        });
    }


    private void shareSuccess() {
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("spid", spid);
        params.put("type", "1");//type  1视频   2直播
        final String url = Constant.BASE_URL + Constant.URL_SHARE_SUCCESS;
        System.out.println("===========================用户观看视频页 转发量 url ======= " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                System.out.println("===========================用户观看视频页 转发量 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mShareBean = new Gson().fromJson(response.toString(), ShareBean.class);
                    if (mShareBean.getResult().equals("1")) {
//                        tv_sharenum.setText(mShareBean.getSharenum());
                        System.out.println("=================分享后的结果message==========" + mShareBean.getMessage());
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
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
//                showTimeoutDialog(PlayVideoActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
//                showErrorDialog(PlayVideoActivity.this);
            }
        });
    }

    /**
     * @param videoUrl 要下载的视频文件URL
     * @throws Exception
     */
    public void downloadVideoFile(String videoUrl) {
        long time = DateUtils.dateToUnixTimestamp();
        int index = videoUrl.lastIndexOf("/");
        String name = videoUrl.substring(index, videoUrl.length());

        String fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();

        //使用系统下载类
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                Uri uri = Uri.parse("http://jinxibi.com/"+ mVersionJson.getDownpath());
        Uri uri = Uri.parse(videoUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置自定义下载路径和文件名
        String fileFullName = videoUrl.substring(videoUrl.lastIndexOf(File.separatorChar) + 1);
        request.setDestinationInExternalPublicDir(fileDir, fileFullName);
        MyApplication.getInstance().setVideoName(fileFullName);
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        //request.setShowRunningNotification(false);

        //不显示下载界面
        request.setVisibleInDownloadsUi(false);
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
//        request.setVisibleInDownloadsUi(true);
//        request.setDestinationInExternalFilesDir()
//        request.setMimeType("application/cn.trinea.download.file");
        /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
        在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/
        //request.setDestinationInExternalFilesDir(this, null, "tar.apk");
        long id = downloadManager.enqueue(request);//TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
        MyApplication.getInstance().setVideoId(id+"");//TODO 把id存储在Preferences里面
    }

    private Bitmap bitmap;
    private String img_base64;

    /**
     * @param url 要下载的文件URL
     * @throws Exception
     */
    public void downloadFile(String url) throws Exception {

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
                    System.out.println("===========下载成功 头像路径是" + tempPath);
                    bitmap = ImageTools.createThumbnail(tempPath, 200, 200);
                    img_base64 = ImageTools.bitmapToBase64(bitmap);

                    String shareUrl = Constant.BASE_URL + Constant.URL_USERAPI_FXZBXX + "/id/" + spid + "/type/1";//type 1视频 2 直播
//                    UMImage image = new UMImage(PlayVideoActivity.this, Constant.BASE_URL_IMG+mShareBean.getPic());//网络图片
                    UMImage image = new UMImage(PlayVideoActivity.this, file);//本地文件
                    mShareAction = new ShareAction(PlayVideoActivity.this).setDisplayList(
                            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                            .withTitle(mShareBean.getTitle())
                            .withTargetUrl(shareUrl)
                            .withMedia(image)
                            .withText(mShareBean.getContent())
                            .setCallback(mShareListener);
                    System.out.println("=========================== 分享连接 ====" + shareUrl);
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

    //当点击下载时的提示框
    private void showDownloadDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("下载提示").setMessage("确定要下载吗?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    downloadVideoFile(url_video);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //启动视频播放
        videoView.start();
        //设置获取焦点
        videoView.setFocusable(true);

    }


    /**
     * 设置videiview的全屏和窗口模式
     *
     * @param paramsType 标识 1为全屏模式 2为窗口模式
     */
    public void setVideoViewLayoutParams(int paramsType) {

        if (1 == paramsType) {
//            btn_layout.setText("窗口");
            img_layout.setImageDrawable(getResources().getDrawable(R.drawable.icon_fill));
            //隐藏标题栏 mTitleBar
            mTitleBar.setVisibility(View.GONE);
            //隐藏信号栏的代码：
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            initStatusbar(this, R.color.black);
            //隐藏状态栏，同时Activity会伸展全屏显示。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            //设置为横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
//            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rl_video.setLayoutParams(LayoutParams);
        } else {
//            btn_layout.setText("全屏");
            img_layout.setImageDrawable(getResources().getDrawable(R.drawable.icon_fill));
            //显示标题栏 mTitleBar
            mTitleBar.setVisibility(View.VISIBLE);
            //设置为竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            initStatusbar(this, R.color.color_titlebar_default);
            initStatusbar(this, R.color.black);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            //动态获取宽高
            DisplayMetrics DisplayMetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
//            videoHeight=DisplayMetrics.heightPixels-20;
            videoHeight = myVideoHeight;
            videoWidth = DisplayMetrics.widthPixels - 20;
            LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(videoWidth, videoHeight);
//            LayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            rl_video.setLayoutParams(LayoutParams);
        }

    }

    /**
     * 视频播放完成以后调用的回调函数
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    /**
     * 视频播放发生错误时调用的回调函数
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.e("text", "发生未知错误");
                System.out.println("===========================text =  发生未知错误");
                showErrorDialog("视频播放错误，请稍候再试");
//                finish();
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.e("text", "媒体服务器死机");
                System.out.println("===========================text =  媒体服务器死机");
                showErrorDialog("视频播放错误，请稍候再试");
//                finish();
                break;
            default:
                Log.e("text", "onError+" + what);
//                finish();
                System.out.println("===========================text =  onError" + what);
                break;
        }
        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                //io读写错误
                Log.e("text", "文件或网络相关的IO操作错误");
                System.out.println("===========================text =  文件或网络相关的IO操作错误");
                showErrorDialog("视频播放错误，请稍候再试");
//                finish();
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                //文件格式不支持
                Log.e("text", "比特流编码标准或文件不符合相关规范");
                System.out.println("===========================text =  比特流编码标准或文件不符合相关规范");
                showErrorDialog("视频播放错误，请稍候再试");
//                finish();
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                //一些操作需要太长时间来完成,通常超过3 - 5秒。
                Log.e("text", "操作超时");
                System.out.println("===========================text =  操作超时");
                showErrorDialog("连接超时，请稍候再试");
//                finish();
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                //比特流编码标准或文件符合相关规范,但媒体框架不支持该功能
                Log.e("text", "比特流编码标准或文件符合相关规范,但媒体框架不支持该功能");
                System.out.println("===========================text =  比特流编码标准或文件符合相关规范,但媒体框架不支持该功能");
                showErrorDialog("视频播放错误，请稍候再试");
//                finish();
                break;
            default:
                Log.e("text", "onError+" + extra);
                System.out.println("===========================text =  onError" + extra);
//                finish();
                break;
        }
        //如果未指定回调函数， 或回调函数返回假，VideoView 会通知用户发生了错误。
        return false;
    }

    MaterialDialog mMaterialDialog;

    private void showErrorDialog(String context) {
        mMaterialDialog = new MaterialDialog(mContext);
        if (mMaterialDialog != null) {
            mMaterialDialog.setTitle(R.string.prompt)
                    .setMessage(context)
                    .setPositiveButton(
                            R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    finish();
                                }
                            }
                    )
//                    .setNegativeButton(
//                            R.string.cancel, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    mMaterialDialog.dismiss();
//                                }
//                            }
//                    )
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

    /**
     * 视频文件加载文成后调用的回调函数
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        //如果文件加载成功,隐藏加载进度条
        progressBar.setVisibility(View.GONE);

    }

    /**
     * 对videoView的触摸监听
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    /**
     * 页面暂停效果处理
     */
    @Override
    protected void onPause() {
        super.onPause();
        //如果当前页面暂定则保存当前播放位置，并暂停
        intPositionWhenPause = videoView.getCurrentPosition();
        //停止回放视频文件
        videoView.stopPlayback();
    }

    /**
     * 页面从暂停中恢复
     */
    @Override
    protected void onResume() {
        super.onResume();
        //跳转到暂停时保存的位置
        if (intPositionWhenPause >= 0) {
            videoView.seekTo(intPositionWhenPause);
            //初始播放位置
            intPositionWhenPause = -1;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != videoView) {
            videoView = null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 横竖屏切换重写方法
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //land
            System.out.println("===========================onConfigurationChanged ======= ");
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //port
        }
    }

    private ViewGroup view;

    public void initStatusbar(Context context, int statusbar_bg) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window window = ((Activity) context).getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            view = (ViewGroup) ((Activity) context).getWindow().getDecorView();
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight());

            TextView textView = new TextView(this);
            textView.setBackgroundResource(statusbar_bg);
            textView.setLayoutParams(lParams);
            view.addView(textView);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
