package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class SeeQXliveBean implements Serializable{


    private String id;//       "id": "2",
    private String uid;//        "uid": "1",
    private String zb_pic;//        "zb_pic": "/Public/Uploads/pic/2016-12-08/d5848fa2635344.jpg",
    private String zb_title;//        "zb_title": "直播1",
    private String see_num;//        "see_num": "5",
    private String istj;//        "istj": "1",
    private String status;//        "status": "1",
    private String times;//        "times": "0",
    private String pic;//        "pic": "/Public/Uploads/userpic/14813594841.png",
    private String uname;//        "uname": "123"
    private String app_types;//  app_types 1手机  2摄像机


    /**
     * 测试
     * */
//    private String id;
    private String img;//
    private String title;//
    private String context;//
    private String num;//
    private String state;//
//    private String times;


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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getZb_pic() {
        return zb_pic;
    }

    public void setZb_pic(String zb_pic) {
        this.zb_pic = zb_pic;
    }

    public String getZb_title() {
        return zb_title;
    }

    public void setZb_title(String zb_title) {
        this.zb_title = zb_title;
    }

    public String getSee_num() {
        return see_num;
    }

    public void setSee_num(String see_num) {
        this.see_num = see_num;
    }

    public String getIstj() {
        return istj;
    }

    public void setIstj(String istj) {
        this.istj = istj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getApp_types() {
        return app_types;
    }

    public void setApp_types(String app_types) {
        this.app_types = app_types;
    }
}
