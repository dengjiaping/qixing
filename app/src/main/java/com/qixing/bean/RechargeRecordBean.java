package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by nuo-rui on 2017/6/8.
 */

public class RechargeRecordBean implements Serializable {

    private String ordno;//订单号
    private String contents;//描述
    private String money;//金额
    private String method;//支付方式
    private String status;//状态
    private String times;//时间

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
