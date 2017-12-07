package com.qixing.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.AdapterView;
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
import com.qixing.R;
import com.qixing.activity.AllVideosActivity;
import com.qixing.activity.MoreVideosActivity;
import com.qixing.activity.PlayVideoActivity;
import com.qixing.activity.ReadPDFActivity;
import com.qixing.activity.SearchActivity;
import com.qixing.activity.webview.MyWebActivity;
import com.qixing.adapter.F2RecommendedVideoAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.MyApplication;
import com.qixing.bean.BannerVideoBean;
import com.qixing.bean.F2RecommendedVideoBean;
import com.qixing.bean.F2RecommendedVideoJson;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.view.MyGridView;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class Fragment2 extends BaseFragment implements View.OnClickListener {

	TextView msg;
	private TextView tv_title;
	private EditText edit_search;
    private LinearLayout ll_left,ll_right,ll_imgandtex;
    private TextView left_tv,right_tv;
    private ImageView left_img,right_img;

	private PullToRefreshScrollView mScrollView;
	private boolean isRefresh = false;

	/**
	 * 推荐视频
	 * */
	private LinearLayout ll_videomore;
	private MyGridView gv_video;

	private List<F2RecommendedVideoBean> mVideoList1;
	private F2RecommendedVideoAdapter mF2RecommendedVideoAdapter1;
	private List<Integer> videos_1;

	/**
	 * 免费体验课
	 * */
	private LinearLayout ll_livemore;
	private MyGridView gv_live;

	private List<F2RecommendedVideoBean> mVideoList2;
	private F2RecommendedVideoAdapter mF2RecommendedVideoAdapter2;
	private List<Integer> videos_2;
	/**
	 * 会员专区
	 * */
	private LinearLayout ll_vipmore;
	private MyGridView gv_vip;

	private List<F2RecommendedVideoBean> mVideoList3;
	private F2RecommendedVideoAdapter mF2RecommendedVideoAdapter3;
	private List<Integer> videos_3;

	private F2RecommendedVideoJson mF2RecommendedVideoJson;

	private BannerVideoBean mBannerVideoBean;
	private ResultBean mResultBean;
	private String param;

	private String seenum,sharenum;
	private String V_Title,V_Pic;


	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.fragment2);
		mAsyncHttpClient=new AsyncHttpClient();

		// 导航栏
		tv_title = getViewById(R.id.title_center_tv_title);
		tv_title.setText("视频");
		tv_title.setVisibility(View.VISIBLE);
		edit_search = getViewById(R.id.title_center_edit_search);
		edit_search.setEnabled(false);
		edit_search.setVisibility(View.GONE);
        ll_left = getViewById(R.id.title_ll_left);
        ll_left.setOnClickListener(this);
        ll_left.setVisibility(View.GONE);

        ll_right = getViewById(R.id.title_ll_right);
        ll_right.setVisibility(View.VISIBLE);
        ll_right.setOnClickListener(this);
        right_img = getViewById(R.id.title_right_img);
        right_img.setVisibility(View.VISIBLE);
        right_img.setOnClickListener(this);


		mScrollView = getViewById(R.id.mScrollview);
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
//				mScrollView.onRefreshComplete();
//				mWebView.reload();
			}
		});


		/**
		 * 推荐视频
		 * */
		videos_1=new ArrayList<>();
		ll_videomore = getViewById(R.id.fragment2_video_ll_videomore);
		ll_videomore.setOnClickListener(this);
		gv_video = getViewById(R.id.fragment2_video_gv_video);
		gv_video.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
		gv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				V_Title=mVideoList1.get(position).getV_title();
				V_Pic=mVideoList1.get(position).getV_pic();
				seenum = mVideoList1.get(position).getSee_num();
				sharenum = mVideoList1.get(position).getShare_num();
				videos_1.add(position);
//				if(("1".equals(MyApplication.getInstance().getFirst_enter()))&&!(mVideoList1.get(position).getId().equals(MyApplication.getInstance().getInfo_new()))) {
//					MyApplication.getInstance().setFirst_enter("0");
//				}
				isnosee("2",mVideoList1.get(position).getId(),"");//类型（1直播  2视频  3资讯  4干货 ）
//				mVideoList1.get(position).setState("1");
//				mF2RecommendedVideoAdapter1.notifyDataSetChanged();
//				initVideoDate(mVideoList1.get(position).getId());
//				Intent intent = new Intent();
//				intent.putExtra("path_video",Constant.BASE_URL_IMG + mVideoList1.get(position).getV_url());
////				intent.putExtra("wapurl",Constant.BASE_URL + mResultBean.getWapurl());
//				AppManager.getAppManager().startNextActivity(getActivity(), PlayVideoActivity.class, intent);
			}
		});

		/**
		 * 免费体验课
		 * */
		videos_2=new ArrayList<>();
		ll_livemore = getViewById(R.id.fragment2_live_ll_livemore);
		ll_livemore.setOnClickListener(this);
		gv_live = getViewById(R.id.fragment2_live_gv_live);
		gv_live.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
		gv_live.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				V_Title=mVideoList2.get(position).getV_title();
				V_Pic=mVideoList2.get(position).getV_pic();
				seenum = mVideoList2.get(position).getSee_num();
				sharenum = mVideoList2.get(position).getShare_num();
				videos_2.add(position);
//				if(("1".equals(MyApplication.getInstance().getFirst_enter()))&&!(mVideoList2.get(position).getId().equals(MyApplication.getInstance().getInfo_new()))) {
//					MyApplication.getInstance().setFirst_enter("0");
//				}
				isnosee("2",mVideoList2.get(position).getId(),"");//类型（1直播  2视频  3资讯  4干货 ）
//				mVideoList2.get(position).setState("1");
//				mF2RecommendedVideoAdapter2.notifyDataSetChanged();
//				initVideoDate(mVideoList2.get(position).getId());
//				Intent intent = new Intent();
//				intent.putExtra("path_video",Constant.BASE_URL_IMG + mVideoList2.get(position).getV_url());
////				intent.putExtra("wapurl",Constant.BASE_URL + mResultBean.getWapurl());
//				AppManager.getAppManager().startNextActivity(getActivity(), PlayVideoActivity.class, intent);
			}
		});


		/**
		 * 会员专区
		 * */
		videos_3=new ArrayList<>();
		ll_vipmore = getViewById(R.id.fragment2_vip_ll_vipmore);
		ll_vipmore.setOnClickListener(this);
		gv_vip = getViewById(R.id.fragment2_vip_gv_vip);
		gv_vip.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
		gv_vip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				V_Title=mVideoList3.get(position).getV_title();
				V_Pic=mVideoList3.get(position).getV_pic();
				seenum = mVideoList3.get(position).getSee_num();
				sharenum = mVideoList3.get(position).getShare_num();
				mVideoList3.get(position).setState("1");
				videos_3.add(position);
//				if(("1".equals(MyApplication.getInstance().getFirst_enter()))&&!(mVideoList3.get(position).getId().equals(MyApplication.getInstance().getInfo_new()))) {
//					MyApplication.getInstance().setFirst_enter("0");
//				}
				isnosee("2",mVideoList3.get(position).getId(),"");//类型（1直播  2视频  3资讯  4干货 ）
//				mVideoList3.get(position).setState("1");
//				mF2RecommendedVideoAdapter3.notifyDataSetChanged();
//				initVideoDate(mVideoList3.get(position).getId());
//				Intent intent = new Intent();
//				intent.putExtra("path_video",Constant.BASE_URL_IMG + mVideoList3.get(position).getV_url());
////				intent.putExtra("wapurl",Constant.BASE_URL + mResultBean.getWapurl());
//				AppManager.getAppManager().startNextActivity(getActivity(), PlayVideoActivity.class, intent);
			}
		});
//		initTestVideo();
//		initTestLive();
//		initTestVip();

		initDate();

	}

	private void initDate(){
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setMessage("加载中...");
		}
		if(!isRefresh){
			mProgressDialog.show();
		}
		RequestParams params = new RequestParams();
		params.put("uid",  MyApplication.getInstance().getUid());
		final String url = Constant.BASE_URL+Constant.URL_INDEX_VIDEOLIST;
		System.out.println("===========================视频url ======= " + url);
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
				System.out.println("===========================视频response ======= " + response.toString());
				if (!TextUtils.isEmpty(response.toString())) {
					mF2RecommendedVideoJson = new Gson().fromJson(response.toString(), F2RecommendedVideoJson.class);
					if (mF2RecommendedVideoJson.getResult().equals("1")) {
						if (!(mF2RecommendedVideoJson.getTjlist() == null || mF2RecommendedVideoJson.getTjlist().size() == 0 ||  "".equals(mF2RecommendedVideoJson.getTjlist()))) {
							mVideoList1 = mF2RecommendedVideoJson.getTjlist();
							mF2RecommendedVideoAdapter1 = new F2RecommendedVideoAdapter(getActivity(),mVideoList1);
							gv_video.setAdapter(mF2RecommendedVideoAdapter1);

//							for (int i = 0; i <videos_1.size() ; i++) {
//								mVideoList1.get(videos_1.get(i)).setState("1");
//							}
//							mF2RecommendedVideoAdapter1.notifyDataSetChanged();
						}
						if (!( mF2RecommendedVideoJson.getMflist() == null ||  mF2RecommendedVideoJson.getMflist().size() == 0 ||  "".equals( mF2RecommendedVideoJson.getMflist()))) {
							mVideoList2 = mF2RecommendedVideoJson.getMflist();
							mF2RecommendedVideoAdapter2 = new F2RecommendedVideoAdapter(getActivity(),mVideoList2);
							gv_live.setAdapter(mF2RecommendedVideoAdapter2);

//							for (int i = 0; i <videos_2.size() ; i++) {
//								mVideoList2.get(videos_2.get(i)).setState("1");
//							}
//							mF2RecommendedVideoAdapter2.notifyDataSetChanged();
						}
						if (!(mF2RecommendedVideoJson.getHylist() == null || mF2RecommendedVideoJson.getHylist().size() == 0 ||  "".equals(mF2RecommendedVideoJson.getHylist()))) {
							mVideoList3 = mF2RecommendedVideoJson.getHylist();
							mF2RecommendedVideoAdapter3 = new F2RecommendedVideoAdapter(getActivity(),mVideoList3);
							gv_vip.setAdapter(mF2RecommendedVideoAdapter3);

//							for (int i = 0; i <videos_3.size() ; i++) {
//								mVideoList3.get(videos_3.get(i)).setState("1");
//							}
//							mF2RecommendedVideoAdapter3.notifyDataSetChanged();
						}

//						ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), img_head, ImageLoaderOptions.get_face_Options());
						System.out.println("===========================1111 ======= " );

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

	private void initVideoDate(final String spid){
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setMessage("加载中...");
		}
		mProgressDialog.show();
		if(!isRefresh){
		}
		RequestParams params = new RequestParams();
		params.put("uid",  MyApplication.getInstance().getUid());
		params.put("spid",  spid);
		final String url = Constant.BASE_URL+Constant.URL_INDEX_SEL_SP;
		System.out.println("===========================用户观看视频 url ======= " + url);
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
				System.out.println("===========================用户观看视频 response ======= " + response.toString());
				if (!TextUtils.isEmpty(response.toString())) {
					mBannerVideoBean = new Gson().fromJson(response.toString(), BannerVideoBean.class);
					if (mBannerVideoBean.getResult().equals("1")) {
						Intent intent = new Intent();
						intent.putExtra("path_video",Constant.BASE_URL_IMG + mBannerVideoBean.getSpinfo().getV_url());
						intent.putExtra("isgz",mBannerVideoBean.getIsgz());
						intent.putExtra("sp_nr",mBannerVideoBean.getSpinfo().getSp_nr());
						intent.putExtra("spid",spid);
						intent.putExtra("seenum",seenum);
						intent.putExtra("sharenum",sharenum);
						intent.putExtra("name_video",V_Title);
						intent.putExtra("pic_video",V_Pic);
						AppManager.getAppManager().startNextActivity(getActivity(), PlayVideoActivity.class, intent);

					} else {
						Toasts.show(mBannerVideoBean.getMessage());
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

	/**
	 * 用户是否可以观看
	 * */
	private void isnosee(final String type,final String id,final String str_url) {
		System.out.println("===========================getTokenFromSever ===== " );
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setMessage("加载中...");
		}
		mProgressDialog.show();
		final RequestParams params = new RequestParams();
		params.put("uid", MyApplication.getInstance().getUid());
		params.put("type", type);//    类型（1直播  2视频  3资讯  4干货 ）	type
		params.put("id", id);//    id	id
		final String url = Constant.BASE_URL + Constant.URL_INDEX_ISNOSEE;
		System.out.println("===========================用户是否可以观看token url ===== " + url);
		System.out.println("===========================params ===== " + params.toString());
		mAsyncHttpClient.post(getActivity(), url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}

				System.out.println("===========================用户是否可以观看 response ===== " + response.toString());
				if (!TextUtils.isEmpty(response.toString())) {
					mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
					if (mResultBean.getResult().equals("1")) {
						//    类型（1直播  2视频  3资讯  4干货 ）	type
						if("1".equals(type)){
//							getTokenFromSever(id,str_url);
						}else if("2".equals(type)){
							initVideoDate(id);
						}else if("3".equals(type)){
							Intent intent = new Intent();
							intent.putExtra("url", Constant.BASE_URL + str_url);
							AppManager.getAppManager().startNextActivity(getActivity(), MyWebActivity.class, intent);
						}else if("4".equals(type)){
							Intent intent = new Intent();
							intent.putExtra("pdfUrl", Constant.BASE_URL_IMG + str_url);
							AppManager.getAppManager().startNextActivity(getActivity(), ReadPDFActivity.class, intent);
						}
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
				showTimeoutDialog(getActivity());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				System.out.println("===========================throwable ,responseString =  " + responseString.toString());
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				showTimeoutDialog(getActivity());
			}
		});
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

	}

	@Override
	protected void onUserVisible() {

//		if(mWebView!=null){
//			mWebView.loadUrl(url);
//			System.out.println("===========================分类 onUserVisible url = " + mWebView.getUrl());
//		}
	}

	@Override
	public void onResume() {
		super.onResume();
//		mWebView.loadUrl(url);
//		mWebView.goBack(); //goBack()表示返回WebView的上一页面
//		System.out.println("===========================分类 onResume url = " + mWebView.getUrl());
	}

	private void initTestVideo(){
		mVideoList1 = new ArrayList<F2RecommendedVideoBean>();
		for (int i = 0;i<2;i++){
			F2RecommendedVideoBean mRecommendedVideoBean = new F2RecommendedVideoBean();
//			mRecommendedVideoBean.setName("");
			mRecommendedVideoBean.setNum("2"+i+"万");
//			mRecommendedVideoBean.setContext("");
			mVideoList1.add(mRecommendedVideoBean);
		}

		mF2RecommendedVideoAdapter1 = new F2RecommendedVideoAdapter(getActivity(),mVideoList1);
		gv_video.setAdapter(mF2RecommendedVideoAdapter1);
	}

	private void initTestLive(){
		mVideoList2 = new ArrayList<F2RecommendedVideoBean>();
		for (int i = 0;i<4;i++){
			F2RecommendedVideoBean mRecommendedVideoBean = new F2RecommendedVideoBean();
//			mRecommendedVideoBean.setName("");
			mRecommendedVideoBean.setNum("2"+i+"万");
//			mRecommendedVideoBean.setContext("");
			mVideoList2.add(mRecommendedVideoBean);
		}

		mF2RecommendedVideoAdapter2 = new F2RecommendedVideoAdapter(getActivity(),mVideoList2);
		gv_live.setAdapter(mF2RecommendedVideoAdapter2);
	}

	private void initTestVip(){
		mVideoList3 = new ArrayList<F2RecommendedVideoBean>();
		for (int i = 0;i<4;i++){
			F2RecommendedVideoBean mRecommendedVideoBean = new F2RecommendedVideoBean();
//			mRecommendedVideoBean.setName("");
			mRecommendedVideoBean.setNum("2"+i+"万");
//			mRecommendedVideoBean.setContext("");
			mVideoList3.add(mRecommendedVideoBean);
		}

		mF2RecommendedVideoAdapter3 = new F2RecommendedVideoAdapter(getActivity(),mVideoList3);
		gv_vip.setAdapter(mF2RecommendedVideoAdapter3);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.title_ll_left://个人中心
//				AppManager.getAppManager().startNextActivity(getActivity(), SearchActivity.class);
				break;
            case R.id.title_ll_right:
				AppManager.getAppManager().startNextActivity(getActivity(), SearchActivity.class);
                break;
			case R.id.fragment2_video_ll_videomore:
				intent = new Intent();
				intent.putExtra("TYPE","1");
				AppManager.getAppManager().startNextActivity(getActivity(), MoreVideosActivity.class,intent);
				break;
			case R.id.fragment2_live_ll_livemore:
				intent = new Intent();
				intent.putExtra("TYPE","2");
				AppManager.getAppManager().startNextActivity(getActivity(), AllVideosActivity.class,intent);
				break;
			case R.id.fragment2_vip_ll_vipmore:
				intent = new Intent();
				intent.putExtra("TYPE","3");
				AppManager.getAppManager().startNextActivity(getActivity(), AllVideosActivity.class,intent);
				break;
		}
	}

}