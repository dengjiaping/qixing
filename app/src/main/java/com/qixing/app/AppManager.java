package com.qixing.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.Stack;

public class AppManager {
	private static Stack<BaseActivity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 单实例 , UI无需考虑多线程同步问题
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到栈
	 */
	public void addActivity(BaseActivity activity) {
		if (activityStack == null) {
			activityStack = new Stack<BaseActivity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（栈顶Activity）
	 */
	public BaseActivity currentActivity() {
		if (activityStack == null || activityStack.isEmpty()) {
			return null;
		}
		BaseActivity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 获取当前Activity（栈顶Activity） 没有找到则返回null
	 */
	public BaseActivity findActivity(Class<?> cls) {
		BaseActivity activity = null;
		for (BaseActivity aty : activityStack) {
			if (aty.getClass().equals(cls)) {
				activity = aty;
				break;
			}
		}
		return activity;
	}

	/**
	 * 结束当前Activity（栈顶Activity）
	 */
	public void finishActivity() {
		BaseActivity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity(重载)
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定的Activity(重载)
	 */
	public void finishActivity(Class<?> cls) {
		for (BaseActivity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
	 *
	 * @param cls
	 */
	public void finishOthersActivity(Class<?> cls) {
		for (BaseActivity activity : activityStack) {
			if (!(activity.getClass().equals(cls))) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 应用程序退出
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.killBackgroundProcesses(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
			System.exit(0);
		}
	}

	/**
	 * 退出Activity
	 *
	 * @param activity
	 *            Activity
	 */
	public void popActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
		}
	}

	/**
	 * 将当前Activity推入栈中
	 *
	 * @param activity
	 *            Activity
	 */
	public void pushActivity(BaseActivity activity) {
		activityStack.add(activity);
	}

	/**
	 * 退出栈中其他所有Activity
	 *
	 * @param cls
	 *            Class 类名
	 */
	@SuppressWarnings("rawtypes")
	public void popOtherActivity(Class cls) {
		if (null == cls) {

			return;
		}

		for (Activity activity : activityStack) {
			if (null == activity || activity.getClass().equals(cls)) {
				continue;
			}

			activity.finish();
		}

	}

	/**
	 * 退出栈中所有Activity
	 *
	 */
	public void popAllActivity() {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			activity.finish();
			popActivity(activity);
		}

	}

	/**
	 * 跳转到指定activity
	 *
	 * @param activity
	 */
	public void startNextActivity(Class<?> activity) {
		Activity curActivity = currentActivity();
		Intent intent = new Intent(curActivity, activity);
		curActivity.startActivity(intent);

		//动画
//		curActivity.overridePendingTransition(R.anim.anim_push_right_in,
//				R.anim.anim_push_left_out);
	}



	public void startNextActivity(Context mContext,Class<?> activity){
		Activity curActivity= (Activity) mContext;
		Intent intent = new Intent(curActivity, activity);
		curActivity.startActivity(intent);
	}

	public void startActivityForResult(Class<?> activity,int requestCode){

		Activity curActivity = currentActivity();
	 Intent intent = new Intent(curActivity, activity);
		curActivity.startActivityForResult(intent, requestCode);

	}
	public void startActivityForResult(Class<?> activity,int requestCode,Intent intent){

		Activity curActivity = currentActivity();
		intent = new Intent(curActivity, activity);
		curActivity.startActivityForResult(intent,requestCode);

	}
	/**
	 * 携带inetnt参数
	 * @param activity
	 */
	public void startNextActivity(Class<?> activity,Intent intent) {
		Activity curActivity = currentActivity();
		intent.setClass(curActivity, activity);
		curActivity.startActivity(intent);

		//动画
//		curActivity.overridePendingTransition(R.anim.anim_push_right_in,
//				R.anim.anim_push_left_out);
	}

	/**
	 * 携带inetnt参数
	 * @param activity
	 */
	public void startNextActivity(Context context,Class<?> activity,Intent intent) {
		intent.setClass(context, activity);
		context.startActivity(intent);
		//动画
//		curActivity.overridePendingTransition(R.anim.anim_push_right_in,
//				R.anim.anim_push_left_out);
	}


	public void startFragmentNextActivity(Context context,Class<?> activity){

		Intent intent = new Intent(context, activity);
		context.startActivity(intent);
	}
	/**
	 * fragment跳转activity
	 * @param activity
	 */
	public void startFragmentNextActivity(Context context,Class<?> activity,Intent intent){

		intent.setClass(context, activity);
		context.startActivity(intent);
	}
}
