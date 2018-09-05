package com.meishai.commission.helper.view

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.meishai.commission.helper.R
import com.meishai.commission.helper.utils.AndriodUtils
import java.util.*

/**
 * 作者：yl
 * 时间: 2017/8/11 20:40
 * 功能：实现轮播图的控件,来自GitHub: https://github.com/loonggg/RecyclerViewBanner
 */
class RecyclerViewBanner @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var mInterval: Int = 0
    private var isShowIndicator: Boolean = false
    private var mSelectedDrawable: Drawable? = null
    private var mUnselectedDrawable: Drawable? = null
    private val mSize: Int
    private val mSpace: Int

    /**
     * 获取RecyclerView实例，便于满足自定义[RecyclerView.ItemAnimator]或者[RecyclerView.Adapter]的需求
     *
     * @return RecyclerView实例
     */
    private var recyclerView: RecyclerView? = null
    private var mLinearLayout: LinearLayout? = null

    private val adapter: RecyclerAdapter?
    private var onRvBannerClickListener: OnRvBannerClickListener? = null
    private var onSwitchRvBannerListener: OnSwitchRvBannerListener? = null
    private val mData: MutableList<Any> = ArrayList<Any>()
    private var startX: Int = 0
    private var startY: Int = 0
    private var currentIndex: Int = 0
    private var isPlaying: Boolean = false
    private val mHandler = Handler()
    private var isTouched: Boolean = false
    private var isAutoPlaying = true
    private var mScaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_XY

    private val playTask = object : Runnable {

        override fun run() {

            recyclerView!!.smoothScrollToPosition(++currentIndex)
            if (isShowIndicator) {
                switchIndicator()
            }
            mHandler.postDelayed(this, mInterval.toLong())
        }
    }

    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerCoverBanner)
        mInterval = a.getInt(R.styleable.RecyclerCoverBanner_rvb_interval, 3000)
        isShowIndicator = a.getBoolean(R.styleable.RecyclerCoverBanner_rvb_showIndicator, true)
        isAutoPlaying = a.getBoolean(R.styleable.RecyclerCoverBanner_rvb_autoPlaying, true)
        val sd = a.getDrawable(R.styleable.RecyclerCoverBanner_rvb_indicatorSelectedSrc)
        val usd = a.getDrawable(R.styleable.RecyclerCoverBanner_rvb_indicatorUnselectedSrc)
        mSelectedDrawable = if (sd == null) {
            generateDefaultDrawable(DEFAULT_SELECTED_COLOR, true)
        } else {
            if (sd is ColorDrawable) {
                generateDefaultDrawable(sd.color, true)
            } else {
                sd
            }
        }
        if (usd == null) {
            mUnselectedDrawable = generateDefaultDrawable(DEFAULT_UNSELECTED_COLOR, false)
        } else {
            if (usd is ColorDrawable) {
                mUnselectedDrawable = generateDefaultDrawable(usd.color, false)
            } else {
                mUnselectedDrawable = usd
            }
        }
        mSize = a.getDimensionPixelSize(R.styleable.RecyclerCoverBanner_rvb_indicatorSize, 0)
        mSpace = a.getDimensionPixelSize(R.styleable.RecyclerCoverBanner_rvb_indicatorSpace, dp2px(4))
        val margin = a.getDimensionPixelSize(R.styleable.RecyclerCoverBanner_rvb_indicatorMargin, dp2px(8))
        val g = a.getInt(R.styleable.RecyclerCoverBanner_rvb_indicatorGravity, 1)
        val gravity: Int
        if (g == 0) {
            gravity = GravityCompat.START
        } else if (g == 2) {
            gravity = GravityCompat.END
        } else {
            gravity = Gravity.CENTER
        }
        a.recycle()

        recyclerView = RecyclerView(context)
        mLinearLayout = LinearLayout(context)

        PagerSnapHelper().attachToRecyclerView(recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = RecyclerAdapter()
        recyclerView!!.adapter = adapter
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {

                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val first = (recyclerView!!.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    val last = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (first == last && currentIndex != last) {
                        currentIndex = last
                        if (isShowIndicator && isTouched) {
                            isTouched = false
                            switchIndicator()
                        }
                    }
                }
            }
        })
        mLinearLayout!!.orientation = LinearLayout.HORIZONTAL
        mLinearLayout!!.gravity = Gravity.CENTER

        val vpLayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        val linearLayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayoutParams.gravity = Gravity.BOTTOM or gravity
        linearLayoutParams.setMargins(margin, margin, margin, margin)
        addView(recyclerView, vpLayoutParams)
        addView(mLinearLayout, linearLayoutParams)

        // 便于在xml中编辑时观察，运行时不执行
        if (isInEditMode) {
            for (i in 0..2) {
                mData.add("")
            }
            createIndicators()
        }
    }


    fun requestRefrush() {
        if (adapter != null && recyclerView != null) adapter.notifyDataSetChanged()
    }

    fun setScaleType(scaleType: ImageView.ScaleType) {
        mScaleType = scaleType
    }

    /**
     * 默认指示器是一系列直径为6dp的小圆点
     */
    private fun generateDefaultDrawable(color: Int, isSelect: Boolean): GradientDrawable {

        val gradientDrawable = GradientDrawable()
        val px = dp2px(3)
        if (isSelect) {
            gradientDrawable.setSize(dp2px(16), px)
        } else
            gradientDrawable.setSize(dp2px(16), px)
        gradientDrawable.cornerRadius = px.toFloat()
        gradientDrawable.setColor(color)
        return gradientDrawable
    }

    /**
     * 设置是否显示指示器导航点
     *
     * @param show 显示
     */
    fun isShowIndicator(show: Boolean) {

        this.isShowIndicator = show
    }

    /**
     * 设置轮播间隔时间
     *
     * @param millisecond 时间毫秒
     */
    fun setIndicatorInterval(millisecond: Int) {

        this.mInterval = millisecond
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    @Synchronized
    private fun setPlaying(playing: Boolean) {

        if (isAutoPlaying) {
            if (!isPlaying && playing && adapter != null && adapter.itemCount > 2) {
                mHandler.postDelayed(playTask, mInterval.toLong())
                isPlaying = true
            } else if (isPlaying && !playing) {
                mHandler.removeCallbacksAndMessages(null)
                isPlaying = false
            }
        }
    }

    /**
     * 设置是否禁止滚动播放
     *
     * @param isAutoPlaying true  是自动滚动播放,false 是禁止自动滚动
     */
    fun setRvAutoPlaying(isAutoPlaying: Boolean) {

        this.isAutoPlaying = isAutoPlaying
    }

    /**
     * 设置轮播数据集
     *
     * @param data Banner对象列表
     */
    fun <T : Any> setRvBannerData(data: MutableList<T>?) {

        setPlaying(false)
        mData.clear()
        if (data != null) {
            mData.addAll(data)
        }
        if (mData.size > 1) {
            currentIndex = mData.size
            adapter!!.notifyDataSetChanged()
            recyclerView!!.scrollToPosition(currentIndex)
            if (isShowIndicator) {
                createIndicators()
            }
            setPlaying(true)
        } else {
            currentIndex = 0
            adapter!!.notifyDataSetChanged()
        }
    }

    /**
     * 指示器整体由数据列表容量数量的AppCompatImageView均匀分布在一个横向的LinearLayout中构成
     * 使用AppCompatImageView的好处是在Fragment中也使用Compat相关属性
     */
    private fun createIndicators() {

        mLinearLayout?.removeAllViews()
        for (i in mData.indices) {
            val img = AppCompatImageView(context)
            val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.leftMargin = mSpace / 2
            lp.rightMargin = mSpace / 2
            if (mSize >= dp2px(4)) { // 设置了indicatorSize属性
                lp.height = mSize
                lp.width = lp.height
            } else {
                // 如果设置的resource.xml没有明确的宽高，默认最小2dp，否则太小看不清
                img.minimumWidth = dp2px(2)
                img.minimumHeight = dp2px(2)
            }
            img.setImageDrawable(if (i == 0) mSelectedDrawable else mUnselectedDrawable)
            mLinearLayout?.addView(img, lp)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //手动触摸的时候，停止自动播放，根据手势变换
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = ev.x.toInt()
                val moveY = ev.y.toInt()
                val disX = moveX - startX
                val disY = moveY - startY
                val hasMoved = 2 * Math.abs(disX) > Math.abs(disY)
                parent.requestDisallowInterceptTouchEvent(hasMoved)
                if (hasMoved) {
                    setPlaying(false)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (!isPlaying) {
                isTouched = true
                setPlaying(true)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onAttachedToWindow() {

        super.onAttachedToWindow()
        setPlaying(true)
    }

    override fun onDetachedFromWindow() {

        super.onDetachedFromWindow()
        setPlaying(false)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {

        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            // 停止轮播
            setPlaying(false)
        } else if (visibility == View.VISIBLE) {
            // 开始轮播
            setPlaying(true)
        }
        super.onWindowVisibilityChanged(visibility)
    }

    /**
     * RecyclerView适配器
     */
    private inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val img = AppCompatImageView(parent.context)
            val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            img.layoutParams = params
            img.id = R.id.rvb_banner_image_view_id
            img.adjustViewBounds = true
            img.scaleType = mScaleType
            img.setOnClickListener {
                if (onRvBannerClickListener != null) {
                    onRvBannerClickListener!!.onClick(currentIndex % mData.size)
                }
            }
            return object : RecyclerView.ViewHolder(img) {

            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val img = holder.itemView.findViewById<View>(R.id.rvb_banner_image_view_id) as AppCompatImageView
            if (onSwitchRvBannerListener != null) {
                onSwitchRvBannerListener!!.switchBanner(position % mData.size, img)
            }
        }

        override fun getItemCount(): Int {

            return if (mData == null) 0 else if (mData.size < 2) mData.size else Integer.MAX_VALUE
        }
    }

    private inner class PagerSnapHelper : LinearSnapHelper() {

        override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager?, velocityX: Int, velocityY: Int): Int {

            var targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
            val currentView = findSnapView(layoutManager!!)
            if (targetPos != RecyclerView.NO_POSITION && currentView != null) {
                var currentPos = layoutManager.getPosition(currentView)
                val first = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val last = layoutManager.findLastVisibleItemPosition()
                currentPos = if (targetPos < currentPos) last else if (targetPos > currentPos) first else currentPos
                targetPos = if (targetPos < currentPos) currentPos - 1 else if (targetPos > currentPos) currentPos + 1 else currentPos
            }
            return targetPos
        }
    }

    /**
     * 改变导航的指示点
     */
    private fun switchIndicator() {

        if (mLinearLayout != null && mLinearLayout!!.childCount > 0) {
            for (i in 0 until mLinearLayout!!.childCount) {
                (mLinearLayout!!.getChildAt(i) as AppCompatImageView).setImageDrawable(
                        if (i == currentIndex % mData.size) mSelectedDrawable else mUnselectedDrawable)
            }
        }
    }

    private fun dp2px(dp: Int): Int {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                Resources.getSystem().displayMetrics).toInt()
    }

    interface OnSwitchRvBannerListener {

        fun switchBanner(position: Int, bannerView: AppCompatImageView)
    }

    fun setOnSwitchRvBannerListener(listener: OnSwitchRvBannerListener) {

        this.onSwitchRvBannerListener = listener
    }

    interface OnRvBannerClickListener {

        fun onClick(position: Int)
    }

    fun setOnRvBannerClickListener(onRvBannerClickListener: OnRvBannerClickListener) {

        this.onRvBannerClickListener = onRvBannerClickListener
    }

    companion object {

        val DEFAULT_SELECTED_COLOR = AndriodUtils.getColor(R.color.color_ff5a81)
        val DEFAULT_UNSELECTED_COLOR = -0x2a2a2b
    }

}
