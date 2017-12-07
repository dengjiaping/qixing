package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/11/21.
 */
public class LiveGiftJson implements Serializable {

    private String result;
    private String message;

    private List<LiveGiftBean> list;

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

    public List<LiveGiftBean> getList() {
        return list;
    }

    public void setList(List<LiveGiftBean> list) {
        this.list = list;
    }
}
