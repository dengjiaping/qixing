package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class QXLiveInfoBean implements Serializable {
    private String id;//        "id": "5",
    private String pic;//        "pic": "/Public/Uploads/userpic/14703839375.png",
    private String uname;//        "uname": "hd1111"

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
