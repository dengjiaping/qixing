package com.qixing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.qixing.activity.ShopMineActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.qixing.R;
import com.qixing.adapter.F4RecommendedInfoAdapter;
import com.qixing.adapter.RecommendedInfoAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.MyApplication;
import com.qixing.bean.RecommendedInfoBean;
import com.qixing.bean.ResultBean;
import com.qixing.view.CircleImageView;
import com.qixing.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;


public class Fragment4 extends BaseFragment implements View.OnClickListener {
	TextView msg;

	private PullToRefreshScrollView mScrollView;
    // 导航栏
    private LinearLayout ll_left,ll_right,ll_imgandtex;
    private TextView tv_title;
    private EditText edit_search;
    private TextView left_tv,right_tv;
    private ImageView left_img,right_img;

	/**
	 * 资讯页buanner
	 * */
	private BGABanner banner;

	/**
	 * 七星资讯
	 * */
	private LinearLayout ll_infomore;
	private MyListView lv_info;

	private List<RecommendedInfoBean> mInfoList;
	private F4RecommendedInfoAdapter mF4RecommendedInfoAdapter;

	private ResultBean mResultBean;
	private boolean isRefresh = false;


	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.fragment4);
		mAsyncHttpClient=new AsyncHttpClient();
		// 导航栏
        ll_left = getViewById(R.id.title_ll_left);
        ll_left.setOnClickListener(this);
        ll_left.setVisibility(View.GONE);

		tv_title = getViewById(R.id.title_center_tv_title);
		tv_title.setText("我的");
		tv_title.setVisibility(View.VISIBLE);
		edit_search = getViewById(R.id.title_center_edit_search);
		edit_search.setVisibility(View.GONE);

        ll_right = getViewById(R.id.title_ll_right);
        ll_right.setVisibility(View.GONE);
        ll_right.setOnClickListener(this);


		mScrollView = getViewById(R.id.fragment4_scrollview);
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


		/**
		 * 首页buanner
		 * */
		banner = getViewById(R.id.fragment4_bgabanner4);
		// 用Java代码方式设置切换动画
		banner.setTransitionEffect(TransitionEffect.Default);
		// banner.setPageTransformer(new RotatePageTransformer());
		// 设置page切换时长
		banner.setPageChangeDuration(1000);
		List<View> views = new ArrayList<>();
		views.add(getPageView(R.drawable.bgabanner_test));
		views.add(getPageView(R.drawable.bgabanner_test));
		views.add(getPageView(R.drawable.bgabanner_test));
		banner.setData(views);

		/**
		 * 七星资讯
		 * */
//		ll_infomore = getViewById(R.id.item_fragment1_info_ll_infomore);
//		ll_infomore.setOnClickListener(this);
		lv_info = getViewById(R.id.item_fragment4_info_lv_info);
		lv_info.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
		lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
		initTestInfo();
	}

	private View getPageView(@DrawableRes int resid) {
		ImageView imageView = new ImageView(getActivity());
		imageView.setImageResource(resid);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		return imageView;
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
		if("1".equals(MyApplication.getInstance().getIsLogining())){
			initDate();
		}
	}

	@Override
	protected void onUserVisible() {

	}

	private void initDate(){
//		if (mProgressDialog == null) {
//			mProgressDialog = new ProgressDialog(getActivity());
//			mProgressDialog.setMessage("加载中...");
//		}
//		if(!isRefresh){
////			mProgressDialog.show();
//		}
//
//		RequestParams params = new RequestParams();
//		params.put("uid",  MyApplication.getInstance().getUid());
//		final String url = Constant.BASE_URL+Constant.URL_APP_INDEX;
//		mAsyncHttpClient.post(getContext(), url, params, new JsonHttpResponseHandler() {
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//				super.onSuccess(statusCode, headers, response);
//
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//				if(isRefresh){
//					isRefresh = false;
//					mScrollView.onRefreshComplete();
//				}
//				System.out.println("===========================url ======= " + url);
//				System.out.println("===========================个人中心response ======= " + response.toString());
//				if (!TextUtils.isEmpty(response.toString())) {
//					mShoppingMineBean = new Gson().fromJson(response.toString(), ShopMineBean.class);
//					if (mShoppingMineBean.getResult().equals("1")) {
//						//		tv_name,tv_commodity,tv_shop,tv_brand;
//						//		tv_surplus_money,tv_jinbi;
//						MyApplication.getInstance().setUser_Head(mShoppingMineBean.getUserinfo().getPic());
//						MyApplication.getInstance().setUname(mShoppingMineBean.getUserinfo().getUname());
//						MyApplication.getInstance().setNickname(mShoppingMineBean.getUserinfo().getNc());
//						MyApplication.getInstance().setSex(mShoppingMineBean.getUserinfo().getSex());
//						MyApplication.getInstance().setShengri(mShoppingMineBean.getUserinfo().getShengri());
//
//						ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), img_head, ImageLoaderOptions.get_face_Options());
//						System.out.println("===========================userhead ======= " + Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head());
//						tv_name.setText(MyApplication.getInstance().getUname());
//						tv_surplus_money.setText(mShoppingMineBean.getUserinfo().getSurplus_money());
//						tv_yhj_cs.setText(mShoppingMineBean.getUserinfo().getYhj_cs());
//
//						tv_yue.setText("¥"+mShoppingMineBean.getUserinfo().getSurplus_money());
//						tv_juan.setText(mShoppingMineBean.getUserinfo().getYhj_cs());
//
//						mShoppingMineBean.getUserinfo().getU_tel();
//
//					} else {
////						Toasts.show(mShoppingMineBean.getMessage());
//					}
//				} else {
//					mAlertDialog = new AlertDialog.Builder(getActivity())
//							.setTitle(R.string.dialog_prompt)
//							.setMessage(R.string.dialog_wrong)
//							.setPositiveButton(R.string.dialog_ok,
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialoginterface, int i) {
//											mAlertDialog.dismiss();
////                                        finish();
//										}
//									}).show();
//				}
//
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//				super.onFailure(statusCode, headers, throwable, errorResponse);
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//				if(isRefresh){
//					isRefresh = false;
//					mScrollView.onRefreshComplete();
//				}
//				mAlertDialog = new AlertDialog.Builder(getActivity())
//						.setTitle(R.string.dialog_prompt)
//						.setMessage(R.string.dialog_wrong)
//						.setPositiveButton(R.string.dialog_ok,
//								new DialogInterface.OnClickListener() {
//									public void onClick(
//											DialogInterface dialoginterface, int i) {
//										mAlertDialog.dismiss();
////                                        finish();
//									}
//								}).show();
//			}
//		});
	}


	@Override
	public void onResume() {
		super.onResume();

		if("1".equals(MyApplication.getInstance().getIsLogining())){
			//刷新
			isRefresh = true;
			initDate();
		}else{

		}
	}

	private void initTestInfo(){
		mInfoList = new ArrayList<RecommendedInfoBean>();
		for (int i = 0;i<10;i++){
			RecommendedInfoBean mRecommendedInfoBean = new RecommendedInfoBean();
//			mRecommendedInfoBean.setName("");
//			mRecommendedInfoBean.setNum("2"+i+"万");
//			mRecommendedInfoBean.setContext("");
			mRecommendedInfoBean.setTimes("2016-12-1"+i);
			if((i+1)%5!=0){
				mRecommendedInfoBean.setType("1");
			}else{
				mRecommendedInfoBean.setType("2");
			}
			mInfoList.add(mRecommendedInfoBean);
		}

//		mF4RecommendedInfoAdapter = new F4RecommendedInfoAdapter(getActivity(),mInfoList);
//		lv_info.setAdapter(mF4RecommendedInfoAdapter);
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
			case R.id.mine_head_img_head://个人中心
				AppManager.getAppManager().startNextActivity(getActivity(), ShopMineActivity.class);
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
				break;
			case R.id.mine_head_ll_surplus_money://账户余额
//				intent = new Intent();
//				intent.putExtra("surplus_money",mShoppingMineBean.getUserinfo().getSurplus_money());
//				AppManager.getAppManager().startNextActivity(getActivity(), MyAmountActivity.class,intent);
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
				break;
			case R.id.mine_head_ll_yhj_cs://优惠券
//				intent = new Intent();
//				intent.putExtra("tag","SubFragment1");
//				AppManager.getAppManager().startNextActivity(getActivity(), QXliveActivity.class,intent);
//				getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
				break;
			//（0未付款，1已付款(未发货)，2待收货(已发货)，3已收货(未评价)，4已过期，5已取消，6申请退货，7退货成功，8交易完成（已评价））
		}
	}



}