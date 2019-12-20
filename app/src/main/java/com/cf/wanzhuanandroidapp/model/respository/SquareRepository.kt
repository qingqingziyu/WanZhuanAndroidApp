package com.cf.wanzhuanandroidapp.model.respository

import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.core.WanRetrofitClient
import com.cf.wanzhuanandroidapp.model.api.BaseRepository
import com.cf.wanzhuanandroidapp.model.bean.Article
import com.cf.wanzhuanandroidapp.model.bean.ArticleList
import com.cf.wanzhuanandroidapp.util.isSuccess
import java.io.IOException

class SquareRepository : BaseRepository() {

    suspend fun getSquareArticleList(page: Int): Result<ArticleList> {
        return safeApiCall(call = { requestSquareArticleList(page) }, errorMessage = "网络异常")
    }

    private suspend fun requestSquareArticleList(page: Int): Result<ArticleList> {
        val response = WanRetrofitClient.serivices.getSquareArticleList(page)
        return if (response.isSuccess()) Result.Success(response.data)
        else Result.Error(IOException(response.errorMsg))
    }
}