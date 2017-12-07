package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class ReviseBankJson implements Serializable{

    private String result;
    private String message;
    private List<Bank> banks;

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

    public List<Bank> getBanks() {
        return banks;
    }

//            "id": "5",
//            "u_id": "2",
//            "card_num": "123456441",
//            "kh_ren": "wen",
//            "name": "招商银行",
//            "carddeposit": "招商",
//            "sfdq_tj": "北京市",
//            "csdq_tj": "北京市",
//            "qydq_tj": "东城区",
//            "bank_tel": "2147483647",
//            "status": "0",
//            "times": "1452926045"
//            "bankid": "3"
    public class Bank implements Serializable{
        private String id;//
        private String u_id;//
        private String card_num;//
        private String name;//
        private String carddeposit;//
        private String sfdq_tj;//
        private String csdq_tj;//
        private String qydq_tj;//
        private String bank_tel;//
        private String status;//
        private String times;//
        private String bankid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getU_id() {
            return u_id;
        }

        public void setU_id(String u_id) {
            this.u_id = u_id;
        }

        public String getCard_num() {
            return card_num;
        }

        public void setCard_num(String card_num) {
            this.card_num = card_num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCarddeposit() {
            return carddeposit;
        }

        public void setCarddeposit(String carddeposit) {
            this.carddeposit = carddeposit;
        }

        public String getSfdq_tj() {
            return sfdq_tj;
        }

        public void setSfdq_tj(String sfdq_tj) {
            this.sfdq_tj = sfdq_tj;
        }

        public String getCsdq_tj() {
            return csdq_tj;
        }

        public void setCsdq_tj(String csdq_tj) {
            this.csdq_tj = csdq_tj;
        }

        public String getQydq_tj() {
            return qydq_tj;
        }

        public void setQydq_tj(String qydq_tj) {
            this.qydq_tj = qydq_tj;
        }

        public String getBank_tel() {
            return bank_tel;
        }

        public void setBank_tel(String bank_tel) {
            this.bank_tel = bank_tel;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getBankid() {
            return bankid;
        }

        public void setBankid(String bankid) {
            this.bankid = bankid;
        }
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }
}
