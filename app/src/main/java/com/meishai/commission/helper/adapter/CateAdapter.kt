package com.meishai.commission.helper.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.meishai.commission.helper.R
import com.meishai.commission.helper.bean.resault.MallListBean
import com.meishai.commission.helper.holder.GoodsHolder
import com.qipa.shop.adapter.EmptyAdapter
import java.util.*

/**
 * 作者：yl
 * 时间: 2018年7月10日17:13:55
 * 功能：首页 适配器
 */

class CateAdapter(mContext: Activity) : EmptyAdapter<GoodsHolder>(mContext) {

    private var mData: MutableList<MallListBean> = ArrayList()

    /**
     * 回掉成员变量
     */

    fun setData(data: List<MallListBean>?) {
        mData.clear()
        addData(data)
    }


    fun addData(data: List<MallListBean>?) {
        if (data != null) {
            mData.addAll(data)
        }
        notifyDataSetChanged()
    }


    /**
     * 这个方法是获取View
     */
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): GoodsHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return GoodsHolder(view)
    }


    /**
     * 这个方法是设置View
     */
    override fun onBindHolder(holder: GoodsHolder, position: Int) {

        val data = mData[position]
        holder.init(mContext, data)
    }

    override fun getCount(): Int {

        return mData.size
    }

}
