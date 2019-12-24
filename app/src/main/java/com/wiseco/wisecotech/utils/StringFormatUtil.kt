package com.wiseco.wisecotech.utils

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class StringFormatUtil {
    companion object {
        /**
         * 验证手机格式
         *
         * @param mobiles the mobiles
         * @return the boolean
         */
        fun isMobileNumber(mobiles: String): Boolean {
            /*		移动：134、135、136、137、138、139、150、151、157(TD)、
        158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9		*/
            val telRegex = "[1][3456789]\\d{9}"
            //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，
            // "\\d{9}"代表后面是可以是0～9的数字，有9位。

            return if (TextUtils.isEmpty(mobiles))
                false
            else
                mobiles.matches(telRegex.toRegex())
        }
        /**
         * 验证输入的名字是否为“中文”或者是否包含“·”
         *
         * @param name the name
         * @return the boolean
         */
        fun isLegalName(name: String): Boolean {
            return if (name.contains("·") || name.contains("•") || name.contains(".") || name.contains(".")) {
                name.matches("^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$".toRegex())
            } else {
                name.matches("^[\\u4e00-\\u9fa5]+$".toRegex())
            }
        }
        /**
         * 判断是否是全中文
         *
         * @param name the name
         * @return the boolean
         */
        fun isChinese(name: String): Boolean {

            return name.matches("^[\\u4e00-\\u9fa5]+$".toRegex())
        }
        /**
         * 判断是否是中文可以包含字母
         *
         * @param name the name
         * @return the boolean
         */
        fun isChineseWithCode(name: String): Boolean {

            val regEx = "^[A-Za-z0-9\\u4E00-\\u9FA5_-]+$"

            return name.matches(regEx.toRegex())

        }

        /**
         * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
         *
         * @param str 需要处理的字符串
         * @return 处理完之后的字符串 string
         */
        fun addComma(str: String): String {
            val decimalFormat = DecimalFormat("#,##0.00")
            return decimalFormat.format(java.lang.Double.parseDouble(str))
        }

        /**
         * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
         *
         * @param str 需要处理的字符串
         * @return 处理完之后的字符串 string
         */
        fun addCommaInt(str: String): String? {
            if (!TextUtils.isEmpty(str)) {
                val decimalFormat = DecimalFormat(",###")
                return decimalFormat.format(java.lang.Double.parseDouble(str))
            }
            return str
        }
        /**
         * 判断性别和年龄
         *
         * @param CardCode the card code
         * @return the car info
         */
        @Throws(Exception::class)
        fun getCarInfo(CardCode: String): Map<String, Any> {
            val map = HashMap<String, Any>()
            val year = CardCode.substring(6).substring(0, 4)// 得到年份
            val yue = CardCode.substring(10).substring(0, 2)// 得到月份
            // String day=CardCode.substring(12).substring(0,2);//得到日
            val sex: String
            if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
                sex = "女"
            } else {
                sex = "男"
            }
            val date = Date()// 得到当前的系统时间
            val format = SimpleDateFormat("yyyy-MM-dd")
            val fyear = format.format(date).substring(0, 4)// 当前年份
            val fyue = format.format(date).substring(5, 7)// 月份
            // String fday=format.format(date).substring(8,10);
            var age = 0
            if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
                age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1
            } else {// 当前用户还没过生
                age = Integer.parseInt(fyear) - Integer.parseInt(year)
            }
            map["sex"] = sex
            map["age"] = age
            return map
        }

        /**
         * 功能：判断字符串是否为数字
         *
         * @param str
         * @return
         */

         fun isNumeric(str: String): Boolean {
            val pattern = Pattern.compile("[0-9]*")
            val isNum = pattern.matcher(str)
            return isNum.matches()
        }
        /**
         * 设置字符串中数字的颜色和大小
         *
         * @param str the str
         * @return the num color
         */
        fun setNumColor(str: String): SpannableStringBuilder {
            val style = SpannableStringBuilder(str)
            for (i in 0 until str.length) {
                val a = str[i]
                if (a >= '0' && a <= '9') {
                    style.setSpan(ForegroundColorSpan(Color.RED), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    style.setSpan(AbsoluteSizeSpan(40), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    style.setSpan(TypefaceSpan("default-bold"), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                }
            }
            return style
        }

    }
}