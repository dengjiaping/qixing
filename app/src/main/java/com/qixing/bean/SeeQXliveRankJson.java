package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class SeeQXliveRankJson implements Serializable{

    private String result;
    private String message;

    private List<SeeQXliveRankBean> paiming;

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

    public List<SeeQXliveRankBean> getPaiming() {
        return paiming;
    }

    public void setPaiming(List<SeeQXliveRankBean> paiming) {
        this.paiming = paiming;
    }
}
