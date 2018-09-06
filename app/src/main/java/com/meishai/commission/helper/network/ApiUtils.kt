package com.meishai.commission.helper.network

import android.accounts.NetworkErrorException
import android.text.TextUtils
import android.widget.TextView
import com.meishai.commission.helper.GlobalContext
import com.meishai.commission.helper.R
import com.meishai.commission.helper.base.BaseSubscriber
import com.meishai.commission.helper.bean.ResaultBean
import com.meishai.commission.helper.cons.Constants.REGEX_MOBILE
import com.meishai.commission.helper.utils.AndriodUtils
import com.meishai.commission.helper.utils.EncodingUtils
import com.meishai.commission.helper.utils.ToastManager
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.regex.Pattern

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

    /**
     * 公共接口 发送短信验证码
     *
     * @param phone
     * @param type
     * @param subscriber
     */
    fun sendSms(phone: TextView, type: Int, subscriber: Subscriber<String>) {
        //检测手机号
        val trim = phone.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(trim)) {
            ToastManager.showShort(GlobalContext.context!!, R.string.hint_input_phone)
            return
        }
        if (!Pattern.matches(REGEX_MOBILE, trim)) {
            ToastManager.showShort(GlobalContext.context!!, R.string.hint_input_valid_phone)
            return
        }
        val data = EncodingUtils.initInstance().put("phone", trim).put("type", type).getBase64()
        ApiUtils.request(ApiUtils.getApiInterface()!!.smsCode(data), subscriber)
    }

}
