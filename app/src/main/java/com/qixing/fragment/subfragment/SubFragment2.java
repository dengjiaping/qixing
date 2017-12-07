package com.qixing.fragment.subfragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.View;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.activity.ReadPDFActivity;
import com.qixing.activity.ShopMineActivity;
import com.qixing.activity.webview.MyWebActivity;
import com.qixing.adapter.F4RecommendedInfoAdapter;
import com.qixing.adapter.InfoDriedAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.MyApplication;
import com.qixing.bean.F2RecommendedVideoJson;
import com.qixing.bean.InfoDriedJson;
import com.qixing.bean.RecommendedInfoBean;
import com.qixing.bean.ResultBean;
import com.qixing.fragment.BaseFragment;
import com.qixing.global.Constant;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.MyListView;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;
import cz.msebera.android.httpclient.Header;


public class SubFragment2 extends BaseFragment implements View.OnClickListener {
    TextView msg;

    private PullToRefreshScrollView mScrollView;

    /**
     * 资讯页buanner
     */
    private BGABanner banner;

    /**
     * 七星资讯
     */
    private LinearLayout ll_infomore;
    private MyListView lv_info;

    private LinearLayout dried_ll_1, dried_ll_2, dried_ll_3, dried_ll_4, dried_ll_5;
    private ImageView dried_img_1, dried_img_2, dried_img_3, dried_img_4, dried_img_5;
    private TextView dried_tv_1;
    private TextView dried_tv_2;
    private TextView dried_tv_3;
    private TextView dried_tv_4;
    private TextView dried_tv_5;

    private List<RecommendedInfoBean> mInfoList;
    private InfoDriedAdapter mInfoDriedAdapter;


    private InfoDriedJson mInfoDriedJson;
    private List<InfoDriedJson.Banner> banners;
    private List<InfoDriedJson.QxDried> list = new ArrayList<InfoDriedJson.QxDried>();

    private ResultBean mResultBean;
    private boolean isRefresh = false;
    private int p = 1;
    private String type = "";
    private String state = "";
    private String pdfTitle;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_sub2);
        mAsyncHttpClient = new AsyncHttpClient();


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
                type = "";
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
        banner = getViewById(R.id.fragment4_bgabanner4);
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

//				isnosee("4",banners.get(position).getId(),banners.get(position).getUrl());//类型（1直播  2视频  3资讯  4干货 ）
            }
        });

        /**
         * 七星干货
         * */
//		ll_infomore = getViewById(R.id.item_fragment1_info_ll_infomore);
//		ll_infomore.setOnClickListener(this);
        lv_info = getViewById(R.id.item_fragment4_info_lv_info);
        lv_info.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("url", Constant.BASE_URL_IMG + list.get(position).getG_url());
//				intent.putExtra("title", list.get(position).getTitle());
//				AppManager.getAppManager().startNextActivity(getActivity(), MyWebActivity.class, intent);
                pdfTitle = list.get(position).getG_title();
                isnosee("4", list.get(position).getId(), list.get(position).getG_url());//类型（1直播  2视频  3资讯  4干货 ）
//				list.get(position).setState("1");
//				mInfoDriedAdapter.notifyDataSetChanged();
            }
        });

        dried_ll_1 = (LinearLayout) getViewById(R.id.item_fragment1_dried_ll_1);
        dried_ll_2 = (LinearLayout) getViewById(R.id.item_fragment1_dried_ll_2);
        dried_ll_3 = (LinearLayout) getViewById(R.id.item_fragment1_dried_ll_3);
        dried_ll_4 = (LinearLayout) getViewById(R.id.item_fragment1_dried_ll_4);
        dried_ll_5 = (LinearLayout) getViewById(R.id.item_fragment1_dried_ll_5);
        dried_ll_1.setOnClickListener(this);
        dried_ll_2.setOnClickListener(this);
        dried_ll_3.setOnClickListener(this);
        dried_ll_4.setOnClickListener(this);
        dried_ll_5.setOnClickListener(this);

        dried_img_1 = (ImageView) getViewById(R.id.item_fragment1_dried_img_1);
        dried_img_2 = (ImageView) getViewById(R.id.item_fragment1_dried_img_2);
        dried_img_3 = (ImageView) getViewById(R.id.item_fragment1_dried_img_3);
        dried_img_4 = (ImageView) getViewById(R.id.item_fragment1_dried_img_4);
        dried_img_5 = (ImageView) getViewById(R.id.item_fragment1_dried_img_5);

        dried_tv_1 = (TextView) getViewById(R.id.item_fragment1_dried_tv_1);
        dried_tv_2 = (TextView) getViewById(R.id.item_fragment1_dried_tv_2);
        dried_tv_3 = (TextView) getViewById(R.id.item_fragment1_dried_tv_3);
        dried_tv_4 = (TextView) getViewById(R.id.item_fragment1_dried_tv_4);
        dried_tv_5 = (TextView) getViewById(R.id.item_fragment1_dried_tv_5);


//		initTestInfo();

        initDate();
    }

    private View getPageView(@DrawableRes int resid) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(resid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    private View getPageView(String urlImg) {
        ImageView imageView = new ImageView(getActivity());
//        imageView.setImageResource(resid);
        if (!TextUtils.isEmpty(urlImg)) {
            if (urlImg.startsWith("http")) {
                ImageLoader.getInstance().displayImage(urlImg, imageView, ImageLoaderOptions.getOptions());
            } else {
                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + urlImg, imageView, ImageLoaderOptions.getOptions());
            }
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    private void init(PullToRefreshScrollView mListView) {
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

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
//		if("1".equals(MyApplication.getInstance().getIsLogining())){
//			initDate();
//		}
    }

    @Override
    protected void onUserVisible() {

    }

    private void initDate() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("加载中...");
        }
        if (!isRefresh) {
//			mProgressDialog.show();
        }

        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("typeid", type);
        final String url = Constant.BASE_URL + Constant.URL_INDEX_GHLIST + "/p/" + p;
        System.out.println("===========================干货列表 url ======= " + url);
        mAsyncHttpClient.post(getContext(), url, params, new JsonHttpResponseHandler() {

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

                System.out.println("===========================干货列表 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mInfoDriedJson = new Gson().fromJson(response.toString(), InfoDriedJson.class);
                    if (mInfoDriedJson.getResult().equals("1")) {
                        if (!(mInfoDriedJson.getBanner() == null || mInfoDriedJson.getBanner().size() == 0 || "".equals(mInfoDriedJson.getBanner()))) {
                            banners = mInfoDriedJson.getBanner();
                            List<View> views = new ArrayList<>();
                            for (int i = 0; i < banners.size(); i++) {
                                views.add(getPageView(banners.get(i).getPic()));
                            }
                            banner.setData(views);
                        }
                        if (!(mInfoDriedJson.getList() == null || mInfoDriedJson.getList().size() == 0 || "".equals(mInfoDriedJson.getList()))) {
                            list.clear();
                            list.addAll(mInfoDriedJson.getList());
                            mInfoDriedAdapter = new InfoDriedAdapter(getActivity(), list);
                            lv_info.setAdapter(mInfoDriedAdapter);
                            mInfoDriedAdapter.notifyDataSetChanged();
                        }

                        dried_tv_5.setText(mInfoDriedJson.getAlltype().get(0).getTitle());
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mInfoDriedJson.getAlltype().get(0).getPic(), dried_img_5, ImageLoaderOptions.getOptions());
                        dried_tv_1.setText(mInfoDriedJson.getAlltype().get(1).getTitle());
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mInfoDriedJson.getAlltype().get(1).getPic(), dried_img_1, ImageLoaderOptions.getOptions());
                        dried_tv_2.setText(mInfoDriedJson.getAlltype().get(2).getTitle());
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mInfoDriedJson.getAlltype().get(2).getPic(), dried_img_2, ImageLoaderOptions.getOptions());
                        dried_tv_3.setText(mInfoDriedJson.getAlltype().get(3).getTitle());
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mInfoDriedJson.getAlltype().get(3).getPic(), dried_img_3, ImageLoaderOptions.getOptions());
                        dried_tv_4.setText(mInfoDriedJson.getAlltype().get(4).getTitle());
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mInfoDriedJson.getAlltype().get(4).getPic(), dried_img_4, ImageLoaderOptions.getOptions());

                        initDriedTextColor(dried_tv_1, dried_tv_2, dried_tv_3, dried_tv_4, dried_tv_5);
                        is_dried_1 = false;
                        is_dried_2 = false;
                        is_dried_3 = false;
                        is_dried_4 = false;
                        is_dried_5 = false;
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
                if (isRefresh) {
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
                if (isRefresh) {
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }
                showErrorDialog(getActivity());
            }
        });
    }

    private void initMoreDatas() {
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("typeid", type);
        final String url = Constant.BASE_URL + Constant.URL_INDEX_GHLIST + "/p/" + (p + 1);//
        System.out.println("===========================干货列表 更多 url ==== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(getActivity(), url, params, new JsonHttpResponseHandler() {

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

                System.out.println("===========================干货列表 更多 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mInfoDriedJson = new Gson().fromJson(response.toString(), InfoDriedJson.class);
                    if (mInfoDriedJson.getResult().equals("1")) {
                        if (mInfoDriedJson.getList().size() == 0 || mInfoDriedJson.getList() == null || "".equals(mInfoDriedJson.getList())) {
                            Toasts.show("暂无更多数据");
                        } else {
//							mListView.setVisibility(View.VISIBLE);
//							tv_noolder.setVisibility(View.GONE);
                            list.addAll(mInfoDriedJson.getList());
                            p = p + 1;
                            System.out.println("===========================mJson.getList().size() = " + mInfoDriedJson.getList().size());
                            mInfoDriedAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mInfoDriedJson.getMessage());
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
                if (isRefresh) {
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
                if (isRefresh) {
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }
                showErrorDialog(getActivity());
            }
        });
    }

    /**
     * 用户是否可以观看
     */
    private void isnosee(final String type, final String id, final String str_url) {
        System.out.println("===========================getTokenFromSever ===== ");
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
                        if ("1".equals(type)) {

                        } else if ("2".equals(type)) {

                        } else if ("3".equals(type)) {

                        } else if ("4".equals(type)) {
                            Intent intent = new Intent();
                            intent.putExtra("pdfUrl", Constant.BASE_URL_IMG + str_url);
                            intent.putExtra("spid", id);
                            intent.putExtra("pdfTitle", pdfTitle);
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

    private void initDriedDate() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("加载中...");
        }

        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("typeid", type);
        final String url = Constant.BASE_URL + Constant.URL_INDEX_GHLIST + "/p/" + p;
        System.out.println("===========================干货列表Dried url ======= " + url);
        System.out.println("===========================干货列表Dried params ======= " + params.toString());
        mAsyncHttpClient.post(getContext(), url, params, new JsonHttpResponseHandler() {

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

                System.out.println("===========================干货列表Dried response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mInfoDriedJson = new Gson().fromJson(response.toString(), InfoDriedJson.class);
                    if (mInfoDriedJson.getResult().equals("1")) {
                        if (!(mInfoDriedJson.getList() == null || mInfoDriedJson.getList().size() == 0 || "".equals(mInfoDriedJson.getList()))) {
                            list.clear();
                            list.addAll(mInfoDriedJson.getList());
                            if (mInfoDriedAdapter != null) {
                                mInfoDriedAdapter.notifyDataSetChanged();
                            } else {
                                mInfoDriedAdapter = new InfoDriedAdapter(getActivity(), list);
                                lv_info.setAdapter(mInfoDriedAdapter);
                                mInfoDriedAdapter.notifyDataSetChanged();
                            }
                        }

                        dried_tv_5.setText(mInfoDriedJson.getAlltype().get(0).getTitle());
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mInfoDriedJson.getAlltype().get(0).getPic(), dried_img_5, ImageLoaderOptions.getOptions());
                        dried_tv_1.setText(mInfoDriedJson.getAlltype().get(1).getTitle());
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mInfoDriedJson.getAlltype().get(1).getPic(), dried_img_1, ImageLoaderOptions.getOptions());
                        dried_tv_2.setText(mInfoDriedJson.getAlltype().get(2).getTitle());
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mInfoDriedJson.getAlltype().get(2).getPic(), dried_img_2, ImageLoaderOptions.getOptions());
                        dried_tv_3.setText(mInfoDriedJson.getAlltype().get(3).getTitle());
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mInfoDriedJson.getAlltype().get(3).getPic(), dried_img_3, ImageLoaderOptions.getOptions());
                        dried_tv_4.setText(mInfoDriedJson.getAlltype().get(4).getTitle());
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mInfoDriedJson.getAlltype().get(4).getPic(), dried_img_4, ImageLoaderOptions.getOptions());
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
                if (isRefresh) {
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
                if (isRefresh) {
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }
                showErrorDialog(getActivity());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

//		if("1".equals(MyApplication.getInstance().getIsLogining())){
//			//刷新
//			isRefresh = true;
//			initDate();
//		}else{
//
//		}
    }

    private void initTestInfo() {
        mInfoList = new ArrayList<RecommendedInfoBean>();
        for (int i = 0; i < 10; i++) {
            RecommendedInfoBean mRecommendedInfoBean = new RecommendedInfoBean();
//			mRecommendedInfoBean.setName("");
//			mRecommendedInfoBean.setNum("2"+i+"万");
//			mRecommendedInfoBean.setContext("");
            mRecommendedInfoBean.setTimes("2016-12-1" + i);
            if ((i + 1) % 5 != 0) {
                mRecommendedInfoBean.setType("1");
            } else {
                mRecommendedInfoBean.setType("2");
            }
            mInfoList.add(mRecommendedInfoBean);
        }

//		mF4RecommendedInfoAdapter = new F4RecommendedInfoAdapter(getActivity(),mInfoList);
//		lv_info.setAdapter(mF4RecommendedInfoAdapter);
    }

    private boolean is_dried_1 = false, is_dried_2 = false, is_dried_3 = false, is_dried_4 = false, is_dried_5 = false;

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title_ll_left://设置
//				intent=new Intent();
//				if("1".equals(MyApplication.getInstance().getIsLogining())){
//					intent.putExtra("tag", "SubFragment1");
//					AppManager.getAppManager().startNextActivity(getActivity(), ShopMineActivity.class,intent);
//				}else{
//					AppManager.getAppManager().startNextActivity(getActivity(), LoginActivity.class);
//				}
                break;
            case R.id.item_fragment1_dried_ll_5://
                initDriedTextColor(dried_tv_1, dried_tv_2, dried_tv_3, dried_tv_4, dried_tv_5);
                dried_tv_5.setTextColor(getResources().getColor(R.color.color_bottom_select));
                if (!is_dried_5) {
                    is_dried_5 = true;
                    is_dried_1 = false;
                    is_dried_2 = false;
                    is_dried_3 = false;
                    is_dried_4 = false;
                    list.clear();
                    type = mInfoDriedJson.getAlltype().get(0).getId();
                    initDriedDate();
                }
                break;
            case R.id.item_fragment1_dried_ll_1://
                initDriedTextColor(dried_tv_1, dried_tv_2, dried_tv_3, dried_tv_4, dried_tv_5);
                dried_tv_1.setTextColor(getResources().getColor(R.color.color_bottom_select));
                if (!is_dried_1) {
                    is_dried_1 = true;
                    is_dried_2 = false;
                    is_dried_3 = false;
                    is_dried_4 = false;
                    is_dried_5 = false;
                    list.clear();
                    type = mInfoDriedJson.getAlltype().get(1).getId();
                    initDriedDate();
                }
                break;
            case R.id.item_fragment1_dried_ll_2://
                initDriedTextColor(dried_tv_1, dried_tv_2, dried_tv_3, dried_tv_4, dried_tv_5);
                dried_tv_2.setTextColor(getResources().getColor(R.color.color_bottom_select));
                if (!is_dried_2) {
                    is_dried_2 = true;
                    is_dried_1 = false;
                    is_dried_3 = false;
                    is_dried_4 = false;
                    is_dried_5 = false;
                    list.clear();
                    type = mInfoDriedJson.getAlltype().get(2).getId();
                    initDriedDate();
                }
                break;
            case R.id.item_fragment1_dried_ll_3://
                initDriedTextColor(dried_tv_1, dried_tv_2, dried_tv_3, dried_tv_4, dried_tv_5);
                dried_tv_3.setTextColor(getResources().getColor(R.color.color_bottom_select));
                if (!is_dried_3) {
                    is_dried_3 = true;
                    is_dried_1 = false;
                    is_dried_2 = false;
                    is_dried_4 = false;
                    is_dried_5 = false;
                    list.clear();
                    type = mInfoDriedJson.getAlltype().get(3).getId();
                    initDriedDate();
                }
                break;
            case R.id.item_fragment1_dried_ll_4://
                initDriedTextColor(dried_tv_1, dried_tv_2, dried_tv_3, dried_tv_4, dried_tv_5);
                dried_tv_4.setTextColor(getResources().getColor(R.color.color_bottom_select));
                if (!is_dried_4) {
                    is_dried_4 = true;
                    is_dried_1 = false;
                    is_dried_2 = false;
                    is_dried_3 = false;
                    is_dried_5 = false;
                    list.clear();
                    type = mInfoDriedJson.getAlltype().get(4).getId();
                    initDriedDate();
                }
                break;
        }
    }

    private void initDriedTextColor(TextView dried_tv_1, TextView dried_tv_2, TextView dried_tv_3, TextView dried_tv_4, TextView dried_tv_5) {
        dried_tv_4.setTextColor(getResources().getColor(R.color.black));
        dried_tv_1.setTextColor(getResources().getColor(R.color.black));
        dried_tv_2.setTextColor(getResources().getColor(R.color.black));
        dried_tv_3.setTextColor(getResources().getColor(R.color.black));
        dried_tv_5.setTextColor(getResources().getColor(R.color.black));
    }


}