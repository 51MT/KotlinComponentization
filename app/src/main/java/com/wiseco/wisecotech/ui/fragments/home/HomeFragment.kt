package com.wiseco.wisecotech.ui.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.wiseco.wisecotech.R
import com.wiseco.wisecotech.base.BaseFragment
import com.wiseco.wisecotech.ui.fragments.main.MainTabFragment

@Suppress("DEPRECATION")
/**
 * 首页
 */

class HomeFragment : BaseFragment(){
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: HomeFragment? = null
            get() {
                if (field==null)
                {
                    field= HomeFragment()
                }
                return field
            }
        @Synchronized
        fun get(): HomeFragment {
            return instance!!
        }
    }
    override fun initData() {

    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
    }



}
