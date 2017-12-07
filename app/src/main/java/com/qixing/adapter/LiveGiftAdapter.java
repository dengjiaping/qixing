package com.qixing.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.bean.LiveGiftBean;
import com.qixing.global.Constant;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.CircleImageView;

import java.util.List;

/**
 * Created by lenovo on 2017/11/21.
 */
public class LiveGiftAdapter extends BaseAdapter {

    private Context context;
    private List<LiveGiftBean> list;
    private LayoutInflater mInflater;

    public LiveGiftAdapter(Context context, List<LiveGiftBean> list) {
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
            convertView=mInflater.inflate(R.layout.item_bond_list,null);
            mHolder=new ViewHolder();
            mHolder.img_rank= (ImageView) convertView.findViewById(R.id.item_bond_list_img_rank);
            mHolder.tv_rank= (TextView) convertView.findViewById(R.id.item_bond_list_tv_rank);
            mHolder.mCircleImageView= (CircleImageView) convertView.findViewById(R.id.item_bond_list_mCirCleImageView);
            mHolder.tv_name= (TextView) convertView.findViewById(R.id.item_bond_list_tv_name);
            mHolder.tv_devote= (TextView) convertView.findViewById(R.id.item_bond_list_tv_devote);
            convertView.setTag(mHolder);
        }else{
            mHolder= (ViewHolder) convertView.getTag();
        }
        mHolder.tv_rank.setText(position+4);
        if (!TextUtils.isEmpty(list.get(position).getPic())){
            if (list.get(position).getPic().startsWith("http")){
                ImageLoader.getInstance().displayImage(list.get(position).getPic(), mHolder.mCircleImageView, ImageLoaderOptions.getOptions());
            }else{
                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + list.get(position).getPic(), mHolder.mCircleImageView, ImageLoaderOptions.getOptions());
            }
        }
        mHolder.tv_name.setText(list.get(position).getUname());
        mHolder.tv_devote.setText(""+list.get(position).getAlljb());
        return convertView;
    }

    static class ViewHolder{
        TextView tv_rank,tv_name,tv_devote;
        CircleImageView mCircleImageView;
        ImageView img_rank;
    }
}
