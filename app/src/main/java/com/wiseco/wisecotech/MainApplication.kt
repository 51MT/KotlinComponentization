package com.wiseco.wisecotech

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.wiseco.wisecotech.utils.Constants
import com.wiseco.wisecotech.utils.OkgoHttpUtil
import me.yokeyword.fragmentation.Fragmentation
import kotlin.properties.Delegates



class MainApplication : Application() {
    private var refWatcher: RefWatcher? = null

    companion object {

        var context: Context by Delegates.notNull()
        var application: Application by Delegates.notNull()


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
        OkgoHttpUtil.init()//初始化okgo网络请求框架s
        initLoger()//初始化日志框架
        refWatcher = setupLeakCanary()
    }
    /**
     *  初始化内存泄漏检测
     */
    private fun setupLeakCanary(): RefWatcher {
        return if (LeakCanary.isInAnalyzerProcess(this)) {
            RefWatcher.DISABLED
        } else LeakCanary.install(this)
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
            .showThreadInfo(true)  // 隐藏线程信息 默认：显示
            .methodCount(3)         // 决定打印多少行（每一行代表一个方法）默认：2
            .methodOffset(2)        // 设置调用堆栈的函数偏移值，默认是 0
            .tag(Constants.TAG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return Constants.isLog
            }
        })
    }


}