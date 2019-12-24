package com.wiseco.wisecotech.ui.fragments.splash

import com.leifu.mvpkotlin.base.BaseMvpPresenter
import com.wiseco.wisecotech.bean.httpbean.ProtocolVersionInfo
import com.wiseco.wisecotech.utils.Constants
import com.wiseco.wisecotech.utils.OkgoHttpUtil
import org.json.JSONObject

class SplashPresenter : BaseMvpPresenter<SplashFragment>() {
    /**
     * 获取首次登录协议版本号，APP本地无此版本号或者版本号不对时，需要展示协议内容。
     */
    fun getVersionData() {
        OkgoHttpUtil.post(
            Constants.GETPROTOCOLVERSION,
            httpCancelTag,
            JSONObject(),
            ProtocolVersionInfo::class.java,
            object :
                OkgoHttpUtil.BaseRepoCallBack<ProtocolVersionInfo> {
                override fun onSuccess(response: ProtocolVersionInfo) {
                    mView?.setVersionData(response)
                }

                override fun onFailed(response: String?, code: String?) {
                    mView?.gotoNextPage()
                }

                override fun onError(response: String) {
                    mView?.gotoNextPage()
                }
            })

    }


}

