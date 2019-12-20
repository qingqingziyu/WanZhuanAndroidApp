package com.cf.wanzhuanandroidapp

import android.app.Application
import android.content.Context
import android.util.Log
import com.cf.wanzhuanandroidapp.di.appModule
import com.cf.wanzhuanandroidapp.model.bean.User
import com.tencent.smtt.sdk.QbSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

/**
 * 入口
 */
class CFApp : Application() {

    /**
     *@作者:陈飞
     *@说明:静态
     *@时间:2019/11/25 15:50
     */
    companion object {
         var CONTEXT: Context by Delegates.notNull() //by:委托模式：非空值强校验
        lateinit var CURENT_USER: User //用户信息bean
    }


    override fun onCreate() {
        super.onCreate()

        CONTEXT = applicationContext

        //创建koinApplication 的实例配置
        startKoin {
            androidContext(this@CFApp)
            modules(appModule)//要加载的模块列表
        }

        //X5内核初始化接口
        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
            }

            override fun onViewInitFinished(p0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinishe 初始化： $p0")
            }
        })
    }

}