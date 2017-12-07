package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class VersionResultBean implements Serializable{

    private String result;
    private String message;

    private String uid;//用户ID
    private String u_tel;//用户手机号
    private String uname;//用户昵称
    private String last_time;//上次登录时间

    private String user_money;//剩余佣金

    private String duigou;

    private String imgs;
    private String tx;
    private String imgs1;

    private String ddh;//订单编号：ddh
    private String sjprice;//订单实际价格：sjprice
    private String url;//回调函数url：url
    private String title;//订单标题：title
    private String discription;//订单描述：discription


    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getImgs1() {
        return imgs1;
    }

    public void setImgs1(String imgs1) {
        this.imgs1 = imgs1;
    }

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getU_tel() {
        return u_tel;
    }

    public void setU_tel(String u_tel) {
        this.u_tel = u_tel;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public String getUser_money() {
        return user_money;
    }

    public void setUser_money(String user_money) {
        this.user_money = user_money;
    }

    public String getDuigou() {
        return duigou;
    }

    public void setDuigou(String duigou) {
        this.duigou = duigou;
    }

    public String getDdh() {
        return ddh;
    }

    public void setDdh(String ddh) {
        this.ddh = ddh;
    }

    public String getSjprice() {
        return sjprice;
    }

    public void setSjprice(String sjprice) {
        this.sjprice = sjprice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
