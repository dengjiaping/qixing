package com.qixing.view.imagecut;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.util.Base64;
import android.util.Log;

import com.qixing.utlis.SDCardUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Tools for handler picture
 *
 * @author Ryan.Tang
 */
public final class ImageTools {


    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        if (checkSDCardAvailable()) {
            SDCardUtils.isFloederMoreExits(path);
            File photoFile = new File(path, photoName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (Exception e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void savePhotoToSDCard(Bitmap photoBitmap, String path) {
        if (checkSDCardAvailable()) {

            SDCardUtils.isFloederMoreExits(path);
            File photoFile = new File(path);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (Exception e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Check the SD card
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 根据路径加载bitmap
     *
     * @param path 路径
     * @param w    款
     * @param h    长
     * @return
     */
    public static final Bitmap convertToBitmap(String path, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            // 设置为ture只获取图片大小
            opts.inJustDecodeBounds = true;
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            // 返回为空
            BitmapFactory.decodeFile(path, opts);
            int width = opts.outWidth;
            int height = opts.outHeight;
            float scaleWidth = 0.f, scaleHeight = 0.f;
            if (width > w || height > h) {
                // 缩放
                scaleWidth = ((float) width) / w;
                scaleHeight = ((float) height) / h;
            }
            opts.inJustDecodeBounds = false;
            float scale = Math.max(scaleWidth, scaleHeight);
            opts.inSampleSize = (int) scale;
            WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
            Bitmap bMapRotate = Bitmap.createBitmap(weak.get(), 0, 0, weak.get().getWidth(), weak.get().getHeight(), null, true);
            if (bMapRotate != null) {
                return bMapRotate;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;

        ByteArrayOutputStream baos = null;

        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap createThumbnail(String filepath, int newWidth, int newHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 第一次采样:仅仅得到原图的宽度和高度，从而获取缩放比例
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;
        int ratioWidth = originalWidth / newWidth;
        int ratioHeight = originalHeight / newHeight;
        // 缩放比例(取值大的)
        options.inSampleSize = (ratioWidth > ratioHeight) ? ratioWidth : ratioHeight;
        // 第二次采样：为了获取缩放后图片的所有像素点
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }


    public static Bitmap createNewPic(Context context, int id, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 第一次采样:仅仅得到原图的宽度和高度，从而获取缩放比例
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), id);
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;
        int ratioWidth = originalWidth / width;
        int ratioHeight = originalHeight / height;
        // 缩放比例(取值大的)
        options.inSampleSize = (ratioWidth > ratioHeight) ? ratioWidth : ratioHeight;
        // 第二次采样：为了获取缩放后图片的所有像素点
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), id);
    }

    public static Drawable createPic(String path, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 第一次采样:仅仅得到原图的宽度和高度，从而获取缩放比例
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;
        int ratioWidth = originalWidth / width;
        int ratioHeight = originalHeight / height;
        // 缩放比例(取值大的)
        options.inSampleSize = (ratioWidth > ratioHeight) ? ratioWidth : ratioHeight;
        // 第二次采样：为了获取缩放后图片的所有像素点
        options.inJustDecodeBounds = false;
        Bitmap bitmap=BitmapFactory.decodeFile(path,options);
        return new BitmapDrawable(bitmap);
    }

    /**
     * 将图片文件转换成bitmap
     *
     * @param imgPath     图片文件路径
     * @param width       宽度
     * @param height      高度
     * @param isThumbnail 是否根据高宽生成缩略图
     * @return
     *//*


	public static Bitmap imgPathToBitmap(String imgPath, int width, int height, boolean isThumbnail) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = Tools.getBitmapOpt();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imgPath, options);
		options.inJustDecodeBounds = false;
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int be = 1;
		if (width > 0 && w > width) {
			be = (int) (w / width + 0.5);
		}
		if (height > 0 && h > height) {
			int be2 = (int) (h / height + 0.5);
			if (be2 > be)
				be = be2;
		}
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(imgPath, options);
		int degree = getPictureDegree(imgPath);
		// 如果有角度上的旋转,则纠正图片;
		if (degree != 0) {
			bitmap = correctImage(degree, bitmap);
		}
		// 生成固定尺寸的缩略图
		if (isThumbnail) {
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		}
		return bitmap;
	}
*/
    //网络图片下载
    public static Bitmap getImageFromNet(String url) {
        HttpURLConnection conn = null;
        try {
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("GET"); //设置请求方法
            conn.setConnectTimeout(10000); //设置连接服务器超时时间
            conn.setReadTimeout(5000);  //设置读取数据超时时间

            conn.connect(); //开始连接

            int responseCode = conn.getResponseCode(); //得到服务器的响应码
            if (responseCode == 200) {
                //访问成功
                InputStream is = conn.getInputStream(); //获得服务器返回的流数据
                Bitmap bitmap = BitmapFactory.decodeStream(is); //根据流数据 创建一个bitmap对象
                return bitmap;

            } else {
                //访问失败
                System.out.println("==============================访问失败===responseCode：" + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect(); //断开连接
            }
        }
        return null;
    }

    //网络图片下载
    public static Bitmap getNetImage(String url) {
        HttpURLConnection conn = null;
        try {
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("GET"); //设置请求方法
            conn.setConnectTimeout(10000); //设置连接服务器超时时间
            conn.setReadTimeout(5000);  //设置读取数据超时时间

            conn.connect(); //开始连接

            int responseCode = conn.getResponseCode(); //得到服务器的响应码
            if (responseCode == 200) {
                //访问成功
                InputStream is = conn.getInputStream(); //获得服务器返回的流数据
                Bitmap bitmap = BitmapFactory.decodeStream(is); //根据流数据 创建一个bitmap对象
//                Drawable drawable=new BitmapDrawable(bitmap);
                return bitmap;

            } else {
                //访问失败
                System.out.println("==============================访问失败===responseCode：" + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect(); //断开连接
            }
        }
        return null;
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Context context, Uri uri) throws Exception{
        InputStream input = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 32) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
