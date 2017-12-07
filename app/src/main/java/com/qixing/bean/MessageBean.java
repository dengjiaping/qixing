package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class MessageBean implements Serializable {
    private String id;//        "id": "155",
    private String uid;//        "uid": "4520",
    private String state;//        "state": "1",
    private String content;//        "content": "亲爱的用户，您的订单：2016110815002645207822,支付完成。",
    private String addtime;//        "addtime": "1478588435"
    private String url;//             "url":     "index.php\/Index\/selxx\/id191"

    private String name;//        "name": "申请成功",
    private String cat;//        "cat": "2",
    private String times;//        "times": "1483439501",
    private String ms;//            "ms":"销售人员在客户介绍上犯的十大错误，看自己是否中招！"
    private String timess;//        "timess": "2017-01-03"

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getTimess() {
        return timess;
    }

    public void setTimess(String timess) {
        this.timess = timess;
    }
}
