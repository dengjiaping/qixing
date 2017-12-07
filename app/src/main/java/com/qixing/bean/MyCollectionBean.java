package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class MyCollectionBean implements Serializable{


    private String id;//      "id": "3",
    private String user_id;//        "user_id": "2",
    private String spid;//        "spid": "1",
    private String times;//        "times": "",
    private String v_pic;//        "v_pic": "/Public/Uploads/pic/2016-12-08/d5848fa2635344.jpg",
    private String v_title;//        "v_title": "视频1",
    private String v_url;//        "v_url": "",
    private String see_num;//        "see_num": "0",
    private String share_num;//        "Share_num": "0",
    private String scid;//        "scid": "3"

    /**
     * 测试
     * */
    private String img;//
    private String title;//
    private String context;//用户手机号
    private String num;//上次登录时间


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
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

    public String getV_url() {
        return v_url;
    }

    public void setV_url(String v_url) {
        this.v_url = v_url;
    }

    public String getSee_num() {
        return see_num;
    }

    public void setSee_num(String see_num) {
        this.see_num = see_num;
    }

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }
}
