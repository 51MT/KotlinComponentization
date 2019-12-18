package com.leifu.mvpkotlin.base

import com.wiseco.wisecotech.utils.OkgoHttpUtils

/**
 *描述:
 */
open class BaseRxPresenter<V : IBaseView> : IBasePresenter<V> {
    var mView: V? = null
    protected val CANCEL_TAG = System.currentTimeMillis().toString()
    override fun attachView(view: V) {
        this.mView = view
    }

    override fun detachView() {
        OkgoHttpUtils.cancelHttp(CANCEL_TAG)
        mView = null
    }
}