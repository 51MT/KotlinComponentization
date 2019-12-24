package com.wiseco.wisecotech.utils

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import com.wiseco.wisecotech.BuildConfig
import com.wiseco.wisecotech.R
import java.util.*

class AppInfoUtil {
    companion object {

        /**
         * Gets version code.
         *
         * @param mContext the m context
         * @return the version code
         */
        fun getVersionCode(mContext: Context): Int {
            var versionCode = 0
            try {
                //获取软件版本号，对应build.gradle下android:versionCode
                versionCode = mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return versionCode
        }

        /**
         * Gets version name.
         *
         * @param context the context
         * @return the version name
         */
        fun getVersionName(context: Context): String {
            var verName = ""
            try {
                //获取版本名称，对应build.gradle下android:versionName
                verName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return verName
        }

        /**
         * Is some app installed boolean.
         *
         * @param context     the context
         * @param packageName the package name
         * @return the boolean
         */
        fun isSomeAppInstalled(context: Context?, packageName: String): Boolean {
            if (context == null || TextUtils.isEmpty(packageName)) {
                throw RuntimeException("AppUtil isAppInstalled 参数为null")
            }
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return if (packageInfo == null) false else true
        }

        /**
         * Gets some app version.
         *
         * @param context     the context
         * @param packageName the package name
         * @return the some app version
         */
        fun getSomeAppVersion(context: Context?, packageName: String): String? {
            if (context == null || TextUtils.isEmpty(packageName)) {
                throw RuntimeException("AppUtil getSomeAppVersion 参数为null")
            }
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return packageInfo?.versionName
        }

        /**
         * Is some process running boolean.
         *
         * @param context     the context
         * @param processName the process name
         * @return the boolean
         */
        fun isSomeProcessRunning(context: Context, processName: String): Boolean {
            val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val list = am.getRunningServices(100)
            for (info in list) {
                if (info.service.className == processName) {
                    return true
                }
            }
            return false
        }

        /**
         * 获取渠道号
         *
         * @param context the context
         * @return channel name
         */
        fun getChannelName(context: Context): String {
            var channelIdStr = ""
            try {
                val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                val channelId = appInfo.metaData.get(Constants.WISECO_CHANNEL)
                channelIdStr = if (TextUtils.isEmpty(channelId!!.toString())) "androidFree" else channelId.toString()
            } catch (e: PackageManager.NameNotFoundException) {
                channelIdStr = "androidFree"
                e.printStackTrace()
            }

            return channelIdStr
        }
        /**
         * Get code push url string.
         *
         * @param isDebug the is debug
         * @return the string
         */
        fun getCodePushUrl(isDebug: Boolean): String {

            return if (isDebug) {
                BuildConfig.CODE_PUSH_URL
            } else {
                BuildConfig.CODE_PUSH_URL
            }
        }
        /**
         * Get code push key string.
         *
         * @return the string
         */
        fun getServerEnv(context: Context): String {
            var index = "00"
            val urls = context.resources.getStringArray(R.array.url)
            val urlList = Arrays.asList(*urls)
            for (i in urlList.indices) {

                if (urlList[i] == Constants.Ip) {
                    if (i < 10) {
                        index = "0$i"
                    } else {
                        index = "" + i
                    }
                }
            }

            return index

        }

        /**
         * 获取应用程序名称
         *
         * @param context the context
         * @return the app name
         */
        @Synchronized
        fun getAppName(context: Context): String {
            try {
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(context.packageName, 0)

                return packageInfo.applicationInfo.loadLabel(context.packageManager).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

        /**
         * 判断app是否处于前台
         * @param context
         * @return
         */
        fun isAppForeground(context: Context): Boolean {
            val activityManager = context.getSystemService(Service.ACTIVITY_SERVICE) as ActivityManager
            val runningAppProcessInfoList = activityManager.runningAppProcesses ?: return false
            for (processInfo in runningAppProcessInfoList) {
                if (processInfo.processName == context.packageName && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true
                }
            }
            return false
        }
    }
}