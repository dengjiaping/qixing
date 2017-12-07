package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class WXUserInfoBean implements Serializable{

    private String openid;//        "openid":"OPENID",
    private String nickname;//        "nickname":"NICKNAME",
    private String sex;//        "sex":1,
    private String province;//        "province":"PROVINCE",
    private String city;//        "city":"CITY",
    private String country;//        "country":"COUNTRY",
    private String headimgurl;//        "headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
//    private String privilege;//        "privilege":["PRIVILEGE1","PRIVILEGE2"],
    private String unionid;//        "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"

    private String errcode;//"errcode":40029,
    private String errmsg;//"errmsg":"invalid code"

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

//    public String getPrivilege() {
//        return privilege;
//    }
//
//    public void setPrivilege(String privilege) {
//        this.privilege = privilege;
//    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }
}
