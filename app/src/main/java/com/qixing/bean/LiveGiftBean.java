package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/11/21.
 */
public class LiveGiftBean implements Serializable {

    private String id;
    private String uid;
    private String zbid;
    private String num;
    private String uname;
    private String pic;
    private String alljb;
    private String types;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getZbid() {
        return zbid;
    }

    public void setZbid(String zbid) {
        this.zbid = zbid;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAlljb() {
        return alljb;
    }

    public void setAlljb(String alljb) {
        this.alljb = alljb;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
