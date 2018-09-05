package com.meishai.commission.helper.bean.resault

/**
 * 作者：yl
 * 时间: 2018/7/11 18:22
 * 功能：发现列表接口返回的数据
 */

data class ArticleListResqBean(
    val list: List<ArticleListBean>,
    val category: ArrayList<ArticleCategoryBean>
)

data class ArticleCategoryBean(
    val id: String,
    val name: String)


data class ArticleListBean(
    val id: String,
    val title: String?,
    val describe: String?,
    val view: String?,
    val like: String?,
    val picture: String?
)