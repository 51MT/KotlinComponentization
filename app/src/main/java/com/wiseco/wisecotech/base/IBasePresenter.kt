package com.leifu.mvpkotlin.base

/**
 *描述:
 */
interface IBasePresenter<in V : IBaseView> {

    fun attachView(view: V)

    fun detachView()
}