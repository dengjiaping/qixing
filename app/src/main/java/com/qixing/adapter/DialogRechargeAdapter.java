package com.qixing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.bean.InfoDriedJson;
import com.qixing.bean.RechargeBean;
import com.qixing.utlis.DateUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class DialogRechargeAdapter extends BaseAdapter{
    private List<RechargeBean> list ;
    private Context context;
    public DialogRechargeAdapter(Context context, List<RechargeBean> list) {
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

            convertView=inflater.inflate(R.layout.item_dialog_recharge,null);

            mHolder.ll_recharge = (LinearLayout) convertView.findViewById(R.id.item_dialog_recharge_ll_recharge);
            mHolder.tv_money = (TextView) convertView.findViewById(R.id.item_dialog_recharge_tv_money);
            mHolder.tv_xingbi = (TextView) convertView.findViewById(R.id.item_dialog_recharge_tv_xingbi);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
//        if(!TextUtils.isEmpty(list.get(position).getG_url())){
//            if(list.get(position).getG_url().startsWith("http")){
//                ImageLoader.getInstance().displayImage(list.get(position).getG_url(),mHolder.img_icon, ImageLoaderOptions.get_face_Options());
//            }else {
//                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+list.get(position).getG_url(),mHolder.img_icon, ImageLoaderOptions.getOptions());
//            }
//        }
        DecimalFormat df=new DecimalFormat("######0.00");
        mHolder.tv_money.setText("￥:"+df.format(Double.valueOf(list.get(position).getMoney())));
        mHolder.tv_xingbi.setText(list.get(position).getNum()+"星币");
        if("0".equals(list.get(position).getSelect())){
            mHolder.ll_recharge.setBackground(context.getResources().getDrawable(R.drawable.bg_item_recharge_gray));
        }else if("1".equals(list.get(position).getSelect())){
            mHolder.ll_recharge.setBackground(context.getResources().getDrawable(R.drawable.bg_item_recharge_orange));
        }

        return convertView;
    }

    class ViewHolder{
        private LinearLayout ll_recharge;
        private TextView tv_money;
        private TextView tv_xingbi;
    }
}
