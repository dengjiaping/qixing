package com.qixing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.bean.MyRebateBean;
import com.qixing.bean.MyRebateJson;

import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class MyRebateAdapter extends BaseAdapter{
    private List<MyRebateJson.Jxs> list ;
    private Context context;
    public MyRebateAdapter(Context context, List<MyRebateJson.Jxs> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder mHolder=null;
        if(convertView==null){

            mHolder=new ViewHolder();

            convertView=inflater.inflate(R.layout.item_my_rebate,null);

            mHolder.tv_times= (TextView) convertView.findViewById(R.id.item_my_rebate_tv_times);
            mHolder.tv_name= (TextView) convertView.findViewById(R.id.item_my_rebate_tv_name);
            mHolder.tv_lv= (TextView) convertView.findViewById(R.id.item_my_rebate_tv_lv);
            mHolder.tv_num= (TextView) convertView.findViewById(R.id.item_my_rebate_tv_num);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
        mHolder.tv_times.setText(list.get(position).getTimes());
        mHolder.tv_name.setText(list.get(position).getUname());
        mHolder.tv_lv.setText(list.get(position).getDj());
        mHolder.tv_num.setText(list.get(position).getTjnum());

        return convertView;

    }
    class ViewHolder{

        private TextView tv_times;
        private TextView tv_name;
        private TextView tv_lv;
        private TextView tv_num;

    }
}
