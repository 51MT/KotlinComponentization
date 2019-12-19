package com.wiseco.wisecotech.utils

import com.wiseco.wisecotech.BuildConfig

/**
 * 变量和常量
 */
class Constants private constructor(){
    companion object {
        var Ip: String=BuildConfig.SERVER_API
        const val isLog = true//todo 统一设置是否打日志（上线时为false）
    }

}