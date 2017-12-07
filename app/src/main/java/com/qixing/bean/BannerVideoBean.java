package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class BannerVideoBean implements Serializable{

    private String result;
    private String message;
    private String isgz;

    private Spinfo spinfo;

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

    public String getIsgz() {
        return isgz;
    }

    public void setIsgz(String isgz) {
        this.isgz = isgz;
    }

    public Spinfo getSpinfo() {
        return spinfo;
    }

    public void setSpinfo(Spinfo spinfo) {
        this.spinfo = spinfo;
    }

    public class Spinfo implements Serializable{

        private String id;//        "id": "2",
        private String v_pic;//        "v_pic": "/Public/Uploads/pic/2016-12-14/d58512363b4cd5.jpg",
        private String v_title;//        "v_title": "视频2",
        private String sp_nr;//        "sp_nr": "裁夺工",
        private String v_url;//        "v_url": "/Public/Uploads/video/xuanchuan.mp4",
        private String v_urltype;//        "v_urltype": "1",
        private String v_type;//        "v_type": "1",
        private String v_istj;//        "v_istj": "2",
        private String v_conten;//        "v_conten": "<p>发光飞碟广东省广发广东分公司<br /></p>",
        private String see_num;//        "see_num": "16",
        private String share_num;//        "share_num": "16",
        private String times;//        "times": "1481712484"

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

        public String getV_urltype() {
            return v_urltype;
        }

        public void setV_urltype(String v_urltype) {
            this.v_urltype = v_urltype;
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

        public String getV_conten() {
            return v_conten;
        }

        public void setV_conten(String v_conten) {
            this.v_conten = v_conten;
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
}
