package com.wiseco.wisecotech

import android.os.Bundle
import com.wiseco.wisecotech.base.BaseActivity
import com.wiseco.wisecotech.ui.fragments.splash.SplashFragment

class MainActivity : BaseActivity() {
    override fun initData() {


    }

    override fun initView(savedInstanceState: Bundle?) {

//        loadRootFragment(R.id.container, HomeFragment.getInstance(""))// 加载根Fragment
        loadRootFragment(R.id.container, SplashFragment())// 加载根Fragment
    }

    override fun layoutId(): Int {
       return  R.layout.activity_main
    }


}
