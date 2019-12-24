package com.leifu.mvpkotlin.base

import com.wiseco.wisecotech.utils.OkgoHttpUtil

/**
 *描述:P层base
 */
open class BaseMvpPresenter<V : IBaseView> : IBasePresenter<V> {
    var mView: V? = null
    protected var httpCancelTag =""
    override fun attachView(view: V) {
        this.mView = view
        httpCancelTag=this.javaClass.simpleName
    }

    override fun detachView() {
        OkgoHttpUtil.cancelHttp(httpCancelTag)
        mView = null
    }
}