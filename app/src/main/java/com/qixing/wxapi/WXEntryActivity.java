package com.qixing.wxapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.UserInfoBean;
import com.qixing.bean.WXAuthBean;
import com.qixing.bean.WXUserInfoBean;
import com.qixing.global.Constant;
import com.qixing.view.imagecut.ImageTools;
import com.qixing.welcome.LoginActivity;
import com.qixing.widget.Toasts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.weixin.callback.WXCallbackActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

public class WXEntryActivity extends WXCallbackActivity {
	// implements IWXAPIEventHandler
//	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
//
//	// IWXAPI 是第三方app和微信通信的openapi接口
//    private IWXAPI api;
//
//
//	public static BaseResp mResp = null;
//	// 是否有新的认证请求
//	public static boolean hasNewAuth = false;
//	private TextView mTvCode;
//
//    @Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// 通过WXAPIFactory工厂，获取IWXAPI的实例
//		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
//		api.registerApp(Constant.APP_ID);
//		api.handleIntent(getIntent(), this);
//		super.onCreate(savedInstanceState);
//	}
//
//
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		setIntent(intent);
//		api.handleIntent(intent, this);
//		finish();
//	}
//
//	private boolean checkWX(){
//		// 通过WXAPIFactory工厂，获取IWXAPI的实例
//		if(api!=null){
//			api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
//		}
//		if( !api.isWXAppInstalled()){
//			Toast.makeText(WXEntryActivity.this, "请先安装微信应用", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		if( !api.isWXAppSupportAPI() ){
//			Toast.makeText(WXEntryActivity.this, "请先更新微信应用", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		return true;
//	}
//
//	@Override
//	public void onReq(BaseReq arg0) { }
//
//
////	private static String GET_REQUEST_ACCESS_TOKEN =
////			"https://api.weixin.qq.com/sns/oauth2/access_token?appid="+Constant.APP_ID+"&secret="+Constant.WEIXIN_SECRET+"&code=CODE&grant_type=authorization_code";
//
////	private static String GET_REQUEST_USER_INFO =
////			"https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
//
//	@Override
//	public void onResp(BaseResp resp) {
//		System.out.println("===========================微信分享和三方登录回调 resp.errCode:"+resp.errCode +",resp.errStr:" + resp.errStr + ",resp.getType():"+resp.getType());
//		switch (resp.errCode) {
//			case BaseResp.ErrCode.ERR_OK:
//				String code = ((SendAuth.Resp) resp).code;
//				//分享成功
//				System.out.println("===========================微信分享成功");
//				if(resp.getType() == ConstantsAPI.COMMAND_SENDAUTH){
//					//登录回调
////					Toasts.show("登录成功");
//					System.out.println("===========================微信登录回调");
//					//		appid	是	应用唯一标识，在微信开放平台提交应用审核通过后获得
//					//		secret	是	应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
//					//		code	是	填写第一步获取的code参数
//					//		grant_type	是	填authorization_code
//					//		Toast.makeText(this, "openid = " + resp.openId, Toast.LENGTH_SHORT).show();
//					String GET_REQUEST_ACCESS_TOKEN =Constant.URL_WX_OAUTH2_ACCESS_TOKEN + "appid="+Constant.APP_ID+"&secret="+Constant.WEIXIN_SECRET+"&code="+code+"&grant_type=authorization_code";
//					wxLogin(GET_REQUEST_ACCESS_TOKEN);
//				}else if(resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){
//					//分享回调
//					Toasts.show("分享成功");
//					System.out.println("===========================微信分享回调");
//					finish();
//				}
//
//				break;
//			case BaseResp.ErrCode.ERR_USER_CANCEL:
//				//分享取消
//				System.out.println("===========================微信分享取消 ");
//				Toasts.show("已取消");
//				finish();
//				break;
//			case BaseResp.ErrCode.ERR_AUTH_DENIED:
//				//分享拒绝
//				System.out.println("===========================微信分享拒绝 ");
//				Toasts.show("已拒绝");
//				finish();
//				break;
//			default:
//				break;
//		}
//	}
//
//	private ProgressDialog mProgressDialog;
//	private MaterialDialog mMaterialDialog;
//	private AsyncHttpClient mAsyncHttpClient;
//	private WXAuthBean mWXAuthBean;
//	private WXUserInfoBean mWXUserInfoBean;
//
//	private Bitmap bitmap;
//	private String img_base64;
//	private UserInfoBean mUserInfoBean;
//
//	private void wxLogin(String url){
//		if(mProgressDialog == null){
//			mProgressDialog = new ProgressDialog(this);
//		}
//		mProgressDialog.setMessage("加载中...");
////		mProgressDialog.show();
//		RequestParams params = new RequestParams();
//		System.out.println("===========================WX登录 url = " + url);
//		System.out.println("===========================WX登录 params = " + params.toString());
//		mAsyncHttpClient = new AsyncHttpClient();
//		mAsyncHttpClient.post(WXEntryActivity.this, url, params, new JsonHttpResponseHandler() {
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//				super.onSuccess(statusCode, headers, response);
//
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//
//				System.out.println("===========================WX登录 response = " + response.toString());
//				if (!TextUtils.isEmpty(response.toString())) {
//					mWXAuthBean = new Gson().fromJson(response.toString(), WXAuthBean.class);
//					if(!TextUtils.isEmpty(mWXAuthBean.getOpenid())) {
////						Toasts.show("授权成功");
//						String GET_REQUEST_USER_INFO =Constant.URL_WX_USERINFO + "access_token="+mWXAuthBean.getAccess_token()+"&openid="+mWXAuthBean.getOpenid();
//						wxUserInfo(GET_REQUEST_USER_INFO);
//					} else {
//						Toasts.show(mWXAuthBean.getErrmsg());
//						finish();
//					}
//				} else {
//					Toasts.show("授权失败");
//					finish();
//				}
//
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//				super.onFailure(statusCode, headers, throwable, errorResponse);
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//				Toasts.show("授权超时");
//				finish();
//			}
//			@Override
//			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//				super.onFailure(statusCode, headers, responseString, throwable);
//				System.out.println("===========================throwable ,responseString =  " + responseString.toString());
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//				Toasts.show("授权失败");
//				finish();
//			}
//		});
//	}
//	private void wxUserInfo(String url){
//		if(mProgressDialog == null){
//			mProgressDialog = new ProgressDialog(this);
//		}
//		mProgressDialog.setMessage("加载中...");
//		mProgressDialog.show();
//		RequestParams params = new RequestParams();
//		System.out.println("===========================WX登录用户信息 url = " + url);
//		System.out.println("===========================WX登录用户信息 params = " + params.toString());
//		mAsyncHttpClient = new AsyncHttpClient();
//		mAsyncHttpClient.post(WXEntryActivity.this, url, params, new JsonHttpResponseHandler() {
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//				super.onSuccess(statusCode, headers, response);
//
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//
//				System.out.println("===========================WX用户信息 response = " + response.toString());
//				if (!TextUtils.isEmpty(response.toString())) {
//					mWXUserInfoBean = new Gson().fromJson(response.toString(), WXUserInfoBean.class);
//					if (!TextUtils.isEmpty(mWXUserInfoBean.getOpenid())) {
////						Toasts.show("授权成功");
//						System.out.println("===========================WX用户头像地址信息 Headimgurl = " + mWXUserInfoBean.getHeadimgurl());
//						try {
//							downloadFile(mWXUserInfoBean.getHeadimgurl());
//						} catch (Exception e) {
//							e.printStackTrace();
//							Toasts.show("网络错误！");
//							finish();
//						}
////						finish();
//					} else  {
//						Toasts.show(mWXUserInfoBean.getErrmsg());
//						finish();
//					}
//				} else {
//					Toasts.show("授权失败");
//					finish();
//				}
//
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//				super.onFailure(statusCode, headers, throwable, errorResponse);
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//				Toasts.show("授权超时");
//				finish();
//			}
//			@Override
//			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//				super.onFailure(statusCode, headers, responseString, throwable);
//				System.out.println("===========================throwable ,responseString =  " + responseString.toString());
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//				Toasts.show("授权失败");
//				finish();
//			}
//		});
//	}
//
//	public static String IS_WX_LOGIN = "";
//
//	private void wxUserInfoToServer(){
//		if(mProgressDialog == null){
//			mProgressDialog = new ProgressDialog(this);
//		}
//		mProgressDialog.setMessage("加载中...");
//		mProgressDialog.show();
//		RequestParams params = new RequestParams();
//		params.put("status", "1");//		类型	status  1微信
//		params.put("openid", mWXUserInfoBean.getOpenid());//		微信id	openid
//		params.put("nickname", mWXUserInfoBean.getNickname());//		昵称	nickname
//		params.put("sex", mWXUserInfoBean.getSex());//		性别	sex
//		params.put("pic", img_base64);//		头像	pic
//		params.put("unionid", mWXUserInfoBean.getUnionid());//		unionid	unionid
//		final String url = Constant.BASE_URL+ Constant.URL_USERAPI_THIRD_PARTY;//
//		System.out.println("===========================WX第三方登录 url = " + url);
//		System.out.println("===========================WX第三方登录 params = " + params.toString());
//		mAsyncHttpClient = new AsyncHttpClient();
//		mAsyncHttpClient.post(WXEntryActivity.this, url, params, new JsonHttpResponseHandler() {
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//				super.onSuccess(statusCode, headers, response);
//
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//
//				System.out.println("===========================WX第三方登录 response = " + response.toString());
//				if (!TextUtils.isEmpty(response.toString())) {
//					mUserInfoBean = new Gson().fromJson(response.toString(), UserInfoBean.class);
//					if ("1".equals(mUserInfoBean.getResult())) {//1:未绑定，绑定手机号
//						IS_WX_LOGIN = "1";
//						MyApplication.getInstance().setUid(mUserInfoBean.getUid());
////						Toasts.show("授权成功");
//						AppManager.getAppManager().startNextActivity(LoginActivity.class);
//					} else if("3".equals(mUserInfoBean.getResult())){//3:已绑定，可直接登录
//						IS_WX_LOGIN = "3";
//						MyApplication.getInstance().setUid(mUserInfoBean.getUid());
//						MyApplication.getInstance().setNickname(mUserInfoBean.getUname());
//						MyApplication.getInstance().setU_tel(mUserInfoBean.getU_phone());
//						MyApplication.getInstance().setUser_Head(mUserInfoBean.getTxurl());
//						AppManager.getAppManager().startNextActivity(LoginActivity.class);
//					} else  {
//						Toasts.show(mUserInfoBean.getResult());
//					}
//				} else {
//					Toasts.show("授权失败");
//				}
//
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//				super.onFailure(statusCode, headers, throwable, errorResponse);
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//				Toasts.show("授权超时");
//			}
//			@Override
//			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//				super.onFailure(statusCode, headers, responseString, throwable);
//				System.out.println("===========================throwable ,responseString =  " + responseString.toString());
//				if (mProgressDialog != null) {
//					mProgressDialog.dismiss();
//				}
//				Toasts.show("授权失败");
//			}
//		});
//	}
//
//	/**
//	 * @param url
//	 *            要下载的文件URL
//	 * @throws Exception
//	 */
//	public void downloadFile(String url) throws Exception {
//
//		// 指定文件类型
//		String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
//		// 获取二进制数据如图片和其他文件
//		mAsyncHttpClient.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//								  byte[] binaryData) {
//				String tempPath = Environment.getExternalStorageDirectory().getPath() + "/"+getNowTime()+".jpg";
//				// TODO Auto-generated method stub
//				// 下载成功后需要做的工作
////                progress.setProgress(0);
//				//
//				Log.e("binaryData:", "共下载了：" + binaryData.length);
//				//
//				Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,binaryData.length);
//
//				File file = new File(tempPath);
//				// 压缩格式
//				Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
//				// 压缩比例
//				int quality = 100;
//				try {
//					// 若存在则删除
//					if (file.exists())
//						file.delete();
//					// 创建文件
//					file.createNewFile();
//					//
//					OutputStream stream = new FileOutputStream(file);
//					// 压缩输出
//					bmp.compress(format, quality, stream);
//					// 关闭
//					stream.close();
//					//
//
////					Toast.makeText(mContext, "下载成功\n" + tempPath,Toast.LENGTH_LONG).show();
//
//					System.out.println("===========下载成功 头像路径是"+tempPath);
//					bitmap= ImageTools.createThumbnail(tempPath, 200, 200);
//					img_base64=ImageTools.bitmapToBase64(bitmap);
//					if(!TextUtils.isEmpty(img_base64)){
//						wxUserInfoToServer();
//					}else{
//						System.out.println("===========头像为空");
//						Toasts.show("网络错误！！");
//						finish();
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//								  byte[] binaryData, Throwable error) {
//				// TODO Auto-generated method stub
//				Toast.makeText(WXEntryActivity.this, "下载失败", Toast.LENGTH_LONG).show();
//			}
//
//
//			@Override
//			public void onProgress(long bytesWritten, long totalSize) {
//				// TODO Auto-generated method stub
//				super.onProgress(bytesWritten, totalSize);
//				int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
//				// 下载进度显示
////                progress.setProgress(count);
//				Log.e("下载 Progress>>>>>", bytesWritten + " / " + totalSize);
//
//			}
//
//			@Override
//			public void onRetry(int retryNo) {
//				// TODO Auto-generated method stub
//				super.onRetry(retryNo);
//				// 返回重试次数
//			}
//
//		});
//	}
//
//
//	private String getNowTime() {
//		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
//		Date date = new Date();
//		String key = format.format(date);
//
//		Random r = new Random();
//		key = key + r.nextInt();
//		key = key.substring(0, 15);
//		return key;
//	}
}