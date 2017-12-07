package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class WXAuthBean implements Serializable{


    private String access_token;//       "access_token": "g9MG8b5dkcjtOXTP2aAzutUr4rADegGl2akFi0Lhyzd-q8_MrDG0m7L9UYZXeAQwI7A4vO7kSLEOIFXaHdAd2Kw_F9MLQ8DSG4LYyp2LoXg",
    private String expires_in;//       "expires_in": 7200,
    private String refresh_token;//        "refresh_token": "4OXPbMY_fS0CqGg9oSUnCa2oYtP1gCE7mq79OZCQmE9ZzHqoUpfwXCWgGE7sibC3x9SLihFL1Lm_jkgrh47jkJ0mrCtRZCC-LN7yErM__Eg",
    private String openid;//        "openid": "oHXsPvzZhfVIgAAwwDz-xFLZY3Dw",
    private String scope;//        "scope": "snsapi_userinfo",
    private String unionid;//        "unionid": "o9673wNKK7SmrRsYs9MVGJia4UD4"

    private String errcode;//"errcode":40029,
    private String errmsg;//"errmsg":"invalid code"

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
