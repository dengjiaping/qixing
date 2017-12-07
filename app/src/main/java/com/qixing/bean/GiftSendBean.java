package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/12/4.
 */
public class GiftSendBean implements Serializable {

    private String uid;
    private String type;
    private String times;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
