package com.qixing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.qixing.R;
import com.qixing.activity.webview.MyWebActivity;
import com.qixing.adapter.SeeQXliveAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.ResultBean;
import com.qixing.bean.SeeQXliveBean;
import com.qixing.bean.SeeQXliveJson;
import com.qixing.global.Constant;
import com.qixing.qxlive.QXLiveSee2Activity;
import com.qixing.qxlive.QXLiveSeeActivity;
import com.qixing.qxlive.rongyun.LiveKit;
import com.qixing.qxlive.rongyun.fakeserver.FakeServer;
import com.qixing.view.MyListView;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by wicep on 2015/12/23.
 *
 */
public class QXliveActivity extends BaseActivity {

    private BGATitlebar mTitleBar;

    private PullToRefreshScrollView mScrollView;
    private MyListView mListView;
    private TextView tv_noolder;

    private int zb_position;
    private SeeQXliveJson mJson;
    private List<SeeQXliveBean> list;
    private SeeQXliveAdapter mAdapter;
    private boolean isRefresh = false;
    private int p = 1;

    private ResultBean mResultBean;

    private String tag = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_qxlive, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        tag = getIntent().getStringExtra("tag");

        initView();
        initDatas();
//        initTestInfo();
    }

    private void initView(){
        mTitleBar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("推荐直播");
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
        mScrollView = (PullToRefreshScrollView)findViewById(R.id.mScrollview);
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
                p = 1;
                isRefresh = true;
                initDatas();
            }


            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                isRefresh = true;
                initMoreDatas();
            }

        });

        mListView = (MyListView)findViewById(R.id.mListView);
        mListView.setFocusable(false);// scrollview嵌套listview运行后最先显示出来的位置不在顶部问题
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                getTokenFromSever(list.get(position).getId(),"rtmp://wicep.rtmplive.ks-cdn.com/live/"+list.get(position).getId());
//                getTokenFromSever(list.get(position).getId(),Constant.URL_LIVE_RTMPLIVE +list.get(position).getId());
                zb_position = position;
                isnosee("1",list.get(position).getId(),Constant.URL_LIVE_RTMPLIVE + list.get(position).getId());//类型（1直播  2视频  3资讯  4干货 ）
            }
        });
        tv_noolder = (TextView)findViewById(R.id.tv_noolder);

    }

    private void initDatas(){
//        initTestData();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            if(!isRefresh){
                mProgressDialog.show();
            }
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("type", "1");//类型（ 1所有直播  2关注的直播  3历史查看直播）
//        params.put("status", "2");//+"/p/"+p
        final String url = Constant.BASE_URL+Constant.URL_INDEX_ZHIBOLIST+"/p/"+p;//
        System.out.println("===========================直播列表url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

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
                System.out.println("===========================直播列表 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mJson = new Gson().fromJson(response.toString(), SeeQXliveJson.class);
                    if (mJson.getResult().equals("1")) {
//                         tv_juan1,tv_juan2,tv_juan3;
                        if (mJson.getList().size() == 0 || mJson.getList() == null || "".equals(mJson.getList())) {
                            mListView.setVisibility(View.GONE);
                            tv_noolder.setVisibility(View.VISIBLE);
                        } else {
                            mListView.setVisibility(View.VISIBLE);
                            tv_noolder.setVisibility(View.GONE);
                            list = mJson.getList();
                            mAdapter = new SeeQXliveAdapter(mContext, list);
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mJson.getMessage());
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
                if (isRefresh) {
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }
                showErrorDialog(mContext);
            }
        });
    }

    private void initMoreDatas(){
        final RequestParams params = new RequestParams();
        params.put("uid",  MyApplication.getInstance().getUid());
        params.put("type", "1");//类型（ 1所有直播  2关注的直播  3历史查看直播）
        // +"/p/"+(p+1)
        final String url = Constant.BASE_URL;//+Constant.URL_APP_YHJLIST
        System.out.println("===========================推荐直播 更多 url ==== " + url);
        System.out.println("===========================p ===== " + p);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

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

                System.out.println("===========================推荐直播 更多 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
//                    mJson = new Gson().fromJson(response.toString(), JinXiBiJson.class);
//                    if (mJson.getResult().equals("1")) {
//                        if (mJson.getList().size() == 0 || mJson.getList() == null || "".equals(mJson.getList())) {
//                            Toasts.show("暂无更多数据");
//                        } else {
//                            mListView.setVisibility(View.VISIBLE);
//                            tv_noolder.setVisibility(View.GONE);
//                            list.addAll(mJson.getList());
//                            p = p+1;
//                            System.out.println("===========================mJson.getList().size() = " + mJson.getList().size());
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    } else {
//                        Toasts.show(mJson.getMessage());
//                    }
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
                if (isRefresh) {
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
                }
                showErrorDialog(mContext);
            }
        });
    }


    private void getTokenFromSever(final String zbid,final String rtmp_url) {
        final UserInfo user = FakeServer.getUserInfo();
        System.out.println("===========================getTokenFromSever ===== " );
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_BYUSERGETINFO;
        System.out.println("===========================直播 融云获取token url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

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
                                seeLive(zbid,rtmp_url);
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
                    showErrorDialog(mContext);
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }
        });
    }

    /**
     * 用户观看直播
     * */
    private void seeLive(final String zbid,final String rtmp_url) {
        System.out.println("===========================getTokenFromSever ===== " );
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("zbid", zbid);
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_SEL_ZB;
        System.out.println("===========================用户观看直播token url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

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
                        if(Integer.parseInt(list.get(zb_position).getApp_types()) == 1){// 1手机
                            intent.setClass(QXliveActivity.this, QXLiveSeeActivity.class);
                        }else if(Integer.parseInt(list.get(zb_position).getApp_types()) == 2){// 2摄像机
                            intent.setClass(QXliveActivity.this, QXLiveSee2Activity.class);
                        }
                        intent.putExtra(QXLiveSeeActivity.LIVE_URL, rtmp_url);
                        intent.putExtra(QXLiveSeeActivity.LIVE_ID, zbid);
                        intent.putExtra(QXLiveSeeActivity.LIVE_UID, mResultBean.getZbuid()+"");
                        intent.putExtra(QXLiveSeeActivity.LIVE_GZ, mResultBean.getIsgz()+"");
                        intent.putExtra(QXLiveSeeActivity.LIVE_DOU, mResultBean.getDoudou()+"");
                        startActivity(intent);

                    } else {
                        Toasts.show(mResultBean.getMessage());
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
                showTimeoutDialog(mContext);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }
        });
    }

    /**
     * 用户是否可以观看
     * */
    private void isnosee(final String type,final String id,final String str_url) {
        System.out.println("===========================getTokenFromSever ===== " );
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
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
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

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
                            getTokenFromSever(id,str_url);
                        }else if("2".equals(type)){
//                            initVideoDate(id);
                        }else if("3".equals(type)){
                            Intent intent = new Intent();
                            intent.putExtra("url", Constant.BASE_URL + str_url);
                            AppManager.getAppManager().startNextActivity(mContext, MyWebActivity.class, intent);
                        }else if("4".equals(type)){
                            Intent intent = new Intent();
                            intent.putExtra("pdfUrl", Constant.BASE_URL_IMG + str_url);
                            AppManager.getAppManager().startNextActivity(mContext, ReadPDFActivity.class, intent);
                        }
                    } else {
                        Toasts.show(mResultBean.getMessage());
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
                showTimeoutDialog(mContext);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("===========================throwable ,responseString =  " + responseString.toString());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
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


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
