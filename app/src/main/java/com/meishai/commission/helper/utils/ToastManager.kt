package com.meishai.commission.helper.utils

/**
 * Created by Administrator on 2017/8/8.
 */

import android.content.Context
import android.widget.Toast

/**
 * 功 能： Toast统一管理类
 * 时 间：2016/11/7 10:35 <br></br>
 */
class ToastManager
//    private static TextView mContent;

private constructor() {
    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {
        private var mToast: Toast? = null

        var isShow = true

        /**
         * 短时间显示Toast
         *
         * @param context
         * @param message
         */
        fun showShort(context: Context, message: CharSequence) {
            if (isShow) {
                if (mToast == null) {
                    mToast = getInstance(context)
                }
                mToast!!.duration = Toast.LENGTH_SHORT
                mToast!!.setText(message)
                //            mContent.setText(message);
                mToast!!.show()
            }
        }

        /**
         * 短时间显示Toast
         *
         * @param context
         * @param message
         */
        fun showShort(context: Context, message: Int) {
            if (isShow) {
                if (mToast == null) {
                    mToast = getInstance(context)
                }
                mToast!!.duration = Toast.LENGTH_SHORT
                mToast!!.setText(context.getString(message))
                //            mContent.setText(context.getString(message));

                mToast!!.show()
            }
            //        if (isShow) Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

        /**
         * 长时间显示Toast
         *
         * @param context
         * @param message
         */
        fun showLong(context: Context, message: CharSequence) {
            if (isShow) {
                if (mToast == null) {
                    mToast = getInstance(context)
                }
                mToast!!.duration = Toast.LENGTH_LONG
                mToast!!.setText(message)
                mToast!!.show()
            }
            //        if (isShow) Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

        /**
         * 长时间显示Toast
         *
         * @param context
         * @param message
         */
        fun showLong(context: Context, message: Int) {
            if (isShow) {
                if (mToast == null) {
                    mToast = getInstance(context)
                }
                mToast!!.duration = Toast.LENGTH_LONG
                mToast!!.setText(context.getString(message))
                mToast!!.show()
            }
            //        if (isShow) Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

        /**
         * 自定义显示Toast时间
         *
         * @param context
         * @param message
         * @param duration
         */
        fun show(context: Context, message: CharSequence, duration: Int) {
            if (isShow) {
                if (mToast == null) {
                    mToast = getInstance(context)
                }
                mToast!!.duration = duration
                mToast!!.setText(message)
                mToast!!.show()
            }
            //        if (isShow) Toast.makeText(context, message, duration).show();
        }

        /**
         * 自定义显示Toast时间
         *
         * @param context
         * @param message
         * @param duration
         */
        fun show(context: Context, message: Int, duration: Int) {
            if (isShow) {
                if (mToast == null) {
                    mToast = getInstance(context)
                }
                mToast!!.duration = duration
                mToast!!.setText(context.getString(message))
                mToast!!.show()
            }
            //        if (isShow) Toast.makeText(context, message, duration).show();
        }

        private fun getInstance(context: Context): Toast {
            //        LayoutInflater inflater = LayoutInflater.from(context);
            //        View view = inflater.inflate(R.layout.layout_toast, null);
            //        mContent = (TextView) view.findViewById(R.id.chapterName);
            //        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 20);
            //        toast.setView(view);
            //        Toast toast = new Toast(context);

            return Toast.makeText(context, "", Toast.LENGTH_SHORT)
        }
    }

}
