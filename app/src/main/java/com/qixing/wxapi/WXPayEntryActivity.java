package com.qixing.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.qixing.MainActivity;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.PayResultBean;
import com.qixing.global.Constant;
import com.qixing.utlis.MD5Utils;
import com.qixing.view.MyListView;
import com.qixing.view.titlebar.BGATitlebar;
import com.qixing.widget.Toasts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.wxapi.alipay.H5PayDemoActivity;
import com.qixing.wxapi.alipay.PayResult;
import com.qixing.wxapi.alipay.SignUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by wicep on 2015/12/23.
 * 选择支付方式页面
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private BGATitlebar mTitleBar;


    public static Context context;

    private PayResultBean mPayResultBean;

    private String urlResult;//回调函数
    private String title;//订单标题
    private String discription;//订单描述
    private String money;//金额
    private String code;//充值订单号

    private String paymethod = "";

    private String tag = "";

    /**
     * 微信支付
     * */
    private IWXAPI api;//是第三方app和微信通信的openapi接口
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        View view = getLayoutInflater().inflate(R.layout.activity_pay, null);
//        setContentView(view);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));
        context = WXPayEntryActivity.this;
        tag = getIntent().getStringExtra("tag");
        paymethod = getIntent().getStringExtra("paymethod");

        if("RechargeActivity".equals(tag)){//充值
            money = getIntent().getStringExtra("totalprice");
            code = getIntent().getStringExtra("orderno");
            urlResult = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
            discription = getIntent().getStringExtra("discription");
        }else if("QXLiveSeeActivity".equals(tag)){//直播充值
            money = getIntent().getStringExtra("totalprice");
            code = getIntent().getStringExtra("orderno");
            urlResult = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
            discription = getIntent().getStringExtra("discription");
        }else if ("RebateEnterActivity".equals(tag)){
            money = getIntent().getStringExtra("totalprice");
            code = getIntent().getStringExtra("orderno");
            urlResult = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
            discription = getIntent().getStringExtra("discription");
        }
//        测试数据
//        urlResult = "http://notify.msp.hk/notify.htm";//回调函数
//        title = "充值测试";//订单标题
//        discription = "充值测试001";//订单描述
//        money = "0.01";//金额
//        code = getOutTradeNo();//充值订单号

        initPay();
    }

    private void initPay(){

        if("1".equals(paymethod)){//支付方式1支付宝 2.微信  3余额
            payZFB();
        }else if("2".equals(paymethod)){
            GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
            getPrepayId.execute();
        }
//        if("RechargeActivity".equals(tag)){//充值
//        }else if("RebateEnterActivity".equals(tag)){//报名
//        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
//            case R.id.pay_older_btn_confirm:
//                if("MyAmountActivity".equals(tag)){//充值
//                    money = edit_money.getText().toString().trim();
//                    if(TextUtils.isEmpty(money)){
//                        Toasts.show("充值金额不能为空");
//                        break;
//                    }
//                    if(is_zhifubao){
//                        initDatas();
////                        payZFB();
//                    }else if(is_weixin){
//                        initDatas();
////                        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
////                        getPrepayId.execute();
//                    }else{
//                        Toasts.show("请选择支付方式");
//                    }
//                }

//                else if("ConfirmOlderActivity".equals(tag)){//支付
//                    if(is_yue){
//                        Intent intent = new Intent();
//                        intent.putExtra("code", code);
//                        intent.putExtra("paymethod", method2);
//                        intent.putExtra("money", money);
//                        AppManager.getAppManager().startNextActivity(OlderPayPopupWindow.class, intent);
//                    }else if(is_zhifubao){
//                        btn_confirm.setEnabled(false);
//                        payZFB();
//                    }else if(is_weixin){
//                        btn_confirm.setEnabled(false);
//                        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
//                        getPrepayId.execute();
//                    }else{
//                        Toasts.show("请选择支付方式");
//                    }
//                }
//                break;
        }
    }

    private boolean checkWX(){
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        if(api!=null){
            api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
        }
        if( !api.isWXAppInstalled()){
            Toast.makeText(context, "请先安装微信应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( !api.isWXAppSupportAPI() ){
            Toast.makeText(context, "请先更新微信应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 充值 成功回调
     * */
    private void yeHandl(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("method", paymethod);
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_TJCZ;//
        System.out.println("===========================充值 成功回调url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================充值 充值成功回调response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mPayResultBean = new Gson().fromJson(response.toString(), PayResultBean.class);
                    if (mPayResultBean.getResult().equals("1")) {
                        Toasts.show(mPayResultBean.getMessage());
                        MyApplication.getInstance().setIsRecharge("1");
//                        ((Activity) MainActivity.context).finish();
                        Intent intent = new Intent();
                        intent.putExtra("vp", 4);
                        AppManager.getAppManager().startNextActivity(mContext, MainActivity.class, intent);
                        finish();
                    } else {
                        Toasts.show(mPayResultBean.getMessage());
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
     * 支付 成功回调
     * */
    private void orderPay(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage("加载中...");
        mProgressDialog.show();

        RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("code", code);//订单编号
        params.put("method",  paymethod);//支付码
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_SUREDJ;//
        System.out.println("===========================支付 支付成功回调url ===== " + url);
        System.out.println("===========================params ===== " + params.toString());
        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                System.out.println("===========================支付 支付成功回调response ===== " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mPayResultBean = new Gson().fromJson(response.toString(), PayResultBean.class);
                    if (mPayResultBean.getResult().equals("1")) {
                        Toasts.show(mPayResultBean.getMessage());
//                        ((Activity) MainActivity.context).finish();
                        Intent intent = new Intent();
                        intent.putExtra("vp", 4);
                        AppManager.getAppManager().startNextActivity(mContext, MainActivity.class, intent);
                        finish();
                    } else {
                        Toasts.show(mPayResultBean.getMessage());
                    }
                } else {
                    mAlertDialog = new AlertDialog.Builder(mContext)
                            .setTitle(R.string.dialog_prompt)
                            .setMessage(R.string.dialog_wrong)
                            .setPositiveButton(R.string.dialog_ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialoginterface, int i) {
                                            mAlertDialog.dismiss();
//                                        finish();
                                        }
                                    }).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mAlertDialog = new AlertDialog.Builder(mContext)
                        .setTitle(R.string.dialog_prompt)
                        .setMessage(R.string.dialog_wrong)
                        .setPositiveButton(R.string.dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        mAlertDialog.dismiss();
//                                        finish();
                                    }
                                }).show();
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



    private void payFinish(){
        if("RechargeActivity".equals(tag)){
            Intent intent = new Intent();
            intent.putExtra("vp", 4);
            AppManager.getAppManager().startNextActivity(mContext, MainActivity.class, intent);
            finish();
        }else if("QXLiveSeeActivity".equals(tag)){
            WXPayEntryActivity.this.finish();
        }else {
            Intent intent = new Intent();
            intent.putExtra("vp", 4);
            AppManager.getAppManager().startNextActivity(mContext, MainActivity.class, intent);
            finish();
        }
    }



    @Override
    protected void onResume(){
        super.onResume();

    }

    public void onPause() {
        super.onPause();
    }



    /*
     * *********************************************************支付宝支付 Start*************************************************************************
     * */
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    System.out.println("===========================支付宝 resultStatus ======= " + resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        payFinish();

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(WXPayEntryActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
//                            btn_confirm.setEnabled(true);

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    private void payZFB(){
        if (TextUtils.isEmpty(Constant.PARTNER) || TextUtils.isEmpty(Constant.RSA_PRIVATE) || TextUtils.isEmpty(Constant.SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        String orderInfo = getOrderInfo(title, discription, money);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(WXPayEntryActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = Constant.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(this, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.meituan.com";
        // url可以是一号店或者美团等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);

    }

    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Constant.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Constant.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + code + "\"";
        String order_number = getOutTradeNo();
        System.out.println("==============支付宝商户网站唯一订单号--out_trade_no----" + code);
        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        //http://kuaiyouzj.com/index.php/App/hdfun.html
        // 服务器异步通知页面路径urlResult
        orderInfo += "&notify_url=" + "\"" + urlResult + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, Constant.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
    /**
     * **********************************************************支付宝支付 end*************************************************************************
     * */



    /**
     * **********************************************************微信支付 start*************************************************************************
     * */

    StringBuffer sb = new StringBuffer();
    private String prepay_id;
    Map<String, String> resultunifiedorder;
    StringBuilder sb_name;
    PayReq req = new PayReq();

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key="+Constant.KEY);

        String packageSign = MD5Utils.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        System.out.println("===========================微信发送XML数据 genPackageSign----" + packageSign);
        Log.e("orion", packageSign);
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constant.KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5Utils.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        System.out.println("===========================微信发送XML数据 genAppSign----" + appSign);
        Log.e("orion", appSign);
        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");


        return sb.toString();
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Map<String, String> result) {


            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");

            prepay_id = result.get("prepay_id");

            resultunifiedorder = result;

            genPayReq();
        }

        public int stringToInt(String string) {
            int j = 0;
            String str = string.substring(0, string.indexOf(".")) + string.substring(string.indexOf(".") + 1);
            int intgeo = Integer.parseInt(str);
            return intgeo;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");

            double dmoney = Double.parseDouble(money);
            BigDecimal b = new BigDecimal(dmoney);
            String f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            int amounts = stringToInt(f1);
            System.out.println("===========================微信支付onReq, 输入金额money="+money+",以分为单位dmoney="+dmoney+",BigDecimal b="+b+",String f1="+f1+",微信支付总金额total_fee ====== " + amounts);
            String entity = genProductArgs(title, discription, amounts+"");

            System.out.println("===========================微信发送XML数据----" + entity);
            Log.e("orion", entity);

            byte[] buf = WxHttpUtlis.httpPost(url, entity);

            String content = new String(buf);

            System.out.println("===========================微信服务器返回的数据是-----" + content);
            Log.e("orion", content);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }


    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }


    private String genNonceStr() {
        Random random = new Random();
        return MD5Utils.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }



    private String genOutTradNo() {
        Random random = new Random();
        return MD5Utils.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }


    public int stringToInt(String string) {
        int j = 0;
        String str = string.substring(0, string.indexOf(".")) + string.substring(string.indexOf(".") + 1);
        int intgeo = Integer.parseInt(str);
        return intgeo;
    }

    //
    private String genProductArgs(String body, String detail, String price) {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();


            prepay_id = genOutTradNo();

//            MyApplication.getInstance().setWechatMCHID(prepay_id);
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constant.APP_ID));
            // packageParams.add(new BasicNameValuePair("attach", order_number));
            packageParams.add(new BasicNameValuePair("body", body));//商品描述
            packageParams.add(new BasicNameValuePair("detail", detail));//商品详情
            packageParams.add(new BasicNameValuePair("mch_id", Constant.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", urlResult));
            packageParams.add(new BasicNameValuePair("out_trade_no", code));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", price+""));//总金额
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);

            return new String(xmlstring.toString().getBytes(), "UTF-8");

        } catch (Exception e) {

            return null;
        }


    }

    private void genPayReq() {

        req.appId = Constant.APP_ID;
        req.partnerId = Constant.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");


        Log.e("orion", signParams.toString());

        sendPayReq();
    }

    private void sendPayReq() {
// 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
        api.registerApp(Constant.APP_ID);
        api.handleIntent(getIntent(), this);//缺少此行代码，无法接受支付结果
        api.sendReq(req);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if(api!=null){
            api.handleIntent(intent, this);
        }
//        if(mNfcAdapter != null){
//            processIntent(getIntent());
//        }
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {
        System.out.println("===========================微信支付onReq, errCode ====== " + baseReq.toString());

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        System.out.println("===========================微信支付onPayFinish, errCode ====== " + resp.errCode);
        String msg = "";
        if (resp.errCode == 0) {
            msg = "支付成功";//展示成功页面
            Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            payFinish();
        } else if (resp.errCode == -1) {
            msg = "支付失败";//可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
            Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            finish();
        } else if (resp.errCode == -2) {
            msg = "已取消支付";//无需处理。发生场景：用户不支付了，点击取消，返回APP。
            Toast.makeText(WXPayEntryActivity.this, "支付已取消", Toast.LENGTH_SHORT).show();
            finish();
        }
//        Toast.makeText(WXPayEntryActivity.this, msg, Toast.LENGTH_SHORT).show();
//        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.app_tip);
//            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//            builder.show();
//        }
    }


    /**
     * **********************************************************微信支付 end*************************************************************************
     * */



}
