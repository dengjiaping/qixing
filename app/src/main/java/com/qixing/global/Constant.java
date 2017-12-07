package com.qixing.global;



public class Constant {

    /**
     * 缓存目录*
     */
    public static final String CACHE_DIR = "/qixing/";
    /**
     * 缓存图片目录*
     */
    public static final String CACHE_DIR_IMAGE = "/qixing/image";

    /**
     * 更新app下载名字
     * */
    public static final String saveFileName = "qixing";
    /**
     * 更新app下载目录
     * */
    public static final String saveFilePath = "/qixing/download/";
    /**
     * 是否第一次进入
     */
    public static final String IS_FIRST = "is_first";
    /**
     * 是否第一次进入
     */
    public static Boolean IS_FIRST_BOL = true;

    public static final int SPLASH_DISPLAY_LENGHT = 2000;

    String imageUri1 = "http://site.com/image.png"; // 网络图片
    String imageUri2 = "file:///mnt/sdcard/image.png"; //SD卡图片
    String imageUri3 = "content://media/external/audio/albumart/13"; // 媒体文件夹
    String imageUri4 = "assets://image.png"; // assets
    String imageUri5 = "drawable://" ; //  drawable文件


    /**
     * host 地址
     * */
//    public static final String BASE_HOST = "http://qxzb1612.wicep.net:999/";//测试
    public static final String BASE_HOST = "http://app.qixingshidai.com/";

    /**
     * base_url 地址
     * */
    public static final String BASE_URL = BASE_HOST+"index.php/";
    /**
     * host 图片地址
     * */
    public static final String BASE_URL_IMG = BASE_HOST;

    public static final String URL_IMG = "http://app.qixingshidai.com";
    /*
    * 分享成功后回调(视频、直播、资讯)
    * */
    public static final String URL_SHARE_SUCCESS="Index/share_success";

    /**
     * 七星引导页
     * */
    public static final String URL_INDEX_YINDAOPIC = "Index/yindaopic";
    /**
     * 注册
     * */
    public static final String URL_USERAPI_USERREG = "UserApi/userreg";
    /**
     * 获取手机验证码接口
     * */
    public static final String URL_USERAPI_FDX = "UserApi/fdx";
    /**
     * 登录
     * */
    public static final String URL_USERAPI_LOGIN = "UserApi/login";
    /**
     * 忘记密码 手机验证码接口
     * */
    public static final String URL_USERAPI_FORGPWD = "UserApi/forgpwd";
    /**
     * 首页
     * */
    public static final String URL_INDEX_INDEX = "Index/index";
    /**
     * 视频列表
     * */
    public static final String URL_INDEX_VIDEOLIST = "Index/videolist";
    /**
     * 推荐视频
     * */
    public static final String URL_INDEX_TJVIDEO = "Index/tjvideo";
    /**
     * 免费体验课
     * */
    public static final String URL_INDEX_MFVIDEO = "Index/mfvideo";
    /**
     * 会员专区
     * */
    public static final String URL_INDEX_HYVIDEO = "Index/hyvideo";
    /**
     * 咨询列表
     * */
    public static final String URL_INDEX_ZXLIST = "Index/zxlist";
    /**
     * 干货列表
     * */
    public static final String URL_INDEX_GHLIST = "Index/ghlist";

    /**
     * 查看咨询
     * */
    public static final String URL_INDEX_SELZX = "Index/selzx";
    /**
     * 预约直播
     * */
    public static final String URL_USERAPI_YUYUE_ZB = "UserApi/yuyue_zb";

    /**
     * 个人中心
     * */
    public static final String URL_USERAPI_GXINFO = "UserApi/gxinfo";
    /**
     * 个人中心 观看历史
     * */
    public static final String URL_USERAPI_SEEJL = "UserApi/seejl";
    /**
     * 个人中心 观看历史 删除
     * */
    public static final String URL_USERAPI_DELSEE = "UserApi/delsee";
    /**
     * 个人中心 我的收藏
     * */
    public static final String URL_USERAPI_MYFAV = "UserApi/myfav";
    /**
     * 个人中心 我的收藏 删除
     * */
    public static final String URL_USERAPI_DELSHOUC = "UserApi/delshouc";
    /**
     * 直播列表
     * */
    public static final String URL_INDEX_ZHIBOLIST = "UserApi/zhibolist";

    /**
     * 个人中心 绑定推荐人
     * */
    public static final String URL_USERAPI_ADTJR = "UserApi/adtjr";

    /**
     * 个人中心  修改头像
     * */
    public static final String URL_USERAPI_UPPIC = "UserApi/uppic";
    /**
     * 个人中心  用户名修改接口
     * */
    public static final String URL_USERAPI_UPUNAME = "UserApi/upuname";
    /**
     * 个人中心  性别修改接口
     * */
    public static final String URL_USERAPI_UPSEX = "UserApi/upsex";
    /**
     * 个人中心 个性签名修改接口
     * */
    public static final String URL_USERAPI_UPQIANM = "UserApi/upqianm";
    /**
     * 我的账单 充值记录
     * */
    public static final String URL_USERAPI_RECHARGERECORD="UserApi/dou_mx";
    /**
     * 我的账单 星币消费记录
     * */
    public static final String URL_USERAPI_XBXFRECORD="UserApi/dou_huamx";
    /**
     * 直播礼物排行
     * */
    public static final String URL_USERAPI_LIVEGIFTRANK="UserApi/zb_giftlist";
    /**
     * 充值协议
     * */
    public static final String URL_USERAPI_RECHARGEPROTOCOL="UserApi/cz_info";

    /**
     * 个人中心 申请提现
     * */
    public static final String URL_USERAPI_APPLAY_TX = "UserApi/applay_tx";
    /**
     * 个人中心 修改支付密码
     * */
    public static final String URL_USERAPI_UPZFPWD = "UserApi/upzfpwd";
    /**
     * 个人中心 忘记支付密码
     * */
    public static final String URL_USERAPI_FORGZFPWD = "UserApi/forgzfpwd";
    /**
     * 个人中心 银行卡列表
     * */
    public static final String URL_USERAPI_HQBANK = "UserApi/hqbank";
    /**
     * 个人中心 添加银行卡
     * */
    public static final String URL_USERAPI_TJBANK = "UserApi/tjbank";
    /**
     * 个人中心 银行名列表
     * */
    public static final String URL_USERAPI_BANKLIST = "UserApi/banklist";

    /**
     * 个人中心 VIP权益
     * */
    public static final String URL_USERAPI_VIPQY = "UserApi/vip_wz";
    /**
     * 个人中心 分销返利 我要报名
     * */
    public static final String URL_USERAPI_BAOM = "UserApi/baom";
    /**
     * 七星会员注册协议
     * */
    public static final String URL_INDEX_REG_XIEYI = "Index/reg_xieyi";
    /**
     * 个人中心 分销返利 提交报名
     * */
    public static final String URL_USERAPI_APPLY_BAOM = "UserApi/apply_baom";
    /**
     * 提交报名之后访问（微信、支付宝）
     * */
    public static final String URL_USERAPI_SUREDJ = "UserApi/suredj";
    /**
     *  钱包充值
     * */
    public static final String URL_USERAPI_CZMONEY = "UserApi/czmoney";
    /**
     * 提交充值
     * */
    public static final String URL_USERAPI_TJCZ = "UserApi/tjcz";
    /**
     * 意见与反馈
     * */
    public static final String URL_USERAPI_YJFK = "UserApi/yjfk";
    /**
     * 关于我们
     * */
    public static final String URL_INDEX_GYWM = "Index/gywm";


    /**
     * 直播 直播token
     * */
    public static final String URL_USERAPI_BYUSERGETINFO = "UserApi/byusergetinfo";
    /**
     * 直播 设置直播标题及封面
     * */
    public static final String URL_USERAPI_ADDZBINFO = "UserApi/addzbinfo";
    /**
     * 直播  用户观看直播
     * */
    public static final String URL_USERAPI_SEL_ZB = "UserApi/sel_zb";
    /**
     * 直播  用户头像列表,直播头像观看人数直播号，时间
     * */
    public static final String URL_USERAPI_SELZBINFO = "UserApi/selzbinfo";
    /**
     * 直播  用户退出直播
     * */
    public static final String URL_USERAPI_NOSEL_ZB = "UserApi/nosel_zb";
    /**
     * 直播  用户关注直播
     * */
    public static final String URL_USERAPI_FAV_ZB = "UserApi/fav_zb";
    /**
     * 直播  用户取消关注直播
     * */
    public static final String URL_USERAPI_NOFAV_ZB = "UserApi/nofav_zb";
    /**
     * 直播  主播退出直播
     * */
    public static final String URL_USERAPI_OUT_ZB = "UserApi/out_zb";

    /**
     * 用户观看视频
     * */
    public static final String URL_INDEX_SEL_SP = "Index/sel_sp";
    /**
     * 用户收藏视频
     * */
    public static final String URL_INDEX_FAV_SP = "Index/fav_sp";
    /**
     * 用户转发视频 转发量
     * */
    public static final String URL_INDEX_SHARE_NUM = "Index/share_num";

    /**
     * 分享
     * */
    public static final String URL_USERAPI_FXZBXX = "UserApi/fxzbxx";

    /**
     * 搜索视频
     * */
    public static final String URL_INDEX_SOUSUO = "Index/sousuo";
//    用户id	uid
//    标题	title
//    分页	p
    /**
     * 是否可以查看
     * */
    public static final String URL_INDEX_ISNOSEE = "Index/isnosee";

    /**
     * 消息
     * */
    public static final String URL_USERAPI_HDXX = "UserApi/hdxx";
    /**
     * 删除消息
     * */
    public static final String URL_USERAPI_DELMSG="UserApi/delxx";


    /**
     * 送礼物
     * */
    /**
     * 查看用户豆个数
     * */
    public static final String URL_USERAPI_SEL_DOU = "UserApi/sel_dou";
    /**
     * 充值购买豆列表
     * */
    public static final String URL_USERAPI_DOU_MONEY = "UserApi/dou_money";
    /**
     * 购买豆
     * */
    public static final String URL_USERAPI_BUY_DOU = "UserApi/buy_dou";
    /**
     * 送礼物
     * */
    public static final String URL_USERAPI_SEND_GIFTS = "UserApi/send_gifts";
    /**
     * 直播 排名接口：Index/allgifts
     * */
    public static final String URL_INDEX_ALLGIFTS = "Index/allgifts";


    /**
     * 第三方登陆  （微信）
     * */
//    类型	status
//    微信id	openid
//    昵称	nickname
//    性别	sex
//    头像	pic
//    unionid	unionid
    public static final String URL_USERAPI_THIRD_PARTY = "UserApi/third_party";
    /**
     * 第三登录微信绑定手机号
     * */
//    类型	types   (1微信  2支付宝)
//    手机号	phone
//    验证码	yzm
//    登录密码	pwd
//    支付密码	zfpwd
//    微信用户id	uid
    public static final String URL_USERAPI_TJPHOPWD = "UserApi/tjphopwd";
    /**
     * 第三方登录（支付宝）
     * */
//    支付宝id	zfb_id
//    昵称	nickname
//    性别	sex
//    头像	pic
    public static final String URL_USERAPI_ZFBLAST = "UserApi/zfblast";
    //    1.UserApi/zfb_token获取url
    public static final String URL_USERAPI_ZFB_TOKEN = "UserApi/zfb_token";
//    2.UserApi/third_party(status=2）获取支付宝用户信息

//    3.UserApi/zfblast写入数据库

    /**
     * 微信登录
     * */
    public static final String URL_WX_OAUTH2_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    public static final String URL_WX_USERINFO = "https://api.weixin.qq.com/sns/userinfo?";

    /**
     * 版本更新
     * */
    public static final String URL_USERAPI_BANBEN = "UserApi/banben";


    /**
     * 充值记录
     * */
    public static final String URL_CZ_RECORDLIST="UserApi/cz_list";


    /**
     * 积分纪录
     * */
    public static final String URL_JF_RECORDLIST="UserApi/jflist";


    /**
     * 积分充值至钱包
     * */
    public static final String URL_JF_RECHARGE_TO_QIANBAO="UserApi/jf_qb";


    public static final String sysName = "android";


    //for receive customer msg from jpush server
//    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";


    /**
     * RTMP 直播推流
     * */
    public static final String URL_LIVE_UPLIVE = "rtmp://qixingshidai.uplive.ks-cdn.com/live/";
    /**
     * RTMP 直播拉流
     * */
    public static final String URL_LIVE_RTMPLIVE = "rtmp://qixingshidai.rtmplive.ks-cdn.com/live/";

    /**
     *消息Bundle携带参数
     * */
    public static final String EXTRA_BUNDLE = "launchBundle";


    /**
     * *****************************************************支付宝 start********************************************************
     * */
    //开放平台应用的APPID
    public static final String APP_ID_ZFB = "2016121204183688";
    // 商户PID
    public static final String PARTNER = "2088521349029931";
    // 商户收款账号
    public static final String SELLER = "5911324@qq.com";
    // 商户私钥，pkcs8格式
//    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ+sFAMj8iPZWXDr" +
//            "mEZ1pD4OqXLz39zxgfxBANpoK+Vi/IitkU5NRVlsoBpeYu2scLFTeu400926zt9P" +
//            "QzVh7MbOe/VicQJ9d+tMbfdLFDehesIgyiK+tCXeqejz0UXACKTocuEW90J+aWeV" +
//            "q+v1wGDWwpTPCcxr234bGT9PMfNxAgMBAAECgYBEAtznVYlPIcZKJd/Pq05RLJea" +
//            "oiwphSkz1FBTt2lzJI2ifiPVcW5nn7A2axvCVuANeBx1mETiPi7dyXo8I8DhZVdG" +
//            "s4Be5hLi1j37j+eSQjC3/huvumaKVXbC+qvDpTlQ1KCkV1jZcM2HC9Jw/+ScB/ry" +
//            "Sbd+zdFSHCG9iAmeAQJBAMtYAHBlpNdCMmc2Ap7hUmzcVByg3IDwn6FwA3rbKyWi" +
//            "oOWQnAIsSnaLxTuNgRwVeISbn1tG9JTA5m45GMk7/SECQQDJBQO2XGBG9K86cYpU" +
//            "jaDzQm/TEHNn+VmBJLnxxO/ZdOGAoMzvn0zKHE7gjC7e72d1QgAvW96pDoGUNTGF" +
//            "X1xRAkADZo1bU9fV4b2GDFku3wXJ3EVr1STa4ytIP99PBqtKDdYzAKAbSTJVmTDL" +
//            "NiqalaFWVmPr2tvsQO2ZmkrX0rEBAkBbccmHiqxNev+/kWkBH9CI+L8P/9rQALGD" +
//            "uyZPYmxNuwreTcgMdaYkBw6mvX6Q8c1ZnSWiZaN7ulqeBji52HCRAkB21s6LCV8W" +
//            "DTP8jJbpxew+IujWHnBu0a0TDeZ2RwCpWqchZ2DATA7yp2Y5RdcdoCR+I0nX0aUT" +
//            "fVVGWT+2IBjX";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKs/pQXXfOg8U3xr" +
            "fIoAA8M7Uq1KedsQe3KkDWqTWWPcDK6jHO2onvqVWYVtRndpVejg8kQsZJYgYyAY" +
            "slhSeUjrymAhuJi4O9Y++hfINEwa6s47RwwgaqzlHzQa1+2dqLUR2XnoSC2O1utt" +
            "PYl+t9Bm9EjbOcsJ19UL2JQl/B6bAgMBAAECgYBej+VzRVvO7cj4Em6R0ZJCU1bD" +
            "JDYHtqFEW7yg1j7tOoyyPpXx0kP/gUsOt5nKxJrQ3q5I4O6mS7oQ9jI9PUWFDID8" +
            "lGfouX+JNLZSS6nadprlRUI0zpp9T/nLqUo194nBOnjeKXkosxBKyL0vbxVfqSok" +
            "FMN7unKPXMaQFPk7+QJBANeOmOinx3EbERbml2Zles5ZQLeb7xxY0K0f0WhBvVqe" +
            "SxaWy/irb2GWaEIZi11Nv+wYnJ9YGAPuqDtVe66k1/UCQQDLYN+gFmtNu7alZRtW" +
            "aSDZiUGCroeUXqCo4E/olU1t/R3Xu/q8Vn1yxmqrzy5+TdOusMR0s+8uPp2n+MA6" +
            "jVJPAkEAss+ynoTXmEbotUOR4jpaViSJ4AX1o2E1sfeMAd1O04YuKc3r5BtqaNyZ" +
            "HgQMrNibVaHRAwnT2ic23Ql7cRODpQJACQVsBfLYoaMQ7/czJjsVjPCsqjpJo0gr" +
            "QjJo+P0eaSGQIFCjR5VijSPF+YcAHfQfzqDipfaRRnDal2fORwqbuwJBAKUeaw5k" +
            "6zmIOiH9Ik/Fi6A/b4ZY7ZHxCIZIsaHgHd/IfBi0mg/OYNBLp36qtBoxsCeGW6zB" +
            "miIIftaIGN55yGM=";

    // 支付宝公钥
//    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfrBQDI/Ij2Vlw65hGdaQ+Dqly" +
//            "89/c8YH8QQDaaCvlYvyIrZFOTUVZbKAaXmLtrHCxU3ruNNPdus7fT0M1YezGznv1" +
//            "YnECfXfrTG33SxQ3oXrCIMoivrQl3qno89FFwAik6HLhFvdCfmlnlavr9cBg1sKU" +
//            "zwnMa9t+Gxk/TzHzcQIDAQAB";
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrP6UF13zoPFN8a3yKAAPDO1Kt" +
            "SnnbEHtypA1qk1lj3AyuoxztqJ76lVmFbUZ3aVXo4PJELGSWIGMgGLJYUnlI68pg" +
            "IbiYuDvWPvoXyDRMGurOO0cMIGqs5R80Gtftnai1Edl56EgtjtbrbT2JfrfQZvRI" +
            "2znLCdfVC9iUJfwemwIDAQAB";

    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;
    public static final int SDK_USERINFO_FLAG = 3;
    public static final String CHARSET = "UTF-8";

    /**
     * *****************************************************支付宝 end********************************************************
     * */

    /**
     * *****************************************************微信支付 start********************************************************
     * */
    //微信平台 appid 应用从官方网站申请到的合法appId
    public static final String APP_ID = "wx0991ee14e8592c3e";//微信平台ID
    public static final String MCH_ID = "1422587602";//微信商户平台商户号
    /**
     * 微信秘钥
     */
    public static final String KEY = "1422587602wx0991ee14e8592c3eqxsd";//微信秘钥 32位
    public static final String WEIXIN_SECRET = "21a411cd320f74b85e3b0ae951856d2f";

    /**
     * *****************************************************微信支付 end********************************************************
     * */

    /**
     * *****************************************************QQ start********************************************************
     * */
    public static final String APP_ID_QQ = "1105895250";//QQ平台ID
    public static final String APP_KEY_QQ = "CWnxgkj26cfKaOz2";//QQ秘钥
    /**
     * *****************************************************QQ end********************************************************
     * */


}
