package com.wiseco.wisecotech.utils

import com.wiseco.wisecotech.MainApplication
import me.jessyan.autosize.utils.AutoSizeUtils
import me.jessyan.autosize.utils.ScreenUtils

/**
 * 屏幕适配工具类
 */
class ScreenAdaptationUtil {
    companion object {

        //dp转px
        fun dp2px(value: Float): Int {
            return  AutoSizeUtils.dp2px(MainApplication.context, value)
        }

        //sp转px
         fun sp2px(value: Float):  Int{
            return AutoSizeUtils.sp2px(MainApplication.context, value)
        }

        //获取屏幕宽度
          fun screenWidth(): Int {
            val screenSize = ScreenUtils.getScreenSize(MainApplication.context)
            return screenSize[0]//宽度
        }

        //获取屏幕高度
         fun screenHeight(): Int {
            val screenSize = ScreenUtils.getScreenSize(MainApplication.context)
            return screenSize[1]//高度
        }
    }
}