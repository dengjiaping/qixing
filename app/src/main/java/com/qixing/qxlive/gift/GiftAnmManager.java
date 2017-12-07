package com.qixing.qxlive.gift;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.qixing.R;
import com.yan.bsrgift.BSRGiftLayout;
import com.yan.bsrgift.BSRGiftView;
import com.yan.bsrgift.BSRPathBase;
import com.yan.bsrgift.BSRPathPoint;
import com.yan.bsrgift.BSRPathView;
import com.yan.bsrgift.OnAnmEndListener;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by yan on 2016/12/14.
 */

public class GiftAnmManager {

    private BSRGiftLayout giftLayout;
    private Context context;
    int[] car1Ids = new int[]{
            R.drawable.gift_caro1,
            R.drawable.gift_caro2,
            R.drawable.gift_caro3,
            R.drawable.gift_caro4,
            R.drawable.gift_caro5,
            R.drawable.gift_caro6,
            R.drawable.gift_caro7,
            R.drawable.gift_caro8,
    };
    int[] car2Ids = new int[]{
            R.drawable.gift_car_t1,
            R.drawable.gift_car_t2,
    };

    int[] Lamborghini=new int[]{//跑车
            R.drawable.lamborghini01,
            R.drawable.lamborghini02,
            R.drawable.lamborghini03,
            R.drawable.lamborghini04,
            R.drawable.lamborghini05,
            R.drawable.lamborghini06,
            R.drawable.lamborghini07,
            R.drawable.lamborghini08,
            R.drawable.lamborghini09,
            R.drawable.lamborghini10,
            R.drawable.lamborghini11,
            R.drawable.lamborghini12,
            R.drawable.lamborghini13,
            R.drawable.lamborghini14,
            R.drawable.lamborghini15,
            R.drawable.lamborghini16,
            R.drawable.lamborghini17,
            R.drawable.lamborghini18,
            R.drawable.lamborghini19,
            R.drawable.lamborghini20,
            R.drawable.lamborghini21,
            R.drawable.lamborghini22,
            R.drawable.lamborghini23,
            R.drawable.lamborghini24,
            R.drawable.lamborghini25,
            R.drawable.lamborghini26,
            R.drawable.lamborghini27,
            R.drawable.lamborghini28,
            R.drawable.lamborghini29,
            R.drawable.lamborghini30
    };

    int[] star=new int[]{//流星雨
            R.drawable.star01,
            R.drawable.star02,
            R.drawable.star03,
            R.drawable.star04,
            R.drawable.star05,
            R.drawable.star06,
            R.drawable.star07,
            R.drawable.star08,
            R.drawable.star09,
            R.drawable.star10,
            R.drawable.star11,
            R.drawable.star12,
            R.drawable.star13,
            R.drawable.star14,
            R.drawable.star15,
            R.drawable.star16,
            R.drawable.star17,
            R.drawable.star18,
            R.drawable.star19,
            R.drawable.star20,
            R.drawable.star21,
            R.drawable.star22,
            R.drawable.star23,
            R.drawable.star24,
            R.drawable.star25,
            R.drawable.star26,
            R.drawable.star27,
            R.drawable.star28,
            R.drawable.star29,
            R.drawable.star30,
            R.drawable.star31,
            R.drawable.star32,
            R.drawable.star33,
            R.drawable.star34,
            R.drawable.star35,
            R.drawable.star36,
            R.drawable.star37,
            R.drawable.star38,
            R.drawable.star39,
            R.drawable.star40,
            R.drawable.star41,
            R.drawable.star42,
            R.drawable.star43,
            R.drawable.star44,
            R.drawable.star45,
            R.drawable.star46,
            R.drawable.star47,
            R.drawable.star48,
            R.drawable.star49,
            R.drawable.star50,
            R.drawable.star51,
            R.drawable.star52,
            R.drawable.star53,
            R.drawable.star54,
            R.drawable.star55,
            R.drawable.star56,
            R.drawable.star57,
            R.drawable.star58,
            R.drawable.star59,
            R.drawable.star60
    };

    int[] air=new int[]{//飞机
            R.drawable.airplane01,
            R.drawable.airplane02,
            R.drawable.airplane03,
            R.drawable.airplane04,
            R.drawable.airplane05,
            R.drawable.airplane06,
            R.drawable.airplane07,
            R.drawable.airplane08,
            R.drawable.airplane09,
            R.drawable.airplane10,
            R.drawable.airplane11,
            R.drawable.airplane12,
            R.drawable.airplane13,
            R.drawable.airplane14,
            R.drawable.airplane15,
            R.drawable.airplane16,
            R.drawable.airplane17,
            R.drawable.airplane18,
            R.drawable.airplane19,
            R.drawable.airplane20,
            R.drawable.airplane21,
            R.drawable.airplane22,
            R.drawable.airplane23,
            R.drawable.airplane24,
            R.drawable.airplane25,
            R.drawable.airplane26,
            R.drawable.airplane27,
            R.drawable.airplane28,
            R.drawable.airplane29,
            R.drawable.airplane30,
            R.drawable.airplane31,
            R.drawable.airplane32,
            R.drawable.airplane33,
            R.drawable.airplane34,
            R.drawable.airplane35,
            R.drawable.airplane36,
            R.drawable.airplane37,
            R.drawable.airplane38,
            R.drawable.airplane39,
            R.drawable.airplane40

    };

    int[] cheer=new int[]{//干杯
            R.drawable.cheer01,
            R.drawable.cheer02,
            R.drawable.cheer03,
            R.drawable.cheer04,
            R.drawable.cheer05,
            R.drawable.cheer06,
            R.drawable.cheer07,
            R.drawable.cheer08,
            R.drawable.cheer09,
            R.drawable.cheer10,
            R.drawable.cheer11,
            R.drawable.cheer12,
            R.drawable.cheer13,
            R.drawable.cheer14,
            R.drawable.cheer15,
            R.drawable.cheer16,
    };

    int[] balloon=new int[]{//告白气球
            R.drawable.balloon01,
            R.drawable.balloon02,
            R.drawable.balloon03,
            R.drawable.balloon04,
            R.drawable.balloon05,
            R.drawable.balloon06,
            R.drawable.balloon07,
            R.drawable.balloon08,
            R.drawable.balloon09,
            R.drawable.balloon10,
            R.drawable.balloon11,
            R.drawable.balloon12,
            R.drawable.balloon13,
            R.drawable.balloon14,
            R.drawable.balloon15,
            R.drawable.balloon16,
            R.drawable.balloon17,
            R.drawable.balloon18,
            R.drawable.balloon19,
            R.drawable.balloon20,
            R.drawable.balloon21,
            R.drawable.balloon22,
            R.drawable.balloon23,
            R.drawable.balloon24,
            R.drawable.balloon25,
            R.drawable.balloon26,
            R.drawable.balloon27,
            R.drawable.balloon28,
            R.drawable.balloon29,
            R.drawable.balloon30,
            R.drawable.balloon31,
            R.drawable.balloon32,
            R.drawable.balloon33,
            R.drawable.balloon34,
            R.drawable.balloon35,
            R.drawable.balloon36,
            R.drawable.balloon37,
            R.drawable.balloon38,
            R.drawable.balloon39,
            R.drawable.balloon40
    };

    int[] rocket=new int[]{//火箭
            R.drawable.rocket01,
            R.drawable.rocket02,
            R.drawable.rocket03,
            R.drawable.rocket04,
            R.drawable.rocket05,
            R.drawable.rocket06,
            R.drawable.rocket07,
            R.drawable.rocket08,
            R.drawable.rocket09,
            R.drawable.rocket10,
            R.drawable.rocket11,
            R.drawable.rocket12,
            R.drawable.rocket13,
            R.drawable.rocket14,
            R.drawable.rocket15,
            R.drawable.rocket16,
            R.drawable.rocket17,
            R.drawable.rocket18,
            R.drawable.rocket19,
            R.drawable.rocket20,
            R.drawable.rocket21,
            R.drawable.rocket22,
            R.drawable.rocket23,
            R.drawable.rocket24,
            R.drawable.rocket25,
            R.drawable.rocket26,
            R.drawable.rocket27,
            R.drawable.rocket28,
            R.drawable.rocket29,
            R.drawable.rocket30,
            R.drawable.rocket31,
            R.drawable.rocket32,
            R.drawable.rocket33,
            R.drawable.rocket34,
            R.drawable.rocket35,
            R.drawable.rocket36,
            R.drawable.rocket37,
            R.drawable.rocket38,
            R.drawable.rocket39,
            R.drawable.rocket40,
            R.drawable.rocket41,
            R.drawable.rocket42,
            R.drawable.rocket43,
            R.drawable.rocket44,
            R.drawable.rocket45,
            R.drawable.rocket46,
            R.drawable.rocket47,
            R.drawable.rocket48,
            R.drawable.rocket49,
            R.drawable.rocket50
    };

    int[] streamship=new int[]{//轮船
            R.drawable.streamship01,
            R.drawable.streamship02,
            R.drawable.streamship03,
            R.drawable.streamship04,
            R.drawable.streamship05,
            R.drawable.streamship06,
            R.drawable.streamship07,
            R.drawable.streamship08,
            R.drawable.streamship09,
            R.drawable.streamship10,
            R.drawable.streamship11,
            R.drawable.streamship12,
            R.drawable.streamship13,
            R.drawable.streamship14,
            R.drawable.streamship15,
            R.drawable.streamship16,
            R.drawable.streamship17,
            R.drawable.streamship18,
            R.drawable.streamship19,
            R.drawable.streamship20,
            R.drawable.streamship21,
            R.drawable.streamship22,
            R.drawable.streamship23,
            R.drawable.streamship24,
            R.drawable.streamship25,
            R.drawable.streamship26,
            R.drawable.streamship27,
            R.drawable.streamship28,
            R.drawable.streamship29,
            R.drawable.streamship30,
    };

    int[] muah=new int[]{//么么哒
            R.drawable.muah01,
            R.drawable.muah02,
            R.drawable.muah03,
            R.drawable.muah04,
            R.drawable.muah05,
            R.drawable.muah06,
            R.drawable.muah07,
            R.drawable.muah08,
            R.drawable.muah09,
            R.drawable.muah10,
            R.drawable.muah11,
            R.drawable.muah12,
            R.drawable.muah13,
            R.drawable.muah14,
            R.drawable.muah15,
            R.drawable.muah16,
            R.drawable.muah17,
            R.drawable.muah18,
            R.drawable.muah19,
            R.drawable.muah20,
            R.drawable.muah21,
            R.drawable.muah22,
            R.drawable.muah23,
            R.drawable.muah24,
            R.drawable.muah25,
            R.drawable.muah26,
            R.drawable.muah27,
            R.drawable.muah28,
            R.drawable.muah29,
            R.drawable.muah30
    };

    int[] wheel=new int[]{//摩天轮
            R.drawable.ferriswheel01,
            R.drawable.ferriswheel02,
            R.drawable.ferriswheel03,
            R.drawable.ferriswheel04,
            R.drawable.ferriswheel05,
            R.drawable.ferriswheel06,
            R.drawable.ferriswheel07,
            R.drawable.ferriswheel08,
            R.drawable.ferriswheel09,
            R.drawable.ferriswheel10,
            R.drawable.ferriswheel11,
            R.drawable.ferriswheel12,
            R.drawable.ferriswheel13,
            R.drawable.ferriswheel14,
            R.drawable.ferriswheel15,
            R.drawable.ferriswheel16,
            R.drawable.ferriswheel17,
            R.drawable.ferriswheel18,
            R.drawable.ferriswheel19,
            R.drawable.ferriswheel20,
            R.drawable.ferriswheel21,
            R.drawable.ferriswheel22,
            R.drawable.ferriswheel23,
            R.drawable.ferriswheel24,
            R.drawable.ferriswheel25,
            R.drawable.ferriswheel26,
            R.drawable.ferriswheel27,
            R.drawable.ferriswheel28,
            R.drawable.ferriswheel29,
            R.drawable.ferriswheel30,
            R.drawable.ferriswheel31,
            R.drawable.ferriswheel32,
            R.drawable.ferriswheel33,
            R.drawable.ferriswheel34,
            R.drawable.ferriswheel35,
            R.drawable.ferriswheel36,
            R.drawable.ferriswheel37,
            R.drawable.ferriswheel38,
            R.drawable.ferriswheel39,
            R.drawable.ferriswheel40,
            R.drawable.ferriswheel41,
            R.drawable.ferriswheel42,
            R.drawable.ferriswheel43,
            R.drawable.ferriswheel44,
            R.drawable.ferriswheel45

    };

    int[] beauty=new int[]{//睡美人
            R.drawable.sleepingbeauty01,
            R.drawable.sleepingbeauty02,
            R.drawable.sleepingbeauty03,
            R.drawable.sleepingbeauty04,
            R.drawable.sleepingbeauty05,
            R.drawable.sleepingbeauty06,
            R.drawable.sleepingbeauty07,
            R.drawable.sleepingbeauty08,
            R.drawable.sleepingbeauty09,
            R.drawable.sleepingbeauty10,
            R.drawable.sleepingbeauty11,
            R.drawable.sleepingbeauty12,
            R.drawable.sleepingbeauty13,
            R.drawable.sleepingbeauty14,
            R.drawable.sleepingbeauty15,
            R.drawable.sleepingbeauty16,
            R.drawable.sleepingbeauty17,
            R.drawable.sleepingbeauty18,
            R.drawable.sleepingbeauty19,
            R.drawable.sleepingbeauty20,
            R.drawable.sleepingbeauty21,
            R.drawable.sleepingbeauty22,
            R.drawable.sleepingbeauty23,
            R.drawable.sleepingbeauty24,
            R.drawable.sleepingbeauty25,
            R.drawable.sleepingbeauty26,
            R.drawable.sleepingbeauty27,
            R.drawable.sleepingbeauty28,
            R.drawable.sleepingbeauty29,
            R.drawable.sleepingbeauty30,
            R.drawable.sleepingbeauty31,
            R.drawable.sleepingbeauty32,
            R.drawable.sleepingbeauty33,
            R.drawable.sleepingbeauty34,
            R.drawable.sleepingbeauty35,
            R.drawable.sleepingbeauty36,
            R.drawable.sleepingbeauty37,
            R.drawable.sleepingbeauty38,
            R.drawable.sleepingbeauty39,
            R.drawable.sleepingbeauty40,
            R.drawable.sleepingbeauty41,
            R.drawable.sleepingbeauty42,
            R.drawable.sleepingbeauty43,
            R.drawable.sleepingbeauty44,
            R.drawable.sleepingbeauty45,
            R.drawable.sleepingbeauty46,
            R.drawable.sleepingbeauty47,
            R.drawable.sleepingbeauty48,
            R.drawable.sleepingbeauty49,
            R.drawable.sleepingbeauty50
    };

    int[] fireworks=new int[]{//烟花
            R.drawable.fireworks01,
            R.drawable.fireworks02,
            R.drawable.fireworks03,
            R.drawable.fireworks04,
            R.drawable.fireworks05,
            R.drawable.fireworks06,
            R.drawable.fireworks07,
            R.drawable.fireworks08,
            R.drawable.fireworks09,
            R.drawable.fireworks10,
            R.drawable.fireworks11,
            R.drawable.fireworks12,
            R.drawable.fireworks13,
            R.drawable.fireworks14,
            R.drawable.fireworks15,
            R.drawable.fireworks16,
            R.drawable.fireworks17,
            R.drawable.fireworks18,
            R.drawable.fireworks19,
            R.drawable.fireworks20,
            R.drawable.fireworks21,
            R.drawable.fireworks22,
            R.drawable.fireworks23,
            R.drawable.fireworks24,
            R.drawable.fireworks25,
            R.drawable.fireworks26,
            R.drawable.fireworks27,
            R.drawable.fireworks28,
            R.drawable.fireworks29,
            R.drawable.fireworks30
    };

    public GiftAnmManager(BSRGiftLayout giftLayout, Context context) {
        this.context = context;
        this.giftLayout = giftLayout;
    }

    public void showCarOne() {
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);

        final int during = 150;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, car1Ids[index++ % 7]);
                        carOne.setAdjustScaleInScreen(0.8f);
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.addPositionControlPoint(1, 0.1f);
        bsrPathView.addPositionControlPoint(0.05f, 0.3f);
        bsrPathView.addPositionControlPoint(0.05f, 0.3f);
        bsrPathView.addPositionControlPoint(0.05f, 0.3f);
        bsrPathView.addPositionControlPoint(0.05f, 0.3f);
        bsrPathView.addPositionControlPoint(0.05f, 0.3f);
        bsrPathView.addPositionControlPoint(0.05f, 0.3f);
        bsrPathView.addPositionControlPoint(-1f, 0.5f);

        bsrPathView.setDuring(3000);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });

        giftLayout.addChild(bsrPathView);
    }

    public void showAirplane(){//飞机
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 100;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, air[index++ % 40]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(40*200);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.setGetControl(true);
        giftLayout.addChild(bsrPathView);
    }

    public void showLamborghini(){//兰博基尼
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 100;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, Lamborghini[index++ % 30]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(30*220);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.setGetControl(true);
        giftLayout.addChild(bsrPathView);
    }

    public void showStar(){//流星雨
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 100;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, star[index++ % 60]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(60*200);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.setGetControl(true);
        giftLayout.addChild(bsrPathView);
    }

    public void showCheer(){//干杯
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 150;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, cheer[index++ % 16]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(8000);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.setGetControl(true);
        giftLayout.addChild(bsrPathView);
    }

    public void showBalloon(){//告白气球
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 100;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, balloon[index++ % 40]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(40*240);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.setGetControl(true);
        giftLayout.addChild(bsrPathView);
    }

    public void showRocket(){//火箭
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 100;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, rocket[index++ % 50]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(50*200);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.setGetControl(true);
        giftLayout.addChild(bsrPathView);
    }

    public void showStreamShip(){//轮船
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 100;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, streamship[index++ % 30]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(30*200);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.setGetControl(true);
        giftLayout.addChild(bsrPathView);
    }

    public void showMuah(){//呵呵哒
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 100;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, muah[index++ % 30]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(30*220);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.setGetControl(true);
        giftLayout.addChild(bsrPathView);
    }

    public void showWheel(){//摩天轮
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 100;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, wheel[index++ % 45]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(45*200);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.setGetControl(true);
        giftLayout.addChild(bsrPathView);
    }

    public void showBeauty(){//睡美人
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 100;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, beauty[index++ % 50]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(50*200);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.setGetControl(true);
        giftLayout.addChild(bsrPathView);
    }

    public void showFireWorks(){//烟花
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 100;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, fireworks[index++ % 30]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(30*200);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.setGetControl(true);
        giftLayout.addChild(bsrPathView);
    }

    public void showCarOnePath() {
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 150;
        final Subscription[] subscription = new Subscription[1];

        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carOne = new BSRPathPoint();
                        carOne.setDuring(during);
                        carOne.setInterpolator(new LinearInterpolator());
                        carOne.setRes(context, car1Ids[index++ % 7]);
                        carOne.centerInside();
                        carOne.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carOne);
                    }

                    public void onError(Throwable t) {
                    }

                    public void onComplete() {
                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.setPositionXPercent(0.5f);
        bsrPathView.setPositionYPercent(0.5f);
        bsrPathView.addPositionControlPoint(0, 0);
        bsrPathView.addPositionControlPoint(0, 1);
        bsrPathView.addPositionControlPoint(1, 1);
        bsrPathView.addPositionControlPoint(1, 0);
        bsrPathView.setScale(0.3f);
        bsrPathView.setFirstRotation(90);
        bsrPathView.setXPercent(0.5f);
        bsrPathView.setYPercent(0.5f);
        bsrPathView.setAutoRotation(true);
        bsrPathView.setDuring(3000);
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.addChild(bsrPathView);
    }

    public void showCarTwo() {
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 300;
        final Subscription[] subscription = new Subscription[1];
        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carTwo = new BSRPathPoint();
                        carTwo.setDuring(during);
                        carTwo.setInterpolator(new LinearInterpolator());
                        carTwo.setRes(context, car2Ids[index++ % 2]);
                        carTwo.setAdjustScaleInScreen(1f);
                        carTwo.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carTwo);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.addPositionControlPoint(0f, 0.3f);
        bsrPathView.addPositionControlPoint(0.06f, 0.3f);
        bsrPathView.addPositionControlPoint(0.06f, 0.3f);
        bsrPathView.addPositionControlPoint(0.06f, 0.3f);
        bsrPathView.addPositionControlPoint(0.06f, 0.3f);
        bsrPathView.addPositionControlPoint(0.06f, 0.3f);
        bsrPathView.addPositionControlPoint(0.06f, 0.3f);
        bsrPathView.addPositionControlPoint(0.06f, 0.3f);
        bsrPathView.addScaleControl(0.2f);
        bsrPathView.addScaleControl(0.8f);
        bsrPathView.addScaleControl(0.8f);
        bsrPathView.addScaleControl(0.8f);
        bsrPathView.addScaleControl(0.8f);
        bsrPathView.addScaleControl(0.8f);
        bsrPathView.addScaleControl(0.8f);
        bsrPathView.addScaleControl(10f);
        bsrPathView.setXPercent(0f);
        bsrPathView.setYPercent(0f);
        bsrPathView.setDuring(2000);
        bsrPathView.setInterpolator(new AccelerateInterpolator());
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.addChild(bsrPathView);
    }

    public void showKQ() {
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        int during = 1200;
        bsrGiftView.setAlphaTrigger(1);
        final List<BSRPathPoint> bsrPathPoints = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            BSRPathPoint bsrPointT = new BSRPathPoint();

            if (i == 0)
                bsrPointT.setLastRotation(0);
            else if (i > 0 && i < 7) {
                bsrPointT.setLastRotation(i * 20);
            } else if (i >= 7 && i < 13) {
                bsrPointT.setLastRotation(-(i - 6) * 20);
            }
            bsrPointT.setPositionInScreen(true);
            bsrPointT.setPositionPoint(0.5f, 0.1f);
            bsrPointT.setPositionXPercent(0.5f);
            bsrPointT.setRes(context, R.drawable.kongque_cibang1);
            bsrPointT.setDuring(during);
            bsrPointT.setXPercent(0.5f);
            bsrPointT.setYPercent(1.0f);
            bsrPointT.setAdjustScaleInScreen(1f);
            bsrPointT.setScale(0.2f);
            bsrPointT.setAntiAlias(true);
            bsrPointT.setInterpolator(new DecelerateInterpolator());
            bsrPathPoints.add(bsrPointT);
        }

        BSRPathPoint bsrPoint = new BSRPathPoint();
        bsrPoint.setPositionInScreen(true);
        bsrPoint.attachPoint(bsrPathPoints.get(0), -0.01f, 0.13f);
        bsrPoint.setRes(context, R.drawable.kongque_main);
        bsrPoint.setDuring(during);
        bsrPoint.setAdjustScaleInScreen(1);
        bsrPoint.setScale(0.4f);
        bsrPoint.setInterpolator(new DecelerateInterpolator());
        bsrPoint.setAntiAlias(true);
        bsrPathPoints.add(bsrPoint);

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setDuring(during * 2);

        giftLayout.addChild(bsrPathView);
        bsrGiftView.addBSRPathPoints(bsrPathPoints);
    }

    public void showPositionInScreen() {
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        int during = 2000;
        bsrGiftView.setAlphaTrigger(0.99f);
        final List<BSRPathPoint> bsrPathPoints = new ArrayList<>();

        BSRPathPoint bsrPoint = new BSRPathPoint();
        bsrPoint.setRes(context, R.drawable.gift_car_t2);
        bsrPoint.setDuring(during);
        bsrPoint.setPositionInScreen(true);
        bsrPoint.setFirstRotation(-90);
        bsrPoint.setAutoRotation(true);
        bsrPoint.setAdjustScaleInScreen(1f);
        bsrPoint.addPositionControlPoint(0f, 0f);
        bsrPoint.addPositionControlPoint(1f, 0f);
        bsrPoint.addPositionControlPoint(1f, 1f);
        bsrPoint.addPositionControlPoint(0f, 1f);
        bsrPoint.setScale(0.4f);
        bsrPoint.setInterpolator(new DecelerateInterpolator());
        bsrPoint.setAntiAlias(true);
        bsrPoint.setPositionXPercent(0.5f);
        bsrPoint.setPositionYPercent(0.5f);
        bsrPathPoints.add(bsrPoint);

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setDuring(during * 2);

        giftLayout.addChild(bsrPathView);
        bsrGiftView.addBSRPathPoints(bsrPathPoints);
    }


    public void showKiss() {
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        int during = 2000;
        bsrGiftView.setAlphaTrigger(0.99f);
        List<BSRPathPoint> bsrPathPoints = new ArrayList<>();

        BSRPathPoint pathPoint = new BSRPathPoint();
        pathPoint.setRes(context, R.drawable.kiss_chun);
        pathPoint.setDuring(during);
        pathPoint.setPositionInScreen(true);
        pathPoint.setPositionXPercent(0.5f);
        pathPoint.setPositionYPercent(0.5f);
        pathPoint.setPositionPoint(0.68f, 0.45f);
        pathPoint.addScaleControl(0.2f);
        pathPoint.addScaleControl(0.8f);
        pathPoint.addScaleControl(0.8f);
        pathPoint.addScaleControl(0.8f);
        pathPoint.addScaleControl(0.8f);
        pathPoint.addScaleControl(0.8f);
        pathPoint.addScaleControl(0.8f);
        pathPoint.setXPercent(0.5f);
        pathPoint.setYPercent(0.5f);
        pathPoint.setAdjustScaleInScreen(0.5f);
        pathPoint.setAntiAlias(true);
        bsrPathPoints.add(pathPoint);

        BSRPathPoint pathPointHeart1 = new BSRPathPoint();
        pathPointHeart1.setRes(context, R.drawable.kiss_xin);
        pathPointHeart1.setDuring(during);
        pathPointHeart1.setPositionInScreen(true);
        pathPointHeart1.setPositionXPercent(0.5f);
        pathPointHeart1.setPositionYPercent(0.5f);
        pathPointHeart1.setPositionPoint(0.3f, 0.45f);
        pathPointHeart1.addScaleControl(0.0f);
        pathPointHeart1.addScaleControl(0.0f);
        pathPointHeart1.addScaleControl(0.0f);
        pathPointHeart1.addScaleControl(0.0f);
        pathPointHeart1.addScaleControl(0.8f);
        pathPointHeart1.addScaleControl(0.8f);
        pathPointHeart1.addScaleControl(0.8f);
        pathPointHeart1.addScaleControl(0.8f);
        pathPointHeart1.setXPercent(0.5f);
        pathPointHeart1.setYPercent(0.5f);
        pathPointHeart1.setAdjustScaleInScreen(0.21f);
        pathPointHeart1.setAntiAlias(true);
        bsrPathPoints.add(pathPointHeart1);

        BSRPathPoint pathPointHeart2 = new BSRPathPoint();
        pathPointHeart2.setRes(context, R.drawable.kiss_xin);
        pathPointHeart2.setDuring(during);
        pathPointHeart2.setPositionInScreen(true);
        pathPointHeart2.setPositionXPercent(0.5f);
        pathPointHeart2.setPositionYPercent(0.5f);
        pathPointHeart2.setPositionPoint(0.22f, 0.45f);
        pathPointHeart2.addScaleControl(0.0f);
        pathPointHeart2.addScaleControl(0.0f);
        pathPointHeart2.addScaleControl(0.0f);
        pathPointHeart2.addScaleControl(0.0f);
        pathPointHeart2.addScaleControl(0.0f);
        pathPointHeart2.addScaleControl(0.0f);
        pathPointHeart2.addScaleControl(0.0f);
        pathPointHeart2.addScaleControl(0.0f);
        pathPointHeart2.addScaleControl(0.5f);
        pathPointHeart2.addScaleControl(0.5f);
        pathPointHeart2.setXPercent(0.5f);
        pathPointHeart2.setYPercent(0.5f);
        pathPointHeart2.setAdjustScaleInScreen(0.17f);
        pathPointHeart2.setAntiAlias(true);
        bsrPathPoints.add(pathPointHeart2);

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setDuring(during * 2);

        giftLayout.addChild(bsrPathView);
        bsrGiftView.addBSRPathPoints(bsrPathPoints);
    }

    public void showShip() {
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        int during = 4000;
        bsrGiftView.setAlphaTrigger(0.99f);
        List<BSRPathPoint> bsrPathPoints = new ArrayList<>();

        BSRPathPoint pathPoint = new BSRPathPoint();
        pathPoint.setRes(context, R.drawable.ship02);
        pathPoint.setDuring(during);

        pathPoint.setPositionInScreen(true);
        pathPoint.addPositionControlPoint(0f, 0.6f);
        pathPoint.addPositionControlPoint(0.45f, 0.5f);
        pathPoint.addPositionControlPoint(0.45f, 0.5f);
        pathPoint.addPositionControlPoint(0.45f, 0.5f);
        pathPoint.addPositionControlPoint(0.45f, 0.5f);
        pathPoint.setPositionXPercent(0.5f);
        pathPoint.setPositionYPercent(0.5f);
        pathPoint.setAdjustScaleInScreen(1);
        pathPoint.addScaleControl(0.3f);
        pathPoint.addScaleControl(0.7f);
        pathPoint.addScaleControl(0.7f);
        pathPoint.addScaleControl(0.7f);
        pathPoint.addScaleControl(0.7f);
        pathPoint.setXPercent(0.5f);
        pathPoint.setYPercent(0.5f);
        pathPoint.setAntiAlias(true);
        bsrPathPoints.add(pathPoint);

        BSRPathPoint pathPoint2 = new BSRPathPoint();
        pathPoint2.setRes(context, R.drawable.ship01);
        pathPoint2.setDuring(during);
        pathPoint2.setPositionInScreen(true);
        pathPoint2.attachPoint(bsrPathPoints.get(0), 0, 0.265f);
        pathPoint2.setXPercent(0.5f);
        pathPoint2.setYPercent(0.5f);
        pathPoint2.setAdjustScaleInScreen(1);
        pathPoint2.addRotationControl(-2f);
        pathPoint2.addRotationControl(-2f);
        pathPoint2.addRotationControl(-2f);
        pathPoint2.addRotationControl(-2f);
        pathPoint2.addRotationControl(5f);
        pathPoint2.addRotationControl(5f);
        pathPoint2.addRotationControl(0f);
        pathPoint2.setAntiAlias(true);
        bsrPathPoints.add(pathPoint2);

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setDuring(during * 2);

        giftLayout.addChild(bsrPathView);
        bsrGiftView.addBSRPathPoints(bsrPathPoints);

    }
}
