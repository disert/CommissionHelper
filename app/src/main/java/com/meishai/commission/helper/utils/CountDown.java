package com.meishai.commission.helper.utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * 作者：yl
 * 时间: 2017/8/10 11:51
 * 功能：倒计时
 */
public class CountDown {

    private Subscription mSubscription;
    private static CountDown mCountDown;
    private CountDown(){};
    public static CountDown getInstance(){
        if(mCountDown == null){
            mCountDown = new CountDown();
        }
        return mCountDown;
    }

    /**
     * 倒计时
     *
     * @param count 倒计时时间 单位:秒
     * @return 返回这个倒计时 让我们可以很方便的去结束它
     */
    public void countDowm(final int count, final CountDownListener listener) {

        mSubscription = Observable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
                .take(count + 1) //设置循环count+1次
                .map(new Func1<Long, Long>() {

                    @Override
                    public Long call(Long aLong) {

                        return count - aLong; //
                    }
                })
                .doOnSubscribe(new Action0() {

                    @Override
                    public void call() {
                        listener.onBefore();
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Subscriber<Long>() {

                    @Override
                    public void onCompleted() {
                        listener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        listener.onNext(aLong);
                    }
                });

    }

    public void unsubscribe(){
        if(mSubscription != null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
        mSubscription = null;
    }
    public static class SimpleCountDownListener implements CountDownListener{
        @Override
        public void onBefore() { }
        @Override
        public void onNext(long time) { }
        @Override
        public void onCompleted() {  }
        @Override
        public void onError(Throwable e) { }
    }

    public interface CountDownListener{
        void onBefore();
        void onNext(long time);
        void onCompleted();
        void onError(Throwable e);
    }

}
