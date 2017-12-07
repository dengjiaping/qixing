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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.qixing.R;
import com.qixing.activity.BondListActivity;
import com.qixing.activity.MyWalletActivity;
import com.qixing.adapter.DialogGiftAdapter;
import com.qixing.adapter.DialogRechargeAdapter;
import com.qixing.adapter.QXliveHeadingAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.MyApplication;
import com.qixing.bean.GiftBean;
import com.qixing.bean.GiftSendBean;
import com.qixing.bean.InitJson;
import com.qixing.bean.PayMethodBean;
import com.qixing.bean.QXLiveInfoBean;
import com.qixing.bean.QXLiveInfoJson;
import com.qixing.bean.RechargeBean;
import com.qixing.bean.RechargeJson;
import com.qixing.bean.ResultBean;
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
import com.qixing.qxlive.rongyun.ui.fragment.TopBarFragment;
import com.qixing.qxlive.rongyun.ui.message.GiftMessage;
import com.qixing.qxlive.rongyun.widget.ChatListView;
import com.qixing.qxlive.rongyun.widget.InputPanel;
import com.qixing.qxlive.view.KSYFloatingPlayer;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.utlis.listener.AutoLoadListener;
import com.qixing.view.CircleImageView;
import com.qixing.view.HorizontalListView;
import com.qixing.view.MyGridView;
import com.qixing.view.TestLayout;
import com.qixing.view.imagecut.ImageTools;
import com.qixing.widget.Toasts;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import io.rong.imlib.model.UserInfo;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;

/**
 * Created by wicep on 2015/12/23.
 * 直播观看页面 手机
 */
public class QXLiveSeeActivity extends FragmentActivity implements View.OnClickListener, Handler.Callback {

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
    private BottomPanelFragment bottomPanel;
    private ImageView btn_close;//关闭
    private ImageView btn_rotate_icon;//前后摄像头
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
    private QXLiveInfoJson mHDLiveInfoJson;
    private List<QXLiveInfoBean> list = new ArrayList<QXLiveInfoBean>();
    private QXliveHeadingAdapter mHDliveHeadingAdapter;

    private TopBarFragment topBar;
    private CircleImageView img_head;
    private TextView tv_head;
    private HorizontalListView horizon_listview;
    private TextView tv_liveid, tv_livetime;
    private LinearLayout ll_bondlist;
    private Button btn_attention, btn_clean;

//    private TextureView mTextureView;
//    private SurfaceTexture mSurfaceTexture;
//    private Surface mSurface;
//    private boolean mPlayingCompleted = false;
//    private boolean mJumpToFloatingActivity = false;


    /**
     * umeng
     */
    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    private ImageView btn_share;//分享
    //    private ImageView btn_float;//悬浮窗
    private ShareBean mShareBean;


    private BSRGiftLayout giftLayout;
    private GiftAnmManager giftAnmManager;
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
    private Handler animHandler = new Handler();

    /**
     * 横竖屏
     */
    private TestLayout rl_surface;
    private int myVideoHeight, myVideoWidth;
    /**
     * 设置窗口模式下videoview的高度
     */
    private int videoHeight;
    /**
     * 设置窗口模式下的videoview的宽度
     */
    private int videoWidth;


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
            System.out.println("===========================播放完成时会发出此回调 QXLiveSeeActivity ==== ");
//            mPlayingCompleted = true;
//            if (!mJumpToFloatingActivity)
//                QXLiveSeeActivity.this.finish();

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

//    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
//        @Override
//        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
//            mSurfaceTexture = surfaceTexture;
//            if (mSurface == null) {
//                mSurface = new Surface(mSurfaceTexture);
//
//                if (KSYFloatingPlayer.getInstance().getKSYMediaPlayer() != null)
//                    KSYFloatingPlayer.getInstance().getKSYMediaPlayer().setSurface(mSurface);
//            }
//        }
//
//        @Override
//        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
//
//        }
//
//        @Override
//        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//            if (KSYFloatingPlayer.getInstance().getKSYMediaPlayer() != null)
//                KSYFloatingPlayer.getInstance().getKSYMediaPlayer().setSurface(null);
//            if (mSurface != null) {
//                mSurface.release();
//                mSurface = null;
//            }
//
//            mSurfaceTexture = null;
//
//            return true;
//        }
//
//        @Override
//        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//
//        }
//    };

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

        setContentView(R.layout.activity_qxlive_see);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕唤醒
//        mContext = this;
        initStatusbar(this, R.color.black);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        LiveKit.setCurrentUser(FakeServer.getUserInfo());
        LiveKit.addEventHandler(handler);//融云聊天
        initView();
        startLiveShow();//开始观看直播
        initUMeng();//初始化友盟view
        initGigt();//初始化刷礼物view

    }

    private void initView() {
        background = (ViewGroup) findViewById(R.id.background);
        chatListView = (ChatListView) findViewById(R.id.chat_listview);//融云聊天记录layout
        /**
         * 底部+背景点击事件
         * */
        bottomPanel = (BottomPanelFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_bar);
//        bottomPanel = (BottomPanelFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_bar);
        btn_close = (ImageView) bottomPanel.getView().findViewById(R.id.btn_close);
        btn_rotate_icon = (ImageView) bottomPanel.getView().findViewById(R.id.btn_rotate_icon);
        btn_rotate_icon.setVisibility(View.GONE);
//        heartLayout = (HeartLayout) findViewById(R.id.heart_layout);//点赞layout
        surfaceView = (SurfaceView) findViewById(R.id.player_surface);
        img_loading = (ImageView) findViewById(R.id.player_img_loading);

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

        giftLayout = (BSRGiftLayout) findViewById(R.id.gift_layout);
        giftAnmManager = new GiftAnmManager(giftLayout, this);

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

        rl_surface = (TestLayout) findViewById(R.id.qxlive_see_ll_surface);

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
        img_head = (CircleImageView) topBar.getView().findViewById(R.id.top_bar_img_head);
        tv_head = (TextView) topBar.getView().findViewById(R.id.top_bar_tv_head);
        horizon_listview = (HorizontalListView) topBar.getView().findViewById(R.id.top_bar_horizon_listview);
        tv_liveid = (TextView) topBar.getView().findViewById(R.id.top_bar_tv_liveid);
        tv_livetime = (TextView) topBar.getView().findViewById(R.id.top_bar_tv_livetime);
        ll_bondlist = (LinearLayout) topBar.getView().findViewById(R.id.top_bar_ll_bondlist);
        ll_bondlist.setOnClickListener(this);
        btn_attention = (Button) topBar.getView().findViewById(R.id.top_bar_btn_attention);
        btn_attention.setOnClickListener(this);
        btn_clean = (Button) topBar.getView().findViewById(R.id.top_bar_btn_clean);
        btn_clean.setOnClickListener(this);
//        topBar.setMenuVisibility(false);
//
//        mTextureView = (TextureView) findViewById(R.id.player_texture);
//        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);

    }


    /**
     * 初始化友盟view
     */
    private void initUMeng() {

        btn_share = (ImageView) bottomPanel.getView().findViewById(R.id.btn_share);
        btn_share.setOnClickListener(this);

        mShareListener = new CustomShareListener(QXLiveSeeActivity.this);

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
//        mShareAction = new ShareAction(QXLiveSeeActivity.this).setDisplayList(
//                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                .withTitle("七星时代")
//                .withTargetUrl(shareUrl)
//                .withText("来自七星时代的分享")
//                .setCallback(mShareListener);
//        System.out.println("=========================== 分享连接 ====" + shareUrl);
    }

    private void share(File file) {
        String shareUrl = Constant.BASE_URL + Constant.URL_USERAPI_FXZBXX + "/id/" + roomId + "/type/2";//type 1视频 2 直播
//                    UMImage image = new UMImage(PlayVideoActivity.this, Constant.BASE_URL_IMG+mShareBean.getPic());//网络图片
        UMImage image = new UMImage(QXLiveSeeActivity.this, file);//本地文件
        mShareAction = new ShareAction(QXLiveSeeActivity.this).setDisplayList(
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

        private WeakReference<QXLiveSeeActivity> mActivity;

        private CustomShareListener(QXLiveSeeActivity activity) {
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

//        btn_float= (ImageView) bottomPanel.getView().findViewById(R.id.btn_float);
//        btn_float.setOnClickListener(this);
//        btn_float.setVisibility(View.VISIBLE);

        llgiftcontent = (LinearLayout) findViewById(R.id.llgiftcontent);

        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_out);
        giftNumAnim = new NumAnim();
        clearTiming();

        liveDou = getIntent().getStringExtra(QXLiveSeeActivity.LIVE_DOU);
    }


    private String userID;
    private String userGiftInfo;

    private List<GiftSendBean> sendLists=new ArrayList<>();
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
            System.out.println("===========================消息记录 message.getTarketId() ==== " + zbid);

            // 此处输出判断是否是文字消息，并输出，其他消息同理。
            if (message.getContent() instanceof GiftMessage) { //instanceof 测试它左边的对象是否是它右边的类的实例

            }
            if ("RC:GiftMsg".equals(message.getObjectName())) {
                GiftMessage msg = (GiftMessage) message.getContent();
                String content = msg.getType();
                userID = (String) msg.getUserInfo().getUserId();
                userGiftInfo = content;
                GiftSendBean sendBean=new GiftSendBean();
                if ("送一架兰博基尼".equals(userGiftInfo)) {
                    sendBean.setUid(userID);
                    sendBean.setType(userGiftInfo);
                    sendBean.setTimes(6600 + "");
                    sendLists.add(sendBean);
                } else if ("送一艘轮船".equals(userGiftInfo)) {
                    sendBean.setUid(userID);
                    sendBean.setType(userGiftInfo);
                    sendBean.setTimes(6000 + "");
                    sendLists.add(sendBean);
                } else if ("送了一个烟花".equals(userGiftInfo)) {
                    sendBean.setUid(userID);
                    sendBean.setType(userGiftInfo);
                    sendBean.setTimes(6000 + "");
                    sendLists.add(sendBean);
                } else if ("送一个火箭".equals(userGiftInfo)) {
                    sendBean.setUid(userID);
                    sendBean.setType(userGiftInfo);
                    sendBean.setTimes(10000 + "");
                    sendLists.add(sendBean);
                } else if ("送一个告白气球".equals(userGiftInfo)) {
                    sendBean.setUid(userID);
                    sendBean.setType(userGiftInfo);
                    sendBean.setTimes(9600 + "");
                } else if ("送一架飞机".equals(userGiftInfo)) {
                    sendBean.setUid(userID);
                    sendBean.setType(userGiftInfo);
                    sendBean.setTimes(8000 + "");
                    sendLists.add(sendBean);
                } else if ("送一个么么哒".equals(userGiftInfo)) {
                    sendBean.setUid(userID);
                    sendBean.setType(userGiftInfo);
                    sendBean.setTimes(6600 + "");
                    sendLists.add(sendBean);
                } else if ("送一个睡美人".equals(userGiftInfo)) {
                    sendBean.setUid(userID);
                    sendBean.setType(userGiftInfo);
                    sendBean.setTimes(10000 + "");
                    sendLists.add(sendBean);
                } else if ("送一杯啤酒".equals(userGiftInfo)) {
                    sendBean.setUid(userID);
                    sendBean.setType(userGiftInfo);
                    sendBean.setTimes(8000 + "");
                    sendLists.add(sendBean);
                } else if ("送一座摩天轮".equals(userGiftInfo)) {
                    sendBean.setUid(userID);
                    sendBean.setType(userGiftInfo);
                    sendBean.setTimes(9000 + "");
                    sendLists.add(sendBean);
                } else if ("送一阵流星雨".equals(userGiftInfo)) {
                    sendBean.setUid(userID);
                    sendBean.setType(userGiftInfo);
                    sendBean.setTimes(12000 + "");
                    sendLists.add(sendBean);
                }
                System.out.println("===========================消息记录 msg.getContent() ==== " + content + ",userID = " + userID + ",userGiftInfo = " + userGiftInfo);
                msg.setContent(userGiftInfo);
                MyGiftRunnable myGiftRunnable = new MyGiftRunnable();
                Thread mThread = new Thread(myGiftRunnable);
                mThread.run();
//                showGift(userID + userGiftInfo, userID, userGiftInfo, GiftDateUtlis.getGiftImgID(userGiftInfo));
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
            if (!(sendLists.size() == 0 || sendLists == null || "".equals(sendLists))) {
                System.out.println("==========================消息记录 刷礼物 size=========" + sendLists.size());
                for (int i = 0; i < sendLists.size(); i++) {
                    String uid = sendLists.get(i).getUid();
                    String giftInfo = sendLists.get(i).getType();
                    try {
                        if (giftLayout.getChildCount()==0){
                            showGift(uid + giftInfo, uid, giftInfo, GiftDateUtlis.getGiftImgID(giftInfo));
                        }else{
                            showGift(uid + giftInfo, uid, giftInfo, GiftDateUtlis.getGiftImgID(giftInfo));
                            Thread.sleep(Long.parseLong(sendLists.get(i).getTimes()));
                        }
                        sendLists.remove(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (sendLists.size() == 0 || sendLists == null || "".equals(sendLists)){
                showGift(userID + userGiftInfo, userID, userGiftInfo, GiftDateUtlis.getGiftImgID(userGiftInfo));
            }
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

        liveUrl = getIntent().getStringExtra(QXLiveSeeActivity.LIVE_URL);
        liveId = getIntent().getStringExtra(QXLiveSeeActivity.LIVE_ID);
        liveGZ = getIntent().getStringExtra(QXLiveSeeActivity.LIVE_GZ);
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
//        if("1".equals(liveGZ)){
//            btn_attention.setVisibility(View.GONE);
//            btn_clean.setVisibility(View.VISIBLE);
//        }else{
//            btn_attention.setVisibility(View.VISIBLE);
//            btn_clean.setVisibility(View.GONE);
//        }
        btn_attention.setVisibility(View.GONE);
        btn_clean.setVisibility(View.GONE);

        /**
         * 播放完成时会发出此回调
         * */
//        ksyMediaPlayer.setOnCompletionListener(onCompletionListener);
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
                Toast.makeText(QXLiveSeeActivity.this, "聊天室加入失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
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
//        KSYFloatingPlayer.getInstance().getKSYMediaPlayer().setOnPreparedListener(onPreparedListener);
//        KSYFloatingPlayer.getInstance().getKSYMediaPlayer().setOnErrorListener(onErrorListener);
//        KSYFloatingPlayer.getInstance().getKSYMediaPlayer().setOnInfoListener(onInfoListener);
//        KSYFloatingPlayer.getInstance().getKSYMediaPlayer().setOnCompletionListener(onCompletionListener);
//        KSYFloatingPlayer.getInstance().getKSYMediaPlayer().setScreenOnWhilePlaying(true);
//        KSYFloatingPlayer.getInstance().getKSYMediaPlayer().setBufferTimeMax(5);
//        KSYFloatingPlayer.getInstance().getKSYMediaPlayer().setTimeout(20, 100);

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
        Intent intent = null;
        switch (v.getId()) {
            case R.id.background://点击背景
                bottomPanel.onBackAction();
                break;
            case R.id.btn_close://关闭直播
                onBackoffClick();
                break;
            case R.id.btn_share://分享
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
            case R.id.top_bar_ll_bondlist:
                intent = new Intent();
                intent.putExtra("zbid", mHDLiveInfoJson.getZbinfo().getId());
                AppManager.getAppManager().startNextActivity(QXLiveSeeActivity.this, BondListActivity.class, intent);
                break;
//            case R.id.btn_float:
//                mJumpToFloatingActivity = true;
//                Intent intent = new Intent(QXLiveSeeActivity.this, FloatingPlayingActivity.class);
//                startActivity(intent);
//                break;
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

    /**
     * 用户退出直播间Dialog
     */
    private void onBackoffClick() {
        mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
                .setTitle("提示")
                .setMessage("退出直播？")
                .setPositiveButton(R.string.dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                mAlertDialog.dismiss();
                                //退出聊天室
                                quit();
                            }
                        })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog.dismiss();
                    }
                }).create();
        mAlertDialog.setCanceledOnTouchOutside(false);//点击外部不可消失dialog
        mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                //屏蔽返回键 不可消失dialog
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                    mAlertDialog.dismiss();
//                    UserBuyActivity.this.finish();
//                    quit();
                }
                return false;
            }
        });
        mAlertDialog.show();
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
        mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
                .setTitle("提示")
                .setMessage("您所观看的直播已经结束，感谢您的观看")
                .setPositiveButton(R.string.dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                mAlertDialog.dismiss();
                                //退出聊天室
                                quit();
                            }
                        }).create();
        mAlertDialog.setCanceledOnTouchOutside(false);//点击外部不可消失dialog
        mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                //屏蔽返回键 不可消失dialog
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                    mAlertDialog.dismiss();
//                    UserBuyActivity.this.finish();
                    quit();
                }
                return false;
            }
        });
        mAlertDialog.show();
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
                Toast.makeText(QXLiveSeeActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
//                Toast.makeText(QXLiveSeeActivity.this, "退出失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
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
        QXLiveSeeActivity.this.finish();
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
//        if (mPlayingCompleted)
//            this.finish();

        if (ksyMediaPlayer != null) {
            if (!ksyMediaPlayer.isPlaying()) {
                ksyMediaPlayer.start();//开始
            }
        } else {
            startLiveShow();
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
                    } else {
                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
                    mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
                mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
                mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
                    mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
                mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
                mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_SELZBINFO;//
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
                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mHDLiveInfoJson.getZbinfo().getZb_pic(), img_head, ImageLoaderOptions.getOptions());
                            tv_head.setText(mHDLiveInfoJson.getZbinfo().getSee_num() + "人");
                            tv_liveid.setText("直播号:" + mHDLiveInfoJson.getZbinfo().getZbno());
                            tv_livetime.setText(mHDLiveInfoJson.getZbinfo().getTimes());//DateUtils.TimeStamp2DateYYYYMMDD(mHDLiveInfoJson.getZbinfo().getTimes())
                        }
                        if (mHDLiveInfoJson.getUserlist() == null || mHDLiveInfoJson.getUserlist().size() == 0 || "".equals(mHDLiveInfoJson.getUserlist())) {
                            list.clear();
                            if (mHDliveHeadingAdapter != null) {
                                mHDliveHeadingAdapter.notifyDataSetChanged();
                            }
                        } else {
                            list.clear();
                            list.addAll(mHDLiveInfoJson.getUserlist());
                            mHDliveHeadingAdapter = new QXliveHeadingAdapter(QXLiveSeeActivity.this, list);
                            horizon_listview.setAdapter(mHDliveHeadingAdapter);
                            mHDliveHeadingAdapter.notifyDataSetChanged();
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
    private int count = 0;
    private long time = 0;

    private void showGiftDialog() {
        View view = LayoutInflater.from(QXLiveSeeActivity.this).inflate(R.layout.dialog_gift_view, null);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(QXLiveSeeActivity.this, R.style.common_dialog);
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
                            switch (giftList.get(select_gift).getTypes()) {
                                case "4":
                                case "5":
                                case "6":
                                case "7":
                                case "8":
                                case "11":
                                case "12":
                                case "13":
                                case "14":
                                case "15":
                                case "16":
                                    count++;
                                    btn_gifts.setEnabled(false);
                                    sendGift(giftList.get(select_gift).getTypes(), "1", giftList.get(select_gift).getXingbi());
                                    break;
                                default:
                                    btn_gifts.setEnabled(false);
                                    sendGift(giftList.get(select_gift).getTypes(), "1", giftList.get(select_gift).getXingbi());
                                    break;
                            }
//                            showGift(MyApplication.getInstance().getUid()+giftList.get(select_gift).getMsg(),MyApplication.getInstance().getUid(),giftList.get(select_gift).getMsg(),giftList.get(select_gift).getImgID());
                        }
//                        dialog.dismiss();
                        break;
                    case R.id.dialog_gift_btn_recharge:
                        System.out.println("=========================== 星币 充值");
                        startActivity(new Intent(QXLiveSeeActivity.this, MyWalletActivity.class));
                        break;
                }
            }

        };
        ViewGroup mViewgift1 = (ViewGroup) view.findViewById(R.id.view_gift_weixin);
        ViewGroup mViewgift2 = (ViewGroup) view.findViewById(R.id.view_gift_pengyou);
        btn_gifts = (Button) view.findViewById(R.id.dialog_gift_btn_gifts);
        btn_recharge = (Button) view.findViewById(R.id.dialog_gift_btn_recharge);
        btn_recharge.setOnClickListener(listener);

        btn_gifts.setTextColor(getResources().getColor(R.color.white_F));
        mViewgift1.setOnClickListener(listener);
        mViewgift2.setOnClickListener(listener);
        btn_gifts.setOnClickListener(listener);

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
        mDialogGiftAdapter = new DialogGiftAdapter(QXLiveSeeActivity.this, giftList);
        gv_gift.setAdapter(mDialogGiftAdapter);

        gv_gift.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {

            @Override
            public void refresh() {
                select_gift = 1;
                giftList.clear();
                giftList = GiftDateUtlis.initGiftDate();
                mDialogGiftAdapter = new DialogGiftAdapter(QXLiveSeeActivity.this, giftList);
                gv_gift.setAdapter(mDialogGiftAdapter);
            }

            @Override
            public void execute() {
                select_gift = -1;
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

        View view = LayoutInflater.from(QXLiveSeeActivity.this).inflate(R.layout.dialog_recharge_view, null);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(QXLiveSeeActivity.this, R.style.recharge_dialog);
        dialog.setContentView(view);
        dialog.show();

        // 监听
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.dialog_recharge_view_wx:
                        if (isWX[0]) {

                        } else {
                            paymethod = "2";//支付方式1支付宝 2.微信  3余额
                            buytype = "1";
                            isWX[0] = true;
                            isYE[0] = false;
                            isZFB[0] = false;
                            tv_wx.setTextColor(getResources().getColor(R.color.color_titlebar_default));
                            view1.setVisibility(View.VISIBLE);
                            tv_zfb.setTextColor(getResources().getColor(R.color.black));
                            view2.setVisibility(View.GONE);
                            tv_ye.setTextColor(getResources().getColor(R.color.black));
                            view3.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.dialog_recharge_view_zfb:
                        if (isZFB[0]) {

                        } else {
                            paymethod = "1";//支付方式1支付宝 2.微信  3余额
                            buytype = "1";
                            isWX[0] = false;
                            isYE[0] = false;
                            isZFB[0] = true;
                            tv_wx.setTextColor(getResources().getColor(R.color.black));
                            view1.setVisibility(View.GONE);
                            tv_ye.setTextColor(getResources().getColor(R.color.black));
                            view3.setVisibility(View.GONE);
                            tv_zfb.setTextColor(getResources().getColor(R.color.color_titlebar_default));
                            view2.setVisibility(View.VISIBLE);
                        }
                        break;

                    case R.id.dialog_recharge_view_ye:
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
        view3 = view.findViewById(R.id.view_line3);

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
            } else if ("送一杯啤酒".equals(msg)) {
                giftAnmManager.showCheer();
            } else if ("送一座摩天轮".equals(msg)) {
                giftAnmManager.showWheel();
            } else if ("送一阵流星雨".equals(msg)) {
                giftAnmManager.showStar();
            } else {
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
            } else if ("送一杯啤酒".equals(msg)) {
                giftAnmManager.showCheer();
            } else if ("送一座摩天轮".equals(msg)) {
                giftAnmManager.showWheel();
            } else if ("送一阵流星雨".equals(msg)) {
                giftAnmManager.showStar();
            } else {
                CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);/*找到头像控件*/
                if (!TextUtils.isEmpty(userHead)) {
                    if (userHead.startsWith("http")) {
                        ImageLoader.getInstance().displayImage(userHead, crvheadimage, ImageLoaderOptions.get_face_Options());
                    } else {
                        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + userHead, crvheadimage, ImageLoaderOptions.get_face_Options());
                    }
//			ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), img_head, ImageLoaderOptions.get_face_Options());
                    System.out.println("===========================个人中心 User_Head = " + Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head());
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

    private void sendGift(final String types, String num, final String alljb) {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(QXLiveSeeActivity.this);
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
        System.out.println("===========================直播 送礼物 types ===== " + types);
        System.out.println("===========================直播 送礼物 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(QXLiveSeeActivity.this, url, params, new JsonHttpResponseHandler() {

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
                        final GiftMessage content1 = new GiftMessage(giftList.get(select_gift).getMsg(), giftList.get(select_gift).getMsg());
                        LiveKit.sendMessage(content1);

                        switch (types) {
                            case "4":
                                time = 6000;
                                if (count == 1) {
                                    System.out.println("================================type 4 count========="+count);
                                    showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                } else {
                                    try {
                                        new Thread().join(time);
                                        count = 0;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "5":
                                time = 10000;
                                if (count == 1) {
                                    showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                } else {
                                    System.out.println("================================type 5 count========="+count);
                                    try {
                                        new Thread().join(time);
                                        count = 0;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "6":
                                time = 9600;
                                if (count == 1) {
                                    showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                } else {
                                    try {
                                        new Thread().join(time);
                                        count = 0;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "7":
                                time = 8000;
                                if (count == 1) {
                                    showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                } else {
                                    try {
                                        new Thread().join(time);
                                        count = 0;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "8":
                                time = 9000;
                                if (count == 1) {
                                    showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                } else {
                                    try {
                                        new Thread().join(time);
                                        count = 0;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "11":
                                time = 8000;
                                if (count == 1) {
                                    showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                } else {
                                    try {
                                        new Thread().join(time);
                                        count = 0;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "12":
                                time = 6600;
                                if (count == 1) {
                                    showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                } else {
                                    try {
                                        new Thread().join(time);
                                        count = 0;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "13":
                                time = 12000;
                                if (count == 1) {
                                    showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                } else {
                                    try {
                                        new Thread().join(time);
                                        count = 0;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "14":
                                time = 6600;
                                if (count == 1) {
                                    showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                } else {
                                    try {
                                        new Thread().join(time);
                                        count = 0;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "15":
                                time = 6000;
                                if (count == 1) {
                                    showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                } else {
                                    try {
                                        new Thread().join(time);
                                        count = 0;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "16":
                                time = 10000;
                                if (count == 1) {
                                    showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                } else {
                                    try {
                                        new Thread().join(time);
                                        count = 0;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            default:
                                showGift(MyApplication.getInstance().getUid() + giftList.get(select_gift).getMsg(), MyApplication.getInstance().getUid(), giftList.get(select_gift).getMsg(), giftList.get(select_gift).getImgID());
                                break;
                        }

//                            case 6:
//                                giftAnmManager.showCarOne();
//                                System.out.println("==================礼物特效=========="+6);
//                                break;
//                            case 7:
//                                giftAnmManager.showShip();
//                                System.out.println("==================礼物特效=========="+7);
//                                break;

                        liveDou = mSendGiftBean.getDoudou();
                        gift_tv_bi.setText(liveDou);
                    } else if (mSendGiftBean.getResult().equals("3")) {
                        Toasts.show(mSendGiftBean.getMessage());
                        startActivity(new Intent(QXLiveSeeActivity.this, MyWalletActivity.class));
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
                mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
                mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
            mProgressDialog = new ProgressDialog(QXLiveSeeActivity.this);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
//        params.put("uid", MyApplication.getInstance().getUid());
//        params.put("status", "2");//+"/p/"+p
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_DOU_MONEY;
        System.out.println("===========================直播充值数据 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(QXLiveSeeActivity.this, url, params, new JsonHttpResponseHandler() {

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
                        mDialogRechargeAdapter = new DialogRechargeAdapter(QXLiveSeeActivity.this, listRecharge);
                        gv_money.setAdapter(mDialogRechargeAdapter);
                        listRecharge.get(2).setSelect("1");
                        select = 2;
                        paymethod = "2";
                    } else {
                        Toasts.show(mRechargeJson.getMessage());
                    }
                } else {
                    mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
                mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
                mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
            mProgressDialog = new ProgressDialog(QXLiveSeeActivity.this);
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
        mAsyncHttpClient.post(QXLiveSeeActivity.this, url, params, new JsonHttpResponseHandler() {

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
                            AppManager.getAppManager().startNextActivity(QXLiveSeeActivity.this, WXPayEntryActivity.class, intent);

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
                    mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
                mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
                mAlertDialog = new AlertDialog.Builder(QXLiveSeeActivity.this)
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
            mProgressDialog = new ProgressDialog(QXLiveSeeActivity.this);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());//    用户id	uid
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_SEL_DOU;
        System.out.println("===========================查看用户豆个数 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(QXLiveSeeActivity.this, url, params, new JsonHttpResponseHandler() {

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
        System.out.println("===========================用户观看直播页 手机 分享成功前转发量 url ======= " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================用户观看直播页 手机 分享成功前转发量 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mShareBean = new Gson().fromJson(response.toString(), ShareBean.class);
                    if (mShareBean.getResult().equals("1")) {
//                        tv_sharenum.setText(mShareBean.getSharenum());
//http://app.qixingshidai.com/Public/Uploads/zhibopic/148964351886.png
                        try {
//                            System.out.println("=====================分享pic=============="+Constant.BASE_URL_IMG+mShareBean.getPic());
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
        System.out.println("===========================用户观看直播页 手机 分享成功后 url ======= " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                System.out.println("===========================用户观看直播页 手机 分享成功后 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mShareBean = new Gson().fromJson(response.toString(), ShareBean.class);
                    if (mShareBean.getResult().equals("1")) {
//                        tv_sharenum.setText(mShareBean.getSharenum());
                        System.out.println("=================用户观看直播页 手机 分享成功后的结果 message==========" + mShareBean.getMessage());
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

//                    share(file);
//                    Toast.makeText(mContext, "下载成功\n" + tempPath,Toast.LENGTH_LONG).show();
                    String shareUrl = Constant.BASE_URL + Constant.URL_USERAPI_FXZBXX + "/id/" + roomId + "/type/2";//type 1视频 2 直播
//                    UMImage image = new UMImage(PlayVideoActivity.this, Constant.BASE_URL_IMG+mShareBean.getPic());//网络图片
                    UMImage image = new UMImage(QXLiveSeeActivity.this, file);//本地文件
                    mShareAction = new ShareAction(QXLiveSeeActivity.this).setDisplayList(
                            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                            .withTitle(mShareBean.getTitle())
                            .withTargetUrl(shareUrl)
                            .withMedia(image)
                            .withText(mShareBean.getContent())
                            .setCallback(mShareListener);
                    System.out.println("=========================== 分享连接 ====" + shareUrl);
                    System.out.println("=========================== 分享图片连接 ====" + Constant.BASE_URL_IMG + mShareBean.getPic());
                    mShareAction.open();

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
