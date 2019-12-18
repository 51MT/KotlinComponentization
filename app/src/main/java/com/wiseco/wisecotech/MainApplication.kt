package com.wiseco.wisecotech

import android.app.Application
import android.content.Context
import com.lzy.okgo.OkGo
import com.lzy.okgo.cookie.CookieJarImpl
import com.lzy.okgo.cookie.store.DBCookieStore
import com.lzy.okgo.https.HttpsUtils
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.RefWatcher
import com.wiseco.wisecotech.utils.OkgoHttpUtils
import me.yokeyword.fragmentation.Fragmentation
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import kotlin.properties.Delegates

class MainApplication : Application() {
    private var refWatcher: RefWatcher? = null

    companion object {

        var context: Context by Delegates.notNull()
        var application: Application by Delegates.notNull()
        const val isLog = true//todo 统一设置是否打日志（上线时为false）

        fun getRefWatcher(context: Context): RefWatcher? {
            val myApplication = context.applicationContext as MainApplication
            return myApplication.refWatcher
        }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        application = this
        initFragmentation()//初始化fragment框架
        OkgoHttpUtils.init()//初始化okgo网络请求框架s
        initLoger()//初始化日志框架
    }

    /**
     * 初始化fragment框架
     */
    private fun initFragmentation() {
        //显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
        Fragmentation.builder()
            .stackViewMode(Fragmentation.BUBBLE)
            .debug(BuildConfig.DEBUG)
            .install()
    }

    /**
     * 初始化logger日志打印
     */
    private fun initLoger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // 隐藏线程信息 默认：显示
            .methodCount(0)         // 决定打印多少行（每一行代表一个方法）默认：2
            .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
            .tag("hao_zz")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return isLog
            }
        })
    }


}