package com.wiseco.wisecotech.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class DateInfoUtil {
    companion object {
        /**
         * getDate
         * yyyy-MM-dd HH:mm:ss
         *
         * @param
         * @param format 如yyyy-MM-dd HH:mm:ss
         * @return
         */
        fun getDateString(milliseconds: Long, format: String): String {
            val sdf = SimpleDateFormat(format)
            return sdf.format(Date(milliseconds))
        }
        //时间转换

        /**
         * Change time string.
         *
         * @param time the time
         * @return the string
         * @throws ParseException the parse exception
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Throws(ParseException::class)
        fun changeTime(time: String): String {

            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+0000")
            val d = format.parse(time)
            val dFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") //HH表示24小时制；

            return dFormat.format(d)
        }
        /**
         * 功能：判断字符串是否为日期格式
         *
         * @param strDate
         * @return
         */
         fun isDate(strDate: String): Boolean {
            val pattern =
                Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$")
            val m = pattern.matcher(strDate)
            return m.matches()
        }
    }
}