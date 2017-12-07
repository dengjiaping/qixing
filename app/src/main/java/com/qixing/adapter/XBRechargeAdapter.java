package com.qixing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.bean.XBRechargeBean;

import java.util.List;

/**
 * Created by lenovo on 2017/10/20.
 */
public class XBRechargeAdapter extends BaseAdapter {
    private Context context;
    private List<XBRechargeBean> list;
    private LayoutInflater mInflater;

    public XBRechargeAdapter(Context context, List<XBRechargeBean> list) {
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
            convertView=mInflater.inflate(R.layout.item_xb_recharge,null);
            mHolder=new ViewHolder();
            mHolder.ll_xb= (LinearLayout) convertView.findViewById(R.id.item_xb_recharge_ll_xb);
            mHolder.tv_xb= (TextView) convertView.findViewById(R.id.item_xb_recharge_tv_xb);
            mHolder.tv_price= (TextView) convertView.findViewById(R.id.item_xb_recharge_tv_price);
            convertView.setTag(mHolder);
        }else{
            mHolder= (ViewHolder) convertView.getTag();
        }

        mHolder.tv_xb.setText(list.get(position).getXb()+"星币");
        mHolder.tv_price.setText("￥"+list.get(position).getPrice());
        if("0".equals(list.get(position).getSelect())){
            mHolder.tv_xb.setTextColor(context.getResources().getColor(R.color.black));
            mHolder.tv_price.setTextColor(context.getResources().getColor(R.color.gray));
            mHolder.ll_xb.setBackground(context.getResources().getDrawable(R.drawable.xb_recharge_default));
        }else if("1".equals(list.get(position).getSelect())){
            mHolder.tv_xb.setTextColor(context.getResources().getColor(R.color.white_F));
            mHolder.tv_price.setTextColor(context.getResources().getColor(R.color.white_F));
            mHolder.ll_xb.setBackground(context.getResources().getDrawable(R.drawable.xb_recharge_onclick));
        }
        return convertView;
    }

    static class ViewHolder{
        LinearLayout ll_xb;
        TextView tv_xb,tv_price;
    }
}
