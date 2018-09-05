package com.meishai.commission.helper.network

/**
 * 作者：yl
 * 时间: 2017/8/9 11:59
 * 功能：错误信息代码对应的文字信息的类
 */
object ErrorInfo {

    const val ERROR_CODE_FAILD = 0
    /**
     * 返回错误代码对应的字符串,部分code需要单独处理
     *
     * @param status
     * @return
     */
    fun getMessage(status: Int): String {

        when (status) {
            ERROR_CODE_FAILD -> return "请求失败"
            else -> return "code:$status"
        }
    }

}
