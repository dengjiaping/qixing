package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class SendGiftBean implements Serializable{

    private String result;//    "result":"1"
    private String message;//        "message":"成功！"
    private String doudou;//     "doudou":311

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

    public String getDoudou() {
        return doudou;
    }

    public void setDoudou(String doudou) {
        this.doudou = doudou;
    }
}
