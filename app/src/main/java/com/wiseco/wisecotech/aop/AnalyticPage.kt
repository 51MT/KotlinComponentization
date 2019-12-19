package com.wiseco.wisecotech.aop

import android.content.Context
import android.text.TextUtils
import android.util.Log
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

/**
 * 页面aop切点
 */

@Aspect
class AnalyticPage {


    private val returnValue: String? = null
    private var time: Long = 0


    @Before("execution(* android.app.Activity.onResume(..))")
    @Throws(Throwable::class)
    fun aroundOnResume(joinPoint: JoinPoint) {


        try {
            time = System.currentTimeMillis()
            val aThis = joinPoint.getThis() as Context
            // statIdMaps = getStatIdMaps("stat_id.json", aThis);
            val split = aThis.javaClass.name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            Log.d("DMZP", "========" + split[split.size - 1])


        } catch (e1: Exception) {


        } finally {

        }
    }


}
