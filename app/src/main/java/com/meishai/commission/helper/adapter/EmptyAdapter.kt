package com.qipa.shop.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.meishai.commission.helper.R
import kotlinx.android.synthetic.main.layout_empty_page.view.*

/**
 * 作者：yl
 * 时间: 2017/10/14 11:09
 * 功能：
 */
abstract class EmptyAdapter<VH : RecyclerView.ViewHolder>(protected var mContext: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_EMPTY = 100000
    private val TYPE_BOTTOM = 100001
    private var emptyContent: CharSequence? = null
    private var isEmpty = false
    private var isBottom: Boolean = false
    private var hasBottomMore: Boolean = false
    private var bottomMoreListener: View.OnClickListener? = null
    private var bottomMoreText: String? = null
    private var bottomText: String? = null
    private var hasEmptyMore: Boolean = false
    private var emptyMoreListener: View.OnClickListener? = null
    private var emptyMoreText: String? = null
    var emptyListener: View.OnClickListener? = null
    var emptyImgResId = -1
    var emptyBtnText: String? = null
    var emptyBtnListener: View.OnClickListener? = null


    fun setBottom(bottom: Boolean) {
        isBottom = bottom
    }

    fun setBottomAndEmpty(bottom: Boolean, empty: Boolean) {
        isBottom = bottom
        isEmpty = empty
        notifyDataSetChanged()
    }

    fun setEmpty(empty: Boolean) {
        isEmpty = empty
        notifyDataSetChanged()
    }

    fun setBottomMore(hasBottomMore: Boolean, bottomMoreText: String, bottomMoreListener: View.OnClickListener) {
        this.hasBottomMore = hasBottomMore
        this.bottomMoreListener = bottomMoreListener
        this.bottomMoreText = bottomMoreText
        notifyDataSetChanged()
    }

    fun setEmptyMore(hasEmptyMore: Boolean, emptyMoreText: String, emptyMoreListener: View.OnClickListener) {
        this.hasEmptyMore = hasEmptyMore
        this.emptyMoreListener = emptyMoreListener
        this.emptyMoreText = emptyMoreText
        notifyDataSetChanged()
    }

    fun setEmptyDesc(desc: CharSequence) {
        emptyContent = desc
    }

    fun setBottomText(text: String) {
        bottomText = text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_EMPTY) {
            EmptyHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_page, parent, false))
        } else if (viewType == TYPE_BOTTOM) {
            BottomHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_page, parent, false))
        } else {
            onCreateHolder(parent, viewType)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getCount() == 0 && position == 0) {
            return TYPE_EMPTY
        } else if (getCount() > 0 && position == getCount()) {
            return TYPE_BOTTOM
        }
        return getItemType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EmptyHolder) {
            holder.init(emptyContent, hasEmptyMore, emptyMoreText, emptyMoreListener, emptyImgResId, emptyListener,emptyBtnText,emptyBtnListener)
        } else if (holder is BottomHolder) {
            holder.init(hasBottomMore, bottomText, bottomMoreText, bottomMoreListener)
        } else {
            onBindHolder(holder as VH, position)
        }
    }

    override fun getItemCount(): Int {
        if (getCount() == 0 && isEmpty) return 1
        return if (getCount() > 0 && isBottom) getCount() + 1 else getCount()
    }

    class EmptyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mTvDesc: TextView = itemView.tv_empty_desc
        private var mIvIcon: ImageView = itemView.iv_icon
        private var mMore: View = itemView.ll_empty_more
        private var mTvMore: TextView = itemView.tv_empty_more
        var mTvBtn: TextView = itemView.tv_empty_btn

        fun init(content: CharSequence?, hasEmptyMore: Boolean, emptyMoreText: String?, emptyMoreListener: View.OnClickListener?, ImgResId: Int, emptyListener: View.OnClickListener?,btnText: String?,btnListener: View.OnClickListener?) {
            if (!TextUtils.isEmpty(content)) mTvDesc.text = content
            if (emptyListener != null) itemView.setOnClickListener(emptyListener)
            if (ImgResId != -1) mIvIcon.setImageResource(ImgResId)
            if (hasEmptyMore) {
                mTvDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13f)
                mMore.visibility = View.VISIBLE
                if (!TextUtils.isEmpty(emptyMoreText)) mTvMore.text = emptyMoreText
                if (emptyMoreListener != null) mMore.setOnClickListener(emptyMoreListener)
            } else {
                mTvDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
                mMore.visibility = View.GONE

            }
            if(TextUtils.isEmpty(btnText)){
                mTvBtn.visibility = View.GONE
            }else{
                mTvBtn.visibility = View.VISIBLE
                mTvBtn.text = btnText
                if(btnListener != null)mTvBtn.setOnClickListener(btnListener)
            }
        }

    }

    internal class BottomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mLeft: View
        var mRight: View
        var mMore: View
        var mTvMore: TextView
        var mTvContent: TextView


        init {
            mLeft = itemView.findViewById(R.id.tv_bottom_left)
            mRight = itemView.findViewById(R.id.tv_bottom_right)
            mMore = itemView.findViewById(R.id.ll_bottom_more)
            mTvMore = itemView.findViewById(R.id.tv_bottom_more) as TextView
            mTvContent = itemView.findViewById(R.id.tv_bottom_title) as TextView

        }

        fun init(hasBottomMore: Boolean, bottomText: String?, bottomMoreText: String?, bottomMoreListener: View.OnClickListener?) {
            if (hasBottomMore) {
                mLeft.visibility = View.GONE
                mRight.visibility = View.GONE
                mMore.visibility = View.VISIBLE
                if (!TextUtils.isEmpty(bottomText)) mTvContent.text = bottomText
                if (!TextUtils.isEmpty(bottomMoreText)) mTvMore.text = bottomMoreText
                if (bottomMoreListener != null) mMore.setOnClickListener(bottomMoreListener)
            } else {
                mLeft.visibility = View.VISIBLE
                mRight.visibility = View.VISIBLE
                mMore.visibility = View.GONE

            }

        }
    }

    open fun getItemType(position: Int): Int {
        return super.getItemViewType(position)
    }

    abstract fun onBindHolder(holder: VH, position: Int)
    abstract fun onCreateHolder(parent: ViewGroup, viewType: Int): VH
    abstract fun getCount(): Int
}
