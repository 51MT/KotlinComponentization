package com.wiseco.wisecotech.ui.activitys.splash

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.wiseco.wisecotech.MainActivity
import com.wiseco.wisecotech.R
import com.wiseco.wisecotech.base.BaseActivity
import com.wiseco.wisecotech.ui.fragments.splash.SplashFragment

class SplashActivity :BaseActivity(){


    override fun layoutId(): Int {
        return  R.layout.activity_splash
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        loadRootFragment(R.id.container,SplashFragment())

    }
    override fun initData() {

    }

    /**
     * 打开首页
     */
    fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /**
     *     禁止用返回键
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else super.onKeyDown(keyCode, event)
    }
    override fun isTransparentBar(): Boolean {

        return true
    }

}