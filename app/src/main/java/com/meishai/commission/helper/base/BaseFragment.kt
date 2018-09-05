package com.meishai.commission.helper.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Fragment基类
 * 1. 初始化布局 initView
 * 2. 初始化数据 setBloackResId
 *
 * @author Ace
 * @date 2016-2-11
 */
abstract class BaseFragment(val resId: Int) : Fragment() {

    var TAG = this.javaClass.simpleName
    var rootView: View? = null
    lateinit var mContext: Activity



    override fun onAttach(context: Context) {

        super.onAttach(context)
        mContext = context as Activity
    }


    /**
     * setContentView;
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (rootView != null) {
            val parent = rootView!!.parent as? ViewGroup
            parent?.removeView(rootView)
            return rootView
        }
        val layoutResID = resId
        if (layoutResID != 0) {//如果initView返回0,view就是空的,我们就不需要去注入view
            rootView = inflater.inflate(resId, container, false)
        }

        initData(savedInstanceState)
        return rootView
    }


    override fun startActivity(intent: Intent) {

        mContext!!.startActivity(intent)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {

        mContext!!.startActivityForResult(intent, requestCode)
    }

    /**
     * 初始化数据
     */
    abstract fun initData(savedInstanceState: Bundle?)

}
