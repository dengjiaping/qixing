package com.qixing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.qixing.R;
import com.qixing.bean.QXLiveInfoBean;
import com.qixing.global.Constant;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.bean.QXLiveInfoBean;

import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class QXliveHeadingAdapter extends BaseAdapter{
    private List<QXLiveInfoBean> list ;
    private Context context;
    public QXliveHeadingAdapter(Context context, List<QXLiveInfoBean> list) {
        this.list = list;
        this.context = context;
//        (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            convertView=inflater.inflate(R.layout.item_hdlive_headimg,null);
            mHolder.img_head= (CircleImageView) convertView.findViewById(R.id.item_qxlive_headimg_img_head);
            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + list.get(position).getPic(),mHolder.img_head, ImageLoaderOptions.getOptions());
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
        return convertView;

    }

    class ViewHolder{
        private CircleImageView img_head;
    }
}
