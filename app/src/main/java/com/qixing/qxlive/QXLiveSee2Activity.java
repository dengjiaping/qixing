package com.qixing.qxlive;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.activity.MyWalletActivity;
import com.qixing.adapter.DialogGiftAdapter;
import com.qixing.adapter.DialogRechargeAdapter;
import com.qixing.adapter.MyBankAdapter;
import com.qixing.adapter.QXliveHeadingAdapter;
import com.qixing.adapter.SeeQXliveRankAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.MyApplication;
import com.qixing.bean.GiftBean;
import com.qixing.bean.InitJson;
import com.qixing.bean.MyRebateBean;
import com.qixing.bean.PayMethodBean;
import com.qixing.bean.QXLiveInfoBean;
import com.qixing.bean.QXLiveInfoJson;
import com.qixing.bean.RechargeBean;
import com.qixing.bean.RechargeJson;
import com.qixing.bean.ResultBean;
import com.qixing.bean.ReviseBankJson;
import com.qixing.bean.SeeQXliveRankBean;
import com.qixing.bean.SeeQXliveRankJson;
import com.qixing.bean.SendGiftBean;
import com.qixing.bean.ShareBean;
import com.qixing.global.Constant;
import com.qixing.qxlive.gift.CustomRoundView;
import com.qixing.qxlive.gift.GiftAnmManager;
import com.qixing.qxlive.gift.GiftDateUtlis;
import com.qixing.qxlive.gift.MagicTextView;
import com.qixing.qxlive.rongyun.LiveKit;
import com.qixing.qxlive.rongyun.controller.ChatListAdapter;
import com.qixing.qxlive.rongyun.fakeserver.FakeServer;
import com.qixing.qxlive.rongyun.ui.fragment.BottomPanelFragment;
import com.qixing.qxlive.rongyun.ui.fragment.BottomPanelFragment2;
import com.qixing.qxlive.rongyun.ui.fragment.TopBarFragment;
import com.qixing.qxlive.rongyun.ui.message.GiftMessage;
import com.qixing.qxlive.rongyun.widget.ChatListView;
import com.qixing.qxlive.rongyun.widget.InputPanel;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.utlis.listener.AutoLoadListener;
import com.qixing.view.CircleImageView;
import com.qixing.view.HorizontalListView;
import com.qixing.view.MyGridView;
import com.qixing.view.imagecut.ImageTools;
import com.qixing.widget.Toasts;
import com.qixing.wxapi.WXPayEntryActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yan.bsrgift.BSRGiftLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;

/**
 * Created by wicep on 2015/12/23.
 * 直播观看页面 摄像机
 */
public class QXLiveSee2Activity extends FragmentActivity implements View.OnClickListener, Handler.Callback {

    public static Context mContext;

    public static final String LIVE_URL = "live_url";//直播地址
    public static final String LIVE_ID = "live_id";//直播ID
    public static final String LIVE_GZ = "live_GZ";//是否关注
    public static final String LIVE_UID = "liveUId";//主播ID
    public static final String LIVE_DOU = "live_dou";//用户星币

    private String liveUrl;//直播地址
    private String liveId;//直播间ID
    private String liveGZ;//是否关注
    private String liveUId;//主播ID
    private String liveDou;//用户星币

    private ViewGroup background;
    private ChatListView chatListView;
    private LinearLayout ll_bottom;
    private BottomPanelFragment2 bottomPanel;
    private ImageView btn_close;//关闭
    private ImageView btn_rotate_icon;//前后摄像头
    private ImageView btn_input;//聊天
    //    private HeartLayout heartLayout;
    private SurfaceView surfaceView;
    private ImageView img_loading;

    /**
     * 音量
     */
    private AudioManager mAudioManager;

    private Random random = new Random();
    private Handler handler = new Handler(this);
    private ChatListAdapter chatListAdapter; //聊天
    private static String roomId;
    private KSYMediaPlayer ksyMediaPlayer;
    private SurfaceHolder surfaceHolder;

    /**
     * 直播信息 头像
     */
    private LinearLayout fragment_topbar_ll1, fragment_topbar_ll2;

    private QXLiveInfoJson mHDLiveInfoJson;
    private List<QXLiveInfoBean> list = new ArrayList<QXLiveInfoBean>();
    private QXliveHeadingAdapter mHDliveHeadingAdapter;

    private TopBarFragment topBar;
    private CircleImageView img_head;
    private TextView tv_head;
    private HorizontalListView horizon_listview;
    private TextView tv_liveid, tv_livetime;
    private Button btn_attention, btn_clean;


    /**
     * umeng
     */
    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    private ImageView btn_share;//分享
    private ImageView btn_share2;
    private ShareBean mShareBean;

    /**
     * 礼物 gift
     */
    private ImageView btn_gift;//送礼物
    /**
     * 送礼物 view
     */
    private LinearLayout llgiftcontent;
    /**
     * 动画相关
     */
    private NumAnim giftNumAnim;
    private TranslateAnimation inAnim;
    private TranslateAnimation outAnim;
    private AnimatorSet animatorSetHide = new AnimatorSet();
    private AnimatorSet animatorSetShow = new AnimatorSet();
    /**
     * 数据相关
     */
    private List<View> giftViewCollection = new ArrayList<View>();


    /**
     * 横竖屏
     */
    private ImageView btn_full, btn_window;
    private RelativeLayout rl_surface;
    private LinearLayout qxlive_ll_top_full;
    private int myVideoHeight, myVideoWidth;
    /**
     * 设置窗口模式下videoview的高度
     */
    private int videoHeight;
    /**
     * 设置窗口模式下的videoview的宽度
     */
    private int videoWidth;

    /**
     * 标题
     */
    private TextView tv_title;
    private LinearLayout rl_liveinfo;
    private Button btn_attention2;
    private TextView tv_name, tv_seenum2;
    private CustomRoundView crvheadimage;


    //选项卡
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private TextView chatLayout;
    private TextView rankLayout;
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


    /**
     * 排行
     */
    private PullToRefreshListView mListView;
    private TextView tv_noolder;
    private SeeQXliveRankAdapter mSeeQXliveRankAdapter;
    private List<SeeQXliveRankBean> listRank;
    private SeeQXliveRankJson mSeeQXliveRankJson;

    private boolean isRefresh = false;


    private IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            ksyMediaPlayer.start();
        }
    };

    private IMediaPlayer.OnCompletionListener onCompletionListener = new IMediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            System.out.println("===========================播放完成时会发出此回调 QXLiveSee2Activity ==== ");
            quitFromeServe();
        }
    };

    private IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {

        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            switch (i) {
                case 1:
                    break;
                case 3:
                    System.out.println("===========================视频渲染结束 ==== ");
                    img_loading.setVisibility(View.GONE);
                    break;
                default:
            }
            return false;
        }
    };

    private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (ksyMediaPlayer != null && ksyMediaPlayer.isPlaying())
                ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (ksyMediaPlayer != null)
                ksyMediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (ksyMediaPlayer != null) {
                ksyMediaPlayer.setDisplay(null);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置当前的Activity 无Title并且全屏 即必须在setContentView之前
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
        setContentView(R.layout.activity_qxlive_see2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕唤醒
//        mContext = this;
        initStatusbar(this, R.color.black);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        LiveKit.setCurrentUser(FakeServer.getUserInfo());
        LiveKit.addEventHandler(handler);//融云聊天
        initViewPage();//初始化聊天记录和排行
        initView();
        startLiveShow();//开始观看直播
        initUMeng();//初始化友盟view
        initGigt();//初始化刷礼物view
        initFullWindow();//初始化横竖屏按钮
    }

    private void initView() {
        background = (ViewGroup) findViewById(R.id.background);
//        chatListView = (ChatListView) findViewById(R.id.chat_listview);//融云聊天记录layout
//        chatListView.setOnClickListener(this);
        /**
         * 底部+背景点击事件
         * */
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        bottomPanel = (BottomPanelFragment2) getSupportFragmentManager().findFragmentById(R.id.bottom_bar);
//        bottomPanel = (BottomPanelFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_bar);
        btn_close = (ImageView) bottomPanel.getView().findViewById(R.id.btn_close);
        btn_rotate_icon = (ImageView) bottomPanel.getView().findViewById(R.id.btn_rotate_icon);
        btn_rotate_icon.setVisibility(View.GONE);
//        heartLayout = (HeartLayout) findViewById(R.id.heart_layout);//点赞layout
        surfaceView = (SurfaceView) findViewById(R.id.player_surface);
        img_loading = (ImageView) findViewById(R.id.player_img_loading);

        btn_input = (ImageView) bottomPanel.getView().findViewById(R.id.btn_input);
        btn_input.setVisibility(View.GONE);//关闭聊天按钮

        chatListAdapter = new ChatListAdapter();//融云聊天记录adapter
        chatListView.setAdapter(chatListAdapter);
        background.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        btn_rotate_icon.setOnClickListener(this);
        bottomPanel.setInputPanelListener(new InputPanel.InputPanelListener() {
            @Override
            public void onSendClick(String text) {//融云聊天记录发送信息
                bottomPanel.onBackAction();
                final TextMessage content = TextMessage.obtain(text);
                LiveKit.sendMessage(content);
            }
        });

        ksyMediaPlayer = new KSYMediaPlayer.Builder(this).build();
        ksyMediaPlayer.setOnPreparedListener(onPreparedListener);
        ksyMediaPlayer.setScreenOnWhilePlaying(true);
        ksyMediaPlayer.setBufferTimeMax(5);
        ksyMediaPlayer.setTimeout(20, 100);
//        ksyMediaPlayer.setRotateDegree(90);//视频旋转90

        // 填充模式
        ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);

        // 裁剪模式
//        ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        rl_surface = (RelativeLayout) findViewById(R.id.qxlive_see_ll_surface);

        ViewTreeObserver vto2 = rl_surface.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl_surface.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                myVideoHeight = rl_surface.getHeight();
                myVideoWidth = rl_surface.getWidth();
                System.out.println("===========================视频播放器高宽 ======= " + rl_surface.getHeight() + "," + rl_surface.getWidth());
            }
        });


        /**
         * 播放完成时会发出此回调
         * */
        ksyMediaPlayer.setOnCompletionListener(onCompletionListener);

        /**
         * 消息监听器,会将关于播放器的消息告知开发者,例如:视频渲染、音频渲染等
         * */
        ksyMediaPlayer.setOnInfoListener(onInfoListener);

        /**
         * 顶部 直播信息
         * */
        topBar = (TopBarFragment) getSupportFragmentManager().findFragmentById(R.id.top_bar);
        fragment_topbar_ll1 = (LinearLayout) topBar.getView().findViewById(R.id.fragment_topbar_ll1);
        fragment_topbar_ll2 = (LinearLayout) topBar.getView().findViewById(R.id.fragment_topbar_ll2);
        fragment_topbar_ll1.setVisibility(View.GONE);
        fragment_topbar_ll2.setVisibility(View.GONE);
        img_head = (CircleImageView) topBar.getView().findViewById(R.id.top_bar_img_head);
        tv_head = (TextView) topBar.getView().findViewById(R.id.top_bar_tv_head);
        horizon_listview = (HorizontalListView) topBar.getView().findViewById(R.id.top_bar_horizon_listview);
        tv_liveid = (TextView) topBar.getView().findViewById(R.id.top_bar_tv_liveid);
        tv_livetime = (TextView) topBar.getView().findViewById(R.id.top_bar_tv_livetime);
        btn_attention = (Button) topBar.getView().findViewById(R.id.top_bar_btn_attention);
        btn_attention.setOnClickListener(this);
        btn_clean = (Button) topBar.getView().findViewById(R.id.top_bar_btn_clean);
        btn_clean.setOnClickListener(this);
//        topBar.setMenuVisibility(false);

    }

    /**
     * 初始化聊天记录和排行
     */
    private void initViewPage() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.layout_qxlive_see2_chat, null);
        View view2 = inflater.inflate(R.layout.layout_qxlive_see2_rank, null);
        chatListView = (ChatListView) view1.findViewById(R.id.chat_listview);//融云聊天记录layout
        initPullToRefresh(view2);//初始化排行
        chatLayout = (TextView) findViewById(R.id.chatLayout);
        rankLayout = (TextView) findViewById(R.id.rankLayout);
        scrollbar = (ImageView) findViewById(R.id.scrollbar);
        chatLayout.setOnClickListener(this);
        rankLayout.setOnClickListener(this);
        pageview = new ArrayList<View>();
        //添加想要切换的界面
        pageview.add(view1);
        pageview.add(view2);
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
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.icon_info_close).getWidth();
        //为了获取屏幕宽度，新建一个DisplayMetrics对象
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //将当前窗口的一些信息放在DisplayMetrics类中
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //得到屏幕的宽度
        int screenW = displayMetrics.widthPixels;
        //设置滚动条imgview的宽度
        ViewGroup.LayoutParams para = scrollbar.getLayoutParams();
        para.width = screenW / 2;
        scrollbar.setLayoutParams(para);

        //计算出滚动条初始的偏移量
//        offset = (screenW / 2 - bmpW) / 2;
        offset = 0;
        //计算出切换一个界面时，滚动条的位移量
//        one = offset * 2 + bmpW;
        one = screenW / 2;

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        //将滚动条的初始位置设置成与左边界间隔一个offset
        scrollbar.setImageMatrix(matrix);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/
                    animation = new TranslateAnimation(one, 0, 0, 0);

                    chatLayout.setTextColor(getResources().getColor(R.color.color_bottom_select));
                    rankLayout.setTextColor(getResources().getColor(R.color.white_F));
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    chatLayout.setTextColor(getResources().getColor(R.color.white_F));
                    rankLayout.setTextColor(getResources().getColor(R.color.color_bottom_select));
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

    /**
     * 初始化
     */
    private void initPullToRefresh(View view) {
        tv_noolder = (TextView) view.findViewById(R.id.financing_bank_tv_noolder);
        tv_noolder.setOnClickListener(this);
        mListView = (PullToRefreshListView) view.findViewById(R.id.mListView);
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
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        init(mListView);
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
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
//                initDatas();
                isRefresh = true;
                rankDatas();//刷新排行数据
//                mListView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
//                mListView.onRefreshComplete();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toasts.show("点击了条目"+position);
            }
        });
//        initTestDate();
    }

    private void initTestDate() {
        listRank = new ArrayList<SeeQXliveRankBean>();
        for (int i = 0; i < 15; i++) {
            SeeQXliveRankBean mSeeQXliveRankBean = new SeeQXliveRankBean();
            mSeeQXliveRankBean.setUname("张三" + i);
            mSeeQXliveRankBean.setZdou((1000 + i) + "");
            listRank.add(mSeeQXliveRankBean);
        }
        mSeeQXliveRankAdapter = new SeeQXliveRankAdapter(QXLiveSee2Activity.this, listRank);
        mListView.setAdapter(mSeeQXliveRankAdapter);
    }

    private void init(PullToRefreshListView mListView) {
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

    /**
     * 初始化友盟view
     */
    private void initUMeng() {

        btn_share = (ImageView) bottomPanel.getView().findViewById(R.id.btn_share);
        btn_share.setOnClickListener(this);

        btn_share2 = (ImageView) findViewById(R.id.btn_share2);
        btn_share2.setOnClickListener(this);

        mShareListener = new CustomShareListener(QXLiveSee2Activity.this);

                /*无自定按钮的分享面板*/
//        mShareAction = new ShareAction(TestActivity.this).setDisplayList(
//                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
//                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
//                SHARE_MEDIA.ALIPAY, SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
//                SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL, SHARE_MEDIA.YNOTE,
//                SHARE_MEDIA.EVERNOTE, SHARE_MEDIA.LAIWANG, SHARE_MEDIA.LAIWANG_DYNAMIC,
//                SHARE_MEDIA.LINKEDIN, SHARE_MEDIA.YIXIN, SHARE_MEDIA.YIXIN_CIRCLE,
//                SHARE_MEDIA.TENCENT, SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.TWITTER,
//                SHARE_MEDIA.WHATSAPP, SHARE_MEDIA.GOOGLEPLUS, SHARE_MEDIA.LINE,
//                SHARE_MEDIA.INSTAGRAM, SHARE_MEDIA.KAKAO, SHARE_MEDIA.PINTEREST,
//                SHARE_MEDIA.POCKET, SHARE_MEDIA.TUMBLR, SHARE_MEDIA.FLICKR,
//                SHARE_MEDIA.FOURSQUARE, SHARE_MEDIA.MORE)
//                .withText(Defaultcontent.text + "来自友盟自定义分享面板")
//                .setCallback(mShareListener);


//        String shareUrl = Constant.BASE_URL+Constant.URL_USERAPI_FXZBXX+"/"+roomId;
//        mShareAction = new ShareAction(QXLiveSee2Activity.this).setDisplayList(
//                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                .withTitle("七星时代")
//                .withTargetUrl(shareUrl)
//                .withText("来自七星时代的分享")
//                .setCallback(mShareListener);
//        System.out.println("=========================== 分享连接 ====" + shareUrl);
    }

    private void share(File file) {
        String shareUrl = Constant.BASE_URL + Constant.URL_USERAPI_FXZBXX + "/id/" + roomId + "/type/2";
//                    UMImage image = new UMImage(PlayVideoActivity.this, Constant.BASE_URL_IMG+mShareBean.getPic());//网络图片
        UMImage image = new UMImage(QXLiveSee2Activity.this, file);//本地文件
        mShareAction = new ShareAction(QXLiveSee2Activity.this).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .withTitle(mShareBean.getTitle())
                .withTargetUrl(shareUrl)
                .withMedia(image)
                .withText(mShareBean.getContent())
                .setCallback(mShareListener);
        System.out.println("=========================== 分享连接 ====" + shareUrl);
        System.out.println("=========================== 分享图片连接 ====" + Constant.BASE_URL_IMG + mShareBean.getPic());
        mShareAction.open();
    }

    private class CustomShareListener implements UMShareListener {

        private WeakReference<QXLiveSee2Activity> mActivity;

        private CustomShareListener(QXLiveSee2Activity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), " 收藏成功", Toast.LENGTH_SHORT).show();
                System.out.println("======================" + platform + "===== 分享 收藏成功啦  ");
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST
                        && platform != SHARE_MEDIA.LINKEDIN
                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    shareSuccess();
                    Toast.makeText(mActivity.get(), " 分享成功", Toast.LENGTH_SHORT).show();
                    System.out.println("========================" + platform + "=== 分享 分享成功啦  ");
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST
                    && platform != SHARE_MEDIA.LINKEDIN
                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), " 分享失败", Toast.LENGTH_SHORT).show();
                System.out.println("=======================" + platform + "==== 分享 分享失败啦  ");
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                    System.out.println("======================" + platform + "===== 分享 throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), " 分享取消", Toast.LENGTH_SHORT).show();
            System.out.println("======================" + platform + "===== 分享 分享取消了  ");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化刷礼物view
     */
    private void initGigt() {
        btn_gift = (ImageView) bottomPanel.getView().findViewById(R.id.btn_gift);
        btn_gift.setOnClickListener(this);
        btn_gift.setVisibility(View.VISIBLE);

        llgiftcontent = (LinearLayout) findViewById(R.id.llgiftcontent);
        mGiftLayout = (BSRGiftLayout) findViewById(R.id.gift_layout);
        giftAnmManager = new GiftAnmManager(mGiftLayout, this);

        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_out);
        giftNumAnim = new NumAnim();
        clearTiming();

        liveDou = getIntent().getStringExtra(QXLiveSee2Activity.LIVE_DOU);
    }

    /**
     * 初始化横竖屏按钮
     */
    private void initFullWindow() {
        qxlive_ll_top_full = (LinearLayout) findViewById(R.id.qxlive_ll_top_full);
        qxlive_ll_top_full.setVisibility(View.GONE);

        btn_full = (ImageView) findViewById(R.id.btn_full);
        btn_full.setOnClickListener(this);
        btn_full.setVisibility(View.VISIBLE);
        btn_window = (ImageView) findViewById(R.id.btn_window);
        btn_window.setOnClickListener(this);
        btn_window.setVisibility(View.GONE);

//        private TextView tv_title;
//        private RelativeLayout rl_liveinfo;
//        private Button btn_attention2;
//        private TextView tv_name;
//        private CustomRoundView crvheadimage;
        tv_title = (TextView) findViewById(R.id.qxlive_ll_tv_title);

        rl_liveinfo = (LinearLayout) findViewById(R.id.qxlive_rl_liveinfo);
        btn_attention2 = (Button) findViewById(R.id.qxlive_rl_liveinfo_btn_attention);
        tv_name = (TextView) findViewById(R.id.qxlive_rl_liveinfo_tv_name);
        tv_seenum2 = (TextView) findViewById(R.id.qxlive_rl_liveinfo_tv_seenum2);
        crvheadimage = (CustomRoundView) findViewById(R.id.qxlive_rl_liveinfo_crvheadimage);
        btn_attention2.setOnClickListener(QXLiveSee2Activity.this);
    }

    private String userID;
    private String userGiftInfo;
    /**
     * 消息接收监听者
     */
    public RongIMClient.OnReceiveMessageListener onReceiveMessageListener = new RongIMClient.OnReceiveMessageListener() {
        /**
         * 收到消息的处理。
         * @param message 收到的消息实体。
         * @param i 剩余未拉取消息数目。
         * @return
         */
        @Override
        public boolean onReceived(io.rong.imlib.model.Message message, int i) {
            LiveKit.handleEvent(LiveKit.MESSAGE_ARRIVED, message.getContent());
            String zbid = message.getTargetId();
            // 此处输出判断是否是文字消息，并输出，其他消息同理。
            if (message.getContent() instanceof GiftMessage) { //instanceof 测试它左边的对象是否是它右边的类的实例

            }
            if ("RC:GiftMsg".equals(message.getObjectName())) {
                GiftMessage msg = (GiftMessage) message.getContent();
                String content = msg.getType();
                userID = (String) msg.getUserInfo().getUserId();
                userGiftInfo = (String) content;
                System.out.println("===========================消息记录 msg.getContent() ==== " + content + ",userID = " + userID + ",userGiftInfo = " + userGiftInfo);
                msg.setContent(userGiftInfo);
                MyGiftRunnable myGiftRunnable = new MyGiftRunnable();
                Thread mThread = new Thread(myGiftRunnable);
                mThread.run();
//                showGift(userID+userGiftInfo,userID,userGiftInfo,GiftDateUtlis.getGiftImgID(userGiftInfo));
            }

            if ("RC:InfoNtf".equals(message.getObjectName())) {
                InformationNotificationMessage msg = (InformationNotificationMessage) message.getContent();
                String content = msg.getMessage();
                System.out.println("===========================消息记录 msg.getContent() ==== " + content + ",zbid = " + zbid + ",roomId = " + roomId);
                String finshStr = roomId + "结束了直播";
                if (roomId.equals(zbid)) {
                    if (finshStr.equals(content)) {
                        System.out.println("===========================消息记录 主播退出直播 ====content " + content + ",zbid = " + zbid + ",finshStr " + finshStr + ",roomId = " + roomId);
//                    quitFromeServe();
//                    onBackoffClick();
                        MyRunnable myRunnable = new MyRunnable();
                        Thread mThread = new Thread(myRunnable);
                        mThread.run();
                    }
                }
            }
            return false;
        }
    };

    /**
     * 主播退出关闭dialog线程
     */
    private int CLOSE_DIALOG = 0;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            System.out.println("===========================消息记录 主播退出直播 ====");
            quitFromeServe();
//            switch (msg.what) {
//                case CLOSE_DIALOG:
//                    //获取传递的数据
//                    //Bundle data = msg.getData();
//                    //int count = data.getInt("COUNT");
//                    //处理UI更新等操作
//                    break;
//                quitFromeServe();
//            }
        }
    };

    class MyRunnable implements Runnable {
        public void run() {
            //执行数据操作，不涉及到UI
            Message msg = new Message();
            msg.what = CLOSE_DIALOG;
            mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
        }
    }

    /**
     * 刷礼物线程
     */
    private int GIFT_DIALOG = 0;

    private Handler mGiftHandler = new Handler() {

        public void handleMessage(Message msg) {
            System.out.println("===========================消息记录 刷礼物 ====");
            showGift(userID + userGiftInfo, userID, userGiftInfo, GiftDateUtlis.getGiftImgID(userGiftInfo));
        }
    };

    class MyGiftRunnable implements Runnable {
        public void run() {
            //执行数据操作，不涉及到UI
            Message msg = new Message();
            msg.what = GIFT_DIALOG;
            mGiftHandler.sendMessage(msg); // 向Handler发送消息,更新UI
        }
    }

    /**
     * 开始观看直播 初始化直播信息
     */
    private void startLiveShow() {

//        String liveUrl = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
//        String liveUrl = "rtmp://test.rtmplive.ks-cdn.com/live/888";

        liveUrl = getIntent().getStringExtra(QXLiveSee2Activity.LIVE_URL);
        liveId = getIntent().getStringExtra(QXLiveSee2Activity.LIVE_ID);
        liveGZ = getIntent().getStringExtra(QXLiveSee2Activity.LIVE_GZ);
        liveUId = getIntent().getStringExtra(QXLiveSee2Activity.LIVE_UID);
        roomId = liveId;
//        liveUrl = "rtmp://test.rtmplive.ks-cdn.com/live/460";
//        liveInfo();
        //启动定时器
        timer.schedule(task, 0, 5000);
        System.out.println("===========================拉流url ==== " + liveUrl);
        joinChatRoom(roomId);//融云聊天 加入了聊天室
        playShow(liveUrl);//开始拉流 观看直播

//        topBar.setMenuVisibility(true);
        if ("1".equals(liveGZ)) {
            btn_attention.setVisibility(View.GONE);
            btn_clean.setVisibility(View.VISIBLE);
        } else {
            btn_attention.setVisibility(View.VISIBLE);
            btn_clean.setVisibility(View.GONE);
        }

        rankDatas();//刷新排行数据
    }

    /**
     * 用户加入聊天室
     */
    private void joinChatRoom(final String roomId) {
        LiveKit.setOnReceiveMessageListener(onReceiveMessageListener);
//        RongIMClient.setOnReceiveMessageListener(onReceiveMessageListener);
        LiveKit.joinChatRoom(roomId, -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
//                final InformationNotificationMessage content = InformationNotificationMessage.obtain("进入了直播间");
//                LiveKit.sendMessage(content);

//                final GiftMessage content1 = new GiftMessage("2",MyApplication.getInstance().getUname()+"进入了直播间");
//                LiveKit.sendMessage(content1);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Toast.makeText(QXLiveSee2Activity.this, "聊天室加入失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });

//        /**
//         * 根据会话类型的目标 Id，回调方式获取某消息类型标识的N条历史消息记录。
//         *
//         * @param conversationType 会话类型。不支持传入 ConversationType.CHATROOM 聊天室会话类型。
//         * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 。
//         * @param dateTime         从该时间点开始获取消息。即：消息中的 sentTime；第一次可传 0，获取最新 count 条。
//         * @param count            要获取的消息数量，最多 20 条。
//         * @param callback         获取历史消息记录的回调，按照时间顺序从新到旧排列。
//         */
//        RongIMClient.getInstance().getRemoteHistoryMessages(Conversation.ConversationType.APP_PUBLIC_SERVICE, "", 0, -1, new RongIMClient.ResultCallback<List<io.rong.imlib.model.Message>>() {
//            @Override
//            public void onSuccess(List<io.rong.imlib.model.Message> messages) {
//
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//
//            }
//        });

    }

    private void playShow(String liveUrl) {
        try {
            ksyMediaPlayer.setDataSource(liveUrl);
            ksyMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceCallback);
    }

    @Override
    public void onBackPressed() {
        if (!bottomPanel.onBackAction()) {
            finish();
            return;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case LiveKit.MESSAGE_ARRIVED: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SENT: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SEND_ERROR: {
                break;
            }
            default:
        }
        chatListAdapter.notifyDataSetChanged();
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.background://点击背景
                bottomPanel.onBackAction();
                break;
            case R.id.chat_listview://点击背景
                bottomPanel.onBackAction();
                break;
            case R.id.btn_close://关闭直播
                onBackoffClick();
                break;
            case R.id.chatLayout:
                //点击"聊天“时切换到第一页
                viewPager.setCurrentItem(0);
                break;
            case R.id.rankLayout:
                //点击“排行”时切换的第二页
                viewPager.setCurrentItem(1);
                break;
            case R.id.financing_bank_tv_noolder:
                //更新“排行”数据
                rankDatas();
                break;
            case R.id.qxlive_rl_liveinfo_btn_attention://关注
                attention();
                break;
            case R.id.btn_full://全屏
                btn_full.setVisibility(View.GONE);
                btn_window.setVisibility(View.VISIBLE);
                qxlive_ll_top_full.setVisibility(View.VISIBLE);
                rl_liveinfo.setVisibility(View.GONE);
                llgiftcontent.setVisibility(View.GONE);
                chatListView.setVisibility(View.GONE);
                btn_gift.setVisibility(View.GONE);
                btn_share.setVisibility(View.GONE);
                btn_input.setVisibility(View.GONE);

                ll_bottom.setVisibility(View.GONE);

                bottomPanel.onBackAction();

                //设置为横屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                /**
                 setSystemUiVisibility(int visibility)方法可传入的实参为：
                 1. View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。
                 2. View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。
                 3. View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
                 4. View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
                 5. View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                 6. View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                 7. View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
                 8. View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。
                 * */
                //隐藏状态栏，同时Activity会伸展全屏显示。
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                rl_surface.setLayoutParams(LayoutParams);

//                ksyMediaPlayer.setRotateDegree(270);//视频旋转 只能设置90/180/270中的某个值
                //裁剪模式
//                ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                // 填充模式
//                ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                break;
            case R.id.btn_window://窗口
                btn_full.setVisibility(View.VISIBLE);
                btn_window.setVisibility(View.GONE);
                qxlive_ll_top_full.setVisibility(View.GONE);
                rl_liveinfo.setVisibility(View.VISIBLE);
                llgiftcontent.setVisibility(View.VISIBLE);
                chatListView.setVisibility(View.VISIBLE);
                btn_gift.setVisibility(View.VISIBLE);
                btn_share.setVisibility(View.VISIBLE);
                btn_input.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.VISIBLE);

                //设置为竖屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //显示状态栏，同时Activity会伸展全屏显示。
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                //动态获取宽高
                DisplayMetrics DisplayMetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
//            videoHeight=DisplayMetrics.heightPixels-20;
                videoHeight = myVideoHeight;
                videoWidth = DisplayMetrics.widthPixels - 20;
                RelativeLayout.LayoutParams LayoutParamss = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, videoHeight);
//            LayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_surface.setLayoutParams(LayoutParamss);


//                ksyMediaPlayer.setRotateDegree(0);//视频旋转 只能设置90/180/270中的某个值

                break;
            case R.id.btn_share://分享
//                mShareAction.open();
                shareNum();
                break;
            case R.id.btn_share2://分享
//                mShareAction.open();
                shareNum();
                break;
            case R.id.btn_gift://送礼物
                showGiftDialog();
                break;
            case R.id.top_bar_btn_attention://关注
                attention();
                break;
            case R.id.top_bar_btn_clean://取消关注
                clean();
                break;
        }
//        if (v.equals(background)) {
//            bottomPanel.onBackAction();
//        } else if (v.equals(btnGift)) {
//            GiftMessage msg = new GiftMessage("2", "送您一个礼物");
//            LiveKit.sendMessage(msg);
//        } else if (v.equals(btnHeart)) {
//            heartLayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    int rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
//                    heartLayout.addHeart(rgb);
//                }
//            });
//            GiftMessage msg = new GiftMessage("1", "为您点赞");
//            LiveKit.sendMessage(msg);
//        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onBackoffClick();
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                break;
            default:
                break;
        }
        return true;
    }

    AlertDialog mAlertDialogQuit;

    /**
     * 用户退出直播间Dialog
     */
    private void onBackoffClick() {
        mAlertDialogQuit = new AlertDialog.Builder(QXLiveSee2Activity.this)
                .setTitle("提示")
                .setMessage("退出直播？")
                .setPositiveButton(R.string.dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                mAlertDialogQuit.dismiss();
                                //退出聊天室
                                quit();
                            }
                        })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialogQuit.dismiss();
                    }
                }).create();
        mAlertDialogQuit.setCanceledOnTouchOutside(false);//点击外部不可消失dialog
        mAlertDialogQuit.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                //屏蔽返回键 不可消失dialog
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                    mAlertDialog.dismiss();
//                    quit();
                }
                return false;
            }
        });
        mAlertDialogQuit.show();
//        final MaterialDialog mMaterialDialog = new MaterialDialog(HDLiveShowActivity.this);
//        if (mMaterialDialog != null) {
//            mMaterialDialog.setTitle("退出直播？")
//                    .setMessage("")
//                    .setPositiveButton(R.string.dialog_ok,
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    mMaterialDialog.dismiss();
//                                    //退出聊天室
//                                    quit();
//                                }
//                            }
//                    )
//                    .setNegativeButton(R.string.dialog_cancel,
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    mMaterialDialog.dismiss();
//                                }
//                            })
//                    .setCanceledOnTouchOutside(false)//点击外部不可消失dialog
//                    .show();
//        } else {
//
//        }
    }

    /**
     * 主播已关闭直播Dialog
     */
    private void quitFromeServe() {
        mAlertDialogQuit = new AlertDialog.Builder(QXLiveSee2Activity.this)
                .setTitle("提示")
                .setMessage("您所观看的直播已经结束，感谢您的观看")
                .setPositiveButton(R.string.dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                mAlertDialogQuit.dismiss();
                                //退出聊天室
                                quit();
                            }
                        }).create();
        mAlertDialogQuit.setCanceledOnTouchOutside(false);//点击外部不可消失dialog
        mAlertDialogQuit.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                //屏蔽返回键 不可消失dialog
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                    mAlertDialog.dismiss();
                    quit();
                }
                return false;
            }
        });
        mAlertDialogQuit.show();
    }

    /**
     * 退出聊天室
     */
    private void quit() {
        //退出聊天室
        LiveKit.removeEventHandler(handler);
        LiveKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
                Toast.makeText(QXLiveSee2Activity.this, "退出成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
//                Toast.makeText(QXLiveSee2Activity.this, "退出失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
                System.out.println("=========================== 退出失败! errorCode = ==== " + errorCode);
            }
        });
        //关闭直播
        ksyMediaPlayer.stop();
        //用户退出直播
        quitLive();
        //停止计时器
        if (timer != null) {
            timer.cancel();
        }
        QXLiveSee2Activity.this.finish();
//        MPMoviePlayerLoadStateDidChangeNotification

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ksyMediaPlayer != null) {
            quit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ksyMediaPlayer != null) {
            if (!ksyMediaPlayer.isPlaying()) {
                ksyMediaPlayer.start();//开始
            }
        }
        if (gift_tv_bi != null) {
            initXingBi();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ksyMediaPlayer != null) {
            if (ksyMediaPlayer.isPlaying()) {
                ksyMediaPlayer.pause();//暂停
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();
    ;
    ProgressDialog mProgressDialog = null;
    AlertDialog mAlertDialog;
    ResultBean mResultBean;

    /**
     * 关注直播
     */
    private void attention() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
//            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("zbid", liveId);
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_FAV_ZB;//
        System.out.println("===========================关注直播 url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================关注直播 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        Toasts.show(mResultBean.getMessage());
                        btn_attention.setVisibility(View.GONE);
                        btn_clean.setVisibility(View.VISIBLE);

                        btn_attention2.setVisibility(View.GONE);
                    } else {
                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
                    mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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
        });
    }

    /**
     * 取消关注
     */
    private void clean() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
//            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("zbuid", liveUId);
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_NOFAV_ZB;//
        System.out.println("===========================取消关注 url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================取消关注 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        Toasts.show(mResultBean.getMessage());
                        btn_attention.setVisibility(View.VISIBLE);
                        btn_clean.setVisibility(View.GONE);
                    } else {
                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
                    mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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
        });
    }

    /**
     * 用户退出直播
     */
    private void quitLive() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
//            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("zbid", liveId);
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_NOSEL_ZB;//
        System.out.println("===========================用户退出直播 url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================用户退出直播 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
//                        Toasts.show(mResultBean.getMessage());
                    } else {
//                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
//                    mAlertDialog = new AlertDialog.Builder(HDLiveShowActivity.this)
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
//                mAlertDialog = new AlertDialog.Builder(HDLiveShowActivity.this)
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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
//                mAlertDialog = new AlertDialog.Builder(HDLiveShowActivity.this)
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


    private final Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            message.what = 1;
            handlerTimer.sendMessage(message);
        }
    };
    ;
    Handler handlerTimer = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // 要做的事情
            super.handleMessage(msg);
            liveInfo();
        }
    };

    private void test() {
        //启动定时器
        timer.schedule(task, 2000, 4000);
        //停止计时器
        if (timer != null) {
            timer.cancel();
        }
    }


    /**
     * 直播信息 头像 直播间ID
     */
    private void liveInfo() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
//            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("zbid", liveId);
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_SELZBINFO;//userapi/selzbinfo
        System.out.println("===========================直播信息 头像 直播间ID url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================直播信息 头像 直播间ID response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mHDLiveInfoJson = new Gson().fromJson(response.toString(), QXLiveInfoJson.class);
                    if (mHDLiveInfoJson.getResult().equals("1")) {
                        if (mHDLiveInfoJson.getZbinfo() == null || "".equals(mHDLiveInfoJson.getZbinfo())) {
                        } else {
//                            liveUId = mHDLiveInfoJson.getZbinfo().getUid();
                            if (!TextUtils.isEmpty(mHDLiveInfoJson.getZbinfo().getZb_pic())) {
                                if (mHDLiveInfoJson.getZbinfo().getZb_pic().startsWith("http")) {
                                    ImageLoader.getInstance().displayImage(mHDLiveInfoJson.getZbinfo().getZb_pic(), img_head, ImageLoaderOptions.get_face_Options());
                                } else {
                                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mHDLiveInfoJson.getZbinfo().getZb_pic(), img_head, ImageLoaderOptions.get_face_Options());
                                }
                                System.out.println("===========================礼物 User_Head = " + Constant.BASE_URL_IMG + mHDLiveInfoJson.getZbinfo().getZb_pic());
                            }
                            tv_head.setText(mHDLiveInfoJson.getZbinfo().getSee_num() + "人");
                            tv_liveid.setText("直播号:" + mHDLiveInfoJson.getZbinfo().getZbno());
                            tv_livetime.setText(mHDLiveInfoJson.getZbinfo().getTimes());//DateUtils.TimeStamp2DateYYYYMMDD(mHDLiveInfoJson.getZbinfo().getTimes())
                            //        private TextView tv_title;
                            //        private RelativeLayout rl_liveinfo;
                            //        private Button btn_attention2;
                            //        private TextView tv_name;
                            //        private CustomRoundView crvheadimage;
                            tv_title.setText(mHDLiveInfoJson.getZbinfo().getZb_title());
                            tv_name.setText(mHDLiveInfoJson.getZbinfo().getZbuname());
                            if (!TextUtils.isEmpty(mHDLiveInfoJson.getZbinfo().getZb_pic())) {
                                if (mHDLiveInfoJson.getZbinfo().getZb_pic().startsWith("http")) {
                                    ImageLoader.getInstance().displayImage(mHDLiveInfoJson.getZbinfo().getZb_pic(), crvheadimage, ImageLoaderOptions.get_face_Options());
                                } else {
                                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mHDLiveInfoJson.getZbinfo().getZb_pic(), crvheadimage, ImageLoaderOptions.get_face_Options());
                                }
                                System.out.println("===========================礼物 User_Head = " + Constant.BASE_URL_IMG + mHDLiveInfoJson.getZbinfo().getZb_pic());
                            }
                            tv_seenum2.setText(mHDLiveInfoJson.getZbinfo().getSee_num() + "人");

                        }
                        //更新头像
                        if (mHDLiveInfoJson.getUserlist() == null || mHDLiveInfoJson.getUserlist().size() == 0 || "".equals(mHDLiveInfoJson.getUserlist())) {
                            list.clear();
//                            if(mHDliveHeadingAdapter!=null){
//                                mHDliveHeadingAdapter.notifyDataSetChanged();
//                            }
                        } else {
                            list.clear();
                            list.addAll(mHDLiveInfoJson.getUserlist());
//                            mHDliveHeadingAdapter = new QXliveHeadingAdapter(QXLiveSee2Activity.this, list);
//                            horizon_listview.setAdapter(mHDliveHeadingAdapter);
//                            mHDliveHeadingAdapter.notifyDataSetChanged();
                        }

                    } else {
//                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
//                    mAlertDialog = new AlertDialog.Builder(HDLiveShowActivity.this)
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
//                mAlertDialog = new AlertDialog.Builder(HDLiveShowActivity.this)
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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
//                mAlertDialog = new AlertDialog.Builder(HDLiveShowActivity.this)
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

    private ViewGroup view;

    public void initStatusbar(Context context, int statusbar_bg) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window window = ((Activity) context).getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

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


    private List<GiftBean> giftList = new ArrayList<GiftBean>();
    private MyGridView gv_gift;
    private TextView gift_tv_bi;
    private Button btn_gifts, btn_recharge;
    private DialogGiftAdapter mDialogGiftAdapter;
    private int select_gift = -1;
    private BSRGiftLayout mGiftLayout;
    private GiftAnmManager giftAnmManager;

    private void showGiftDialog() {
        View view = LayoutInflater.from(QXLiveSee2Activity.this).inflate(R.layout.dialog_gift_view, null);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(QXLiveSee2Activity.this, R.style.common_dialog);
        dialog.setContentView(view);
        dialog.show();

        // 监听
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.view_gift_weixin:
                        System.out.println("========================== 打开充值dialog ");
//                        showRechargeDialog();
                        break;

                    case R.id.view_gift_pengyou:
                        // 分享到朋友圈
                        break;

                    case R.id.dialog_gift_btn_gifts:
                        // 取消
                        System.out.println("========================== 礼物 赠送 ");
                        if (select_gift < 0) {
                            Toasts.show("请选择您要赠送的礼物");
                        } else {
//                            Toasts.show(giftList.get(select_gift).getMsg());
                            btn_gifts.setEnabled(false);
                            sendGift(giftList.get(select_gift).getTypes(), "1", giftList.get(select_gift).getXingbi());
//                            showGift(MyApplication.getInstance().getUid()+giftList.get(select_gift).getMsg(),MyApplication.getInstance().getUid(),giftList.get(select_gift).getMsg(),giftList.get(select_gift).getImgID());
                        }
//                        dialog.dismiss();
                        break;
                    case R.id.dialog_gift_btn_recharge:
                        System.out.println("=========================== 星币 充值");
                        startActivity(new Intent(QXLiveSee2Activity.this,MyWalletActivity.class));
                        break;

                }
            }

        };
        ViewGroup mViewgift1 = (ViewGroup) view.findViewById(R.id.view_gift_weixin);
        ViewGroup mViewgift2 = (ViewGroup) view.findViewById(R.id.view_gift_pengyou);
        btn_gifts = (Button) view.findViewById(R.id.dialog_gift_btn_gifts);
        btn_gifts.setTextColor(getResources().getColor(R.color.white_F));
        btn_recharge = (Button) view.findViewById(R.id.dialog_gift_btn_recharge);

        mViewgift1.setOnClickListener(listener);
        mViewgift2.setOnClickListener(listener);
        btn_gifts.setOnClickListener(listener);
        btn_recharge.setOnClickListener(listener);

        gv_gift = (MyGridView) view.findViewById(R.id.dialog_gift_gv_gift);
        gv_gift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < giftList.size(); i++) {
                    if (i == position) {
                        giftList.get(i).setSelect("1");
                    } else {
                        giftList.get(i).setSelect("0");
                    }
                    select_gift = position;
                }
                System.out.println("========================== 选择了 " + giftList.get(position).getMsg());
                mDialogGiftAdapter.notifyDataSetChanged();
            }
        });
        select_gift = -1;
        giftList.clear();
        giftList = GiftDateUtlis.initGiftDate();//初始化礼物列表
        mDialogGiftAdapter = new DialogGiftAdapter(QXLiveSee2Activity.this, giftList);
        gv_gift.setAdapter(mDialogGiftAdapter);

        AutoLoadListener.AutoLoadCallBack callBack=new AutoLoadListener.AutoLoadCallBack() {

            @Override
            public void refresh() {
                select_gift=1;
                giftList.clear();
                giftList=GiftDateUtlis.initGiftDate();
                mDialogGiftAdapter=new DialogGiftAdapter(QXLiveSee2Activity.this,giftList);
                gv_gift.setAdapter(mDialogGiftAdapter);
            }

            @Override
            public void execute() {
                select_gift=-1;
                giftList.clear();
                giftList.addAll(GiftDateUtlis.loadMore());
                mDialogGiftAdapter.notifyDataSetChanged();
            }
        };

        gv_gift.setOnScrollListener(new AutoLoadListener(callBack));

        gift_tv_bi = (TextView) view.findViewById(R.id.dialog_gift_tv_bi);
        gift_tv_bi.setText(liveDou);

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

    }

    private TextView tv_wx = null, tv_zfb = null, tv_ye = null;
    private TextView rechrge_tv_bi = null;
    private View view1 = null, view2 = null, view3 = null;
    private final boolean[] isWX = {true};
    private final boolean[] isZFB = {false};
    private final boolean[] isYE = {false};
    private int select = -1;
    private String buytype = "";

    private void showRechargeDialog() {

        View view = LayoutInflater.from(QXLiveSee2Activity.this).inflate(R.layout.dialog_recharge_view, null);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(QXLiveSee2Activity.this, R.style.recharge_dialog);
        dialog.setContentView(view);
        dialog.show();

        // 监听
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.dialog_recharge_view_wx://微信
                        if (isWX[0]) {

                        } else {
                            paymethod = "2";//支付方式1支付宝 2.微信  3余额
                            buytype = "1";
                            isWX[0] = true;
                            isZFB[0] = false;
                            isYE[0] = false;
                            tv_wx.setTextColor(getResources().getColor(R.color.color_titlebar_default));
                            view1.setVisibility(View.VISIBLE);
                            tv_zfb.setTextColor(getResources().getColor(R.color.black));
                            view2.setVisibility(View.GONE);
                            tv_ye.setTextColor(getResources().getColor(R.color.black));
                            view3.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.dialog_recharge_view_zfb://支付宝
                        if (isZFB[0]) {

                        } else {
                            paymethod = "1";//支付方式1支付宝 2.微信  3余额
                            buytype = "1";
                            isWX[0] = false;
                            isZFB[0] = true;
                            isYE[0] = false;
                            tv_wx.setTextColor(getResources().getColor(R.color.black));
                            view1.setVisibility(View.GONE);
                            tv_zfb.setTextColor(getResources().getColor(R.color.color_titlebar_default));
                            view2.setVisibility(View.VISIBLE);
                            tv_ye.setTextColor(getResources().getColor(R.color.black));
                            view3.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.dialog_recharge_view_ye://余额
                        if (isYE[0]) {

                        } else {
                            paymethod = "3";//支付方式1支付宝 2.微信  3余额
                            buytype = "2";
                            isWX[0] = false;
                            isZFB[0] = false;
                            isYE[0] = true;
                            tv_wx.setTextColor(getResources().getColor(R.color.black));
                            view1.setVisibility(View.GONE);
                            tv_zfb.setTextColor(getResources().getColor(R.color.black));
                            view2.setVisibility(View.GONE);
                            tv_ye.setTextColor(getResources().getColor(R.color.color_titlebar_default));
                            view3.setVisibility(View.VISIBLE);
                        }
                        break;

                    case R.id.dialog_rechrge_btn_confirm:
                        // 充值 确认
                        System.out.println("========================== 充值 确认 ");
                        if (select < 0) {
                            Toasts.show("请选择充值金额");
                        } else {
                            rechargeXingBi(select);
                            dialog.dismiss();
                        }

                        break;

                }

//                dialog.dismiss();
            }

        };

        ViewGroup mViewWX = (ViewGroup) view.findViewById(R.id.dialog_recharge_view_wx);
        tv_wx = (TextView) view.findViewById(R.id.dialog_recharge_name_wx);
        view1 = (View) view.findViewById(R.id.view_line1);

        ViewGroup mViewZFB = (ViewGroup) view.findViewById(R.id.dialog_recharge_view_zfb);
        tv_zfb = (TextView) view.findViewById(R.id.dialog_recharge_name_zfb);
        view2 = (View) view.findViewById(R.id.view_line2);

        ViewGroup mViewYE = (ViewGroup) view.findViewById(R.id.dialog_recharge_view_ye);
        tv_ye = (TextView) view.findViewById(R.id.dialog_recharge_name_ye);
        view3 = (View) view.findViewById(R.id.view_line3);

        gv_money = (MyGridView) view.findViewById(R.id.dialog_recharge_gv_money);
        gv_money.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < listRecharge.size(); i++) {
                    if (i == position) {
                        listRecharge.get(i).setSelect("1");
                    } else {
                        listRecharge.get(i).setSelect("0");
                    }
                    select = position;
                }
                mDialogRechargeAdapter.notifyDataSetChanged();
            }
        });

        rechrge_tv_bi = (TextView) view.findViewById(R.id.dialog_rechrge_tv_bi);
        rechrge_tv_bi.setText(liveDou);

        Button btn_confirm = (Button) view.findViewById(R.id.dialog_rechrge_btn_confirm);
        btn_confirm.setTextColor(getResources().getColor(R.color.white_F));
        mViewWX.setOnClickListener(listener);
        mViewZFB.setOnClickListener(listener);
        mViewYE.setOnClickListener(listener);
        btn_confirm.setOnClickListener(listener);

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        //获取充值数据
        initRechargeDatas();
    }

    /**
     * 添加礼物view,(考虑垃圾回收)
     */
    private View addGiftView() {
        View view = null;
        if (giftViewCollection.size() <= 0) {
            /*如果垃圾回收中没有view,则生成一个*/
            view = LayoutInflater.from(this).inflate(R.layout.item_gift, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            view.setLayoutParams(lp);
            llgiftcontent.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    giftViewCollection.add(view);
                }
            });
        } else {
            view = giftViewCollection.get(0);
            giftViewCollection.remove(view);
        }
        return view;
    }

    /**
     * 删除礼物view
     */
    private void removeGiftView(final int index) {
        final View removeView = llgiftcontent.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llgiftcontent.removeViewAt(index);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeView.startAnimation(outAnim);
            }
        });
    }

    /**
     * 显示礼物的方法
     */
    private void showGift(String tag, String userID, String msg, int imgID) {
        View giftView = llgiftcontent.findViewWithTag(tag);
        String name = null;
        String userHead = null;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (userID.equals(list.get(i).getId())) {
                    name = list.get(i).getUname();
                    userHead = list.get(i).getPic();
                    break;
                }
            }
        }
        if (giftView == null) {/*该用户不在礼物显示列表*/

            if ("送一架兰博基尼".equals(msg)) {
                giftAnmManager.showLamborghini();
                System.out.println("=======================礼物特效=========" + "法拉利");
            } else if ("送一艘轮船".equals(msg)) {
                giftAnmManager.showStreamShip();
                System.out.println("=======================礼物特效=========" + "游轮");
            } else if ("送了一个烟花".equals(msg)) {
                giftAnmManager.showFireWorks();
            } else if ("送一个火箭".equals(msg)) {
                giftAnmManager.showRocket();
            } else if ("送一个告白气球".equals(msg)) {
                giftAnmManager.showBalloon();
            } else if ("送一架飞机".equals(msg)) {
                giftAnmManager.showAirplane();
            } else if ("送一个么么哒".equals(msg)) {
                giftAnmManager.showMuah();
            } else if ("送一个睡美人".equals(msg)) {
                giftAnmManager.showBeauty();
            } else if ("送一杯啤酒".equals(msg)){
                giftAnmManager.showCheer();
            }else if ("送一座摩天轮".equals(msg)){
                giftAnmManager.showWheel();
            }else if ("送一阵流星雨".equals(msg)){
                giftAnmManager.showStar();
            }else {
                if (llgiftcontent.getChildCount() > 2) {/*如果正在显示的礼物的个数超过两个，那么就移除最后一次更新时间比较长的*/
                    View giftView1 = llgiftcontent.getChildAt(0);
                    CustomRoundView picTv1 = (CustomRoundView) giftView1.findViewById(R.id.crvheadimage);
                    long lastTime1 = (Long) picTv1.getTag();
                    View giftView2 = llgiftcontent.getChildAt(1);
                    CustomRoundView picTv2 = (CustomRoundView) giftView2.findViewById(R.id.crvheadimage);
                    long lastTime2 = (Long) picTv2.getTag();
                    if (lastTime1 > lastTime2) {/*如果第二个View显示的时间比较长*/
                        removeGiftView(1);
                    } else {/*如果第一个View显示的时间长*/
                        removeGiftView(0);
                    }
                }

                giftView = addGiftView();/*获取礼物的View的布局*/
                giftView.setTag(tag);/*设置view标识*/

                CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);
                if (!TextUtils.isEmpty(userHead)) {
                    if (userHead.startsWith("http")) {
                        ImageLoader.getInstance().displayImage(userHead, crvheadimage, ImageLoaderOptions.get_face_Options());
                    } else {
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + userHead, crvheadimage, ImageLoaderOptions.get_face_Options());
                    }
//			ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), img_head, ImageLoaderOptions.get_face_Options());
                    System.out.println("===========================礼物 User_Head = " + Constant.BASE_URL_IMG + userHead);
                }
                final TextView item_gift_tv_name = (TextView) giftView.findViewById(R.id.item_gift_tv_name);/*找到name*/
                item_gift_tv_name.setText(name);
                final TextView item_gift_tv_msg = (TextView) giftView.findViewById(R.id.item_gift_tv_msg);/*找到msg*/
                item_gift_tv_msg.setText(msg);
                final ImageView gift_img_gift = (ImageView) giftView.findViewById(R.id.gift_img_gift);/*找到gift*/
                gift_img_gift.setImageDrawable(getResources().getDrawable(imgID));
                final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
                giftNum.setText("x1");/*设置礼物数量*/
                crvheadimage.setTag(System.currentTimeMillis());/*设置时间标记*/
                giftNum.setTag(1);/*给数量控件设置标记*/

                llgiftcontent.addView(giftView);/*将礼物的View添加到礼物的ViewGroup中*/
                llgiftcontent.invalidate();/*刷新该view*/
                giftView.startAnimation(inAnim);/*开始执行显示礼物的动画*/
                inAnim.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        giftNumAnim.start(giftNum);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        } else {/*该用户在礼物显示列表*/
            if ("送一架兰博基尼".equals(msg)) {
                giftAnmManager.showLamborghini();
                System.out.println("=======================礼物特效=========" + "法拉利");
            } else if ("送一艘轮船".equals(msg)) {
                giftAnmManager.showStreamShip();
                System.out.println("=======================礼物特效=========" + "游轮");
            } else if ("送了一个烟花".equals(msg)) {
                giftAnmManager.showFireWorks();
            } else if ("送一个火箭".equals(msg)) {
                giftAnmManager.showRocket();
            } else if ("送一个告白气球".equals(msg)) {
                giftAnmManager.showBalloon();
            } else if ("送一架飞机".equals(msg)) {
                giftAnmManager.showAirplane();
            } else if ("送一个么么哒".equals(msg)) {
                giftAnmManager.showMuah();
            } else if ("送一个睡美人".equals(msg)) {
                giftAnmManager.showBeauty();
            } else if ("送一杯啤酒".equals(msg)){
                giftAnmManager.showCheer();
            }else if ("送一座摩天轮".equals(msg)){
                giftAnmManager.showWheel();
            }else if ("送一阵流星雨".equals(msg)){
                giftAnmManager.showStar();
            }else {
                CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);/*找到头像控件*/
                if (!TextUtils.isEmpty(userHead)) {
                    if (userHead.startsWith("http")) {
                        ImageLoader.getInstance().displayImage(userHead, crvheadimage, ImageLoaderOptions.get_face_Options());
                    } else {
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + userHead, crvheadimage, ImageLoaderOptions.get_face_Options());
                    }
//			ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), img_head, ImageLoaderOptions.get_face_Options());
                    System.out.println("===========================礼物 User_Head = " + Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head());
                }
                final TextView item_gift_tv_name = (TextView) giftView.findViewById(R.id.item_gift_tv_name);/*找到name*/
                item_gift_tv_name.setText(name);
                final TextView item_gift_tv_msg = (TextView) giftView.findViewById(R.id.item_gift_tv_msg);/*找到msg*/
                item_gift_tv_msg.setText(msg);
                final ImageView gift_img_gift = (ImageView) giftView.findViewById(R.id.gift_img_gift);/*找到gift*/
                gift_img_gift.setImageDrawable(getResources().getDrawable(imgID));
                MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
                int showNum = (Integer) giftNum.getTag() + 1;
                giftNum.setText("x" + showNum);
                giftNum.setTag(showNum);
                crvheadimage.setTag(System.currentTimeMillis());
                giftNumAnim.start(giftNum);
            }
        }
    }

    /**
     * 定时清除礼物
     */
    private void clearTiming() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int count = llgiftcontent.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = llgiftcontent.getChildAt(i);
                    CustomRoundView crvheadimage = (CustomRoundView) view.findViewById(R.id.crvheadimage);
                    long nowtime = System.currentTimeMillis();
                    long upTime = (Long) crvheadimage.getTag();
                    if ((nowtime - upTime) >= 3000) {
                        removeGiftView(i);
                        return;
                    }
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 3000);
    }

    /**
     * 数字放大动画
     */
    public class NumAnim {
        private Animator lastAnimator = null;

        public void start(View view) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.end();
                lastAnimator.cancel();
            }
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1.0f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            lastAnimator = animSet;
            animSet.setDuration(200);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(anim1, anim2);
            animSet.start();
        }
    }


    /**
     * 赠送礼物
     */
    private SendGiftBean mSendGiftBean;

    private void sendGift(String types, String num, String alljb) {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(QXLiveSee2Activity.this);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());//    用户id	uid
        params.put("zbuid", liveUId);//    直播用户id	zbuid  主播ID
        params.put("types", types);//    类型	types
        params.put("num", num);//    个数	num
        params.put("alljb", alljb);//    金币个数	alljb
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_SEND_GIFTS;
        System.out.println("===========================直播 送礼物 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(QXLiveSee2Activity.this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                btn_gifts.setEnabled(true);
                System.out.println("===========================直播 送礼物 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mSendGiftBean = new Gson().fromJson(response.toString(), SendGiftBean.class);
                    if (mSendGiftBean.getResult().equals("1")) {
                        final GiftMessage content1 = new GiftMessage(MyApplication.getInstance().getUid() + " " + giftList.get(select_gift).getMsg(), giftList.get(select_gift).getMsg());
                        LiveKit.sendMessage(content1);

                        showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                        liveDou = mSendGiftBean.getDoudou();
                        gift_tv_bi.setText(liveDou);
                    } else if (mSendGiftBean.getResult().equals("3")) {
                        Toasts.show(mSendGiftBean.getMessage());
                        startActivity(new Intent(QXLiveSee2Activity.this,MyWalletActivity.class));
                    } else {
                        Toasts.show(mSendGiftBean.getMessage());
                    }
                } else {
//                    showErrorDialog(mContext);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                btn_gifts.setEnabled(true);
                mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                btn_gifts.setEnabled(true);
                mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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
        });
    }

    private DialogRechargeAdapter mDialogRechargeAdapter;
    private RechargeJson mRechargeJson;
    private List<RechargeBean> listRecharge = new ArrayList<RechargeBean>();
    private MyGridView gv_money;

    /**
     * 初始化礼物数据
     */
    private void initRechargeDatas() {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(QXLiveSee2Activity.this);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
//        params.put("uid", MyApplication.getInstance().getUid());
//        params.put("status", "2");//+"/p/"+p
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_DOU_MONEY;
        System.out.println("===========================直播充值数据 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(QXLiveSee2Activity.this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================直播充值数据 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mRechargeJson = new Gson().fromJson(response.toString(), RechargeJson.class);
                    if (mRechargeJson.getResult().equals("1")) {
//                        listRecharge = mRechargeJson.getAndlist();
                        listRecharge.clear();
                        select = -1;
                        for (int i = 0; i < 4; i++) {
                            RechargeBean mRechargeBean = mRechargeJson.getAndlist().get(i);
                            listRecharge.add(mRechargeBean);
                        }
                        mDialogRechargeAdapter = new DialogRechargeAdapter(QXLiveSee2Activity.this, listRecharge);
                        gv_money.setAdapter(mDialogRechargeAdapter);
                        listRecharge.get(2).setSelect("1");
                        select = 2;
                        paymethod = "2";
                    } else {
                        Toasts.show(mRechargeJson.getMessage());
                    }
                } else {
                    mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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
        });
    }

    /**
     * 礼物充值
     */
    private PayMethodBean mPayMethodBean;
    private String paymethod = "2";

    private void rechargeXingBi(int select) {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(QXLiveSee2Activity.this);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());//    用户id	uid
        params.put("money", listRecharge.get(select).getMoney());//    钱	money
        params.put("num", listRecharge.get(select).getNum());//    个数	num
        params.put("buytype", buytype);             //充值方式  buytype

        final String url = Constant.BASE_URL + Constant.URL_USERAPI_BUY_DOU;
        System.out.println("===========================直播 充值 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(QXLiveSee2Activity.this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================直播 充值 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mPayMethodBean = new Gson().fromJson(response.toString(), PayMethodBean.class);
                    if ("1".equals(buytype)) {
                        if (mPayMethodBean.getResult().equals("1")) {
                            Toasts.show("支付跳转中....");
                            Intent intent = new Intent();
                            intent.putExtra("tag", "QXLiveSeeActivity");//tag 标识
                            intent.putExtra("paymethod", paymethod);//支付方式1支付宝 2.微信  3余额
                            if ("1".equals(paymethod)) {
                                isZFB[0] = false;
                                isWX[0] = true;
                            }

                            intent.putExtra("totalprice", mPayMethodBean.getMoney());//订单实际价格
                            intent.putExtra("orderno", mPayMethodBean.getId());//订单编号
                            intent.putExtra("url", mPayMethodBean.getUrl());//回调函数url
                            intent.putExtra("title", mPayMethodBean.getTitle());//订单标题
                            intent.putExtra("discription", mPayMethodBean.getDiscription());//订单描述
                            AppManager.getAppManager().startNextActivity(QXLiveSee2Activity.this, WXPayEntryActivity.class, intent);

                            if ("1".equals(MyApplication.getInstance().getIsRecharge())) {
                                liveDou = mPayMethodBean.getDoudou();
                                gift_tv_bi.setText(liveDou);
                            }
                        } else {
                            Toasts.show(mPayMethodBean.getMessage());
                        }
                    } else if ("2".equals(buytype)) {
                        if (mPayMethodBean.getResult().equals("1")) {
                            Toasts.show(mPayMethodBean.getMessage());
                            liveDou = mPayMethodBean.getDoudou();
                            gift_tv_bi.setText(liveDou);
                        } else {
                            Toasts.show(mPayMethodBean.getMessage());
                        }
                    }
                } else {
                    mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mAlertDialog = new AlertDialog.Builder(QXLiveSee2Activity.this)
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
        });
    }

    /**
     * 初始化星币
     */
    private InitJson mInitJson;

    private void initXingBi() {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(QXLiveSee2Activity.this);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());//    用户id	uid
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_SEL_DOU;
        System.out.println("===========================查看用户豆个数 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(QXLiveSee2Activity.this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================查看用户豆个数 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mInitJson = new Gson().fromJson(response.toString(), InitJson.class);
                    if (mInitJson.getResult().equals("1")) {
                        liveDou = mInitJson.getDoudou();
                        gift_tv_bi.setText(liveDou);
                    } else {
                        Toasts.show(mInitJson.getMessage());
                    }
                } else {
                    Toasts.show("星币余额无法刷新请重新进入页面");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                Toasts.show("星币余额无法刷新请重新进入页面");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                Toasts.show("星币余额无法刷新请重新进入页面");
            }
        });
    }


    /**
     * 排行
     */
    private void rankDatas() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(QXLiveSee2Activity.this);
            mProgressDialog.setMessage("加载中...");
//            mProgressDialog.show();
        }
        RequestParams params = new RequestParams();
        params.put("zbuid", liveUId);
        final String url = Constant.BASE_URL + Constant.URL_INDEX_ALLGIFTS;//
        System.out.println("===========================观看直播 排行url = " + url);
        System.out.println("===========================观看直播 params = " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (isRefresh) {
                    isRefresh = false;
                    mListView.onRefreshComplete();
                }
                System.out.println("===========================观看直播 排行 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mSeeQXliveRankJson = new Gson().fromJson(response.toString(), SeeQXliveRankJson.class);
                    if (mSeeQXliveRankJson.getResult().equals("1")) {
                        if (mSeeQXliveRankJson.getPaiming() == null || mSeeQXliveRankJson.getPaiming().size() == 0 || "".equals(mSeeQXliveRankJson.getPaiming())) {
                            mListView.setVisibility(View.GONE);
                            tv_noolder.setVisibility(View.VISIBLE);
                            mListView.onRefreshComplete();
                        } else {
                            mListView.setVisibility(View.VISIBLE);
                            tv_noolder.setVisibility(View.GONE);
                            mSeeQXliveRankAdapter = new SeeQXliveRankAdapter(QXLiveSee2Activity.this, mSeeQXliveRankJson.getPaiming());
                            System.out.println("===========================观看直播 排行size = " + mSeeQXliveRankJson.getPaiming().size());
                            mListView.setAdapter(mSeeQXliveRankAdapter);
                            mListView.onRefreshComplete();
                        }
                    } else {
                        Toasts.show(mSeeQXliveRankJson.getMessage());
                    }
                } else {
//                    showErrorDialog(mContext);
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
                    mListView.onRefreshComplete();
                }
//                showErrorDialog(mContext);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (isRefresh) {
                    isRefresh = false;
                    mListView.onRefreshComplete();
                }
//                showTimeoutDialog(mContext);
            }
        });
    }

    private void shareNum() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
//            mProgressDialog.show();
        }
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("spid", liveId);
        params.put("type", "2");//type  1视频   2直播
        final String url = Constant.BASE_URL + Constant.URL_INDEX_SHARE_NUM;
        System.out.println("===========================用户观看直播页 摄像机 分享成功前转发量 url ======= " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================用户观看直播页 摄像机 分享成功前转发量 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mShareBean = new Gson().fromJson(response.toString(), ShareBean.class);
                    if (mShareBean.getResult().equals("1")) {
//                        tv_sharenum.setText(mShareBean.getSharenum());
                        try {
                            downloadFile(Constant.BASE_URL_IMG + mShareBean.getPic());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        Toasts.show(mResultBean.getMessage());
                    } else {
//                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
//                    showErrorDialog(PlayVideoActivity.this);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
//                showTimeoutDialog(PlayVideoActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
//                showErrorDialog(PlayVideoActivity.this);
            }
        });
    }

    //分享成功后调用
    private void shareSuccess() {
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("spid", liveId);
        params.put("type", "2");//type  1视频   2直播 3资讯
        final String url = Constant.BASE_URL + Constant.URL_SHARE_SUCCESS;
        System.out.println("===========================用户观看直播页 摄像机 分享成功后转发量 url ======= " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                System.out.println("===========================用户观看直播页 摄像机 分享成功后转发量 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mShareBean = new Gson().fromJson(response.toString(), ShareBean.class);
                    if (mShareBean.getResult().equals("1")) {
//                        tv_sharenum.setText(mShareBean.getSharenum());
                        System.out.println("=================用户观看直播页 摄像机 分享成功后的结果 message==========" + mShareBean.getMessage());
//                        Toasts.show(mResultBean.getMessage());
                    } else {
//                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
//                    showErrorDialog(PlayVideoActivity.this);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                if (mProgressDialog != null) {
//                    mProgressDialog.dismiss();
//                }
//                showTimeoutDialog(PlayVideoActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
//                if (mProgressDialog != null) {
//                    mProgressDialog.dismiss();
//                }
//                showErrorDialog(PlayVideoActivity.this);
            }
        });
    }

    private Bitmap bitmap;
    private String img_base64;

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
                        .getPath() + "/temp.jpg";
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
                Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
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
                    System.out.println("===========下载成功 头像路径是" + tempPath);
                    bitmap = ImageTools.createThumbnail(tempPath, 200, 200);
                    img_base64 = ImageTools.bitmapToBase64(bitmap);

                    share(file);

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
                Toast.makeText(mContext, "分享失败", Toast.LENGTH_LONG).show();
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
}
