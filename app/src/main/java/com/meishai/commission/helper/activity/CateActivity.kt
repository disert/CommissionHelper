package com.meishai.commission.helper.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.meishai.commission.helper.R
import com.meishai.commission.helper.base.BaseActivity
import com.meishai.commission.helper.fragment.CateFragemt
import kotlinx.android.synthetic.main.activity_shops.*
import java.util.*


/**
 * 作者：yl
 * 时间: 2018年1月12日16:33:10
 * 功能：商户页面
 */
class CateActivity : BaseActivity() {


    private val list = ArrayList<CateFragemt>()
    private var mid: String? = null
    private val titles = arrayListOf("推荐","销量↓","价格↑","佣金↓")

    override val resId: Int  = R.layout.activity_shops


    override fun initData(savedInstanceState: Bundle?) {

        initEvent()
        initData()

    }




    fun search(): Boolean {

        val key = search.text.toString().trim { it <= ' ' }
        if (!TextUtils.isEmpty(key) && !list.isEmpty() && viewpager != null) {
            list[viewpager.currentItem].keyChange(key)
            return true
        }
        return false
    }

    private fun initData() {

        mid = intent.getStringExtra("mid")
        val title = intent.getStringExtra("title")
        //        mTvTitle.setText(title);
        for (i in 0 until 4){
            val mAllFragment = CateFragemt()
            val args = Bundle()
            args.putInt(ORDER_TYPE, i)
            mAllFragment.arguments = args
            list.add(mAllFragment)
        }



        viewpager.adapter = MyAdapter(supportFragmentManager)
        viewpager.offscreenPageLimit = 4
        viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {

                search()
            }
        })
    }

    private fun initEvent() {

        search.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
                return@OnEditorActionListener true
            }
            false
        })
        iv_back.setOnClickListener{finish()}

    }


    internal inner class MyAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {

            return list[position]
        }

        override fun getCount(): Int {

            return list.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

    }

    companion object {
        const val ORDER_TYPE = "ORDER_TYPE"

        fun newIntent(context: Context, sid: String, title: String): Intent {

            val intent = Intent(context, CateActivity::class.java)
            intent.putExtra("mid", sid)
            intent.putExtra("title", title)
            return intent
        }
    }

}
