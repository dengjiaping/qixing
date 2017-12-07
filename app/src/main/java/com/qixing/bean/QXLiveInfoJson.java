package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class QXLiveInfoJson implements Serializable{

    private String result;
    private String message;
    private Zbinfo zbinfo;
    private List<QXLiveInfoBean> userlist;

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

    public List<QXLiveInfoBean> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<QXLiveInfoBean> userlist) {
        this.userlist = userlist;
    }

    public Zbinfo getZbinfo() {
        return zbinfo;
    }

    public void setZbinfo(Zbinfo zbinfo) {
        this.zbinfo = zbinfo;
    }

    public class Zbinfo implements Serializable{
        private String id;//        "id": "17",
        private String uid;//        "uid": "38",
        private String zb_pic;//        "zb_pic": "/Public/Uploads/zhibopic/147790541738.png",
        private String zb_title;//        "zb_title": "测试",
        private String see_num;//        "see_num": "1",
        private String times;//        "times": "20161031",
        private String status;//        "status": "1",
        private String zbno;//        "zbno": "201610313817"
        private String zbuname;

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

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getZbno() {
            return zbno;
        }

        public void setZbno(String zbno) {
            this.zbno = zbno;
        }

        public String getZbuname() {
            return zbuname;
        }

        public void setZbuname(String zbuname) {
            this.zbuname = zbuname;
        }
    }
}
