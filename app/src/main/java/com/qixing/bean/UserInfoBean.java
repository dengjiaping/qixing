package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class UserInfoBean implements Serializable{

    private String result;//        "result": "1",
    private String message;//        "message": "登陆成功",
    private String uid;//        "uid": "34",
    private String uname;//        "uname": "Bitter",
    private String u_phone;//        "u_phone": null,
    private String last_time;//        "last_time": null,
    private String txurl;//        "txurl": "/Public/Uploads/userpic/1472787925oHXsPvzZhfVIgAAwwDz-xFLZY3Dw.png"

    private String list;

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

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getU_phone() {
        return u_phone;
    }

    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public String getTxurl() {
        return txurl;
    }

    public void setTxurl(String txurl) {
        this.txurl = txurl;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }
}
