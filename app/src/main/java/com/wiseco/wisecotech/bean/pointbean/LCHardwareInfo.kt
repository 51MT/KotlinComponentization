/*
 * Copyright (C) Beijing Decision Credit Technologies, Ltd
 *
 * ProjectName:WIKKaShen501
 * PackageName:com.woaika.kashen.entity.loan
 * FileName:LCHardwareInfo.java
 * 
 */

package com.wiseco.wisecotech.bean.pointbean

import java.io.Serializable

/**
 * @Description:贷款还信用卡硬件参数信息对象集合体 注意:该对象用作对象实例转json发送服务端,参数不能修改
 * @Author: tmy
 * @Date: 2017-10-20
 * @Version: 1.0
 */
class LCHardwareInfo : Serializable {
    /**
     * ANDROID_ID android系统设备标识，iOS不用传
     */
    var aid = ""
    /**
     * iOS平台广告标示符 andoriod不用传
     */
    var idfa = ""
    /**
     * deviceId 设备ID
     */
    var deviceId = ""
    /**
     * 手机品牌 华为
     */
    var brand = ""
    /**
     * ro.product.model xxx手机型号
     */
    var model = ""
    /**
     * 分辨率
     */
    var resolution = ""
    /**
     * MAC地址“7c:7d:3d:d0:d8:c6”
     */
    var mac = ""

    /**
     * 手机名称“某某某”
     */
    var name = ""

    /**
     * 操作系统类型
     */
    var os = ""
    /**
     * 手机操作系统版本号 5.1
     */
    var osversion = ""
    /**
     * 手机运营商“中国电信”“中国移动”“中国联通”
     */
    var operator = ""
    /**
     * IMEI
     */
    var imei = ""
    /**
     * 国际移动用户识别码IMSI
     */
    var imsi = ""

    /**
     * GPS地址“中国江苏省无锡市滨湖区河埒街道湖滨路”
     */
    var gpsAddress = ""
    /**
     * 经度
     */
    var lng = ""
    /**
     * 纬度
     */
    var lat = ""

    /**
     * 网络状态“wifi”“4g”“3g”“2g”
     */
    var networkType = ""
    /**
     * 手机制造商
     */
    var phoneMarker = ""
    /**
     * 手机字体
     */
    var font = ""
    /**
     * 手机字体大小
     */
    var fontSize = ""
    /**
     * 是否是模拟器 "true" "false"
     */
    var isEmulator = ""
    /**
     * 内网地址
     */
    var intranelIP = ""
    /**
     * 系统语言
     */
    var lang = ""
    /**
     * 设备销往地
     */
    var product = ""
    /**
     * CPU类型
     */
    var cpuType = ""
    /**
     * CPU速度
     */
    var cpuSpeed = ""
    /**
     * CPU硬件
     */
    var cpuHardware = ""
    /**
     * CPU序列化
     */
    var cpuSerial = ""
    /**
     * CPU版本
     */
    var cpuAbi = ""
    /**
     * 手机蓝牙mac地址
     */
    var blueMac = ""

    /**
     * 存储空间
     */
    var totalStorage = ""
    /**
     * 内存大小
     */
    var totalMemory = ""
    /**
     * Android是否root，iOS是否越狱 "true" "false"
     */
    var isRoot = ""

    /**
     * 手机号
     */
    var simPhone = ""

    /**
     * SIM串号序列号
     */
    var simSerial = ""

    /**
     * 手机序列号
     */
    var serialNo = ""
    /**
     * 剩余可用存储空间
     */
    var freeStorage = ""
    /**
     * 剩余可用内存大小
     */
    var freeMemory = ""
    /**
     * 电量，100分制度，double类型
     */
    var bettary = ""
    /**
     * 如果使用WIFI，读取wifi的ssid
     */
    var ssid = ""
    /**
     * 多个DNS以逗号分隔,最多获取2个
     */
    var dns = ""

    override fun toString(): String {
        return "LCHardwareInfo{" +
                "aid='" +
                aid +
                ", idfa='" +
                idfa +
                ", deviceId='" +
                deviceId +
                ", brand='" +
                brand +
                ", model='" +
                model +
                ", resolution='" +
                resolution +
                ", mac='" +
                mac +
                ", name='" +
                name +
                ", os='" +
                os +
                ", osversion='" +
                osversion +
                ", operator='" +
                operator +
                ", imei='" +
                imei +
                ", imsi='" +
                imsi +
                ", gpsAddress='" +
                gpsAddress +
                ", lng='" +
                lng +
                ", lat='" +
                lat +
                ", networkType='" +
                networkType +
                ", phoneMarker='" +
                phoneMarker +
                ", font='" +
                font +
                ", fontSize='" +
                fontSize +
                ", isEmulator='" +
                isEmulator +
                ", intranelIP='" +
                intranelIP +
                ", lang='" +
                lang +
                ", product='" +
                product +
                ", cpuType='" +
                cpuType +
                ", cpuSpeed='" +
                cpuSpeed +
                ", cpuHardware='" +
                cpuHardware +
                ", cpuSerial='" +
                cpuSerial +
                ", cpuAbi='" +
                cpuAbi +
                ", blueMac='" +
                blueMac +
                ", totalStorage='" +
                totalStorage +
                ", totalMemory='" +
                totalMemory +
                ", isRoot='" +
                isRoot +
                ", simPhone='" +
                simPhone +
                ", simSerial='" +
                simSerial +
                ", serialNo='" +
                serialNo +
                ",freeStorage ='" +
                freeStorage +
                ",freeMemory ='" +
                freeMemory +
                ",bettary ='" +
                bettary +
                ",ssid ='" +
                ssid +
                ",dns ='" +
                dns +
                '}'.toString()
    }

    companion object {

        private const val serialVersionUID = 2376600458533386882L
    }
}
