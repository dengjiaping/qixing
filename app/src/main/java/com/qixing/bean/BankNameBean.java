package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class BankNameBean implements Serializable{

    private String result;
    private String message;
    private List<BankName> banklist;

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

    public List<BankName> getBanklist() {
        return banklist;
    }

    public void setBanklist(List<BankName> banklist) {
        this.banklist = banklist;
    }


    public class BankName implements Serializable{
//        "id": "1",
//        "bank_name": "中国农业银行",
//        "sort": "1"
        private String id;
        private String bank_name;
        private String sort;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }
}
