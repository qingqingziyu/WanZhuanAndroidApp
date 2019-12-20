package com.cf.wanzhuanandroidapp.core

import com.cf.wanzhuanandroidapp.model.bean.*
import retrofit2.http.*

interface WanService {
    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @GET("/navi/json")
    suspend fun getNavigation(): WanResponse<List<Navigation>>

    @FormUrlEncoded
    @POST("/article/query/{page}/json")
    suspend fun searchHot(@Path("page") page: Int, @Field("k") key: String): WanResponse<ArticleList>

    @GET("/friend/json")
    suspend fun getWebSites(): WanResponse<List<Hot>>

    @GET("/hotkey/json")
    suspend fun getHot(): WanResponse<List<Hot>>

    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(
        @Field("username") userName: String, @Field("password") passWord: String, @Field("repassword") rePassWord:
        String
    ): WanResponse<User>

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(@Field("username") userName: String, @Field("password") passWord: String): WanResponse<User>

    //退出登录
    @GET("/user/logout/json")
    suspend fun logOut(): WanResponse<Any>

    @GET("/banner/json")
    suspend fun getBanner(): WanResponse<List<Banner>>

    @GET("/article/listproject/{page}/json")
    suspend fun getLastedProject(@Path("page") page: Int): WanResponse<ArticleList>

    @GET("/project/tree/json")
    suspend fun getProjectType(): WanResponse<List<SystemParent>>

    @GET("/wxarticle/chapters/json")
    suspend fun getBlogType(): WanResponse<List<SystemParent>>

    @GET("/tree/json")
    suspend fun getSystemType(): WanResponse<List<SystemParent>>

    @GET("/article/list/{page}/json")
    suspend fun getSystemTypeDetail(@Path("page") page: Int, @Query("cid") cid: Int): WanResponse<ArticleList>

    @GET("/lg/collect/list/{page}/json")
    suspend fun getCollectArticles(@Path("page") page: Int): WanResponse<ArticleList>

    @POST("/lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") id: Int): WanResponse<ArticleList>

    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun cancelCollectArticle(@Path("id") id: Int): WanResponse<ArticleList>

    @GET("/project/list/{page}/json")
    suspend fun getProjectTypeDetail(@Path("page") page: Int, @Query("cid") cid: Int): WanResponse<ArticleList>

    @GET("/article/list/{page}/json")
    suspend fun getHomeArticles(@Path("page") page: Int): WanResponse<ArticleList>

    @GET("/user_article/list/{page}/json")
    suspend fun getSquareArticleList(@Path("page") page: Int): WanResponse<ArticleList>
}