package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/10/13.
 */
public class VIPJson implements Serializable {

    private String result;
    private String message;

    private String wzinfo;

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

    public String getWzinfo() {
        return wzinfo;
    }

    public void setWzinfo(String wzinfo) {
        this.wzinfo = wzinfo;
    }
}
