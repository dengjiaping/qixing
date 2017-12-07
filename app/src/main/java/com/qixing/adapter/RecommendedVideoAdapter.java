package com.qixing.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.app.MyApplication;
import com.qixing.bean.IndexJson;
import com.qixing.global.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.qxlive.rongyun.controller.CommonUtil;
import com.qixing.utlis.CommonUtils;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.view.imagecut.ImageTools;

import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class RecommendedVideoAdapter extends BaseAdapter {
    private List<IndexJson.Video> list;
    private Context context;

    public RecommendedVideoAdapter(Context context, List<IndexJson.Video> list) {
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
        ViewHolder mHolder = null;
        if (convertView == null) {

            mHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.item_recommended_video, null);

            mHolder.img_icon = (ImageView) convertView.findViewById(R.id.item_recommended_video_img_icon);
            mHolder.img_share = (ImageView) convertView.findViewById(R.id.item_recommended_video_img_share);
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.item_recommended_video_tv_name);
            mHolder.tv_num = (TextView) convertView.findViewById(R.id.item_recommended_video_tv_num);
            mHolder.tv_context = (TextView) convertView.findViewById(R.id.item_recommended_video_tv_context);
            mHolder.tv_share = (TextView) convertView.findViewById(R.id.item_recommended_video_tv_share);
            mHolder.img_new = (ImageView) convertView.findViewById(R.id.item_recommended_video_img_new);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(list.get(position).getV_pic())) {
            if (list.get(position).getV_pic().startsWith("http")) {
                ImageLoader.getInstance().displayImage(list.get(position).getV_pic(), mHolder.img_icon, ImageLoaderOptions.getOptions());
            } else {
                ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + list.get(position).getV_pic(), mHolder.img_icon, ImageLoaderOptions.getOptions());
            }
        }

        mHolder.tv_name.setText(list.get(position).getV_title());
        mHolder.tv_context.setText(list.get(position).getSp_nr());
        mHolder.tv_num.setText("" + list.get(position).getSee_num());
        mHolder.tv_share.setText("" + list.get(position).getShare_num());

        mHolder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享
            }
        });

//        if (!"1".equals(MyApplication.getInstance().getFirst_enter())&&!list.get(position).getId().equals(MyApplication.getInstance().getInfo_new())){
        if ("1".equals(list.get(position).getIs_new())) {
//            System.out.println("==================================Bitmap bitmap==========" + MyApplication.getInstance().getNew_tag());
//            Bitmap bitmap = ImageTools.createPic(MyApplication.getInstance().getNew_tag(), 200, 200);
//            ImageSpan imageSpan = new ImageSpan(context, bitmap);
//            SpannableString spanString = new SpannableString("icon");
//            spanString.setSpan(imageSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            mHolder.tv_name.append(spanString);
            System.out.println("=================================tag 路径========="+MyApplication.getInstance().getNew_index());
            if (!TextUtils.isEmpty(MyApplication.getInstance().getNew_tag())) {
                addImage(mHolder.tv_name, mHolder.tv_name.getText().toString(), MyApplication.getInstance().getNew_tag(), 2);
            }else{

            }

        } else {
            mHolder.tv_name.append("");
        }

        return convertView;
    }

    class ViewHolder {
        private ImageView img_icon;
        private TextView tv_name;
        private TextView tv_num;
        private TextView tv_context;
        private ImageView img_share;
        private TextView tv_share;
        private ImageView img_new;
    }

    /**
     * Textview追加图片方法
     *
     * @param textView 文本控件
     * @param content  内容
     * @param res      图片id
     * @param maxLines 最大行数
     */
    private void addImage(TextView textView, String content, String res,
                          int maxLines) {
        textView.setMaxLines(maxLines);// 设置最大行数
        Drawable drawable=ImageTools.createPic(res,200,200);
        TextPaint paint = textView.getPaint();// 获取文本控件字体样式
        Paint.FontMetrics fm = paint.getFontMetrics();
        int textFontHeight = (int) Math.ceil(fm.descent - fm.top) + 2;// 计算字体高度座位图片高度
        int imageWidth = drawable.getIntrinsicWidth() * textFontHeight
                / drawable.getIntrinsicHeight();// 计算图片根据字体大小等比例缩放后的宽度
        drawable.setBounds(0, CommonUtils.dip2px(context, 1), imageWidth / 2,
                textFontHeight / 2);// 缩放图片
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        int maxWidth = width - CommonUtils.dip2px(context, 179);// 获取文本控件最大宽度
        float textWidth = paint.measureText(content);
        if (textWidth > maxWidth) {// 如果文本大于一行才进入复杂的计算逻辑
            String s = "";// 临时存储截取的字符串
            int start = 0;// 截取的起始位置
            int end = 1;// 截取的结束位置
            int lines = 1;// 计算的行数

            float index = paint.measureText("...");
            if ((textWidth + index + imageWidth / 2) > (2 * maxWidth)) {
                int length = (int) ((textWidth + index + imageWidth / 2) - (2 * maxWidth));
                System.out.println("================================字符串 content长度========" + content.length());
                System.out.println("================================length===========" + length);
                int size = (int) (textWidth / content.length());
                System.out.println("================================size===========" + size);
                end = (int) (content.length() - (length / size) - (index / size));
                System.out.println("================================end===========" + end);
                content = content.substring(0, end - 2);
                content+="...";
            }
            ;
        }
        // 文本后面拼接图片
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        content += "*";
        SpannableString spanString = new SpannableString(content);
        spanString.setSpan(span, content.length() - 1, content.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanString);
    }

}
