package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nuo-rui on 2017/6/8.
 */

public class RechargeRecordJson implements Serializable {

    private String result;
    private List<RechargeRecordBean> list;
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<RechargeRecordBean> getList() {
        return list;
    }

    public void setList(List<RechargeRecordBean> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
