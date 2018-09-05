package com.meishai.commission.helper.bean.resault

import android.os.Parcel
import android.os.Parcelable

/**
 * 作者：yl
 * 时间: 2018/5/2 14:06
 * 功能：接口返回的数据的封装文件
 */

data class MallIndexBean(
        val list: MutableList<MallListBean>,
        val banner: MutableList<BannerListBean>?,
        val category: MutableList<CategoryListBean>?,
        val model: MutableList<ModelListBean>?,
        val queue: MutableList<HomeQueueListBean>?,
        val s_time: Long
)

data class HomeQueueListBean(
        val id: String,
        val price: Int,
        val queue_price: Int,
        val pictures: String,
        val queue_end_date: Long
)
data class ModelListBean(
        val icon: String?,
        val tid: String?,
        val name: String,
        val content: String
)
data class BannerListBean(
        val picture: String,
        val type: Int,
        val value: String
)
data class MallListBean(
    val image: String,
    val name: String,
    val content: String,
    val tid: String,
    val item: List<ItemBean>
)

data class ItemBean(
    val gid: String,
    val tid: String,
    val title: String,
    val pictures: String,
    val sell_price: Int,
    val is_group: Int,
    val is_queue: Int
)
data class CategoryListBean(
        val cid: String,
        val name: String,
        val ico: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cid)
        parcel.writeString(name)
        parcel.writeString(ico)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryListBean> {
        override fun createFromParcel(parcel: Parcel): CategoryListBean {
            return CategoryListBean(parcel)
        }

        override fun newArray(size: Int): Array<CategoryListBean?> {
            return arrayOfNulls(size)
        }
    }
}