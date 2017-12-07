package com.qixing.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.adapter.MessageAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.MessageBean;
import com.qixing.bean.MessageJson;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.utlis.DateUtils;
import com.qixing.view.pulltorefreshswipemenu.PullToRefreshSwipeMenuListView;
import com.qixing.view.pulltorefreshswipemenu.pulltorefresh.RefreshTime;
import com.qixing.view.pulltorefreshswipemenu.swipemenulistview.SwipeMenu;
import com.qixing.view.pulltorefreshswipemenu.swipemenulistview.SwipeMenuCreator;
import com.qixing.view.pulltorefreshswipemenu.swipemenulistview.SwipeMenuItem;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by wicep on 2015/12/23.
 * 我的消息
 */
public class MessageActivity extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener {

    private BGATitlebar mTitleBar;

    private PullToRefreshSwipeMenuListView mListView;
    private TextView tv_noolder;

    private MessageAdapter mAdapter;
    private MessageJson mJson;
    private List<MessageBean> list;
    private boolean isRefresh = false;
    private int p = 1;

    private String title;

    private ResultBean mResultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_message, null);
        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        title = getIntent().getStringExtra("title");

        initView();
        initDatas();

        MyApplication.getInstance().setInfo_num(0);
    }

    private void initView() {
        mTitleBar = (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitleBar.setTitleText(title);
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

        tv_noolder = (TextView) findViewById(R.id.my_bonus_tv_noolder);
        mListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.mListView);

    }

    private void initDatas() {
        getRefreshDate();

        initSwipeListView();
    }

    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        setIntent(intent);//must store the new intent unless getIntent() will return the old one

        getRefreshDate();

        MyApplication.getInstance().setInfo_num(0);

    }

    private void getRefreshDate() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            if (!isRefresh) {
                mProgressDialog.show();
            }
        }
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_HDXX + "/p/" + p;//
        System.out.println("===========================我的消息 url = " + url);
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

                System.out.println("===========================我的消息 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mJson = new Gson().fromJson(response.toString(), MessageJson.class);
                    if (mJson.getResult().equals("1")) {
                        if (mJson.getList() == null || mJson.getList().size() == 0 || "".equals(mJson.getList())) {
                            mListView.setVisibility(View.GONE);
                            tv_noolder.setVisibility(View.VISIBLE);
                        } else {
                            mListView.setVisibility(View.VISIBLE);
                            tv_noolder.setVisibility(View.GONE);
                            list = mJson.getList();
                            mAdapter = new MessageAdapter(mContext, list);
                            mListView.setAdapter(mAdapter);
                        }
                    } else {
                        Toasts.show(mJson.getMessage());
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
                if (isRefresh) {
                    isRefresh = false;
                    mListView.stopRefresh();
                }
                showErrorDialog(mContext);
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
                showTimeoutDialog(mContext);
            }
        });

    }

    private void initMoreDatas() {
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_HDXX + "/p/" + (p + 1);//
        System.out.println("===========================我的消息 更多url = " + url);
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

                System.out.println("===========================我的消息 更多response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mJson = new Gson().fromJson(response.toString(), MessageJson.class);
                    if (mJson.getResult().equals("1")) {
                        if (mJson.getList().size() == 0 || mJson.getList() == null || "".equals(mJson.getList())) {
                            Toasts.show("暂无更多数据");
                        } else {
                            mListView.setVisibility(View.VISIBLE);
                            tv_noolder.setVisibility(View.GONE);
                            list.addAll(mJson.getList());
                            p = p + 1;
                            System.out.println("===========================mJson.getList().size() = " + mJson.getList().size());
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toasts.show(mJson.getMessage());
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
                showErrorDialog(mContext);
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
                showTimeoutDialog(mContext);
            }
        });
    }

    private void delete(int position) {
        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("id", list.get(position).getId());
        final String url = Constant.BASE_URL + Constant.URL_USERAPI_DELMSG;//
        System.out.println("===========================我的消息 删除消息 url = " + url);
        System.out.println("===========================我的消息 删除消息 params = " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================我的消息 删除消息 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if(mResultBean.getResult().equals("1")) {
                        Toasts.show(mResultBean.getMessage());
                    }else if (mResultBean.getResult().equals("3")) {
                        Toasts.show("此消息为系统消息,无法删除!");
                    }else {
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
                showErrorDialog(mContext);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                System.out.println("===========================throwable ,responseString =  " + responseString);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showTimeoutDialog(mContext);
            }
        });
    }

    /**
     * 初始化侧滑按钮
     */
    private void initSwipeListView() {
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
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
                        showDelDialog("提示","是否确定删除此条信息",position);
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = Constant.BASE_HOST + list.get(position - 1).getUrl();
                // 	跳转到另外的activity
                Intent intent = new Intent();
                intent.putExtra("url", url);
                AppManager.getAppManager().startNextActivity(mContext, MessageDetailActivity.class, intent);
            }
        });

    }

    /**
     * 删除消息dialog
     */
    private void showDelDialog(String title, String msg, final int position) {
        final MaterialDialog materialDialog = new MaterialDialog(mContext);
        materialDialog.setTitle(title);
        materialDialog.setMessage(msg);
        materialDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
                materialDialog.dismiss();
            }
        });
        materialDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });

        materialDialog.show();
    }

    private void showDeleteDialog(String content, final int position) {
        final Dialog dialog = new Dialog(mContext, R.style.recharge_pay_dialog);
        View view = getLayoutInflater().inflate(R.layout.message_dialog, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (view.getId()) {
                    case R.id.msg_dialog_ll_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.msg_dialog_ll_confirm:
                        delete(position);
                        break;
                }
                dialog.dismiss();
            }
        };

        TextView tv_content = (TextView) view.findViewById(R.id.msg_dialog_tv_content);
        tv_content.setText(content);

        LinearLayout ll_cancel = (LinearLayout) view.findViewById(R.id.msg_dialog_ll_cancel);
        LinearLayout ll_confirm = (LinearLayout) view.findViewById(R.id.msg_dialog_ll_confirm);
        ll_cancel.setOnClickListener(listener);
        ll_confirm.setOnClickListener(this);

        //设置dialog显示的相对位置 在dialog show之后
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = AbsListView.LayoutParams.MATCH_PARENT;
        lp.y = 0;//设置Dialog距离底部的距离
        window.setAttributes(lp);
    }

//    private void init() {
//        ILoadingLayout startLabels = mListView.getLoadingLayoutProxy(true, false);
//        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
//        startLabels.setRefreshingLabel("正在载入...");// 刷新时
//        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
//
//        ILoadingLayout endLabels = mListView.getLoadingLayoutProxy(
//                false, true);
//        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
//        endLabels.setRefreshingLabel("正在载入...");// 刷新时
//        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
//    }

    private void onLoad() {
        mListView.setRefreshTime(DateUtils.getNowTime("yyyy-MM-dd HH:mm"));
//        mListView.stopRefresh();
//
//        mListView.stopLoadMore();

    }

    private void initTestData() {
    }

    @Override
    public void onRefresh() {
        onLoad();
        p = 1;
        isRefresh = true;
        getRefreshDate();
    }

    @Override
    public void onLoadMore() {
        onLoad();
        isRefresh = true;
        initMoreDatas();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
