package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class GiftBean implements Serializable{
    private String num;//    "num": 60,
    private String money;//        "money": 6
    private String select = "0";
    private int imgID;
    private String xingbi;
    private String msg;
    private String types;
    private String name;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public String getXingbi() {
        return xingbi;
    }

    public void setXingbi(String xingbi) {
        this.xingbi = xingbi;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
