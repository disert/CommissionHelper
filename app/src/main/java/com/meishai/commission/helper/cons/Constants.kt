package com.meishai.commission.helper.cons

/**
 * 作者 Administrator
 * 时间   2017/7/15 0015 01:10.
 * 描述   一些常量
 */

object Constants {
    /**
     * 正则表达式：验证手机号
     */
    const val REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"


    //正式环境
    //    int SDK_APPID = 1400045528;
    //    String BASE_URL = "https://www.guopai365.com/v6/";
    //    String getKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdwmcmjsb3MblCh68CcMRfRoKV\n" +
    //            "5rL3eInf45KF3V+59F6P/Bc/IQO6JSuqXsKiqWkcOT/CVj1DyRTDUnmfT1FVAc8j\n" +
    //            "Wz7ghK1nt2xkB4xrBs0t8mtT6zRBYF1HWU4MU/LWHgOhoK+xSsrVOUHHTfq+RkvI\n" +
    //            "Kny9Jw3W4WopzddBZwIDAQAB";

    //测试环境
    const val SDK_APPID = 1400040747
    const val BASE_URL = "https://app.guopai365.com/v5/"
    const val getKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCtxXyGdzNt0S/IgfVKwqp9m9qD\n" +
            "XwEC8t9mS5Z1kV8Vqep9S1oXkoCTt/DHYkxltwJcN5FX/frC1qsC7xfV55dEEBTX\n" +
            "nWRjSNMJ/HWnSFC3Vqhbo2GrZD7nY9th3/rFxOm5uREXENs82Geknh3MW0yKHtPv\n" +
            "w9h3+QVEGJjBMnmMVQIDAQAB"

    const val UMENG_KEY = "59ce0ddd65b6d67866000033"



    const val PATH_WECHAT_USER_INFO = "https://api.weixin.qq.com/sns/userinfo"
    const val PATH_TAOBAO_TEL_SEGMENT = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm"

    //share配置文件的名字
    const val SP_PACKAGE = "tagalog"
    //配置文件中的键
    const val KEY_IS_FIRST_OPEN = "KEY_IS_FIRST_OPEN"//是否是第一次打开

    //接口路径: 发现列表
    const val PATH_ARTICLE_LIST = "article/list"
    //接口路径: 商城首页
    const val PATH_MALL_INDEX = "home/index"
    const val PATH_SHARE = "user/appshare"
    const val PATH_UPDATE_IMG = "upload/showimage"

    //网络返回数据的统一参数
    const val NETWORK_CODE = "code"

    const val NETWORK_DATA = "data"

    const val PAGE_COUNT = 10//每页加载数据的条数
    const val REQUEST_CODE_CONTACT = 101

}
