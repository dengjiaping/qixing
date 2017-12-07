package com.qixing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.bean.GiftBean;

import java.util.List;

import me.nereo.multi_image_selector.bean.Image;

/**
 * Created by wicep on 2015/12/25.
 */
public class DialogGiftAdapter extends BaseAdapter{
    private List<GiftBean> list ;
    private Context context;
    public DialogGiftAdapter(Context context, List<GiftBean> list) {
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

            convertView=inflater.inflate(R.layout.item_dialog_gift,null);

            mHolder.ll_recharge = (RelativeLayout) convertView.findViewById(R.id.item_dialog_gift_ll_gift);
            mHolder.img_gift = (ImageView) convertView.findViewById(R.id.item_dialog_gift_img_gift);
            mHolder.img_select = (ImageView) convertView.findViewById(R.id.item_dialog_gift_img_select);
            mHolder.tv_xingbi = (TextView) convertView.findViewById(R.id.item_dialog_gift_tv_xingbi);
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
        mHolder.img_gift.setImageDrawable(context.getResources().getDrawable(list.get(position).getImgID()));
        mHolder.tv_xingbi.setText(list.get(position).getXingbi()+"星币");
//        mHolder.tv_xingbi.setText(list.get(position).getName());
        if("0".equals(list.get(position).getSelect())){
            mHolder.img_select.setVisibility(View.GONE);
            mHolder.ll_recharge.setBackground(context.getResources().getDrawable(R.drawable.bg_item_recharge_gray));
        }else if("1".equals(list.get(position).getSelect())){
            mHolder.img_select.setVisibility(View.VISIBLE);
            mHolder.ll_recharge.setBackground(context.getResources().getDrawable(R.drawable.bg_item_recharge_orange));
        }

        return convertView;
    }

    class ViewHolder{
        private RelativeLayout ll_recharge;
        private ImageView img_gift;
        private ImageView img_select;
        private TextView tv_xingbi;
    }
}
