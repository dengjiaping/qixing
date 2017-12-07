package com.qixing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.bean.BillRechargeBean;
import com.qixing.utlis.DateUtils;

import java.util.List;

/**
 * Created by lenovo on 2017/10/24.
 */
public class BillRechargeAdapter extends BaseAdapter {

    private Context context;
    private List<BillRechargeBean> list;
    private LayoutInflater mInflater;

    public BillRechargeAdapter(Context context, List<BillRechargeBean> list) {
        this.context = context;
        this.list = list;
        mInflater=LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder=null;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.item_gift_record,null);
            mHolder=new ViewHolder();
            mHolder.iv_pic= (ImageView) convertView.findViewById(R.id.item_gift_record_iv_pic);
            mHolder.tv_num= (TextView) convertView.findViewById(R.id.item_gift_record_tv_num);
            mHolder.tv_intro= (TextView) convertView.findViewById(R.id.item_gift_record_tv_intro);
            mHolder.tv_time= (TextView) convertView.findViewById(R.id.item_gift_record_tv_time);
            convertView.setTag(mHolder);
        }else{
            mHolder= (ViewHolder) convertView.getTag();
        }
        mHolder.tv_num.setText("+"+list.get(position).getNum());
        mHolder.tv_intro.setText(list.get(position).getContents());
        mHolder.tv_time.setText(DateUtils.TimeStamp2DateYYYYMMDDHHmmss(list.get(position).getTimes()));
        return convertView;
    }

    static class ViewHolder{
        ImageView iv_pic;
        TextView tv_num,tv_intro,tv_time;
    }
}
