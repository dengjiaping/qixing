package com.qixing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.bean.RechargeRecordBean;
import com.qixing.utlis.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by nuo-rui on 2017/6/8.
 */

public class RechargeRecordAdapter extends BaseAdapter {
    private Context mContext;
    private List<RechargeRecordBean> mList;

    public RechargeRecordAdapter(Context mContext, List<RechargeRecordBean> mList) {
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_cash_record,null);
            mHolder=new ViewHolder();
            mHolder.tv_recharge_name= (TextView) convertView.findViewById(R.id.item_cash_recharge_record_name);
            mHolder.tv_recharge_money= (TextView) convertView.findViewById(R.id.item_cash_recharge_record_money);
            mHolder.tv_recharge_time= (TextView) convertView.findViewById(R.id.item_cash_recharge_record_paymethod);
            mHolder.tv_recharge_status= (TextView) convertView.findViewById(R.id.item_cash_recharge_record_status);
            convertView.setTag(mHolder);
        }else{
            mHolder= (ViewHolder) convertView.getTag();
        }

        RechargeRecordBean recordBean=mList.get(position);
        if("1".equals(recordBean.getMethod())){
            mHolder.tv_recharge_name.setText("支付宝");
            mHolder.tv_recharge_money.setText("充值"+recordBean.getMoney()+"元");
        }else if("2".equals(recordBean.getMethod())){
            mHolder.tv_recharge_name.setText("微信");
            mHolder.tv_recharge_money.setText("充值"+recordBean.getMoney()+"元");
        }else if("3".equals(recordBean.getMethod())){
            mHolder.tv_recharge_name.setText("退款");
            mHolder.tv_recharge_money.setText("收入"+recordBean.getMoney()+"元");
        }else if("4".equals(recordBean.getMethod())){
            mHolder.tv_recharge_name.setText("花费");
            mHolder.tv_recharge_money.setText("支出"+recordBean.getMoney()+"元");
        }else if("5".equals(recordBean.getMethod())){
            mHolder.tv_recharge_name.setText("积分");
            mHolder.tv_recharge_money.setText("兑换"+recordBean.getMoney()+"元");
        }else{
            mHolder.tv_recharge_name.setText("平台充值");
            mHolder.tv_recharge_money.setText("充值"+recordBean.getMoney()+"元");
        }

        String format= DateUtils.TimeStamp2DateYYYYMMDD(recordBean.getTimes());
        mHolder.tv_recharge_time.setText(format);
        if("1".equals(recordBean.getStatus())) {
            mHolder.tv_recharge_status.setText("交易成功");
        }else{
            mHolder.tv_recharge_status.setText("交易失败");
        }
        return convertView;
    }

    class ViewHolder{
        TextView tv_recharge_name,tv_recharge_money,tv_recharge_time,tv_recharge_status;
    }
}
