package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class MyRebateJson implements Serializable{

    private String result;
    private String message;

    private Userdat userdat;//
    private List<Jxs> onejxs;//    "onejxs": null,
    private List<Jxs> twjxs;//    "twjxs": null,
    private String wz;//    "wz": "报名介绍"

    private List<MyRebateBean> list;

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

    public List<MyRebateBean> getList() {
        return list;
    }

    public void setList(List<MyRebateBean> list) {
        this.list = list;
    }

    public Userdat getUserdat() {
        return userdat;
    }

    public void setUserdat(Userdat userdat) {
        this.userdat = userdat;
    }

    public List<Jxs> getOnejxs() {
        return onejxs;
    }

    public void setOnejxs(List<Jxs> onejxs) {
        this.onejxs = onejxs;
    }

    public List<Jxs> getTwjxs() {
        return twjxs;
    }

    public void setTwjxs(List<Jxs> twjxs) {
        this.twjxs = twjxs;
    }

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
    }

    public class Userdat implements Serializable{
        private String uname;//       "uname": "七",
        private String pic;//         "pic": "/Public/Uploads/userpic/14813673802.png",
        private String sy_money;//         "sy_money": "980.00",
        private String lj_money;//         "lj_money": null,
        private String tjid;//         "tjid": "701",
        private String tjnum;//        "tjnum": 0
        private String dengji;

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

        public String getSy_money() {
            return sy_money;
        }

        public void setSy_money(String sy_money) {
            this.sy_money = sy_money;
        }

        public String getLj_money() {
            return lj_money;
        }

        public void setLj_money(String lj_money) {
            this.lj_money = lj_money;
        }

        public String getTjid() {
            return tjid;
        }

        public void setTjid(String tjid) {
            this.tjid = tjid;
        }

        public String getTjnum() {
            return tjnum;
        }

        public void setTjnum(String tjnum) {
            this.tjnum = tjnum;
        }

        public String getDengji() {
            return dengji;
        }

        public void setDengji(String dengji) {
            this.dengji = dengji;
        }
    }

    public class Onejxs implements Serializable{
        private String uname;//        "uname": "123",
        private String dj;//        "dj": "普通会员",
        private String times;//        "times": "2016-12-09",
        private String tjnum;//        "tjnum": 1

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getDj() {
            return dj;
        }

        public void setDj(String dj) {
            this.dj = dj;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getTjnum() {
            return tjnum;
        }

        public void setTjnum(String tjnum) {
            this.tjnum = tjnum;
        }
    }

    public class Twjxs implements Serializable{

        private String uname;//        "uname": "七",
        private String dj;//        "dj": "普通会员",
        private String times;//        "times": "2016-12-09",
        private String tjnum;//        "tjnum": 1

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getDj() {
            return dj;
        }

        public void setDj(String dj) {
            this.dj = dj;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getTjnum() {
            return tjnum;
        }

        public void setTjnum(String tjnum) {
            this.tjnum = tjnum;
        }
    }

    public class Jxs implements Serializable{

        private String uname;//        "uname": "七",
        private String dj;//        "dj": "普通会员",
        private String times;//        "times": "2016-12-09",
        private String tjnum;//        "tjnum": 1

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getDj() {
            return dj;
        }

        public void setDj(String dj) {
            this.dj = dj;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getTjnum() {
            return tjnum;
        }

        public void setTjnum(String tjnum) {
            this.tjnum = tjnum;
        }
    }
}
