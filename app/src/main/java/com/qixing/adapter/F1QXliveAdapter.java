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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.app.MyApplication;
import com.qixing.bean.IndexJson;
import com.qixing.bean.ResultBean;
import com.qixing.bean.SeeQXliveBean;
import com.qixing.global.Constant;
import com.qixing.utlis.DateUtils;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.CircleImageView;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wicep on 2015/12/25.
 */
public class F1QXliveAdapter extends BaseAdapter{
    private List<IndexJson.Live> list ;
    private Context context;
    public F1QXliveAdapter(Context context, List<IndexJson.Live> list) {
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

            convertView=inflater.inflate(R.layout.item_fragment1_qxlive,null);

            mHolder.img_icon = (ImageView) convertView.findViewById(R.id.item_fragment1_qxlive_img_icon);
            mHolder.btn_state = (Button) convertView.findViewById(R.id.item_fragment1_qxlive_btn_attention);
            mHolder.img_head = (CircleImageView) convertView.findViewById(R.id.item_fragment1_qxlive_img_head);
            mHolder.tv_title = (TextView) convertView.findViewById(R.id.item_fragment1_qxlive_tv_title);
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.item_fragment1_qxlive_tv_name);
            mHolder.tv_times = (TextView) convertView.findViewById(R.id.item_fragment1_qxlive_tv_times);

            mHolder.btn_reservation = (Button)convertView.findViewById(R.id.item_fragment1_qxlive_btn_reservation);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(list.get(position).getZb_pic())){
            if(list.get(position).getZb_pic().startsWith("http")){
                ImageLoader.getInstance().displayImage(list.get(position).getZb_pic(),mHolder.img_icon, ImageLoaderOptions.getOptions());
            }else {
                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+list.get(position).getZb_pic(),mHolder.img_icon, ImageLoaderOptions.getOptions());
            }
        }
        if(!TextUtils.isEmpty(list.get(position).getPic())){
            if(list.get(position).getPic().startsWith("http")){
                ImageLoader.getInstance().displayImage(list.get(position).getPic(),mHolder.img_head, ImageLoaderOptions.getOptions());
            }else {
                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+list.get(position).getPic(),mHolder.img_head, ImageLoaderOptions.getOptions());
            }
        }

        mHolder.tv_title.setText(list.get(position).getZb_title());
        mHolder.tv_name.setText(list.get(position).getUname());
        mHolder.tv_times.setText(DateUtils.TimeStamp2DateHHMM(Long.parseLong(list.get(position).getTimes())));
//        mHolder.tv_num.setText("x "+list.get(position).getNum());

        //1直播中 2已结束 3预告
        if("1".equals(list.get(position).getStatus())){
            mHolder.btn_state.setText("直播中");
            mHolder.btn_state.setVisibility(View.VISIBLE);
            mHolder.btn_reservation.setVisibility(View.GONE);
        }else if("2".equals(list.get(position).getStatus())){
            mHolder.btn_state.setText("已结束");
            mHolder.btn_state.setVisibility(View.VISIBLE);
            mHolder.btn_reservation.setVisibility(View.GONE);
        }else if("3".equals(list.get(position).getStatus())){
            mHolder.btn_state.setText("预告");
            mHolder.btn_state.setVisibility(View.VISIBLE);
            if("0".equals(list.get(position).getIs_yy())){//1预约过  0没有预约
                mHolder.btn_reservation.setText("预约");
                mHolder.btn_reservation.setVisibility(View.VISIBLE);
            }else if("1".equals(list.get(position).getIs_yy())){
                mHolder.btn_reservation.setText("已预约");
                mHolder.btn_reservation.setEnabled(false);
                mHolder.btn_reservation.setVisibility(View.VISIBLE);
            }
        }

        mHolder.btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservation(position);
            }
        });


            return convertView;
    }

    AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();;
    ProgressDialog mProgressDialog = null;
    AlertDialog mAlertDialog;
    ResultBean mResultBean;

    /**
     * 删除消息
     * */
    private void reservation(final int position){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("zbid", list.get(position).getId());
        final String url = Constant.BASE_URL+Constant.URL_USERAPI_YUYUE_ZB;//
        System.out.println("=========================== 预约直播 url = " + url);
        System.out.println("===========================params = " + params.toString());
        mAsyncHttpClient.post(context, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("=========================== 预约直播 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        Toasts.show(mResultBean.getMessage());
                        list.get(position).setIs_yy("1");
                        F1QXliveAdapter.this.notifyDataSetChanged();
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
        private ImageView img_icon;
        private Button btn_state;

        private CircleImageView img_head;
        private TextView tv_title;
        private TextView tv_name;
        private TextView tv_times;

        private Button btn_reservation;
    }
}
