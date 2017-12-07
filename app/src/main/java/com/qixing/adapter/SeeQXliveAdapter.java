package com.qixing.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.bean.RecommendedDriedBean;
import com.qixing.bean.SeeQXliveBean;
import com.qixing.global.Constant;
import com.qixing.utlis.DateUtils;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.CircleImageView;

import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class SeeQXliveAdapter extends BaseAdapter{
    private List<SeeQXliveBean> list ;
    private Context context;
    public SeeQXliveAdapter(Context context, List<SeeQXliveBean> list) {
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

            convertView=inflater.inflate(R.layout.item_seeqxlive,null);

            mHolder.img_icon = (ImageView) convertView.findViewById(R.id.item_seeqxlive_img_icon);
            mHolder.btn_state = (Button) convertView.findViewById(R.id.item_seeqxlive_btn_state);
            mHolder.img_head = (CircleImageView) convertView.findViewById(R.id.item_seeqxlive_img_head);
            mHolder.tv_title = (TextView) convertView.findViewById(R.id.item_seeqxlive_tv_title);
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.item_seeqxlive_tv_name);
            mHolder.tv_times = (TextView) convertView.findViewById(R.id.item_seeqxlive_tv_times);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(list.get(position).getZb_pic())){
            if(list.get(position).getZb_pic().startsWith("http")){
                ImageLoader.getInstance().displayImage(list.get(position).getZb_pic(),mHolder.img_icon, ImageLoaderOptions.getOptions());
            }else {
                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+list.get(position).getZb_pic(),mHolder.img_icon, ImageLoaderOptions.getOptions());
            }
        }
        if(!TextUtils.isEmpty(list.get(position).getPic())){
            if(list.get(position).getPic().startsWith("http")){
                ImageLoader.getInstance().displayImage(list.get(position).getPic(),mHolder.img_head, ImageLoaderOptions.getOptions());
            }else {
                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG+list.get(position).getPic(),mHolder.img_head, ImageLoaderOptions.getOptions());
            }
        }

        mHolder.tv_title.setText(list.get(position).getZb_title());
        mHolder.tv_name.setText(list.get(position).getUname());
        mHolder.tv_times.setText(DateUtils.TimeStamp2Date(DateUtils.StrngToLong(list.get(position).getTimes())));
//        mHolder.tv_num.setText("x "+list.get(position).getNum());

        if("0".equals(list.get(position).getStatus())){
            mHolder.btn_state.setText("已结束");
            mHolder.btn_state.setVisibility(View.VISIBLE);
        }else if("1".equals(list.get(position).getStatus())){
            mHolder.btn_state.setText("直播中");
            mHolder.btn_state.setVisibility(View.VISIBLE);
        }else if("2".equals(list.get(position).getStatus())){
            mHolder.btn_state.setText("未开始");
            mHolder.btn_state.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    class ViewHolder{
        private ImageView img_icon;
        private Button btn_state;

        private CircleImageView img_head;
        private TextView tv_title;
        private TextView tv_name;
        private TextView tv_times;
    }
}
