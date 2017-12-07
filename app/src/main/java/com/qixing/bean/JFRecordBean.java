package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by nuo-rui on 2017/6/8.
 */

public class JFRecordBean implements Serializable {

    private String id;
    private String uid;
    private String fxid;
    private String type;
    private String jifen;
    private String is_jia;
    private String addtime;
    private String typename;
    private String title;


    public JFRecordBean(String title, String typename, String jifen, String addtime) {
        this.title = title;
        this.typename = typename;
        this.jifen = jifen;
        this.addtime = addtime;
    }

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

    public String getFxid() {
        return fxid;
    }

    public void setFxid(String fxid) {
        this.fxid = fxid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_jia() {
        return is_jia;
    }

    public void setIs_jia(String is_jia) {
        this.is_jia = is_jia;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "JFRecordBean{" +
                "title='" + title + '\'' +
                ", typename='" + typename + '\'' +
                ", jifen='" + jifen + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
