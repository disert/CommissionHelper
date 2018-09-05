package com.meishai.commission.helper.network

import android.accounts.NetworkErrorException
import com.meishai.commission.helper.GlobalContext
import com.meishai.commission.helper.base.BaseSubscriber
import com.meishai.commission.helper.bean.ResaultBean
import com.meishai.commission.helper.utils.AndriodUtils
import com.meishai.commission.helper.utils.ToastManager
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 作者 Administrator
 * 时间   2017/7/12 0012 18:13.
 * 描述   网络请求的工具类
 */

object ApiUtils {

    fun getApiInterface(): ApiInterface? {
        return GlobalContext.context!!.getAPIInterface()
    }


    /**
     * 用于发送http请求的方法,该方法会返回一个没有去掉公共部分的bean对象
     *
     * @param observable
     * @param subscriber
     * @param <T>
    </T> */
    fun <T> request(observable: Observable<ResaultBean<T>>, subscriber: Subscriber<T>) {

        if (!AndriodUtils.isNetworkAvailable(GlobalContext.context!!)) {
            ToastManager.showShort(GlobalContext.context!!, "")
            subscriber.onError(NetworkErrorException(""))
            return
        }
        val subscribe = observable.subscribeOn(Schedulers.io())
                .onBackpressureDrop()//防止MissingBackpressureException异常,把来不及处理的数据给丢掉
                .map(HttpResultFunc<T>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
        if (subscriber is BaseSubscriber<*>)
            subscriber.setSubscription(subscribe)

    }

}
