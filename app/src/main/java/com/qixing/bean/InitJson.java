package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class InitJson implements Serializable{
    private String result;//
    private String message;//
    private String doudou;//"doudou":"119"

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
