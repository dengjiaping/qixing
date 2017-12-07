package com.qixing.qxlive.gift;

import com.qixing.R;
import com.qixing.bean.GiftBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/1/6.
 */
public class GiftDateUtlis {

    public static List<GiftBean> initGiftDate(){
        List<GiftBean> giftList = new ArrayList<GiftBean>();
        int img[] = {R.drawable.icon_hua,R.drawable.icon_clup,R.drawable.icon_fire,R.drawable.icon_firework,
                R.drawable.icon_rocket,R.drawable.icon_balloon,R.drawable.icon_airplane,R.drawable.icon_wheel,
                };
        String msg[] = {"送一朵鲜花","送一个掌声","送一个打火机","送了一个烟花",
                "送一个火箭","送一个告白气球","送一架飞机","送一座摩天轮",};
        String name[] = {"鲜花","掌声","打火机","烟花",
                "火箭","告白气球","飞机","摩天轮",};
        String xingbi[] = {"10","55","99","299","369","1111","2888","5888"};
        String types[] = {"1","2","3","4","5","6","7","8"};
        for(int i=0;i<8;i++){
            GiftBean mGiftBean = new GiftBean();
            mGiftBean.setSelect("0");
            mGiftBean.setImgID(img[i]);
            mGiftBean.setXingbi(xingbi[i]);
            mGiftBean.setMsg(msg[i]);
            mGiftBean.setName(name[i]);
            mGiftBean.setTypes(types[i]);
            giftList.add(mGiftBean);
        }
        return giftList;
    }

    public static List<GiftBean> loadMore(){
        List<GiftBean> giftList = new ArrayList<GiftBean>();
        int img[] = {R.drawable.icon_like,R.drawable.icon_666,R.drawable.icon_cheer,R.drawable.icon_muah,
                R.drawable.icon_star,R.drawable.icon_lamborghini,R.drawable.icon_streamship,R.drawable.icon_sleepingbeauty};
        String msg[] = {"送一个赞","送一个666","送一杯啤酒","送一个么么哒",
                "送一阵流星雨","送一架兰博基尼","送一艘轮船","送一个睡美人"};
        String name[] = {"赞","666","啤酒","么么哒",
                "流星雨","兰博基尼","轮船","睡美人"};
        String xingbi[] = {"18","66","188","520","888","1888","3888","8888"};
        String types[] = {"9","10","11","12","13","14","15","16"};
        for(int i=0;i<8;i++){
            GiftBean mGiftBean = new GiftBean();
            mGiftBean.setSelect("0");
            mGiftBean.setImgID(img[i]);
            mGiftBean.setXingbi(xingbi[i]);
            mGiftBean.setMsg(msg[i]);
            mGiftBean.setName(name[i]);
            mGiftBean.setTypes(types[i]);
            giftList.add(mGiftBean);
        }
        return giftList;
    }

    public static int getGiftImgID(String msg){
        int imgID = -1;
        List<GiftBean> giftList = initGiftDate();
        for(int i = 0; i<giftList.size();i++){
            if(msg.equals(giftList.get(i).getMsg())){
                imgID = giftList.get(i).getImgID();
            }
        }
        return imgID;
    }
}
