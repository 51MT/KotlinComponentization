package com.wiseco.wisecotech.utils

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.wiseco.wisecotech.MainApplication
import com.wiseco.wisecotech.R


/**
 * Created by Administrator on 2018/3/15.
 */

class ToastUtil {

    companion object {


        private var toast: Toast? = null

        fun showToast(info: String) {
            if (toast == null) {
                toast = Toast.makeText(MainApplication.context, info, Toast.LENGTH_SHORT)


                val layout = toast!!.view as LinearLayout
                toast!!.setGravity(Gravity.CENTER, 0, 0)
                layout.setBackgroundColor(Color.parseColor("#00000000"))
                val v = toast!!.view.findViewById<View>(android.R.id.message) as TextView
                v.background = (MainApplication.context.getResources().getDrawable(R.drawable.toast_login_shape))
                v.alpha = 0.8f
                v.setPadding(50, 30, 50, 30)
                v.setTextColor(Color.WHITE)
                v.textSize = 14f
            } else {
                toast!!.setText(info)
            }
            toast!!.show()
        }


        fun showLoginToast(info: String) {

            var toastlogin: Toast? = null
            if (toastlogin == null) {
                toastlogin = Toast.makeText(MainApplication.context, info, Toast.LENGTH_SHORT)


                val layout = toastlogin!!.view as LinearLayout
                toastlogin.setGravity(Gravity.CENTER, 0, 0)
                layout.setBackgroundColor(Color.parseColor("#00000000"))
                val v = toastlogin.view.findViewById<View>(android.R.id.message) as TextView
                v.background = (MainApplication.context.getResources().getDrawable(R.drawable.toast_login_shape))

                v.alpha = 0.8f
                v.setPadding(50, 30, 50, 30)
                v.setTextColor(Color.WHITE)
                v.textSize = 14f
            } else {
                toastlogin.setText(info)
            }
            toastlogin.show()
        }

        fun toastInCenter(context: Context, StringId: Int) {
            val toast = Toast.makeText(context.applicationContext, StringId, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

        fun getTransparentToast(context: Context, StringId: Int, alpha: Int): Toast {
            val toast = Toast.makeText(context, StringId, Toast.LENGTH_SHORT)
            toast.view.background.alpha = alpha//设置背景透明度
            toast.setGravity(Gravity.CENTER, 0, 0)
            return toast
        }
    }

}
