package com.meishai.commission.helper.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View

import com.meishai.commission.helper.R
import com.meishai.commission.helper.base.BaseActivity
import com.meishai.commission.helper.utils.ToastManager
import kotlinx.android.synthetic.main.activity_pid_set.*
import kotlinx.android.synthetic.main.layout_top.*

/**
 * Created by Administrator on 2017/8/1.
 *
 *
 * 修改密码
 */

class PIDSetActivity : BaseActivity(), View.OnClickListener {

    override val resId: Int = R.layout.activity_pid_set
    override fun initData(savedInstanceState: Bundle?) {
        iv_back.setOnClickListener(this)
        tv_commit.setOnClickListener(this)
    }


    override fun onClick(view: View) {

        when (view.id) {
            R.id.iv_back -> finish()
            R.id.tv_commit -> if (check()) {
                requestSet()
            }
        }
    }

    private fun requestSet() {

    }


    private fun check(): Boolean {
        val name = et_pid_name.text.toString()
        if (TextUtils.isEmpty(name)) {
            ToastManager.showShort(mContext, "请输入推广位名称")
            return false
        }
        val pid = et_pid_id.text.toString()
        if (TextUtils.isEmpty(pid)) {
            ToastManager.showShort(mContext, "请输入推广位PID")
            return false
        }
        return true
    }

    companion object {
        fun newIntent(context: Activity): Intent {
            return Intent(context, PIDSetActivity::class.java)
        }
    }
}
