package com.qixing.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.adapter.DialogGiftAdapter;
import com.qixing.adapter.DialogRechargeAdapter;
import com.qixing.adapter.LayoutAdapter;
import com.qixing.adapter.MyRebateAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.GiftBean;
import com.qixing.bean.MyRebateBean;
import com.qixing.bean.RechargeBean;
import com.qixing.bean.RechargeJson;
import com.qixing.global.Constant;
import com.qixing.qxlive.gift.CustomRoundView;
import com.qixing.qxlive.gift.GiftDateUtlis;
import com.qixing.qxlive.gift.MagicTextView;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.MyGridView;
import com.qixing.view.MyListView;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 * 加盟快优
 */
public class TestActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private PullToRefreshScrollView mScrollView;
    private TextView tv_content, tv_phone;
    private Button btn_call;

    private int p = 1;

    private String state = "1";

    private MyListView mListView;

    private List<MyRebateBean> list;
    private MyRebateAdapter mMyRebateAdapter;

//    private JoinBean mJson;

    /**
     * 送礼物
     * */
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

    private TextView tvSendone;
    private TextView tvSendtwo;
    private TextView tvSendthree;
    private TextView tvSendfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_test, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        initView();
        initGift();
//        initDatas();
        initTestDate();
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("测试");
        mTitleBar.setDelegate(new BGATitlebar.BGATitlebarDelegate() {

            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }


            @Override
            public void onClickRightCtv() {
                super.onClickRightCtv();
            }
        });
        mScrollView = (PullToRefreshScrollView) findViewById(R.id.mScrollview);
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
//        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
//            @Override
//            public void onRefresh(PullToRefreshBase refreshView) {
//                isRefresh = true;
//                initDatas();
//            }
//        });
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
//                p = 1;
//                isRefresh = true;
//                initDatas();
            }


            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
//                isRefresh = true;
//                initMoreDatas();
            }

        });

        tv_content = (TextView) findViewById(R.id.join_tv_content);
        tv_phone = (TextView) findViewById(R.id.join_tv_phone);
        btn_call = (Button) findViewById(R.id.join_btn_call);
        btn_call.setOnClickListener(this);

        mListView = (MyListView)findViewById(R.id.mListView);
        mListView.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }

    private void initDatas() {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
//        params.put("uid", MyApplication.getInstance().getUid());
//        params.put("status", "2");//+"/p/"+p
//        final String url = Constant.BASE_URL + Constant.URL_USERAPI_DOU_MONEY;
        params.put("phone", "18612523501");
        final String url = "http://app.qexic.com/app.php/User/ajaxphone/type/1";
        //http://app.qexic.com/index.php/User/ajaxphone/type/1/phone/18612523501
        System.out.println("===========================个人中心 测试 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================个人中心 测试 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {

                } else {
                    showErrorDialog(mContext);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }
        });
    }

    private List<GiftBean> giftList = new ArrayList<GiftBean>();
    private MyGridView gv_gift;
    private DialogGiftAdapter mDialogGiftAdapter;
    private int select_gift = -1;
    private void showGiftDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_gift_view, null);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(mContext, R.style.common_dialog);
        dialog.setContentView(view);
        dialog.show();

        // 监听
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.view_gift_weixin:
                        System.out.println("========================== 打开充值dialog " );
//                        showRechargeDialog();
                        break;

                    case R.id.view_gift_pengyou:
                        // 分享到朋友圈
                        break;

                    case R.id.dialog_gift_btn_gifts:
                        // 取消
                        System.out.println("========================== 礼物 赠送 " );
                        if(select_gift<0){
                            Toasts.show("请选择您要赠送的礼物");
                        }else{
//                            Toasts.show(giftList.get(select_gift).getMsg());
                            showGift(MyApplication.getInstance().getUid()+giftList.get(select_gift).getMsg(),MyApplication.getInstance().getUid(),giftList.get(select_gift).getMsg(),giftList.get(select_gift).getImgID());
                        }
//                        dialog.dismiss();
                        break;

                }
            }

        };
        ViewGroup mViewgift1 = (ViewGroup) view.findViewById(R.id.view_gift_weixin);
        ViewGroup mViewgift2 = (ViewGroup) view.findViewById(R.id.view_gift_pengyou);
        Button btn_gifts = (Button) view.findViewById(R.id.dialog_gift_btn_gifts);
        btn_gifts.setTextColor(getResources().getColor(R.color.white_F));
        mViewgift1.setOnClickListener(listener);
        mViewgift2.setOnClickListener(listener);
        btn_gifts.setOnClickListener(listener);

        gv_gift = (MyGridView) view.findViewById(R.id.dialog_gift_gv_gift);
        gv_gift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<giftList.size();i++){
                    if(i==position){
                        giftList.get(i).setSelect("1");
                    }else{
                        giftList.get(i).setSelect("0");
                    }
                    select_gift = position;
                }
                System.out.println("========================== 选择了 " +giftList.get(position).getMsg());
                mDialogGiftAdapter.notifyDataSetChanged();
            }
        });
        select_gift = -1;
        giftList.clear();
        giftList = GiftDateUtlis.initGiftDate();//初始化礼物列表
        mDialogGiftAdapter = new DialogGiftAdapter(mContext,giftList);
        gv_gift.setAdapter(mDialogGiftAdapter);

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

    }

    private TextView tv_wx = null,tv_zfb = null;
    private TextView rechrge_tv_bi = null;
    private View view1 = null,view2  = null;
    private final boolean[] isWX = {true};
    private final boolean[] isZFB = {false};
    private int select = -1;
    private void showRechargeDialog() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_recharge_view, null);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(mContext, R.style.recharge_dialog);
        dialog.setContentView(view);
        dialog.show();

        // 监听
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.dialog_recharge_view_wx:
                        if(isWX[0]){

                        }else{
                            isWX[0] = true;
                            isZFB[0] = false;
                            tv_wx.setTextColor(getResources().getColor(R.color.color_titlebar_default));
                            view1.setVisibility(View.VISIBLE);
                            tv_zfb.setTextColor(getResources().getColor(R.color.black));
                            view2.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.dialog_recharge_view_zfb:
                        if(isZFB[0]){

                        }else{
                            isWX[0] = false;
                            isZFB[0] = true;
                            tv_wx.setTextColor(getResources().getColor(R.color.black));
                            view1.setVisibility(View.GONE);
                            tv_zfb.setTextColor(getResources().getColor(R.color.color_titlebar_default));
                            view2.setVisibility(View.VISIBLE);
                        }
                        break;

                    case R.id.dialog_rechrge_btn_confirm:
                        // 充值 确认
                        System.out.println("========================== 充值 确认 " );
                        if(select<0){
                            Toasts.show("请选择充值金额");
                        }else{
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

        gv_money = (MyGridView) view.findViewById(R.id.dialog_recharge_gv_money);
        gv_money.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<listRecharge.size();i++){
                    if(i==position){
                        listRecharge.get(i).setSelect("1");
                    }else{
                        listRecharge.get(i).setSelect("0");
                    }
                    select = position;
                }
                mDialogRechargeAdapter.notifyDataSetChanged();
            }
        });

        rechrge_tv_bi = (TextView) view.findViewById(R.id.dialog_rechrge_tv_bi);

        Button btn_confirm = (Button) view.findViewById(R.id.dialog_rechrge_btn_confirm);
        btn_confirm.setTextColor(getResources().getColor(R.color.white_F));
        mViewWX.setOnClickListener(listener);
        mViewZFB.setOnClickListener(listener);
        btn_confirm.setOnClickListener(listener);

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        initXingbiDatas();
    }


    private void initGift(){
        llgiftcontent = (LinearLayout) findViewById(R.id.llgiftcontent);

        tvSendone = (TextView) findViewById(R.id.tvSendone);
        tvSendtwo = (TextView) findViewById(R.id.tvSendtwo);
        tvSendthree = (TextView) findViewById(R.id.tvSendthree);
        tvSendfor = (TextView) findViewById(R.id.tvSendfor);
        tvSendone.setOnClickListener(this);
        tvSendtwo.setOnClickListener(this);
        tvSendthree.setOnClickListener(this);
        tvSendfor.setOnClickListener(this);

        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_out);
        giftNumAnim = new NumAnim();
        clearTiming();
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
                public void onViewAttachedToWindow(View view) { }
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
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                llgiftcontent.removeViewAt(index);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
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
    private void showGift(String tag,String name,String msg,int imgID) {
        View giftView = llgiftcontent.findViewWithTag(tag);
        if (giftView == null) {/*该用户不在礼物显示列表*/

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
            if(!TextUtils.isEmpty(MyApplication.getInstance().getUser_Head())){
                if(MyApplication.getInstance().getUser_Head().startsWith("http")){
                    ImageLoader.getInstance().displayImage(MyApplication.getInstance().getUser_Head(),crvheadimage, ImageLoaderOptions.get_face_Options());
                }else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), crvheadimage, ImageLoaderOptions.get_face_Options());
                }
//			ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), img_head, ImageLoaderOptions.get_face_Options());
                System.out.println("===========================个人中心 User_Head = " + Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head());
            }
            final TextView item_gift_tv_name = (TextView) giftView.findViewById(R.id.item_gift_tv_name);/*找到name*/
            item_gift_tv_name.setText(name);
            final TextView item_gift_tv_msg = (TextView) giftView.findViewById(R.id.item_gift_tv_msg);/*找到name*/
            item_gift_tv_msg.setText(msg);
            final ImageView gift_img_gift = (ImageView) giftView.findViewById(R.id.gift_img_gift);/*找到name*/
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
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumAnim.start(giftNum);
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        } else {/*该用户在礼物显示列表*/
            CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);/*找到头像控件*/
            if(!TextUtils.isEmpty(MyApplication.getInstance().getUser_Head())){
                if(MyApplication.getInstance().getUser_Head().startsWith("http")){
                    ImageLoader.getInstance().displayImage(MyApplication.getInstance().getUser_Head(),crvheadimage, ImageLoaderOptions.get_face_Options());
                }else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), crvheadimage, ImageLoaderOptions.get_face_Options());
                }
//			ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head(), img_head, ImageLoaderOptions.get_face_Options());
                System.out.println("===========================个人中心 User_Head = " + Constant.BASE_URL_IMG + MyApplication.getInstance().getUser_Head());
            }
            final TextView item_gift_tv_name = (TextView) giftView.findViewById(R.id.item_gift_tv_name);/*找到name*/
            item_gift_tv_name.setText(name);
            final TextView item_gift_tv_msg = (TextView) giftView.findViewById(R.id.item_gift_tv_msg);/*找到name*/
            item_gift_tv_msg.setText(msg);
            final ImageView gift_img_gift = (ImageView) giftView.findViewById(R.id.gift_img_gift);/*找到name*/
            gift_img_gift.setImageDrawable(getResources().getDrawable(imgID));
            MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            int showNum = (Integer) giftNum.getTag() + 1;
            giftNum.setText("x"+showNum);
            giftNum.setTag(showNum);
            crvheadimage.setTag(System.currentTimeMillis());
            giftNumAnim.start(giftNum);
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
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX",1.3f, 1.0f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",1.3f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            lastAnimator = animSet;
            animSet.setDuration(200);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(anim1, anim2);
            animSet.start();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
    }

    private DialogRechargeAdapter mDialogRechargeAdapter;
    private RechargeJson mRechargeJson;
    private List<RechargeBean> listRecharge = new ArrayList<RechargeBean>();
    private MyGridView gv_money;

    private void initXingbiDatas() {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
//        params.put("uid", MyApplication.getInstance().getUid());
//        params.put("status", "2");//+"/p/"+p
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_DOU_MONEY;
        System.out.println("===========================个人中心 测试 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================个人中心 测试 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mRechargeJson = new Gson().fromJson(response.toString(), RechargeJson.class);
                    if (mRechargeJson.getResult().equals("1")) {
//                        listRecharge = mRechargeJson.getAndlist();
                        listRecharge.clear();
                        select = -1;
                        for (int i=0;i<4;i++){
                            RechargeBean mRechargeBean = mRechargeJson.getAndlist().get(i);
                            listRecharge.add(mRechargeBean);
                        }
                        mDialogRechargeAdapter = new DialogRechargeAdapter(mContext,listRecharge);
                        gv_money.setAdapter(mDialogRechargeAdapter);
                    } else {
                        Toasts.show(mRechargeJson.getMessage());
                    }
                } else {
                    showErrorDialog(mContext);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }
        });
    }

    private void initMoreDatas() {
//        final RequestParams params = new RequestParams();
////        params.put("uid",  MyApplication.getInstance().getUid());
////        params.put("static", state);
//        // +"/p/"+(p+1)
//        final String url = Constant.BASE_URL+Constant.URL_APP_NKCX;
//        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//
//                if (mProgressDialog != null) {
//                    mProgressDialog.dismiss();
//                }
//                if (isRefresh) {
//                    isRefresh = false;
//                    mScrollView.onRefreshComplete();
//                }
//                System.out.println("===========================url ==== " + url);
//                System.out.println("===========================p ===== " + p);
//                System.out.println("===========================params ===== " + params.toString());
//                System.out.println("===========================个人中心 优惠券更多 response ===== " + response.toString());
//                if (!TextUtils.isEmpty(response.toString())) {
////                    mJson = new Gson().fromJson(response.toString(), JinXiBiJson.class);
////                    if (mJson.getResult().equals("1")) {
////                        if (mJson.getList().size() == 0 || mJson.getList() == null || "".equals(mJson.getList())) {
////                            Toasts.show("暂无更多数据");
////                        } else {
////                            mListView.setVisibility(View.VISIBLE);
////                            tv_noolder.setVisibility(View.GONE);
////                            list.addAll(mJson.getList());
////                            p = p+1;
////                            System.out.println("===========================mJson.getList().size() = " + mJson.getList().size());
////                            mAdapter.notifyDataSetChanged();
////                        }
////                    } else {
////                        Toasts.show(mJson.getMessage());
////                    }
//                } else {
//                    showErrorDialog(mContext);
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                if (mProgressDialog != null) {
//                    mProgressDialog.dismiss();
//                }
//                if (isRefresh) {
//                    isRefresh = false;
//                    mScrollView.onRefreshComplete();
//                }
//                showErrorDialog(mContext);
//            }
//        });
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


    private void rechargeXingBi(int select) {
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());//    用户id	uid
        params.put("money", listRecharge.get(select).getMoney());//    钱	money
        params.put("num", listRecharge.get(select).getNum());//    个数	num
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_BUY_DOU;
        System.out.println("===========================个人中心 充值 url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================个人中心 测试 response ===== " + response.toString());
//                if (!TextUtils.isEmpty(response.toString())) {
//                    mRechargeJson = new Gson().fromJson(response.toString(), RechargeJson.class);
//                    if (mRechargeJson.getResult().equals("1")) {
//                    } else {
//                        Toasts.show(mRechargeJson.getMessage());
//                    }
//                } else {
//                    showErrorDialog(mContext);
//                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.join_btn_call:
//                if (!TextUtils.isEmpty(mJson.getLists().getIphone())) {
//                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mJson.getLists().getIphone()));
//                    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)) {
//                        //has permission, do operation directly
//                        startActivity(intent);
//                    } else {
//
//                    }
//                }
//                showGiftDialog();
                initDatas();
                break;
            case R.id.tvSendone:/*礼物1*/
//                showGift("Johnny1");
                break;
            default:
                break;
        }
    }

    private void initTestDate(){
        list = new ArrayList<MyRebateBean>();
        for(int i=0 ; i<15;i++){
            MyRebateBean mMyRebateBean = new MyRebateBean();
            mMyRebateBean.setNum(i+1+"");
            mMyRebateBean.setTime("2016-12-07");
            mMyRebateBean.setName("七星时代");
            mMyRebateBean.setLv("普通会员");
            list.add(mMyRebateBean);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
