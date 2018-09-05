package com.meishai.commission.helper.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.meishai.commission.helper.R
import com.meishai.commission.helper.base.BaseActivity
import com.meishai.commission.helper.utils.AndriodUtils
import kotlinx.android.synthetic.main.activity_goods_details.*
import kotlinx.android.synthetic.main.layout_top.*

/**
 * Created by Administrator on 2017/7/21.
 *
 *
 * 齐啪购商品详情
 */

class GoodsDetailsActivity : BaseActivity(), View.OnClickListener {


    //报名详情接口返回的数据

    private var isShowPics = true
    var mGid: String? = null
    override val resId: Int = R.layout.activity_goods_details
    override fun initData(savedInstanceState: Bundle?) {

        //在这里接受传过来的参数
        val intent = intent
        mGid = intent.getStringExtra(GID)
        initEvent()

        val realWH = AndriodUtils.getViewRealWH(1, 0, 100.0, 100.0)
        rl_banner.layoutParams = LinearLayout.LayoutParams(realWH.x, realWH.y)

        //加载数据
        loadData()
    }





    private fun loadData() {

        //        String json = EncodingUtils.initInstance().put(GID, "103").params2Json();
//        val json = EncodingUtils.initInstance().put(GID, mGid).params2Json()
//        ApiUtils.request(ApiUtils.getApiInterface().qipaDetails(bean.AESdata, bean.id, bean.timestamp, bean.sign), object : BaseSubscriber<QipaDetailsBean>() {
//
//            override fun onError(e: Throwable) {
//
//                super.onError(e)
//            }
//
//            override fun onNext(bean: QipaDetailsBean) {
//
//                initData(bean)
//            }
//        })

    }


    private fun initEvent() {
        iv_back.setOnClickListener(this)
        tv_commit.setOnClickListener(this)
        tv_commit.setOnClickListener(this)
    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.iv_back -> finish()
            R.id.tv_commit -> {
            }
        }
    }




    companion object {
        val GID = "id"

        fun newIntent(mContext: Activity, gid: String): Intent {

            val intent = Intent(mContext, GoodsDetailsActivity::class.java)
            intent.putExtra(GID, gid)
            return intent
        }
    }

}