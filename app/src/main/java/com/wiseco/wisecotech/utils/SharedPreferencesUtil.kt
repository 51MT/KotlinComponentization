package com.wiseco.wisecotech.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPreferencesUtil {
    companion object {
        private val NAME = "smartAbstraction"
        private var sp: SharedPreferences? = null
        // 从sp中获取一个int值
        fun getInt(
            context: Context, key: String,
            defValue: Int
        ): Int {
            spIsNull(context)
            return sp!!.getInt(key, defValue)
        }

        // 往sp中存boolean值
        fun putInt(context: Context, key: String, value: Int) {
            spIsNull(context)
            sp!!.edit().putInt(key, value).commit()
        }

        // 从sp中获取一个boolean值
        fun getBoolean(
            context: Context, key: String,
            defValue: Boolean
        ): Boolean {
            spIsNull(context)
            return sp!!.getBoolean(key, defValue)
        }

        // 往sp中存boolean值
        fun putBoolean(context: Context, key: String, value: Boolean) {
            spIsNull(context)
            sp!!.edit().putBoolean(key, value).commit()
        }

        // 往sp中存String值
        fun putString(context: Context, key: String, value: String) {
            spIsNull(context)
            sp!!.edit().putString(key, value).commit()
        }

        // 往sp中取String值
        fun getString(
            context: Context, key: String,
            defValue: String
        ): String? {
            spIsNull(context)
            return sp!!.getString(key, defValue)
        }

        private fun spIsNull(context: Context) {
            if (sp == null) {
                sp = context.getSharedPreferences(
                    NAME,
                    Context.MODE_PRIVATE
                )
            }
        }
    }


}
