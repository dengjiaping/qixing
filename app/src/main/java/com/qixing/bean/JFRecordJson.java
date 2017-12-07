package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nuo-rui on 2017/6/8.
 */

public class JFRecordJson implements Serializable{

    private String result;
    private String message;
    private String jifen;
    private String bili;
    private List<JFRecordBean> list;

    public JFRecordJson(String result, String jifen, String bili, List<JFRecordBean> list, String message) {
        this.result = result;
        this.jifen = jifen;
        this.bili = bili;
        this.list = list;
        this.message = message;
    }

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

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getBili() {
        return bili;
    }

    public void setBili(String bili) {
        this.bili = bili;
    }

    public List<JFRecordBean> getList() {
        return list;
    }

    public void setList(List<JFRecordBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "JFRecordJson{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", jifen='" + jifen + '\'' +
                ", bili='" + bili + '\'' +
                ", list=" + list +
                '}';
    }
}
