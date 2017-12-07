package com.qixing.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.qixing.R;


public class Fragment3 extends BaseFragment implements View.OnClickListener{
	TextView msg;

	private TextView tv_title;
	private EditText edit_search;
    private LinearLayout ll_left,ll_right,ll_imgandtex;
    private TextView left_tv,right_tv;
    private ImageView left_img,right_img;

//	private String url = Constant.BASE_URL+Constant.URL_SHOPCART;

	ValueCallback<Uri> mUploadMessage;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.fragment3);
		mAsyncHttpClient=new AsyncHttpClient();

		// 导航栏
		tv_title = getViewById(R.id.title_center_tv_title);
		tv_title.setText("购物车");
		tv_title.setVisibility(View.VISIBLE);
        ll_left = getViewById(R.id.title_ll_left);
        ll_left.setOnClickListener(this);
        ll_left.setVisibility(View.GONE);
		edit_search = getViewById(R.id.title_center_edit_search);
		edit_search.setVisibility(View.GONE);
        ll_right = getViewById(R.id.title_ll_right);
        ll_right.setVisibility(View.GONE);
        ll_right.setOnClickListener(this);


	}


	@Override
	protected void setListener() {

	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {

	}

	@Override
	protected void onUserVisible() {

//		if(mWebView==null){
//			mWebView = getViewById(R.id.mWebView);
//		}
//		if(mWebView!=null){
////			mWebView.loadUrl(url + "uid/" + MyApplication.getInstance().getUid());
//			String postData = "&uid="+ MyApplication.getInstance().getUid() ;
//			mWebView.postUrl(url, EncodingUtils.getBytes(postData, "UTF-8"));
//			System.out.println("===========================购物车 onUserVisible url = " + mWebView.getUrl());
//		}
}

	@Override
	public void onResume() {
		super.onResume();
//		if("1".equals(MyApplication.getInstance().getIsLogining())){
//			//加载服务器网页
////			mWebView.loadUrl(url + "uid/" + MyApplication.getInstance().getUid());
//			String postData = "&uid="+ MyApplication.getInstance().getUid() ;
//			mWebView.postUrl(url, EncodingUtils.getBytes(postData, "UTF-8"));
//			System.out.println("===========================购物车 onResume url = " + mWebView.getUrl());
//		}else{
//		}
	}

//	private ConfirmOlderJson mConfirmOlderJson;
//	private void initDatas(final String param){
//		if (mProgressDialog == null) {
//			mProgressDialog = new ProgressDialog(getActivity());
//			mProgressDialog.setMessage("加载中...");
//			mProgressDialog.show();
//		}
//		final String url = Constant.BASE_URL+Constant.URL_APP_TJDD+"/"+param+"/uid/" + MyApplication.getInstance().getUid();
//		mAsyncHttpClient.get(getActivity(), url, new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int i, Header[] headers, byte[] bytes) {
//				String response = new String(bytes);
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//				System.out.println("===========================确认订单页面 url = " + url);
//				System.out.println("===========================确认订单页面 response = " + response);
//				if (!TextUtils.isEmpty(response.toString())) {
//					mConfirmOlderJson = new Gson().fromJson(response.toString(), ConfirmOlderJson.class);
//					if (mConfirmOlderJson.getResult().equals("1")) {
//						Intent intent = new Intent();
//						intent.putExtra("param", param);
//						AppManager.getAppManager().startNextActivity(getActivity(), ConfirmOlderActivity.class, intent);
//					} else {
//						Toasts.show(mConfirmOlderJson.getMessage());
//					}
//				} else {
//					showErrorDialog(getActivity());
//				}
//			}
//
//			@Override
//			public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//				Toasts.show(R.string.service_wrong);
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//				showErrorDialog(getActivity());
//			}
//		});
//	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_ll_left://个人中心
//				AppManager.getAppManager().startNextActivity(getActivity(), SearchActivity.class);
                break;
            case R.id.title_ll_right:
//				AppManager.getAppManager().startNextActivity(getActivity(), SearchActivity.class);
                break;
        }
    }
}