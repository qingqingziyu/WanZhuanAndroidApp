package com.cf.wanzhuanandroidapp.model.bean

data class User(
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val password: String,
    val type: Int,
    val username: String
)