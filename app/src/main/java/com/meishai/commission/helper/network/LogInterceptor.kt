package com.meishai.commission.helper.network

import android.text.TextUtils
import android.util.Log
import com.meishai.commission.helper.cons.Constants
import com.meishai.commission.helper.cons.Constants.NETWORK_CODE
import com.meishai.commission.helper.cons.Constants.NETWORK_DATA
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * 作者：yl
 * 时间: 2017/8/8 16:18
 * 功能：日志拦截器
 */
class LogInterceptor : Interceptor {

    private var TAG = javaClass.simpleName

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val body = StringBuilder()
        if (request.body() != null && FormBody::class.java.name == request.body()!!.javaClass.name) {
            val formBody = request.body() as FormBody?
            for (i in 0 until formBody!!.size()) {
                body.append("\n").append(formBody.name(i)).append("\t").append(formBody.value(i))
            }
        }
        val response = chain.proceed(chain.request())
        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        //ali支付订单接口数据需单独处理

        //解密
        //        content = pareseContent(content);
        Log.w(TAG, request.url().toString() + "  " + content)
        //解析后需要单独处理的进行错误抛出
        throwExceptionAtParesed(content, request.url().toString())

        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build()
    }

    /**
     * 尝试对所有的网络请求返回的数据进行解密
     * 成功 返回解密后重新包装的数据
     * 失败 原样返回
     *
     * @param content 返回的数据
     */
    private fun pareseContent(content: String): String {

        try {
            val `object` = JSONObject(content)
            //解析数据
            val decodeData = decodeData(`object`)
            return decodeData ?: content
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return content
    }

    /**
     * 在解析后
     * 需要单独处理的采用异常进行抛出
     *
     * @param content
     * @param url
     */
    private fun throwExceptionAtParesed(content: String, url: String) {

        val code: Int
        try {
            code = JSONObject(content).getInt(Constants.NETWORK_CODE)
        } catch (e: Exception) {
            throw ApiException("服务器异常,请稍后重试", content, -1)
        }

        val message = ErrorInfo.getMessage(code)

        if (code != 1) {
            throw ApiException(message, content, code)
        }

    }

    /**
     * 解密data部分数据,并与code一起拼装成一个解密后的新数据返回,任何情况下的解密失败都返回null
     *
     * @param object 原数据的json对象
     * @return 如果data部分有加密, 则解密之后再把数据拼装起来返回, 否则返回null
     */
    @Throws(JSONException::class)
    private fun decodeData(`object`: JSONObject): String? {
        //判断状态是否成功
        val code = `object`.getInt(Constants.NETWORK_CODE)
        val o = `object`.get(NETWORK_DATA)
        if (o is String) {
            try {
                //拿去解个密
                //                String temp = AESUtils.decrypt(data, UserDataManager.getInstance().getToken());
                //                return assemblyData(code, temp);
                return assemblyData(code, o)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return null
    }

    /**
     * 拼装数据,使用code和data,如果data是空的,则返回null
     *
     * @param code
     * @param data
     * @return
     */
    private fun assemblyData(code: Int, data: String): String? {

        if (TextUtils.isEmpty(data)) return null

        val builder = StringBuilder()
        builder.append("{")
                .append("\"").append(NETWORK_CODE).append("\"").append(":").append(code).append(",")
                .append("\"").append(NETWORK_DATA).append("\"").append(":").append(data)
                .append("}")
        return builder.toString()
    }

}
