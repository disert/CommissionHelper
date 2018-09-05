package com.qipa.shop.fragment


import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.meishai.commission.helper.R
import com.meishai.commission.helper.adapter.SchoolAdapter
import com.meishai.commission.helper.base.BaseFragment
import com.meishai.commission.helper.base.BaseSubscriber
import com.meishai.commission.helper.bean.resault.ArticleListBean
import com.meishai.commission.helper.bean.resault.ArticleListResqBean
import com.meishai.commission.helper.cons.Constants
import com.meishai.commission.helper.cons.Constants.PAGE_COUNT
import com.meishai.commission.helper.network.ApiUtils
import com.meishai.commission.helper.utils.EncodingUtils
import kotlinx.android.synthetic.main.fragment_recycler.view.*

/**
 * 作者：yl
 * 时间: 2018年7月9日17:33:13
 * 功能：发现 分类
 */

class SchoolFragment : BaseFragment(R.layout.fragment_recycler) {

    private var mData: ArticleListResqBean? = null
    private var mAdapter: SchoolAdapter? = null
    private var isFree = true
    private var layoutManager: LinearLayoutManager? = null
    private var hasMore = true
    private var mPage = 1

    fun refresh(layout: SwipeRefreshLayout?) {
        layout?.isRefreshing = false
        if (!isFree) return
        mPage = 1
        hasMore = true
        loadData(mPage)
    }


    override fun initData(savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        //        layoutManager.setGapStrategy(GAP_HANDLING_NONE);
        rootView?.rv_recycview?.layoutManager = layoutManager
        mAdapter = SchoolAdapter(mContext)
        rootView?.rv_recycview?.adapter = mAdapter
        rootView?.rv_recycview?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {

                if (hasMore && rootView?.rv_recycview?.canScrollVertically(1) == false && mData != null) {
                    if (!isFree) return
                    mPage++
                    loadData(mPage)
                }
            }
        })

        loadData(mPage)

    }


    private fun loadData(page: Int) {
        val list = ArrayList<ArticleListBean>()
        for (i in 0..10) {
            list.add(ArticleListBean("", "", "", "","",""))
        }
        mAdapter?.setData(list, null)
        isFree = false


        if(!isFree) return
        isFree = false
        val data = EncodingUtils.initInstance().put("cp",page).put("pp", Constants.PAGE_COUNT).base64
        ApiUtils.request(ApiUtils.getApiInterface()!!.articleList(data),object : BaseSubscriber<ArticleListResqBean>(){
            override fun onError(e: Throwable) {
                isFree = true
                super.onError(e)
            }
            override fun onNext(t: ArticleListResqBean?) {
                isFree = true
                mData = t
                hasMore = !(t?.list == null || t.list.size < PAGE_COUNT)
                mAdapter?.setBottom(!hasMore)
                if(page == 1){
                    mAdapter?.setData(t?.list,t)
                }else{
                    mAdapter?.addData(t?.list)
                }
            }
        })
    }


}


