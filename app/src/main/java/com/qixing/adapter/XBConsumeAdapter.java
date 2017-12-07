package com.qixing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.bean.XBConsumeBean;
import com.qixing.utlis.DateUtils;

import java.util.List;

/**
 * Created by lenovo on 2017/10/24.
 */
public class XBConsumeAdapter extends BaseAdapter {

    private Context context;
    private List<XBConsumeBean> list;
    private LayoutInflater mInflater;

    public XBConsumeAdapter(Context context, List<XBConsumeBean> list) {
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
            mHolder.iv_gift= (ImageView) convertView.findViewById(R.id.item_gift_record_iv_pic);
            mHolder.tv_intro= (TextView) convertView.findViewById(R.id.item_gift_record_tv_intro);
            mHolder.tv_num= (TextView) convertView.findViewById(R.id.item_gift_record_tv_num);
            mHolder.tv_time= (TextView) convertView.findViewById(R.id.item_gift_record_tv_time);
            convertView.setTag(mHolder);
        }else{
            mHolder= (ViewHolder) convertView.getTag();
        }
        if ("1".equals(list.get(position).getTypes())){
            mHolder.tv_intro.setText("送一朵鲜花");
        }else if ("2".equals(list.get(position).getTypes())){
            mHolder.tv_intro.setText("送一个掌声");
        }else if ("3".equals(list.get(position).getTypes())){
            mHolder.tv_intro.setText("送一块蛋糕");
        }else if ("4".equals(list.get(position).getTypes())){
            mHolder.tv_intro.setText("送了一瓶香槟");
        }else if ("5".equals(list.get(position).getTypes())){
            mHolder.tv_intro.setText("送了一束鲜花");
        }else if ("6".equals(list.get(position).getTypes())){
            mHolder.tv_intro.setText("送了一个美女");
        }else if ("7".equals(list.get(position).getTypes())){
            mHolder.tv_intro.setText("送了一辆法拉利");
        }else if ("8".equals(list.get(position).getTypes())){
            mHolder.tv_intro.setText("送了一艘游轮");
        }
        mHolder.tv_num.setText("-"+list.get(position).getNum());
        mHolder.tv_time.setText(DateUtils.TimeStamp2DateYYYYMMDDHHmmss(list.get(position).getTimes()));
        return convertView;
    }

    static class ViewHolder{
        ImageView iv_gift;
        TextView tv_num,tv_intro,tv_time;
    }
}
