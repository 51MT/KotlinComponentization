package com.wiseco.wisecotech.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class KeyBoardUtil {
    companion object {
        /**
         * 往剪贴板上设置内容
         * @param context 上线文
         * @param content 设置的内容
         */
        fun setClipboard(context: Context, content: String): Boolean {
            try {
                //获取剪贴板管理器
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // 创建普通字符型ClipData
                val mClipData = ClipData.newPlainText("Label", content)
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData)
                return true
            } catch (e: Exception) {
                return false
            }

        }
    }
}