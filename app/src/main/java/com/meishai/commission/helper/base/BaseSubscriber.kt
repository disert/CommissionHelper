package com.meishai.commission.helper.base

import android.accounts.NetworkErrorException
import com.umeng.analytics.MobclickAgent
import com.meishai.commission.helper.GlobalContext
import com.meishai.commission.helper.network.ApiException
import com.meishai.commission.helper.utils.ToastManager
import rx.Subscriber
import rx.Subscription
import java.net.SocketException
import java.net.SocketTimeoutException


/**
 * 作者：yl
 * 时间: 2017/8/21 10:43
 * 功能：订阅者的基类 方便书写
 */
abstract class BaseSubscriber<T> : Subscriber<T>() {

    private var mSubscription: Subscription? = null

    fun setSubscription(subscription: Subscription) {
        mSubscription = subscription
    }

    override fun onCompleted() {
        if (mSubscription != null) mSubscription!!.unsubscribe()
    }

    override fun onError(e: Throwable) {
        if (mSubscription != null) mSubscription!!.unsubscribe()
        e.printStackTrace()
        if (e is ApiException)
            ToastManager.showShort(GlobalContext.context!!, e.getMsg())
        else if (e is SocketTimeoutException || e is SocketException || e is NetworkErrorException) {
            ToastManager.showShort(GlobalContext.context!!,"网络异常")
        } else {
            MobclickAgent.reportError(GlobalContext.context, e)
        }
    }

    override fun onNext(t: T?) {}
}
