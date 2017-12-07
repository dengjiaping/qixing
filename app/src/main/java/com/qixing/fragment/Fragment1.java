package com.qixing.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.ValueCallback;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.j256.ormlite.stmt.query.In;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.MainActivity;
import com.qixing.R;
import com.qixing.activity.MoreVideosActivity;
import com.qixing.activity.PlayVideoActivity;
import com.qixing.activity.QXliveActivity;
import com.qixing.activity.ReadPDFActivity;
import com.qixing.activity.SearchActivity;
import com.qixing.activity.YxListActivity;
import com.qixing.activity.webview.MyWebActivity;
import com.qixing.adapter.F1QXliveAdapter;
import com.qixing.adapter.RecommendedDriedAdapter;
import com.qixing.adapter.RecommendedInfoAdapter;
import com.qixing.adapter.RecommendedMarketAdapter;
import com.qixing.adapter.RecommendedVideoAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.MyApplication;
import com.qixing.bean.BannerVideoBean;
import com.qixing.bean.IndexJson;
import com.qixing.bean.RecommendedDriedBean;
import com.qixing.bean.RecommendedInfoBean;
import com.qixing.bean.RecommendedVideoBean;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.qxlive.QXLiveSee2Activity;
import com.qixing.qxlive.QXLiveSeeActivity;
import com.qixing.qxlive.rongyun.LiveKit;
import com.qixing.qxlive.rongyun.fakeserver.FakeServer;
import com.qixing.utlis.DateUtils;
import com.qixing.utlis.SystemUtils;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.CircleImageView;
import com.qixing.view.MyListView;
import com.qixing.view.imagecut.ImageTools;
import com.qixing.widget.Toasts;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;
import cz.msebera.android.httpclient.Header;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;


public class Fragment1 extends BaseFragment implements View.OnClickListener {

    private PullToRefreshScrollView mScrollView;
    private boolean isRefresh = false;

    private TextView tv_title;
    private EditText edit_search;
    private LinearLayout ll_left, ll_right, ll_imgandtex;
    private TextView left_tv, right_tv;
    private ImageView left_img, right_img;

    /**
     * 首页buanner
     */
    private BGABanner banner;

    private BannerVideoBean mBannerVideoBean;

    /**
     * 推荐直播
     */
    private LinearLayout ll_livemore;
    private MyListView lv_live;

    private F1QXliveAdapter mF1QXliveAdapter;
    private int zb_position;


    private LinearLayout ll_live;
    private ImageView qxlive_img_icon;
    private Button qxlive_btn_attention;
    private CircleImageView qxlive_img_head;
    private TextView qxlive_tv_name;
    private TextView qxlive_tv_num;
    private TextView qxlive_tv_times;

    /**
     * 推荐视频
     */
    private LinearLayout ll_videomore;
    private MyListView lv_video;

    private List<RecommendedVideoBean> mVideoList;
    private RecommendedVideoAdapter mRecommendedVideoAdapter;

    /**
     * 七星资讯
     */
    private LinearLayout ll_infomore;
    private MyListView lv_info;

    private List<RecommendedInfoBean> mInfoList;
    private RecommendedInfoAdapter mRecommendedInfoAdapter;

    /**
     * 七星营销
     */
    private LinearLayout ll_marketmore;
    private MyListView lv_market;

    private RecommendedMarketAdapter mRecommendedMarketAdapter;

    /**
     * 七星干货
     */
    private LinearLayout ll_driedmore;
    private MyListView lv_dried;

    private List<RecommendedDriedBean> mDriedList;
    private RecommendedDriedAdapter mRecommendedDriedAdapter;

    private IndexJson mIndexJson;
    private List<IndexJson.Banner> banners;
    private List<IndexJson.Live> zb;
    private List<IndexJson.Video> video;
    private List<IndexJson.Zxlist> zxlist;
    private List<IndexJson.Yxlist> yxlist;
    private List<IndexJson.Gh> ghlist;

    private List<Integer> videos;
    private List<Integer> zxs;
    private List<Integer> yxs;
    private List<Integer> ghs1, ghs2, ghs3, ghs4;

    private String seenum, sharenum;
    private int video_position;
    private int pdf_position;

    private ResultBean mResultBean;

    ValueCallback<Uri> mUploadMessage;


    //选项卡
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private LinearLayout dried_ll_1, dried_ll_2, dried_ll_3, dried_ll_4, dried_ll_5;
    private ImageView dried_img_1, dried_img_2, dried_img_3, dried_img_4, dried_img_5;
    private TextView dried_tv_1;
    private TextView dried_tv_2;
    private TextView dried_tv_3;
    private TextView dried_tv_4;
    private TextView dried_tv_5;
    // 滚动条图片
    private ImageView scrollbar;
    // 滚动条初始偏移量
    private int offset = 0;
    // 当前页编号
    private int currIndex = 0;
    // 滚动条宽度
    private int bmpW;
    //一倍滚动量
    private int one;
    //起始位置
    private int start_scroll = 0;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment1);

        mAsyncHttpClient = new AsyncHttpClient();

        // 导航栏
        tv_title = getViewById(R.id.title_center_tv_title);
        tv_title.setText("首页");
        tv_title.setVisibility(View.VISIBLE);
        ll_left = getViewById(R.id.title_ll_left);
        ll_left.setOnClickListener(this);
        ll_left.setVisibility(View.GONE);
        edit_search = getViewById(R.id.title_center_edit_search);
        edit_search.setVisibility(View.GONE);
        edit_search.setOnClickListener(this);
        ll_right = getViewById(R.id.title_ll_right);
        ll_right.setOnClickListener(this);
        right_img = getViewById(R.id.title_right_img);
        right_img.setVisibility(View.VISIBLE);
        ll_right.setVisibility(View.VISIBLE);


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
            }
        });

        /**
         * 首页buanner
         * */
        banner = getViewById(R.id.fragment1_bgabanner1);
        // 用Java代码方式设置切换动画
        banner.setTransitionEffect(TransitionEffect.Default);
        // banner.setPageTransformer(new RotatePageTransformer());
        // 设置page切换时长
        banner.setPageChangeDuration(1000);
//		banner.setAutoPlayAble(true);
//		List<View> views = new ArrayList<>();
//        views.add(getPageView(R.drawable.bgabanner_test));
//        views.add(getPageView(R.drawable.bgabanner_test));
//        views.add(getPageView(R.drawable.bgabanner_test));
//		banner.setViews(views);
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {

//				initVideoDate(banners.get(position).getId());
                if ("1".equals(banners.get(position).getBanner_types())) {//banner_types（1资讯  2干货 3视频 4其他）
                    isnosee("3", banners.get(position).getId(), Constant.URL_INDEX_SELZX + "/id/" + banners.get(position).getId());//类型（1直播  2视频  3资讯  4干货 5营销）
                } else if ("2".equals(banners.get(position).getBanner_types())) {
                    //2干货
//					isnosee("4",banners.get(position).getId(),banners.get(position).getUrl());//类型（1直播  2视频  3资讯  4干货 5营销）
                    Intent intent = new Intent();
                    intent.putExtra("pdfUrl", Constant.BASE_URL_IMG + banners.get(position).getUrl());
                    AppManager.getAppManager().startNextActivity(getActivity(), ReadPDFActivity.class, intent);
                } else if ("3".equals(banners.get(position).getBanner_types())) {
                    //3视频
                    isnosee("2", banners.get(position).getId(), "");//类型（1直播  2视频  3资讯  4干货 5营销）
                } else if ("4".equals(banners.get(position).getBanner_types())) {
                    //4其他
//					isnosee("2",banners.get(position).getId(),"");//类型（1直播  2视频  3资讯  4干货 5营销）
                    Intent intent = new Intent();
                    intent.putExtra("url", banners.get(position).getUrl());
                    AppManager.getAppManager().startNextActivity(getActivity(), MyWebActivity.class, intent);
                }

            }
        });


        /**
         * 推荐直播
         * */
        ll_livemore = getViewById(R.id.layout_fragment1_qxlive_ll_livemore);
        ll_livemore.setOnClickListener(this);
        lv_live = getViewById(R.id.item_fragment1_qxlive_lv_live);
        lv_live.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        lv_live.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                zb_position = position;
                isnosee("1", zb.get(position).getId(), Constant.URL_LIVE_RTMPLIVE + zb.get(position).getId());//类型（1直播  2视频  3资讯  4干货 ）
            }
        });

        ll_live = getViewById(R.id.item_fragment1_qxlive_ll_live);
        ll_live.setOnClickListener(this);
        qxlive_img_icon = getViewById(R.id.item_fragment1_qxlive_img_icon);
        qxlive_btn_attention = getViewById(R.id.item_fragment1_qxlive_btn_attention);
        qxlive_img_head = getViewById(R.id.item_fragment1_qxlive_img_head);
        qxlive_tv_name = getViewById(R.id.item_fragment1_qxlive_tv_title);
        qxlive_tv_num = getViewById(R.id.item_fragment1_qxlive_tv_name);
        qxlive_tv_times = getViewById(R.id.item_fragment1_qxlive_tv_times);

        /**
         * 推荐视频
         * */
        videos = new ArrayList<>();
        ll_videomore = getViewById(R.id.item_fragment1_video_ll_videomore);
        ll_videomore.setOnClickListener(this);
        lv_video = getViewById(R.id.item_fragment1_video_lv_video);
        lv_video.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        lv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                seenum = video.get(position).getSee_num();
                sharenum = video.get(position).getShare_num();
                video_position = position;
                videos.add(video_position);
//				MyApplication.getInstance().setRead_num(Integer.parseInt(video.get(position).getSee_num()));
//				if(("1".equals(MyApplication.getInstance().getFirst_enter()))&&!(list_dried.get(position).getId().equals(MyApplication.getInstance().getInfo_new()))) {
//					MyApplication.getInstance().setFirst_enter("0");
//				}
                isnosee("2", video.get(position).getId(), "");//类型（1直播  2视频  3资讯  4干货 ）

//				Intent intent = new Intent();
//				intent.putExtra("path_video",Constant.BASE_URL_IMG + video.get(position).getV_url());
////				intent.putExtra("wapurl",Constant.BASE_URL + mResultBean.getWapurl());
//				AppManager.getAppManager().startNextActivity(getActivity(), PlayVideoActivity.class, intent);
            }
        });
//		initTestVideo();

        /**
         * 七星资讯
         * */
        zxs = new ArrayList<>();
        ll_infomore = getViewById(R.id.item_fragment1_info_ll_infomore);
        ll_infomore.setOnClickListener(this);
        lv_info = getViewById(R.id.item_fragment1_info_lv_info);
        lv_info.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Intent intent = new Intent();
//				intent.putExtra("url", Constant.BASE_URL + Constant.URL_INDEX_SELZX + "/id/" + zxlist.get(position).getId());
//				AppManager.getAppManager().startNextActivity(getActivity(), MyWebActivity.class, intent);
                zxs.add(position);
                if (("1".equals(MyApplication.getInstance().getFirst_enter())) && !(list_dried.get(position).getId().equals(MyApplication.getInstance().getInfo_new()))) {
                    MyApplication.getInstance().setFirst_enter("0");
                }
                isnosee("3", zxlist.get(position).getId(), Constant.URL_INDEX_SELZX + "/id/" + zxlist.get(position).getId());//类型（1直播  2视频  3资讯  4干货 ）
//				zxlist.get(position).setState("1");
//				mRecommendedInfoAdapter.notifyDataSetChanged();
            }
        });
//		initTestInfo();

        /**
         *七星营销
         */
        yxs = new ArrayList<>();
        ll_marketmore = getViewById(R.id.layout_fragment1_marketing_ll_marketmore);
        ll_marketmore.setOnClickListener(this);
        lv_market = getViewById(R.id.item_fragment1_marketing_lv_market);
        lv_market.setFocusable(false);
        lv_market.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                yxs.add(position);
//				if(("1".equals(MyApplication.getInstance().getFirst_enter()))&&!(list_dried.get(position).getId().equals(MyApplication.getInstance().getInfo_new()))) {
//					MyApplication.getInstance().setFirst_enter("0");
//				}
                isnosee("5", yxlist.get(position).getId(), Constant.URL_INDEX_SELZX + "/id/" + yxlist.get(position).getId());//营销/咨讯
//				yxlist.get(position).setState("1");
//				mRecommendedMarketAdapter.notifyDataSetChanged();
            }
        });

        /**
         * 七星干货
         * */
        ghs1 = new ArrayList<>();
        ghs2 = new ArrayList<>();
        ghs3 = new ArrayList<>();
        ghs4 = new ArrayList<>();
        ll_driedmore = getViewById(R.id.item_fragment1_dried_ll_driedmore);
        ll_driedmore.setOnClickListener(this);
        lv_dried = getViewById(R.id.item_fragment1_dried_lv_dried);
        lv_dried.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        lv_dried.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Intent intent = new Intent();
//				intent.putExtra("url", Constant.BASE_URL_IMG + ghlist.get(position).getG_url());
//				intent.putExtra("title", list.get(position).getTitle());
//				AppManager.getAppManager().startNextActivity(getActivity(), MyWebActivity.class, intent);
                pdf_position = position;
                switch (zt) {
                    case "1":
                        ghs1.add(pdf_position);
                        break;
                    case "2":
                        ghs2.add(pdf_position);
                        break;
                    case "3":
                        ghs3.add(pdf_position);
                        break;
                    case "4":
                        ghs4.add(pdf_position);
                        break;
                }
//				if(("1".equals(MyApplication.getInstance().getFirst_enter()))&&!(list_dried.get(position).getId().equals(MyApplication.getInstance().getInfo_new()))) {
//					MyApplication.getInstance().setFirst_enter("0");
//				}
                isnosee("4", list_dried.get(position).getId(), list_dried.get(position).getG_url());//类型（1直播  2视频  3资讯  4干货 ）
//				list_dried.get(position).setState("1");
//				list_dried.get(position).setIsnew("1");
//				mRecommendedDriedAdapter.notifyDataSetChanged();
            }
        });
//		initDriedInfo();

        initViewPage();
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
        //
        if (!TextUtils.isEmpty(urlImg)) {
            if (urlImg.startsWith("http")) {
                ImageLoader.getInstance().displayImage(urlImg, imageView, ImageLoaderOptions.getBannerOptions());
            } else {
                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + urlImg, imageView, ImageLoaderOptions.getBannerOptions());
            }
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    private void initViewPage() {
        viewPager = (ViewPager) getViewById(R.id.viewPager_fragment1);
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view1 = inflater.inflate(R.layout.layout_fragment1_recommended_dried1, null);
        View view2 = inflater.inflate(R.layout.layout_fragment1_recommended_dried2, null);
        View view3 = inflater.inflate(R.layout.layout_fragment1_recommended_dried3, null);
        View view4 = inflater.inflate(R.layout.layout_fragment1_recommended_dried4, null);
        View view5 = inflater.inflate(R.layout.layout_fragment1_recommended_dried5, null);
        initPullToRefresh(view5, view1, view2, view3, view4);
//		private LinearLayout dried_ll_1,dried_ll_2,dried_ll_3,dried_ll_4;
//		private ImageView dried_img_1,dried_img_2,dried_img_3,dried_img_4;
        dried_ll_1 = (LinearLayout) getViewById(R.id.item_fragment1_dried_ll_1);
        dried_ll_2 = (LinearLayout) getViewById(R.id.item_fragment1_dried_ll_2);
        dried_ll_3 = (LinearLayout) getViewById(R.id.item_fragment1_dried_ll_3);
        dried_ll_4 = (LinearLayout) getViewById(R.id.item_fragment1_dried_ll_4);
        dried_ll_5 = (LinearLayout) getViewById(R.id.item_fragment1_dried_ll_5);

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
        scrollbar = (ImageView) getViewById(R.id.scrollbar);
        scrollbar.setVisibility(View.GONE);
        dried_ll_1.setOnClickListener(this);
        dried_ll_2.setOnClickListener(this);
        dried_ll_3.setOnClickListener(this);
        dried_ll_4.setOnClickListener(this);
        dried_ll_5.setOnClickListener(this);
        pageview = new ArrayList<View>();
        //添加想要切换的界面
        pageview.add(view5);
        pageview.add(view1);
        pageview.add(view2);
        pageview.add(view3);
        pageview.add(view4);
        //数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageview.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1) {
                ((ViewPager) arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }
        };
        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        //设置viewPager的初始界面为第一个界面
        viewPager.setCurrentItem(0);
        //添加切换界面的监听器
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        // 获取滚动条的宽度
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.img_line_red).getWidth();
        //为了获取屏幕宽度，新建一个DisplayMetrics对象
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //将当前窗口的一些信息放在DisplayMetrics类中
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //得到屏幕的宽度
        int screenW = displayMetrics.widthPixels;
        //设置滚动条imgview的宽度
        ViewGroup.LayoutParams para = scrollbar.getLayoutParams();
        para.width = screenW / 4;
        scrollbar.setLayoutParams(para);

        //计算出滚动条初始的偏移量
//        offset = (screenW / 2 - bmpW) / 2;
        offset = 0;
        //计算出切换一个界面时，滚动条的位移量
//        one = offset * 2 + bmpW;
        one = screenW / 4;

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        //将滚动条的初始位置设置成与左边界间隔一个offset
        scrollbar.setImageMatrix(matrix);
    }

    private boolean is_updated = false, is_untreated = true, is_confirm = false, is_finish = false, is_cancel = false;

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0://待处理
                    /**
                     * TranslateAnimation的四个属性分别为
                     * 参数fromXDelta：位置变化的起始点X坐标。
                     * 参数toXDelta：位置变化的结束点X坐标。
                     * 参数fromYDelta：位置变化的起始点Y坐标。
                     * 参数toYDelta：位置变化的结束点Y坐标。
                     *
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/
                    animation = new TranslateAnimation(start_scroll, 0, 0, 0);
                    start_scroll = 0;
//					dried_tv_1 dried_tv_2 dried_tv_3 dried_tv_4 
                    dried_tv_5.setTextColor(getResources().getColor(R.color.color_bottom_select));
                    dried_tv_1.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_2.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_3.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_4.setTextColor(getResources().getColor(R.color.black));
                    if (!is_updated) {
                        is_updated = true;
                        is_untreated = false;
                        is_confirm = false;
                        is_finish = false;
                        is_cancel = false;
                        list_dried.clear();
                        list_dried.addAll(ghlist.get(0).getList());
                        System.out.println("=========================== 首页干货1 ======= " + ghlist.get(0).getList().size());
                        mRecommendedDriedAdapter.notifyDataSetChanged();
                        zt = "0";

//						initDatas(zt);
                    }
                    break;
                case 1://待处理
                    animation = new TranslateAnimation(start_scroll, one, 0, 0);
                    start_scroll = one;
                    dried_tv_1.setTextColor(getResources().getColor(R.color.color_bottom_select));
                    dried_tv_2.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_3.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_4.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_5.setTextColor(getResources().getColor(R.color.black));

                    if (!is_untreated) {
                        is_untreated = true;
                        is_updated = false;
                        is_confirm = false;
                        is_finish = false;
                        is_cancel = false;
                        list_dried.clear();
                        list_dried.addAll(ghlist.get(1).getList());
                        System.out.println("=========================== 首页干货2 ======= " + list_dried.size());
                        mRecommendedDriedAdapter.notifyDataSetChanged();
                        zt = "1";

//						for (int i = 0; i <ghs2.size() ; i++) {
//							list_dried.get(ghs2.get(i)).setState("1");
//						}
//						mRecommendedDriedAdapter.notifyDataSetChanged();
//						initDatas(zt);
                    }
                    break;
                case 2://已确认
                    animation = new TranslateAnimation(start_scroll, one + one, 0, 0);
                    start_scroll = one + one;
                    dried_tv_2.setTextColor(getResources().getColor(R.color.color_bottom_select));
                    dried_tv_1.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_3.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_4.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_5.setTextColor(getResources().getColor(R.color.black));
                    if (!is_confirm) {
                        is_confirm = true;
                        is_finish = false;
                        is_updated = false;
                        is_untreated = false;
                        is_cancel = false;
                        list_dried.clear();
                        list_dried.addAll(ghlist.get(2).getList());
                        System.out.println("=========================== 首页干货3 ======= " + list_dried.size());
                        mRecommendedDriedAdapter.notifyDataSetChanged();
                        zt = "2";
//						initDatas(zt);

//						for (int i = 0; i <ghs3.size() ; i++) {
//							list_dried.get(ghs3.get(i)).setState("1");
//						}
//						mRecommendedDriedAdapter.notifyDataSetChanged();
                    }
                    break;
                case 3://已完成
                    animation = new TranslateAnimation(start_scroll, one + one + one, 0, 0);
                    start_scroll = one + one + one;
                    dried_tv_3.setTextColor(getResources().getColor(R.color.color_bottom_select));
                    dried_tv_1.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_2.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_4.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_5.setTextColor(getResources().getColor(R.color.black));
                    if (!is_finish) {
                        is_finish = true;
                        is_updated = false;
                        is_cancel = false;
                        is_confirm = false;
                        is_untreated = false;
                        is_cancel = false;
                        list_dried.clear();
                        list_dried.addAll(ghlist.get(3).getList());
                        System.out.println("=========================== 首页干货4 ======= " + list_dried.size());
                        mRecommendedDriedAdapter.notifyDataSetChanged();
                        zt = "3";
//						initDatas(zt);

//						for (int i = 0; i <ghs4.size() ; i++) {
//							list_dried.get(ghs4.get(i)).setState("1");
//						}
//						mRecommendedDriedAdapter.notifyDataSetChanged();
                    }
                    break;
                case 4://取消
                    animation = new TranslateAnimation(start_scroll, one + one + one + one, 0, 0);
                    start_scroll = one + one + one + one;
                    dried_tv_4.setTextColor(getResources().getColor(R.color.color_bottom_select));
                    dried_tv_1.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_2.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_3.setTextColor(getResources().getColor(R.color.black));
                    dried_tv_5.setTextColor(getResources().getColor(R.color.black));
                    if (!is_cancel) {
                        is_cancel = true;
                        is_finish = false;
                        is_confirm = false;
                        is_updated = false;
                        is_untreated = false;
                        is_cancel = false;
                        list_dried.clear();
                        list_dried.addAll(ghlist.get(4).getList());
                        System.out.println("=========================== 首页干货4 ======= " + list_dried.size());
                        mRecommendedDriedAdapter.notifyDataSetChanged();
                        zt = "4";
//						initDatas(zt);

//						for (int i = 0; i <ghs4.size() ; i++) {
//							list_dried.get(ghs4.get(i)).setState("1");
//						}
//						mRecommendedDriedAdapter.notifyDataSetChanged();
                    }
                    break;
            }
            //arg0为切换到的页的编码
            currIndex = arg0;
            // 将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            //滚动条开始动画
            scrollbar.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private TextView tv_noolder_untreated;
    private MyListView lv_dried1;
    private RecommendedDriedAdapter mDriedAdapter_dried1;

    private TextView tv_noolder_confirm;
    private MyListView lv_dried2;
    private RecommendedDriedAdapter mDriedAdapter_dried2;

    private TextView tv_noolder_finish;
    private MyListView lv_dried3;
    private RecommendedDriedAdapter mDriedAdapter_dried3;

    private TextView tv_noolder_cancel;
    private MyListView lv_dried4;
    private RecommendedDriedAdapter mDriedAdapter_dried4;

    private TextView tv_noolder_updated;
    private MyListView lv_dried5;
    private RecommendedDriedAdapter mDriedAdapter_dried5;

    private List<IndexJson.Ghlist> list_dried = new ArrayList<IndexJson.Ghlist>();

    private String zt = "1";//zt:(1: 待处理的;2: 已确认的;3: 成功;4: 已取消)
    private int type = 0;

//	private boolean isRefresh = false;


    /**
     * 初始化
     */
    private void initPullToRefresh(View view5, View view, View view2, View view3, View view4) {
        lv_dried5 = (MyListView) view5.findViewById(R.id.item_fragment1_dried_lv_dried5);
        lv_dried5.setFocusable(false);
        lv_dried5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                isnosee("4", ghlist.get(position).getId(), ghlist.get(0).getList().get(position).getG_url());//类型（1直播  2视频  3资讯  4干货 ）
            }
        });

        lv_dried1 = (MyListView) view.findViewById(R.id.item_fragment1_dried_lv_dried1);
        lv_dried1.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        lv_dried1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toasts.show("点击了条目"+position);
                isnosee("4", ghlist.get(position).getId(), ghlist.get(1).getList().get(position).getG_url());//类型（1直播  2视频  3资讯  4干货 ）
            }
        });
//		mDriedAdapter_dried1 = new RecommendedDriedAdapter(getActivity(),ghlist.get(0).getList());
//		lv_dried1.setAdapter(mDriedAdapter_dried1);

        lv_dried2 = (MyListView) view2.findViewById(R.id.item_fragment1_dried_lv_dried2);
        lv_dried2.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        lv_dried2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toasts.show("点击了条目"+position);
                isnosee("4", ghlist.get(position).getId(), ghlist.get(2).getList().get(position).getG_url());//类型（1直播  2视频  3资讯  4干货 ）
            }
        });
//		mDriedAdapter_dried2 = new RecommendedDriedAdapter(getActivity(),ghlist.get(1).getList());
//		lv_dried2.setAdapter(mDriedAdapter_dried2);

        lv_dried3 = (MyListView) view3.findViewById(R.id.item_fragment1_dried_lv_dried3);
        lv_dried3.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        lv_dried3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toasts.show("点击了条目"+position);
                isnosee("4", ghlist.get(position).getId(), ghlist.get(3).getList().get(position).getG_url());//类型（1直播  2视频  3资讯  4干货 ）
            }
        });
//		mDriedAdapter_dried3 = new RecommendedDriedAdapter(getActivity(),ghlist.get(2).getList());
//		lv_dried3.setAdapter(mDriedAdapter_dried3);

        lv_dried4 = (MyListView) view4.findViewById(R.id.item_fragment1_dried_lv_dried4);
        lv_dried4.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        lv_dried4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toasts.show("点击了条目"+position);
                isnosee("4", ghlist.get(position).getId(), ghlist.get(4).getList().get(position).getG_url());//类型（1直播  2视频  3资讯  4干货 ）
            }
        });
//		mDriedAdapter_dried4 = new RecommendedDriedAdapter(getActivity(),ghlist.get(3).getList());
//		lv_dried4.setAdapter(mDriedAdapter_dried4);
    }

    private void initDate() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("加载中...");
        }
        if (!isRefresh) {
            mProgressDialog.show();
        }
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        final String url = Constant.BASE_URL + Constant.URL_INDEX_INDEX;
        System.out.println("===========================首页url ======= " + url);
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
                System.out.println("===========================首页response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mIndexJson = new Gson().fromJson(response.toString(), IndexJson.class);
                    if (mIndexJson.getResult().equals("1")) {
                        //		tv_name,tv_commodity,tv_shop,tv_brand;
                        //		tv_surplus_money,tv_jinbi;
                        if (mIndexJson.getNewpic() != null) {
                            System.out.println("=============================首页 newpic============" + mIndexJson.getNewpic());
                            MyApplication.getInstance().setNew_index(mIndexJson.getNewpic());

                            try {
                                downloadFile(Constant.URL_IMG + MyApplication.getInstance().getNew_index());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //首页banner
                        if (!(mIndexJson.getBanner() == null || mIndexJson.getBanner().size() == 0 || "".equals(mIndexJson.getBanner()))) {
                            banners = mIndexJson.getBanner();
                            List<View> views = new ArrayList<>();
                            for (int i = 0; i < banners.size(); i++) {
                                views.add(getPageView(banners.get(i).getPic()));
                            }
                            banner.setData(views);
                        }
                        //推荐直播
                        if (!(mIndexJson.getZb() == null || mIndexJson.getZb().size() == 0 || "".equals(mIndexJson.getZb()))) {
                            zb = mIndexJson.getZb();
                            mF1QXliveAdapter = new F1QXliveAdapter(getActivity(), zb);
                            lv_live.setAdapter(mF1QXliveAdapter);

//							zb = mIndexJson.getZb();
//							if(!TextUtils.isEmpty(zb.get(0).getZb_pic())){
//								if(zb.get(0).getZb_pic().startsWith("http")){
//									ImageLoader.getInstance().displayImage(zb.get(0).getZb_pic(),qxlive_img_icon, ImageLoaderOptions.getOptions());
//								}else {
//									ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+zb.get(0).getZb_pic(),qxlive_img_icon, ImageLoaderOptions.getOptions());
//								}
//							}
//							System.out.println("===========================userhead ======= " + Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head());
                        }
                        //推荐视频
                        if (!(mIndexJson.getVideo() == null || mIndexJson.getVideo().size() == 0 || "".equals(mIndexJson.getVideo()))) {
                            video = mIndexJson.getVideo();
                            mRecommendedVideoAdapter = new RecommendedVideoAdapter(getActivity(), video);
                            lv_video.setAdapter(mRecommendedVideoAdapter);
                            mRecommendedVideoAdapter.notifyDataSetChanged();
                        }
                        //推荐资讯
                        if (!(mIndexJson.getZxlist() == null || mIndexJson.getZxlist().size() == 0 || "".equals(mIndexJson.getZxlist()))) {
                            zxlist = mIndexJson.getZxlist();
                            mRecommendedInfoAdapter = new RecommendedInfoAdapter(getActivity(), zxlist);
                            lv_info.setAdapter(mRecommendedInfoAdapter);
                            mRecommendedInfoAdapter.notifyDataSetChanged();

                        }
                        //推荐营销
                        if (!(mIndexJson.getMarketList() == null || mIndexJson.getMarketList().size() == 0 || "".equals(mIndexJson.getMarketList()))) {
                            yxlist = mIndexJson.getMarketList();
                            mRecommendedMarketAdapter = new RecommendedMarketAdapter(getActivity(), yxlist);
                            lv_market.setAdapter(mRecommendedMarketAdapter);
                            mRecommendedMarketAdapter.notifyDataSetChanged();

                        }
                        //推荐干货
                        if (!(mIndexJson.getGhlist() == null || mIndexJson.getGhlist().size() == 0 || "".equals(mIndexJson.getGhlist()))) {
                            ghlist = mIndexJson.getGhlist();
                            list_dried.clear();
                            list_dried.addAll(ghlist.get(0).getList());
//							System.out.println("=========================== 首页干货1 ======= " +ghlist.get(0).getList().size());
                            mRecommendedDriedAdapter = new RecommendedDriedAdapter(getActivity(), list_dried);
                            lv_dried.setAdapter(mRecommendedDriedAdapter);
                            mRecommendedDriedAdapter.notifyDataSetChanged();
                            lv_dried.setVisibility(View.VISIBLE);
                            is_updated = true;
                            is_untreated = false;
                            is_confirm = false;
                            is_finish = false;
                            is_cancel = false;
                            viewPager.setCurrentItem(0);

                            dried_tv_5.setText(ghlist.get(0).getTitle());
                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + ghlist.get(0).getPic(), dried_img_5, ImageLoaderOptions.getOptions());
                            dried_tv_1.setText(ghlist.get(1).getTitle());
                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + ghlist.get(1).getPic(), dried_img_1, ImageLoaderOptions.getOptions());
                            dried_tv_2.setText(ghlist.get(2).getTitle());
                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + ghlist.get(2).getPic(), dried_img_2, ImageLoaderOptions.getOptions());
                            dried_tv_3.setText(ghlist.get(3).getTitle());
                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + ghlist.get(3).getPic(), dried_img_3, ImageLoaderOptions.getOptions());
                            dried_tv_4.setText(ghlist.get(4).getTitle());
                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + ghlist.get(4).getPic(), dried_img_4, ImageLoaderOptions.getOptions());


//							for (int i = 0; i <ghs1.size() ; i++) {
//								list_dried.get(ghs1.get(i)).setState("1");
//							}
//							mRecommendedDriedAdapter.notifyDataSetChanged();
//							mDriedAdapter_dried1 = new RecommendedDriedAdapter(getActivity(),ghlist.get(0).getList());
//							System.out.println("=========================== 首页干货1 ======= " +ghlist.get(0).getList().size());
//							lv_dried1.setAdapter(mDriedAdapter_dried1);
//							lv_dried1.setVisibility(View.VISIBLE);
//							mDriedAdapter_dried2 = new RecommendedDriedAdapter(getActivity(),ghlist.get(1).getList());
//							System.out.println("=========================== 首页干货2 ======= " +ghlist.get(1).getList().size());
//							lv_dried2.setAdapter(mDriedAdapter_dried2);
//							mDriedAdapter_dried3 = new RecommendedDriedAdapter(getActivity(),ghlist.get(2).getList());
//							System.out.println("=========================== 首页干货3 ======= " +ghlist.get(2).getList().size());
//							lv_dried3.setAdapter(mDriedAdapter_dried3);
//							mDriedAdapter_dried4 = new RecommendedDriedAdapter(getActivity(),ghlist.get(3).getList());
//							System.out.println("=========================== 首页干货4 ======= " +ghlist.get(3).getList().size());
//							lv_dried4.setAdapter(mDriedAdapter_dried4);
                        }

//						System.out.println("===========================1111 ======= " );

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

    private void initVideoDate(final String spid) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();
        if (!isRefresh) {
        }
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("spid", spid);
        final String url = Constant.BASE_URL + Constant.URL_INDEX_SEL_SP;
        System.out.println("===========================用户观看视频 url ======= " + url);
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
                System.out.println("===========================用户观看视频 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mBannerVideoBean = new Gson().fromJson(response.toString(), BannerVideoBean.class);
                    if (mBannerVideoBean.getResult().equals("1")) {
                        Intent intent = new Intent();
                        intent.putExtra("path_video", Constant.BASE_URL_IMG + mBannerVideoBean.getSpinfo().getV_url());
                        intent.putExtra("isgz", mBannerVideoBean.getIsgz());
                        intent.putExtra("sp_nr", mBannerVideoBean.getSpinfo().getSp_nr());
                        intent.putExtra("spid", spid);
                        intent.putExtra("seenum", seenum);
                        intent.putExtra("sharenum", sharenum);
                        intent.putExtra("name_video", video.get(video_position).getV_title());
                        intent.putExtra("pic_video", video.get(video_position).getV_pic());
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

    private void getTokenFromSever(final String zbid, final String rtmp_url) {
        final UserInfo user = FakeServer.getUserInfo();
        System.out.println("===========================getTokenFromSever ===== ");
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_BYUSERGETINFO;
        System.out.println("===========================直播 融云获取token url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(getActivity(), url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================直播 融云获取token response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
//                        LiveKit.setOnReceiveMessageListener(HDLiveShowActivity.onReceiveMessageListener);
                        LiveKit.connect(mResultBean.getToken(), new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                System.out.println("===========================直播 融云获取token connect onTokenIncorrect ===== Token 跟 AppKey 不匹配");
                                // 检查appKey 与token是否匹配.
                                Toasts.show("网络问题，连接失败，请稍后再试");
                            }

                            @Override
                            public void onSuccess(String userId) {
                                System.out.println("===========================直播 融云获取token connect onSuccess ===== ");
                                seeLive(zbid, rtmp_url);
                                Toasts.show("进入直播间");
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                System.out.println("===========================直播 融云获取token connect onError = " + errorCode);
                                // 根据errorCode 检查原因.
                                Toasts.show("网络问题，连接失败，请稍后再试");
                            }
                        });
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
                showErrorDialog(getActivity());
            }
        });
    }

    /**
     * 用户观看直播
     */
    private void seeLive(final String zbid, final String rtmp_url) {
        System.out.println("===========================getTokenFromSever ===== ");
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("zbid", zbid);
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_SEL_ZB;
        System.out.println("===========================用户观看直播token url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(getActivity(), url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================用户观看直播 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        LiveKit.setCurrentUser(FakeServer.getUserInfo());
                        Intent intent = new Intent();
                        if (Integer.parseInt(zb.get(zb_position).getApp_types()) == 1) {// 1手机
                            intent.setClass(getActivity(), QXLiveSeeActivity.class);
                            intent.putExtra(QXLiveSeeActivity.LIVE_URL, rtmp_url);
                            intent.putExtra(QXLiveSeeActivity.LIVE_ID, zbid);
                            intent.putExtra(QXLiveSeeActivity.LIVE_UID, mResultBean.getZbuid() + "");
                            intent.putExtra(QXLiveSeeActivity.LIVE_GZ, mResultBean.getIsgz() + "");
                            intent.putExtra(QXLiveSeeActivity.LIVE_DOU, mResultBean.getDoudou() + "");
                        } else if (Integer.parseInt(zb.get(zb_position).getApp_types()) == 2) {// 2摄像机
                            intent.setClass(getActivity(), QXLiveSee2Activity.class);
                            intent.putExtra(QXLiveSee2Activity.LIVE_URL, rtmp_url);
                            intent.putExtra(QXLiveSee2Activity.LIVE_ID, zbid);
                            intent.putExtra(QXLiveSee2Activity.LIVE_UID, mResultBean.getZbuid() + "");
                            intent.putExtra(QXLiveSee2Activity.LIVE_GZ, mResultBean.getIsgz() + "");
                            intent.putExtra(QXLiveSee2Activity.LIVE_DOU, mResultBean.getDoudou() + "");
                        }

                        startActivity(intent);

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
        params.put("type", type);//    类型（1直播  2视频  3资讯  4干货 5营销）	type
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
                        //    类型（1直播  2视频  3资讯/咨讯  4干货 ）	type
                        if ("1".equals(type)) {
                            getTokenFromSever(id, str_url);
                        } else if ("2".equals(type)) {
                            initVideoDate(id);
                        } else if ("3".equals(type)) {
                            Intent intent = new Intent();
                            intent.putExtra("url", Constant.BASE_URL + str_url + "/uid/" + MyApplication.getInstance().getUid());
                            intent.putExtra("kind", "2");
                            AppManager.getAppManager().startNextActivity(getActivity(), MyWebActivity.class, intent);
                        } else if ("4".equals(type)) {
                            Intent intent = new Intent();
                            intent.putExtra("pdfUrl", Constant.BASE_URL_IMG + str_url);
                            intent.putExtra("spid", id);
                            intent.putExtra("pdfTitle", list_dried.get(pdf_position).getG_title());
                            System.out.println("===========================Fragment1 state======" + list_dried.get(pdf_position).getState());
                            AppManager.getAppManager().startNextActivity(getActivity(), ReadPDFActivity.class, intent);
                        } else if ("5".equals(type)) {
                            Intent intent = new Intent();
                            intent.putExtra("url", Constant.BASE_URL + str_url + "/uid/" + MyApplication.getInstance().getUid());
                            intent.putExtra("kind", "1");
                            AppManager.getAppManager().startNextActivity(getActivity(), MyWebActivity.class, intent);
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
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title_ll_left:
//				if("1".equals(MyApplication.getInstance().getIsLogining())){
//					Intent intent_scan = new Intent(getActivity(),QRCodeScanActivity.class);
//					startActivity(intent_scan);
//				}else{
//					AppManager.getAppManager().startNextActivity(getActivity(), LoginActivity.class);
//				}

                break;
            case R.id.title_center_edit_search:
//				AppManager.getAppManager().startNextActivity(getActivity(), SearchActivity.class);
                break;
            case R.id.title_ll_right:
                AppManager.getAppManager().startNextActivity(getActivity(), SearchActivity.class);
                break;
            case R.id.item_fragment1_qxlive_ll_live:
                isnosee("1", zb.get(0).getId(), Constant.URL_LIVE_RTMPLIVE + zb.get(0).getId());//类型（1直播  2视频  3资讯  4干货 ）
//				getTokenFromSever(zb.get(0).getId(),Constant.URL_LIVE_RTMPLIVE + zb.get(0).getId());
                break;
            case R.id.layout_fragment1_qxlive_ll_livemore:
                intent = new Intent();
//                intent.putExtra("state","");
                AppManager.getAppManager().startNextActivity(getActivity(), QXliveActivity.class, intent);
                break;
            case R.id.item_fragment1_video_ll_videomore:
                intent = new Intent();
                intent.putExtra("TYPE", "1");
                AppManager.getAppManager().startNextActivity(getActivity(), MoreVideosActivity.class, intent);
                break;
            case R.id.item_fragment1_info_ll_infomore:
                intent = new Intent();
                intent.putExtra("vp", 3);
                intent.putExtra("tag",0);
                AppManager.getAppManager().startNextActivity(getActivity(), MainActivity.class, intent);
                break;
            case R.id.layout_fragment1_marketing_ll_marketmore:
                intent = new Intent();
                intent.putExtra("vp",3);
                intent.putExtra("tag",2);
                AppManager.getAppManager().startNextActivity(getActivity(), MainActivity.class, intent);
                break;
            case R.id.item_fragment1_dried_ll_driedmore:
                intent = new Intent();
                intent.putExtra("vp", 3);
                intent.putExtra("tag",1);
                AppManager.getAppManager().startNextActivity(getActivity(), MainActivity.class, intent);
                break;
            case R.id.item_fragment1_dried_ll_5:
                viewPager.setCurrentItem(0);
                break;
            case R.id.item_fragment1_dried_ll_1:
                viewPager.setCurrentItem(1);
                break;
            case R.id.item_fragment1_dried_ll_2:
                viewPager.setCurrentItem(2);
                break;
            case R.id.item_fragment1_dried_ll_3:
                viewPager.setCurrentItem(3);
                break;
            case R.id.item_fragment1_dried_ll_4:
                viewPager.setCurrentItem(4);
                break;
        }
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
////			mWebView.loadUrl(url);
////			System.out.println("===========================首页 onUserVisible url = " + mWebView.getUrl());
//		}
    }

    @Override
    public void onResume() {
        super.onResume();
        initDate();
//		mWebView.loadUrl(url);
//		mWebView.goBack(); //goBack()表示返回WebView的上一页面
//		System.out.println("===========================首页 onResume url = " + mWebView.getUrl());
    }

    /**
     * @param url 要下载的文件URL
     * @throws Exception
     */
    public void downloadFile(String url) throws Exception {

        // 指定文件类型
        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
        // 获取二进制数据如图片和其他文件
        mAsyncHttpClient.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] binaryData) {
                String tempPath = Environment.getExternalStorageDirectory()
                        .getPath() + "/newpic.png";
                // TODO Auto-generated method stub
                // 下载成功后需要做的工作
//                progress.setProgress(0);
                //
                Log.e("binaryData:", "共下载了：" + binaryData.length);
                //
                Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,
                        binaryData.length);

                File file = new File(tempPath);
                // 压缩格式
                Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
                // 压缩比例
                int quality = 100;
                try {
                    // 若存在则删除
                    if (file.exists())
                        file.delete();
                    // 创建文件
                    file.createNewFile();
                    //
                    OutputStream stream = new FileOutputStream(file);
                    // 压缩输出
                    bmp.compress(format, quality, stream);
                    // 关闭
                    stream.close();
                    //
                    System.out.println("===========下载成功 newpic路径为" + tempPath);
                    MyApplication.getInstance().setNew_tag(tempPath);
                    mRecommendedVideoAdapter.notifyDataSetChanged();
//                    Toast.makeText(mContext, "下载成功\n" + tempPath,Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                // TODO Auto-generated method stub
                super.onProgress(bytesWritten, totalSize);
                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                // 下载进度显示
//                progress.setProgress(count);
                Log.e("下载 Progress>>>>>", bytesWritten + " / " + totalSize);

            }

            @Override
            public void onRetry(int retryNo) {
                // TODO Auto-generated method stub
                super.onRetry(retryNo);
                // 返回重试次数
            }

        });
    }

    private void initTestVideo() {
        mVideoList = new ArrayList<RecommendedVideoBean>();
        for (int i = 0; i < 3; i++) {
            RecommendedVideoBean mRecommendedVideoBean = new RecommendedVideoBean();
//			mRecommendedVideoBean.setName("");
            mRecommendedVideoBean.setNum("2" + i + "万");
//			mRecommendedVideoBean.setContext("");
            mVideoList.add(mRecommendedVideoBean);
        }

//		mRecommendedVideoAdapter = new RecommendedVideoAdapter(getActivity(),mVideoList);
//		lv_video.setAdapter(mRecommendedVideoAdapter);
    }

    private void initTestInfo() {
        mInfoList = new ArrayList<RecommendedInfoBean>();
        for (int i = 0; i < 4; i++) {
            RecommendedInfoBean mRecommendedInfoBean = new RecommendedInfoBean();
//			mRecommendedInfoBean.setName("");
//			mRecommendedInfoBean.setNum("2"+i+"万");
//			mRecommendedInfoBean.setContext("");
            mRecommendedInfoBean.setTimes("2016-12-1" + i);
            if (i < 3) {
                mRecommendedInfoBean.setType("1");
            } else {
                mRecommendedInfoBean.setType("2");
            }
            mInfoList.add(mRecommendedInfoBean);
        }

//		mRecommendedInfoAdapter = new RecommendedInfoAdapter(getActivity(),mInfoList);
//		lv_info.setAdapter(mRecommendedInfoAdapter);
    }

    private void initDriedInfo() {
        mDriedList = new ArrayList<RecommendedDriedBean>();
        for (int i = 0; i < 4; i++) {
            RecommendedDriedBean mRecommendedDriedBean = new RecommendedDriedBean();
//			mRecommendedInfoBean.setName("");
//			mRecommendedInfoBean.setContext("");
            mRecommendedDriedBean.setTimes("2016-12-1" + i);
            mDriedList.add(mRecommendedDriedBean);
        }

//		mRecommendedDriedAdapter = new RecommendedDriedAdapter(getActivity(),mDriedList);
//		lv_dried.setAdapter(mRecommendedDriedAdapter);
    }


}