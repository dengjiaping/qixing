package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class RebateEnterJson implements Serializable{

    private String result;//       "result": "1",
    private String message;//        "message": "成功！",
    private String money;//        "money": 1980,
    private String yemoney;//        "yemoney": "990.00",
    private Banner banner;//        "banner":

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getYemoney() {
        return yemoney;
    }

    public void setYemoney(String yemoney) {
        this.yemoney = yemoney;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public class Banner implements Serializable{
        private String pic;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
