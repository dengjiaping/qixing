package com.qixing.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.activity.webview.MyWebActivity;
import com.qixing.adapter.HistoryAdapter;
import com.qixing.adapter.MyCollectionAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.BannerVideoBean;
import com.qixing.bean.HistoryBean;
import com.qixing.bean.HistoryJson;
import com.qixing.bean.MyCollectionBean;
import com.qixing.bean.MyCollectionJson;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.view.pulltorefreshswipemenu.PullToRefreshSwipeMenuListView;
import com.qixing.view.pulltorefreshswipemenu.pulltorefresh.RefreshTime;
import com.qixing.view.pulltorefreshswipemenu.swipemenulistview.SwipeMenu;
import com.qixing.view.pulltorefreshswipemenu.swipemenulistview.SwipeMenuCreator;
import com.qixing.view.pulltorefreshswipemenu.swipemenulistview.SwipeMenuItem;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/23.
 */
public class MyCollectionActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener {

    private BGATitlebar mTitleBar;

    private PullToRefreshSwipeMenuListView mListView;
    private TextView tv_noolder;

    private MyCollectionAdapter mAdapter;
    private List<MyCollectionBean> list;
    private MyCollectionJson mMyCollectionJson;

    private ResultBean mResultBean;
    private BannerVideoBean mBannerVideoBean;

    private boolean isRefresh = false;
    private int p = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_my_collection, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        initView();
        initDatas();
    }

    private void initView(){
        mTitleBar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText("我的收藏");
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
        tv_noolder = (TextView)findViewById(R.id.collect_shop_tv_noolder);
        mListView = (PullToRefreshSwipeMenuListView)findViewById(R.id.mListView);

    }

    private void initDatas(){
//        initLiveData();
        getRefreshDate();

        initSwipeMenuListView();
    }

    private void getRefreshDate(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            if(!isRefresh){
                mProgressDialog.show();
            }
        }
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
//        params.put("p", "1");
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_MYFAV+"/p/"+p;//
        System.out.println("===========================我的收藏url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (isRefresh) {
                    isRefresh = false;
                    mListView.stopRefresh();
                }

                System.out.println("===========================我的收藏 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {//CollectCommodityBean
                    mMyCollectionJson = new Gson().fromJson(response.toString(), MyCollectionJson.class);
                    if (mMyCollectionJson.getResult().equals("1")) {
                        if (mMyCollectionJson.getList().size() == 0 || mMyCollectionJson.getList() == null || "".equals(mMyCollectionJson.getList())) {
                            mListView.setVisibility(View.GONE);
                            tv_noolder.setVisibility(View.VISIBLE);
                        } else {
                            mListView.setVisibility(View.VISIBLE);
                            tv_noolder.setVisibility(View.GONE);
                            list = mMyCollectionJson.getList();
                            mAdapter = new MyCollectionAdapter(mContext, list);
                            mListView.setAdapter(mAdapter);
                            p = 1;
                        }
                    } else {
                        Toasts.show(mMyCollectionJson.getMessage());
                    }
                } else {
                    showErrorDialog(mContext);
                }
                mListView.stopRefresh();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mListView.stopRefresh();
                showTimeoutDialog(mContext);
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
                    mListView.stopRefresh();
                }
                showErrorDialog(mContext);
            }
        });
    }

    private void getMoreDate(){
        RequestParams params = new RequestParams();
        params.put("uid",  MyApplication.getInstance().getUid());
//        params.put("p",  (p+1)+"");
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_MYFAV+"/p/"+(p+1);//
        System.out.println("===========================我的收藏 更多url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (isRefresh) {
                    isRefresh = false;
                    mListView.stopRefresh();
                }

                System.out.println("===========================我的收藏 更多response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mMyCollectionJson = new Gson().fromJson(response.toString(), MyCollectionJson.class);
                    if (mMyCollectionJson.getResult().equals("1")) {
                        if (mMyCollectionJson.getList().size() == 0 || mMyCollectionJson.getList() == null || "".equals(mMyCollectionJson.getList())) {
                            Toasts.show("暂无更多数据");
                        } else {
                            mListView.setVisibility(View.VISIBLE);
                            tv_noolder.setVisibility(View.GONE);
                            list.addAll(mMyCollectionJson.getList());
                            p = p+1;
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mMyCollectionJson.getMessage());
                    }
                } else {
                    showErrorDialog(mContext);
                }
                mListView.stopLoadMore();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (isRefresh) {
                    isRefresh = false;
                    mListView.stopLoadMore();
                }
                showTimeoutDialog(mContext);
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
                    mListView.stopRefresh();
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
        System.out.println("===========================用户观看视频 params ======= " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
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
                        intent.putExtra("seenum",mBannerVideoBean.getSpinfo().getSee_num());
                        intent.putExtra("sharenum",mBannerVideoBean.getSpinfo().getShare_num());
//						intent.putExtra("wapurl",Constant.BASE_URL + mResultBean.getWapurl());
                        AppManager.getAppManager().startNextActivity(mContext, PlayVideoActivity.class, intent);

                    } else {
                        Toasts.show(mBannerVideoBean.getMessage());
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

    /**
     * 删除
     * */
    private void del(final int position){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();

        final String url = Constant.BASE_URL+Constant.URL_USERAPI_DELSHOUC;//
        final RequestParams params = new RequestParams();
        params.put("uid",  MyApplication.getInstance().getUid());
        params.put("scid",  list.get(position).getScid());
        System.out.println("===========================我的收藏删除url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    System.out.println("===========================我的收藏response = " + response);
                    if (!TextUtils.isEmpty(response.toString())) {
                        mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                        if (mResultBean.getResult().equals("1")) {
                            list.remove(position);
                            mAdapter.notifyDataSetChanged();
                            Toasts.show(mResultBean.getMessage());
                        } else {
                            Toasts.show(mResultBean.getMessage() + "");
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

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }
        });

    }

    /**
     * 初始化侧滑删除按钮
     * */
    private void initSwipeMenuListView(){
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mHandler = new Handler();

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                // set item background  //new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE))
                openItem.setBackground(getResources().getDrawable(R.color.color_btn_red_defailt));
                // set item width
                openItem.setWidth(dp2px(60));
                // set item title
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
//                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new PullToRefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
//                ApplicationInfo item = list.get(position);
                switch (index) {
                    case 0:
                        // open
//                        open(item);
//                        list.remove(position);
                        del(position);
//                        mAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        // delete
                        // delete(item);
//                        list.remove(position);
//                        mCollectCommodityAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new PullToRefreshSwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                String url = Constant.URL_BASE_SHOP + Constant.URL_WAP_SHOPPER + "/uid/" + list.get(position - 1).getUid();
//                intent.putExtra("url", url);
//                AppManager.getAppManager().startNextActivity(mContext, ProductDetailsWebViewActivity.class, intent);
//                System.out.println("=================="+"position = "+position+"======店铺===url = " + url);
                isnosee("2",list.get(position - 1).getSpid(),"");//类型（1直播  2视频  3资讯  4干货 ）
            }
        });
    }

    private void onLoad() {
        mListView.setRefreshTime(RefreshTime.getRefreshTime(getApplicationContext()));
//        mListView.stopRefresh();
//
//        mListView.stopLoadMore();

    }

    @Override
    public void onRefresh() {
        onLoad();
        p = 1;
        isRefresh = true;
        getRefreshDate();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
//                RefreshTime.setRefreshTime(getApplicationContext(), df.format(new Date()));
//                onLoad();
//            }
//        }, 2000);
    }

    @Override
    public void onLoadMore() {
        onLoad();
        isRefresh = true;
        getMoreDate();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                onLoad();
//            }
//        }, 2000);
    }

    private void initLiveData(){
        if(list == null){
            list = new ArrayList<MyCollectionBean>();
        }else{
            list.clear();
        }
        for (int i = 0; i < 10;i++){
            MyCollectionBean mMyCollectionBean = new MyCollectionBean();
            mMyCollectionBean.setTitle("收藏史蒂夫法国队");
            mMyCollectionBean.setId("" + i);
            mMyCollectionBean.setContext("觉得回复说的");
            list.add(mMyCollectionBean);
        }

        mAdapter = new MyCollectionAdapter(this,list);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
