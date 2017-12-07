package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class PayMethodBean implements Serializable{

    private String result;
    private String message;


    private String ddh;//订单编号：ddh
    private String sjprice;//订单实际价格：sjprice
    private String url;//回调函数url：url
    private String title;//订单标题：title
    private String discription;//订单描述：discription


    /**
     * 充值
     * */
    private String id;//    订单编号    "id": "20",
//    private String title;//    订单标题：    "title": "钱包充值",
//    private String discription;//        "discription": "钱包充值:1",
    private String money;//        "money": "1",
//    private String url;//        "url": "http://qxzb1612.wicep.net:999/index.php/UserApi/czhf.html"

    /**
     * 报名
     * */
    private String totalprice;//       "totalprice": 1980,
    private String orderno;//        "orderno": "20161214094216252031",
//    private String title;//        "title": "七星直播",
//    private String discription;//        "discription": "七星直播会员报名",
//    private String url;//        "url": "http://qxzb1612.wicep.net:999/index.php/UserApi/dengjiurl.html"


    public String getDoudou() {
        return doudou;
    }

    public void setDoudou(String doudou) {
        this.doudou = doudou;
    }

    /**
     * 送礼物充值
     * */
//    private String id;//       "id": "27",
//    private String title;//        "title": "七星时代",
//    private String discription;//        "discription": "七星时代:6",
//    private String money;//        "money": "6",
    private String doudou;//        "num": "60",
//    private String url;//        "url": "http://app.qixingshidai.com/index.php/UserApi/czhf_dou.html"


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
}
