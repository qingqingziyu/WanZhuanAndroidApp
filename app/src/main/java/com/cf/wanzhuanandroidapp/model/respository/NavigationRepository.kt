package com.cf.wanzhuanandroidapp.model.respository

import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.core.WanRetrofitClient
import com.cf.wanzhuanandroidapp.model.api.BaseRepository
import com.cf.wanzhuanandroidapp.model.bean.Navigation

class NavigationRepository : BaseRepository() {
    suspend fun getNavigation(): Result<List<Navigation>> {
        return safeApiCall(call = { requestNavigation() }, errorMessage = "获取数据失败")
    }

    private suspend fun requestNavigation(): Result<List<Navigation>> =
        executeResponse(WanRetrofitClient.serivices.getNavigation())
}