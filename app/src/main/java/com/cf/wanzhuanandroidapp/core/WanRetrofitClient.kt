package com.cf.wanzhuanandroidapp.core

import android.content.SharedPreferences
import android.net.Network
import com.cf.wanzhuanandroidapp.CFApp
import com.cf.wanzhuanandroidapp.util.NetWorkUtils
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import java.io.File
import java.util.*

object WanRetrofitClient : BaseRetrofitClient() {

    val serivices by lazy { getService(WanService::class.java, WanService.BASE_URL) }

    private val cookieJar by lazy {
        PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(CFApp.CONTEXT)
        )
    }


    override fun handleBuilder(builder: OkHttpClient.Builder) {
        val httpCacheDirectory = File(CFApp.CONTEXT.cacheDir, "responses")
        val cacheSize = 10 * 1024 * 1024L
        val cache = Cache(httpCacheDirectory, cacheSize)
        builder.cache(cache)
            .cookieJar(cookieJar)
            .addInterceptor { chain ->
                var request = chain.request()
                if (!NetWorkUtils.isNetworkAvailable(CFApp.CONTEXT)) {
                    request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
                }
                val respose = chain.proceed(request)
                if (!NetWorkUtils.isNetworkAvailable(CFApp.CONTEXT)) {
                    val maxAge = 60 * 60
                    respose.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .build()
                } else {
                    val maxStale = 60 * 60 * 24 * 28
                    respose.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
                }
                respose
            }
    }
}