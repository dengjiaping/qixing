package com.qixing.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Debug;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cookie.store.PersistentCookieStore;
import com.lzy.okhttputils.model.HttpParams;
import com.qixing.global.Constant;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.qixing.qxlive.rongyun.LiveKit;
import com.qixing.qxlive.rongyun.fakeserver.FakeServer;
import com.qixing.utlis.SystemUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import me.leolin.shortcutbadger.ShortcutBadger;


public class MyApplication extends Application {

    public static Context applicationContext;
    private static MyApplication instance;

    private final String PREF_UID = "uid";//用户ID
    private String uid = null;
    private final String PREF_U_TEL = "u_tel";//用户电话
    private String u_tel = null;
    private final String PREF_UNAME = "uname";//用户名
    private String uname = null;
    private final String PREF_NICKNAME = "nickname";//用户昵称
    private String nickname = null;
    private static final String PREF_USER_IS_LOGINING = "is_login";//是否登录
    private String is_login = null;
    private static final String PREF_USER_LAST_TIME = "last_time";//上次登录时间
    private String last_time = null;
    private static final String PREF_USER_HEAD = "head_path";//头像路径
    private String user_head = null;
    private static final String PREF_PWD = "pwd";//登录密码
    private String password = null;
    private final String PREF_ISPAYPWD = "ispaypwd";//0未设置支付密码 1已设置支付密码
    private String ispaypwd = null;
    private static final String PREF_SHENGRI = "shengri";//生日
    private String shengri = null;
    private static final String PREF_USER_SEX = "sex";//性别
    private String sex = null;
    private static final String PREF_USER_QIANM = "qianm";//个性签名
    private String qianm = null;
    private static final String PREF_USER_PROMOTIONID = "promotionid";//推广ID
    private String promotionid = null;
    private static final String PREF_USER_LOGIN_TYPE = "login_type";//登录类型 微信 WX,支付宝 ZFB
    private String login_type = null;
    private final String PREF_VERSIONCODE_SERVICE = "versioncode_service";//版本号
    private String versioncode_service = null;
    private final String PREF_USER_DJPIC = "djpic";//等级图片
    private String user_djpic = null;
    private final String PREF_DOWNLOAD_FILEMK = "down_fileMk";
    private String down_fileMk = null;
    private final String PREF_DOWNLOAD_STATUS = "isDown";
    private String is_Down = null;
    private final String PREF_USER_JFCOUNT="jf_count";
    private String jf_count=null;
    private final String PREF_USER_VIPDENGJI="vip_dj";
    private String vip_dj=null;


    private final String PREF_USERNAME = "username";
    private final String PREF_USERNICKNAME = "usernickname";
    private final String PREF_USERHEAD = "userhead";
    private final String PREF_USERHEAD_BASE64 = "userheadbase64";
    private String userName = null;
    private String userNickName = null;
    private String userHeade = null;
    private Boolean isFirst = null;
    private boolean is_jpush;
    private String userheadnetpath = null;

    private static final String APK_ID = "apk_id";
    private String apk_id = null;

    private static final String VIDEO_ID = "video_id";
    private String video_id = null;

    private static final String VIDEO_NAME = "video_name";//设置下载视频名字
    private String video_name = null;

    private static final String PDF_ID = "pdf_id"; //pdf文件的id
    private String pdf_id = null;

    private static final String PDF_NAME = "pdf_name";//pdf文件的名称
    private String pdf_name = null;

    private static final String PREF_LOGIN_PLATFORM = "login_platform";//登录平台信息
    private String login_platform = null;

    private static final String APK_NAME = "apk_name";//设置下载APK 名字
    private String apk_name = null;

    private static final String PREF_USER_ADDRESS = "address";
    private String address = null;

    private static final String PREF_USER_IS_RECHARGE = "is_recharge";//充值回调成功
    private String is_recharge = null;

    private final String PREF_INFO_NUM = "info_num";//APP未读推送消息数量
    private int info_num = 0;

    private final String PREF_FIRST_ENTER = "first_enter";//用户第一次进行操作
    private String first_enter = null;

    private final String PREF_INFO_NEW = "info_new";//新消息阅读前后状态
    private String info_new = null;

    private final String PREF_VIDEO_READNUM = "read_num";//视频查看次数
    private int read_num = 0;

    private final String PREF_USERAPI_KFPHONE = "kf_phone";//客服电话
    private String kf_phone = null;

    private final String PREF_USERAPI_NEWTAG="new_tag";
    private String new_tag=null;

    private final String PREF_USERAPI_NEWINDEX="new_index";//消息标识
    private String new_index=null;

    private String signature = null;
    public static Map<String, Long> TimeMap;

    public boolean isNewVersion = true;//检查更新 t 需要检查更新 f 不需要

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        applicationContext = this;
        instance = this;
        initImageloader(applicationContext);

//		Fresco.initialize(applicationContext);
//		LitePalApplication.initialize(applicationContext);
//		AssetsDatabaseManager.initManager(applicationContext);
//		PushManager.getInstance().initialize(this.getApplicationContext());
//		SQLiteDatabase db = Connector.getDatabase();
        JPushInterface.setDebugMode(true);// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);// 初始化 JPush
        initEmailReporter();
        initMyLocation();

        MyApplication.getInstance().setIs_jpush(true);

        initOkHttp();

        /**
         * 融云 初始化
         * */
        LiveKit.init(applicationContext, FakeServer.getAppKey());

        /**
         * UMeng 调试模式
         * */
//        Config.DEBUG=true;
        /**
         * umeng 初始化
         * */
        UMShareAPI.get(this);
        PlatformConfig.setWeixin(Constant.APP_ID, Constant.WEIXIN_SECRET);
        //wx0991ee14e8592c3e
        //
        PlatformConfig.setQQZone(Constant.APP_ID_QQ, Constant.APP_KEY_QQ);

        if (SystemUtils.isAppAlive(applicationContext, applicationContext.getPackageName())) {
            setBadgeCount(applicationContext);
        }
    }


    public static MyApplication getInstance() {

        return instance;
    }

    public static Context getAppContext() {

        return applicationContext;
    }

    public static Resources getAppResource() {

        return getAppResource();
    }

    /**
     * 获取用户vip等级
     *
     * @return vip_dj
     */
    public String getVip_dj() {

        if (vip_dj == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            vip_dj = preferences.getString(PREF_USER_VIPDENGJI, null);
        }
        return vip_dj;
    }

    /**
     * 设置 用户vip等级
     *
     * @param dj
     */
    public void setVip_dj(String dj) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_USER_VIPDENGJI, dj).commit()) {
            vip_dj = dj;
        }
    }
    /**
     * 获取用户积分数目
     *
     * @return jf_count
     */
    public String getJf_count() {

        if (jf_count == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            jf_count = preferences.getString(PREF_USER_JFCOUNT, null);
        }
        return jf_count;
    }

    /**
     * 设置 用户积分数目
     *
     * @param jf
     */
    public void setJf_count(String jf) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_USER_JFCOUNT, jf).commit()) {
            jf_count = jf;
        }
    }
    /**
     * 获取信息标识
     *
     * @return new_index
     */
    public String getNew_index() {

        if (new_index == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            new_index = preferences.getString(PREF_USERAPI_NEWINDEX, null);
        }
        return new_index;
    }

    /**
     * 设置 消息标识
     *
     * @param newIndex
     */
    public void setNew_index(String newIndex) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_USERAPI_NEWINDEX, newIndex).commit()) {
            new_index = newIndex;
        }
    }
    /**
     * 获取newpic路径
     *
     * @return new_tag
     */
    public String getNew_tag() {

        if (new_tag == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            new_tag = preferences.getString(PREF_USERAPI_NEWTAG, null);
        }
        return new_tag;
    }

    /**
     * 设置 set
     *
     * @param newTag
     */
    public void setNew_tag(String newTag) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_USERAPI_NEWTAG, newTag).commit()) {
            new_tag = newTag;
        }
    }
    /**
     * 获取客服电话
     *
     * @return kf_phone
     */
    public String getKf_phone() {

        if (kf_phone == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            kf_phone = preferences.getString(PREF_USERAPI_KFPHONE, null);
        }
        return kf_phone;
    }

    /**
     * 设置 客服电话
     *
     * @param kfPhone
     */
    public void setKf_phone(String kfPhone) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_USERAPI_KFPHONE, kfPhone).commit()) {
            kf_phone = kfPhone;
        }
    }
    /**
     * 视频的观看次数
     *
     * @return read_num
     */
    public int getRead_num() {

        if (read_num == 0) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            read_num = preferences.getInt(PREF_VIDEO_READNUM, 0);
        }
        return read_num;
    }

    /**
     * 设置 视频的观看次数
     *
     * @param readNum
     */
    public void setRead_num(int readNum) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putInt(PREF_VIDEO_READNUM, readNum).commit()) {
            read_num = readNum;
        }
    }

    /**
     * 用户第一次的操作状态
     *
     * @return first_enter
     */
    public String getFirst_enter() {

        if (first_enter == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            first_enter = preferences.getString(PREF_FIRST_ENTER, null);
        }
        return first_enter;
    }

    /**
     * 设置 用户第一次的操作状态
     *
     * @param is_First
     */
    public void setFirst_enter(String is_First) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_FIRST_ENTER, is_First).commit()) {
            first_enter = is_First;
        }
    }


    /**
     * 视频、资讯、干货阅读状态
     *
     * @return info_new
     */
    public String getInfo_new() {

        if (info_new == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            info_new = preferences.getString(PREF_INFO_NEW, null);
        }
        return info_new;
    }

    /**
     * 设置 视频、资讯、干货的阅读状态
     *
     * @param info_state
     */
    public void setInfo_new(String info_state) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_INFO_NEW, info_state).commit()) {
            info_new = info_state;
        }
    }

    /**
     * 获取APP消息Num
     *
     * @return info_num
     */
    public int getInfo_num() {

        if (info_num == 0) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            info_num = preferences.getInt(PREF_INFO_NUM, 0);
        }
        return info_num;
    }

    /**
     * 设置APP消息Num
     *
     * @param info_size
     */
    public void setInfo_num(int info_size) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putInt(PREF_INFO_NUM, info_size).commit()) {
            info_num = info_size;
        }
    }

    /**
     * 获取用户id:uid
     *
     * @return uid
     */
    public String getUid() {

        if (uid == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            uid = preferences.getString(PREF_UID, null);
        }
        return uid;
    }

    /**
     * 设置用户id:uid
     *
     * @param uid1
     */
    public void setUid(String uid1) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_UID, uid1).commit()) {
            uid = uid1;
        }
    }

    /**
     * 获取视频id:video_id
     *
     * @return video_id
     */
    public String getVideoId() {

        if (video_id == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            video_id = preferences.getString(VIDEO_ID, null);
        }
        return video_id;
    }

    /**
     * 设置视频id:video_id
     *
     * @param videoId
     */
    public void setVideoId(String videoId) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(VIDEO_ID, videoId).commit()) {
            video_id = videoId;
        }
    }

    /**
     * 获取视频id:pdf_id
     *
     * @return pdf_id
     */
    public String getPdfId() {

        if (pdf_id == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            pdf_id = preferences.getString(PDF_ID, null);
        }
        return pdf_id;
    }

    /**
     * 设置视频id:pdf_id
     *
     * @param pdfId
     */
    public void setPdfId(String pdfId) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PDF_ID, pdfId).commit()) {
            pdf_id = pdfId;
        }
    }

    /**
     * 获取用户id:down_fileMk
     *
     * @return down_fileMk
     */
    public String getDown_fileMk() {

        if (down_fileMk == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            down_fileMk = preferences.getString(PREF_DOWNLOAD_FILEMK, null);
        }
        return down_fileMk;
    }

    /**
     * 设置用户id:down_fileMk
     *
     * @param fileMk
     */
    public void setDown_fileMk(String fileMk) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_DOWNLOAD_FILEMK, fileMk).commit()) {
            down_fileMk = fileMk;
        }
    }

    /**
     * 获取用户id:is_Down
     *
     * @return is_Down
     */
    public String getIs_Down() {

        if (is_Down == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            is_Down = preferences.getString(PREF_DOWNLOAD_STATUS, null);
        }
        return is_Down;
    }

    /**
     * 设置用户id:is_Down
     *
     * @param isDown
     */
    public void setIs_Down(String isDown) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_DOWNLOAD_STATUS, isDown).commit()) {
            is_Down = isDown;
        }
    }

    /**
     * 获取视频id:video_name
     *
     * @return video_name
     */
    public String getVideoName() {

        if (video_name == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            video_name = preferences.getString(VIDEO_NAME, null);
        }
        return video_name;
    }

    /**
     * 设置视频id:video_name
     *
     * @param videoName
     */
    public void setVideoName(String videoName) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(VIDEO_NAME, videoName).commit()) {
            video_name = videoName;
        }
    }

    /**
     * 获取视频id:pdf_name
     *
     * @return pdf_name
     */
    public String getPdfName() {

        if (pdf_name == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            pdf_name = preferences.getString(PDF_NAME, null);
        }
        return pdf_name;
    }

    /**
     * 设置视频id:pdf_name
     *
     * @param pdfName
     */
    public void setPdfName(String pdfName) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PDF_NAME, pdfName).commit()) {
            pdf_name = pdfName;
        }
    }


    /**
     * 获取用户手机号
     *
     * @return u_tel
     */
    public String getU_tel() {

        if (u_tel == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            u_tel = preferences.getString(PREF_U_TEL, null);
        }
        return u_tel;
    }

    /**
     * 设置用户手机号
     *
     * @param u_tel1
     */
    public void setU_tel(String u_tel1) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_U_TEL, u_tel1).commit()) {
            u_tel = u_tel1;
        }
    }


    /**
     * 获取用户名
     *
     * @return u_tel
     */
    public String getUname() {

        if (uname == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            uname = preferences.getString(PREF_UNAME, null);
        }
        return uname;
    }

    /**
     * 设置用户名
     *
     * @param uname1
     */
    public void setUname(String uname1) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_UNAME, uname1).commit()) {
            uname = uname1;
        }
    }

    /**
     * 获取用户昵称
     *
     * @return nickname
     */
    public String getNickname() {

        if (nickname == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            nickname = preferences.getString(PREF_NICKNAME, null);
        }
        return nickname;
    }

    /**
     * 设置用户昵称
     *
     * @param nickname1
     */
    public void setNickname(String nickname1) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_NICKNAME, nickname1).commit()) {
            nickname = nickname1;
        }
    }

    /**
     * 设置登录状态
     *
     * @param login
     * @return
     */
    public void setIsLogining(String login) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_USER_IS_LOGINING, login).commit()) {
            is_login = login;
        }
    }

    /**
     * 获取登录状态
     *
     * @return
     */
    public String getIsLogining() {

        if (is_login == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            is_login = preferences.getString(PREF_USER_IS_LOGINING, null);
        }
        return is_login;
    }


    /**
     * 设置充值状态
     *
     * @param recharge
     * @return
     */
    public void setIsRecharge(String recharge) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_USER_IS_RECHARGE, recharge).commit()) {
            is_recharge = recharge;
        }
    }

    /**
     * 获取充值状态
     *
     * @return
     */
    public String getIsRecharge() {

        if (is_recharge == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            is_recharge = preferences.getString(PREF_USER_IS_RECHARGE, null);
        }
        return is_recharge;
    }

    /**
     * 设置等级图片
     *
     * @param login
     * @return
     */
    public void setU_djpic(String djpic) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_USER_DJPIC, djpic).commit()) {
            user_djpic = djpic;
        }
    }

    /**
     * 获取等级图片
     *
     * @return
     */
    public String getU_djpic() {

        if (user_djpic == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            user_djpic = preferences.getString(PREF_USER_DJPIC, null);
        }
        return user_djpic;
    }

    /**
     * 设置登录状态
     *
     * @param lastTime
     * @return
     */
    public void setLastTime(String lastTime) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_USER_LAST_TIME, lastTime).commit()) {
            last_time = lastTime;
        }
    }

    /**
     * 获取登录状态
     *
     * @return
     */
    public String getLastTime() {

        if (last_time == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            last_time = preferences.getString(PREF_USER_LAST_TIME, null);
        }
        return last_time;
    }

    /**
     * 设置头像
     *
     * @param path
     */
    public void setUser_Head(String path) {


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_USER_HEAD, path).commit()) {
            user_head = path;
        }

    }

    /**
     * 获取用户头像
     *
     * @return
     */
    public String getUser_Head() {


        if (user_head == null) {

            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(applicationContext);
            user_head = preferences.getString(PREF_USER_HEAD, null);
        }

        return user_head;
    }

    /**
     * 设置密码 加密后存入 preference
     *
     * @param pwd
     */
    public void setPassword(String pwd) {


        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_PWD, pwd).commit()) {
            password = pwd;
        }
    }

    /**
     * 获取用户密码
     *
     * @return
     */
    public String getPassword() {
        if (password == null) {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(applicationContext);
            password = preferences.getString(PREF_PWD, null);
        }
        return password;
    }

    /**
     * 获取用户性别
     *
     * @return
     */
    public String getSex() {
        if (sex == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            sex = preferences.getString(PREF_USER_SEX, null);
        }
        return sex;
    }

    /**
     * 设置密码 设置用户性别
     *
     * @param sex1
     */
    public void setSex(String sex1) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_USER_SEX, sex1).commit()) {
            sex = sex1;
        }
    }

//	private static final String PREF_USER_QIANM="qianm";//个性签名
//	private String  qianm=null;

    /**
     * 获取用户个性签名
     *
     * @return
     */
    public String getQianm() {
        if (qianm == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            qianm = preferences.getString(PREF_USER_QIANM, null);
        }
        return qianm;
    }

    /**
     * 设置密码 设置个性签名
     *
     * @param qianm1
     */
    public void setQianm(String qianm1) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_USER_QIANM, qianm1).commit()) {
            qianm = qianm1;
        }
    }
//	private static final String PREF_USER_PROMOTIONID="promotionid";//推广ID
//	private String  promotionid=null;

    /**
     * 获取推广ID
     *
     * @return
     */
    public String getPromotionid() {
        if (promotionid == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            promotionid = preferences.getString(PREF_USER_PROMOTIONID, null);
        }
        return promotionid;
    }

    /**
     * 设置推广ID
     *
     * @param promotionid1
     */
    public void setPromotionid(String promotionid1) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_USER_PROMOTIONID, promotionid1).commit()) {
            promotionid = promotionid1;
        }
    }

//	private static final String PREF_USER_LOGIN_TYPE="login_type";//登录类型 微信 WX,支付宝 ZFB
//	private String  login_type=null;

    /**
     * 获取登录类型
     *
     * @return
     */
    public String getLogin_type() {
        if (login_type == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            login_type = preferences.getString(PREF_USER_LOGIN_TYPE, null);
        }
        return login_type;
    }

    /**
     * 设置登录类型
     *
     * @param login_type1
     */
    public void setLogin_type(String login_type1) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_USER_LOGIN_TYPE, login_type1).commit()) {
            login_type = login_type1;
        }
    }

//	private final String PREF_ISPAYPWD = "ispaypwd";//0未设置支付密码 1已设置支付密码
//	private String ispaypwd = null;

    /**
     * 获取登录类型
     *
     * @return
     */
    public String getPref_ispaypwd() {
        if (ispaypwd == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            ispaypwd = preferences.getString(PREF_ISPAYPWD, null);
        }
        return ispaypwd;
    }

    /**
     * 设置登录类型
     *
     * @param ispaypwd1
     */
    public void setPref_ispaypwd(String ispaypwd1) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_ISPAYPWD, ispaypwd1).commit()) {
            ispaypwd = ispaypwd1;
        }
    }

    /**
     * 获取用户生日
     *
     * @return
     */
    public String getShengri() {
        if (shengri == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            shengri = preferences.getString(PREF_SHENGRI, null);
        }
        return shengri;
    }

    /**
     * 设置密码 设置用户生日
     *
     * @param shengri1
     */
    public void setShengri(String shengri1) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_SHENGRI, shengri1).commit()) {
            shengri = shengri1;
        }
    }

    /**
     * 获取新版本号
     *
     * @return
     */
    public String getVersioncode_service() {
        if (versioncode_service == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            versioncode_service = preferences.getString(PREF_VERSIONCODE_SERVICE, null);
        }
        return versioncode_service;
    }

    /**
     * 设置 新版本号
     *
     * @param versioncode_service1
     */
    public void setVersioncode_service(String versioncode_service1) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_VERSIONCODE_SERVICE, versioncode_service1).commit()) {
            versioncode_service = versioncode_service1;
        }
    }


    //=====================================================================================================


    /**
     * 获取当前登陆用户 加密字符串
     *
     * @return
     */
    public String getUser() {
        if (userName == null) {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(applicationContext);
            userName = preferences.getString(PREF_USERNAME, null);
        }
        return userName;
    }


    public String getUserAddress() {

        if (address == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            address = preferences.getString(PREF_USER_ADDRESS, null);
        }
        return address;
    }

    public void setUserAddress(String address1) {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putString(PREF_USER_ADDRESS, address1).commit()) {

            address = address1;

        }

    }


    /**
     * 设置用户昵称
     *
     * @param NickName
     */
    public void setUserNickName(String NickName) {
        if (NickName != null) {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(applicationContext);
            SharedPreferences.Editor editor = preferences.edit();
            if (editor.putString(PREF_USERNICKNAME, NickName).commit()) {
                userNickName = NickName;
            }
        }
    }


    /**
     * 设置用户名 加密字符串
     *
     * @param username
     */
    // public void setUserName(String username) {
    public void setUser(String username) {
        if (username != null) {


            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(applicationContext);
            SharedPreferences.Editor editor = preferences.edit();

            if (editor.putString(PREF_USERNAME, username).commit()) {
                userName = username;
            }
        }
    }


    /**
     * 设置登录平台信息
     *
     * @param login
     */
    public void setLoginPlatform(String login) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_LOGIN_PLATFORM, login).commit()) {

            login_platform = login;
        }


    }


    /**
     * 获取登录平台信息
     *
     * @return
     */
    public String getLoginPlatform() {

        if (login_platform == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            login_platform = preferences.getString(PREF_LOGIN_PLATFORM, null);
        }

        return login_platform;
    }


    /**
     * 是否第一次进入App
     *
     * @param bol
     */
    public void setIsFirst(boolean bol) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putBoolean(Constant.IS_FIRST, bol).commit()) {
            Constant.IS_FIRST_BOL = bol;
        }

    }

    /**
     * 获取第一次进入APP的状态
     *
     * @param bol
     */
    public Boolean getIsFirst(boolean bol) {

        if (isFirst == null) {

            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(applicationContext);
            isFirst = preferences.getBoolean(Constant.IS_FIRST, bol);

        }

        return isFirst;

    }

    /**
     * 设置下载APK ID
     *
     * @param id
     * @return
     */
    public void setApkId(String id) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(APK_ID, id).commit()) {
            apk_id = id;
        }
    }

    /**
     * 获取下载APK ID
     *
     * @return
     */
    public String getApkId() {
        apk_id = null;
        if (apk_id == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            apk_id = preferences.getString(APK_ID, null);
        }
        return apk_id;
    }

    /**
     * 设置下载APK 名字
     *
     * @param name
     * @return
     */
    public void setApkName(String name) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(APK_NAME, name).commit()) {
            apk_name = name;
        }
    }

    /**
     * 获取下载APK 名字
     *
     * @return
     */
    public String getApkName() {
        apk_name = null;
        if (apk_name == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            apk_name = preferences.getString(APK_NAME, null);
        }
        return apk_name;
    }

    /**
     * 退出登录,清空数据
     */
    public void logout() {

        // MyDbOpenHelper.getInstance(applicationContext).closeDB();
        // reset password to null
        setPassword(null);

    }

    /**
     * 获取APP名称
     *
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 获取当前版本
     *
     * @return
     */
    public String getVersionName() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 获取当前版本号
     *
     * @return
     */
    public int getVersionCode()// 获取版本号(内部识别号)
    {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            return 0;
        }
        /*
         * try { PackageInfo
		 * pi=context.getPackageManager().getPackageInfo(context
		 * .getPackageName(), 0); return pi.versionCode; } catch
		 * (NameNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); return 0; }
		 */
    }

    /**
     * 获取机器码
     *
     * @return
     */
    public String getDeviceId() {

        TelephonyManager mTelephonyManager = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deiviceID = mTelephonyManager.getDeviceId();

        return deiviceID;

    }


    /**
     * 获取当前手机号码
     *
     * @return
     */
    public String getPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) applicationContext.getSystemService(applicationContext.TELEPHONY_SERVICE);

        String phone_number = tm.getLine1Number();

        return phone_number;


    }


    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getScreenWidth() {
        WindowManager windowManager = (WindowManager) applicationContext.getSystemService(applicationContext.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();

        return width;

    }

    /**
     * 获取屏幕高度
     */

    public int getScreenHeight() {
        WindowManager windowManager = (WindowManager) applicationContext.getSystemService(applicationContext.WINDOW_SERVICE);
        int height = windowManager.getDefaultDisplay().getHeight();

        return height;

    }

    /**
     * dip转换px
     */
    public int dip2px(int dip) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 获取当前手机型号
     */

    public String getMobileModel() {

        String mobile_phone = android.os.Build.MODEL;

        return mobile_phone;

    }

    public static void initImageloader(Context context) {

        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                Constant.CACHE_DIR_IMAGE);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // 线程池内加载的数量
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                /***
                 * 新增*** / .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                 * .memoryCacheSize(2 * 1024 * 1024)
                 * .memoryCacheSizePercentage(13) // default /***新增end
                 ***/
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)

                .diskCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
                // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

//	public LocationClient mLocationClient;

    public volatile boolean isFristLocation = true;

    public double mCurrentLantitude;
    public double mCurrentLongitude;

    public float mCurrentAccracy;

//	public MyLocationListener mMyLocationListener;

//	public BDLocation mBdLocation;

    /**
     * 初始化定位相关数据
     */
    public void initMyLocation() {
        // 定位初始化
//		mLocationClient = new LocationClient(applicationContext);
//		mMyLocationListener = new MyLocationListener();
//		mLocationClient.registerLocationListener(mMyLocationListener);
//		// 设置定位的相关配置
//		LocationClientOption option = new LocationClientOption();
//		option.setIsNeedAddress(true);// 允许获取当前地址信息
//		option.setOpenGps(true);// 打开gps
//		option.setCoorType("bd09ll"); // 设置坐标类型
//		option.setScanSpan(1000);
//		mLocationClient.setLocOption(option);
    }

    /**
     * 实现实位回调监听
     */
//	public class MyLocationListener implements BDLocationListener {
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//
//		}
//
//	}

    /**
     * 获取当前地理位置
     *
     * @return
     */
    public String getAddress() {

        String city = null;
//		if (mBdLocation != null) {
//			city = mBdLocation.getCity();
//
//		}

        return city;

    }

    /**
     * 获取当前时间戳
     */
    public String getCurrentdate() {
        String strDate = null;
        Date currentdate = new Date();//当前时间
//		long i = (currentdate.getTime()/1000-timestamp)/(60);
//		System.out.println(currentdate.getTime());
//		System.out.println(i);
        int time = (int) (System.currentTimeMillis());//获取系统当前时间
        strDate = time + "";
        System.out.println("-------------------now-->" + time);//返回结果精确到毫秒。
        Timestamp tsTemp = new Timestamp(time);
        String strTime = tsTemp.toString();

        return strDate;
    }

    private void initOkHttp() {
        //        HttpHeaders headers = new HttpHeaders();
        //        headers.put("commonHeaderKey1", "commonHeaderValue1");    //所有的 header 都 不支持 中文
        //        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        //        params.put("commonParamsKey1", "commonParamsValue1");     //所有的 params 都 支持 中文
        //        params.put("commonParamsKey2", "这里支持中文参数");

        //必须调用初始化
        OkHttpUtils.init(this);
        //以下都不是必须的，根据需要自行选择
        OkHttpUtils.getInstance()//
                .debug("OkHttpUtils")                                              //是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
                //                .setCookieStore(new MemoryCookieStore())                           //cookie使用内存缓存（app退出后，cookie消失）
                .setCookieStore(new PersistentCookieStore())                       //cookie持久化存储，如果cookie不过期，则一直有效
                //                .addCommonHeaders(headers)                                         //设置全局公共头
                .addCommonParams(params);                                          //设置全局公共参数
    }


    /**
     * 使用EMAIL发送日志
     */
    private void initEmailReporter() {
//		CrashEmailReporter reporter = new CrashEmailReporter(this);
////		reporter.setReceiver("408597932@qq.com");
//		reporter.setReceiver("bitter_work@163.com");
//		reporter.setSender("sunqi1006@126.com");
//		reporter.setSendPassword("asdfgh");
//		reporter.setSMTPHost("smtp.126.com");
//		reporter.setPort("465");
//		AndroidCrash.getInstance().setCrashReporter(reporter).init(this);
    }


    public Boolean getIs_jpush() {
        return is_jpush;
    }

    public void setIs_jpush(Boolean is_jpush) {
        this.is_jpush = is_jpush;
    }

    public void setBadgeCount(Context context) {
        int badgecount = 0;
        try {
            badgecount = MyApplication.getInstance().getInfo_num();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        boolean success = ShortcutBadger.applyCount(context, badgecount);

        System.out.println("=====================================Set count====" + badgecount + "========success====" + success);

        //find the home launcher Package
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;

        System.out.println("===============================Launcher APP_launcher======" + currentHomePackage);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        MyApplication.getInstance().setFirst_enter("");
        MyApplication.getInstance().setInfo_new("");
    }
}
