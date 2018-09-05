package com.meishai.commission.helper.fragment

import android.os.Bundle
import android.view.View
import com.meishai.commission.helper.R
import com.meishai.commission.helper.base.BaseFragment
import com.meishai.commission.helper.base.BaseSubscriber
import com.meishai.commission.helper.bean.resault.ItemBean
import com.meishai.commission.helper.bean.resault.MallIndexBean
import com.meishai.commission.helper.bean.resault.MallListBean
import com.meishai.commission.helper.holder.CateHolder
import com.meishai.commission.helper.network.ApiUtils
import com.meishai.commission.helper.utils.EncodingUtils
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.item_home_cate.view.*

/**
 * 作者：yl
 * 时间: 2018年3月22日10:33:47
 * 功能：商铺分类页面
 */
class SearchFragment : BaseFragment(R.layout.fragment_search), View.OnClickListener {

    private var isFree = true
    override fun initData(savedInstanceState: Bundle?) {
        initEvent()
        loadData()
    }

    private fun loadData() {
        val list = ArrayList<MallListBean>()
        for (i in 0..10) {
            list.add(MallListBean("", "", "", "", ArrayList<ItemBean>()))
        }
        setData()
        isFree = false

        if (!isFree) return
        isFree = false
        val data = EncodingUtils.initInstance().base64
        ApiUtils.request(ApiUtils.getApiInterface()!!.mallIndex(data), object : BaseSubscriber<MallIndexBean>() {
            override fun onError(e: Throwable) {
                isFree = true
                super.onError(e)
            }

            override fun onNext(t: MallIndexBean?) {
                isFree = true
                setData()
            }
        })
    }

    private fun setData() {
        val cateHolder = CateHolder(rootView!!.ll_home_cate_root,mContext)
        cateHolder.init()
    }

    private fun initEvent() {
        rootView!!.tv_search_coupon.setOnClickListener(this)
        rootView!!.tv_search_taobao.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_search_coupon ->search()
            R.id.tv_search_taobao ->search()
        }

    }

    private fun search() {

    }

}
