package com.qixing.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qixing.MainActivity;
import com.qixing.R;
import com.qixing.activity.webview.MyWebActivity2;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.VersionJson;
import com.qixing.global.Constant;
import com.qixing.manager.DataCleanManager;
import com.qixing.utlis.SDCardUtils;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.welcome.LoginActivity;
import com.qixing.widget.Toasts;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

public class SettingActivity extends BaseActivity {

    private LinearLayout layout_cheked_version, layout_clear_cache,layout_bind_account,layout_reset_pwd,layout_jpush_set,layout_about_us,layout_feedback;
    private BGATitlebar mTitleBar;
    private Button btn_exit;
    private TextView tv_cache,tv_version;
    private ImageView img_jpush;
    private boolean is_Push = true;

    private VersionJson mVersionJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_setting, null);
        setContentView(view);
        initView();
    }


    @Override
    protected void onStart() {
        super.onStart();

        try {
            String cache = DataCleanManager.getImageCacheImageSize(mContext);
            System.out.println("===========================缓存TotalCacheSize大小 ====== " + DataCleanManager.getTotalCacheSize(mContext));
            tv_cache.setText(cache);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {

        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("设置");
        mTitleBar.setDelegate(new BGATitlebar.BGATitlebarDelegate() {

            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }
        });

        tv_cache = (TextView) findViewById(R.id.tv_cache);
        tv_version = (TextView) findViewById(R.id.tv_version);
        layout_bind_account= (LinearLayout) findViewById(R.id.layout_bind_account);
        layout_bind_account.setOnClickListener(this);
        layout_cheked_version = (LinearLayout) findViewById(R.id.layout_cheked_version);
        layout_cheked_version.setOnClickListener(this);
        layout_jpush_set= (LinearLayout) findViewById(R.id.layout_jpush_set);
        layout_jpush_set.setOnClickListener(this);
        layout_reset_pwd = (LinearLayout) findViewById(R.id.layout_reset_pwd);
        layout_reset_pwd.setOnClickListener(this);
        layout_about_us= (LinearLayout) findViewById(R.id.layout_about_us);
        layout_about_us.setOnClickListener(this);
        layout_clear_cache = (LinearLayout) findViewById(R.id.layout_clear_cache);
        layout_clear_cache.setOnClickListener(this);
        layout_feedback = (LinearLayout) findViewById(R.id.layout_feedback);
        layout_feedback.setOnClickListener(this);

        img_jpush = (ImageView)findViewById(R.id.iv_jpush);

        if(TextUtils.isEmpty(MyApplication.getInstance().getVersioncode_service())){
            tv_version.setText(MyApplication.getInstance().getVersionName()+"");
        }else{
            tv_version.setTextColor(getResources().getColor(R.color.color_titlebar_default));
            tv_version.setText("有新版本"+MyApplication.getInstance().getVersioncode_service()+"!");
        }

        is_Push = MyApplication.getInstance().getIs_jpush();
        if(MyApplication.getInstance().getIs_jpush()){
//            JPushInterface.getConnectionState(mContext);
            JPushInterface.resumePush(mContext);
            img_jpush.setImageDrawable(getResources().getDrawable(R.drawable.icon_switch_open));
        }else{
            JPushInterface.stopPush(mContext);
            img_jpush.setImageDrawable(getResources().getDrawable(R.drawable.icon_switch_close));
        }

        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.layout_clear_cache:
                show( "确定清除缓存?");
                break;
            case R.id.layout_bind_account://绑定账号
                intent = new Intent();
//                intent.putExtra("url" ,Constant.BASE_URL+Constant.URL_USERAPI_POLICY);
                AppManager.getAppManager().startNextActivity(mContext, BindAccountActivity.class,intent);
                break;
            case R.id.layout_reset_pwd://修改密码
                intent = new Intent();
//                intent.putExtra("url" ,Constant.BASE_URL);//+Constant.URL_USERAPI_HELPA
//                intent.putExtra("title" ,"修改密码");
                AppManager.getAppManager().startNextActivity(mContext, SettingPwdActivity.class, intent);
                break;
            case R.id.layout_cheked_version://版本信息
                getVersionFromService();
                break;
            case R.id.layout_feedback://意见反馈
                intent = new Intent();
                intent.putExtra("url", Constant.BASE_URL );//+ Constant.URL_USERAPI_ABOUTAS
                AppManager.getAppManager().startNextActivity(mContext,FeedbackActivity.class,intent);
                break;
            case R.id.layout_about_us://关于我们
                intent = new Intent();
                intent.putExtra("url", Constant.BASE_URL + Constant.URL_INDEX_GYWM);//
                AppManager.getAppManager().startNextActivity(mContext,MyWebActivity2.class,intent);
                break;
            case R.id.layout_jpush_set://推送设置
                if(is_Push){
                    is_Push = false;
                    JPushInterface.stopPush(mContext);
                    img_jpush.setImageDrawable(getResources().getDrawable(R.drawable.icon_switch_close));
                    MyApplication.getInstance().setIs_jpush(false);//存储JPush状态 关闭
                    Toasts.show("推送服务已关闭");
                }else{
                    is_Push = true;
                    JPushInterface.resumePush(mContext);
                    img_jpush.setImageDrawable(getResources().getDrawable(R.drawable.icon_switch_open));
                    MyApplication.getInstance().setIs_jpush(true);//存储JPush状态 开启
                    Toasts.show("推送服务已开启");
                }
                break;

            case R.id.btn_exit:
                showExit( "确定退出登录?");
                break;
            default:
                break;
        }
    }

    private void getVersionFromService(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();
        final RequestParams params = new RequestParams();
        params.put("sysname",  "android");//（系统）
        params.put("version",  MyApplication.getInstance().getVersionCode()+"");//（版本号）
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_BANBEN;//
        System.out.println("===========================设置 检测版本url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================设置 检测版本 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mVersionJson = new Gson().fromJson(response.toString(), VersionJson.class);
                    if (mVersionJson.getResult().equals("1")) {
                        //服务器版本号
                        String versionName = mVersionJson.getVersionname();
                        String versionCode = mVersionJson.getVersioncode();
                        String versionUrl = mVersionJson.getDownpath();
//						Toast.makeText(MyselfActivity.this, "version " +version,Toast.LENGTH_SHORT).show();
                        System.out.println("===========================服务器版本号" + "versionName=" + versionName + "，versionCode=" + versionCode + "，versionUrl=" + versionUrl);
                        //当前应用版本号
                        String visonNameNow = MyApplication.getInstance().getVersionName();
                        String visonCodeNow = MyApplication.getInstance().getVersionCode() + "";
                        System.out.println("===========================当前版本号" + "visonName=" + visonNameNow + ",visonCode=" + visonCodeNow);
                        //s2.compareTo(s1)
                        //s2>s1 返回值大于0    s2=s1返回值等于0    s2<s1 返回值小于0
                        if (!TextUtils.isEmpty(versionCode)) {
                            if (Integer.parseInt(versionCode)<=Integer.parseInt(visonCodeNow)) {
							Toasts.show("当前版本" + versionName +" ,您已经是最新版本");
                                MyApplication.getInstance().setVersioncode_service("");
                            } else {
                                MyApplication.getInstance().setVersioncode_service(mVersionJson.getVersionname());
                                showUpdateDialog("提示", "发现新版本，是否更新？");
                            }
                        }
                    } else {
                        Toasts.show(mVersionJson.getMessage());
                    }
                } else {
                    System.out.println("===========================版本更新检查失败！  ");
                    showErrorDialog(SettingActivity.this);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================版本更新检查失败！  ");
                showTimeoutDialog(SettingActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(SettingActivity.this);
            }
        });
    }


    MaterialDialog mMaterialDialog;

    /**
     * 自动更新提示框
     * */
    private void showUpdateDialog(String title,String msg){
        final MaterialDialog materialDialog = new MaterialDialog(mContext);
        materialDialog.setTitle(title);
        materialDialog.setMessage(msg);
        materialDialog.setPositiveButton(R.string.update_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用系统下载类
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                Uri uri = Uri.parse("http://jinxibi.com/"+ mVersionJson.getDownpath());
                Uri uri = Uri.parse(Constant.BASE_HOST+ mVersionJson.getDownpath());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                // 设置自定义下载路径和文件名
//                                    String apkName =  "yourName" + DateUtils.getCurrentMillis() + ".apk";
//                                    request.setDestinationInExternalPublicDir(yourPath, apkName);
//                                    MyApplication.getInstance().setApkName(apkName);
                //设置允许使用的网络类型，这里是移动网络和wifi都可以
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);

                //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
                //request.setShowRunningNotification(false);

                //不显示下载界面
                request.setVisibleInDownloadsUi(false);
                // 设置为可被媒体扫描器找到
                request.allowScanningByMediaScanner();
                // 设置为可见和可管理
                request.setVisibleInDownloadsUi(true);
                request.setMimeType("application/cn.trinea.download.file");
        /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
        在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/
                //request.setDestinationInExternalFilesDir(this, null, "tar.apk");
                long id = downloadManager.enqueue(request);//TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
                MyApplication.getInstance().setApkId(Long.toString(id));//TODO 把id存储在Preferences里面
                materialDialog.dismiss();
            }
        });
        materialDialog.setNegativeButton(R.string.update_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });

        materialDialog.show();
    }

    public void show(String message) {
        mMaterialDialog = new MaterialDialog(mContext);
        if (mMaterialDialog != null) {
            mMaterialDialog.setTitle(R.string.prompt)
                    .setMessage(
                     message
                    )
                    .setPositiveButton(
                            R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();

                                    DataCleanManager.cleanCustomCache(SDCardUtils.getSdCardPath() + Constant.CACHE_DIR_IMAGE);

                                    try {
                                        tv_cache.setText(DataCleanManager.getImageCacheImageSize(mContext));
                                    } catch (Exception e) {
                                        e.printStackTrace();
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

    public void showExit(String message) {
        mMaterialDialog = new MaterialDialog(mContext);
        if (mMaterialDialog != null) {
            mMaterialDialog.setTitle(R.string.prompt)
                    .setMessage(
                            message
                    )
                    .setPositiveButton(
                            R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
//										//清空JPush 别名 alias
                                    Set<String> set = null;
                                    JPushInterface.resumePush(mContext);
                                    JPushInterface.setAliasAndTags(mContext, "", null, new TagAliasCallback() {
                                        @Override
                                        public void gotResult(int i, String s, Set<String> set) {
                                            //回调方法
                                        }
                                    });
                                    MyApplication.getInstance().setIsLogining("");
                                    MyApplication.getInstance().setUid("");
                                    MyApplication.getInstance().setU_tel("");
                                    MyApplication.getInstance().setUname("");
                                    MyApplication.getInstance().setNickname("");
                                    MyApplication.getInstance().setUser_Head("");
                                    ((Activity)MainActivity.context).finish();
                                    Intent intent = new Intent();
//                                    intent.putExtra("vp", 0);
                                    AppManager.getAppManager().startNextActivity(mContext, LoginActivity.class, intent);
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
    protected void onRestart() {
        super.onRestart();

    }









}
