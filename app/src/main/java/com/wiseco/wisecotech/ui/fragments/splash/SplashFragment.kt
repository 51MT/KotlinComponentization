package com.wiseco.wisecotech.ui.fragments.splash

import android.os.Bundle
import android.view.View
import com.leifu.mvpkotlin.base.BaseMvpFragment
import com.orhanobut.logger.Logger
import com.wiseco.wisecotech.R
import com.wiseco.wisecotech.aop.AopOnclick
import com.wiseco.wisecotech.utils.LoggerUtil
import com.wiseco.wisecotech.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_splash.*

/**
 * 闪屏页面
 */
class SplashFragment : BaseMvpFragment<SplashFragment,SplashPresenter>(), View.OnClickListener {
    @AopOnclick
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text ->
                LoggerUtil.d("点击")
        }

    }


    override fun createPresenter(): SplashPresenter {
        return SplashPresenter()
    }

    override fun getLayoutId(): Int {

        return  R.layout.fragment_splash
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        //设置沉浸式状态栏
        activity?.let { StatusBarUtil.darkMode(it) }
        //设置顶部view不被状态栏覆盖
        activity?.let { StatusBarUtil.setPaddingSmart(it, text) }
        text.setOnClickListener(this)

        super.initView(view, savedInstanceState)
    }
    override fun lazyLoad() {
    }

}