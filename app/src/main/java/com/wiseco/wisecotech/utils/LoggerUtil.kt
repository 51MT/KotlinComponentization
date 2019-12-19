package com.wiseco.wisecotech.utils


import com.orhanobut.logger.Logger
import java.lang.Exception

/**
 * 日志打印工具
 */
class LoggerUtil private constructor() {

    companion object {
        fun v(msg: String) {
            Logger.v(msg)
        }

        fun d(msg: String) {
            Logger.d(msg)
        }

        fun d(vararg args: Any) {
            Logger.d(args)
        }

        fun d(`object`: Any) {
            Logger.d(`object`)
        }


        fun i(msg: String) {
            Logger.i(msg)
        }

        fun w(msg: String) {
            Logger.w(msg)
        }


        fun e(e: Exception, msg: String) {
            Logger.e(e, msg)
        }

        fun json(msg: String) {
            Logger.json(msg)
        }

        fun xml(msg: String) {
            Logger.xml(msg)
        }
    }


}


