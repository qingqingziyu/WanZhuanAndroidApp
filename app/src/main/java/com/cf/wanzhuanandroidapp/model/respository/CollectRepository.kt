package com.cf.wanzhuanandroidapp.model.respository

import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.core.WanRetrofitClient
import com.cf.wanzhuanandroidapp.model.api.BaseRepository
import com.cf.wanzhuanandroidapp.model.bean.ArticleList

class CollectRepository : BaseRepository() {
    suspend fun getCollectArticles(page: Int): Result<ArticleList> {
        return safeApiCall(call = {requestCollectArticles(page)}, errorMessage = "网络错误")
    }

    suspend fun collectArticle(articleId: Int):Result<ArticleList>{
        return safeApiCall(call = {requestCollectArticle(articleId)},errorMessage = "网络错误")
    }

    suspend fun unCollectArticle(articleId: Int):Result<ArticleList>{
        return safeApiCall(call = {requestCancelCollectArticle(articleId)},errorMessage = "网络错误")
    }

    private suspend fun requestCollectArticles(page: Int): Result<ArticleList> =
        executeResponse(WanRetrofitClient.serivices.getCollectArticles(page))

    private suspend fun requestCollectArticle(articleId: Int): Result<ArticleList> =
        executeResponse(WanRetrofitClient.serivices.collectArticle(articleId))

    private suspend fun requestCancelCollectArticle(articleId: Int): Result<ArticleList> =
        executeResponse(WanRetrofitClient.serivices.cancelCollectArticle(articleId))
}