package com.meishai.commission.helper.network

import android.text.TextUtils

import com.meishai.commission.helper.bean.ResaultBean
import rx.functions.Func1

/**
 * 功 能：用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber <br></br>
 * 时 间：2016/6/16 10:17 <br></br>
 */
class HttpResultFunc<T> : Func1<ResaultBean<T>, T> {
    override fun call(httpResult: ResaultBean<T>?): T? {
        return if (httpResult?.data == null) {
            null
        } else httpResult.data

        //        Class<T> entityClass =(Class<T>) ((ParameterizedType) getClass()
        //                .getGenericSuperclass()).getActualTypeArguments()[0];
        //        if(String.class == entityClass){
        //            return (T) httpResult.getData();
        //        }
        //
        //        String data = httpResult.getData();
        //        if (!isJson(data)) {
        //            //拿去解个密
        //            try {
        //                String temp = AESUtils.decrypt(data, ShareUtils.getShare(Constants.KEY_TOKEN, ""));
        //                if(!TextUtils.isEmpty(temp))
        //                    data = temp;
        //            } catch (Exception e) {
        //                e.printStackTrace();
        //            }
        //        }
        //        return ParseJson.parse(data,entityClass);
    }

    /**
     * 判断当前返回的数据是否是json数据,如果不是的话,说明是进行了加密的数据,直接解密,当然也有可能是服务器抛出的异常,解密不处理也直接不管他
     * @param data
     * @return
     */
    private fun isJson(data: String): Boolean {

        if (TextUtils.isEmpty(data)) return false
        return data.startsWith("{") && data.endsWith("}")
    }

}