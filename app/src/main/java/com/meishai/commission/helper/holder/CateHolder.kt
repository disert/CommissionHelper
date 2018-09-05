package com.meishai.commission.helper.holder

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.meishai.commission.helper.R
import com.meishai.commission.helper.utils.AndriodUtils
import kotlinx.android.synthetic.main.item_home_cate.view.*

/**
 * 作者：yl
 * 时间: 2018/9/4 15:33
 * 功能：
 */
class CateHolder(itemView: View, private val mContext: Activity) : RecyclerView.ViewHolder(itemView) {
    var cateRoot = itemView.ll_home_cate

    fun init() {
        val height = AndriodUtils.dip2px(mContext,70f)
        val params = LinearLayout.LayoutParams(0,height,1f)
        cateRoot.removeAllViews()
        val lParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1f)
        val list =  arrayListOf<Int>(0,1,2,3,4,5,6,7,8,9)
        var i = 0
        while (i < list.size){
            val linear = LinearLayout(mContext)
            cateRoot.addView(linear,lParams)
            for (j in 0 until 5){
                val view = View.inflate(mContext, R.layout.item_cate,null)
                linear.addView(view,params)
            }
            i += 5
        }

    }

}