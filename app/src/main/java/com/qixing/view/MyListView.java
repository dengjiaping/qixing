package com.qixing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView extends ListView {
	private static final String TAG = "MyListView";

	private Context context;
	
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/**
	 * 方法描述：解决ListView item高度显示问题
	 * */
	@Override  
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	            MeasureSpec.AT_MOST);  
	    super.onMeasure(widthMeasureSpec, expandSpec);  
	} 
}
