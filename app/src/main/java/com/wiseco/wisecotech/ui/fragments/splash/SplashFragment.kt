package com.wiseco.wisecotech.ui.fragments.splash

import android.os.Bundle
import android.view.View
import com.leifu.mvpkotlin.base.BaseMvpFragment
import com.wiseco.wisecotech.R
import com.wiseco.wisecotech.aop.AopOnclick
import com.wiseco.wisecotech.bean.httpbean.ProtocolVersionInfo
import com.wiseco.wisecotech.ui.activitys.splash.SplashActivity
import com.wiseco.wisecotech.utils.*

/**
 * 闪屏页面
 */
class SplashFragment : BaseMvpFragment<SplashFragment, SplashPresenter>(), View.OnClickListener {


    @AopOnclick
    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.text ->
//                LoggerUtil.d("点击")
        }

    }


    override fun createPresenter(): SplashPresenter {
        return SplashPresenter()
    }

    override fun getLayoutId(): Int {

        return  R.layout.fragment_splash
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        //设置沉浸式状态栏
        activity?.let { StatusBarUtil.darkMode(it) }

        LocationUtil.instance.getLocations(mContext)//todo 应该放到获取权限之后  获取定位信息
        SharedPreferencesUtil.putString(mContext,Constants.CHANNEL_ID, AppInfoUtil.getChannelName(mContext))//缓存渠道号
        mPresenter?.getVersionData()
    }

    /**
     * 获取版本号返回数据
     */
    fun setVersionData(response: ProtocolVersionInfo) {
        LoggerUtil.d(response.version.toString())
        gotoNextPage()
    }

    override fun initData() {


    }

    override fun startNetwork() {

    }

    /**
     * 进入下级页面
     */
    fun gotoNextPage() {
        // 从sp中取boolean值，判断用户是否已经打开过应用
        val isAppFirstOpen = SharedPreferencesUtil.getBoolean(mContext, Constants.IS_APP_FIRST_OPEN, true)
        if (isAppFirstOpen) {
            // 第一次打开应用，跳转到激活界面
//            start(WelcomeActivity::class.java)
//            pop()
            LoggerUtil.d(getString(R.string.splash_first_open))

        } else {
            // 不是第一次打开应用，跳转到主界面
//            start(AdActivity::class.java)
//            pop()
            LoggerUtil.d(getString(R.string.splash_no_first_open))

        }
        if (activity is SplashActivity) {
            (activity as SplashActivity).startMainActivity()
        }

    }

}