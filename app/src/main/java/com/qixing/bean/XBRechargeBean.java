package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/10/20.
 */
public class XBRechargeBean implements Serializable {

    private String xb;
    private String price;
    private String select="0";

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
