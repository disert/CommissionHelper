package com.qipa.shop.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.meishai.commission.helper.R
import com.meishai.commission.helper.activity.*
import com.meishai.commission.helper.base.BaseFragment
import com.meishai.commission.helper.view.InfoItemView
import com.meishai.commission.helper.view.InfoItemView.Companion.BOTTOM_TYPE_LINE
import com.meishai.commission.helper.view.InfoItemView.Companion.BOTTOM_TYPE_SPACE
import kotlinx.android.synthetic.main.fragment_my.view.*

/**
 * 作者：yl
 * 时间: 2018年7月9日14:53:02
 * 功能：我的
 */

class MyFragment : BaseFragment(R.layout.fragment_my), View.OnClickListener {


    override fun initData(savedInstanceState: Bundle?) {

        val safe = InfoItemView(mContext)
        rootView!!.ll_item_root.addView(safe)
        safe.getItemView("PID设置", "暂未设置", -1, BOTTOM_TYPE_LINE, View.OnClickListener {
            startActivity(PIDSetActivity.newIntent(mContext))//PID设置
        })

        val author = InfoItemView(mContext)
        rootView!!.ll_item_root.addView(author)
        author.getItemView("高佣授权", "暂未授权", -1, BOTTOM_TYPE_SPACE, View.OnClickListener {
            startActivity(AuthorActivity.newIntent(mContext))//高佣授权
        })
        val buy = InfoItemView(mContext)
        rootView!!.ll_item_root.addView(buy)
        buy.getItemView("购买服务", "暂未开通", -1, BOTTOM_TYPE_LINE, View.OnClickListener {
            startActivity(BuyVipActivity.newIntent(mContext))//购买服务
        })

        val bind = InfoItemView(mContext)
        rootView!!.ll_item_root.addView(bind)
        bind.getItemView("绑定手机", "暂未绑定", -1, BOTTOM_TYPE_SPACE, View.OnClickListener {
            startActivity(SetPhoneActivity.newIntent(mContext))//绑定手机
        })

        val help = InfoItemView(mContext)
        rootView!!.ll_item_root.addView(help)
        help.getItemView("使用帮助", "", -1, BOTTOM_TYPE_LINE, View.OnClickListener {
            startActivity(HelpListActivity.newIntent(mContext))//使用帮助
        })
        val service = InfoItemView(mContext)
        rootView!!.ll_item_root.addView(service)
        service.getItemView("联系客服", "", -1, BOTTOM_TYPE_LINE, View.OnClickListener {
            showCallOur()
        })

    }
    private fun showCallOur(){
        val dialog = Dialog(mContext, R.style.dialog_call_our)
        dialog.setContentView(R.layout.dialog_call_our)
        dialog.show()
    }

    override fun onClick(v: View?) {


    }

}


