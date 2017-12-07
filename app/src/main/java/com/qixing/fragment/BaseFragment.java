package com.qixing.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.qixing.R;
import com.qixing.app.MyApplication;


/**
 * Created by SunQi on 2015/9/2.
 */
public abstract  class BaseFragment extends Fragment {


    protected  String TAG;

    protected View mContentView;

    public AsyncHttpClient mAsyncHttpClient;
    public ProgressDialog  mProgressDialog;
    public static  AlertDialog mAlertDialog;

//    protected OnShowLeftMenuListener  mListenerShowLeftMenu;
//
//    public interface  OnShowLeftMenuListener{
//
//        void show(int position);
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        TAG=this.getClass().getSimpleName();
        mProgressDialog=new ProgressDialog(activity);

        mProgressDialog.setMessage("加载中....");

        mAsyncHttpClient=new AsyncHttpClient();

//        mListenerShowLeftMenu= (OnShowLeftMenuListener) activity;


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){

            onUserVisible();
        }
    }

    /**
     * 设置View
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 业务逻辑
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**
     * fragment可见的时候，处理网络数据
     */
    protected abstract void onUserVisible();
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) mContentView.findViewById(id);
    }
    protected void setContentView(@LayoutRes int layoutResID) {
        mContentView = LayoutInflater.from(MyApplication.getInstance()).inflate(layoutResID, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(mContentView==null){

            initView(savedInstanceState);
            setListener();
            processLogic(savedInstanceState);


        }else {

            ViewGroup parent= (ViewGroup) mContentView.getParent();
            if(parent!=null){

                parent.removeView(mContentView);
            }


        }


        return  mContentView;
    }

    public static void showErrorDialog(Context mContext){

        mAlertDialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.dialog_prompt)
                .setMessage(R.string.dialog_wrong)
                .setPositiveButton(R.string.dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                mAlertDialog.dismiss();
//                                        finish();
                            }
                        }).show();
    }

    public static void showTimeoutDialog(Context mContext){
        mAlertDialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.dialog_prompt)
                .setMessage(R.string.dialog_timeout)
                .setPositiveButton(R.string.dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                mAlertDialog.dismiss();
//                                        finish();
                            }
                        }).show();
    }

}
