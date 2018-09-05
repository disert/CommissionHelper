package com.meishai.commission.helper.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qipa.shop.adapter.EmptyAdapter
import com.qipa.shop.bean.ItemBean
import com.meishai.commission.helper.R
import com.meishai.commission.helper.bean.resault.ArticleListBean
import com.meishai.commission.helper.bean.resault.ArticleListResqBean
import kotlinx.android.synthetic.main.item_school.view.*

/**
 * 作者：yl
 * 时间: 2018年7月9日17:35:31
 * 功能：发现 分类 适配器
 */

class SchoolAdapter(mContext: Activity) : EmptyAdapter<RecyclerView.ViewHolder>(mContext) {

    private var mData: MutableList<ItemBean> = ArrayList()
    private val TYPE_GOODS = 104


    fun setData(data: List<ArticleListBean>?, allData: ArticleListResqBean?) {
        mData.clear()
        addData(data)
    }

    fun addData(data: List<ArticleListBean>?) {
        if (data != null) {
            for (item in data) {
                mData.add(ItemBean(TYPE_GOODS, item))
            }
        }
        setEmpty(mData.isEmpty())
    }

    override fun getItemType(position: Int): Int {

        return mData[position].type
    }

    /**
     * 这个方法是获取View
     */
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_school, parent, false)
        return ItemHolder(view)
    }

    /**
     * 这个方法是设置View
     */
    override fun onBindHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val data = mData[position].data
        when (holder) {
            is ItemHolder -> {
                val bean = if (data == null) null else data as ArticleListBean
                holder.init(super.mContext, bean)
            }
        }
    }

    override fun getCount(): Int {

        return mData.size
    }


    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var iv_item_img = view.iv_item_img
        private var tv_item_title = view.tv_item_title
        private var tv_item_content = view.tv_item_content
        private var tv_item_date = view.tv_item_date
        fun init(mContext: Activity, bean: ArticleListBean?) {
            if (bean == null) return
        }

    }


}
