package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class InfoJson implements Serializable{

    private String result;
    private String message;

    private List<Banner> banner;
    private List<QxInfo> list;
//    private List<Yxlist> yxlist;
    private List<Ghlist> ghlist;

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

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public List<QxInfo> getList() {
        return list;
    }

    public void setList(List<QxInfo> list) {
        this.list = list;
    }

//    public List<Yxlist> getYxlist() {
//        return yxlist;
//    }
//
//    public void setYxlist(List<Yxlist> yxlist) {
//        this.yxlist = yxlist;
//    }

    public List<Ghlist> getGhlist() {
        return ghlist;
    }

    public void setGhlist(List<Ghlist> ghlist) {
        this.ghlist = ghlist;
    }

    public class Banner implements Serializable{
        private String id;//        "id": "2",
        private String pic;//        "pic": "/Public/Uploads/banner/20161209/584a5e4b4191f.png",
        private String sort;//        "sort": "2",
        private String title;//        "title": "首页2",
        private String url;//        "url": "",
        private String state;//        "state": "1",
        private String types;//        "types": "3",
        private String times;//        "times": "0"

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

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }
    }



    public class QxInfo implements  Serializable{
        private String id;//        "id": "1",
        private String g_pic;//        "g_pic": "/Public/Uploads/zx/2016-12-08/d5849011dabe1b.jpg",
        private String g_pic1;//        "g_pic1": null,
        private String g_pic2;//        "g_pic2": null,
        private String g_title;//        "g_title": "咨询1",
        private String times;//        "times": "1481273528"
        private String state;
        private String is_new;

        public String getIs_new() {
            return is_new;
        }

        public void setIs_new(String is_new) {
            this.is_new = is_new;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getG_pic() {
            return g_pic;
        }

        public void setG_pic(String g_pic) {
            this.g_pic = g_pic;
        }

        public String getG_pic1() {
            return g_pic1;
        }

        public void setG_pic1(String g_pic1) {
            this.g_pic1 = g_pic1;
        }

        public String getG_pic2() {
            return g_pic2;
        }

        public void setG_pic2(String g_pic2) {
            this.g_pic2 = g_pic2;
        }

        public String getG_title() {
            return g_title;
        }

        public void setG_title(String g_title) {
            this.g_title = g_title;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public class Yxlist implements Serializable{
        private String id;//        "id": "1",
        private String g_pic;//        "g_pic": "/Public/Uploads/zx/2016-12-08/d5849011dabe1b.jpg",
        private String g_pic1;//        "g_pic1": null,
        private String g_pic2;//        "g_pic2": null,
        private String g_title;//        "g_title": "咨询1",
        private String times;//        "times": "1481273528"
        private String state;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getG_pic() {
            return g_pic;
        }

        public void setG_pic(String g_pic) {
            this.g_pic = g_pic;
        }

        public String getG_pic1() {
            return g_pic1;
        }

        public void setG_pic1(String g_pic1) {
            this.g_pic1 = g_pic1;
        }

        public String getG_pic2() {
            return g_pic2;
        }

        public void setG_pic2(String g_pic2) {
            this.g_pic2 = g_pic2;
        }

        public String getG_title() {
            return g_title;
        }

        public void setG_title(String g_title) {
            this.g_title = g_title;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public class Ghlist implements Serializable{
        private String id;//            "id": "7",
        private String g_url;//        "g_url": "/Public/Uploads/gh/2016-12-09/584a75fa49fc9.pdf",
        private String g_title;//        "g_title": "干货3",
        private String times;//        "times": "1481274874"
        private String state;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getG_url() {
            return g_url;
        }

        public void setG_url(String g_url) {
            this.g_url = g_url;
        }

        public String getG_title() {
            return g_title;
        }

        public void setG_title(String g_title) {
            this.g_title = g_title;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
