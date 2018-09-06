package com.meishai.commission.helper.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.meishai.commission.helper.R
import com.meishai.commission.helper.base.BaseActivity
import com.meishai.commission.helper.cons.Constants.REGEX_MOBILE
import com.meishai.commission.helper.network.ApiUtils
import com.meishai.commission.helper.utils.CountDown
import com.meishai.commission.helper.utils.EncodingUtils
import com.meishai.commission.helper.utils.ToastManager
import kotlinx.android.synthetic.main.activity_set_phone.*
import kotlinx.android.synthetic.main.layout_top.*
import rx.Subscriber
import java.util.regex.Pattern

/**
 * Created by Administrator on 2017/8/1.
 *
 *
 * 修改手机号
 */

class SetPhoneActivity : BaseActivity(), View.OnClickListener {

    private var type: Int = 0

    internal var mCountDown: CountDown? = CountDown.getInstance()
    internal var count = 60
//    private var uuid: String? = null
//    private var url = Constants.BASE_URL + PATH_GETCAPTCHA + "?data="

    override val resId: Int = R.layout.activity_set_phone


    override fun initData(savedInstanceState: Bundle?) {

        tv_title.text = "绑定手机"
        v_sms_space.visibility = View.GONE


        initEvent()
    }


    private fun check(): Boolean {

        val phone = et_phone.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(phone)) {
            ToastManager.showShort(mContext, R.string.hint_input_phone)
            return false
        }
        if (!Pattern.matches(REGEX_MOBILE, phone)) {
            ToastManager.showShort(mContext, R.string.hint_input_valid_phone)
            return false
        }
        val smsCode = et_sms_code.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(smsCode)) {
            ToastManager.showShort(mContext, R.string.hint_input_msg_code)
            return false
        }
        return true
    }

    private fun sendSms() {
        val t = 2
        ApiUtils.sendSms(et_phone, t, object : Subscriber<String>() {

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

                e.printStackTrace()
                ToastManager.showShort(mContext, e.message?: "")
            }

            override fun onNext(resaultBean: String?) {

                ToastManager.showShort(mContext, R.string.toast_send_msg_success)
                countDown()
            }
        })

    }

    private fun countDown() {

        mCountDown!!.countDowm(count, object : CountDown.CountDownListener {

            override fun onBefore() {

                tv_send_sms.isEnabled = false
            }

            override fun onNext(time: Long) {

                tv_send_sms.text = "$time 秒"
            }

            override fun onCompleted() {

                count = 60
                tv_send_sms.isEnabled = true
                tv_send_sms.text = "重新获取"
                mCountDown!!.unsubscribe()
            }

            override fun onError(e: Throwable) {

                e.printStackTrace()
                count = 60
                mCountDown!!.unsubscribe()
            }
        })

    }

    override fun onDestroy() {

        if (mCountDown != null)
            mCountDown!!.unsubscribe()
        super.onDestroy()
    }

    private fun initEvent() {
        iv_back.setOnClickListener(this)
        tv_send_sms.setOnClickListener(this)
        tv_commit.setOnClickListener(this)

    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.iv_back -> finish()
            R.id.tv_send_sms -> sendSms()
            R.id.tv_commit -> if (check()) {
                bindPhone()
            }
        }
    }



    private fun bindPhone() {
        val phone = et_phone.text.toString().trim { it <= ' ' }
        val smsCode = et_sms_code.text.toString().trim { it <= ' ' }
        val data = EncodingUtils.initInstance().put("phone", phone).put("smsCode", smsCode).params2Json()
//        val instance = PostIdRequestDataUtils.getInstance(data)
//        ApiUtils.request(ApiUtils.getApiInterface().bindPhone(instance.AESdata, instance.id, instance.timestamp, instance.sign), object : BaseSubscriber<String>() {
//
//            override fun onError(e: Throwable) {
//
//                super.onError(e)
//            }
//
//            override fun onNext(s: String?) {
//                UserDataManager.getInstance().mobile = phone
//                ToastManager.showShort(mContext, R.string.bind_phone_success)
//                finish()
//            }
//        })

    }


    companion object {

        fun newIntent(mContext: Activity): Intent {
            return Intent(mContext, SetPhoneActivity::class.java)
        }
    }
}
