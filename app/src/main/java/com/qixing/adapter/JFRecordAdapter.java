package com.qixing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.bean.JFRecordBean;
import com.qixing.utlis.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 积分记录
 * Created by nuo-rui on 2017/6/8.
 */

public class JFRecordAdapter extends BaseAdapter {
    private Context mContext;
    private List<JFRecordBean> mList;

    public JFRecordAdapter(Context mContext, List<JFRecordBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_jf_recharge_record,null);
            mHolder=new ViewHolder();
            mHolder.tv_recharge_dir= (TextView) convertView.findViewById(R.id.item_jf_recharge_record_name);
            mHolder.tv_recharge_count= (TextView) convertView.findViewById(R.id.item_jf_recharge_record_count);
            mHolder.tv_recharge_paymethod= (TextView) convertView.findViewById(R.id.item_jf_recharge_record_paymethod);
            mHolder.tv_recharge_time= (TextView) convertView.findViewById(R.id.item_jf_recharge_record_time);
            convertView.setTag(mHolder);
        }else{
            mHolder= (ViewHolder) convertView.getTag();
        }
        JFRecordBean recordBean=mList.get(position);
        mHolder.tv_recharge_dir.setText(recordBean.getTitle());
        if("1".equals(recordBean.getIs_jia())){
            mHolder.tv_recharge_count.setText("积分+"+recordBean.getJifen());
        }else{
            mHolder.tv_recharge_count.setText("积分-"+recordBean.getJifen());
        }
//        mHolder.tv_recharge_count.setText(recordBean.getJifen());
        mHolder.tv_recharge_paymethod.setText(recordBean.getTypename());
        String format=DateUtils.TimeStamp2DateYYYYMMDD(recordBean.getAddtime());
        mHolder.tv_recharge_time.setText(format);
        return convertView;
    }

    class ViewHolder{
        TextView tv_recharge_dir,tv_recharge_count,tv_recharge_paymethod,tv_recharge_time;
    }
}
