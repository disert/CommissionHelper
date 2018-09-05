package com.meishai.commission.helper.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.meishai.commission.helper.R
import com.meishai.commission.helper.activity.CateActivity.Companion.ORDER_TYPE
import com.meishai.commission.helper.adapter.CateAdapter
import com.meishai.commission.helper.base.BaseFragment
import com.meishai.commission.helper.base.BaseSubscriber
import com.meishai.commission.helper.bean.resault.ItemBean
import com.meishai.commission.helper.bean.resault.MallIndexBean
import com.meishai.commission.helper.bean.resault.MallListBean
import com.meishai.commission.helper.cons.Constants.PAGE_COUNT
import com.meishai.commission.helper.network.ApiUtils
import com.meishai.commission.helper.utils.EncodingUtils
import kotlinx.android.synthetic.main.fragment_recycler.view.*

/**
 * Created by Administrator on 2017/7/25.
 *
 *
 * 我的订单
 */

class CateFragemt : BaseFragment(R.layout.fragment_recycler) {

    private var type = -1
    private var mData: List<MallListBean>? = null
    private var mAdapter: CateAdapter? = null
    private var mKey: String? = null
    internal var isFree = true
    private var layoutManager: LinearLayoutManager? = null
    private var hasMore = true
    private val RESULT_INVOICE = "RESULT_INVOICE"
    private var mGid: String? = null

    internal var mPage = 1


    override fun initData(savedInstanceState: Bundle?) {

        layoutManager = LinearLayoutManager(context)
        rootView!!.rv_recycview.layoutManager = layoutManager
        type = arguments!!.getInt(ORDER_TYPE)
        mAdapter = CateAdapter(mContext)
        rootView!!.rv_recycview.adapter = mAdapter
        rootView!!.srl_refresh.setOnRefreshListener {
            if (isFree) {
                updateData()
            } else {
                rootView!!.srl_refresh.isRefreshing = false
            }
        }
        rootView!!.rv_recycview.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {

                val totalItemCount = layoutManager!!.itemCount
                val visibleItemCount = recyclerView!!.childCount
                val firstVisibleItem = layoutManager!!.findFirstVisibleItemPosition()
                //可以加载 并且也拉到了最后一个
                if (totalItemCount - visibleItemCount <= firstVisibleItem && mData != null && hasMore) {
                    if (!isFree) return
                    mPage++
                    loadData(mPage)
                }
            }
        })
        loadData(mPage)

    }
    fun keyChange(key: String){
        mKey = key
        updateData()
    }


    private fun updateData() {
        if (isFree) {
            hasMore = true
            mPage = 1
            loadData(mPage)
        }
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            //这里可能会出现一个bug,就是它的调用 先与oncreateview方法的调用
//            if (type == -1) type = arguments!!.getInt(ORDER_TYPE)
//            if (isFree) {
//                updateData()
//            }
        }
    }

    private fun loadData(page: Int) {
        val list = ArrayList<MallListBean>()
        for (i in 0..10) {
            list.add(MallListBean("", "", "", "", ArrayList<ItemBean>()))
        }
        mAdapter?.setData(list)
        isFree = false


        if (!isFree) return
        isFree = false
        val subscriber = object : BaseSubscriber<MallIndexBean>() {

            override fun onError(e: Throwable) {

                super.onError(e)
                isFree = true
                if (rootView!!.srl_refresh == null) return
                rootView!!.srl_refresh.isRefreshing = false
                mAdapter!!.setEmpty(true)
            }

            override fun onNext(bean: MallIndexBean?) {
                isFree = true
                rootView?.srl_refresh?.isRefreshing = false


            }
        }
        val json = EncodingUtils.initInstance().put("currentPage", page).put("perPage", PAGE_COUNT).put("key",mKey).params2Json()
        ApiUtils.request(ApiUtils.getApiInterface()!!.mallIndex(json), subscriber)

    }

}
