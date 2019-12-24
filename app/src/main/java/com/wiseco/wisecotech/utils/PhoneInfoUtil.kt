package com.wiseco.wisecotech.utils

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.*
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import com.alibaba.fastjson.JSON
import com.wiseco.wisecotech.bean.pointbean.DeviceAppayInfo
import com.wiseco.wisecotech.bean.pointbean.LCHardwareInfo
import me.jessyan.autosize.utils.LogUtils
import java.io.*
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

/**
 * 手机信息工具类
 */
class PhoneInfoUtil {
    companion object {
        private var hardWare: LCHardwareInfo? = null
        /**
         * 获取当前手机系统语言。
         *
         * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
         */
        fun getSystemLanguage(): String {
            return Locale.getDefault().language
        }

        /**
         * 获取当前系统上的语言列表(Locale列表)
         *
         * @return 语言列表
         */
        fun getSystemLanguageList(): Array<Locale> {
            return Locale.getAvailableLocales()
        }

        /**
         * 获取当前手机系统版本号
         *
         * @return 系统版本号
         */
        fun getSystemVersion(): String {
            return android.os.Build.VERSION.RELEASE
        }

        fun getAppHardwareInfo(context: Context): LCHardwareInfo {
            if (hardWare == null) {
                hardWare = LCHardwareInfo()
                hardWare?.aid = getAndroidId(context)
                hardWare?.brand = Build.BRAND
                hardWare?.resolution = ScreenAdaptationUtil.screenHeight().toString() + "*" + ScreenAdaptationUtil.screenWidth()
                hardWare?.model = android.os.Build.MODEL
                hardWare?.deviceId = getImeiId(context)//deviceId
                hardWare?.os = "android"//os
                hardWare?.phoneMarker = getPhoneManufacturer()//phoneMarker

                hardWare?.mac = getWifiMAC(context)
                hardWare?.name = BluetoothAdapter.getDefaultAdapter().name

                hardWare?.osversion = getSystemVersion()

                hardWare?.imsi = getIMSI(context)
                hardWare?.networkType = NetworkUtil.getNetworkType(context)
                hardWare?.cpuAbi = Build.CPU_ABI
                hardWare?.totalStorage = getStorageVol()//totalStorage
                hardWare?.totalStorage = getTotalMemory()
            }
            return hardWare as LCHardwareInfo
        }

        /**
         * 获取手机型号
         *
         * @return 手机型号
         */
        fun getSystemModel(): String {
            return android.os.Build.MODEL
        }

        /**
         * 获取手机厂商
         *
         * @return 手机厂商
         */
        fun getDeviceBrand(): String {
            return android.os.Build.BRAND
        }

        fun getUserAgent(context: Context): String {
            var userAgent = ""
            //        APP版本
            // String versionName = SystemUtil.getVersionName(context);
            //        手机型号
            val systemModel = getSystemModel()
            //        系统版本
            val systemVersion = getSystemVersion()
            val deviceBrand = getDeviceBrand()
            userAgent =
                "Android/" + "kuai yi sheng" + AppInfoUtil.getVersionCode(context) + "." + AppInfoUtil.getVersionName(
                    context
                ) + "/" + deviceBrand + "" + systemModel + "/" + systemVersion
            return userAgent
        }


        /**
         * 获取meid
         *
         * @param context
         */
        @SuppressLint("MissingPermission")
        fun getMeid(context: Context): String {

            var meid = ""

            try {
                val mTelephonyMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (mTelephonyMgr != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        meid = mTelephonyMgr.meid
                    } else {
                        val method = mTelephonyMgr.javaClass.getMethod("getDeviceId", Int::class.javaPrimitiveType)
                        //获取MEID号
                        meid = method.invoke(mTelephonyMgr, 2) as String
                    }
                }
            } catch (e: Exception) {
            }

            return meid
        }

        /**
         * 获取运营商sim卡的ICCID号
         *
         * @return ICCID号
         */
        @SuppressLint("MissingPermission")
        fun getICCID(context: Context): String {

            var ccid = ""
            try {
                val tm = context
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (tm.simSerialNumber != null) {
                    ccid = tm.simSerialNumber
                }

            } catch (e: Exception) {
            }

            // 获取sim卡的ICCID号
            return ccid
        }

        /**
         * 获取diveceid,如果deviceid为空 用imei或androidid替换
         *
         * @return
         */
        fun getDeviceIdInfo(context: Context): String {
            val deviceInfo = getDeviceID(context)
            val androidId = getAndroidId(context)
            val meid = getMeid(context)
            if (!TextUtils.isEmpty(deviceInfo)) {
                return deviceInfo
            }
            if (!TextUtils.isEmpty(androidId)) {
                return androidId
            }
            return if (!TextUtils.isEmpty(meid)) {
                meid
            } else ""
        }

        /**
         * get Device Info
         *
         * @param context
         */
        @SuppressLint("MissingPermission")
        fun getDeviceID(context: Context): String {
            var string = ""
            try {
                val mTelephonyMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (mTelephonyMgr != null) {
                    //安卓10设备id为空
                    string = if (mTelephonyMgr.deviceId == null) "" else mTelephonyMgr.deviceId
                }
            } catch (e: Exception) {
            }
            return string
        }

        /**
         * 安卓ID
         */
        @SuppressLint("MissingPermission")
        fun getAndroidId(context: Context): String {
            var ANDROID_ID = ""
            try {
                ANDROID_ID = Settings.System.getString(context.contentResolver, Settings.System.ANDROID_ID)
            } catch (e: Exception) {
            }

            return ANDROID_ID
        }

        /**
         * 获取手机的储存大小，单位是M
         */
        private fun getStorageVol(): String {
            var res = ""
            try {
                val fs = StatFs(Environment.getDataDirectory().path)
                val l3 = fs.blockSizeLong
                val l4 = fs.blockCountLong

                res = (l4 * l3 / 1024 / 1024).toString() + "M"
            } catch (e: Exception) {
                LogUtils.w("getStorageVol(),$e")
            }

            return res
        }

        /**
         * 获取手机的内存大小，单位是M
         */
        private fun getTotalMemory(): String {
            val str1 = "/proc/meminfo"
            val str2: String
            val arrayOfString: Array<String>
            var initial_memory: Long = 0
            var localBufferedReader: BufferedReader? = null
            var localFileReader: FileReader? = null
            try {
                localFileReader = FileReader(str1)
                localBufferedReader = BufferedReader(localFileReader, 8192)

                str2 = localBufferedReader.readLine()
                arrayOfString = str2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                initial_memory = (Integer.valueOf(arrayOfString[1]) / 1024).toLong()
            } catch (e: Exception) {
            } finally {
                try {
                    localFileReader?.close()
                    localBufferedReader?.close()
                } catch (e: Exception) {

                }

            }
            return initial_memory.toString() + "M"
        }

        /**
         * 获取IMSI
         */
        @SuppressLint("MissingPermission")
        fun getIMSI(context: Context): String {
            var subscriberId = ""
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) !== PackageManager.PERMISSION_GRANTED
            ) {

                return subscriberId
            }

            try {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                subscriberId = if (telephonyManager.subscriberId == null) "" else telephonyManager.subscriberId

            } catch (e: Exception) {

                LoggerUtil.e(e, "getIMSI() ")
                return subscriberId
            } finally {
                return subscriberId
            }

        }

        /**
         * 获取手机号
         *
         * @param context
         * @return
         */
        @SuppressLint("MissingPermission")
        fun getPhoneNum(context: Context): String {
            try {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return if (telephonyManager.line1Number == null) "" else telephonyManager.line1Number
            } catch (e: Exception) {
                LogUtils.e("getPhoneNum() ,e = $e")
            }

            return ""
        }

        /**
         * 判断是否包含SIM卡
         *
         * @return 状态
         */
        fun isHasSimCard(context: Context): Boolean {
            val telMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simState = telMgr.simState
            var result = true
            when (simState) {
                TelephonyManager.SIM_STATE_ABSENT -> result = false // 没有SIM卡
                TelephonyManager.SIM_STATE_UNKNOWN -> result = false
            }
            LogUtils.d(if (result) "有SIM卡" else "无SIM卡")
            return result
        }

        fun getBattery(sContext: Context): Double {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val batteryManager = sContext.getSystemService(BATTERY_SERVICE) as BatteryManager
                    return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) / 100.00
                } else {
                    val intent =
                        ContextWrapper(sContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                    return intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(
                        BatteryManager.EXTRA_SCALE,
                        -1
                    ) / 100.00
                }
            } catch (e: Exception) {

            }

            return 0.0
        }

        fun getWifiName(sContext: Context): String {
            var wifiName = ""
            try {
                val wifi = sContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val info = wifi.connectionInfo
                wifiName = info.ssid.toString()
                if (!TextUtils.isEmpty(wifiName) && wifiName.contains("\"")) {
                    wifiName = wifiName.replace("\"", "")
                }
            } catch (e: Exception) {
            }

            return wifiName
        }


        fun installAppList(packageManager: PackageManager): List<String> {
            val myAppInfos = ArrayList<String>()
            try {
                val packageInfos = packageManager.getInstalledPackages(0)
                for (i in packageInfos.indices) {
                    val packageInfo = packageInfos[i] as PackageInfo
                    //过滤掉系统app
                    if (ApplicationInfo.FLAG_SYSTEM and packageInfo.applicationInfo.flags != 0) {
                        continue
                    }
                    myAppInfos.add(packageInfo.applicationInfo.loadLabel(packageManager).toString())


                }
            } catch (e: Exception) {
                LogUtils.e("===============获取应用包信息失败")
            }

            return myAppInfos
        }


        //获取是否存在NavigationBar
        fun checkDeviceHasNavigationBar(context: Context): Boolean {
            var hasNavigationBar = false
            val rs = context.resources
            val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id)
            }
            try {
                val systemPropertiesClass = Class.forName("android.os.SystemProperties")
                val m = systemPropertiesClass.getMethod("get", String::class.java)
                val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
                if ("1" == navBarOverride) {
                    hasNavigationBar = false
                } else if ("0" == navBarOverride) {
                    hasNavigationBar = true
                }
            } catch (e: Exception) {

            }

            return hasNavigationBar

        }

        // 获取NavigationBar高度
        fun getNavigationBarHeight(context: Context): Int {
            val resources = context.resources
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return resources.getDimensionPixelSize(resourceId)
        }

        fun getAppayDevice(context: Context): DeviceAppayInfo? {

            var deviceinfo: DeviceAppayInfo? = null
            if (deviceinfo != null) {
                return deviceinfo
            }
            deviceinfo = DeviceAppayInfo()
            // 设备ID，
            deviceinfo!!.gpsLatitude = SharedPreferencesUtil.getString(context, Constants.LOCATION_LATITUDE_SP_KEY, "")
            deviceinfo!!.gpsLongitude =
                SharedPreferencesUtil.getString(context, Constants.LOCATION_lONGITUDE_SP_KEY, "")
            deviceinfo!!.mobileBattery = getBattery(context).toString()
            deviceinfo!!.deviceMode = (getSystemModel())
            deviceinfo!!.deviiceId = getDeviceIdInfo(context)
            deviceinfo!!.systemModel = getSystemModel()
            deviceinfo!!.deviceBrand = getDeviceBrand()
            deviceinfo!!.bootTime =
                DateInfoUtil.getDateString(
                    System.currentTimeMillis() - SystemClock.elapsedRealtimeNanos() / 1000000,
                    "yyyy-MM-dd HH:mm:ss"
                )
            deviceinfo!!.deviceId = getDeviceIdInfo(context)
            deviceinfo!!.wifiName = getWifiName(context)
            deviceinfo!!.wifiMac = getWifiMAC(context)
            deviceinfo!!.deviceImei = getDeviceIdInfo(context)
            deviceinfo!!.meid = getMeid(context)
            deviceinfo!!.iccid = getICCID(context)
            deviceinfo!!.imsi = getIMSI(context)
            deviceinfo!!.systemVersion = getSystemVersion()
            deviceinfo!!.appVersion = AppInfoUtil.getVersionName(context)
            deviceinfo!!.appList = JSON.toJSONString(installAppList(context.packageManager))
            deviceinfo!!.mac = getWifiMAC(context)
            deviceinfo!!.onVersion = "Android " + getSystemVersion()
            deviceinfo!!.cpu = ""
            deviceinfo!!.diskSpace = ""
            deviceinfo!!.marketRAM = ""
            deviceinfo!!.ram = ""
            if (isHasSimCard(context)) {//（关键值： 0: 没有sim卡。 1: sim正常。）
                deviceinfo!!.installSimFlag = "1"
            } else {
                deviceinfo!!.installSimFlag = "0"
            }

            return deviceinfo
        }

        /**
         * 获取当前手机的IMEI号码
         */
        @SuppressLint("MissingPermission")
        private fun getImeiId(context: Context): String {
            var imei = ""
            try {
                val telephonyManager = context.getSystemService(
                    Context.TELEPHONY_SERVICE
                ) as TelephonyManager
                imei = telephonyManager.deviceId
            } catch (e: Exception) {
                LogUtils.w("getImeiId(),$e")
                return ""
            }

            return imei
        }

        /**
         * 获取ip
         *
         * @param context the context
         * @return the ip address
         */
        fun getIPAddress(context: Context): String? {
            val info = (context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            if (info != null && info.isConnected) {
                if (info.type == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                    try {
                        //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                        val en = NetworkInterface.getNetworkInterfaces()
                        while (en.hasMoreElements()) {
                            val intf = en.nextElement()
                            val enumIpAddr = intf.inetAddresses
                            while (enumIpAddr.hasMoreElements()) {
                                val inetAddress = enumIpAddr.nextElement()
                                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                    return inetAddress.getHostAddress()
                                }
                            }
                        }
                    } catch (e: SocketException) {
                        e.printStackTrace()
                    }

                } else if (info.type == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val wifiInfo = wifiManager.connectionInfo
                    return intIP2StringIP(wifiInfo.ipAddress)
                }
            } else {
                //当前无网络连接,请在设置中打开网络
            }
            return null
        }

        /**
         * 将得到的int类型的IP转换为String类型
         *
         * @param ip the ip
         * @return string
         */
        fun intIP2StringIP(ip: Int): String {
            return (ip and 0xFF).toString() + "." +
                    (ip shr 8 and 0xFF) + "." +
                    (ip shr 16 and 0xFF) + "." +
                    (ip shr 24 and 0xFF)
        }

        /**
         * 获取当前手机的制造商
         */
        private fun getPhoneManufacturer(): String {
            return try {
                Build.MANUFACTURER
            } catch (e: Exception) {
                ""
            }

        }

        fun isMIUI(): Boolean {
            return "xiaomi".equals(getPhoneManufacturer(), ignoreCase = true)
        }

        fun isEMUI(): Boolean {
            return "HUAWEI".equals(getPhoneManufacturer(), ignoreCase = true)
        }

        fun isOPPO(): Boolean {
            return "OPPO".equals(getPhoneManufacturer(), ignoreCase = true)
        }

        fun isVIVO(): Boolean {
            return "vivo".equals(getPhoneManufacturer(), ignoreCase = true)
        }

        /**
         * is Wifi Enabled
         */
        fun getWifiMAC(context: Context): String {
            var mac: String = ""
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                mac = getLocalMacAddress(context)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                mac = getMacAddress()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mac = getMacFromHardware()
            }
            return mac
        }

        /**
         * Android 6.0-Android 7.0 获取mac地址
         */
        fun getMacAddress(): String {
            var macSerial: String = ""
            var str: String = ""

            try {
                val pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address")
                val ir = InputStreamReader(pp.inputStream)
                val input = LineNumberReader(ir)

                while (null != str) {
                    str = input.readLine()
                    if (str != null) {
                        macSerial = str.trim { it <= ' ' }//去空格
                        break
                    }
                }
            } catch (ex: IOException) {
                // 赋予默认值
                ex.printStackTrace()
            }

            return macSerial
        }

        /**
         * Android 7.0之后获取Mac地址
         * 遍历循环所有的网络接口，找到接口是 wlan0
         * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
         * @return
         */
        fun getMacFromHardware(): String {
            try {
                val all = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (nif in all) {
                    if (nif.name != "wlan0")
                        continue
                    val macBytes = nif.hardwareAddress ?: return ""
                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        res1.append(String.format("%02X:", b))
                    }
                    if (!TextUtils.isEmpty(res1)) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

        /**
         * 获取6.0之前WIFIMac
         *
         * @param context
         * @return
         */
        private fun getLocalMacAddress(context: Context): String {
            var mac = ""
            try {
                val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                var info: WifiInfo? = null
                if (wifi != null) {
                    info = wifi.connectionInfo
                }

                if (info != null) {
                    mac = info.macAddress
                }
                if (TextUtils.isEmpty(mac)) {
                    mac = ""
                }
            } catch (e: Exception) {

            }

            return mac
        }
    }
}