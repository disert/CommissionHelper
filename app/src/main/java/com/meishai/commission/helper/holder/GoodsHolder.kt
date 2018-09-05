package com.meishai.commission.helper.holder

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.meishai.commission.helper.bean.resault.MallListBean
import kotlinx.android.synthetic.main.item_home.view.*

/**
 * 作者：yl
 * 时间: 2018/9/4 15:34
 * 功能：
 */
class GoodsHolder(view: View) : RecyclerView.ViewHolder(view) {

    val iv_item_img = view.iv_item_img
    val iv_item_shop = view.iv_item_shop
    val tv_item_title = view.tv_item_title
    val tv_item_yongjin = view.tv_item_yongjin
    val tv_item_bilv = view.tv_item_bilv
    val tv_item_zaishou = view.tv_item_zaishou
    val tv_item_xiaoliang = view.tv_item_xiaoliang
    val tv_item_quanhou = view.tv_item_quanhou
    val tv_item_coupon = view.tv_item_coupon
    val tv_item_share = view.tv_item_share
    fun init(mContext: Activity, bean: MallListBean?) {
        if (bean == null) return


    }

}