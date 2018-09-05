package com.meishai.commission.helper

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.qipa.shop.fragment.SchoolFragment
import com.qipa.shop.fragment.HomeFragment
import com.qipa.shop.fragment.MyFragment
import com.meishai.commission.helper.base.BaseActivity
import com.meishai.commission.helper.fragment.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),View.OnClickListener {

    private var mHome: HomeFragment? = null
    private var mPingTuan: SearchFragment? = null
    private var mFind: SchoolFragment? = null
    private var mMy: MyFragment? = null
    private val FRAGMENT_INDEX = "FRAGMENT_INDEX"
    private var currentTag = 0
    private var expTag = 0


    override val resId: Int = R.layout.activity_main
    override fun initData(savedInstanceState: Bundle?) {
        init(savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        tv_home.setOnClickListener(this)
        tv_pt.setOnClickListener(this)
        tv_find.setOnClickListener(this)
        tv_my.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_home->onShowTab(0)
            R.id.tv_pt->onShowTab(1)
            R.id.tv_find->onShowTab(2)
            R.id.tv_my->onShowTab(3)
        }
    }

    private fun init(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            mHome = HomeFragment()
            mPingTuan = SearchFragment()
            mFind = SchoolFragment()
            mMy = MyFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, mHome, HomeFragment::class.java.simpleName)
                    .commit()
            tv_home.isSelected = true
        } else {
            onRestoreFragment(savedInstanceState)
        }
    }

    protected override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(FRAGMENT_INDEX, currentTag)
    }
    /**
     * 功 能：重启时进行Fragment的找回,创建以及切换,解决Fragment的重叠问题 <br></br>
     * 时 间：2016/6/23 11:14 <br></br>
     */
    private fun onRestoreFragment(savedInstanceState: Bundle) {

        val home = supportFragmentManager.findFragmentByTag(HomeFragment::class.java
                .simpleName) as? HomeFragment
        val pt = supportFragmentManager.findFragmentByTag(SearchFragment::class.java
                .simpleName) as? SearchFragment
        val find = supportFragmentManager.findFragmentByTag(SchoolFragment::class.java
                .simpleName) as? SchoolFragment
        val my = supportFragmentManager.findFragmentByTag(MyFragment::class.java
                .simpleName) as? MyFragment
        val transaction = supportFragmentManager.beginTransaction()

        if (home != null) transaction.remove(home)
        if (pt != null) transaction.remove(pt)
        if (find != null) transaction.remove(find)
        if (my != null) transaction.remove(my)

        mHome = HomeFragment()
        mPingTuan = SearchFragment()
        mFind = SchoolFragment()
        mMy = MyFragment()

        val anInt = savedInstanceState.getInt(FRAGMENT_INDEX, 0)
        val currentFragment = getCurrentFragment(anInt)
        transaction
                .add(currentFragment, currentFragment!!.javaClass.simpleName)
                .commit()
        switchTab(anInt)
        currentTag = anInt

    }

    private fun getCurrentFragment(i: Int): Fragment? {

        when (i) {
            0 -> return mHome
            1 -> return mPingTuan
            2 -> return mFind
            3 -> return mMy
        }
        return null
    }

    private fun switchTab(anInt: Int) {

        if (currentTag == anInt) return
        when (anInt) {
            0 -> {
                tv_home.isSelected = true
                tv_pt.isSelected = false
                tv_find.isSelected = false
                tv_my.isSelected = false
            }
            1 -> {
                tv_home.isSelected = false
                tv_pt.isSelected = true
                tv_find.isSelected = false
                tv_my.isSelected = false
            }
            2 -> {
                tv_home.isSelected = false
                tv_pt.isSelected = false
                tv_find.isSelected = true
                tv_my.isSelected = false
            }
            3 -> {
                tv_home.isSelected = false
                tv_pt.isSelected = false
                tv_find.isSelected = false
                tv_my.isSelected = true
            }
        }

    }

    private fun onShowTab(i: Int) {

        if (currentTag == i) return
        val replace = getCurrentFragment(i)
        val current = getCurrentFragment(currentTag)
        if (replace == null) return
        if (!replace.isAdded) {
            supportFragmentManager.beginTransaction()
                    .hide(current)
                    .add(R.id.content, replace, replace.javaClass.simpleName)
                    .show(replace)
                    .commit()
        } else {
            supportFragmentManager.beginTransaction().hide(current).show(replace).commit()
        }

        switchTab(i)
        currentTag = i
        expTag = currentTag
    }
}
