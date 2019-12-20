package com.cf.wanzhuanandroidapp.model.respository

import com.cf.wanzhuanandroidapp.CFApp
import com.cf.wanzhuanandroidapp.core.Result
import com.cf.wanzhuanandroidapp.core.WanRetrofitClient
import com.cf.wanzhuanandroidapp.model.api.BaseRepository
import com.cf.wanzhuanandroidapp.model.bean.User
import com.cf.wanzhuanandroidapp.util.Preference
import com.cf.wanzhuanandroidapp.view.SharedPreferencesUtils
import com.google.gson.Gson

class LoginRepository : BaseRepository() {
    private var isLogin = SharedPreferencesUtils.getParam(
        CFApp.CONTEXT, Preference.IS_LOGIN, false
    ) as Boolean
    private var userJson = SharedPreferencesUtils.getParam(CFApp.CONTEXT, Preference.IS_LOGIN, "")

    suspend fun login(userName: String, passWord: String): Result<User> {
        return safeApiCall(call = { requestLogin(userName, passWord) }, errorMessage = "登录失败!")
    }

    private suspend fun requestLogin(userName: String, passWord: String): Result<User> {
        val response = WanRetrofitClient.serivices.login(userName, passWord)
        return executeResponse(response, {
            val user = response.data
            isLogin = true
            userJson = Gson().toJson(user)
            CFApp.CURENT_USER = user
        })
    }


    suspend fun register(userName: String, passWord: String): Result<User> {
        return safeApiCall(call = { requestRegister(userName, passWord) }, errorMessage = "注册失败")
    }

    private suspend fun requestRegister(userName: String, passWord: String): Result<User> {
        val response = WanRetrofitClient.serivices.register(userName, passWord, passWord)
        return executeResponse(response, {
            requestLogin(userName, passWord)
        })
    }

}
