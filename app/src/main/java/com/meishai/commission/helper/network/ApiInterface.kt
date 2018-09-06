package com.meishai.commission.helper.network

import com.meishai.commission.helper.bean.ResaultBean
import com.meishai.commission.helper.bean.ShareData
import com.meishai.commission.helper.bean.WechatAuthorBean
import com.meishai.commission.helper.bean.resault.ArticleListResqBean
import com.meishai.commission.helper.bean.resault.MallIndexBean
import com.meishai.commission.helper.cons.Constants.PATH_ARTICLE_LIST
import com.meishai.commission.helper.cons.Constants.PATH_MALL_INDEX
import com.meishai.commission.helper.cons.Constants.PATH_SHARE
import com.meishai.commission.helper.cons.Constants.PATH_SMSCODE
import com.meishai.commission.helper.cons.Constants.PATH_TAOBAO_TEL_SEGMENT
import com.meishai.commission.helper.cons.Constants.PATH_UPDATE_IMG
import com.meishai.commission.helper.cons.Constants.PATH_WECHAT_USER_INFO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import rx.Observable


/**
 * 作者 Administrator
 * 时间   2017/7/12 0012 18:30.
 * 描述   网络请求的接口
 *
 * 服务器提供的接口通常最多只有4个参数
 *
 * data         具体的数据,通过把要传递的数据封装成了一个json格式的数据,加密之后发送给服务器
 * id           用户ID。POST请求
 * timestamp    请求的时间戳（服务端默认超过10秒请求无效）。POST请求
 * sign         签名验证，验证规则md5(id+token+timestamp)。POST请求。（token登录时获得。传参时一定是进行md5加密传参）
 */

interface ApiInterface {
    companion object {
        const val DATA = "data"
        const val ID = "id"
        const val TIMESTAMP = "timestamp"
        const val SIGN = "sign"
    }

    /**
     * 获取微信用户数据
     * @return
     */
    @GET(PATH_WECHAT_USER_INFO)
    fun wechatUserInfo(@Query("access_token") access_token: String, @Query("openid") openid: String): Observable<ResaultBean<WechatAuthorBean>>

    /**
     * 获取号码归属地
     * @return
     */
    @GET(PATH_TAOBAO_TEL_SEGMENT)
    fun telSegment(@Query("tel") tel: String): Observable<ResaultBean<String>>

    /**
     * 上传多张图片 upload/showimage
     *
     * @return
     */
    @Multipart
    @POST(PATH_UPDATE_IMG)
    fun uploadImgs(@PartMap params: Map<String, RequestBody>, @Part(DATA) data: RequestBody, @Part(ID) id: RequestBody, @Part(TIMESTAMP) timestamp: RequestBody, @Part(SIGN) sign: RequestBody): Observable<ResaultBean<String>>

    /**
     * 上传单张图片 upload/showimage
     *
     * @return
     */
    @Multipart
    @POST(PATH_UPDATE_IMG)
    fun uploadImg(@Part file: MultipartBody.Part, @Part(DATA) data: RequestBody, @Part(ID) id: RequestBody, @Part(TIMESTAMP) timestamp: RequestBody, @Part(SIGN) sign: RequestBody): Observable<ResaultBean<String>>


    /**
     * 下载图片
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    fun downloadPicFromNet(@Url fileUrl: String): Observable<ResponseBody>


    /**
     * 分享数据
     * @return
     */
    @FormUrlEncoded
    @POST(PATH_SHARE)
    fun shareData(@Field(DATA) data: String): Observable<ResaultBean<ShareData>>

    /**
     * 分享数据
     * @return
     */
    @FormUrlEncoded
    @POST(PATH_SHARE)
    fun shareData(@Field(DATA) data: String, @Field(ID) id: String, @Field(TIMESTAMP) timestamp: Long, @Field(SIGN) sign: String): Observable<ResaultBean<ShareData>>

    @GET(PATH_ARTICLE_LIST)
    fun articleList(@Query(DATA)data: String): Observable<ResaultBean<ArticleListResqBean>>


    @GET(PATH_MALL_INDEX)
    fun mallIndex(@Query(DATA)data: String): Observable<ResaultBean<MallIndexBean>>

    /**
     * 获取短信验证码
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST(PATH_SMSCODE)
    fun smsCode(@Field(DATA) data: String): Observable<ResaultBean<String>>
}