package com.qixing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wicep on 2015/12/25.
 */
public class IndexJson implements Serializable{

    private String result;
    private String message;

    private String newpic;
    private List<Banner> banner;
    private List<Live> zb;
    private List<Video> video;
    private List<Zxlist> zxlist;
//    private List<Ghlist> ghlist;
    private List<Yxlist> yxlist;
    private List<Gh> ghlist;

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

    public String getNewpic() {
        return newpic;
    }

    public void setNewpic(String newpic) {
        this.newpic = newpic;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public List<Live> getZb() {
        return zb;
    }

    public void setZb(List<Live> zb) {
        this.zb = zb;
    }

    public List<Video> getVideo() {
        return video;
    }

    public void setVideo(List<Video> video) {
        this.video = video;
    }

    public List<Zxlist> getZxlist() {
        return zxlist;
    }

    public void setZxlist(List<Zxlist> zxlist) {
        this.zxlist = zxlist;
    }

    public List<Yxlist> getMarketList() {
        return yxlist;
    }

    public void setMarketList(List<Yxlist> yxlist) {
        this.yxlist = yxlist;
    }


    public List<Gh> getGhlist() {
        return ghlist;
    }

    public void setGhlist(List<Gh> ghlist) {
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
        private String banner_types;//         "banner_types": "1",

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

        public String getBanner_types() {
            return banner_types;
        }

        public void setBanner_types(String banner_types) {
            this.banner_types = banner_types;
        }
    }

    public class Live implements Serializable{
        private String id;//       "id": "1",
        private String uid;//        "uid": "1",
        private String zb_pic;//        "zb_pic": "/Public/Uploads/pic/2016-12-08/d5848fa2635344.jpg",
        private String zb_title;//        "zb_title": "直播1",
        private String see_num;//        "see_num": "2",
        private String istj;//        "istj": "1",
        private String status;//        "status": "1",
        private String times;//        "times": "0",
        private String pic;//        "pic": null,
        private String uname;//        "uname": "18749090599"
        private String app_types;//    app_types 1手机  2摄像机

        private String yy_uids;//       "yy_uids": "58",
        private String is_yy;//         "is_yy": 0

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getZb_pic() {
            return zb_pic;
        }

        public void setZb_pic(String zb_pic) {
            this.zb_pic = zb_pic;
        }

        public String getZb_title() {
            return zb_title;
        }

        public void setZb_title(String zb_title) {
            this.zb_title = zb_title;
        }

        public String getSee_num() {
            return see_num;
        }

        public void setSee_num(String see_num) {
            this.see_num = see_num;
        }

        public String getIstj() {
            return istj;
        }

        public void setIstj(String istj) {
            this.istj = istj;
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

        public String getApp_types() {
            return app_types;
        }

        public void setApp_types(String app_types) {
            this.app_types = app_types;
        }

        public String getYy_uids() {
            return yy_uids;
        }

        public void setYy_uids(String yy_uids) {
            this.yy_uids = yy_uids;
        }

        public String getIs_yy() {
            return is_yy;
        }

        public void setIs_yy(String is_yy) {
            this.is_yy = is_yy;
        }
    }

    public class Video implements Serializable{
        private String id;//       "id": "1",
        private String v_pic;//        "v_pic": "/Public/Uploads/pic/2016-12-08/d5848fa2635344.jpg",
        private String v_title;//        "v_title": "视频1",
        private String sp_nr;//        "sp_nr": "奔奔在",
        private String see_num;//        "see_num": "0",
        private String v_url;//        "v_url": "/Public/Uploads/video/xuanchuan.mp4",
        private String v_urltype;//         "v_urltype": "1",
        private String times;//        "times": "1481176959"
        private String share_num;//
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

        public String getV_pic() {
            return v_pic;
        }

        public void setV_pic(String v_pic) {
            this.v_pic = v_pic;
        }

        public String getV_title() {
            return v_title;
        }

        public void setV_title(String v_title) {
            this.v_title = v_title;
        }

        public String getSp_nr() {
            return sp_nr;
        }

        public void setSp_nr(String sp_nr) {
            this.sp_nr = sp_nr;
        }

        public String getSee_num() {
            return see_num;
        }

        public void setSee_num(String see_num) {
            this.see_num = see_num;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getV_url() {
            return v_url;
        }

        public void setV_url(String v_url) {
            this.v_url = v_url;
        }

        public String getV_urltype() {
            return v_urltype;
        }

        public void setV_urltype(String v_urltype) {
            this.v_urltype = v_urltype;
        }

        public String getShare_num() {
            return share_num;
        }

        public void setShare_num(String share_num) {
            this.share_num = share_num;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public class Zxlist implements  Serializable{
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
        private String id;      //   "id": "1",
        private String g_pic;  //   "g_pic": "/Public/Uploads/zx/2016-12-08/d5849011dabe1b.jpg"
        private String g_pic1;//        "g_pic1": null,
        private String g_pic2;//        "g_pic2": null,
        private String g_title;//    "g_title": "视频1"
        private String times;  //     "times":  "1481273528"
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

    public class Gh implements Serializable{
        private String id;//id = 1;
        private String pic;//pic = "/Public/Uploads/pic/2017-03-13/x58c63b577befa.png";
        private String times;//times = 1489381819;
        private String title;//title = "\U6848\U4f8b\U5206\U4eab";
        private List<Ghlist> list;

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

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Ghlist> getList() {
            return list;
        }

        public void setList(List<Ghlist> list) {
            this.list = list;
        }
    }

    public class Ghlist implements Serializable{
        private String id;//            "id": "7",
        private String g_url;//        "g_url": "/Public/Uploads/gh/2016-12-09/584a75fa49fc9.pdf",
        private String g_title;//        "g_title": "干货3",
        private String times;//        "times": "1481274874"
        private String state;
        private String isnew;
        private String tag;
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

        public String getIsnew() {
            return isnew;
        }

        public void setIsnew(String isnew) {
            this.isnew = isnew;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
