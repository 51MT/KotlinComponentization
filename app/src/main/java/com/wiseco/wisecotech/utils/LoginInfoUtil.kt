package com.wiseco.wisecotech.utils

import com.wiseco.wisecotech.MainApplication

class LoginInfoUtil {
    companion object {
        /**
         * 是否注册用户
         *
         * @return the boolean
         */
        fun isRegist(): Boolean {

            return SharedPreferencesUtil.getBoolean(MainApplication.context, Constants.IS_USER_REGIEST, false)
        }

        /**
         * 是否实名
         *
         * @return the boolean
         */
        fun isTrueName(): Boolean {

            return SharedPreferencesUtil.getBoolean(MainApplication.context, Constants.IS_TRUE_NAME, false)
        }


        /**
         * Gets user id.
         *
         * @return the user id
         */
        //获取userid
        fun getUserId(): String {
            return if (isRegist()) {
                SharedPreferencesUtil.getString(MainApplication.context, Constants.UserId, "").toString()
            } else {
                ""
            }

        }
    }
}