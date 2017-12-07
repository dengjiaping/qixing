package com.qixing.activity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.joanzapata.pdfview.util.FileUtils;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.qixing.R;
import com.qixing.activity.webview.MyWebActivity;
import com.qixing.adapter.MyRebateAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.MyRebateBean;
import com.qixing.bean.ShareBean;
import com.qixing.global.Constant;
import com.qixing.manager.DataCleanManager;
import com.qixing.utlis.ColorUtils;
import com.qixing.utlis.DateUtils;
import com.qixing.utlis.FileUtilss;
import com.qixing.view.MyListView;
import com.qixing.view.imagecut.ImageTools;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wicep on 2015/12/23.
 * 加盟快优
 */
public class ReadPDFActivity extends BaseActivity implements OnPageChangeListener {

    private BGATitlebar mTitleBar;

    private TextView tv_content;

    private FloatingActionButton mBtn_download;

    private PDFView pdfview;

    private int p = 1;

    private String spid;

    private ShareBean mShareBean;

    private ShareAction mShareAction;
    private UMShareListener mShareListener;

    private String pdfUrl;
    private String pdfName;
    private String firlName;
    private String pdfTitle;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_read_pdf, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        pdfUrl = getIntent().getStringExtra("pdfUrl");
        spid= getIntent().getStringExtra("spid");
        pdfTitle= getIntent().getStringExtra("pdfTitle");
//        firlName = FileUtilss.getFileNameFromUrl(pdfUrl);
        firlName = DateUtils.getNowTime()+"";
        System.out.println("===========================pdfUrl ===== " + pdfUrl);
        System.out.println("===========================pdfTitle ===== " + pdfTitle);

//        if(!("1".equals(MyApplication.getInstance().getFirst_enter()))&&!(spid.equals(MyApplication.getInstance().getInfo_new()))) {
//            MyApplication.getInstance().setFirst_enter("1");
//            MyApplication.getInstance().setInfo_new(spid);
//        }

        int pro_idIndex =pdfUrl.indexOf("/id/");
        System.out.println("===========================订单页面 spid = " + spid );
        initView();
        initUMeng();

        try {
            downloadPdfFile(pdfUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("七星干货");
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


        tv_content = (TextView) findViewById(R.id.read_pdf_tv_content);
        tv_content.setVisibility(View.GONE);
        pdfview = (PDFView) findViewById(R.id.pdfview);

        mBtn_download= (FloatingActionButton) findViewById(R.id.read_pdf_btn_download);
        mBtn_download.setOnClickListener(this);

    }

    private void initUMeng(){

        mShareListener = new CustomShareListener(ReadPDFActivity.this);

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
//        mShareAction = new ShareAction(ReadPDFActivity.this).setDisplayList(
//                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                .withTitle("七星时代")
//                .withTargetUrl(shareUrl)
//                .withText("来自七星时代的分享")
//                .setCallback(mShareListener);
//        System.out.println("=========================== 分享连接 ====" + shareUrl);
    }

    private void display(String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage)
            setTitle(pdfName = assetFileName);
        File file = new File(assetFileName, pdfTitle+".pdf");//qcl.pdf
        pdfview.fromFile(file)
//                .pages(0, 2, 1, 3, 3, 3)//   .pages(0, 0, 0, 0, 0, 0) // 默认全部显示，pages属性可以过滤性显示
                .defaultPage(1)//默认展示第一页
                .showMinimap(false)
                .enableSwipe(true)
//                .onDraw(onDrawListener)
//                .onLoad(onLoadCompleteListener)
                .onPageChange(this)//监听页面切换
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        System.out.println("===========================PDF阅读页 ===== " + page + "/" + pageCount);
        tv_content.setText(page + "/" + pageCount);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private Bitmap bitmap;
    private String img_base64;
    /**
     * @param url
     *            要下载的pdf文件URL
     * @throws Exception
     */
    public void downloadPdfFile(String url) throws Exception{
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        OkHttpUtils.get(url)//
                .tag(this)//
                .execute(new DownloadFileCallBack(Environment.getExternalStorageDirectory() +
                        "/qixing/pdf", pdfTitle+".pdf"));//保存到sd卡qcl.pdf
    }

    /**
     * @param url
     *            要下载的pdf文件URL
     * @throws Exception
     */
    public void downloadPdf(String url) throws Exception{
        long time = DateUtils.dateToUnixTimestamp();
        int index = url.lastIndexOf("/");
        String name = url.substring(index, url.length());

        String fileDir = Environment.getExternalStorageDirectory() + "/qixing/pdf";

        //使用系统下载类
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                Uri uri = Uri.parse("http://jinxibi.com/"+ mVersionJson.getDownpath());
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置自定义下载路径和文件名
        String fileFullName = url.substring(url.lastIndexOf(File.separatorChar) + 1);
        request.setDestinationInExternalPublicDir(fileDir, fileFullName);
        MyApplication.getInstance().setPdfName(fileFullName);
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
        MyApplication.getInstance().setPdfId(id+"");//TODO 把id存储在Preferences里面
    }

    /**
     * @param url
     *            要下载的文件URL
     * @throws Exception
     */
    public void downloadFile(String url) throws Exception {
//        mProgressDialog = new ProgressDialog(mContext);
//        mProgressDialog.setMessage("加载中...");
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
//
//        OkHttpUtils.get(url)//
//                .tag(this)//
//                .execute(new DownloadFileCallBack(Environment.getExternalStorageDirectory() +
//                        "/qixing/pdf", firlName+".pdf"));//保存到sd卡qcl.pdf
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
                    System.out.println("===========下载成功 头像路径是"+tempPath);
                    bitmap= ImageTools.createThumbnail(tempPath, 200, 200);
                    Bitmap bitmap_default= ColorUtils.changeColor(bitmap);
                    img_base64=ImageTools.bitmapToBase64(bitmap_default);

                    String shareUrl = Constant.BASE_URL+Constant.URL_USERAPI_FXZBXX+"/id/"+spid+"/type/4/";//type 1视频 2 直播
//                    UMImage image = new UMImage(MyWebActivity.this, Constant.BASE_URL_IMG+mShareBean.getPic());//网络图片
                    UMImage image = new UMImage(ReadPDFActivity.this, file);//本地文件
                    mShareAction = new ShareAction(ReadPDFActivity.this).setDisplayList(
                            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                            .withTitle(mShareBean.getTitle())
                            .withTargetUrl(pdfUrl)
                            .withMedia(image)
                            .withText(mShareBean.getContent())
                            .setCallback(mShareListener);
                    System.out.println("=========================== 分享连接 ====" + pdfUrl);
                    System.out.println("=========================== 分享图片连接 ====" + Constant.BASE_URL_IMG+mShareBean.getPic());
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

    private class DownloadFileCallBack extends FileCallback {

        public DownloadFileCallBack(String destFileDir, String destFileName) {
            super(destFileDir, destFileName);
        }

        @Override
        public void onBefore(BaseRequest request) {
//            Toasts.show("正在打开");
        }

        @Override
        public void onResponse(boolean isFromCache, File file, Request request, Response response) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

//            Toasts.show("下载完成");
            tv_content.setVisibility(View.VISIBLE);
            pdfName = Environment.getExternalStorageDirectory() + "/qixing/pdf";
            display(pdfName, false);
        }

        @Override
        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
            System.out.println("downloadProgress -- " + totalSize + "  " + currentSize + "  " + progress + "  " + networkSpeed);

            String downloadLength = Formatter.formatFileSize(getApplicationContext(),currentSize);
            String totalLength = Formatter.formatFileSize(getApplicationContext(), totalSize);
//            tvDownloadSize.setText(downloadLength + "/" + totalLength);
            String netSpeed = Formatter.formatFileSize(getApplicationContext(), networkSpeed);
//            tvNetSpeed.setText(netSpeed + "/S");
//            tvProgress.setText((Math.round(progress * 10000) * 1.0f / 100) + "%");
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            Toasts.show("打开失败");
        }
    }

    private class CustomShareListener implements UMShareListener {

        private WeakReference<ReadPDFActivity> mActivity;

        private CustomShareListener(ReadPDFActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toasts.show( "收藏成功");
                System.out.println("======================"+platform+"===== 分享 收藏成功啦  " );
            } else {
                if (platform!= SHARE_MEDIA.MORE&&platform!=SHARE_MEDIA.SMS
                        &&platform!=SHARE_MEDIA.EMAIL
                        &&platform!=SHARE_MEDIA.FLICKR
                        &&platform!=SHARE_MEDIA.FOURSQUARE
                        &&platform!=SHARE_MEDIA.TUMBLR
                        &&platform!=SHARE_MEDIA.POCKET
                        &&platform!=SHARE_MEDIA.PINTEREST
                        &&platform!=SHARE_MEDIA.LINKEDIN
                        &&platform!=SHARE_MEDIA.INSTAGRAM
                        &&platform!=SHARE_MEDIA.GOOGLEPLUS
                        &&platform!=SHARE_MEDIA.YNOTE
                        &&platform!=SHARE_MEDIA.EVERNOTE){
                    shareSuccess();
                    Toast.makeText(mActivity.get(), " 分享成功", Toast.LENGTH_SHORT).show();
                    System.out.println("====================="+platform+"====== 分享 分享成功啦  " );

                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform!= SHARE_MEDIA.MORE&&platform!=SHARE_MEDIA.SMS
                    &&platform!=SHARE_MEDIA.EMAIL
                    &&platform!=SHARE_MEDIA.FLICKR
                    &&platform!=SHARE_MEDIA.FOURSQUARE
                    &&platform!=SHARE_MEDIA.TUMBLR
                    &&platform!=SHARE_MEDIA.POCKET
                    &&platform!=SHARE_MEDIA.PINTEREST
                    &&platform!=SHARE_MEDIA.LINKEDIN
                    &&platform!=SHARE_MEDIA.INSTAGRAM
                    &&platform!=SHARE_MEDIA.GOOGLEPLUS
                    &&platform!=SHARE_MEDIA.YNOTE
                    &&platform!=SHARE_MEDIA.EVERNOTE){
                Toast.makeText(mActivity.get(), " 分享失败", Toast.LENGTH_SHORT).show();
                System.out.println("====================="+platform+"====== 分享 分享失败啦  " );
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                    System.out.println("===================="+platform+"======= 分享 throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), " 分享取消", Toast.LENGTH_SHORT).show();
            System.out.println("====================="+platform+"====== 分享 分享取消了  " );
        }
    }

    private void shareNum(){
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(mContext);
//            mProgressDialog.setMessage("加载中...");
////            mProgressDialog.show();
//        }
        RequestParams params = new RequestParams();
        params.put("uid",  MyApplication.getInstance().getUid());
        params.put("spid",  spid);
        params.put("type",  "4");//type  1视频   2直播  3资讯  4干货
        final String url = Constant.BASE_URL+Constant.URL_INDEX_SHARE_NUM;
        System.out.println("===========================用户观看视频页 分享成功前转发量 url ======= " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

//                if (mProgressDialog != null) {
//                    mProgressDialog.dismiss();
//                }
                System.out.println("===========================用户观看视频页 分享成功前转发量 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mShareBean = new Gson().fromJson(response.toString(), ShareBean.class);
                    if (mShareBean.getResult().equals("1")) {
//                        tv_sharenum.setText(mShareBean.getSharenum());

                        try {
                            downloadFile(Constant.BASE_URL_IMG+mShareBean.getPic());
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
    private void shareSuccess(){
        RequestParams params = new RequestParams();
        params.put("uid",  MyApplication.getInstance().getUid());
        params.put("spid",  spid);
        params.put("type",  "4");//type  1视频   2直播 3资讯 4干货
        final String url = Constant.BASE_URL+Constant.URL_SHARE_SUCCESS;
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
                        System.out.println("=================分享成功后的提示信息 message =========="+mShareBean.getMessage());
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
                    downloadPdf(pdfUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).create().show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.join_btn_call:
                break;
            case R.id.read_pdf_btn_download:
                intent=new Intent();
                intent.putExtra("name",pdfTitle);
                intent.putExtra("pic","http://app.qixingshidai.com//Public/Uploads/zhibopic/14963858353.png");
                intent.putExtra("url",pdfUrl);
                finish();
                AppManager.getAppManager().startNextActivity(ReadPDFActivity.this,DownloadListActivity.class,intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkHttpUtils.getInstance().cancelTag(this);
        DataCleanManager.deleteFolderFile(pdfName,true);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
