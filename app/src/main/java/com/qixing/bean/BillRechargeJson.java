package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/10/24.
 */
public class BillRechargeJson implements Serializable {

    private String result;
    private String message;
    private List<BillRechargeBean> list;

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

    public List<BillRechargeBean> getList() {
        return list;
    }

    public void setList(List<BillRechargeBean> list) {
        this.list = list;
    }
}
