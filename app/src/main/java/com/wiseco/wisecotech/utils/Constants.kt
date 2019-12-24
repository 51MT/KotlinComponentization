package com.wiseco.wisecotech.utils

import com.wiseco.wisecotech.BuildConfig

/**
 * 变量和常量
 */
class Constants private constructor() {
    companion object {
        //--------------变量-----------------
        var Ip: String = BuildConfig.SERVER_API
        const val isLog = true//todo 统一设置是否打日志（上线时为false）
        //-----------------url-------------------------------
        // 获取首次登录协议版本号，APP本地无此版本号或者版本号不对时，需要展示协议内容。
        const  val GETPROTOCOLVERSION = "/api/wisecofin/getProtocolVersion"
        //----------------------------code-----------------------
        const val TM: String="TM"//账号在其他设备登录,导致本设备登出
        const val V: String="V"//token错误
        const val TE: String="TE"//token错误
        const val S: String="S"//成功  api旧版
        const val SU: String="200"//成功  api新版

        const val TAG: String="TAG"//tag

        //-------------------------key------------------------
        //经纬度
        const  val LOCATION_LATITUDE_SP_KEY = "LOCATION_LATITUDE_SP_KEY"

        const val LOCATION_lONGITUDE_SP_KEY = "LOCATION_lONGITUDE_SP_KEY"
        //login
        const val IS_TRUE_NAME = "IS_TRUE_NAME"
        const val UserId = "UserId"
        const val IS_USER_REGIEST = "IS_USER_REGIEST"
        //缓存
        const val CHANNEL_ID = "channelID"//渠道号
        const val WISECO_CHANNEL="WISECO_CHANNEL"//渠道号
        const val IS_APP_FIRST_OPEN="IS_APP_FIRST_OPEN"//是否第一打开本地标记 判断用户是否已经打开过应用


        //-----------------------value-----------------------
    }

}