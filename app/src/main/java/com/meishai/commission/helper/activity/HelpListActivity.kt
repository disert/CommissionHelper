package com.meishai.commission.helper.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.meishai.commission.helper.R
import com.meishai.commission.helper.base.BaseActivity
import kotlinx.android.synthetic.main.activity_recycler.*
import kotlinx.android.synthetic.main.layout_top.*

/**
 * Created by Administrator on 2017/7/22.
 *
 *
 * 任务中心页面
 */

class HelpListActivity : BaseActivity() {

//    private var mAdapter: TaskCenterAdapter? = null

    private var isFree = true
    private var layoutManager: LinearLayoutManager? = null
    override val resId: Int = R.layout.activity_recycler
    override fun initData(savedInstanceState: Bundle?) {

        tv_title.text = "任务中心"
        iv_back.setOnClickListener { finish() }
        layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        rv_recycview.layoutManager = layoutManager
//        mAdapter = TaskCenterAdapter(mContext)
//        rv_recycview.adapter = mAdapter

        initEvent()

    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun initEvent() {

        srl_refresh?.setOnRefreshListener {
            if (isFree) {
                loadData()
            } else {
                srl_refresh!!.isRefreshing = false
            }
        }

    }

    private fun loadData() {

//        if (!isFree) return
//        isFree = false

//        val instance = PostIdRequestDataUtils.getInstance()
//        ApiUtils.request(ApiUtils.getApiInterface().getTaskList(instance.id, instance.timestamp, instance.sign), object : BaseSubscriber<TaskListBean>() {
//
//            override fun onError(e: Throwable) {
//
//                isFree = true
//                srl_refresh?.isRefreshing = false
//                super.onError(e)
//                mAdapter!!.setEmpty(true)
//            }
//
//            override fun onNext(bean: TaskListBean?) {
//
//                isFree = true
//                srl_refresh?.isRefreshing = false
//                mAdapter?.setData(bean)
//            }
//        })

    }

    companion object {

        fun newIntent(mContext: Activity): Intent {

            return Intent(mContext, HelpListActivity::class.java)
        }
    }
}
