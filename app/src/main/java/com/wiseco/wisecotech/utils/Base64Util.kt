package com.wiseco.wisecotech.utils

import android.text.TextUtils
import android.util.Base64

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

object Base64Util {
    /**
     * Base64加密字符串
     * @param content -- 代加密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
    fun base64Encode(content: String, charsetName: String): String {
        var charsetName = charsetName
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "UTF-8"
        }
        try {
            val contentByte = content.toByteArray(charset(charsetName))
            return Base64.encodeToString(contentByte, Base64.DEFAULT)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * Base64解密字符串
     * @param content -- 待解密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
    fun base64Decode(content: String, charsetName: Charset): String {
        var charsetName = charsetName
        if (charsetName==null) {
            charsetName =Charsets.UTF_8
        }
        val contentByte = Base64.decode(content, Base64.DEFAULT)
        try {
            return String(contentByte, charsetName)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return ""
    }
}
