package com.meishai.commission.helper.view

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.meishai.commission.helper.R
import com.meishai.commission.helper.utils.AndriodUtils
import kotlinx.android.synthetic.main.layout_info_item.view.*

/**
 * 作者：yl
 * 时间: 2017/8/9 16:43
 * 功能：一个封装了一个布局的view 目前用于做个人信息页面的item 和设置页面的item
 */
class InfoItemView(context: Context) : LinearLayout(context) {
    companion object {
        const val BOTTOM_TYPE_LINE = 1
        const val BOTTOM_TYPE_SPACE = 2
    }

    private var mIvItemLeftIcon: ImageView
    private var tvItemLeftText:TextView
    private var mTvItemRightText:TextView
    private var ivItemRightIcon: ImageView
    private var mIvItemRightMore: ImageView
    private var mVItemLine: View
    private var mVItemSpace: View
    private var mRlItem: RelativeLayout

    init {
        View.inflate(context, R.layout.layout_info_item, this)
        mIvItemLeftIcon = iv_item_left_icon
        tvItemLeftText = tv_item_left_text
        mTvItemRightText = tv_item_right_text
        ivItemRightIcon = iv_item_right_icon
        mIvItemRightMore = iv_item_right_more
        mVItemLine = v_item_line
        mVItemSpace = v_item_space
        mRlItem = rl_item
    }

    /**
     * 个人信息页的item样式
     * @param leftIconRes
     * @param leftText
     * @param rightText
     * @param rightAvatarUrl
     * @param typeLineOrSpace
     * @param listener
     */
    fun getItemView(leftIconRes: Int, leftText: String, rightText: String, rightAvatarUrl: String, typeLineOrSpace: Int, listener: View.OnClickListener?) {

        if (leftIconRes < 0) {
            mIvItemLeftIcon.visibility = View.GONE
        } else {
            mIvItemLeftIcon.visibility = View.VISIBLE
            mIvItemLeftIcon.setImageResource(leftIconRes)
        }
        tvItemLeftText.text = leftText
        if (TextUtils.isEmpty(rightAvatarUrl)) {
            ivItemRightIcon.visibility = View.GONE
            mTvItemRightText.visibility = View.VISIBLE
            mTvItemRightText.text = rightText
        } else {
            ivItemRightIcon.visibility = View.VISIBLE
            mTvItemRightText.visibility = View.GONE
            //设置图片
            AndriodUtils.onLoadHttpImage(context, rightAvatarUrl, ivItemRightIcon, R.mipmap.default_avatar)
        }
        setBottomType(typeLineOrSpace)
        if (listener != null)
            setOnClickListener(listener)
    }

    /**
     * 设置页面的item样式
     *
     * @param leftText
     * @param rightResId
     * @param typeLineOrSpace 1.line 2.space 其他.隐藏
     * @param listener
     */
    fun getItemView(leftText: String, rightText: String, rightResId: Int, typeLineOrSpace: Int, listener: View.OnClickListener?) {
        mIvItemLeftIcon.visibility = View.GONE
        ivItemRightIcon.visibility = View.GONE

        mTvItemRightText.text = rightText
        tvItemLeftText.text = leftText
        if (rightResId != -1)
            mIvItemRightMore.setImageResource(rightResId)
        setBottomType(typeLineOrSpace)
        if (listener != null)
            setOnClickListener(listener)
    }

    private fun setBottomType(type: Int) {
        when (type) {
            BOTTOM_TYPE_LINE//line
            -> {
                mVItemLine.visibility = View.VISIBLE
                mVItemSpace.visibility = View.GONE
            }
            BOTTOM_TYPE_SPACE//space
            -> {
                mVItemLine.visibility = View.GONE
                mVItemSpace.visibility = View.VISIBLE
            }
            else//default
            -> {
                mVItemLine.visibility = View.GONE
                mVItemSpace.visibility = View.GONE
            }
        }
    }

    fun setRightIconVisibility(visibility: Int) {
        ivItemRightIcon.visibility = visibility
    }

    fun setRightResId(resId: Int) {
        ivItemRightIcon.setImageResource(resId)
    }

    fun setMoreResId(resId: Int) {
        mIvItemRightMore.setImageResource(resId)
    }

    fun setRightText(text: String) {
        mTvItemRightText.text = text
    }
}
