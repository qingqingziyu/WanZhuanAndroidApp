package com.cf.wanzhuanandroidapp.model.bean

data class WanResponse<out T>(val errorCode: Int, val errorMsg: String, val data: T)