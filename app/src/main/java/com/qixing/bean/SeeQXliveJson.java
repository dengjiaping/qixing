package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class SeeQXliveJson implements Serializable{

    private String result;
    private String message;

    private List<SeeQXliveBean> list;

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

    public List<SeeQXliveBean> getList() {
        return list;
    }

    public void setList(List<SeeQXliveBean> list) {
        this.list = list;
    }
}
