package com.cf.wanzhuanandroidapp.model.respository

import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.core.WanRetrofitClient
import com.cf.wanzhuanandroidapp.model.api.BaseRepository
import com.cf.wanzhuanandroidapp.model.bean.ArticleList
import com.cf.wanzhuanandroidapp.model.bean.SystemChild
import com.cf.wanzhuanandroidapp.model.bean.SystemParent

class ProjectRepository : BaseRepository() {
    suspend fun getProjectTypeDetailList(page: Int, cid: Int): Result<ArticleList> {
        return safeApiCall(
            call = { requestProjectTypeDetailList(page, cid) },
            errorMessage = "发生未知错误"
        )
    }

    suspend fun getLastedProject(page: Int): Result<ArticleList> {
        return safeApiCall(call = { requestLastedProject(page) }, errorMessage = "网络错误")
    }

    suspend fun getProjectTypeList(): Result<List<SystemParent>> {
        return safeApiCall(call = { requestProjectTypeList() }, errorMessage = "网络错误")
    }

    suspend fun getBlog(): Result<List<SystemParent>> {
        return safeApiCall(call = { requestBlogTypeList() }, errorMessage = "网络错误")
    }

    private suspend fun requestProjectTypeDetailList(page: Int, cid: Int) =
        executeResponse(WanRetrofitClient.serivices.getProjectTypeDetail(page, cid))

    private suspend fun requestLastedProject(page: Int): Result<ArticleList> =
        executeResponse(WanRetrofitClient.serivices.getLastedProject(page))

    private suspend fun requestProjectTypeList() =
        executeResponse(WanRetrofitClient.serivices.getProjectType())

    private suspend fun requestBlogTypeList() =
        executeResponse(WanRetrofitClient.serivices.getBlogType())
}