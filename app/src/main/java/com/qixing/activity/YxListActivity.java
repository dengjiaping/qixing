package com.qixing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.activity.webview.MyWebActivity;
import com.qixing.adapter.F4RecommendedInfoAdapter;
import com.qixing.adapter.RecommendedYxListAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.InfoJson;
import com.qixing.bean.RecommendedInfoBean;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.MyListView;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;
import cz.msebera.android.httpclient.Header;

public class YxListActivity extends BaseActivity {

    private BGATitlebar mTitlebar;
    private PullToRefreshScrollView mScrollView;
    /**
     * 营销页buanner
     * */
    private BGABanner banner;

    /**
     * 七星营销
     * */
    private LinearLayout ll_infomore;
    private MyListView lv_yx;

    private List<RecommendedInfoBean> mInfoList;
    private RecommendedYxListAdapter mRecommendedYxListAdapter;

    private InfoJson mInfoJson;
    private List<InfoJson.Banner> banners;
    private List<InfoJson.QxInfo> list;
    private ResultBean mResultBean;
    private boolean isRefresh = false;
    private int p = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=getLayoutInflater().inflate(R.layout.activity_yx_list,null);
        setContentView(view);

        mAsyncHttpClient=new AsyncHttpClient();
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        initView();
        initDate();
    }

    private void initView() {
        mTitlebar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitlebar.setTitleText("七星营销");
        mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }
        });

        mScrollView = (PullToRefreshScrollView) findViewById(R.id.yx_list_scrollview);
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
        mScrollView.setMode(PullToRefreshBase.Mode.BOTH);
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
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                p = 1;
                isRefresh = true;
                initDate();
            }


            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                isRefresh = true;
                initMoreDatas();
            }

        });

        /**
         * 首页buanner
         * */
        banner = (BGABanner) findViewById(R.id.yx_list_bgabanner4);
        // 用Java代码方式设置切换动画
        banner.setTransitionEffect(TransitionEffect.Default);
        // banner.setPageTransformer(new RotatePageTransformer());
        // 设置page切换时长
        banner.setPageChangeDuration(1000);
//		List<View> views = new ArrayList<>();
//		views.add(getPageView(R.drawable.bgabanner_test));
//		views.add(getPageView(R.drawable.bgabanner_test));
//		views.add(getPageView(R.drawable.bgabanner_test));
//		banner.setData(views);
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
//				Intent intent = new Intent();
//				intent.putExtra("url", Constant.BASE_URL + Constant.URL_INDEX_SELZX + "/id/" + banners.get(position).getId());
//				intent.putExtra("title", list.get(position).getTitle());
//				AppManager.getAppManager().startNextActivity(getActivity(), MyWebActivity.class, intent);
                isnosee("5",banners.get(position).getId(), Constant.URL_INDEX_SELZX + "/id/" + banners.get(position).getId());//类型（1直播  2视频  3资讯  4干货 5营销）
            }
        });

        lv_yx= (MyListView) findViewById(R.id.item_yx_list_lv_info);
        lv_yx.setFocusable(false);
        lv_yx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                isnosee("5",list.get(position).getId(),Constant.URL_INDEX_SELZX+"/id/"+list.get(position).getId());//营销
            }
        });
    }

    private View getPageView(String urlImg) {
        ImageView imageView = new ImageView(this);
//        imageView.setImageResource(resid);
        if(!TextUtils.isEmpty(urlImg)){
            if(urlImg.startsWith("http")){
                ImageLoader.getInstance().displayImage(urlImg, imageView, ImageLoaderOptions.getOptions());
            }else {
                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + urlImg, imageView, ImageLoaderOptions.getOptions());
            }
        }
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

    }

    private void initDate(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
        }
        if(!isRefresh){
//			mProgressDialog.show();
        }

        RequestParams params = new RequestParams();
        params.put("uid",  MyApplication.getInstance().getUid());
        params.put("type","3");
        final String url = Constant.BASE_URL+Constant.URL_INDEX_ZXLIST+"/p/"+p;
        System.out.println("===========================营销列表 url ======= " + url);
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

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
                System.out.println("===========================营销列表 url ======= " + url);
                System.out.println("===========================个人中心response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mInfoJson = new Gson().fromJson(response.toString(), InfoJson.class);
                    if (mInfoJson.getResult().equals("1")) {
                        if (!(mInfoJson.getBanner() == null || mInfoJson.getBanner().size() == 0 ||  "".equals(mInfoJson.getBanner()))) {
                            banners = mInfoJson.getBanner();
                            List<View> views = new ArrayList<>();
                            for (int i = 0;i<banners.size(); i++){
                                views.add(getPageView(banners.get(i).getPic()));
                            }
                            banner.setData(views);
                        }
                        if (!(mInfoJson.getList() == null || mInfoJson.getList().size() == 0 ||  "".equals(mInfoJson.getList()))) {
                            list = mInfoJson.getList();
                            mRecommendedYxListAdapter = new RecommendedYxListAdapter(YxListActivity.this,list);
                            lv_yx.setAdapter(mRecommendedYxListAdapter);
                        }
                    } else {
//						Toasts.show(mShoppingMineBean.getMessage());
                    }
                } else {
                    showErrorDialog(YxListActivity.this);
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
                showTimeoutDialog(YxListActivity.this);
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
                showErrorDialog(YxListActivity.this);
            }
        });
    }

    private void initMoreDatas(){
        final RequestParams params = new RequestParams();
        params.put("uid",  MyApplication.getInstance().getUid());
        params.put("type","3");
        final String url = Constant.BASE_URL+Constant.URL_INDEX_ZXLIST+"/p/"+(p+1);//
        System.out.println("===========================营销列表 更多 url ==== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (isRefresh) {
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }

                System.out.println("===========================咨询列表 更多 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mInfoJson = new Gson().fromJson(response.toString(), InfoJson.class);
                    if (mInfoJson.getResult().equals("1")) {
                        if (mInfoJson.getList().size() == 0 || mInfoJson.getList() == null || "".equals(mInfoJson.getList())) {
                            Toasts.show("暂无更多数据");
                        } else {
//							mListView.setVisibility(View.VISIBLE);
//							tv_noolder.setVisibility(View.GONE);
                            list.addAll(mInfoJson.getList());
                            p = p+1;
                            System.out.println("===========================mJson.getList().size() = " + mInfoJson.getList().size());
                            mRecommendedYxListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mInfoJson.getMessage());
                    }
                } else {
                    showErrorDialog(YxListActivity.this);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (isRefresh) {
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }
                showTimeoutDialog(YxListActivity.this);
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
                showErrorDialog(YxListActivity.this);
            }
        });
    }

    /**
     * 用户是否可以观看
     * */
    private void isnosee(final String type,final String id,final String str_url) {
        System.out.println("===========================getTokenFromSever ===== " );
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("type", type);//    类型（1直播  2视频  3资讯  4干货 5营销）	type
        params.put("id", id);//    id	id
        final String url = Constant.BASE_URL + Constant.URL_INDEX_ISNOSEE;
        System.out.println("===========================用户是否可以观看token url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

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
                        //    类型（1直播  2视频  3资讯  4干货 5 营销）	type
                        if("1".equals(type)){
//							getTokenFromSever(id,str_url);
                        }else if("2".equals(type)){
//							initVideoDate(id);
                        }else if("3".equals(type)){
                            Intent intent = new Intent();
                            intent.putExtra("url", Constant.BASE_URL + str_url+"/uid/"+MyApplication.getInstance().getUid());
                            intent.putExtra("kind","2");
                            AppManager.getAppManager().startNextActivity(YxListActivity.this, MyWebActivity.class, intent);
                        }else if("4".equals(type)){
                            Intent intent = new Intent();
                            intent.putExtra("pdfUrl", Constant.BASE_URL_IMG + str_url);
                            AppManager.getAppManager().startNextActivity(YxListActivity.this, ReadPDFActivity.class, intent);
                        }else if("5".equals(type)){
                            Intent intent=new Intent();
                            intent.putExtra("url",Constant.BASE_URL+str_url+"/uid/"+MyApplication.getInstance().getUid());
                            intent.putExtra("kind","1");
//                            if(!(id.equals(MyApplication.getInstance().getFirst_enter()))){
//                                MyApplication.getInstance().setInfo_new("0");
//                            }
                            AppManager.getAppManager().startNextActivity(YxListActivity.this,MyWebActivity.class,intent);
                        }
                    } else {
                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
                    showErrorDialog(YxListActivity.this);
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(YxListActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(YxListActivity.this);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
