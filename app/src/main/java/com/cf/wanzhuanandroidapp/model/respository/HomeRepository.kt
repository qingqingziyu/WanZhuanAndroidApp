package com.cf.wanzhuanandroidapp.model.respository

import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.core.WanRetrofitClient
import com.cf.wanzhuanandroidapp.model.api.BaseRepository
import com.cf.wanzhuanandroidapp.model.bean.ArticleList
import com.cf.wanzhuanandroidapp.model.bean.Banner

class HomeRepository : BaseRepository() {
    suspend fun getBanners(): Result<List<Banner>> {
        return safeApiCall(call = { requestBanners() }, errorMessage = "")
    }

    private suspend fun requestBanners(): Result<List<Banner>> =
        executeResponse(WanRetrofitClient.serivices.getBanner())

    suspend fun getArticleList(page: Int): Result<ArticleList> {
        return safeApiCall(call = { requestArticleList(page) }, errorMessage = "")
    }

    private suspend fun requestArticleList(page: Int): Result<ArticleList> =
        executeResponse(WanRetrofitClient.serivices.getHomeArticles(page))

}