package com.wiseco.wisecotech

import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.wiseco.wisecotech.base.BaseActivity
import com.wiseco.wisecotech.ui.fragments.home.HomeFragment
import com.wiseco.wisecotech.ui.fragments.main.MainTabFragment
import com.wiseco.wisecotech.ui.fragments.splash.SplashFragment
import androidx.core.app.ActivityCompat.finishAfterTransition



class MainActivity : BaseActivity() {

    override fun initData() {


    }

    override fun initView(savedInstanceState: Bundle?) {
        loadRootFragment(R.id.container,  MainTabFragment())

    }

    override fun layoutId(): Int {
       return  R.layout.activity_main
    }


    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            pop()
        } else {
            ActivityCompat.finishAfterTransition(this)
        }
    }
    override fun isTransparentBar(): Boolean {
        return true
    }

}
