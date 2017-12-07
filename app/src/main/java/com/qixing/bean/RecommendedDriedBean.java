package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class RecommendedDriedBean implements Serializable{

    private String name;
    private String context;
    private String times;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }


    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
