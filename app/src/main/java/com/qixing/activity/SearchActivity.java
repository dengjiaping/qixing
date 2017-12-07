package com.qixing.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.activity.webview.MyWebActivity;
import com.qixing.adapter.MoreVideoAdapter;
import com.qixing.adapter.SearchAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.BannerVideoBean;
import com.qixing.bean.F2RecommendedVideoJson;
import com.qixing.bean.ResultBean;
import com.qixing.bean.SearchBean;
import com.qixing.bean.SearchJson;
import com.qixing.global.Constant;
import com.qixing.view.MyListView;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 * 搜索页面
 */
public class SearchActivity extends BaseActivity  {
    // 导航栏
    private LinearLayout ll_left,ll_right;
    private TextView tv_title;
    private ImageView left_img,right_img;
    private TextView tv_searchtype;
    private EditText edit_search;

    private String str_nickname;

    private PullToRefreshScrollView mScrollView;
    private MyListView mListView;
    private TextView tv_noolder;

    private SearchJson mSearchJson;
    private List<SearchBean> list;
    private SearchAdapter mAdapter;
    private boolean isRefresh = false;
    private int p = 1;

    private String seenum,sharenum;

    private ResultBean mResultBean;
    private BannerVideoBean mBannerVideoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_search, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        initView();
    }

    private void initView(){

        // 导航栏
        ll_left = (LinearLayout)findViewById(R.id.title_ll_left);
        ll_left.setOnClickListener(this);
        ll_left.setVisibility(View.VISIBLE);
        left_img = (ImageView)findViewById(R.id.title_left_img);
        left_img.setVisibility(View.VISIBLE);
        tv_title = (TextView)findViewById(R.id.title_center_tv_title);
        tv_title.setText("首页");
        tv_title.setVisibility(View.GONE);
        edit_search = (EditText)findViewById(R.id.title_center_edit_search);
        edit_search.setVisibility(View.VISIBLE);


        tv_searchtype = (TextView)findViewById(R.id.title_center_tv_searchtype);
        tv_searchtype.setOnClickListener(this);
        tv_searchtype.setVisibility(View.GONE);

        ll_right = (LinearLayout)findViewById(R.id.title_ll_right);
        ll_right.setOnClickListener(this);
        ll_right.setVisibility(View.VISIBLE);
        right_img = (ImageView)findViewById(R.id.title_right_img);
        right_img.setVisibility(View.VISIBLE);

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
//                initVideoDate(list.get(position).getId());
                seenum = list.get(position).getSee_num();
                sharenum = list.get(position).getShare_num();
                isnosee("2",list.get(position).getId(),"");//类型（1直播  2视频  3资讯  4干货 ）
            }
        });
        tv_noolder = (TextView)findViewById(R.id.tv_noolder);
        mListView.setVisibility(View.GONE);


    }


    private void initDatas(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
        }
        if(!isRefresh){
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("title", str_nickname);//+"/p/"+p
        final String url = Constant.BASE_URL+Constant.URL_INDEX_SOUSUO+"/p/"+p;//
        System.out.println("===========================搜索列表 url ===== " + url);
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
                System.out.println("===========================搜索列表 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mListView.setVisibility(View.VISIBLE);
                    mSearchJson = new Gson().fromJson(response.toString(), SearchJson.class);
                    if (mSearchJson.getResult().equals("1")) {
                        if (mSearchJson.getList().size() == 0 || mSearchJson.getList() == null || "".equals(mSearchJson.getList())) {
                            mListView.setVisibility(View.GONE);
                            tv_noolder.setVisibility(View.VISIBLE);
                        } else {
                            mListView.setVisibility(View.VISIBLE);
                            tv_noolder.setVisibility(View.GONE);
                            list = mSearchJson.getList();
                            mAdapter = new SearchAdapter(mContext, list);
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mSearchJson.getMessage());
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
                showTimeoutDialog(mContext);
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
                showErrorDialog(mContext);
            }
        });
    }

    private void initMoreDatas(){
        final RequestParams params = new RequestParams();
        params.put("uid",  MyApplication.getInstance().getUid());
        final String url = Constant.BASE_URL+Constant.URL_INDEX_SOUSUO+"/p/"+(p+1);//
        System.out.println("===========================搜索列表 更多 url ==== " + url);
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

                System.out.println("===========================搜索列表 更多 response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mSearchJson = new Gson().fromJson(response.toString(), SearchJson.class);
                    if (mSearchJson.getResult().equals("1")) {
                        if (mSearchJson.getList().size() == 0 || mSearchJson.getList() == null || "".equals(mSearchJson.getList())) {
                            Toasts.show("暂无更多数据");
                        } else {
                            mListView.setVisibility(View.VISIBLE);
                            tv_noolder.setVisibility(View.GONE);
                            list.addAll(mSearchJson.getList());
                            p = p+1;
                            System.out.println("===========================mJson.getList().size() = " + mSearchJson.getList().size());
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mSearchJson.getMessage());
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
                showTimeoutDialog(mContext);
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
                showErrorDialog(mContext);
            }
        });
    }

    private void initVideoDate(final String spid){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
        }
        mProgressDialog.show();
        if(!isRefresh){
        }
        RequestParams params = new RequestParams();
        params.put("uid",  MyApplication.getInstance().getUid());
        params.put("spid",  spid);
        final String url = Constant.BASE_URL+Constant.URL_INDEX_SEL_SP;
        System.out.println("===========================用户观看视频 url ======= " + url);
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

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
                System.out.println("===========================用户观看视频 response ======= " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mBannerVideoBean = new Gson().fromJson(response.toString(), BannerVideoBean.class);
                    if (mBannerVideoBean.getResult().equals("1")) {
                        Intent intent = new Intent();
                        intent.putExtra("path_video",Constant.BASE_URL_IMG + mBannerVideoBean.getSpinfo().getV_url());
                        intent.putExtra("isgz",mBannerVideoBean.getIsgz());
                        intent.putExtra("sp_nr",mBannerVideoBean.getSpinfo().getSp_nr());
                        intent.putExtra("spid",spid);
                        intent.putExtra("seenum",seenum);
                        intent.putExtra("sharenum",sharenum);
                        AppManager.getAppManager().startNextActivity(mContext, PlayVideoActivity.class, intent);

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
                if(isRefresh){
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
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
                if(isRefresh){
                    isRefresh = false;
                    mScrollView.onRefreshComplete();
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
//                            getTokenFromSever(id,str_url);
                        }else if("2".equals(type)){
                            initVideoDate(id);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_ll_left://
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.title_ll_right://搜索
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 关闭软键盘
                imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
                str_nickname = edit_search.getText().toString().trim();
                if(TextUtils.isEmpty(str_nickname)){
                    Toasts.show("请输入搜索的内容");
                }else{
                    p = 1;
                    initDatas();
                }
                break;
        }
    }


}
