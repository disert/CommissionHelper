package com.meishai.commission.helper.base

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.umeng.analytics.MobclickAgent
import com.meishai.commission.helper.R
import com.meishai.commission.helper.utils.AppManager
import com.meishai.commission.helper.utils.SystemBarTintManager
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers

/**
 * 作者 Administrator
 * 时间   2017/7/15 0015 14:28.
 * 描述   base类
 */

abstract class BaseActivity : AppCompatActivity() {

    protected val TAG = this.javaClass.simpleName
    protected lateinit var mContext: Activity

    abstract val resId: Int


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        AppManager.getAppManager().addActivity(this)
        mContext = this
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = Color.TRANSPARENT

        val layoutResID = resId
        if (layoutResID != 0) {//如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            setContentView(layoutResID)
        }
        if (SystemBarTintManager.StatusBarLightMode(mContext) != 0) {
            SystemBarTintManager.setStatusBarColor(mContext, R.color.white)
        } else {
            SystemBarTintManager.setStatusBarColor(mContext, R.color.black)
        }
        initData(savedInstanceState)
    }

    override fun onResume() {

        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {

        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {

        AppManager.getAppManager().removeActivity(this)
        super.onDestroy()
    }

    fun <T, R> runAsyncTask(t: T, action: Func1<T, R>) {

        runAsyncTask(t, action, Action1 { })
    }

    fun <T, R> runAsyncTask(t: T, func: Func1<T, R>, action: Action1<R>) {

        Observable.just(t)
                .map(func)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action)
    }

    override fun finish() {

        super.finish()
        overridePendingTransition(0, R.anim.finish_zoomout)
    }

    override fun startActivity(intent: Intent) {

        super.startActivity(intent)
        overridePendingTransition(R.anim.create_zoomin, 0)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {

        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.create_zoomin, R.anim.create_zoomout)
    }

    abstract fun initData(savedInstanceState: Bundle?)
}
