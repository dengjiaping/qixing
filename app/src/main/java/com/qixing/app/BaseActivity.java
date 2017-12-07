package com.qixing.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.qixing.R;
import com.qixing.model.CityModel;
import com.qixing.model.DistrictModel;
import com.qixing.model.ProvinceModel;
import com.qixing.service.XmlParserHandler;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public abstract class BaseActivity extends Activity implements OnClickListener {

    /**
     * UI 线程ID
     */
    private long mUIThreadId;
    private static final int ACTIVITY_RESUME = 0;
    private static final int ACTIVITY_STOP = 1;
    private static final int ACTIVITY_PAUSE = 2;
    private static final int ACTIVITY_DESTROY = 3;
    private LayoutInflater mInflater;
    public int activityState;
    public AsyncHttpClient mAsyncHttpClient;

    /**
     * 提示框
     * */
    public ProgressDialog mProgressDialog;
    public static AlertDialog mAlertDialog;

    private boolean mAllowFullScreen = true;

    // public abstract void initWidget();


    private TextView tv_title;

    protected Button btn_sure;

    private ViewGroup view;

    /**获取屏幕宽度*/
    protected int width_Display;// 屏幕宽度（像素）
    protected int height_Display;// 屏幕高度（像素）
    protected float density_Display;// 屏幕密度（0.75 / 1.0 / 1.5）
    protected int densityDpi_Display;// 屏幕密度DPI（120 / 160 / 240）


    // public abstract void widgetClick(View v);

    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }

    protected Context mContext;

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            onHandleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        // widgetClick(v);
    }

    protected void onHandleMessage(Message msg) {

    }

    public void initBackListener(View view) {
        if (view != null) {


//            BGATitlebar mBackView = (BGATitlebar) view.findViewById(R.id.mTitleBar);
//
//            if (mBackView != null) {
//
//                mBackView.setDelegate(new BGATitlebar.BGATitlebarDelegate() {
//
//                    @Override
//                    public void onClickLeftCtv() {
//                        super.onClickLeftCtv();
//                        AppManager.getAppManager().finishActivity();
//                    }
//                });
//            }


//            btn_sure = (Button) view.findViewById(R.id.btn_sure);
//
//            if (btn_sure != null) {
//
//                btn_sure.setOnClickListener(this);
//
//            }


        }

    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (tv_title != null) {

            tv_title.setText(title);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消标题
        }

        initStatusbar(mContext, R.color.color_titlebar_default);
        mAsyncHttpClient = new AsyncHttpClient();
        AppManager.getAppManager().addActivity(this);

        /**获取屏幕宽度*/
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width_Display = metric.widthPixels; // 屏幕宽度（像素）
        height_Display = metric.heightPixels; // 屏幕高度（像素）
        density_Display = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        densityDpi_Display = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）


//		beforeSetContentView();
//		if (getContentViewId() != 0) {
//			setContentView(getContentViewId());
//		}
//		findViews();
//		beforeSetViews();
//		setViews();


    }

    public void initStatusbar(Context context, int statusbar_bg) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window window = ((Activity) context).getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            view = (ViewGroup) ((Activity) context).getWindow().getDecorView();
            LayoutParams lParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT, getStatusBarHeight());

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


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = ACTIVITY_RESUME;

    }

//    @Override
//    protected void onStop() {
//        super.onResume();
//        activityState = ACTIVITY_STOP;
//    }

    @Override
    protected void onPause() {
        super.onPause();
        activityState = ACTIVITY_PAUSE;

    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityState = ACTIVITY_DESTROY;
        AppManager.getAppManager().finishActivity(this);
    }

    public Context getThemeContext() {
        return this;
    }

    public View inflate(int resId) {
        if (null == mInflater) {
            mInflater = LayoutInflater.from(getThemeContext());
        }
        return mInflater.inflate(resId, null);
    }

    public String[] getStringArray(int resId) {
        return getThemeContext().getResources().getStringArray(resId);
    }

    public int getDimen(int resId) {
        return getThemeContext().getResources().getDimensionPixelSize(resId);
    }

//    public int getColor(int resId) {
//        return getThemeContext().getResources().getColor(resId);
//    }
//
//    public ColorStateList getColorStateList(int resId) {
//        return getThemeContext().getResources().getColorStateList(resId);
//    }


    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }


    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    public static void showErrorDialog(Context mContext){

        mAlertDialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.dialog_prompt)
                .setMessage(R.string.dialog_wrong)
                .setPositiveButton(R.string.dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                mAlertDialog.dismiss();
//                                        finish();
                            }
                        }).show();
    }

    public static void showTimeoutDialog(Context mContext){
        mAlertDialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.dialog_prompt)
                .setMessage(R.string.dialog_timeout)
                .setPositiveButton(R.string.dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                mAlertDialog.dismiss();
//                                        finish();
                            }
                        }).show();
    }

    /**
     * dip转换px
     */
    public int dip2px(int dip) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * pxz转换dip
     */
    public int px2dip(int px) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取UI线程ID
     *
     * @return UI线程ID
     */
    public long getUIThreadId() {
        return mUIThreadId;
    }

    public boolean post(Runnable run) {
        return mHandler.post(run);
    }

    public boolean postDelayed(Runnable run, long delay) {
        return mHandler.postDelayed(run, delay);
    }

    public void removeCallbacks(Runnable run) {
        mHandler.removeCallbacks(run);
    }



    protected String[] mProvinceDatas;

    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();


    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();


    protected String mCurrentProviceName;

    protected String mCurrentCityName;

    protected String mCurrentDistrictName = "";


    protected String mCurrentZipCode = "";



    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = mContext.getAssets();
        try {
            //InputStream input = asset.open("assets/province_data.xml");

            InputStream input = mContext.getClass().getClassLoader().getResourceAsStream("assets/province_data_all.xml");


            SAXParserFactory spf = SAXParserFactory.newInstance();

            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();

            provinceList = handler.getDataList();



            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }

            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {

                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {

                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {

                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());

                        mZipcodeDatasMap.put(provinceList.get(i).getName() + cityNames[j] + districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }

                    mDistrictDatasMap.put(provinceList.get(i).getName() + cityNames[j], distrinctNameArray);
                }

                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }


    public void signOut(final Context context, final Activity activity1){
        mAlertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_prompt)
                .setMessage(R.string.dialog_exit)
                .setPositiveButton(R.string.dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
//                                MyApplication.getInstance().setIsLogining("");
//
//                                Intent intent = new Intent();
//                                intent.putExtra("vp",0);
//                                AppManager.getAppManager().startNextActivity(BaseActivity.this, MainActivity1.class,intent);
                                mAlertDialog.dismiss();
                                activity1.finish();
                            }
                        })
                .setNegativeButton(R.string.dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                mAlertDialog.dismiss();
                            }
                        }).show();
    }


}
