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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.app.MyApplication;
import com.qixing.bean.MessageBean;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.utlis.DateUtils;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by wicep on 2015/12/25.
 */
public class MessageAdapter extends BaseAdapter{
    private List<MessageBean> list ;
    private Context context;
    public MessageAdapter(Context context, List<MessageBean> list) {
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

            convertView=inflater.inflate(R.layout.item_message,null);
            mHolder.tv_name= (TextView) convertView.findViewById(R.id.item_my_bonus_tv_name);
            mHolder.tv_context= (TextView) convertView.findViewById(R.id.item_my_bonus_tv_context);
            mHolder.tv_time= (TextView) convertView.findViewById(R.id.item_my_bonus_tv_time);
            mHolder.img_del= (ImageView) convertView.findViewById(R.id.item_my_bonus_img_del);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
        mHolder.tv_name.setText(list.get(position).getName());
        mHolder.tv_context.setText(list.get(position).getMs());
        mHolder.tv_time.setText(DateUtils.TimeStamp2Date(DateUtils.StrngToLong(list.get(position).getTimes())));

//        mHolder.tv_time.setText(DateUtils.TimeStamp2DateYYYYMMDD(list.get(position).getAddtime()));

        mHolder.img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelDialog("删除","确定删除消息？",position);
            }
        });
        return convertView;

    }

    /**
     * 删除订单dialog
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

    AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();;
    ProgressDialog mProgressDialog = null;
    AlertDialog mAlertDialog;
    ResultBean mResultBean;

    /**
     * 删除消息
     * */
    private void del(final int position){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("id", list.get(position).getId());
        final String url = Constant.BASE_URL;//+Constant.URL_MYCENTER_XIAOXI_DEL
        System.out.println("===========================消息  删除消息url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(context, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================消息  删除消息 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        list.remove(position);
                        Toasts.show(mResultBean.getMessage());
                        MessageAdapter.this.notifyDataSetChanged();
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

    class ViewHolder{
        private TextView tv_name;
        private TextView tv_context;
        private TextView tv_time;
        private ImageView img_del;
    }
}
