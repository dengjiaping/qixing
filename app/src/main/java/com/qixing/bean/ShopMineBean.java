package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class ShopMineBean implements Serializable{

    private String result;
    private String message;

    private UserInfo info;//

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

    public UserInfo getInfo() {
        return info;
    }

    public void setInfo(UserInfo info) {
        this.info = info;
    }

    public class UserInfo implements Serializable {

        private String id;//         "id": "2",
        private String promotionid;//        "promotionid": "701",
        private String pic;//        "pic": "/Public/Uploads/userpic/14813673802.png",
        private String uname;//        "uname": "七",
        private String qianm;//        "qianm": "",
        private String dengji;//        "dengji": "1",
        private String sex;//        "sex": "1",
        private String password;//        "password": "96e79218965eb72c92a549dd5a330112",
        private String zfpwd;//        "zfpwd": "670b14728ad9902aecba32e22fa4f6bd",
        private String u_phone;//        "u_phone": "18612523201",
        private String u_email;//        "u_email": null,
        private String money;//        "money": "990.00",    账户余额
        private String dj_money;//        "dj_money": "10.00",  冻结金额
        private String fx_money;//        "fx_money": null,   累计返利
        private String agent;//        "agent": "980.00",  分销返利
        private String last_time;//        "last_time": "1481506737",
        private String tuijianren;//        "tuijianren": null
        private String kf;//        "kf": "18600333877"
        private String jifen;

        public String getJifen() {
            return jifen;
        }

        public void setJifen(String jifen) {
            this.jifen = jifen;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPromotionid() {
            return promotionid;
        }

        public void setPromotionid(String promotionid) {
            this.promotionid = promotionid;
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

        public String getQianm() {
            return qianm;
        }

        public void setQianm(String qianm) {
            this.qianm = qianm;
        }

        public String getDengji() {
            return dengji;
        }

        public void setDengji(String dengji) {
            this.dengji = dengji;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getZfpwd() {
            return zfpwd;
        }

        public void setZfpwd(String zfpwd) {
            this.zfpwd = zfpwd;
        }

        public String getU_phone() {
            return u_phone;
        }

        public void setU_phone(String u_phone) {
            this.u_phone = u_phone;
        }

        public String getU_email() {
            return u_email;
        }

        public void setU_email(String u_email) {
            this.u_email = u_email;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getDj_money() {
            return dj_money;
        }

        public void setDj_money(String dj_money) {
            this.dj_money = dj_money;
        }

        public String getFx_money() {
            return fx_money;
        }

        public void setFx_money(String fx_money) {
            this.fx_money = fx_money;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        public String getLast_time() {
            return last_time;
        }

        public void setLast_time(String last_time) {
            this.last_time = last_time;
        }

        public String getTuijianren() {
            return tuijianren;
        }

        public void setTuijianren(String tuijianren) {
            this.tuijianren = tuijianren;
        }

        public String getKf() {
            return kf;
        }

        public void setKf(String kf) {
            this.kf = kf;
        }
    }


}
