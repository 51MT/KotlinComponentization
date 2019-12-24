package com.wiseco.wisecotech.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.wiseco.wisecotech.MainApplication
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

/**
 * Created by wiseco on 2018/12/20.
 */

class LocationUtil {


    companion object {

        private var locationUtils: LocationUtil? = null

        val instance: LocationUtil
            get() {
                if (locationUtils == null) {
                    locationUtils = LocationUtil()
                }
                return locationUtils as LocationUtil
            }
    }
    private var locationManager: LocationManager? = null

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            SharedPreferencesUtil.putString(MainApplication.context, Constants.LOCATION_LATITUDE_SP_KEY, location.latitude.toString() + "")
            SharedPreferencesUtil.putString(MainApplication.context, Constants.LOCATION_lONGITUDE_SP_KEY, location.longitude.toString() + "")

            LoggerUtil.d(location.latitude.toString() + "============" + location.longitude)
        }

        override fun onProviderDisabled(provider: String) {
            LoggerUtil.d("Tobin", "Provider now is disabled..")
        }

        override fun onProviderEnabled(provider: String) {
            LoggerUtil.d("Tobin", "Provider now is enabled..")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    //放入经纬度就可以了
    fun getAddress(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(
                latitude,
                longitude, 1
            )

            if (addresses.size > 0) {
                val address = addresses[0]
                LoggerUtil.d("Location", "+++++$address")
                LoggerUtil.d("Location", "+++++" + address.locality + "=======" + address.thoroughfare)

                val thoroughfare = if (address.thoroughfare == null) "获取失败" else address.thoroughfare
                return address.locality + "," + thoroughfare
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return "获取失败"
        }

        return "获取失败"
    }

    fun getLocations(context: Context): String {
        var strLocation = "0,0"
        val df = DecimalFormat("#####0.0000")
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {


        }
        try {
            //获取系统的服务，
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            //创建一个criteria对象
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_COARSE
            //设置不需要获取海拔方向数据
            criteria.isAltitudeRequired = false
            criteria.isBearingRequired = false
            //设置允许产生资费
            criteria.isCostAllowed = true
            //要求低耗电
            criteria.powerRequirement = Criteria.POWER_LOW
            val provider = locationManager!!.getBestProvider(criteria, true)

            val location = locationManager!!.getLastKnownLocation(provider!!)
            LoggerUtil.d("Location", "Location Provider is $provider")

            LoggerUtil.d("Location", "Location  is " + location!!.latitude + "====" + location.longitude)
            SharedPreferencesUtil.putString(context, Constants.LOCATION_LATITUDE_SP_KEY, location.latitude.toString() + "")
            SharedPreferencesUtil.putString(context, Constants.LOCATION_lONGITUDE_SP_KEY, location.longitude.toString() + "")
            /**
             * 重要函数，监听数据测试
             * 位置提供器、监听位置变化的时间间隔（毫秒），监听位置变化的距离间隔（米），LocationListener监听器
             */
            locationManager!!.requestLocationUpdates(provider, 0, 0f, locationListener)
            Handler().postDelayed({ locationManager!!.removeUpdates(locationListener) }, 2000)

            //第一次获得设备的位置
            if (location != null) {
                strLocation = df.format(location.latitude) + "," + df.format(location.longitude)
                // 耗时操作
                //  getAddress(context, location.getLatitude(), location.getLongitude());

            }


        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return strLocation

    }

}
