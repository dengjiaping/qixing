package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class F2RecommendedVideoBean implements Serializable{

    private String name;
    private String context;
    private String num;

    //推荐视频
    private String id;//        "id": "1",
    private String v_pic;//        "v_pic": "/Public/Uploads/pic/2016-12-08/d5848fa2635344.jpg",
    private String v_title;//        "v_title": "视频1",
    private String sp_nr;//        "sp_nr": "奔奔在",
    private String see_num;//        "see_num": "0",
    private String times;//        "times": "1481176959"
    private String share_num;//        "see_num": "0",
    private String state;
    private String is_new;

    //免费体验课
//    private String id;//        "id": "2",
//    private String v_pic;//        "v_pic": "/Public/Uploads/pic/2016-12-08/d5848fa6a13014.jpg",
//    private String v_title;//        "v_title": "视频2",
    private String v_url;//        "v_url": "",
//    private String sp_nr;//        "sp_nr": "裁夺工",
//    private String see_num;//        "see_num": "0",
//    private String times;//        "times": "1481177706"

    //会员专区
//    private String id;//        "id": "1",
//    private String v_pic;//        "v_pic": "/Public/Uploads/pic/2016-12-08/d5848fa2635344.jpg",
//    private String v_title;//        "v_title": "视频1",
//    private String v_url;//        "v_url": "",
//    private String sp_nr;//        "sp_nr": "奔奔在",
//    private String see_num;//        "see_num": "0",
//    private String times;//        "times": "1481176959"


    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getV_pic() {
        return v_pic;
    }

    public void setV_pic(String v_pic) {
        this.v_pic = v_pic;
    }

    public String getV_title() {
        return v_title;
    }

    public void setV_title(String v_title) {
        this.v_title = v_title;
    }

    public String getSp_nr() {
        return sp_nr;
    }

    public void setSp_nr(String sp_nr) {
        this.sp_nr = sp_nr;
    }

    public String getSee_num() {
        return see_num;
    }

    public void setSee_num(String see_num) {
        this.see_num = see_num;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getV_url() {
        return v_url;
    }

    public void setV_url(String v_url) {
        this.v_url = v_url;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
