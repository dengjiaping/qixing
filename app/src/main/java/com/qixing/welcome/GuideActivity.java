package com.qixing.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qixing.MainActivity;
import com.qixing.R;
import com.qixing.app.MyApplication;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;

public class GuideActivity extends Activity {

    private ResultBean mResultBean;
    private TextView tv_go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        tv_go = (TextView)findViewById(R.id.guide_tv_go);
        tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("1".equals(MyApplication.getInstance().getIsLogining())) {//已登录
                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                    GuideActivity.this.startActivity(intent);
                    GuideActivity.this.finish();
                }else{
                    Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
//                    intent.putExtra("ADimgs",mResultBean.getADimgs());
                    GuideActivity.this.startActivity(intent);
                    GuideActivity.this.finish();
                }
            }
        });

        mResultBean = (ResultBean)getIntent().getSerializableExtra("mResultBean");

        BGABanner banner = (BGABanner)findViewById(R.id.banner_splash_pager);
        // 用Java代码方式设置切换动画
        banner.setTransitionEffect(TransitionEffect.Default);
        // banner.setPageTransformer(new RotatePageTransformer());
        // 设置page切换时长
        banner.setPageChangeDuration(1000);
        List<View> views = new ArrayList<>();
//        views.add(getPageView(R.drawable.guide_1));
//        views.add(getPageView(R.drawable.guide_2));
//        views.add(getPageView(R.drawable.guide_3));

//        View lastView = getLayoutInflater().inflate(R.layout.view_last, null);
//        views.add(lastView);
//        lastView.findViewById(R.id.btn_last_main).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(GuideActivity2.this, MainActivity.class));
//                finish();
//            }
//        });
//
        if(mResultBean!=null){

            views.add(getPageView(mResultBean.getPiclist().get(0).getPic()));
            views.add(getPageView(mResultBean.getPiclist().get(1).getPic()));
            View lastView = getLayoutInflater().inflate(R.layout.view_last, null);
            ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + mResultBean.getPiclist().get(2).getPic(), (ImageView)lastView.findViewById(R.id.img_last_main), ImageLoaderOptions.getOptions());
            views.add(lastView);
            lastView.findViewById(R.id.btn_last_main).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("1".equals(MyApplication.getInstance().getIsLogining())) {//已登录
                        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                        GuideActivity.this.startActivity(intent);
                        GuideActivity.this.finish();
                    }else{
                        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
//                    intent.putExtra("ADimgs",mResultBean.getADimgs());
                        GuideActivity.this.startActivity(intent);
                        GuideActivity.this.finish();
                    }
                }
            });
        }else{
            startActivity(new Intent(GuideActivity.this, MainActivity.class));
            finish();
        }

        banner.setData(views);
        // banner.setCurrentItem(1);
    }

    private View getPageView(@DrawableRes int resid) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(resid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
    private View getPageView(String urlImg) {
        ImageView imageView = new ImageView(this);
//        imageView.setImageResource(resid);
        ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + urlImg, imageView, ImageLoaderOptions.getOptions());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}
