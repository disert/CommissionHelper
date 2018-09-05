package com.meishai.commission.helper.network

/**
 * 作者 Administrator
 * 时间   2017/7/14 0014 12:09.
 * 描述   处理公共数据的异常,当返回的请求结果为失败的情况下,直接抛出异常
 */

class ApiException(private val msg: String, private val orgData: String, private val code: Int) : RuntimeException() {

    fun getMsg(): String {
        return msg
    }
    fun getOrgData():String{
        return orgData
    }
    fun getCode():Int{
        return code
    }
}
