package com.wiseco.wisecotech.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.cookie.CookieJarImpl
import com.lzy.okgo.cookie.store.DBCookieStore
import com.lzy.okgo.exception.HttpException
import com.lzy.okgo.https.HttpsUtils
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.HttpParams
import com.lzy.okgo.model.Response
import com.wiseco.wisecotech.MainApplication
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.logging.Level

open class OkgoHttpUtils {
    companion object {
        /**
         * 初始化网络框架爱
         */
        fun init() {
            //设置公共参数
            val commonParams = HttpParams()
            commonParams.put("appver", "")     //param支持中文,直接传,不要自己编码
            commonParams.put("devicetype", "android")
            //设置公共头
            val commonHeaders = HttpHeaders()
            commonHeaders.put("key", "value")

            val builder = OkHttpClient.Builder()
            //log相关
            val loggingInterceptor = HttpLoggingInterceptor("OkGo")
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)        //log打印级别，决定了log显示的详细程度
            loggingInterceptor.setColorLevel(Level.INFO)                               //log颜色级别，决定了log在控制台显示的颜色
            if (MainApplication.isLog) {
                builder.addInterceptor(loggingInterceptor)                                 //添加OkGo默认debug日志（上线时去掉）
            }
            //超时时间设置，默认60秒
            builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)      //全局的读取超时时间
            builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)     //全局的写入超时时间
            builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)   //全局的连接超时时间

            //自动管理cookie（或者叫session的保持），以下几种任选其一就行
            //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
            builder.cookieJar(CookieJarImpl(DBCookieStore(MainApplication.context)))              //使用数据库保持cookie，如果cookie不过期，则一直有效
            //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

            //https相关设置，以下几种方案根据需要自己设置
            //方法一：信任所有证书,不安全有风险
            val sslParams1 = HttpsUtils.getSslSocketFactory()
            builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager)
            //方法二：自定义信任规则，校验服务端证书
            //        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
            //方法三：使用预埋证书，校验服务端证书（自签名证书）
            //        try {
            //            HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("mobpieces.cer"));
            //            builder.sslSocketFactory(sslParams3.sSLSocketFactory, sslParams3.trustManager);
            //        } catch (IOException e) {
            //            e.printStackTrace();
            //        }
            //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
            //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
            // 详细说明看GitHub文档：https://github.com/jeasonlzy/
            OkGo.getInstance()
                .init(MainApplication.application)                           //必须调用初始化
                .setRetryCount(0).addCommonParams(commonParams).addCommonHeaders(commonHeaders).okHttpClient =
                builder.build()             //建议设置OkHttpClient，不设置会使用默认的

        }


        /**
         * 交易成功返回T，需要具体的javaBean接收
         *
         * @param url 服务器地址
         * @param jsonObject json
         * @param clazz 实体类
         * @param baseRepoCallBack 回调
         */
        open fun post(
            url: String,tag:String ,jsonObject: JSONObject, clazz: Class<*>,
            baseRepoCallBack: BaseRepoCallBack
        ) {
            if (isConnected()) {//是否有网络

                OkGo.post<String>(url).tag(tag)
                    .upJson(jsonObject)//上传map的话可以map转jsonObject
                    .execute(object : StringCallback() {
                        override fun onSuccess(response: Response<String>) {
                            success(response, baseRepoCallBack, clazz, true)
                        }

                        override fun onError(response: Response<String>) {
                            error(response, baseRepoCallBack)
                        }
                    })
            } else {
                baseRepoCallBack.onError("网络连接失败")
            }
        }

        /**
         * 交易成功返回T，需要具体的javaBean接收
         *
         * @param url 服务器地址
         * @param jsonObject json
         * @param clazz 实体类
         * @param baseRepoCallBack 回调
         */
        open fun put(
            url: String,tag:String, jsonObject: JSONObject, clazz: Class<*>,
            baseRepoCallBack: BaseRepoCallBack
        ) {
            if (isConnected()) {//是否有网络
                OkGo.put<String>(Constants.Ip + url).tag(tag)
                    .upJson(jsonObject)//上传map的话可以map转jsonObject
                    .execute(object : StringCallback() {
                        override fun onSuccess(response: Response<String>) {
                            success(response, baseRepoCallBack, clazz, true)
                        }

                        override fun onError(response: Response<String>) {
                            error(response, baseRepoCallBack)
                        }
                    })
            } else {
                baseRepoCallBack.onError("网络连接失败")
            }
        }

        /**
         * 交易成功返回T，需要具体的javaBean接收
         *
         * @param url 服务器地址
         * @param parameter 参数
         * @param clazz 实体类
         * @param baseRepoCallBack 回调
         */
        open operator fun get(
            url: String,tag:String, parameter: String, clazz: Class<*>,
            baseRepoCallBack: BaseRepoCallBack
        ) {
            if (isConnected()) {//是否有网络
                val requestUrl: String
                if (TextUtils.isEmpty(parameter)) {
                    requestUrl = Constants.Ip + url
                } else {
                    requestUrl = Constants.Ip + url + "/{" + parameter + "}"
                }
                OkGo.get<String>(requestUrl).tag(tag).execute(object : StringCallback() {
                    override fun onSuccess(response: Response<String>) {
                        success(response, baseRepoCallBack, clazz, true)
                    }

                    override fun onError(response: Response<String>) {
                        error(response, baseRepoCallBack)
                    }
                })
            } else {
                baseRepoCallBack.onError("网络连接失败")
            }
        }

        /**
         * 交易成功返回字符串
         *
         * @param url 服务器地址
         * @param jsonObject json
         * @param baseRepoCallBack 回调
         */
        open fun post(url: String,tag:String, jsonObject: JSONObject, baseRepoCallBack: BaseRepoCallBack) {
            if (isConnected()) {//是否有网络
                OkGo.post<String>(url).tag(tag).upJson(jsonObject)//上传map的话可以map转jsonObject
                    .execute(object : StringCallback() {
                        override fun onSuccess(response: Response<String>) {
                            success(response, baseRepoCallBack, null, false)
                        }

                        override fun onError(response: Response<String>) {
                            error(response, baseRepoCallBack)
                        }
                    })
            } else {
                baseRepoCallBack.onError("网络连接失败")
            }
        }

        /**
         * access to server successfully
         */
        private fun success(
            response: Response<String>, baseRepoCallBack: BaseRepoCallBack, clazz: Class<*>?,
            isClass: Boolean
        ) {
            if (200 == response.code()) {
                val body = response.body()
                if (!TextUtils.isEmpty(body)) {//非空判断
                    try {
                        val jsonObject = JSONObject(body)
                        val returnCode = jsonObject.optString("ReturnCode")//根据自己业务取成功标识
                        if ("000000" == returnCode) {//根据自己业务取成功标识
                            if (isClass) {
                                baseRepoCallBack.onSuccess(JSON.parseObject(body, clazz))
                            } else {
                                baseRepoCallBack.onSuccess(body)
                            }
                        } else {
                            val returnMsg = jsonObject.optString("ReturnMsg")//根据自己业务取失败信息
                            val errorId = jsonObject.optString("ErrorId")//根据自己业务取失败信息
                            baseRepoCallBack.onFailed(returnMsg, errorId)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }
        }

        /**
         * access to server unsuccessfully
         */
        private fun error(response: Response<String>, baseRepoCallBack: BaseRepoCallBack) {
            val r = response.exception
            val msg: String
            if (r is HttpException) {
                msg = "网络异常，请检查您的网络状态"
            } else if (r is SocketTimeoutException) {
                msg = "网络超时，请检查您的网络状态"
            } else if (r is ConnectException) {
                msg = "网络中断，请检查您的网络状态"
            } else if (r is UnknownError) {
                msg = "未知错误"
            } else {
                msg = r.message.toString()
            }
            baseRepoCallBack.onError(msg)
        }

        /**
         * 取消网络请求
         */
        open fun cancelHttp(tag:String) {
            OkGo.getInstance().cancelTag(tag)
        }

        open interface BaseRepoCallBack {
            /**
             * 交易成功
             */
            fun <T> onSuccess(response: T)

            /**
             * 交易失败
             */
            fun onFailed(response: String, code: String)

            /**
             * 网络错误
             */
            fun onError(response: String)
        }

        /**
         * 判断网络是否连接
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        private fun isConnected(): Boolean {
            val info = getActiveNetworkInfo()
            return info != null && info.isConnected
        }

        /**
         * 获取活动网络信息
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
         *
         * @return NetworkInfo
         */
        @SuppressLint("MissingPermission")
        private fun getActiveNetworkInfo(): NetworkInfo? {
            return (MainApplication.context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        }
    }
}