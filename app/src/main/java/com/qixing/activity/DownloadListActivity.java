package com.qixing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qixing.R;
import com.qixing.adapter.BaseRecyclerViewAdapter;
import com.qixing.adapter.DownloadListAdapter;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.global.Constant;
import com.qixing.utlis.domain.MyBusinessInfo;
import com.qixing.view.titlebar.BGATitlebar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadListActivity extends BaseActivity implements BaseRecyclerViewAdapter.OnItemClickListener {

    private static final int REQUEST_DOWNLOAD_DETAIL_PAGE = 100;

    private RecyclerView rv;
    private BGATitlebar mTitlebar;
    private DownloadListAdapter downloadListAdapter;

    private String url;
    private String name;
    private String pic;

    private String fullName,fileMK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);
        initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

        url = getIntent().getStringExtra("url");
        pic = getIntent().getStringExtra("pic");
        name = getIntent().getStringExtra("name");

        fullName=url.substring(url.lastIndexOf(File.separator) + 1);
        fileMK=fullName.substring(fullName.indexOf("."));

        System.out.println("===========================下载队列 后缀名fileMK==========="+fileMK);
        System.out.println("===========================下载队列 文件名fileName==========="+fullName);
        MyApplication.getInstance().setDown_fileMk(fileMK);

        initView();
        initData();
//        initListener();
    }

    public void initListener() {
        downloadListAdapter.setOnItemClickListener(this);
    }

    public void initData() {
        downloadListAdapter = new DownloadListAdapter(this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(downloadListAdapter);

        downloadListAdapter.setData(getDownloadListData());
    }

    private List<MyBusinessInfo> getDownloadListData() {
        ArrayList<MyBusinessInfo> myBusinessInfos = new ArrayList<>();
        myBusinessInfos.add(new MyBusinessInfo(name, pic, url));
        return myBusinessInfos;
    }

    public void initView() {
        mTitlebar= (BGATitlebar) findViewById(R.id.mTitleBar);
        mTitlebar.setTitleText("下载队列");
        mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){
            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                AppManager.getAppManager().finishActivity();
            }
        });

        rv = (RecyclerView) findViewById(R.id.rv);
    }

    @Override
    public void onItemClick(int position) {
        MyBusinessInfo data = downloadListAdapter.getData(position);
        Intent intent = new Intent(this, DownloadDetailActivity.class);
        intent.putExtra(DownloadDetailActivity.DATA, data);
        startActivityForResult(intent, REQUEST_DOWNLOAD_DETAIL_PAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        downloadListAdapter.notifyDataSetChanged();
    }
}
