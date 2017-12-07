package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class SearchBean implements Serializable{

    private String id;//        "id": "4",
    private String v_pic;//        "v_pic": "/Public/Uploads/pic/2016-12-16/d5853a6b55472d.JPG",
    private String v_title;//        "v_title": "视频4",
    private String v_url;//        "v_url": "/Public/Uploads/video/xuanchuan.mp4",
    private String v_urltype;//        "v_urltype": "1",
    private String sp_nr;//        "sp_nr": "奔奔在奔",
    private String share_num;//        "share_num": "0",
    private String see_num;//        "see_num": "0",
    private String times;//        "times": "1481877173"


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

    public String getV_url() {
        return v_url;
    }

    public void setV_url(String v_url) {
        this.v_url = v_url;
    }

    public String getV_urltype() {
        return v_urltype;
    }

    public void setV_urltype(String v_urltype) {
        this.v_urltype = v_urltype;
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

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }
}
