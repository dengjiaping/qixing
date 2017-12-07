package com.qixing.utlis.images;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.qixing.R;

public class ImageLoaderOptions {

	public static DisplayImageOptions getOptions(){
		DisplayImageOptions options;
		
		 options=new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.icon_loading)
		.showImageOnFail(R.drawable.icon_loading)
		.showImageOnLoading(R.drawable.icon_loading)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		 
		 return options;		
	}
	
	public static DisplayImageOptions get_gushi_Options() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}
	
	public static DisplayImageOptions get_face_Options() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.img_head_default)
				.showImageOnFail(R.drawable.img_head_default)
				.showImageOnLoading(R.drawable.img_head_default)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}

	public static DisplayImageOptions getBannerOptions(){
		DisplayImageOptions options;

		options=new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.empty_icon)
				.showImageOnFail(R.drawable.empty_icon)
				.showImageOnLoading(R.drawable.empty_icon)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();


		return options;
	}
}
