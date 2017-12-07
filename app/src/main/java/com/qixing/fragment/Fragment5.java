package com.qixing.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.activity.DownloadListActivity;
import com.qixing.activity.DownloadManagerActivity;
import com.qixing.activity.HistoryActivity;
import com.qixing.activity.MessageActivity;
import com.qixing.activity.MyAmountActivity;
import com.qixing.activity.MyCollectionActivity;
import com.qixing.activity.MyRebateActivity;
import com.qixing.activity.MyWalletActivity;
import com.qixing.activity.QXliveActivity;
import com.qixing.activity.RebateEnterActivity;
import com.qixing.activity.SettingActivity;
import com.qixing.activity.ShareProfitActivity;
import com.qixing.activity.ShopMineActivity;
import com.qixing.activity.TestViewPagerActivity;
import com.qixing.activity.VIPCenterActivity;
import com.qixing.app.AppManager;
import com.qixing.app.MyApplication;
import com.qixing.bean.ResultBean;
import com.qixing.bean.ShopMineBean;
import com.qixing.global.Constant;
import com.qixing.qxlive.QXLiveShowActivity;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.CircleImageView;
import com.qixing.welcome.ComplementUserinfoActivity;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import cn.woblog.android.downloader.callback.DownloadManager;
import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;


public class Fragment5 extends BaseFragment implements View.OnClickListener {
	TextView msg;

	private PullToRefreshScrollView mScrollView;
    // 导航栏
    private LinearLayout ll_left,ll_right,ll_imgandtex;
    private TextView tv_title;
    private EditText edit_search;
    private TextView left_tv,right_tv;
    private ImageView left_img,right_img,dj_img;

	private CircleImageView img_head;

	//测试
	private LinearLayout ll_test;

	private LinearLayout ll_head,ll_wallet,ll_history,ll_collection,ll_kefu,ll_rebate,ll_seelive,ll_startlive,ll_setting,ll_message,ll_download_queue,ll_vip,ll_fenxiang;
    private TextView tv_vip;

	private TextView tv_name;

	private ShopMineBean mShopMineBean;
	private ResultBean mResultBean;
	private boolean isRefresh = false;

	private String isPayPwd = "0";

	private boolean isRestart = false;


	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.fragment5);
		mAsyncHttpClient=new AsyncHttpClient();
		// 导航栏
        ll_left = getViewById(R.id.title_ll_left);
        ll_left.setOnClickListener(this);
        ll_left.setVisibility(View.GONE);

		tv_title = getViewById(R.id.title_center_tv_title);
		tv_title.setText("我");
		tv_title.setVisibility(View.VISIBLE);
		edit_search = getViewById(R.id.title_center_edit_search);
		edit_search.setVisibility(View.GONE);

        ll_right = getViewById(R.id.title_ll_right);
        ll_right.setVisibility(View.GONE);
        ll_right.setOnClickListener(this);

		img_head = getViewById(R.id.mine_body_img_head);
		dj_img=getViewById(R.id.mine_body_iv_dj);

		tv_vip=getViewById(R.id.mine_body_tv_vip);
		//ll_head,ll_wallet,ll_history,ll_collection,ll_recommend,ll_enroll,ll_startlive,ll_rebate,ll_setting;
		ll_head = getViewById(R.id.mine_body_ll_head);
		ll_head.setOnClickListener(this);
		tv_name = getViewById(R.id.mine_body_tv_name);
		ll_vip=getViewById(R.id.mine_body_ll_huiyuan);
		ll_vip.setOnClickListener(this);
		ll_wallet = getViewById(R.id.mine_body_ll_wallet);
		ll_wallet.setOnClickListener(this);
		ll_fenxiang=getViewById(R.id.mine_body_ll_share_money);
		ll_fenxiang.setOnClickListener(this);
		ll_download_queue=getViewById(R.id.mine_body_ll_download_queue);
		ll_download_queue.setOnClickListener(this);
		ll_history = getViewById(R.id.mine_body_ll_history);
		ll_history.setOnClickListener(this);
		ll_collection = getViewById(R.id.mine_body_ll_collection);
		ll_collection.setOnClickListener(this);
		ll_kefu = getViewById(R.id.mine_body_ll_kefu);
		ll_kefu.setOnClickListener(this);
		ll_rebate = getViewById(R.id.mine_body_ll_rebate);
		ll_rebate.setOnClickListener(this);
		ll_seelive = getViewById(R.id.mine_body_ll_seelive);
		ll_seelive.setOnClickListener(this);
		ll_startlive = getViewById(R.id.mine_body_ll_startlive);
		ll_startlive.setOnClickListener(this);
		ll_setting = getViewById(R.id.mine_body_ll_setting);
		ll_setting.setOnClickListener(this);
		ll_message = getViewById(R.id.mine_body_ll_message);
		ll_message.setOnClickListener(this);

		ll_test = getViewById(R.id.mine_body_ll_test);
		ll_test.setOnClickListener(this);
		ll_test.setVisibility(View.GONE);


		mScrollView = getViewById(R.id.fragment5_scrollview);
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
		mScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		init(mScrollView);
		         /*
         * setOnRefreshListener(OnRefreshListener listener):设置刷新监听器；
         * setOnLastItemVisibleListener(OnLastItemVisibleListener listener):设置是否到底部监听器；
         * setOnPullEventListener(OnPullEventListener listener);设置事件监听器；
         * onRefreshComplete()：设置刷新完成
         */
        /*
         * pulltorefresh.setOnScrollListener()
         */
		// SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
		// SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
		// SCROLL_STATE_IDLE(0) 停止滚动
        /*
         * setOnLastItemVisibleListener
         * 当用户拉到底时调用
         */
        /*
         * setOnTouchListener是监控从点下鼠标 （可能拖动鼠标）到放开鼠标（鼠标可以换成手指）的整个过程 ，他的回调函数是onTouchEvent（MotionEvent event）,
         * 然后通过判断event.getAction()是MotionEvent.ACTION_UP还是ACTION_DOWN还是ACTION_MOVE分别作不同行为。
         * setOnClickListener的监控时间只监控到手指ACTION_DOWN时发生的行为
         */
		mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				isRefresh = true;
				initDate();
				mScrollView.onRefreshComplete();
			}
		});
		if(!TextUtils.isEmpty(MyApplication.getInstance().getUser_Head())){
			dj_img.setVisibility(View.VISIBLE);
			if(MyApplication.getInstance().getUser_Head().startsWith("http")){
				ImageLoader.getInstance().displayImage(MyApplication.getInstance().getUser_Head(),img_head, ImageLoaderOptions.get_face_Options());
			}else {
				ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+MyApplication.getInstance().getUser_Head(),img_head, ImageLoaderOptions.get_face_Options());
			}
		}
		System.out.println("===========================Fragment5 userhead ======= " + Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head());
		if(!TextUtils.isEmpty(MyApplication.getInstance().getU_djpic())){
			if(MyApplication.getInstance().getU_djpic().startsWith("http")){
				ImageLoader.getInstance().displayImage(MyApplication.getInstance().getU_djpic(),dj_img, ImageLoaderOptions.get_face_Options());
			}else {
				ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+MyApplication.getInstance().getU_djpic(),dj_img, ImageLoaderOptions.get_face_Options());
			}
		}
		System.out.println("===========================Fragment5 userdjpic ======= " + Constant.BASE_URL_IMG + MyApplication.getInstance().getU_djpic());
		tv_name.setText(MyApplication.getInstance().getNickname());

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

	@Override
	protected void setListener() {

	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		System.out.println("===========================个人中心 processLogic ======= ");
		if("1".equals(MyApplication.getInstance().getIsLogining())){
		}
//		initDate();
	}

	@Override
	protected void onUserVisible() {
		System.out.println("===========================个人中心 onUserVisible ======= ");
		initDate();
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("===========================个人中心 onResume ======= ");
		if("1".equals(MyApplication.getInstance().getIsLogining())){
			//刷新
		}else{

		}
		if(isRestart && getUserVisibleHint()){//是否重新返回页面 并且当前页面是否正在显示
			isRestart = false;//是否重新返回页面
			isRefresh = true;
			initDate();
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		isRestart = true;

	}

	private void initDate(){
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setMessage("加载中...");
		}
		if(!isRefresh){
//			mProgressDialog.show();
		}
		RequestParams params = new RequestParams();
		params.put("uid",  MyApplication.getInstance().getUid());
		final String url = Constant.BASE_URL+Constant.URL_USERAPI_GXINFO;
		System.out.println("===========================个人中心 url ======= " + url);
		mAsyncHttpClient.post(getContext(), url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				if(isRefresh){
					isRefresh = false;
					mScrollView.onRefreshComplete();
				}
				System.out.println("===========================个人中心response ======= " + response.toString());
				if (!TextUtils.isEmpty(response.toString())) {
					mShopMineBean = new Gson().fromJson(response.toString(), ShopMineBean.class);
					if (mShopMineBean.getResult().equals("1")) {
						//		tv_name,tv_commodity,tv_shop,tv_brand;
						//		tv_surplus_money,tv_jinbi;
						MyApplication.getInstance().setUser_Head(mShopMineBean.getInfo().getPic());
//						MyApplication.getInstance().setUname(mShopMineBean.getInfo().getUname());
						MyApplication.getInstance().setNickname(mShopMineBean.getInfo().getUname());
						MyApplication.getInstance().setSex(mShopMineBean.getInfo().getSex());
						MyApplication.getInstance().setU_tel(mShopMineBean.getInfo().getU_phone());
						MyApplication.getInstance().setQianm(mShopMineBean.getInfo().getQianm());
						MyApplication.getInstance().setPromotionid(mShopMineBean.getInfo().getPromotionid());
						MyApplication.getInstance().setKf_phone(mShopMineBean.getInfo().getKf());
						MyApplication.getInstance().setJf_count(mShopMineBean.getInfo().getJifen());
						MyApplication.getInstance().setVip_dj(mShopMineBean.getInfo().getDengji());
//						MyApplication.getInstance().setShengri(mShopMineBean.getInfo().getShengri());

						if(!TextUtils.isEmpty(MyApplication.getInstance().getUser_Head())){
							if(MyApplication.getInstance().getUser_Head().startsWith("http")){
								ImageLoader.getInstance().displayImage(MyApplication.getInstance().getUser_Head(),img_head, ImageLoaderOptions.get_face_Options());
							}else {
								ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+MyApplication.getInstance().getUser_Head(),img_head, ImageLoaderOptions.get_face_Options());
							}
						}
						System.out.println("===========================Fragment5 userhead ======= " + Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head());
						tv_name.setText(MyApplication.getInstance().getNickname());
//						tv_surplus_money.setText(mShopMineBean.getInfo().getMoney());//帐户余额
//						tv_yhj_cs.setText(mShopMineBean.getInfo().getAgent());

						if (TextUtils.isEmpty(MyApplication.getInstance().getVip_dj())){
							tv_vip.setText("非会员");
						}else{
							if ("1".equals(MyApplication.getInstance().getVip_dj())){
								tv_vip.setText("Vip会员");
							}else{
								tv_vip.setText("普通会员");
							}
						}

						if(TextUtils.isEmpty(mShopMineBean.getInfo().getZfpwd())){
							isPayPwd = "0";
							MyApplication.getInstance().setPref_ispaypwd(isPayPwd);
						}else{
							isPayPwd = "1";
							MyApplication.getInstance().setPref_ispaypwd(isPayPwd);
						}
//						rtmp://qixingshidai.rtmplive.ks-cdn.com/live/114

					} else {
//						Toasts.show(mShoppingMineBean.getMessage());
					}
				} else {
					showErrorDialog(getActivity());
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				if(isRefresh){
					isRefresh = false;
					mScrollView.onRefreshComplete();
				}
				showTimeoutDialog(getActivity());
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				System.out.println("===========================throwable ,responseString =  " + responseString.toString());
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				if(isRefresh){
					isRefresh = false;
					mScrollView.onRefreshComplete();
				}
				showErrorDialog(getActivity());
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()){
			case R.id.title_ll_left://设置
//				intent=new Intent();
//				if("1".equals(MyApplication.getInstance().getIsLogining())){
//					intent.putExtra("tag", "SubFragment1");
//					AppManager.getAppManager().startNextActivity(getActivity(), ShopMineActivity.class,intent);
//				}else{
//					AppManager.getAppManager().startNextActivity(getActivity(), LoginActivity.class);
//				}
				break;
			case R.id.title_ll_right://消息列表
//				AppManager.getAppManager().startNextActivity(getActivity(), MessageActivity.class);
				break;
			case R.id.mine_body_ll_head://个人中心
				AppManager.getAppManager().startNextActivity(getActivity(), ShopMineActivity.class);
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
				break;
			case R.id.mine_body_ll_huiyuan://会员中心
				AppManager.getAppManager().startNextActivity(getActivity(), VIPCenterActivity.class);
				break;
			case R.id.mine_body_ll_wallet://账户余额
				if(TextUtils.isEmpty(MyApplication.getInstance().getU_tel())){
					AppManager.getAppManager().startNextActivity(getActivity(), ComplementUserinfoActivity.class);
				}else{
					intent = new Intent();
					intent.putExtra("money",mShopMineBean.getInfo().getMoney());
					intent.putExtra("isPayPwd",isPayPwd);//0未设置支付密码 1已设置支付密码
					AppManager.getAppManager().startNextActivity(getActivity(), MyWalletActivity.class,intent);
				}
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
				break;
			case R.id.mine_body_ll_share_money://分享我赚钱
				AppManager.getAppManager().startNextActivity(getActivity(), ShareProfitActivity.class);
				break;
			case R.id.mine_body_ll_history://观看历史
				intent = new Intent();
//				intent.putExtra("tag","SubFragment1");
				AppManager.getAppManager().startNextActivity(getActivity(), HistoryActivity.class,intent);
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
				break;
			case R.id.mine_body_ll_download_queue://离线缓存
//				intent=new Intent();
//				if("success".equals(MyApplication.getInstance().getIs_Down())) {
					AppManager.getAppManager().startFragmentNextActivity(getActivity(), DownloadManagerActivity.class);
//				}
				break;
			case R.id.mine_body_ll_collection://我的收藏
                intent = new Intent();
//                intent.putExtra("state","");
                AppManager.getAppManager().startNextActivity(getActivity(), MyCollectionActivity.class,intent);
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
                break;
			case R.id.mine_body_ll_kefu://在线客服
				if("1".equals(MyApplication.getInstance().getIsLogining())){
					if (!TextUtils.isEmpty(mShopMineBean.getInfo().getKf())) {
						Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mShopMineBean.getInfo().getKf()));
						if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)) {
							//has permission, do operation directly
							startActivity(intentPhone);
						} else {

						}
					}
				}else{
//					Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01057272494"));
//					if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)) {
//						//has permission, do operation directly
//						startActivity(intentPhone);
//					} else {
//
//					}
				}
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
				break;
			case R.id.mine_body_ll_rebate://分销返利
				if(TextUtils.isEmpty(MyApplication.getInstance().getU_tel())){
					AppManager.getAppManager().startNextActivity(getActivity(), ComplementUserinfoActivity.class);
				}else{
					if("1".equals(mShopMineBean.getInfo().getDengji())){
						AppManager.getAppManager().startNextActivity(getActivity(),MyRebateActivity.class);
					}else{
						intent = new Intent();
						intent.putExtra("dengji",mShopMineBean.getInfo().getDengji());
						intent.putExtra("isPayPwd",isPayPwd);//0未设置支付密码 1已设置支付密码
						AppManager.getAppManager().startNextActivity(getActivity(), RebateEnterActivity.class,intent);
					}
				}
//				if("1".equals(MyApplication.getInstance().getIsLogining())){
//				}else{
//					AppManager.getAppManager().startNextActivity(getActivity(), LoginActivity.class);
//				}
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
				break;
			case R.id.mine_body_ll_seelive://观看直播
                intent = new Intent();
//                intent.putExtra("state","");
                AppManager.getAppManager().startNextActivity(getActivity(), QXliveActivity.class,intent);
//				AppManager.getAppManager().startNextActivity(getActivity(), QXLiveSeeActivity.class,intent);
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
			break;
			case R.id.mine_body_ll_startlive://开始直播
                intent = new Intent();
//                intent.putExtra("state","");
                AppManager.getAppManager().startNextActivity(getActivity(), QXLiveShowActivity.class,intent);
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
			break;
			case R.id.mine_body_ll_setting://设置
                intent = new Intent();
//                intent.putExtra("state","");
                AppManager.getAppManager().startNextActivity(getActivity(), SettingActivity.class,intent);
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
				break;
			case R.id.mine_body_ll_test://设置
				intent = new Intent();
//                intent.putExtra("state","");
//				AppManager.getAppManager().startNextActivity(getActivity(), TestActivity.class,intent);
				AppManager.getAppManager().startNextActivity(getActivity(), TestViewPagerActivity.class,intent);
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
				break;
			case R.id.mine_body_ll_message://我的消息
				intent = new Intent();
//                intent.putExtra("state","");
				intent.putExtra("title","我的消息");
				AppManager.getAppManager().startNextActivity(getActivity(), MessageActivity.class,intent);
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
				break;

		}
	}

	MaterialDialog mMaterialDialog;

	public void showTjrDialog(String message) {
		View lastView = getActivity().getLayoutInflater().inflate(R.layout.view_dialog_edit, null);
		TextView tv_title = (TextView)lastView.findViewById(R.id.view_dialog_tv_title);
		TextView tv_content = (TextView)lastView.findViewById(R.id.view_dialog_tv_content);
		final EditText edit_context = (EditText)lastView.findViewById(R.id.view_dialog_edit_context);
		tv_title.setText("请输入您的推荐人ID");
//		tv_content.setText("请把智能戒指贴向手机...");
		mMaterialDialog = new MaterialDialog(getActivity());
		mMaterialDialog.setView(lastView);
		if (mMaterialDialog != null) {
			mMaterialDialog.setTitle(R.string.prompt)
					.setMessage(message)
					.setPositiveButton(
							R.string.ok, new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
									// 关闭软键盘
									imm.hideSoftInputFromWindow(edit_context.getWindowToken(), 0);
									String edit_str = edit_context.getText().toString().trim();
									if(TextUtils.isEmpty(edit_str)){
										Toasts.show("推荐人不能为空");
									}else{
										mMaterialDialog.dismiss();
										adtjr(edit_str);
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

	private void adtjr(String edit_str){
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setMessage("加载中...");
		}
		mProgressDialog.show();
		RequestParams params = new RequestParams();
		params.put("uid", MyApplication.getInstance().getUid());
		params.put("tjno", edit_str);
		final String url = Constant.BASE_URL+ Constant.URL_USERAPI_ADTJR;
		System.out.println("===========================绑定推荐人url ======= " + url);
		System.out.println("===========================params ======= " + params.toString());
		mAsyncHttpClient.post(getContext(), url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				if(isRefresh){
					isRefresh = false;
					mScrollView.onRefreshComplete();
				}
				System.out.println("===========================个人中心 绑定推荐人response ======= " + response.toString());
				if (!TextUtils.isEmpty(response.toString())) {
					mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
					if (mResultBean.getResult().equals("1")) {
						Toasts.show(mResultBean.getMessage());
						AppManager.getAppManager().startNextActivity(getActivity(), MyRebateActivity.class);
					} else {
						Toasts.show(mResultBean.getMessage());
					}
				} else {
					showErrorDialog(getActivity());
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				if(isRefresh){
					isRefresh = false;
					mScrollView.onRefreshComplete();
				}
				showErrorDialog(getActivity());
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);

				System.out.println("===========================throwable ,responseString =  " + responseString);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				showTimeoutDialog(getActivity());
			}
		});
	}
}