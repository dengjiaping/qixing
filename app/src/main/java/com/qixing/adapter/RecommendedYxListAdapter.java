package com.qixing.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.app.MyApplication;
import com.qixing.bean.InfoJson;
import com.qixing.global.Constant;
import com.qixing.utlis.DateUtils;
import com.qixing.utlis.images.ImageLoaderOptions;

import java.util.List;

/**
 * Created by lenovo on 2017/7/5.
 */
public class RecommendedYxListAdapter extends BaseAdapter {

    private List<InfoJson.QxInfo> list;
    private Context context;

    public RecommendedYxListAdapter(Context context, List<InfoJson.QxInfo> list) {
        this.context = context;
        this.list = list;
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

            convertView=inflater.inflate(R.layout.item_yx_list_market,null);

            mHolder.img_icon = (ImageView) convertView.findViewById(R.id.item_yx_list_img_icon);
            mHolder.img_share = (ImageView) convertView.findViewById(R.id.item_yx_list_img_share);
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.item_yx_list_tv_name);
            mHolder.tv_times = (TextView) convertView.findViewById(R.id.item_yx_list_tv_times);
            mHolder.tv_num = (TextView) convertView.findViewById(R.id.item_yx_list_tv_num);

            mHolder.tv_name2 = (TextView) convertView.findViewById(R.id.item_yx_list_tv_name2);
            mHolder.tv_times2 = (TextView) convertView.findViewById(R.id.item_yx_list_tv_times2);
            mHolder.tv_num2 = (TextView) convertView.findViewById(R.id.item_yx_list_tv_num2);
            mHolder.img_icon1 = (ImageView) convertView.findViewById(R.id.item_yx_list_img_icon1);
            mHolder.img_icon2 = (ImageView) convertView.findViewById(R.id.item_yx_list_img_icon2);
            mHolder.img_icon3 = (ImageView) convertView.findViewById(R.id.item_yx_list_img_icon3);

            mHolder.ll_type1 = (LinearLayout) convertView.findViewById(R.id.item_yx_list_ll_type1);
            mHolder.ll_type2 = (LinearLayout) convertView.findViewById(R.id.item_yx_list_ll_type2);

            mHolder.img_new= (ImageView) convertView.findViewById(R.id.item_yx_list_img_new);
//            mHolder.view_divide=convertView.findViewById(R.id.item_yx_list_view);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
//        if(!TextUtils.isEmpty(list.get(position).getImg())){
//            if(list.get(position).getImg().startsWith("http")){
//                ImageLoader.getInstance().displayImage(list.get(position).getImg(),mHolder.img_icon, ImageLoaderOptions.get_face_Options());
//            }else {
//                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+list.get(position).getImg(),mHolder.img_icon, ImageLoaderOptions.getOptions());
//            }
//        }

//        mHolder.tv_name.setText(list.get(position).getName());
//        mHolder.tv_times.setText(list.get(position).getTimes());
//        mHolder.tv_num.setText("x "+list.get(position).getNum());

        mHolder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享
            }
        });

//        if("1".equals(list.get(position).getType())){
//            mHolder.ll_type1.setVisibility(View.VISIBLE);
//            mHolder.ll_type2.setVisibility(View.GONE);
//            mHolder.tv_times.setText(list.get(position).getTimes());
//        }else{
//            mHolder.ll_type1.setVisibility(View.GONE);
//            mHolder.ll_type2.setVisibility(View.VISIBLE);
//            mHolder.tv_times2.setText(list.get(position).getTimes());
//        }

        if(TextUtils.isEmpty(list.get(position).getG_pic2())){
            mHolder.ll_type1.setVisibility(View.VISIBLE);
            mHolder.ll_type2.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(list.get(position).getG_pic())){
                if(list.get(position).getG_pic().startsWith("http")){
                    ImageLoader.getInstance().displayImage(list.get(position).getG_pic(),mHolder.img_icon, ImageLoaderOptions.getOptions());
                }else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+list.get(position).getG_pic(),mHolder.img_icon, ImageLoaderOptions.getOptions());
                }
            }

            mHolder.tv_name.setText(list.get(position).getG_title());
//        mHolder.tv_num.setText("x "+list.get(position).getNum());
            mHolder.tv_times.setText(DateUtils.TimeStamp2DateYYYYMMDD(list.get(position).getTimes()));
        }else{
            mHolder.ll_type1.setVisibility(View.GONE);
            mHolder.ll_type2.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(list.get(position).getG_pic())){
                if(list.get(position).getG_pic().startsWith("http")){
                    ImageLoader.getInstance().displayImage(list.get(position).getG_pic(),mHolder.img_icon1, ImageLoaderOptions.getOptions());
                }else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+list.get(position).getG_pic(),mHolder.img_icon1, ImageLoaderOptions.getOptions());
                }
            }
            if(!TextUtils.isEmpty(list.get(position).getG_pic1())){
                if(list.get(position).getG_pic().startsWith("http")){
                    ImageLoader.getInstance().displayImage(list.get(position).getG_pic1(),mHolder.img_icon2, ImageLoaderOptions.getOptions());
                }else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+list.get(position).getG_pic1(),mHolder.img_icon2, ImageLoaderOptions.getOptions());
                }
            }
            if(!TextUtils.isEmpty(list.get(position).getG_pic2())){
                if(list.get(position).getG_pic().startsWith("http")){
                    ImageLoader.getInstance().displayImage(list.get(position).getG_pic2(),mHolder.img_icon3, ImageLoaderOptions.getOptions());
                }else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+list.get(position).getG_pic2(),mHolder.img_icon3, ImageLoaderOptions.getOptions());
                }
            }

            mHolder.tv_name2.setText(list.get(position).getG_title());
//        mHolder.tv_num2.setText("x "+list.get(position).getNum());
            mHolder.tv_times2.setText(DateUtils.TimeStamp2DateYYYYMMDD(list.get(position).getTimes()));

//            if(!("1".equals(MyApplication.getInstance().getInfo_new()))){
//                mHolder.img_new.setVisibility(View.VISIBLE);
//            }else{
//                mHolder.img_new.setVisibility(View.GONE);
//            }

//            if(list.size()>1){
//                mHolder.view_divide.setVisibility(View.VISIBLE);
//            }else{
//                mHolder.view_divide.setVisibility(View.INVISIBLE);
//            }
        }

        return convertView;
    }

    class ViewHolder{
        private ImageView img_icon;
        private TextView tv_name;
        private TextView tv_num;
        private TextView tv_times;
        private ImageView img_share;

        private TextView tv_name2;
        private ImageView img_icon1,img_icon2,img_icon3;
        private TextView tv_times2;
        private TextView tv_num2;

        private LinearLayout ll_type1,ll_type2;
        private ImageView img_new;
//        private View view_divide;
    }
}
