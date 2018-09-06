package com.meishai.commission.helper.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import com.meishai.commission.helper.R
import com.meishai.commission.helper.base.BaseActivity
import com.meishai.commission.helper.bean.BuyVipBean
import com.meishai.commission.helper.utils.AndriodUtils
import com.meishai.commission.helper.utils.ToastManager
import com.meishai.commission.helper.view.DeepRadioGroup
import kotlinx.android.synthetic.main.activity_buy_vip.*
import kotlinx.android.synthetic.main.layout_top.*

/**
 * 作者：yl
 * 时间: 2018年1月16日17:05:55
 * 功能：手机充值的页面
 */
class BuyVipActivity : BaseActivity(), View.OnClickListener {

    private var balance: Int = 0
    private var payType: Int = 0
    private var charge: BuyVipBean? = null
    private var mData: List<BuyVipBean>? = null
    private var chargeNumber = 0
    internal var isUseGb: Boolean = false
    internal var isUseRemain: Boolean = false
    override val resId: Int = R.layout.activity_buy_vip

    override fun initData(savedInstanceState: Bundle?) {

        chargeNumber = intent.getIntExtra("chargeNumber", 0)
        tv_title.text = "开通服务"

        initEvent()

        loadData()
    }


    /**
     * 微信支付回调的事件
     *
     * @param resault
     */
//    @Subscribe(tags = arrayOf(Tag(value = Constants.BUS_TAG_WECHAT_PAY)), thread = EventThread.MAIN_THREAD)
    fun wechatPayResault(resault: String) {

        if ("0" == resault) {
            if (charge != null) {
//                startActivity(PaySucceedActivity.newIntent(mContext, charge!!.sell_price, getPayType(), PaySucceedActivity.SOURCE_LIJI_ORDER))
                finish()
            }
        } else if ("-1" == resault) {
            ToastManager.showShort(mContext, "支付失败")
        } else if ("-2" == resault) {
            ToastManager.showShort(mContext, "取消支付")
        }
    }

    private fun loadData() {
        val vipTimes = arrayListOf("1个月", "3个月", "6个月", "12个月", "终身")
        val vipPrice = arrayListOf(168, 380, 580, 780, 1800)
        val charges = ArrayList<BuyVipBean>()
        for (i in vipTimes.indices) {
            charges.add(BuyVipBean(vipPrice[i], vipTimes[i]))
        }
        initCharges(charges)
    }


    private fun initCharges(charges: List<BuyVipBean>) {

        mData = charges
        rg_recharge_number.removeAllViews()
        val params = DeepRadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndriodUtils.dip2px(mContext, 75f))
        params.leftMargin = AndriodUtils.dip2px(mContext, 10f)
        params.topMargin = AndriodUtils.dip2px(mContext, 5f)
        val vParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        vParams.rightMargin = params.leftMargin
        var i = 0
        while (i < charges.size) {
            val linear = LinearLayout(mContext)
            linear.orientation = LinearLayout.HORIZONTAL
            for (j in 0..2) {
                val button = View.inflate(mContext, R.layout.item_charge, null) as RadioButton
                button.id = 100 + j + i
                if (j + i >= charges.size) {
                    button.visibility = View.INVISIBLE
                } else {
                    button.visibility = View.VISIBLE
                    val bean = charges[j + i]
                    val s1 = bean.time
                    val s = "${bean.price}元"
                    val ssb = SpannableStringBuilder()
                    ssb.append(s1).append("\n").append(s)
                    AndriodUtils.onChangTextSize(mContext, ssb, s1.length + 1, ssb.length, 12)
                    AndriodUtils.onChangTextColor(ssb, s1.length + 1, ssb.length, AndriodUtils.getColor(R.color.color_999999))
                    button.text = ssb
                    if (bean.price == chargeNumber) {
                        button.isChecked = true
                    }
                }
                linear.addView(button, vParams)
            }
            rg_recharge_number.addView(linear, params)
            i += 3
        }

    }

    private fun initEvent() {
        iv_back.setOnClickListener(this)
        tv_buy.setOnClickListener(this)
        rg_recharge_number.setOnCheckedChangeListener { group, checkedId ->
            Log.w(TAG, "选中id=$checkedId")
            val curPos = checkedId - 100
            charge = mData?.get(curPos)
            //总计
            tv_buy.text = "支付${charge?.price}元"
        }
    }


    override fun onClick(view: View) {

        when (view.id) {
            R.id.iv_back -> finish()
            R.id.tv_buy -> if (chech(true)) {
                recharge()
            }
        }
    }

    private fun chech(checkPhone: Boolean): Boolean {

        if (charge == null || mData == null) {
            ToastManager.showShort(mContext, "请选择服务类型")
            return false
        }
        return true
    }



    private fun recharge() {

        pay(null)

    }

    private fun pay(pwd: String?) {
        //gid,useYue,mobile  payPwd payType device
//        if (charge == null) return
//        val utils = EncodingUtils.initInstance()
//                .put("gid", charge!!.gid)
//                .put("useYue", if (isUseRemain) 1 else 0)
//                .put("mobile", et_phone.text.toString())
//                .put("payType", if (payType == PAY_TYPE_WECHAT) 3 else payType)
//                .put("device", 2)
//        if (!TextUtils.isEmpty(pwd)) {
//            utils.put("payPwd", pwd)
//        }
//
//        val dataUtils = PostIdRequestDataUtils.getInstance(utils.params2Json())
//        ApiUtils.request(ApiUtils.getApiInterface().rechargePay(dataUtils.AESdata, dataUtils.id, dataUtils.timestamp, dataUtils.sign), object : BaseSubscriber<InviteListBean>() {
//
//            override fun onError(e: Throwable) {
//
//                PayUtlis.ctrlInfo(mContext, e) { message, state ->
//                    if (state == PayUtlis.PayState.SUCCESS) {
//                        if (charge != null) {
//                            startActivity(PaySucceedActivity.newIntent(mContext, charge!!.sell_price, getPayType(), PaySucceedActivity.SOURCE_LIJI_ORDER))
//                            finish()
//                        }
//                    } else {
//                        if (!TextUtils.isEmpty(message)) {
//                            ToastManager.showShort(mContext, message)
//                        }
//                    }
//                }
//            }
//        })

    }


    companion object {

        fun newIntent(mContext: Activity): Intent {

            return Intent(mContext, BuyVipActivity::class.java)
        }

    }
}
