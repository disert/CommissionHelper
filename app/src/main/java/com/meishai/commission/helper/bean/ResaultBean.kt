package com.meishai.commission.helper.bean

/**
 * Created by Administrator on 2017/7/17.
 *
 * 网络接口返回数据的基本格式
 */


class ResaultBean<T> {

    /**
     * status : 2
     */
    var code: Int = 0
    var data: T? = null
}
