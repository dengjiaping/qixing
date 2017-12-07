package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class SeeQXliveRankBean implements Serializable{

    /**
     * 测试
     * */
    private String uid;//"uid": "58",
    private String jsuid;//"jsuid": "183",
    private String zdou;//"zdou": "443",
    private String uname;//"uname": null,
    private String pic;//"pic": null

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJsuid() {
        return jsuid;
    }

    public void setJsuid(String jsuid) {
        this.jsuid = jsuid;
    }

    public String getZdou() {
        return zdou;
    }

    public void setZdou(String zdou) {
        this.zdou = zdou;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
