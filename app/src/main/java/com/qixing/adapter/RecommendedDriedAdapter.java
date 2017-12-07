package com.qixing.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.app.MyApplication;
import com.qixing.bean.IndexJson;
import com.qixing.global.Constant;
import com.qixing.utlis.DateUtils;

import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class RecommendedDriedAdapter extends BaseAdapter{
    private List<IndexJson.Ghlist> list ;
    private Context context;
    public RecommendedDriedAdapter(Context context, List<IndexJson.Ghlist> list) {
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

            convertView=inflater.inflate(R.layout.item_recommended_dried,null);

            mHolder.img_icon = (ImageView) convertView.findViewById(R.id.item_recommended_dried_img_icon);
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.item_recommended_dried_tv_name);
            mHolder.tv_times = (TextView) convertView.findViewById(R.id.item_recommended_dried_tv_times);
            mHolder.img_new= (ImageView) convertView.findViewById(R.id.item_recommended_dried_img_new);
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

        mHolder.tv_name.setText(list.get(position).getG_title());
        mHolder.tv_times.setText(DateUtils.TimeStamp2DateYYYYMMDD(list.get(position).getTimes()));
//        mHolder.tv_num.setText("x "+list.get(position).getNum());

        if("1".equals(list.get(position).getIs_new())){
            mHolder.img_new.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(Constant.URL_IMG+MyApplication.getInstance().getNew_index(),mHolder.img_new);
        }else{
            mHolder.img_new.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder{
        private ImageView img_icon;
        private TextView tv_name;
        private TextView tv_times;
        private ImageView img_new;
    }
}
