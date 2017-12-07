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
import com.qixing.bean.SearchBean;
import com.qixing.global.Constant;
import com.qixing.utlis.DateUtils;
import com.qixing.utlis.images.ImageLoaderOptions;

import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class SearchAdapter extends BaseAdapter{
    private List<SearchBean> list ;
    private Context context;
    public SearchAdapter(Context context, List<SearchBean> list) {
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

            convertView=inflater.inflate(R.layout.item_search,null);

            mHolder.img_icon= (ImageView) convertView.findViewById(R.id.item_search_img_icon);
            mHolder.tv_name= (TextView) convertView.findViewById(R.id.item_search_tv_name);
            mHolder.tv_context= (TextView) convertView.findViewById(R.id.item_search_tv_context);
            mHolder.tv_num= (TextView) convertView.findViewById(R.id.item_search_tv_num);
            mHolder.tv_share = (TextView) convertView.findViewById(R.id.item_search_tv_share);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }

        if(!TextUtils.isEmpty(list.get(position).getV_pic())){
            if(list.get(position).getV_pic().startsWith("http")){
                ImageLoader.getInstance().displayImage(list.get(position).getV_pic(), mHolder.img_icon, ImageLoaderOptions.getOptions());
            }else {
                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + list.get(position).getV_pic(), mHolder.img_icon, ImageLoaderOptions.getOptions());
            }
        }
        mHolder.tv_name.setText(list.get(position).getV_title());
        mHolder.tv_context.setText(list.get(position).getSp_nr());
//        mHolder.tv_num.setText(DateUtils.TimeStamp2DateYYYYMMDD(list.get(position).getTimes()));
        mHolder.tv_num.setText(""+list.get(position).getSee_num());
        mHolder.tv_share.setText(""+list.get(position).getShare_num());

        return convertView;

    }
    class ViewHolder{

        private ImageView img_icon;

        private TextView tv_name;
        private TextView tv_context;
        private TextView tv_num;
        private ImageView img_share;
        private TextView tv_share;

    }
}
