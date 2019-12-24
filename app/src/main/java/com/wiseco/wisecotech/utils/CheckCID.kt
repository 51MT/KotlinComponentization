package com.wiseco.wisecotech.utils

import com.wiseco.wisecotech.utils.DateInfoUtil.Companion.isDate
import com.wiseco.wisecotech.utils.StringFormatUtil.Companion.isNumeric
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Hashtable
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by wiseco on 2018/11/21.
 */
class CheckCID {
    companion object {
        /**
         * 身份证验证
         *
         * @param idCard the id card
         * @return boolean
         */
        fun isIdCard(idCard: String?): Boolean {
            if (idCard == null || "" == idCard)
                return false
            return if ("" == validateIDCard(idCard)) true else false
        }


        /**
         * 功能：身份证的有效验证
         *
         * @param
         * @return 有效：返回"" 无效：返回String信息
         */
        fun validateIDCard(cidStr: String?): String? {
            if (cidStr == null) return null
            val IDStr = cidStr.toLowerCase()
            var errorInfo = ""// 记录错误信息
            val ValCodeArr = arrayOf("1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2")
            val Wi = arrayOf("7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2")
            var Ai = ""
            // ================ 号码的长度 15位或18位 ================
            if (IDStr.length != 15 && IDStr.length != 18) {
                errorInfo = "身份证号码长度应该为15位或18位"
                return errorInfo
            }
            // =======================(end)======================== // ================ 数字 除最后以为都为数字 ================
            if (IDStr.length == 18) {
                Ai = IDStr.substring(0, 17)
            } else if (IDStr.length == 15) {
                Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15)
            }
            if (isNumeric(Ai) == false) {
                errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字"
                return errorInfo
            } // =======================(end)======================== // ================ 出生年月是否有效 ================
            val strYear = Ai.substring(6, 10)
            // 年份
            val strMonth = Ai.substring(10, 12)
            // 月份
            val strDay = Ai.substring(12, 14)
            // 月份
            if (isDate("$strYear-$strMonth-$strDay") == false) {
                errorInfo = "身份证生日无效"
                return errorInfo
            }
            val gc = GregorianCalendar()
            val s = SimpleDateFormat("yyyy-MM-dd")
            try {
                if (gc.get(Calendar.YEAR) - Integer.parseInt(strYear) > 150 || gc.time.time - s.parse("$strYear-$strMonth-$strDay")!!.time < 0) {
                    errorInfo = "身份证生日不在有效范围"
                    return errorInfo
                }
            } catch (e: Exception) {
                errorInfo = "身份证生日不在有效范围"
                return errorInfo
            }

            if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
                errorInfo = "身份证月份无效"
                return errorInfo
            }
            if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
                errorInfo = "身份证日期无效"
                return errorInfo
            } // =====================(end)===================== // ================ 地区码时候有效 ================

            val h = GetAreaCode()
            if (h[Ai.substring(0, 2)] == null) {
                errorInfo = "身份证地区编码错误"
                return errorInfo
            }
            // ============================================== // ================ 判断最后一位的值 ================
            var TotalmulAiWi = 0
            for (i in 0..16) {
                TotalmulAiWi = TotalmulAiWi + Integer.parseInt(Ai[i].toString()) * Integer.parseInt(Wi[i])
            }
            val modValue = TotalmulAiWi % 11
            val strVerifyCode = ValCodeArr[modValue]
            Ai = Ai + strVerifyCode
            if (IDStr.length == 18) {
                if (Ai == IDStr == false) {
                    errorInfo = "身份证无效，不是合法的身份证号码"
                    return errorInfo
                }
            } else {
                return ""
            }
            // =====================(end)=====================

            return ""
        }


        private fun GetAreaCode(): Hashtable<*, *> {
            val hashtable = Hashtable<String,String>()
            hashtable.put("11", "北京")
            hashtable.put("12", "天津")
            hashtable.put("13", "河北")
            hashtable.put("14", "山西")
            hashtable.put("15", "内蒙古")
            hashtable.put("21", "辽宁")
            hashtable.put("22", "吉林")
            hashtable.put("23", "黑龙江")
            hashtable.put("31", "上海")
            hashtable.put("32", "江苏")
            hashtable.put("33", "浙江")
            hashtable.put("34", "安徽")
            hashtable.put("35", "福建")
            hashtable.put("36", "江西")
            hashtable.put("37", "山东")
            hashtable.put("41", "河南")
            hashtable.put("42", "湖北")
            hashtable.put("43", "湖南")
            hashtable.put("44", "广东")
            hashtable.put("45", "广西")
            hashtable.put("46", "海南")
            hashtable.put("50", "重庆")
            hashtable.put("51", "四川")
            hashtable.put("52", "贵州")
            hashtable.put("53", "云南")
            hashtable.put("54", "西藏")
            hashtable.put("61", "陕西")
            hashtable.put("62", "甘肃")
            hashtable.put("63", "青海")
            hashtable.put("64", "宁夏")
            hashtable.put("65", "新疆")
            hashtable.put("71", "台湾")
            hashtable.put("81", "香港")
            hashtable.put("82", "澳门")
            hashtable.put("91", "国外")
            return hashtable
        }

    }

}
