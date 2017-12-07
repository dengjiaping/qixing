package com.qixing.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.global.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.bean.HistoryBean;
import com.qixing.utlis.DateUtils;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.CircleImageView;
import com.qixing.view.imagecut.ClipImageBorderView;

import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class HistoryAdapter extends BaseAdapter{
    private List<HistoryBean> list ;
    private Context context;
    private String type;
    public HistoryAdapter(Context context, List<HistoryBean> list,String type) {
        this.list = list;
        this.context = context;
        this.type = type;
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

            convertView=inflater.inflate(R.layout.item_history,null);

            mHolder.img_icon= (ImageView) convertView.findViewById(R.id.item_history_img_icon);
            mHolder.tv_name= (TextView) convertView.findViewById(R.id.item_history_tv_name);
            mHolder.tv_context= (TextView) convertView.findViewById(R.id.item_history_tv_context);
            mHolder.tv_num= (TextView) convertView.findViewById(R.id.item_history_tv_num);
            mHolder.ll_video= (LinearLayout) convertView.findViewById(R.id.item_history_ll_video);

            mHolder.rl_live= (RelativeLayout) convertView.findViewById(R.id.item_history_rl_live);
            mHolder.img_icon2= (ImageView) convertView.findViewById(R.id.item_history_img_icon2);
            mHolder.tv_num= (TextView) convertView.findViewById(R.id.item_history_tv_num);
            mHolder.btn_state= (Button) convertView.findViewById(R.id.item_history_btn_state);
            mHolder.img_head= (CircleImageView) convertView.findViewById(R.id.item_history_img_head);
            mHolder.tv_title= (TextView) convertView.findViewById(R.id.item_history_tv_title);
            mHolder.tv_name2= (TextView) convertView.findViewById(R.id.item_history_tv_name2);
            mHolder.tv_times= (TextView) convertView.findViewById(R.id.item_history_tv_times);
            mHolder.tv_share= (TextView) convertView.findViewById(R.id.item_history_tv_share);

            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }

        if("1".equals(type)){//直播
            mHolder.ll_video.setVisibility(View.GONE);
            mHolder.rl_live.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(list.get(position).getZb_pic())){
                if(list.get(position).getZb_pic().startsWith("http")){
                    ImageLoader.getInstance().displayImage(list.get(position).getZb_pic(), mHolder.img_icon2, ImageLoaderOptions.getOptions());
                }else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + list.get(position).getZb_pic(), mHolder.img_icon2, ImageLoaderOptions.getOptions());
                }
            }
            if(!TextUtils.isEmpty(list.get(position).getPic())){
                if(list.get(position).getPic().startsWith("http")){
                    ImageLoader.getInstance().displayImage(list.get(position).getPic(), mHolder.img_head, ImageLoaderOptions.get_face_Options());
                }else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + list.get(position).getPic(), mHolder.img_head, ImageLoaderOptions.get_face_Options());
                }
            }
            mHolder.tv_title.setText(list.get(position).getZb_title());
            mHolder.tv_name2.setText(list.get(position).getUname());
            mHolder.tv_times.setText(DateUtils.TimeStamp2Date(DateUtils.StrngToLong(list.get(position).getTimes())));
//            mHolder.tv_num.setText(list.get(position).getSee_num());
        }else if("2".equals(type)){//视频
            mHolder.ll_video.setVisibility(View.VISIBLE);
            mHolder.rl_live.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(list.get(position).getV_pic())){
                if(list.get(position).getV_pic().startsWith("http")){
                    ImageLoader.getInstance().displayImage(list.get(position).getV_pic(), mHolder.img_icon, ImageLoaderOptions.getOptions());
                }else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + list.get(position).getV_pic(), mHolder.img_icon, ImageLoaderOptions.getOptions());
                }
            }
            mHolder.tv_name.setText(list.get(position).getV_title());
            mHolder.tv_context.setText(list.get(position).getSp_nr());
            mHolder.tv_num.setText(list.get(position).getSee_num());
            mHolder.tv_share.setText(list.get(position).getShare_num());
        }

        return convertView;

    }
    class ViewHolder{

        private ImageView img_icon;

        private TextView tv_name;
        private TextView tv_context;
        private TextView tv_num;
        private LinearLayout ll_video;

        private RelativeLayout rl_live;
        private ImageView img_icon2;
        private Button btn_state;
        private CircleImageView img_head;
        private TextView tv_title;
        private TextView tv_name2;
        private TextView tv_times;
        private TextView tv_share;


    }
}
