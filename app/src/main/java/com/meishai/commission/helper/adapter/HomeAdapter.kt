package com.meishai.commission.helper.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.qipa.shop.adapter.EmptyAdapter
import com.qipa.shop.bean.ItemBean
import com.meishai.commission.helper.R
import com.meishai.commission.helper.bean.resault.MallIndexBean
import com.meishai.commission.helper.bean.resault.MallListBean
import com.meishai.commission.helper.holder.CateHolder
import com.meishai.commission.helper.holder.GoodsHolder
import kotlinx.android.synthetic.main.item_home_head.view.*
import java.util.*

/**
 * 作者：yl
 * 时间: 2018年7月10日17:13:55
 * 功能：首页 适配器
 */

class HomeAdapter(mContext: Activity) : EmptyAdapter<RecyclerView.ViewHolder>(mContext) {

    private var mData: MutableList<ItemBean> = ArrayList()
    private val TYPE_HEAD = 103
    private val TYPE_CATE = 104
    private val TYPE_ITEM = 105

    /**
     * 回掉成员变量
     */

    fun setData(data: List<MallListBean>?, bean: MallIndexBean?) {
        mData.clear()
        mData.add(ItemBean(TYPE_HEAD, bean?.banner))
        mData.add(ItemBean(TYPE_CATE, null))
        addData(data)
    }




    fun addData(data: List<MallListBean>?) {
        if (data != null) {
            for (item in data) {
                mData.add(ItemBean(TYPE_ITEM, item))
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemType(position: Int): Int {

        return mData[position].type
    }

    /**
     * 这个方法是获取View
     */
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            TYPE_HEAD//头
            -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_head, parent, false)
                return HeadHolder(view, super.mContext)
            }
            TYPE_CATE
            -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_cate, parent, false)
                return CateHolder(view, super.mContext)
            }
            else//推荐
            -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
                return GoodsHolder(view)
            }
        }
    }


    /**
     * 这个方法是设置View
     */
    override fun onBindHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val data = mData[position].data
        when (holder) {
            is HeadHolder -> {
                holder.init()
            }
            is CateHolder -> {
                holder.init()
            }
            is GoodsHolder -> {
                holder.init(super.mContext, data as? MallListBean)
            }
        }
    }

    override fun getCount(): Int {

        return mData.size
    }


    /**
     * 头
     */
    inner class HeadHolder(itemView: View, private val mContext: Activity) : RecyclerView.ViewHolder(itemView) {
        var mRlBanner: RelativeLayout = itemView.rl_banner

        fun init(){

        }
    }

}
