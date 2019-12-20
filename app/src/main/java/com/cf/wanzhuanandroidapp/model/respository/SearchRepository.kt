package com.cf.wanzhuanandroidapp.model.respository

import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.core.WanRetrofitClient
import com.cf.wanzhuanandroidapp.model.api.BaseRepository
import com.cf.wanzhuanandroidapp.model.bean.Article
import com.cf.wanzhuanandroidapp.model.bean.ArticleList
import com.cf.wanzhuanandroidapp.model.bean.Hot

/**
 *@作者:陈飞
 *@说明:搜索
 *@时间:2019/12/3 9:46
 */
class SearchRepository : BaseRepository() {
    suspend fun searchHot(page: Int, key: String): Result<ArticleList> {
        return safeApiCall(call = { requestSearch(page, key) }, errorMessage = "网络错误")
    }

    suspend fun getWebSite(): Result<List<Hot>> {
        return safeApiCall(call = { requestWebSites() }, errorMessage = "网络错误")
    }

    suspend fun getHotSearch(): Result<List<Hot>> {
        return safeApiCall(call = { requestHotSearch() }, errorMessage = "网络错误")
    }


    private suspend fun requestWebSites(): Result<List<Hot>> =
        executeResponse(WanRetrofitClient.serivices.getWebSites())

    private suspend fun requestHotSearch(): Result<List<Hot>> =
        executeResponse(WanRetrofitClient.serivices.getHot())

    private suspend fun requestSearch(page: Int, key: String): Result<ArticleList> =
        executeResponse(WanRetrofitClient.serivices.searchHot(page, key))
}