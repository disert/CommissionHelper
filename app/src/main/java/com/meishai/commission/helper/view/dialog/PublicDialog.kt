package com.meishai.commission.helper.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.meishai.commission.helper.R
import kotlinx.android.synthetic.main.dialog_public.*

/**
 * 升级弹出框
 *
 * @author sh
 */
class PublicDialog(private var mBuilder: Builder) : Dialog(mBuilder.mContext, R.style.dialog_public) {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.dialog_public)
        this.initView()

    }

    private fun initView() {


        setOnDismissListener { dialog ->
            if (mBuilder.dismissListener != null) {
                mBuilder.dismissListener!!.onDismiss(dialog)
            }
        }

        if (TextUtils.isEmpty(mBuilder.title)) {
            tv_title.visibility = View.GONE
        } else {
            tv_title.visibility = View.VISIBLE
            tv_title.text = mBuilder.title
        }
        if (mBuilder.mLeftBg != -1) {
            btn_close.setBackgroundResource(mBuilder.mLeftBg)
        }
        if (mBuilder.mRightBg != -1) {
            btn_conform.setBackgroundResource(mBuilder.mRightBg)
        }
        tv_content.text = mBuilder.content
        btn_close.text = mBuilder.cancel
        btn_conform.text = mBuilder.conform
        btn_close.setOnClickListener {
            if (mBuilder.cancelListener != null) {
                mBuilder.cancelListener!!.onClick(btn_close)
            }
            dismiss()
        }
        btn_conform.setOnClickListener { v ->
            if (mBuilder.conformListener != null) {
                mBuilder.conformListener!!.onClick(v)
            }
            dismiss()
        }
        when (mBuilder.type) {
            PublicDialog.Builder.ShowEnum.CANCEL_CONFORM -> {
                lay_close.visibility = View.VISIBLE
                lay_conform.visibility = View.VISIBLE
            }
            PublicDialog.Builder.ShowEnum.CONFORM -> {
                lay_close.visibility = View.GONE
                lay_conform.visibility = View.VISIBLE
            }
            PublicDialog.Builder.ShowEnum.CANCEL -> {
                lay_close.visibility = View.VISIBLE
                lay_conform.visibility = View.GONE
            }
        }

    }

    class Builder(val mContext: Context) {
        var content: String? = null
        var title: String? = null
        var cancel: String? = null
        var conform: String? = null
        var mLeftBg = -1
        var mRightBg = -1
        var type = ShowEnum.CANCEL_CONFORM
        var cancelListener: View.OnClickListener? = null
        var conformListener: View.OnClickListener? = null
        var dismissListener: DialogInterface.OnDismissListener? = null


        enum class ShowEnum {
            CANCEL_CONFORM, CANCEL, CONFORM
        }

        fun setLeftBg(leftBg: Int): Builder {
            this.mLeftBg = leftBg
            return this
        }

        fun setRightBg(rightBg: Int): Builder {
            this.mRightBg = rightBg
            return this
        }

        fun setType(type: ShowEnum): Builder {
            this.type = type
            return this
        }

        fun setContent(content: String): Builder {

            this.content = content
            return this
        }

        fun setTitle(title: String): Builder {

            this.title = title
            return this
        }

        fun setCancel(cancel: String): Builder {

            this.cancel = cancel
            return this
        }

        fun setConform(conform: String): Builder {

            this.conform = conform
            return this
        }

        fun setOnDismissListenr(dismissListener: DialogInterface.OnDismissListener): Builder {

            this.dismissListener = dismissListener
            return this
        }

        fun setConformListenr(conformListener: View.OnClickListener): Builder {

            this.conformListener = conformListener
            return this
        }

        fun setCancelListenr(cancelListener: View.OnClickListener?): Builder {

            this.cancelListener = cancelListener
            return this
        }

        fun build(): PublicDialog {

            return PublicDialog(this)
        }

        fun show() {

            val dialog = build()
            dialog.show()
        }
    }
}