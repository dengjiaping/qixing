package com.qixing.welcome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.qixing.MainActivity;
import com.qixing.R;
import com.qixing.app.MyApplication;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.utlis.NetworkUtils;
import com.qixing.widget.Toasts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;
import me.leolin.shortcutbadger.ShortcutBadger;


public class SplashActivity  extends InstrumentedActivity {
	private final int SPLASH_DISPLAY_LENGHT = 2000; // 延迟三秒
	private List<Map<String, String>> updateList;
	String netVersion, downloadUrl, localVersion;

	/**
	 * 版本查询
	 * */
//	private VersionJson versionJson;

	private boolean isNewVersion = false;

	private AsyncHttpClient mAsyncHttpClient;

	/**
	 * 提示框
	 * */
	public ProgressDialog mProgressDialog;
	private MaterialDialog mMaterialDialog;

	private ResultBean mResultBean;

	private boolean isDate = false;

	private Context mContext;

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);

		mContext=getApplicationContext();
		mAsyncHttpClient = new AsyncHttpClient();

		if(NetworkUtils.isNetworkAvailable(SplashActivity.this)){
			getBannerUrl();
		}else{
			isDate = false;
			Toasts.show("您的网络暂时不可用！请检查网络状态！");
		}
		postDelayedHomeActivity();

//		setBadgeCount(mContext);
	}

	private void gotoHomePage() {

		if("1".equals(MyApplication.getInstance().getIsLogining())){//已登录，设置JPush 别名 alias
			JPushInterface.resumePush(mContext);
			JPushInterface.setAliasAndTags(SplashActivity.this, MyApplication.getInstance().getUid(), null, new TagAliasCallback() {
				@Override
				public void gotResult(int i, String s, Set<String> set) {

				}
			});


			Intent intent = null;
			if(isDate){
				intent = new Intent(SplashActivity.this, GuideActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("mResultBean",mResultBean);
				intent.putExtras(mBundle);
			}else{
				intent = new Intent(SplashActivity.this, MainActivity.class);
				//如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
				//将参数取出，传递到MainActivity中
				if(getIntent().getBundleExtra(Constant.EXTRA_BUNDLE) != null){
					intent.putExtra(Constant.EXTRA_BUNDLE,
							getIntent().getBundleExtra(Constant.EXTRA_BUNDLE));
				}
			}
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}else{//未登录，清空JPush 别名 alias
			Set<String> set ;
			JPushInterface.resumePush(mContext);
			JPushInterface.setAliasAndTags(SplashActivity.this, "", null, new TagAliasCallback() {
				@Override
				public void gotResult(int i, String s, Set<String> set) {

				}
			});
			Intent intent = null;
			if(isDate){
				intent = new Intent(SplashActivity.this, GuideActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("mResultBean",mResultBean);
				intent.putExtras(mBundle);
			}else{
				intent = new Intent(SplashActivity.this, MainActivity.class);
			}
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();

//			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//			SplashActivity.this.startActivity(intent);
//			SplashActivity.this.finish();
		}

		
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
//		setBadgeCount(mContext);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	private void postDelayedHomeActivity() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				gotoHomePage();
			}

		}, SPLASH_DISPLAY_LENGHT);
	}

	/**
	 * 引导页和广告
	 * */
	private void getBannerUrl(){
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("加载中...");
//		mProgressDialog.show();
		final RequestParams params = new RequestParams();
		final String url = Constant.BASE_URL+Constant.URL_INDEX_YINDAOPIC;//
		System.out.println("===========================引导页和广告url ===== " + url);
		mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}

				System.out.println("===========================引导页和广告response ===== " + response);
				if (!TextUtils.isEmpty(response.toString())) {
					mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
					if (mResultBean.getResult().equals("1")) {
						isDate = true;
					} else {
//						Toasts.show(mResultBean.getMessage() + "");
					}
				} else {
//					showErrorDialog(mContext);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toasts.show(R.string.service_wrong);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
//				new MaterialDialog(SplashActivity.this)
//						.setTitle(R.string.dialog_prompt)
//						.setMessage(R.string.dialog_timeout)
//						.setPositiveButton( R.string.dialog_ok,
//								new View.OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										mMaterialDialog.dismiss();
//									}
//								}
//						).show();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);

				System.out.println("===========================throwable ,responseString =  " + responseString);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
//				new MaterialDialog(SplashActivity.this)
//						.setTitle(R.string.dialog_prompt)
//						.setMessage(R.string.dialog_wrong)
//						.setPositiveButton( R.string.dialog_ok,
//								new View.OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										mMaterialDialog.dismiss();
//									}
//								}
//						).show();
			}
		});

	}
	
	/**
	 * 弹出框
	 * */
	private void showNoticeDialog() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("发现新版本，是否更新？")
                .setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                }).show();
    }
	
	/**
	 * 方法描述：访问网络，获取app版本号
	 * */
	private void getVersionFromService() {

	}

	public void setBadgeCount(Context context){
		int badgecount=0;
		try {
			badgecount = MyApplication.getInstance().getInfo_num();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		boolean success = ShortcutBadger.applyCount(context, badgecount);

		System.out.println("=====================================Set count===="+badgecount+"========success===="+success);

		//find the home launcher Package
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
		String currentHomePackage = resolveInfo.activityInfo.packageName;

		System.out.println("===============================Launcher APP_launcher======"+currentHomePackage);
	}
}
