package com.qipa.shop.fragment


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.meishai.commission.helper.R
import com.meishai.commission.helper.adapter.HomeAdapter
import com.meishai.commission.helper.base.BaseFragment
import com.meishai.commission.helper.base.BaseSubscriber
import com.meishai.commission.helper.bean.resault.ItemBean
import com.meishai.commission.helper.bean.resault.MallIndexBean
import com.meishai.commission.helper.bean.resault.MallListBean
import com.meishai.commission.helper.network.ApiUtils
import com.meishai.commission.helper.utils.AndriodUtils
import com.meishai.commission.helper.utils.EncodingUtils
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * 作者：yl
 * 时间: 2018年7月10日18:25:55
 * 功能：首页
 */

class HomeFragment : BaseFragment(R.layout.fragment_home), View.OnClickListener {


    private var mData: MutableList<MallListBean>? = null
    private var mAdapter: HomeAdapter? = null
    private var isFree = true
    private var hasMore = true
    private var mPage = 1


    override fun initData(savedInstanceState: Bundle?) {
        initEvent()
        loadData(mPage)
    }


    private fun loadData(page: Int) {
        val list = ArrayList<MallListBean>()
        for (i in 0..10) {
            list.add(MallListBean("", "", "", "", ArrayList<ItemBean>()))
        }
        mAdapter?.setData(list, null)
        isFree = false

        if (!isFree) return
        isFree = false
        val data = EncodingUtils.initInstance().put("cp", page).put("pp", 10).base64
        ApiUtils.request(ApiUtils.getApiInterface()!!.mallIndex(data), object : BaseSubscriber<MallIndexBean>() {
            override fun onError(e: Throwable) {
                isFree = true
                super.onError(e)
            }

            override fun onNext(t: MallIndexBean?) {
                isFree = true
                hasMore = !(t?.list == null || t.list.size < 10)
                mData = AndriodUtils.ctlData(mData, t?.list, page)
                if (page == 1) {
                    mAdapter?.setData(t?.list, t)
                } else {
                    mAdapter?.addData(t?.list)
                }
            }
        })
    }


    private fun initEvent() {
        mAdapter = HomeAdapter(mContext!!)
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        rootView?.rv_recycview?.layoutManager = layoutManager
        rootView?.rv_recycview?.adapter = mAdapter

        rootView?.iv_back_top?.setOnClickListener { rootView?.rv_recycview?.scrollToPosition(0) }
        rootView?.rv_recycview?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = recyclerView!!.childCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                //可以加载 并且也拉到了最后一个
                if (totalItemCount - visibleItemCount <= firstVisibleItem && mData != null && hasMore) {
                    if (!isFree) return
                    mPage++
                    loadData(mPage)
                }
                if(firstVisibleItem > 0){
                    rootView!!.iv_back_top.visibility = View.VISIBLE
                }else{
                    rootView!!.iv_back_top.visibility = View.GONE
                }
            }

        })
    }


    override fun onClick(v: View?) {

    }


}


