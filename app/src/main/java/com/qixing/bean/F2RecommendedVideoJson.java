package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class F2RecommendedVideoJson implements Serializable{

    private String result;
    private String message;

    //推荐视频
    private List<F2RecommendedVideoBean> tjlist;

    //免费体验课
    private List<F2RecommendedVideoBean> mflist;

    //会员专区
    private List<F2RecommendedVideoBean> hylist;

    //视频更多
    private List<F2RecommendedVideoBean> list;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<F2RecommendedVideoBean> getTjlist() {
        return tjlist;
    }

    public void setTjlist(List<F2RecommendedVideoBean> tjlist) {
        this.tjlist = tjlist;
    }

    public List<F2RecommendedVideoBean> getMflist() {
        return mflist;
    }

    public void setMflist(List<F2RecommendedVideoBean> mflist) {
        this.mflist = mflist;
    }

    public List<F2RecommendedVideoBean> getHylist() {
        return hylist;
    }

    public void setHylist(List<F2RecommendedVideoBean> hylist) {
        this.hylist = hylist;
    }

    public List<F2RecommendedVideoBean> getList() {
        return list;
    }

    public void setList(List<F2RecommendedVideoBean> list) {
        this.list = list;
    }
}
