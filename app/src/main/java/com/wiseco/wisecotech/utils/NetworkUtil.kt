package com.wiseco.wisecotech.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.wiseco.wisecotech.MainApplication
import java.io.IOException
import java.net.HttpURLConnection
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL

class NetworkUtil {
    companion object {
        var NET_CNNT_BAIDU_OK = 1
        var NET_CNNT_BAIDU_TIMEOUT = 2
        var NET_NOT_PREPARE = 3
        var NET_ERROR = 4
        private val TIMEOUT = 3000

        /**
         * Unknown network class
         */
        val NETWORK_CLASS_UNKNOWN = "UNKNOWN"

        /**
         * wifi net work
         */
        val NETWORK_WIFI = "WIFI"

        /**
         * "2G" networks
         */
        val NETWORK_CLASS_2_G = "2G"

        /**
         * "3G" networks
         */
        val NETWORK_CLASS_3_G = "3G"

        /**
         * "4G" networks
         */

        val NETWORK_CLASS_4_G = "4G"

        /**
         * isNetworkAvailable
         *
         * @param context
         * @return
         */
        fun isNetworkAvailable(context: Context): Boolean {
            val manager = context.applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager ?: return false
            val info = manager.activeNetworkInfo
            return if (null == info || !info.isAvailable) false else true
        }

        /**
         * getLocalIpAddress
         *
         * @return
         */
        fun getLocalIpAddress(): String {
            var ret = ""
            try {
                val en = NetworkInterface.getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val intf = en.nextElement()
                    val enumIpAddr = intf.inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress) {
                            ret = inetAddress.hostAddress.toString()
                        }
                    }
                }
            } catch (ex: SocketException) {
                ex.printStackTrace()
            }

            return ret
        }

        /**
         * getNetState
         *
         * @param context
         * @return
         */
        fun getNetState(context: Context): Int {
            try {
                val connectivity = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (connectivity != null) {
                    val networkinfo = connectivity.activeNetworkInfo
                    if (networkinfo != null) {
                        return if (networkinfo.isAvailable && networkinfo.isConnected) {
                            if (!connectionNetwork())
                                NET_CNNT_BAIDU_TIMEOUT
                            else
                                NET_CNNT_BAIDU_OK
                        } else {
                            NET_NOT_PREPARE
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return NET_ERROR
        }

        /**
         * connectionNetwork
         *
         * @return
         */
        private fun connectionNetwork(): Boolean {
            var result = false
            var httpUrl: HttpURLConnection? = null
            try {
                httpUrl = URL("http://www.baidu.com")
                    .openConnection() as HttpURLConnection
                httpUrl.connectTimeout = TIMEOUT
                httpUrl.connect()
                result = true
            } catch (e: IOException) {
            } finally {
                httpUrl?.disconnect()
                httpUrl = null
            }
            return result
        }

        /**
         * net is3G
         *
         * @param context
         * @return boolean
         */
        fun is3G(context: Context): Boolean {
            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            return if (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_MOBILE) {
                true
            } else false
        }

        /**
         * net is Wifi
         *
         * @param context
         * @return boolean
         */
        fun isWifi(context: Context): Boolean {
            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            return if (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI) {
                true
            } else false
        }

        /**
         * Net is 2G
         *
         * @param context
         * @return boolean
         */
        fun is2G(context: Context): Boolean {
            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            return if (activeNetInfo != null && (activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_EDGE
                        || activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
                    .subtype == TelephonyManager.NETWORK_TYPE_CDMA)
            ) {
                true
            } else false
        }

        /**
         * is Wifi Enabled
         */
        fun isWifiEnabled(context: Context): Boolean {
            val mgrConn = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mgrTel = context
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return mgrConn.activeNetworkInfo != null && mgrConn
                .activeNetworkInfo!!.state == NetworkInfo.State.CONNECTED || mgrTel
                .networkType == TelephonyManager.NETWORK_TYPE_UMTS
        }
        /**
         * @param context
         * @return WiFi/4G/3G/2G/CellNetwork 取值限定为前面的值域,大小写敏感.无法判断则为空
         * @Description: 获取网络环境类型
         */
        fun getNetworkType(context: Context): String {
            val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo

            if (networkInfo != null && networkInfo.isConnected) {
                val type = networkInfo.type

                if (type == ConnectivityManager.TYPE_WIFI) {
                    return "WiFi"
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    val telephonyManager = context.getSystemService(
                        Context.TELEPHONY_SERVICE
                    ) as TelephonyManager

                    return if (telephonyManager.networkType == TelephonyManager.NETWORK_TYPE_LTE) {
                        "4G"
                    } else {
                        "3G"
                    }


                }
            }

            return ""
        }


        fun getSimState(context: Context): String {
            var simOperatorName = ""

            try {
                val mTelephonyMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

                simOperatorName = mTelephonyMgr.simOperatorName
                if (mTelephonyMgr.simState == TelephonyManager.SIM_STATE_READY) {
                    simOperatorName = mTelephonyMgr.simOperatorName
                }
            } catch (e: Exception) {
            }

            return simOperatorName
        }

        fun getNetWorkClass(context: Context): String {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            when (telephonyManager.networkType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> return NETWORK_CLASS_2_G

                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> return NETWORK_CLASS_3_G

                TelephonyManager.NETWORK_TYPE_LTE -> return NETWORK_CLASS_4_G

                else -> return NETWORK_CLASS_UNKNOWN
            }
        }

        fun getNetWorkStatus(context: Context): String {
            var netWorkType = NETWORK_CLASS_UNKNOWN

            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo

            if (networkInfo != null && networkInfo.isConnected) {
                val type = networkInfo.type

                if (type == ConnectivityManager.TYPE_WIFI) {
                    netWorkType = NETWORK_WIFI
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    netWorkType = getNetWorkClass(context)
                }
            }

            return netWorkType
        }
        /**
         * 判断网络是否连接
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        open fun isConnected(): Boolean {
            val info = getActiveNetworkInfo()
            return info != null && info.isConnected
        }

        /**
         * 获取活动网络信息
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
         *
         * @return NetworkInfo
         */
        @SuppressLint("MissingPermission")
        open fun getActiveNetworkInfo(): NetworkInfo? {
            return (MainApplication.context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        }
    }
}