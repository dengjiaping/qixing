package com.qixing.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.MainActivity;
import com.qixing.R;
import com.qixing.activity.child.ImageCutActivity;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.CircleImageView;
import com.qixing.view.imagecut.ImageTools;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.welcome.LoginActivity;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 2016年4月6日11:29:59
 * 账户设置
 * */
public class ShopMineActivity extends BaseActivity implements View.OnClickListener {


	private PullToRefreshScrollView mScrollview;

	private BGATitlebar mTitleBar;
	private LinearLayout ll_headphoto,ll_username,ll_nickname,ll_sex,ll_birthday,ll_pwd,ll_paypwd,ll_exit,ll_id,ll_signature;
	private CircleImageView img_head;
	private TextView tv_username,tv_nickname,tv_sex,tv_signature,tv_id,tv_birthday;

	private ArrayList<String> mSelectPath;
	private String path;
	private Bitmap bitmap;
	private String img_base64 ;

	public static final int RESULT_OK = -1;
	private static final int REQUEST_IMAGE = 1;
	private static final int REQUEST_NICKNAME = 2;
	private static final int REQUEST_SEX = 3;
	private static final int REQUEST_BIRTHDAY = 4;
	private static final int REQUEST_SIGNATURE = 5;

	private AlertDialog mAlertDialog;

	//定义显示时间控件
	private Calendar calendar; //通过Calendar获取系统时间
	private int mYear ,mMonth,mDay;

	//自定义变量
	private EditText titleEdit;
	private EditText dateEdit;
	private EditText timeEdit;
	private EditText contentEdit;


	private ResultBean mResultBean;

	private String tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.activity_shop_mine, null);
		setContentView(view);
		initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
		calendar = Calendar.getInstance();
		tag = getIntent().getStringExtra("tag");
		initView();
    }


	private void initView(){
		mTitleBar= (BGATitlebar) findViewById(R.id.mTitleBar);
		mTitleBar.setDelegate(new BGATitlebar.BGATitlebarDelegate() {

			@Override
			public void onClickLeftCtv() {
				super.onClickLeftCtv();
				finish();
			}

			@Override
			public void onClickRightCtv() {
				super.onClickRightCtv();

			}
		});

		mScrollview = (PullToRefreshScrollView)findViewById(R.id.mScrollview);
		 /*
         * Mode.BOTH：同时支持上拉下拉
         * Mode.PULL_FROM_START：只支持下拉Pulling Down
         * Mode.PULL_FROM_END：只支持上拉Pulling Up
         */
        /*
         * 如果Mode设置成Mode.BOTH，需要设置刷新Listener为OnRefreshListener2，并实现onPullDownToRefresh()、onPullUpToRefresh()两个方法。
         * 如果Mode设置成Mode.PULL_FROM_START或Mode.PULL_FROM_END，需要设置刷新Listener为OnRefreshListener，同时实现onRefresh()方法。
         * 当然也可以设置为OnRefreshListener2，但是Mode.PULL_FROM_START的时候只调用onPullDownToRefresh()方法，
         * Mode.PULL_FROM的时候只调用onPullUpToRefresh()方法.
         */
		mScrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		init(mScrollview);//初始化上拉 下拉的显示信息
//		mScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
//			@Override
//			public void onRefresh(PullToRefreshBase refreshView) {
////				isRefresh = true;
////				initDatas();
//			}
//		});

		ll_headphoto = (LinearLayout)findViewById(R.id.shop_mine_ll_headphoto);
		ll_headphoto.setOnClickListener(this);
		img_head = (CircleImageView)findViewById(R.id.shop_mine_img_head);
		ll_username = (LinearLayout)findViewById(R.id.shop_mine_ll_username);
		ll_username.setOnClickListener(this);
		tv_username = (TextView)findViewById(R.id.shop_mine_tv_username);
		ll_nickname = (LinearLayout)findViewById(R.id.shop_mine_ll_nickname);
		ll_nickname.setOnClickListener(this);
		tv_nickname = (TextView)findViewById(R.id.shop_mine_tv_nickname);
		ll_sex = (LinearLayout)findViewById(R.id.shop_mine_ll_sex);
		ll_sex.setOnClickListener(this);
		tv_sex = (TextView)findViewById(R.id.shop_mine_tv_sex);
		ll_birthday = (LinearLayout)findViewById(R.id.shop_mine_ll_birthday);
		ll_birthday.setOnClickListener(this);
		tv_birthday = (TextView)findViewById(R.id.shop_mine_tv_birthday);
		ll_pwd  = (LinearLayout)findViewById(R.id.shop_mine_ll_pwd);
		ll_pwd.setOnClickListener(this);
		ll_paypwd  = (LinearLayout)findViewById(R.id.shop_mine_ll_paypwd);
		ll_paypwd.setOnClickListener(this);
		ll_id  = (LinearLayout)findViewById(R.id.shop_mine_ll_id);
		ll_id.setOnClickListener(this);
		tv_id = (TextView)findViewById(R.id.shop_mine_tv_id);
		ll_signature  = (LinearLayout)findViewById(R.id.shop_mine_ll_signature);
		ll_signature.setOnClickListener(this);
		tv_signature = (TextView)findViewById(R.id.shop_mine_tv_signature);

		ll_exit = (LinearLayout)findViewById(R.id.shop_mine_ll_exit);
		ll_exit.setOnClickListener(this);
		ll_exit.setVisibility(View.GONE);
		if(!TextUtils.isEmpty(MyApplication.getInstance().getUser_Head())){
			if(MyApplication.getInstance().getUser_Head().startsWith("http")){
				ImageLoader.getInstance().displayImage(MyApplication.getInstance().getUser_Head(),img_head, ImageLoaderOptions.get_face_Options());
			}else {
				ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), img_head, ImageLoaderOptions.get_face_Options());
			}
//			ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), img_head, ImageLoaderOptions.get_face_Options());
			System.out.println("===========================个人中心 User_Head = " + Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head());
		}
//		tv_username.setText(MyApplication.getInstance().getUname());
		tv_nickname.setText(MyApplication.getInstance().getNickname());
		tv_id.setText(MyApplication.getInstance().getPromotionid());
		//sex(1:男   2女 默认是1)
		if("1".equals(MyApplication.getInstance().getSex())){
			tv_sex.setText("男");
		}else if("2".equals(MyApplication.getInstance().getSex())){
			tv_sex.setText("女");
		}
		if(TextUtils.isEmpty(MyApplication.getInstance().getQianm())){
			tv_signature.setText("这个人很懒，什么也没写");
		}else{
			tv_signature.setText(MyApplication.getInstance().getQianm());
		}

//		tv_birthday.setText(MyApplication.getInstance().getShengri());
	}
	private void init(PullToRefreshScrollView mListView){
		ILoadingLayout startLabels = mListView
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在载入...");// 刷新时
		startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = mListView.getLoadingLayoutProxy(
				false, true);
		endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

//      // 设置下拉刷新文本
//      pullToRefresh.getLoadingLayoutProxy(false, true)
//              .setPullLabel("上拉刷新...");
//      pullToRefresh.getLoadingLayoutProxy(false, true).setReleaseLabel(
//              "放开刷新...");
//      pullToRefresh.getLoadingLayoutProxy(false, true).setRefreshingLabel(
//              "正在加载...");
//      // 设置上拉刷新文本
//      pullToRefresh.getLoadingLayoutProxy(true, false)
//              .setPullLabel("下拉刷新...");
//      pullToRefresh.getLoadingLayoutProxy(true, false).setReleaseLabel(
//              "放开刷新...");
//      pullToRefresh.getLoadingLayoutProxy(true, false).setRefreshingLabel(
//              "正在加载...");
	}


	/**
	 * TODO 刷新头像
	 * */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);

		path= ImageCutActivity.path+ ImageCutActivity.photo_name;

		if(path!=null){

			System.out.println("===========选择的图片路径是"+path);
			bitmap= ImageTools.createThumbnail(path, 200, 200);
////            Bitmap bmp= BitmapFactory.decodeFile(path);
//            is = InputStreamUtils.bitmapToInputStream(bitmap,1024);

			img_base64=ImageTools.bitmapToBase64(bitmap);
			img_head.setImageBitmap(bitmap);
//            try {
//                bytes = getBitmapByte(bmp);
//                img_base64 = InputStreamUtils.byteTOString(bytes);
//                System.out.println("===========bytes = "+bytes.toString());
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            String photo = Base64.encodeToString(bytes, 0, bytes.length,Base64.DEFAULT);
//            img_base64 = photo;

			upHeadPhoto();

//            img_test.setImageBitmap(ImageTools.base64ToBitmap(img_base64));
		}

	}

	public byte[] getBitmapByte(Bitmap bitmap){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.shop_mine_ll_headphoto://修改头像
				int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
				Intent intent=new Intent(mContext,MultiImageSelectorActivity.class);
				intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
				startActivityForResult(intent, REQUEST_IMAGE);
				break;
			case R.id.shop_mine_ll_username://修改用户名
//				Intent intentNickname = new Intent(this, ShopMineNicknameActivity.class);
////				intentShopname.putExtra("shopid", mImSellerManagerBean.getShopinfo().getId());
//				startActivityForResult(intentNickname, REQUEST_SHOPNAME);
//				showEditTextDialog();
				break;
			case R.id.shop_mine_ll_nickname://修改昵称
				showNikeNameDialog("提示");
//				Intent intentNickname = new Intent(this, ShopMineNicknameActivity.class);
//				startActivityForResult(intentNickname, REQUEST_NICKNAME);
//				showEditTextDialog();
				break;
			case R.id.shop_mine_ll_sex://性别
				showSexDialog("提示");
//				Intent intentSex = new Intent(this, ShopMineSexActivity.class);
////				intentShopname.putExtra("shopid", mImSellerManagerBean.getShopinfo().getId());
//				startActivityForResult(intentSex, REQUEST_SEX);
				break;
			case R.id.shop_mine_ll_id://ID
//				Intent intentSex = new Intent(this, ShopMineSexActivity.class);
////				intentShopname.putExtra("shopid", mImSellerManagerBean.getShopinfo().getId());
//				startActivityForResult(intentSex, REQUEST_SEX);
				break;
			case R.id.shop_mine_ll_signature://个性签名
				Intent intentSex = new Intent(this, SignatureActivity.class);
				startActivityForResult(intentSex, REQUEST_SIGNATURE);
				break;
			case R.id.shop_mine_ll_birthday://生日
				showDatePickerDialog();
//				pickDate();
				break;
			case R.id.shop_mine_ll_pwd://修改登录密码
//				AppManager.getAppManager().startNextActivity(mContext, ShopMinePwdActivity.class);
				break;
			case R.id.shop_mine_ll_paypwd://修改支付密码
//				AppManager.getAppManager().startNextActivity(mContext, ShopMinePayPwdActivity.class);
				break;
			case R.id.shop_mine_ll_exit://退出登录
//				signOut(mContext,this);
				mAlertDialog = new AlertDialog.Builder(mContext)
						.setTitle(R.string.dialog_prompt)
						.setMessage(R.string.dialog_exit)
						.setPositiveButton(R.string.dialog_ok,
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialoginterface, int i) {
//										//清空JPush 别名 alias
//										JPushInterface.setAliasAndTags(mContext, null, null, new TagAliasCallback() {
//											@Override
//											public void gotResult(int i, String s, Set<String> set) {
//												//回调方法
//											}
//										});
										MyApplication.getInstance().setIsLogining("");
										MyApplication.getInstance().setUid("");
										MyApplication.getInstance().setU_tel("");
										MyApplication.getInstance().setUname("");
										MyApplication.getInstance().setNickname("");
										MyApplication.getInstance().setUser_Head("");
										((Activity)MainActivity.context).finish();
										Intent intent = new Intent();
//										intent.putExtra("vp", 0);
										AppManager.getAppManager().startNextActivity(mContext, LoginActivity.class, intent);
										mAlertDialog.dismiss();
										finish();
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
				break;

		}
	}

	MaterialDialog mMaterialDialog;
	/**
	 * 修改昵称Dialog
	 * */
	private void showNikeNameDialog(String message){
		View lastView = this.getLayoutInflater().inflate(R.layout.view_dialog_edit, null);
		TextView tv_title = (TextView)lastView.findViewById(R.id.view_dialog_tv_title);
		TextView tv_content = (TextView)lastView.findViewById(R.id.view_dialog_tv_content);
		final EditText edit_context = (EditText)lastView.findViewById(R.id.view_dialog_edit_context);
		tv_title.setText("请输入您的昵称");
//		tv_content.setText("请把智能戒指贴向手机...");
		mMaterialDialog = new MaterialDialog(this);
		mMaterialDialog.setView(lastView);
		if (mMaterialDialog != null) {
			mMaterialDialog.setTitle(R.string.prompt)
					.setMessage(message)
					.setPositiveButton(
							R.string.ok, new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
									// 关闭软键盘
									imm.hideSoftInputFromWindow(edit_context.getWindowToken(), 0);
									String edit_str = edit_context.getText().toString().trim();
									if(TextUtils.isEmpty(edit_str)){
										Toasts.show("请输入您的昵称");
									}else{
										upNikeName(edit_str);
										mMaterialDialog.dismiss();
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

	private void upNikeName(final String str){
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage("加载中...");
			mProgressDialog.show();
		}
		mAsyncHttpClient = new AsyncHttpClient();
		final RequestParams params = new RequestParams();
		params.put("uid",  MyApplication.getInstance().getUid());
		params.put("uname",  str);
		final String url = Constant.BASE_URL+ Constant.URL_USERAPI_UPUNAME;//
		System.out.println("===========================个人中心 昵称用户名修改url ====== " + url);
		System.out.println("===========================params ====== " + params.toString());
		mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}

				System.out.println("===========================个人中心 昵称用户名修改 response ====== " + response.toString());
				if (!TextUtils.isEmpty(response.toString())) {
					mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
					if (mResultBean.getResult().equals("1")) {
						MyApplication.getInstance().setNickname(str);
						tv_nickname.setText(MyApplication.getInstance().getNickname());
						Toasts.show(mResultBean.getMessage());
					} else {
						Toasts.show(mResultBean.getMessage());
					}
				} else {
					showErrorDialog(mContext);
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				showTimeoutDialog(mContext);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				System.out.println("===========================throwable ,responseString =  " + responseString.toString());
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				showErrorDialog(mContext);
			}
		});
	}


	/**
	 * 修改性别Dialog
	 * */
	private void showSexDialog(String message){
		View lastView = this.getLayoutInflater().inflate(R.layout.view_dialog_radio_sex, null);
		TextView tv_title = (TextView)lastView.findViewById(R.id.view_dialog_tv_title);
		TextView tv_content = (TextView)lastView.findViewById(R.id.view_dialog_tv_content);
		final RadioGroup radioGroup = (RadioGroup)lastView.findViewById(R.id.apply_seller_radioGroup);
		final RadioButton rb_business_1 = (RadioButton)lastView.findViewById(R.id.apply_seller_rb_business_1);
		final RadioButton rb_business_2 = (RadioButton)lastView.findViewById(R.id.apply_seller_rb_business_2);
		//sex(1:男   2女 默认是1)
		if("1".equals(MyApplication.getInstance().getSex())){
			rb_business_1.setChecked(true);
		}else if("2".equals(MyApplication.getInstance().getSex())){
			rb_business_2.setChecked(true);
		}
		final String[] sex = new String[1];
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if(checkedId == rb_business_1.getId()){
					sex[0] = "1";
				}else if(checkedId == rb_business_2.getId()){
					sex[0] = "2";
				}else{
					sex[0] = "";
				}
			}
		});
		tv_title.setText("提示");
//		tv_content.setText("请把智能戒指贴向手机...");
		mMaterialDialog = new MaterialDialog(this);
		mMaterialDialog.setView(lastView);
		if (mMaterialDialog != null) {
			mMaterialDialog.setTitle(R.string.prompt)
					.setMessage(message)
					.setPositiveButton(
							R.string.ok, new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									if(TextUtils.isEmpty(sex[0])){
										Toasts.show("请选择性别");
									}else{
										upSex(sex[0]);
										mMaterialDialog.dismiss();
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

	private void upSex(final String str){
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage("加载中...");
			mProgressDialog.show();
		}
		mAsyncHttpClient = new AsyncHttpClient();
		final RequestParams params = new RequestParams();
		params.put("uid",  MyApplication.getInstance().getUid());
		params.put("sex",  str);
		final String url = Constant.BASE_URL + Constant.URL_USERAPI_UPSEX;//
		System.out.println("===========================个人中心 性别修改url ====== " + url);
		System.out.println("===========================params ====== " + params.toString());
		mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}

				System.out.println("===========================个人中心 性别修改 response ====== " + response.toString());
				if (!TextUtils.isEmpty(response.toString())) {
					mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
					if (mResultBean.getResult().equals("1")) {
						MyApplication.getInstance().setSex(str);
						if("1".equals(MyApplication.getInstance().getSex())){
							tv_sex.setText("男");
						}else if("2".equals(MyApplication.getInstance().getSex())){
							tv_sex.setText("女");
						}
						Toasts.show(mResultBean.getMessage());
					} else {
						Toasts.show(mResultBean.getMessage());
					}
				} else {
					showErrorDialog(mContext);
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				showTimeoutDialog(mContext);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				System.out.println("===========================throwable ,responseString =  " + responseString.toString());
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				showErrorDialog(mContext);
			}
		});
	}

	private void upHeadPhoto(){
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage("加载中...");
			mProgressDialog.show();
		}
		mAsyncHttpClient = new AsyncHttpClient();
		final RequestParams params = new RequestParams();
		params.put("uid", MyApplication.getInstance().getUid());
		params.put("pic", img_base64);//img_base64
		final String url = Constant.BASE_URL + Constant.URL_USERAPI_UPPIC;
		System.out.println("===========================个人中心 修改头像url ====== " + url);
		System.out.println("===========================params ====== " + params.toString());
		mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}

				System.out.println("===========================个人中心 修改头像 response ====== " + response.toString());
				if (!TextUtils.isEmpty(response.toString())) {
					mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
					if (mResultBean.getResult().equals("1")) {
						Toasts.show(mResultBean.getMessage());
						ImageLoader.getInstance().displayImage("file://" + path, img_head, ImageLoaderOptions.get_face_Options());
						MyApplication.getInstance().setUser_Head(mResultBean.getPic());
//                        img_test.setImageBitmap(ImageTools.base64ToBitmap(mResultBean.getImgs1()));
					} else {
						Toasts.show(mResultBean.getMessage());
					}
				} else {
					showErrorDialog(mContext);
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				showErrorDialog(mContext);
			}
		});
	}

	private void upShengri(){
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage("加载中...");
			mProgressDialog.show();
		}
		mAsyncHttpClient = new AsyncHttpClient();
		final RequestParams params = new RequestParams();
		params.put("uid",  MyApplication.getInstance().getUid());
		params.put("shengri",  mYear+"-"+(mMonth+1)+"-"+mDay);
		final String url = Constant.BASE_URL;

		mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				System.out.println("===========================url ====== " + url);
				System.out.println("===========================params ====== " + params.toString());
				System.out.println("===========================个人中心 生日修改 response ====== " + response.toString());
				if (!TextUtils.isEmpty(response.toString())) {
					mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
					if (mResultBean.getResult().equals("1")) {
						MyApplication.getInstance().setShengri(mYear + "-" + (mMonth + 1) + "-" + mDay);
						tv_birthday.setText(MyApplication.getInstance().getShengri());
					} else {
						Toasts.show(mResultBean.getMessage());
					}
				} else {
					showErrorDialog(mContext);
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				showErrorDialog(mContext);
			}
		});
	}


	public void pickDate() {
		Calendar cal = Calendar.getInstance();
		final DatePickerDialog mDialog = new DatePickerDialog(this, null,
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

		//手动设置按钮
		mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
				DatePicker datePicker = mDialog.getDatePicker();
				datePicker.setCalendarViewShown(false);
				int year = datePicker.getYear();
				int month = datePicker.getMonth();
				int day = datePicker.getDayOfMonth();
				System.out.println(year + "," + month + "," + day);
				mDialog.dismiss();
			}
		});
		//取消按钮，如果不需要直接不设置即可
		mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("BUTTON_NEGATIVE~~");
				mDialog.dismiss();
			}
		});

		mDialog.show();
	}

	private void showDatePickerDialog(){
		//通过自定义控件AlertDialog实现
		AlertDialog.Builder builder = new AlertDialog.Builder(ShopMineActivity.this);
		View view = (LinearLayout) getLayoutInflater().inflate(R.layout.date_dialog, null);
		final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
		//设置日期简略显示 否则详细显示 包括:星期周
		datePicker.setCalendarViewShown(false);
		//初始化当前日期
		calendar.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), null);
		//设置date布局
		builder.setView(view);
		builder.setTitle("请选择日期");
		builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//赋值后面闹钟使用
				mYear = datePicker.getYear();
				mMonth = datePicker.getMonth();
				mDay = datePicker.getDayOfMonth();
				dialog.cancel();
				upShengri();
			}
		});
		builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}

	private void showEditTextDialog(){
		final EditText inputServer = new EditText(mContext);
		inputServer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("修改用户名").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setMessage("请输入用户名")
				.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				InputMethodManager immDialog = (InputMethodManager) (mContext.getSystemService(Context.INPUT_METHOD_SERVICE));
				immDialog.hideSoftInputFromWindow(inputServer.getWindowToken(), 0);
				String str_nickname = inputServer.getText().toString();
				if(TextUtils.isEmpty(str_nickname)){
					Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
					return;
				}else{
					Toast.makeText(mContext, "用户名："+str_nickname, Toast.LENGTH_SHORT).show();
				}
			}
		}).show();
	}

	private void showDialog(String title,String msg){
		final EditText inputServer = new EditText(mContext);
		inputServer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		final MaterialDialog materialDialog = new MaterialDialog(mContext);
		materialDialog.setTitle(title);
		materialDialog.setMessage(msg);
		materialDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager immDialog = (InputMethodManager) (mContext.getSystemService(Context.INPUT_METHOD_SERVICE));
				immDialog.hideSoftInputFromWindow(inputServer.getWindowToken(), 0);
				String str_nickname = inputServer.getText().toString();
				if(TextUtils.isEmpty(str_nickname)){
					Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
					return;
				}else{
					Toast.makeText(mContext, "用户名："+str_nickname, Toast.LENGTH_SHORT).show();
//					tv_username.setText(str_nickname);
					materialDialog.dismiss();
				}
			}
		});
		materialDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				materialDialog.dismiss();
			}
		});

		materialDialog.show();
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_IMAGE://修改头像
				if(resultCode == RESULT_OK){

					mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

					Intent intent=new Intent();

					intent.putStringArrayListExtra("path",mSelectPath);
					intent.putExtra("tag","ShopMineActivity");
					AppManager.getAppManager().startFragmentNextActivity(mContext, ImageCutActivity.class, intent);

					//   Toast.makeText(PersonnalInfomation.this, "file://"+mSelectPath, Toast.LENGTH_SHORT).show();
					//   StringBuilder sb = new StringBuilder();
					for(String p: mSelectPath){
//	                    sb.append(p);
//	                    sb.append("\n");

//	                	  ImageLoader.getInstance().displayImage("file://"+p, img_face,ImageLoaderOptions.getOptions());
					}
					//  mResultText.setText(sb.toString());

				}
				break;
			case REQUEST_NICKNAME://修改昵称
				if(resultCode == RESULT_OK){
					String result = data.getExtras().getString("result");//得到Activity 关闭后返回的数据
					System.out.println("===========================REQUEST_NICKNAME  result = " + result);
					tv_nickname.setText(MyApplication.getInstance().getNickname());
				}
				break;
			case REQUEST_SEX:
				if(resultCode == RESULT_OK){
					String result = data.getExtras().getString("result");//得到Activity 关闭后返回的数据
					System.out.println("===========================REQUEST_SEX  result = " + result);
					if("1".equals(MyApplication.getInstance().getSex())){
						tv_sex.setText("男");
					}else if("2".equals(MyApplication.getInstance().getSex())){
						tv_sex.setText("女");
					}
				}
				break;
			case REQUEST_SIGNATURE:
				if(resultCode == RESULT_OK){
					String result = data.getExtras().getString("result");//得到Activity 关闭后返回的数据
					System.out.println("===========================REQUEST_SIGNATURE  result = " + result);
					if(TextUtils.isEmpty(result)){
						tv_signature.setText("这个人很懒，什么也没写");
					}else{
						tv_signature.setText(result);
					}
				}
				break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onRestart() {
		super.onRestart();

//		ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), img_head, ImageLoaderOptions.get_face_Options());
//		System.out.println("===========================个人中心 User_Head ====== " + Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head());
		tv_username.setText(MyApplication.getInstance().getUname());
	}
}