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
import com.qixing.bean.SeeQXliveBean;
import com.qixing.bean.SeeQXliveRankBean;
import com.qixing.global.Constant;
import com.qixing.utlis.DateUtils;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.CircleImageView;

import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class SeeQXliveRankAdapter extends BaseAdapter{
    private List<SeeQXliveRankBean> list ;
    private Context context;
    public SeeQXliveRankAdapter(Context context, List<SeeQXliveRankBean> list) {
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position<9){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder mHolder=null;
        ViewHolder2 mHolder2=null;
        int type=getItemViewType(position);
        if(convertView==null){
            if(type==0) {
                mHolder = new ViewHolder();

                convertView = inflater.inflate(R.layout.item_qxlive_see2_rank, null);

                mHolder.img_head = (CircleImageView) convertView.findViewById(R.id.item_qxlive_see2_rank_img_head);
                mHolder.tv_lv = (TextView) convertView.findViewById(R.id.item_qxlive_see2_rank_tv_lv);
                mHolder.tv_name = (TextView) convertView.findViewById(R.id.item_qxlive_see2_rank_tv_name);
                mHolder.tv_money = (TextView) convertView.findViewById(R.id.item_qxlive_see2_rank_tv_money);
                convertView.setTag(mHolder);
            }else if(type==1){
                mHolder2 = new ViewHolder2();

                convertView = inflater.inflate(R.layout.item2_qxlive_see2_rank, null);

                mHolder2.img_head2 = (CircleImageView) convertView.findViewById(R.id.item2_qxlive_see2_rank_img_head);
                mHolder2.tv_lv2 = (TextView) convertView.findViewById(R.id.item2_qxlive_see2_rank_tv_lv);
                mHolder2.tv_name2 = (TextView) convertView.findViewById(R.id.item2_qxlive_see2_rank_tv_name);
                mHolder2.tv_money2 = (TextView) convertView.findViewById(R.id.item2_qxlive_see2_rank_tv_money);
                convertView.setTag(mHolder2);
            }
        }else {
            if(type==0) {
                mHolder = (ViewHolder) convertView.getTag();
            }else if(type==1){
                mHolder2= (ViewHolder2) convertView.getTag();
            }
        }
        if(type==0) {
            if (!TextUtils.isEmpty(list.get(position).getPic())) {
                if (list.get(position).getPic().startsWith("http")) {
                    ImageLoader.getInstance().displayImage(list.get(position).getPic(), mHolder.img_head, ImageLoaderOptions.getOptions());
                } else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + list.get(position).getPic(), mHolder.img_head, ImageLoaderOptions.getOptions());
                }
            }

            mHolder.tv_lv.setText("No." + (position + 1));
            mHolder.tv_name.setText(list.get(position).getUname() + "");
            mHolder.tv_money.setText("贡献 " + list.get(position).getZdou());
//        mHolder.tv_money.setText(DateUtils.TimeStamp2Date(DateUtils.StrngToLong(list.get(position).getTimes())));
        }else if(type==1){
            if (!TextUtils.isEmpty(list.get(position).getPic())) {
                if (list.get(position).getPic().startsWith("http")) {
                    ImageLoader.getInstance().displayImage(list.get(position).getPic(), mHolder2.img_head2, ImageLoaderOptions.getOptions());
                } else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + list.get(position).getPic(), mHolder2.img_head2, ImageLoaderOptions.getOptions());
                }
            }

            mHolder2.tv_lv2.setText("No." + (position + 1));
            mHolder2.tv_name2.setText(list.get(position).getUname() + "");
            mHolder2.tv_money2.setText("贡献 " + list.get(position).getZdou());
        }

        return convertView;
    }

    class ViewHolder{
        private CircleImageView img_head;
        private TextView tv_lv;
        private TextView tv_name;
        private TextView tv_money;
    }
    class ViewHolder2{
        private CircleImageView img_head2;
        private TextView tv_lv2;
        private TextView tv_name2;
        private TextView tv_money2;
    }
}
