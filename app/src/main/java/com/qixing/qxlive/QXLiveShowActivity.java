package com.qixing.qxlive;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.qixing.R;
import com.qixing.activity.BondListActivity;
import com.qixing.activity.child.ImageCutActivity;
import com.qixing.adapter.QXliveHeadingAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.MyApplication;
import com.qixing.bean.GiftSendBean;
import com.qixing.bean.QXLiveInfoBean;
import com.qixing.bean.QXLiveInfoJson;
import com.qixing.bean.ResultBean;
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
import com.qixing.utlis.ColorUtils;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.CircleImageView;
import com.qixing.view.HorizontalListView;
import com.qixing.view.imagecut.ImageTools;
import com.qixing.widget.Toasts;
import com.ksyun.media.streamer.capture.camera.CameraTouchHelper;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.OnAudioRawDataListener;
import com.ksyun.media.streamer.kit.OnPreviewFrameListener;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.logstats.StatsLogReport;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import me.drakeet.materialdialog.MaterialDialog;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by lenovo on 2016/10/18.
 * 红道直播主播页面
 */

public class QXLiveShowActivity extends FragmentActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener, Handler.Callback {

    private static final String TAG = "HDMyLiveActivity";

    private GLSurfaceView mCameraPreviewView;
    //private TextureView mCameraPreviewView;
    private CameraHintView mCameraHintView;
//    private Chronometer mChronometer;//开播时间
//    private View mDeleteView;
//    private View mSwitchCameraView;
//    private View mFlashView;
//    private TextView mShootingText;
//    private CheckBox mWaterMarkCheckBox;
//    private CheckBox mBeautyCheckBox;
//    private CheckBox mReverbCheckBox;
//    private CheckBox mAudioPreviewCheckBox;
//    private CheckBox mBgmCheckBox;
//    private CheckBox mMuteCheckBox;
//    private CheckBox mAudioOnlyCheckBox;
//    private CheckBox mFrontMirrorCheckBox;
//    private TextView mUrlTextView;
//    private TextView mDebugInfoTextView;

    private ButtonObserver mObserverButton;
    private CheckBoxObserver mCheckBoxObserver;

    private KSYStreamer mStreamer;
    private Handler mMainHandler;
    private Timer mTimer;

    private boolean mAutoStart;
    private boolean mIsLandscape;
    private boolean mPrintDebugInfo = false;
    private boolean mRecording = false;
    private boolean isFlashOpened = false;
    private String mUrl;
    private String mDebugInfo = "";
    private String mBgmPath = "/sdcard/test.mp3";
    private String mLogoPath = "assets://test.png";
//        private String mLogoPath = "file:///sdcard/test.png";

    private final static int PERMISSION_REQUEST_CAMERA_AUDIOREC = 1;
    private static final String START_STRING = "开始直播";
    private static final String STOP_STRING = "停止直播";

    public final static String URL = "url";
    public final static String FRAME_RATE = "framerate";
    public final static String VIDEO_BITRATE = "video_bitrate";
    public final static String AUDIO_BITRATE = "audio_bitrate";
    public final static String VIDEO_RESOLUTION = "video_resolution";
    public final static String LANDSCAPE = "landscape";
    public final static String ENCDODE_METHOD = "encode_method";
    public final static String START_ATUO = "start_auto";
    public static final String SHOW_DEBUGINFO = "show_debuginfo";

    public static void startActivity(Context context, int fromType,
                                     String rtmpUrl, int frameRate,
                                     int videoBitrate, int audioBitrate,
                                     int videoResolution, boolean isLandscape,
                                     int encodeMethod, boolean startAuto,
                                     boolean showDebugInfo) {
        Intent intent = new Intent(context, QXLiveShowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type", fromType);
        intent.putExtra(URL, rtmpUrl);
        intent.putExtra(FRAME_RATE, frameRate);
        intent.putExtra(VIDEO_BITRATE, videoBitrate);
        intent.putExtra(AUDIO_BITRATE, audioBitrate);
        intent.putExtra(VIDEO_RESOLUTION, videoResolution);
        intent.putExtra(LANDSCAPE, isLandscape);
        intent.putExtra(ENCDODE_METHOD, encodeMethod);
        intent.putExtra(START_ATUO, startAuto);
        intent.putExtra(SHOW_DEBUGINFO, showDebugInfo);
        context.startActivity(intent);
    }

    /**
     * 直播准备信息
     */
    private LinearLayout layout_live_info;
    private ImageView img_head;
    private EditText edit_name;
    private Button btn_golive;
    private boolean is_live_info = false;

    private ViewGroup background;
    private BottomPanelFragment bottomPanel;
    private ImageView img_close, img_switch_cam, btn_input, btn_beauty;
    private CheckBox checkBox_recommend;
    private boolean is_beauty = false;

    private String uplive_url;

    /**
     * 融云
     */
    private Random random = new Random();
    private Handler handler = new Handler(this);
    private ChatListView chatListView;//聊天记录
    private ChatListAdapter chatListAdapter; //聊天
    private String roomId;

    /**
     * 音量
     */
    private AudioManager mAudioManager;

    /**
     * 选择封面
     */
    private ArrayList<String> mSelectPath;
    private String path;
    private Bitmap bitmap;
    private String img_base64;
    private String str_title;

    /**
     * 直播 ID
     */
    private String zbid;

    public static final int RESULT_OK = -1;
    private static final int REQUEST_IMAGE = 1;

    /**
     * 直播信息 头像
     */
    private LinearLayout fragment_topbar_ll1, fragment_topbar_ll2;
    private QXLiveInfoJson mHDLiveInfoJson;
    private List<QXLiveInfoBean> list;
    private QXliveHeadingAdapter mHDliveHeadingAdapter;

    private TopBarFragment topBar;
    private CircleImageView img_head_info;
    private TextView tv_head;
    private HorizontalListView horizon_listview;
    private TextView tv_liveid, tv_livetime;
    private LinearLayout ll_bondlist;
    private Button btn_attention, btn_clean;


    /**
     * umeng
     */
    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    private ImageView btn_share;//分享
    private ShareBean mShareBean;

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
    private BSRGiftLayout giftLayout;
    private GiftAnmManager giftAnmManager;
    /**
     * 数据相关
     */
    private List<View> giftViewCollection = new ArrayList<View>();

    private List<GiftSendBean> sendLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置当前的Activity 无Title并且全屏 即必须在setContentView之前
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//Android5.0全透明状态栏效果
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//            System.out.println("===========================Build.VERSION.SDK_INT ==== " + Build.VERSION.SDK_INT +",Build.VERSION_CODES.LOLLIPOP = "+Build.VERSION_CODES.LOLLIPOP);
//        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//全透明状态栏效果
        initStatusbar(this, R.color.black);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕唤醒
        //让布局向上移来显示软键盘
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.camera_activity);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);//设置音量键

        mCameraHintView = (CameraHintView) findViewById(R.id.camera_hint);
        mCameraPreviewView = (GLSurfaceView) findViewById(R.id.camera_preview);
        //mCameraPreviewView = (TextureView) findViewById(R.id.camera_preview);
//        mChronometer = (Chronometer) findViewById(R.id.chronometer);//开播时间

        mObserverButton = new ButtonObserver();
//        mShootingText = (TextView) findViewById(R.id.click_to_shoot);
//        mShootingText.setOnClickListener(mObserverButton);
//        mDeleteView = findViewById(R.id.backoff);
//        mDeleteView.setOnClickListener(mObserverButton);
//        mSwitchCameraView = findViewById(R.id.switch_cam);
//        mSwitchCameraView.setOnClickListener(mObserverButton);
//        mFlashView = findViewById(R.id.flash);
//        mFlashView.setOnClickListener(mObserverButton);

        mCheckBoxObserver = new CheckBoxObserver();
//        mBeautyCheckBox = (CheckBox) findViewById(R.id.click_to_switch_beauty);
//        mBeautyCheckBox.setOnCheckedChangeListener(mCheckBoxObserver);
//        mReverbCheckBox = (CheckBox) findViewById(R.id.click_to_select_audio_filter);
//        mReverbCheckBox.setOnCheckedChangeListener(mCheckBoxObserver);
//        mBgmCheckBox = (CheckBox) findViewById(R.id.bgm);
//        mBgmCheckBox.setOnCheckedChangeListener(mCheckBoxObserver);
//        mAudioPreviewCheckBox = (CheckBox) findViewById(R.id.ear_mirror);
//        mAudioPreviewCheckBox.setOnCheckedChangeListener(mCheckBoxObserver);
//        mMuteCheckBox = (CheckBox) findViewById(R.id.mute);
//        mMuteCheckBox.setOnCheckedChangeListener(mCheckBoxObserver);
//        mWaterMarkCheckBox = (CheckBox) findViewById(R.id.watermark);
//        mWaterMarkCheckBox.setOnCheckedChangeListener(mCheckBoxObserver);
//        mFrontMirrorCheckBox = (CheckBox) findViewById(R.id.front_camera_mirror);
//        mFrontMirrorCheckBox.setOnCheckedChangeListener(mCheckBoxObserver);
//        mAudioOnlyCheckBox = (CheckBox) findViewById(R.id.audio_only);
//        mAudioOnlyCheckBox.setOnCheckedChangeListener(mCheckBoxObserver);

        mMainHandler = new Handler();
        // 创建KSYStreamer实例
        mStreamer = new KSYStreamer(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mIsLandscape = false;
            if (mIsLandscape) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            mAutoStart = bundle.getBoolean(START_ATUO, false);
            mPrintDebugInfo = bundle.getBoolean(SHOW_DEBUGINFO, false);
        }
        // 设置预览View
        mStreamer.setDisplayPreview(mCameraPreviewView);
        // 设置推流url（需要向相关人员申请，测试地址并不稳定！）
//        UPLIVE_URL = "rtmp://test.uplive.ksyun.com/live/888";//直播推流测试地址
//        uplive_url = "rtmp://wicep.uplive.ks-cdn.com/live/888";//直播推流正式地址
//        System.out.println("===========================推流url ==== " + uplive_url);
//        mStreamer.setUrl(uplive_url);
        // 设置预览分辨率, 当一边为0时，SDK会根据另一边及实际预览View的尺寸进行计算
        mStreamer.setPreviewResolution(1080, 0);//1920x1080
        // 设置推流分辨率，可以不同于预览分辨率
        mStreamer.setTargetResolution(720, 0);
        // 设置预览帧率
        mStreamer.setPreviewFps(15);
        // 设置推流帧率，当预览帧率大于推流帧率时，编码模块会自动丢帧以适应设定的推流帧率
        mStreamer.setTargetFps(15);
        // 设置视频码率，分别为初始码率、最高码率、最低码率，单位为bps
        mStreamer.setVideoBitrate(600 * 1000, 800 * 1000, 400 * 1000);
        // 设置音频采样率
        mStreamer.setAudioSampleRate(44100);
        // 设置音频码率，单位为bps
        mStreamer.setAudioBitrate(48 * 1000);
        /**
         * 设置编码模式(软编、硬编):
         * StreamerConstants.ENCODE_METHOD_SOFTWARE
         * StreamerConstants.ENCODE_METHOD_HARDWARE
         */
        mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
        // 设置屏幕的旋转角度，支持 0, 90, 180, 270
        mStreamer.setRotateDegrees(0);


        mStreamer.setEnableStreamStatModule(true);
        mStreamer.enableDebugLog(true);
        mStreamer.setFrontCameraMirror(true);//设置前置摄像头
        mStreamer.setMuteAudio(false);//设置静音
        mStreamer.setEnableAudioPreview(false);//设置音频预览
        mStreamer.setOnInfoListener(mOnInfoListener);
        mStreamer.setOnErrorListener(mOnErrorListener);
        mStreamer.setOnLogEventListener(mOnLogEventListener);
        //mStreamer.setOnAudioRawDataListener(mOnAudioRawDataListener);
        //mStreamer.setOnPreviewFrameListener(mOnPreviewFrameListener);
        mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                ImgTexFilterMgt.KSY_FILTER_BEAUTY_DENOISE);
        mStreamer.setEnableImgBufBeauty(true);
        mStreamer.getImgTexFilterMgt().setOnErrorListener(new ImgTexFilterBase.OnErrorListener() {
            @Override
            public void onError(ImgTexFilterBase filter, int errno) {
                Toast.makeText(QXLiveShowActivity.this, "当前机型不支持该滤镜",
                        Toast.LENGTH_SHORT).show();
                mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                        ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
            }
        });
        //GPUImageCropFilter  *cropfilter;

        // touch focus and zoom support
        CameraTouchHelper cameraTouchHelper = new CameraTouchHelper();
        cameraTouchHelper.setCameraCapture(mStreamer.getCameraCapture());
//        mCameraPreviewView.setOnTouchListener(cameraTouchHelper);
        mCameraPreviewView.setOnClickListener(this);
        // set CameraHintView to show focus rect and zoom ratio
        cameraTouchHelper.setCameraHintView(mCameraHintView);


        /**
         * 直播准备信息
         * */
        layout_live_info = (LinearLayout) findViewById(R.id.layout_live_info);
        layout_live_info.setVisibility(View.VISIBLE);
        img_head = (ImageView) findViewById(R.id.live_info_img_head);
        img_head.setOnClickListener(this);
        edit_name = (EditText) findViewById(R.id.live_info_edit_name);
        btn_golive = (Button) findViewById(R.id.live_info_btn_golive);
        btn_golive.setOnClickListener(this);
        checkBox_recommend = (CheckBox) findViewById(R.id.live_info_checkBox_recommend);

        /**
         * 底部+背景点击事件
         * */
        background = (ViewGroup) findViewById(R.id.background);
        background.setOnClickListener(this);
        bottomPanel = (BottomPanelFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_bar);
        img_close = (ImageView) bottomPanel.getView().findViewById(R.id.btn_close);
        img_close.setOnClickListener(this);
        img_switch_cam = (ImageView) bottomPanel.getView().findViewById(R.id.btn_rotate_icon);
        img_switch_cam.setOnClickListener(this);
        img_switch_cam.setVisibility(View.VISIBLE);
        btn_input = (ImageView) bottomPanel.getView().findViewById(R.id.btn_input);
        btn_input.setVisibility(View.GONE);//关闭聊天按钮
        btn_beauty = (ImageView) bottomPanel.getView().findViewById(R.id.btn_beauty);
        btn_beauty.setVisibility(View.GONE);//关闭美颜按钮
        btn_beauty.setOnClickListener(this);

        /**
         * 顶部 直播信息
         * */
        topBar = (TopBarFragment) getSupportFragmentManager().findFragmentById(R.id.top_bar);
        fragment_topbar_ll1 = (LinearLayout) topBar.getView().findViewById(R.id.fragment_topbar_ll1);
        fragment_topbar_ll2 = (LinearLayout) topBar.getView().findViewById(R.id.fragment_topbar_ll2);
        img_head_info = (CircleImageView) topBar.getView().findViewById(R.id.top_bar_img_head);
        tv_head = (TextView) topBar.getView().findViewById(R.id.top_bar_tv_head);
        horizon_listview = (HorizontalListView) topBar.getView().findViewById(R.id.top_bar_horizon_listview);
        tv_liveid = (TextView) topBar.getView().findViewById(R.id.top_bar_tv_liveid);
        tv_livetime = (TextView) topBar.getView().findViewById(R.id.top_bar_tv_livetime);
        ll_bondlist = (LinearLayout) topBar.getView().findViewById(R.id.top_bar_ll_bondlist);
        ll_bondlist.setOnClickListener(this);
        ll_bondlist.setVisibility(View.GONE);
        btn_attention = (Button) topBar.getView().findViewById(R.id.top_bar_btn_attention);
        btn_attention.setOnClickListener(this);
        btn_attention.setVisibility(View.GONE);
        btn_clean = (Button) topBar.getView().findViewById(R.id.top_bar_btn_clean);
        btn_clean.setOnClickListener(this);
        btn_clean.setVisibility(View.GONE);
//        topBar.setMenuVisibility(false);
        //隐藏顶部信息
        fragment_topbar_ll1.setVisibility(View.GONE);
        fragment_topbar_ll2.setVisibility(View.GONE);

        initGigt();//初始化刷礼物view
    }

    /**
     * 初始化刷礼物view
     */
    private void initGigt() {

        llgiftcontent = (LinearLayout) findViewById(R.id.llgiftcontent);
        giftLayout = (BSRGiftLayout) findViewById(R.id.gift_layout);
        giftAnmManager = new GiftAnmManager(giftLayout, this);

        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_out);
        giftNumAnim = new NumAnim();
        clearTiming();

    }

    private void startLive1() {
        is_live_info = true;
        layout_live_info.setVisibility(View.GONE);
//        /**
//         * 融云
//         * */
//        LiveKit.setCurrentUser(FakeServer.getUserInfo());
//        initLiveKit();
//        LiveKit.addEventHandler(handler);//融云聊天

        /**
         * 直播
         * */
        uplive_url = "rtmp://test.uplive.ksyun.com/live/460";//直播推流测试地址
        //              rtmp://test.rtmplive.ks-cdn.com/live/460";//直播拉流测试地址
        System.out.println("===========================推流url ==== " + uplive_url);
        mStreamer.setUrl(uplive_url);
        startStream();

        //启动顶部直播信息定时器
//        timer.schedule(task, 0, 5000);
        //显示顶部信息
//        topBar.setMenuVisibility(true);
//        fragment_topbar_ll1.setVisibility(View.VISIBLE);
//        fragment_topbar_ll2.setVisibility(View.VISIBLE);
    }

    private void startLive() {
        is_live_info = true;
        layout_live_info.setVisibility(View.GONE);
        /**
         * 融云
         * */
        LiveKit.setCurrentUser(FakeServer.getUserInfo());
        initLiveKit();
        LiveKit.addEventHandler(handler);//融云聊天

        /**
         * 直播
         * */

//        uplive_url = "rtmp://wicep.uplive.ks-cdn.com/live/"+zbid;//直播推流正式地址
        uplive_url = Constant.URL_LIVE_UPLIVE + zbid;//直播推流正式地址
        System.out.println("===========================推流url ==== " + uplive_url);
        mStreamer.setUrl(uplive_url);
        startStream();

        //启动顶部直播信息定时器
        timer.schedule(task, 0, 5000);
        //显示顶部信息
//        topBar.setMenuVisibility(true);
        fragment_topbar_ll1.setVisibility(View.VISIBLE);
        fragment_topbar_ll2.setVisibility(View.VISIBLE);
        ll_bondlist.setVisibility(View.VISIBLE);

        /**
         * UMeng
         * */
        initUMeng();
    }

    /**
     * 初始化融云聊天
     */
    private void initLiveKit() {
        chatListView = (ChatListView) findViewById(R.id.chat_listview);//融云聊天记录layout
        chatListAdapter = new ChatListAdapter();//融云聊天记录adapter
        chatListView.setAdapter(chatListAdapter);
        bottomPanel.setInputPanelListener(new InputPanel.InputPanelListener() {
            @Override
            public void onSendClick(String text) {//融云聊天记录发送信息
                final TextMessage content = TextMessage.obtain(text);
                LiveKit.sendMessage(content);
            }
        });
        roomId = zbid;
        joinChatRoom(roomId);//融云聊天 加入了聊天室
    }

    private void joinChatRoom(final String roomId) {
        LiveKit.setOnReceiveMessageListener(onReceiveMessageListener);
        LiveKit.joinChatRoom(roomId, -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
//                final InformationNotificationMessage content = InformationNotificationMessage.obtain("进入了直播间");
//                LiveKit.sendMessage(content);
                System.out.println("===========================直播加入聊天室成功 =roomId=== " + roomId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Toast.makeText(QXLiveShowActivity.this, "聊天室加入失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
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
            if ("RC:InfoNtf".equals(message.getObjectName())) {
                InformationNotificationMessage msg = (InformationNotificationMessage) message.getContent();
                String content = msg.getMessage();
//            System.out.println("===========================消息记录 msg.getContent() ==== " +  content +",zbid = " +zbid + ",roomId = " +roomId);
            }
            if ("RC:GiftMsg".equals(message.getObjectName())) {
                GiftMessage msg = (GiftMessage) message.getContent();
                GiftSendBean sendBean = new GiftSendBean();
                String content = msg.getType();
                userID = msg.getUserInfo().getUserId();
                userGiftInfo = content;
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
//                showGift(userID+userGiftInfo,userID,userGiftInfo,GiftDateUtlis.getGiftImgID(userGiftInfo));
            }
            return false;
        }
    };

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
                        if (giftLayout.getChildCount()==0) {
                            showGift(uid + giftInfo, uid, giftInfo, GiftDateUtlis.getGiftImgID(giftInfo));
                        }else {
                            Thread.sleep(Long.parseLong(sendLists.get(i).getTimes()));
                            showGift(uid + giftInfo, uid, giftInfo, GiftDateUtlis.getGiftImgID(giftInfo));
                        }
                        sendLists.remove(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (sendLists.size() == 0 || sendLists == null || "".equals(sendLists)){
                showGift(userID + userGiftInfo, userID, userGiftInfo, GiftDateUtlis.getGiftImgID(userGiftInfo));
            }
//            showGift(userID + userGiftInfo, userID, userGiftInfo, GiftDateUtlis.getGiftImgID(userGiftInfo));
        }
    };

    class MyGiftRunnable implements Runnable {
        public void run() {
            //执行数据操作，不涉及到UI
            Message msg = new Message();
            msg.what = GIFT_DIALOG;
            mGiftHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            System.out.println("===========================消息记录 更新UI ====");
        }
    }

    private void quit() {
        final InformationNotificationMessage content = InformationNotificationMessage.obtain(roomId + "结束了直播");
        LiveKit.sendMessage(content);

        /**
         * 主播退出
         * */
        quitLive();

        //退出聊天室
        LiveKit.removeEventHandler(handler);
        LiveKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
                Toast.makeText(QXLiveShowActivity.this, "直播已退出!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
//                Toast.makeText(QXLiveShowActivity.this, "退出失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
                System.out.println("=========================== 退出失败! errorCode = ==== " + errorCode);
            }
        });
    }

    private void initUMeng() {

        btn_share = (ImageView) bottomPanel.getView().findViewById(R.id.btn_share);
        btn_share.setOnClickListener(this);

        mShareListener = new CustomShareListener(QXLiveShowActivity.this);

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


//        String shareUrl = Constant.BASE_URL+Constant.URL_USERAPI_FXZBXX+"/"+zbid;
//        mShareAction = new ShareAction(QXLiveShowActivity.this).setDisplayList(
//                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                .withTitle("七星时代")
//                .withTargetUrl(shareUrl)
//                .withText("来自七星时代的分享")
//                .setCallback(mShareListener);
//        System.out.println("=========================== 分享连接 ====" + shareUrl);
    }

    private void share(File file) {
        String shareUrl = Constant.BASE_URL + Constant.URL_USERAPI_FXZBXX + "/id/" + roomId + "/type/2";////type 1视频 2 直播
//                    UMImage image = new UMImage(PlayVideoActivity.this, Constant.BASE_URL_IMG+mShareBean.getPic());//网络图片
        UMImage image = new UMImage(QXLiveShowActivity.this, file);//本地文件
        mShareAction = new ShareAction(QXLiveShowActivity.this).setDisplayList(
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

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<QXLiveShowActivity> mActivity;

        private CustomShareListener(QXLiveShowActivity activity) {
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
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onResume() {
        super.onResume();
        // 一般可以在onResume中开启摄像头预览
        startCameraPreviewWithPermCheck();
        // 调用KSYStreamer的onResume接口
        mStreamer.onResume();
        // 如果正在推流，切回音视频模式  && !mAudioOnlyCheckBox.isChecked()
        if (mStreamer.isRecording()) {
            mStreamer.setAudioOnly(false);
        }
        mStreamer.hideWaterMarkLogo();//关闭水印
//        if (mWaterMarkCheckBox.isChecked()) {
//            showWaterMark();//水印 图片地址问题
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mStreamer.onPause();
        // 一般在这里停止摄像头采集
        mStreamer.stopCameraPreview();
        // 如果正在推流，切换至音频推流模式  && !mAudioOnlyCheckBox.isChecked()
        if (mStreamer.isRecording()) {
            mStreamer.setAudioOnly(true);
        }
        hideWaterMark();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        mStreamer.release();
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

    private void startStream() {
        btn_input.setVisibility(View.VISIBLE);//显示聊天按钮
        btn_beauty.setVisibility(View.VISIBLE);//显示美颜按钮
        mStreamer.startStream();
//        mShootingText.setText(STOP_STRING);
//        mShootingText.postInvalidate();
        mRecording = true;
    }

    private void stopStream() {
        mStreamer.stopStream();
//        mChronometer.stop();
//        mShootingText.setText(START_STRING);
//        mShootingText.postInvalidate();
        mRecording = false;
    }

    private void beginInfoUploadTimer() {
    }

    //update debug info
    private void updateDebugInfo() {
        if (mStreamer == null) return;
        mDebugInfo = String.format(Locale.getDefault(),
                "RtmpHostIP()=%s DroppedFrameCount()=%d \n " +
                        "ConnectTime()=%d DnsParseTime()=%d \n " +
                        "UploadedKB()=%d EncodedFrames()=%d \n" +
                        "CurrentKBitrate=%d Version()=%s",
                mStreamer.getRtmpHostIP(), mStreamer.getDroppedFrameCount(),
                mStreamer.getConnectTime(), mStreamer.getDnsParseTime(),
                mStreamer.getUploadedKBytes(), mStreamer.getEncodedFrames(),
                mStreamer.getCurrentUploadKBitrate(), mStreamer.getVersion());
    }

    //show watermark in specific location
    private void showWaterMark() {
        if (!mIsLandscape) {
            mStreamer.showWaterMarkLogo(mLogoPath, 0.08f, 0.04f, 0.20f, 0, 0.8f);
            mStreamer.showWaterMarkTime(0.03f, 0.01f, 0.35f, Color.RED, 1.0f);
        } else {
            mStreamer.showWaterMarkLogo(mLogoPath, 0.05f, 0.09f, 0, 0.20f, 0.8f);
            mStreamer.showWaterMarkTime(0.01f, 0.03f, 0.22f, Color.RED, 1.0f);
        }
    }

    private void hideWaterMark() {
        mStreamer.hideWaterMarkLogo();
        mStreamer.hideWaterMarkTime();
    }

    // Example to handle camera related operation
    private void setCameraAntiBanding50Hz() {
        Camera.Parameters parameters = mStreamer.getCameraCapture().getCameraParameters();
        if (parameters != null) {
            parameters.setAntibanding(Camera.Parameters.ANTIBANDING_50HZ);
            mStreamer.getCameraCapture().setCameraParameters(parameters);
        }
    }


    /**
     *
     * */
    private KSYStreamer.OnInfoListener mOnInfoListener = new KSYStreamer.OnInfoListener() {
        @Override
        public void onInfo(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_INIT_DONE:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_INIT_DONE");
                    setCameraAntiBanding50Hz();
                    if (mAutoStart) {
                        startStream();
                    }
                    break;
                case StreamerConstants.KSY_STREAMER_OPEN_STREAM_SUCCESS:
                    Log.d(TAG, "KSY_STREAMER_OPEN_STREAM_SUCCESS");
//                    mChronometer.setBase(SystemClock.elapsedRealtime());
//                    mChronometer.start();
                    beginInfoUploadTimer();
                    break;
                case StreamerConstants.KSY_STREAMER_FRAME_SEND_SLOW:
                    Log.d(TAG, "KSY_STREAMER_FRAME_SEND_SLOW " + msg1 + "ms");
                    Toast.makeText(QXLiveShowActivity.this, "Network not good!",
                            Toast.LENGTH_SHORT).show();
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_RAISE:
                    Log.d(TAG, "BW raise to " + msg1 / 1000 + "kbps");
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_DROP:
                    Log.d(TAG, "BW drop to " + msg1 / 1000 + "kpbs");
                    break;
                default:
                    Log.d(TAG, "OnInfo: " + what + " msg1: " + msg1 + " msg2: " + msg2);
                    break;
            }
        }
    };

    /**
     * Error 监听
     */
    private KSYStreamer.OnErrorListener mOnErrorListener = new KSYStreamer.OnErrorListener() {
        @Override
        public void onError(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_ERROR_DNS_PARSE_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_DNS_PARSE_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_PUBLISH_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_PUBLISH_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_BREAKED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_BREAKED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_AV_ASYNC:
                    Log.d(TAG, "KSY_STREAMER_ERROR_AV_ASYNC " + msg1 + "ms");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_SERVER_DIED");
                    break;
                default:
                    Log.d(TAG, "what=" + what + " msg1=" + msg1 + " msg2=" + msg2);
                    break;
            }
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    mStreamer.stopCameraPreview();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startCameraPreviewWithPermCheck();
                        }
                    }, 5000);
                    break;
                default:
                    stopStream();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startStream();
                        }
                    }, 3000);
                    break;
            }
        }
    };

    private StatsLogReport.OnLogEventListener mOnLogEventListener =
            new StatsLogReport.OnLogEventListener() {
                @Override
                public void onLogEvent(StringBuilder singleLogContent) {
                    Log.i(TAG, "***onLogEvent : " + singleLogContent.toString());
                }
            };

    private OnAudioRawDataListener mOnAudioRawDataListener = new OnAudioRawDataListener() {
        @Override
        public short[] OnAudioRawData(short[] data, int count) {
            Log.d(TAG, "OnAudioRawData data.length=" + data.length + " count=" + count);
            //audio pcm data
            return data;
        }
    };

    private OnPreviewFrameListener mOnPreviewFrameListener = new OnPreviewFrameListener() {
        @Override
        public void onPreviewFrame(byte[] data, int width, int height, boolean isRecording) {
            Log.d(TAG, "onPreviewFrame data.length=" + data.length + " " +
                    width + "x" + height + " isRecording=" + isRecording);
        }
    };

    private void onSwitchCamera() {
        mStreamer.switchCamera();
        mCameraHintView.hideAll();
    }

    private void onFlashClick() {
        if (isFlashOpened) {
            mStreamer.toggleTorch(false);
            isFlashOpened = false;
        } else {
            mStreamer.toggleTorch(true);
            isFlashOpened = true;
        }
    }

    private void onBackoffClick() {
        final MaterialDialog mMaterialDialog = new MaterialDialog(QXLiveShowActivity.this);
        if (mMaterialDialog != null) {
            mMaterialDialog.setTitle("结束直播？")
                    .setMessage("")
                    .setPositiveButton(R.string.dialog_ok,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                    if (is_live_info) {
                                        stopStream();//停止直播
                                        quit();//退出融云聊天室
//                                        mChronometer.stop();
                                        mRecording = false;
                                        //停止计时器
                                        if (timer != null) {
                                            timer.cancel();
                                        }
                                    }
                                    QXLiveShowActivity.this.finish();
                                }
                            }
                    )
                    .setNegativeButton(R.string.dialog_cancel,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                }
                            })
                    .setCanceledOnTouchOutside(false)//点击外部不可消失dialog
                    .show();
        } else {

        }

    }

    private void onShootClick() {
        if (mRecording) {
            stopStream();
        } else {
            startStream();
        }
    }

    /**
     * 请选择美颜滤镜
     */
//    BEAUTY_SOFT",美柔 "
//    SKIN_WHITEN"皮肤美白"
//    BEAUTY_ILLUSION"美的幻想 "
//    DENOISE"去噪"
//    BEAUTY_SMOOTH"美丽光滑 "
//    DEMOFILTER"演示过滤器 "
//    GROUP_FILTER 组过滤器
    private void showChooseFilter() {
    }

    /**
     * 请选择音频滤镜
     */
    private void showChooseAudioFilter() {
    }

    private void onBeautyChecked(boolean isChecked) {
        if (isChecked) {
            if (mStreamer.getVideoEncodeMethod() ==
                    StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT) {
                mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                        ImgTexFilterMgt.KSY_FILTER_BEAUTY_DENOISE);
                mStreamer.setEnableImgBufBeauty(true);
            } else {
                showChooseFilter();
            }
        } else {
            mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                    ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
            mStreamer.setEnableImgBufBeauty(false);
        }
    }

    private void onAudioFilterChecked(boolean isChecked) {
        showChooseAudioFilter();
    }

    private void onBgmChecked(boolean isChecked) {
    }

    private void onAudioPreviewChecked(boolean isChecked) {
        mStreamer.setEnableAudioPreview(isChecked);
    }

    private void onMuteChecked(boolean isChecked) {
        mStreamer.setMuteAudio(isChecked);
    }

    private void onWaterMarkChecked(boolean isChecked) {
        if (isChecked)
            showWaterMark();
        else
            hideWaterMark();
    }

    private void onFrontMirrorChecked(boolean isChecked) {
        mStreamer.setFrontCameraMirror(isChecked);
    }

    private void onAudioOnlyChecked(boolean isChecked) {
        mStreamer.setAudioOnly(isChecked);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.live_info_img_head://封面
                int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
                intent = new Intent(this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.live_info_btn_golive:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm.hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
                str_title = edit_name.getText().toString().trim();
//                startLive1();
                if (!is_live_info) {
                    if (!TextUtils.isEmpty(str_title)) {
                        if (TextUtils.isEmpty(img_base64)) {
                            //实际上这是一个BitmapDrawable对象
                            BitmapDrawable bitmapDrawable = (BitmapDrawable) (getResources().getDrawable(R.drawable.icon_logo));
                            //可以在调用getBitmap方法，得到这个位图
                            Bitmap bitmap = bitmapDrawable.getBitmap();
                            Bitmap bitmap_default = ColorUtils.changeColor(bitmap);
                            img_base64 = ImageTools.bitmapToBase64(bitmap_default);
                        }
                        getTokenFromSever();
                    } else {
                        Toasts.show("先给直播起个标题吧！");
                    }
                }
                break;
            case R.id.btn_close:
                onBackoffClick();
                break;
            case R.id.btn_share://分享
//                mShareAction.open();
                shareNum();
                break;
            case R.id.btn_rotate_icon:
                onSwitchCamera();
                break;
            case R.id.background:
                bottomPanel.onBackAction();
                break;
            case R.id.camera_preview:
                bottomPanel.onBackAction();
                break;
            case R.id.btn_beauty:
                if (is_beauty) {//关闭美颜
                    is_beauty = false;
                    //关闭美颜
                    mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                            ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
                    Toasts.show("美颜已关闭");
                    btn_beauty.setImageDrawable(getResources().getDrawable(R.drawable.live_beauty_off));
                } else {//开启美颜
                    is_beauty = true;
                    //设置美颜
                    mStreamer.getImgTexFilterMgt().setFilter(
                            mStreamer.getGLRender(), ImgTexFilterMgt.KSY_FILTER_BEAUTY_SOFT);
                    Toasts.show("美颜已打开");
                    btn_beauty.setImageDrawable(getResources().getDrawable(R.drawable.live_beauty_on));
                }
                break;
            case R.id.top_bar_ll_bondlist:
                intent = new Intent();
                intent.putExtra("zbid", zbid);
                AppManager.getAppManager().startNextActivity(QXLiveShowActivity.this, BondListActivity.class, intent);
                break;
        }
    }

    private class ButtonObserver implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
//                case R.id.switch_cam:
//                    onSwitchCamera();
//                    break;
//                case R.id.backoff:
//                    onBackoffClick();
//                    break;
//                case R.id.flash:
//                    onFlashClick();
//                    break;
//                case R.id.click_to_shoot:
//                    onShootClick();
//                    break;
                default:
                    break;
            }
        }
    }

    /**
     * CheckBox 监听
     */
    private class CheckBoxObserver implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
//                case R.id.click_to_switch_beauty:
//                    onBeautyChecked(isChecked);
//                    break;
//                case R.id.click_to_select_audio_filter:
//                    onAudioFilterChecked(isChecked);
//                    break;
//                case R.id.bgm:
//                    onBgmChecked(isChecked);
//                    break;
//                case R.id.ear_mirror:
//                    onAudioPreviewChecked(isChecked);
//                    break;
//                case R.id.mute:
//                    onMuteChecked(isChecked);
//                    break;
//                case R.id.watermark:
//                    onWaterMarkChecked(isChecked);
//                    break;
//                case R.id.front_camera_mirror:
//                    onFrontMirrorChecked(isChecked);
//                    break;
//                case R.id.audio_only:
//                    onAudioOnlyChecked(isChecked);
//                    break;
                default:
                    break;
            }
        }
    }

    private void startCameraPreviewWithPermCheck() {
        int cameraPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int audioPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (cameraPerm != PackageManager.PERMISSION_GRANTED ||
                audioPerm != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Log.e(TAG, "No CAMERA or AudioRecord permission, please check");
                Toast.makeText(this, "No CAMERA or AudioRecord permission, please check",
                        Toast.LENGTH_LONG).show();
            } else {
                String[] permissions = {Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions,
                        PERMISSION_REQUEST_CAMERA_AUDIOREC);
            }
        } else {
            mStreamer.startCameraPreview();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA_AUDIOREC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mStreamer.startCameraPreview();
                } else {
                    Log.e(TAG, "No CAMERA or AudioRecord permission");
                    Toast.makeText(this, "No CAMERA or AudioRecord permission",
                            Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        path = ImageCutActivity.path + ImageCutActivity.photo_name;
        System.out.println("===========选择的图片路径是" + path);
        if (path != null) {

            bitmap = ImageTools.createThumbnail(path, 200, 200);
////            Bitmap bmp= BitmapFactory.decodeFile(path);
//            is = InputStreamUtils.bitmapToInputStream(bitmap,1024);

            img_base64 = ImageTools.bitmapToBase64(bitmap);
//            upHeadPhoto();
            img_head.setImageBitmap(ImageTools.base64ToBitmap(img_base64));
        }
    }

    public ProgressDialog mProgressDialog;
    public AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();
    ;
    public static MaterialDialog mMaterialDialog;
    private ResultBean mResultBean;

    /**
     * 获取融云Token
     */
    private void getTokenFromSever() {
        final UserInfo user = FakeServer.getUserInfo();
        System.out.println("===========================getTokenFromSever12 ===== ");
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_BYUSERGETINFO;
        System.out.println("===========================直播 融云获取token url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

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
                                Toasts.show("进入直播");
                                getLiveInfo();
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
                    mMaterialDialog = new MaterialDialog(QXLiveShowActivity.this);
                    mMaterialDialog.setTitle(R.string.dialog_prompt)
                            .setMessage(R.string.dialog_wrong)
                            .setPositiveButton(R.string.dialog_ok,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mMaterialDialog.dismiss();
                                        }
                                    }
                            ).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mMaterialDialog = new MaterialDialog(QXLiveShowActivity.this);
                mMaterialDialog.setTitle(R.string.dialog_prompt)
                        .setMessage(R.string.dialog_wrong)
                        .setPositiveButton(R.string.dialog_ok,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                    }
                                }
                        ).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mMaterialDialog = new MaterialDialog(QXLiveShowActivity.this);
                mMaterialDialog.setTitle(R.string.dialog_prompt)
                        .setMessage(R.string.dialog_timeout)
                        .setPositiveButton(R.string.dialog_ok,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                    }
                                }
                        ).show();
            }
        });
    }

    /**
     * 设置直播标题及封面
     */
    private void getLiveInfo() {
        final UserInfo user = FakeServer.getUserInfo();
        System.out.println("===========================getTokenFromSever12 ===== ");
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("title", str_title);
        params.put("pic", img_base64);
        params.put("istj", checkBox_recommend.isChecked() ? 1 : 0);
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_ADDZBINFO;
        System.out.println("===========================设置直播标题及封面token url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================设置直播标题及封面token response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        zbid = mResultBean.getZbid();
                        startLive();
                    } else {
                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
                    mMaterialDialog = new MaterialDialog(QXLiveShowActivity.this);
                    mMaterialDialog.setTitle(R.string.dialog_prompt)
                            .setMessage(R.string.dialog_wrong)
                            .setPositiveButton(R.string.dialog_ok,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mMaterialDialog.dismiss();
                                        }
                                    }
                            ).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mMaterialDialog = new MaterialDialog(QXLiveShowActivity.this);
                mMaterialDialog.setTitle(R.string.dialog_prompt)
                        .setMessage(R.string.dialog_wrong)
                        .setPositiveButton(R.string.dialog_ok,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                    }
                                }
                        ).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mMaterialDialog = new MaterialDialog(QXLiveShowActivity.this);
                mMaterialDialog.setTitle(R.string.dialog_prompt)
                        .setMessage(R.string.dialog_timeout)
                        .setPositiveButton(R.string.dialog_ok,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                    }
                                }
                        ).show();
            }
        });
    }

    /**
     * 主播退出直播
     */
    private void quitLive() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
//            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("zbid", zbid);
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_OUT_ZB;//
        System.out.println("===========================主播退出直播 url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================主播退出直播 response = " + response.toString());
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
                System.out.println("===========================onFailure ,responseString =  " + errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
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
        timer.schedule(task, 0, 4000);
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
        params.put("zbid", zbid);
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
                            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mHDLiveInfoJson.getZbinfo().getZb_pic(), img_head_info, ImageLoaderOptions.getOptions());
                            tv_head.setText(mHDLiveInfoJson.getZbinfo().getSee_num() + "人");
                            tv_liveid.setText("直播号:" + mHDLiveInfoJson.getZbinfo().getZbno());
                            tv_livetime.setText(mHDLiveInfoJson.getZbinfo().getTimes());//DateUtils.TimeStamp2DateYYYYMMDD(mHDLiveInfoJson.getZbinfo().getTimes())
                        }
                        if (mHDLiveInfoJson.getUserlist() == null || mHDLiveInfoJson.getUserlist().size() == 0 || "".equals(mHDLiveInfoJson.getUserlist())) {
                            list = null;
                            if (mHDliveHeadingAdapter != null) {
                                mHDliveHeadingAdapter.notifyDataSetChanged();
                            }
                            horizon_listview.setVisibility(View.GONE);
                        } else {

                            list = mHDLiveInfoJson.getUserlist();
                            mHDliveHeadingAdapter = new QXliveHeadingAdapter(QXLiveShowActivity.this, list);
                            horizon_listview.setAdapter(mHDliveHeadingAdapter);
                            mHDliveHeadingAdapter.notifyDataSetChanged();
                            horizon_listview.setVisibility(View.VISIBLE);
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
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE://修改头像
                if (resultCode == RESULT_OK) {

                    mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

                    Intent intent = new Intent();

                    intent.putStringArrayListExtra("path", mSelectPath);
                    intent.putExtra("tag", "QXLiveShowActivity");
                    AppManager.getAppManager().startFragmentNextActivity(this, ImageCutActivity.class, intent);

                    //   Toast.makeText(PersonnalInfomation.this, "file://"+mSelectPath, Toast.LENGTH_SHORT).show();
                    //   StringBuilder sb = new StringBuilder();
                    for (String p : mSelectPath) {
//	                    sb.append(p);
//	                    sb.append("\n");

//	                	  ImageLoader.getInstance().displayImage("file://"+p, img_face,ImageLoaderOptions.getOptions());
                    }
                    //  mResultText.setText(sb.toString());

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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


    /**
     * 赠送礼物动画
     * */
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


    private void shareNum() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(QXLiveShowActivity.this);
            mProgressDialog.setMessage("加载中...");
//            mProgressDialog.show();
        }
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("spid", zbid);
        params.put("type", "2");//type  1视频   2直播
        final String url = Constant.BASE_URL + Constant.URL_INDEX_SHARE_NUM;
        System.out.println("===========================用户观看视频页 分享成功前转发量 url ======= " + url);
        mAsyncHttpClient.post(QXLiveShowActivity.this, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================用户观看视频页 分享成功前转发量 response ======= " + response.toString());
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

    private Bitmap bitmap2;
//    private String img_base64;

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
//                    bitmap2= ImageTools.createThumbnail(tempPath, 200, 200);
//                    img_base64=ImageTools.bitmapToBase64(bitmap);

                    share(file);

//                    Toast.makeText(QXLiveShowActivity.this, "下载成功\n" + tempPath,Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {
                // TODO Auto-generated method stub
                Toast.makeText(QXLiveShowActivity.this, "分享失败", Toast.LENGTH_LONG).show();
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
