package com.qixing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qixing.app.AppManager;
import com.qixing.app.MyApplication;
import com.qixing.bean.VersionJson;
import com.qixing.fragment.Fragment1;
import com.qixing.fragment.Fragment2;
import com.qixing.fragment.Fragment3;
import com.qixing.fragment.Fragment4;
import com.qixing.fragment.Fragment4s;
import com.qixing.fragment.Fragment5;
import com.qixing.global.Constant;
import com.qixing.qxlive.QXLiveShowActivity;
import com.qixing.utlis.SystemUtils;
import com.qixing.view.DisableScrollViewpager;
import com.qixing.welcome.ComplementUserinfoActivity;
import com.qixing.widget.Toasts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private final String TAG=MainActivity.class.getSimpleName();
    public static boolean isFirstVersion = true;

    private DisableScrollViewpager vp_main;
    //    private MyFragmentVpAdapter vp_Adapter;
    private List<Fragment> list;

    private int SCREEN_WIDTH;

    private ViewGroup view;

    private LinearLayout layout_home,layout_search,layout_ewm,layout_shopping_car,layout_personal;
    private ImageView img_home,img_search,img_ewm,img_shopping_car,img_personal;
    private TextView tv_home,tv_search,tv_ewm,tv_shopping_car,tv_mine;

    private int vp=0,tag=0;

    public static boolean isForeground = false;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = MainActivity.this;
//        AppManager.getAppManager().addActivity(this);
        /**
         * 沉浸式状态栏
         * */
        initStatusbar(MainActivity.this, R.color.color_titlebar_default);

        vp = getIntent().getIntExtra("vp",0);

        initView();

        Bundle bundle = getIntent().getBundleExtra(Constant.EXTRA_BUNDLE);
        if(bundle != null){
            //如果bundle存在，取出其中的参数，启动MessageActivity
            String title = bundle.getString("title");
            SystemUtils.startMessageActivity(this,title);
            Log.i(TAG, "launchParam exists, redirect to MessageActivity");
        }
    }

    private void initView() {
        vp_main = (DisableScrollViewpager)findViewById(R.id.vp_main);
        layout_home = (LinearLayout)findViewById(R.id.bottom_layout_home);
        layout_search = (LinearLayout)findViewById(R.id.bottom_layout_search);
        layout_ewm = (LinearLayout)findViewById(R.id.bottom_layout_ewm);
        layout_shopping_car = (LinearLayout)findViewById(R.id.bottom_layout_shopping_car);
        layout_personal = (LinearLayout)findViewById(R.id.bottom_layout_personal);
        layout_home.setOnClickListener(this);
        layout_search.setOnClickListener(this);
        layout_ewm.setOnClickListener(this);
        layout_shopping_car.setOnClickListener(this);
        layout_personal.setOnClickListener(this);
        vp_main.setAdapter(new ContentFragment(getSupportFragmentManager(), this));
        vp_main.setNoScroll(true);//是否禁止滑动
        vp_main.setOffscreenPageLimit(4);//一共加载3页，如果此处不指定，默认只加载相邻页


        img_home = (ImageView)findViewById(R.id.bottom_img_home);
        img_search = (ImageView)findViewById(R.id.bottom_img_search);
        img_ewm = (ImageView)findViewById(R.id.bottom_img_ewm);
        img_shopping_car = (ImageView)findViewById(R.id.bottom_img_shopping_car);
        img_personal = (ImageView)findViewById(R.id.bottom_img_personal);

        tv_home = (TextView)findViewById(R.id.bottom_tv_home);
        tv_search = (TextView)findViewById(R.id.bottom_tv_search);
        tv_ewm = (TextView)findViewById(R.id.bottom_tv_ewm);
        tv_shopping_car = (TextView)findViewById(R.id.bottom_tv_shopping_car);
        tv_mine = (TextView)findViewById(R.id.bottom_tv_mine);

        //初始化选择状态
        initVpSelect();
        vp_main.setCurrentItem(vp);
        if(isFirstVersion){
            getVersionFromService();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        vp = getIntent().getIntExtra("vp",0);
        vp_main.setCurrentItem(vp);
        if (vp==3){
            tag=getIntent().getIntExtra("tag",0);
            Fragment4s fragment4s=new Fragment4s();
            Bundle bundle=new Bundle();
            bundle.putInt("tag",tag);
            fragment4s.setArguments(bundle);
        }

        System.out.println("===========================MainActivity onNewIntent vp = " + vp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottom_layout_home://首页
                vp_main.setCurrentItem(0, false);
                break;
            case R.id.bottom_layout_search://搜索
                vp_main.setCurrentItem(1, false);
                break;
            case R.id.bottom_layout_ewm://搜索
//                vp_main.setCurrentItem(2, false);
                AppManager.getAppManager().startNextActivity(MainActivity.this, QXLiveShowActivity.class);
                break;
            case R.id.bottom_layout_shopping_car://购物车
                vp_main.setCurrentItem(3, false);
//                if("1".equals(MyApplication.getInstance().getIsLogining())){
//                }else{
//                    AppManager.getAppManager().startNextActivity(MainActivity.this, Login.class);
//                }
                break;
            case R.id.bottom_layout_personal://个人中心
                vp_main.setCurrentItem(4, false);
//                if("".equals(MyApplication.getInstance().getU_tel())){
//                    AppManager.getAppManager().startNextActivity(MainActivity.this, ComplementUserinfoActivity.class);
//                }else{
////                    AppManager.getAppManager().startNextActivity(MainActivity.this, LoginActivity.class);
//                }
                break;
        }
    }


    private static class ContentFragment extends FragmentPagerAdapter {

        private Class[] mFrgaments = new Class[]{Fragment1.class, Fragment2.class, Fragment3.class, Fragment4s.class, Fragment5.class};

        private Context mContext;


        public ContentFragment(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int i) {
            return Fragment.instantiate(mContext, mFrgaments[i].getName());
        }

        @Override
        public int getCount() {
            return mFrgaments.length;
        }
    }

    private void initVpSelect() {
        vp_main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetBottom();
                switch (position) {
                    case 0://首页
//                        img_home.setBackgroundResource(R.drawable.icon_bar_home_onclick);
                        img_home.setImageDrawable(getResources().getDrawable(R.drawable.icon_bar_home_onclick));
                        tv_home.setTextColor(getResources().getColor(R.color.color_bottom_select));
                        break;
                    case 1://搜索
//                        img_search.setBackgroundResource(R.drawable.icon_bar_search_onclick);
                        img_search.setImageDrawable(getResources().getDrawable(R.drawable.icon_bar_video_onclick));
                        tv_search.setTextColor(getResources().getColor(R.color.color_bottom_select));
                        break;
                    case 2://二维码
//                        img_search.setBackgroundResource(R.drawable.icon_bar_search_onclick);
                        img_ewm.setImageDrawable(getResources().getDrawable(R.drawable.icon_bar_live_onclick));
                        tv_ewm.setTextColor(getResources().getColor(R.color.color_bottom_select));
                        break;
                    case 3://购物车
                        img_shopping_car.setImageDrawable(getResources().getDrawable(R.drawable.icon_bar_info_onclick));
                        tv_shopping_car.setTextColor(getResources().getColor(R.color.color_bottom_select));
                        break;
                    case 4://个人中心
//                        img_personal.setBackgroundResource(R.drawable.icon_bar_mine_onclick);
                        img_personal.setImageDrawable(getResources().getDrawable(R.drawable.icon_bar_mine_onclick));
                        tv_mine.setTextColor(getResources().getColor(R.color.color_bottom_select));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化底部按钮 图标和颜色
     * */
    public void resetBottom() {
        img_home.setImageDrawable(getResources().getDrawable(R.drawable.icon_bar_home));
        img_search.setImageDrawable(getResources().getDrawable(R.drawable.icon_bar_video));
        img_ewm.setImageDrawable(getResources().getDrawable(R.drawable.icon_bar_live_onclick));
        img_shopping_car.setImageDrawable(getResources().getDrawable(R.drawable.icon_bar_info));
        img_personal.setImageDrawable(getResources().getDrawable(R.drawable.icon_bar_mine));

//        img_home.setBackgroundResource(R.drawable.icon_bar_home);
//        img_search.setBackgroundResource(R.drawable.icon_bar_search);
//        img_financing.setBackgroundResource(R.drawable.icon_bar_financing);
//        img_shopping_car.setBackgroundResource(R.drawable.icon_bar_shopping_car);
//        img_personal.setBackgroundResource(R.drawable.icon_bar_mine);

        tv_home.setTextColor(getResources().getColor(R.color.color_bottom_default));
        tv_search.setTextColor(getResources().getColor(R.color.color_bottom_default));
        tv_ewm.setTextColor(getResources().getColor(R.color.color_bottom_default));
        tv_shopping_car.setTextColor(getResources().getColor(R.color.color_bottom_default));
        tv_mine.setTextColor(getResources().getColor(R.color.color_bottom_default));

    }

    /**
     * 沉浸式状态栏
     * */
    public void initStatusbar(Context context, int statusbar_bg) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window window = ((Activity) context).getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			window.setFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

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

    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().AppExit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 自动更新提示框
     * */
    private void showUpdateDialog(String title,String msg){
        final MaterialDialog materialDialog = new MaterialDialog(context);
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
                MyApplication.getInstance().setApkId(id+"");//TODO 把id存储在Preferences里面
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

    AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();;
    ProgressDialog mProgressDialog = null;
    AlertDialog mAlertDialog;
    VersionJson mVersionJson;

    private void getVersionFromService(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
        }
//        mProgressDialog.show();
        final RequestParams params = new RequestParams();
        params.put("sysname",  "android");//（系统）
        params.put("version",  MyApplication.getInstance().getVersionCode()+"");//（版本号）
//        final String url = "http://jinxibi.com/shop.php/"+Constant.URL_APP_BANBEN;
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_BANBEN;
        System.out.println("===========================首页 检测版本url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================首页 检测版本 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mVersionJson = new Gson().fromJson(response.toString(), VersionJson.class);
                    if (mVersionJson.getResult().equals("1")) {
                        //服务器版本号
                        String versionName = mVersionJson.getVersionname();
                        String versionCode = mVersionJson.getVersioncode();
                        String versionUrl = mVersionJson.getDownpath();
//						Toast.makeText(MyselfActivity.this, "version " +version,Toast.LENGTH_SHORT).show();
                        System.out.println("===========================服务器版本号" + "versionName=" + versionName +"，versionCode=" + versionCode + "，versionUrl=" + versionUrl);
                        //当前应用版本号
                        String visonNameNow = MyApplication.getInstance().getVersionName();
                        String visonCodeNow = MyApplication.getInstance().getVersionCode()+"";
                        System.out.println("===========================当前版本号" + "visonName=" + visonNameNow+",visonCode=" + visonCodeNow);
                        //s2.compareTo(s1)
                        //s2>s1 返回值大于0    s2=s1返回值等于0    s2<s1 返回值小于0
                        if(!TextUtils.isEmpty(versionCode)){
                            //versionCode.compareTo(visonCodeNow)<=0
                            if(Integer.parseInt(versionCode)<=Integer.parseInt(visonCodeNow)){
////							Toast.makeText(SplashActivity.this, "当前版本" +version+",您已经是最新版本",Toast.LENGTH_SHORT).show();
                            }else{
                                isFirstVersion = false;
                                showUpdateDialog("提示", "发现新版本，是否更新？");
                            }
                        }
                    } else {
                        Toasts.show(mVersionJson.getMessage());
                    }
                } else {
                    System.out.println("===========================版本更新检查失败！  " );
//                    mAlertDialog = new AlertDialog.Builder(context)
//                            .setTitle(R.string.dialog_prompt)
//                            .setMessage(R.string.dialog_wrong)
//                            .setPositiveButton(R.string.dialog_ok,
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(
//                                                DialogInterface dialoginterface, int i) {
//                                            mAlertDialog.dismiss();
////                                        finish();
//                                        }
//                                    }).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================版本更新检查失败！  " );
//                mAlertDialog = new AlertDialog.Builder(context)
//                        .setTitle(R.string.dialog_prompt)
//                        .setMessage(R.string.dialog_wrong)
//                        .setPositiveButton(R.string.dialog_ok,
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(
//                                            DialogInterface dialoginterface, int i) {
//                                        mAlertDialog.dismiss();
////                                        finish();
//                                    }
//                                }).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

//                mAlertDialog = new AlertDialog.Builder(context)
//                        .setTitle(R.string.dialog_prompt)
//                        .setMessage(R.string.dialog_timeout)
//                        .setPositiveButton(R.string.dialog_ok,
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(
//                                            DialogInterface dialoginterface, int i) {
//                                        mAlertDialog.dismiss();
////                                        finish();
//                                    }
//                                }).show();
            }
        });
    }
}
