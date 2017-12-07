package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class HistoryBean implements Serializable{

    /**
     * 视频
     * */
    private String id;//       "id": "3",
    private String v_pic;//        "v_pic": "/Public/Uploads/pic/2016-12-08/d5848fa6a13014.jpg",
    private String v_title;//        "v_title": "视频3",
    private String sp_nr;//        "sp_nr": "裁夺工",
    private String v_url;//        "v_url": "",
    private String v_type;//        "v_type": "1",
    private String v_istj;//        "v_istj": "1",
    private String see_num;//        "see_num": "0",
    private String share_num;//        "share_num": "0",
    private String times;//        "times": "1481177706"

    /**
     * 直播
     * */
    private String uid;//        "uid": "1",
    private String zb_pic;//        "zb_pic": "\/Public\/Uploads\/pic\/2016-12-08\/d5848fa2635344.jpg",
    private String zb_title;//        "zb_title": "直播1",
    private String istj;//        "istj": "1",
    private String status;//        "status": "1",
    private String pic;//        "pic": "\/Public\/Uploads\/userpic\/14813594841.png",
    private String uname;//        "uname": "123"
//    private String times;//        "times": "1481177706"
//    private String see_num;//        "see_num": "0",
    private String app_types;//  app_types 1手机  2摄像机

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

    public String getV_url() {
        return v_url;
    }

    public void setV_url(String v_url) {
        this.v_url = v_url;
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }

    public String getV_istj() {
        return v_istj;
    }

    public void setV_istj(String v_istj) {
        this.v_istj = v_istj;
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

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }
}
