package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class LoginBean implements Serializable{

    private String result;
    private String message;

    private String uid;//用户ID         "uid": "2",
    private String uname;//用户昵称           "uname": "18612523201",
    private String u_phone;//用户手机号        "u_phone": "18612523201",
    private String last_time;//上次登录时间        "last_time": "1481262608",
    private String txurl;//用户头像    "txurl": null,
    private String sex;//       "sex": "男",
    private String money;//        "money": "0.00",
    private String qianm;//        "qianm": null
    private String dengji;

    public String getDengji() {
        return dengji;
    }

    public void setDengji(String dengji) {
        this.dengji = dengji;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getDjpic() {
        return djpic;
    }

    public void setDjpic(String djpic) {
        this.djpic = djpic;
    }

    private String jifen;
    private String djpic;


    private List<UserInfo> lists;

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

    public String getU_phone() {
        return u_phone;
    }

    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
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

    public String getTxurl() {
        return txurl;
    }

    public void setTxurl(String txurl) {
        this.txurl = txurl;
    }

    public List<UserInfo> getLists() {
        return lists;
    }

    public void setLists(List<UserInfo> lists) {
        this.lists = lists;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getQianm() {
        return qianm;
    }

    public void setQianm(String qianm) {
        this.qianm = qianm;
    }

    public class UserInfo implements Serializable{
        private String id;//        "id": "56",
        private String pic;//        "pic": null,
        private String uname;//        "uname": "北风",
        private String nc;//        "nc": "",
        private String sex;//        "sex": "1",
        private String shengri;//        "shengri": null,
        private String sheng;//        "sheng": null,
        private String shi;//        "shi": null,
        private String qu;//        "qu": null,
        private String address;//        "address": null,
        private String password;//        "password": "96e79218965eb72c92a549dd5a330112",
        private String zfpwd;//        "zfpwd": "123456",
        private String realname;//        "realname": null,
        private String cardno;//        "cardno": null,
        private String u_tel;//        "u_tel": "18612523201",
        private String u_email;//        "u_email": null,
        private String surplus_money;//        "surplus_money": "0.00",
        private String sale_money;//        "sale_money": "0.00",
        private String frozen_money;//        "frozen_money": "0.00",
        private String fh_money;//        "fh_money": "0.00",
        private String commission_money;//        "commission_money": "0.000",
        private String jinbi;//        "jinbi": null,
        private String dj_jinbi;//        "dj_jinbi": null,
        private String bound_money;//        "bound_money": "0.00",
        private String tx_money;//        "tx_money": "0.00",
        private String finance_money;//        "finance_money": "0.00",
        private String frost_money;//        "frost_money": "0.00",
        private String last_time;//         "last_time": null,
        private String login_time;//        "login_time": null,
        private String res_time;//        "res_time": "1461571009",
        private String isshopper;//        "isshopper": "0",
        private String user_rank;//        "user_rank": "0",
        private String status;//        "status": "0"

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getNc() {
            return nc;
        }

        public void setNc(String nc) {
            this.nc = nc;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getShengri() {
            return shengri;
        }

        public void setShengri(String shengri) {
            this.shengri = shengri;
        }

        public String getSheng() {
            return sheng;
        }

        public void setSheng(String sheng) {
            this.sheng = sheng;
        }

        public String getShi() {
            return shi;
        }

        public void setShi(String shi) {
            this.shi = shi;
        }

        public String getQu() {
            return qu;
        }

        public void setQu(String qu) {
            this.qu = qu;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getCardno() {
            return cardno;
        }

        public void setCardno(String cardno) {
            this.cardno = cardno;
        }

        public String getU_tel() {
            return u_tel;
        }

        public void setU_tel(String u_tel) {
            this.u_tel = u_tel;
        }

        public String getU_email() {
            return u_email;
        }

        public void setU_email(String u_email) {
            this.u_email = u_email;
        }

        public String getSurplus_money() {
            return surplus_money;
        }

        public void setSurplus_money(String surplus_money) {
            this.surplus_money = surplus_money;
        }

        public String getSale_money() {
            return sale_money;
        }

        public void setSale_money(String sale_money) {
            this.sale_money = sale_money;
        }

        public String getFrozen_money() {
            return frozen_money;
        }

        public void setFrozen_money(String frozen_money) {
            this.frozen_money = frozen_money;
        }

        public String getFh_money() {
            return fh_money;
        }

        public void setFh_money(String fh_money) {
            this.fh_money = fh_money;
        }

        public String getCommission_money() {
            return commission_money;
        }

        public void setCommission_money(String commission_money) {
            this.commission_money = commission_money;
        }

        public String getJinbi() {
            return jinbi;
        }

        public void setJinbi(String jinbi) {
            this.jinbi = jinbi;
        }

        public String getDj_jinbi() {
            return dj_jinbi;
        }

        public void setDj_jinbi(String dj_jinbi) {
            this.dj_jinbi = dj_jinbi;
        }

        public String getBound_money() {
            return bound_money;
        }

        public void setBound_money(String bound_money) {
            this.bound_money = bound_money;
        }

        public String getTx_money() {
            return tx_money;
        }

        public void setTx_money(String tx_money) {
            this.tx_money = tx_money;
        }

        public String getFinance_money() {
            return finance_money;
        }

        public void setFinance_money(String finance_money) {
            this.finance_money = finance_money;
        }

        public String getFrost_money() {
            return frost_money;
        }

        public void setFrost_money(String frost_money) {
            this.frost_money = frost_money;
        }

        public String getLast_time() {
            return last_time;
        }

        public void setLast_time(String last_time) {
            this.last_time = last_time;
        }

        public String getLogin_time() {
            return login_time;
        }

        public void setLogin_time(String login_time) {
            this.login_time = login_time;
        }

        public String getRes_time() {
            return res_time;
        }

        public void setRes_time(String res_time) {
            this.res_time = res_time;
        }

        public String getIsshopper() {
            return isshopper;
        }

        public void setIsshopper(String isshopper) {
            this.isshopper = isshopper;
        }

        public String getUser_rank() {
            return user_rank;
        }

        public void setUser_rank(String user_rank) {
            this.user_rank = user_rank;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
