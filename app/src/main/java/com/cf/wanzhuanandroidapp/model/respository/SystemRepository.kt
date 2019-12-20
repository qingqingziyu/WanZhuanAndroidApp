package com.cf.wanzhuanandroidapp.model.respository

import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.core.WanRetrofitClient
import com.cf.wanzhuanandroidapp.model.api.BaseRepository
import com.cf.wanzhuanandroidapp.model.bean.ArticleList
import com.cf.wanzhuanandroidapp.model.bean.SystemParent

class SystemRepository :BaseRepository() {
    suspend fun getSystemTypeDetail(cid:Int,page:Int):Result<ArticleList>{
       return safeApiCall(call = {requestSystemTypeDetail(cid, page)},errorMessage = "网络错误")
    }

    suspend fun getSystemTypes():Result<List<SystemParent>>{
        return safeApiCall(call = {requestSystemTypes()},errorMessage = "网络错误")
    }

    suspend fun getBlogArticle(cid: Int,page: Int):Result<ArticleList>{
        return safeApiCall(call = {requestBlogArticle(cid, page)},errorMessage = "网络错误")
    }

    private suspend fun requestSystemTypes():Result<List<SystemParent>> =
        executeResponse(WanRetrofitClient.serivices.getSystemType())

    private suspend fun requestSystemTypeDetail(cid: Int,page: Int):Result<ArticleList> =
        executeResponse(WanRetrofitClient.serivices.getSystemTypeDetail(page, cid))

    private suspend fun requestBlogArticle(cid: Int,page: Int):Result<ArticleList> =
        executeResponse(WanRetrofitClient.serivices.getSystemTypeDetail(page, cid))
}
