package com.qixing.activity.child;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qixing.R;
import com.qixing.activity.ShopMineActivity;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.bean.ResultBean;
import com.qixing.global.Constant;
import com.qixing.qxlive.QXLiveShowActivity;
import com.qixing.view.imagecut.ClipImageLayout;
import com.qixing.view.imagecut.ImageTools;
import com.qixing.widget.Toasts;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by SunQi on 2015/7/27.
 */
public class ImageCutActivity extends BaseActivity {

    private ClipImageLayout mClipImageLayout;
    public static  String path;

    public static  String photo_name;

    private ArrayList<String> mSelectPath;
    private Button btn_sure;

    private ProgressDialog progressDialog;

    public static String bit_map_str;

    private String tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view=getLayoutInflater().inflate(R.layout.activity_image_cut,null);
        setContentView(view);

        initBackListener(view);

        Intent intent=getIntent();

        progressDialog=new ProgressDialog(mContext);
        if(intent!=null){
            mSelectPath=intent.getStringArrayListExtra("path");
            tag = intent.getStringExtra("tag");
        }
        mClipImageLayout= (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        for (String path:mSelectPath){


            Bitmap bitmap= ImageTools.convertToBitmap(path, 600, 600);
            if(bitmap==null){

                Toasts.show("图片加载失败!");
            }

            mClipImageLayout.setBitmap(bitmap);




        }

        btn_sure= (Button) findViewById(R.id.btn_sure);

        btn_sure.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();



    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.btn_sure:
                progressDialog.show();
                progressDialog.setMessage("加载中...");

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        Bitmap bitmap = mClipImageLayout.clip();


                        path= Environment.getExternalStorageDirectory()+"/qixing/user_header/";

//                        photo_name=System.currentTimeMillis()+".png";
                        photo_name=System.currentTimeMillis()+".jpg";



                        ImageTools.savePhotoToSDCard(bitmap, path, photo_name);


                //        bit_map_str= ImageTools.bitmapToBase64(bitmap);


                        progressDialog.dismiss();


                        if("ShopMineActivity".equals(tag)){
                            AppManager.getAppManager().startNextActivity(mContext,ShopMineActivity.class);
                        }else if("QXLiveShowActivity".equals(tag)){
                            AppManager.getAppManager().startNextActivity(mContext,QXLiveShowActivity.class);
                        }
//                        else if("FinancingMineActivity".equals(tag)){
//                            AppManager.getAppManager().startNextActivity(FinancingMineActivity.class);
//                        }else if("Financing_fragment3".equals(tag)){
//                            Intent intent = new Intent();
//                            intent.putExtra("vp", 3);
//                            AppManager.getAppManager().startNextActivity(mContext, MainActivity2.class, intent);
////                            bitmapToBase64(path+photo_name);
//                        }

//

                        finish();
                    }
                }).start();


                break;

        }
    }


    private Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        if( baos.toByteArray().length / 1024>200) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }else if(image.getHeight()>200||image.getWidth()>200){

            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = 200f;
        float ww = 200f;

        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例

        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }


//================================================================================================================
    private Bitmap bitmap;
    private String img_base64 ;
    private ResultBean mResultBean;
    private void bitmapToBase64(String path){
        System.out.println("===========选择的图片路径是"+path);
        bitmap= ImageTools.createThumbnail(path, 200, 200);
        img_base64=ImageTools.bitmapToBase64(bitmap);
        upHeadPhoto();
//        ImageLoader.getInstance().displayImage("file://" + path, img_head, ImageLoaderOptions.get_face_Options());
    }

    private void upHeadPhoto(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        }
        mAsyncHttpClient = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        params.put("uid", MyApplication.getInstance().getUid());
        params.put("pic", img_base64);//img_base64
        params.put("azpic", "1");
        final String url = Constant.BASE_URL;

        mAsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                System.out.println("===========================url = " + url);
                System.out.println("===========================params = " + params.toString());
                System.out.println("===========================个人中心 修改头像 response = " + response.toString());
                if (!TextUtils.isEmpty(response.toString())) {
                    mResultBean = new Gson().fromJson(response.toString(), ResultBean.class);
                    if (mResultBean.getResult().equals("1")) {
                        Toasts.show(mResultBean.getMessage());
//                        MyApplication.getInstance().setUser_Head(mResultBean.getImgs());
//                        Intent intent = new Intent();
//                        intent.putExtra("vp", 3);
//                        AppManager.getAppManager().startNextActivity(mContext, MainActivity2.class, intent);
//                        ((Activity)MainActivity2.context).finish();
                    } else {
                        Toasts.show(mResultBean.getMessage());
                    }
                } else {
                    showErrorDialog(mContext);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                showErrorDialog(mContext);
            }
        });
    }
}
