package com.qixing.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qixing.R;
import com.qixing.app.MyApplication;
import com.qixing.bean.ResultBean;
import com.qixing.bean.ReviseBankJson;
import com.qixing.global.Constant;
import com.qixing.widget.Toasts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by wicep on 2015/12/25.
 */
public class MyBankAdapter extends BaseAdapter{
    private List<ReviseBankJson.Bank> list ;
    private Context context;

    /**
     * 提示框
     * */
    public ProgressDialog mProgressDialog;
    public AlertDialog mAlertDialog;

    public AsyncHttpClient mAsyncHttpClient;

    private ResultBean mResultBean;

    public MyBankAdapter(Context context, List<ReviseBankJson.Bank> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder mHolder=null;
        if(convertView==null){

            mHolder=new ViewHolder();

            convertView=inflater.inflate(R.layout.item_my_bank,null);

            mHolder.tv_name= (TextView) convertView.findViewById(R.id.item_revise_bank_tv_name);
            mHolder.tv_jiebang= (TextView) convertView.findViewById(R.id.item_revise_bank_tv_jiebang);
            mHolder.tv_endcode= (TextView) convertView.findViewById(R.id.item_revise_bank_tv_endcode);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
        //str.substring(str.length()-4,str.length);
        mHolder.tv_name.setText(list.get(position).getName());
        String str = list.get(position).getCard_num();
//        System.out.println("===========================银行卡号 = " + str);
        if(!TextUtils.isEmpty(str)){
            if(str.length()<4){
//                System.out.println("===========================不够四位四位 = " + str);
                mHolder.tv_endcode.setText("尾号：" + str);
            }else{
//                System.out.println("===========================后四位 = " + str.substring(str.length() - 4, str.length()));
                mHolder.tv_endcode.setText("尾号：" + str.substring(str.length() - 4, str.length()));
            }
        }
        mHolder.tv_jiebang.setOnClickListener(new View.OnClickListener() {//解除绑定
            @Override
            public void onClick(View v) {
//                showDelDialog("解绑","确定解绑银行卡？",position);
            }
        });

        return convertView;

    }

    class ViewHolder{
        private TextView tv_name;
        private TextView tv_jiebang;
        private TextView tv_endcode;

    }

    /**
     * dialog
     * */
    private void showDelDialog(String title,String msg,final int position){
        final MaterialDialog materialDialog = new MaterialDialog(context);
        materialDialog.setTitle(title);
        materialDialog.setMessage(msg);
        materialDialog.setPositiveButton(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                del(position);
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

    private void del(final int position){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        mAsyncHttpClient = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        params.put("id",  MyApplication.getInstance().getUid());
        params.put("bid",  list.get(position).getId());
        final String url = Constant.BASE_URL;//+Constant.URL_USERAPI_JBBANK

        mAsyncHttpClient.post(context, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================url = " + url);
                System.out.println("===========================params : " + params.toString());
                System.out.println("===========================金玺币 个人中心银行卡 解绑 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        list.remove(position);
                        MyBankAdapter.this.notifyDataSetChanged();
                        Toasts.show(mResultBean.getMessage());
                    } else {
                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
                    mAlertDialog = new AlertDialog.Builder(context)
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
                mAlertDialog = new AlertDialog.Builder(context)
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
                mAlertDialog = new AlertDialog.Builder(context)
                        .setTitle(R.string.dialog_prompt)
                        .setMessage(R.string.dialog_timeout)
                        .setPositiveButton(R.string.dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        mAlertDialog.dismiss();
//                                        finish();
                                    }
                                }).show();
            }
        });
    }
}
