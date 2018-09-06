package com.meishai.commission.helper.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.meishai.commission.helper.R
import com.meishai.commission.helper.base.BaseActivity
import kotlinx.android.synthetic.main.activity_author.*
import kotlinx.android.synthetic.main.layout_top.*

/**
 * Created by Administrator on 2017/8/1.
 *
 *
 * 修改密码
 */

class AuthorActivity : BaseActivity(), View.OnClickListener {

    override val resId: Int = R.layout.activity_author
    override fun initData(savedInstanceState: Bundle?) {
        iv_back.setOnClickListener(this)
        tv_commit.setOnClickListener(this)
    }


    override fun onClick(view: View) {

        when (view.id) {
            R.id.iv_back -> finish()
            R.id.tv_commit ->  {
                requestSet()
            }
        }
    }

    private fun requestSet() {
        
    }



    companion object {
        fun newIntent(context: Activity): Intent {
            return Intent(context, AuthorActivity::class.java)
        }
    }
}
