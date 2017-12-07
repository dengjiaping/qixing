package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/10/24.
 */
public class XBConsumeJson implements Serializable {

    private String result;
    private String message;

    private List<XBConsumeBean> list;

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

    public List<XBConsumeBean> getList() {
        return list;
    }

    public void setList(List<XBConsumeBean> list) {
        this.list = list;
    }
}
