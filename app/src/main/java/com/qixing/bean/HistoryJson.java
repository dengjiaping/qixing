package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class HistoryJson implements Serializable{

    private String result;
    private String message;

    private List<HistoryBean> list;

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

    public List<HistoryBean> getList() {
        return list;
    }

    public void setList(List<HistoryBean> list) {
        this.list = list;
    }
}
