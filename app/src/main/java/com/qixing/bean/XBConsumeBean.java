package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/10/24.
 */
public class XBConsumeBean implements Serializable {

    private String contents;
    private String num;
    private String types;
    private String status;
    private String times;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
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
