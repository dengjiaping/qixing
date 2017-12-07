package com.qixing.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.qixing.bean.F2RecommendedVideoBean;
import com.qixing.global.Constant;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.imagecut.ImageTools;

import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class AllVideoAdapter extends BaseAdapter{
    private List<F2RecommendedVideoBean> list ;
    private Context context;
    public AllVideoAdapter(Context context, List<F2RecommendedVideoBean> list) {
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

            convertView=inflater.inflate(R.layout.item_fragment2_video,null);

            mHolder.img_icon = (ImageView) convertView.findViewById(R.id.item_fragment2_video_img_icon);
            mHolder.img_share = (ImageView) convertView.findViewById(R.id.item_fragment2_video_img_share);
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.item_fragment2_video_tv_name);
            mHolder.tv_num = (TextView) convertView.findViewById(R.id.item_fragment2_video_tv_num);
            mHolder.tv_context = (TextView) convertView.findViewById(R.id.item_fragment2_video_tv_context);
            mHolder.img_new= (ImageView) convertView.findViewById(R.id.item_fragment2_video_img_new);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(list.get(position).getV_pic())){
            if(list.get(position).getV_pic().startsWith("http")){
                ImageLoader.getInstance().displayImage(list.get(position).getV_pic(),mHolder.img_icon, ImageLoaderOptions.getOptions());
            }else {
                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+list.get(position).getV_pic(),mHolder.img_icon, ImageLoaderOptions.getOptions());
            }
        }

        mHolder.tv_name.setText(list.get(position).getV_title());
        mHolder.tv_context.setText("播放量:"+list.get(position).getSee_num());
//        mHolder.tv_num.setText("x "+list.get(position).getNum());

        mHolder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享
            }
        });

//        if(!("1".equals(list.get(position).getState()))){
////            mHolder.img_new.setVisibility(View.VISIBLE);
//            if(mHolder.tv_name.getLineCount()==1){
//                mHolder.img_new.setVisibility(View.VISIBLE);
//            }else {
//                Bitmap bitmap = ImageTools.createNewPic(context, R.drawable.icon_tag_new, 20, 10);
//                ImageSpan imageSpan = new ImageSpan(context, bitmap);
//                SpannableString spanString = new SpannableString("icon");
//                spanString.setSpan(imageSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                mHolder.tv_name.append(spanString);
//            }
//        }else{
////            mHolder.img_new.setVisibility(View.GONE);
//            if(mHolder.tv_name.getLineCount()==1){
//                mHolder.img_new.setVisibility(View.GONE);
//            }else {
//                mHolder.tv_name.append("");
//            }
//        }

        return convertView;
    }

    class ViewHolder{
        private ImageView img_icon;
        private TextView tv_name;
        private TextView tv_num;
        private TextView tv_context;
        private ImageView img_share;

        private ImageView img_new;
    }
}
