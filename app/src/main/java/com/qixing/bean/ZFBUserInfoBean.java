package com.qixing.bean;

import java.io.Serializable;

/**
 * Created by wicep on 2015/12/25.
 */
public class ZFBUserInfoBean implements Serializable{


    private UserInfoShare alipay_user_userinfo_share_response;
    private UserInfoShare alipay_user_info_share_response;
    private String sign;//    "sign": "kDP5qfJ2mAjk0ZIKc7aL4Lm4Mn4RTPKYwk9xGnaBHOGBwvBWeN4jznrweTjpog/32mJuAvgyIgW2fiAfzEGddpE5IIVO6/Qny+qvtZV12QkpZrC2Wrz5Mz170/PCrsNckhOcPn4BTCKw2VdxtHR2jxSKl4gtKhEymUh5yTicfkA="

    public UserInfoShare getAlipay_user_userinfo_share_response() {
        return alipay_user_userinfo_share_response;
    }

    public void setAlipay_user_userinfo_share_response(UserInfoShare alipay_user_userinfo_share_response) {
        this.alipay_user_userinfo_share_response = alipay_user_userinfo_share_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public UserInfoShare getAlipay_user_info_share_response() {
        return alipay_user_info_share_response;
    }

    public void setAlipay_user_info_share_response(UserInfoShare alipay_user_info_share_response) {
        this.alipay_user_info_share_response = alipay_user_info_share_response;
    }

    public class UserInfoShare implements Serializable{
        private String code;//     "code": "10000",
        private String msg;//        "msg": "Success",
//        private String avatar;//        "avatar": "https://tfs.alipayobjects.com/images/partner/T1evBwXepXXXXXXXXX",
//        private String city;//        "city": "北京市",
//        private String gender;//        "gender": "m",
//        private String is_certified;//         "is_certified": "T",
//        private String is_student_certified;//        "is_student_certified": "F",
//        private String nick_name;//        "nick_name": "C",
//        private String province;//        "province": "北京",
//        private String user_id;//        "user_id": "2088602154370190",
//        private String user_status;//        "user_status": "T",
        private String user_type;//        "user_type": "2"

        private String user_type_value;//     "user_type_value": "2",
        private String is_licence_auth;//      "is_licence_auth": "F",
        private String is_certified;//     "is_certified": "T",
        private String is_certify_grade_a;//      "is_certify_grade_a": "T",
        private String avatar;//      "avatar": "https://tfsimg.alipay.com/images/partner/T1ejtcXhXIXXXXXXXX",
        private String city;//     "city": "北京市",
        private String is_student_certified;//     "is_student_certified": "F",
        private String area;//       "area": "西城区",
        private String is_bank_auth;//       "is_bank_auth": "T",
        private String is_mobile_auth;//       "is_mobile_auth": "T",
        private String nick_name;//        "nick_name": "崔",
        private String alipay_user_id;//       "alipay_user_id": "2088602154370190",
        private String user_id;//      "user_id": "20880072593677144333703431910119",
        private String province;//      "province": "北京",
        private String user_status;//      "user_status": "T",
        private String gender;//      "gender": "m",
        private String is_id_auth;//      "is_id_auth": "T"

        public String getUser_type_value() {
            return user_type_value;
        }

        public void setUser_type_value(String user_type_value) {
            this.user_type_value = user_type_value;
        }

        public String getIs_licence_auth() {
            return is_licence_auth;
        }

        public void setIs_licence_auth(String is_licence_auth) {
            this.is_licence_auth = is_licence_auth;
        }

        public String getIs_certified() {
            return is_certified;
        }

        public void setIs_certified(String is_certified) {
            this.is_certified = is_certified;
        }

        public String getIs_certify_grade_a() {
            return is_certify_grade_a;
        }

        public void setIs_certify_grade_a(String is_certify_grade_a) {
            this.is_certify_grade_a = is_certify_grade_a;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getIs_student_certified() {
            return is_student_certified;
        }

        public void setIs_student_certified(String is_student_certified) {
            this.is_student_certified = is_student_certified;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getIs_bank_auth() {
            return is_bank_auth;
        }

        public void setIs_bank_auth(String is_bank_auth) {
            this.is_bank_auth = is_bank_auth;
        }

        public String getIs_mobile_auth() {
            return is_mobile_auth;
        }

        public void setIs_mobile_auth(String is_mobile_auth) {
            this.is_mobile_auth = is_mobile_auth;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getAlipay_user_id() {
            return alipay_user_id;
        }

        public void setAlipay_user_id(String alipay_user_id) {
            this.alipay_user_id = alipay_user_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getIs_id_auth() {
            return is_id_auth;
        }

        public void setIs_id_auth(String is_id_auth) {
            this.is_id_auth = is_id_auth;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
    }
}
