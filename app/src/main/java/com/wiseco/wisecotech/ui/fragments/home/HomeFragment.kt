package com.wiseco.wisecotech.ui.fragments.home

import android.os.Bundle
import android.view.View
import com.wiseco.wisecotech.R
import com.wiseco.wisecotech.base.BaseFragment

@Suppress("DEPRECATION")
/**
 * 首页
 */

class HomeFragment : BaseFragment(){
    override fun getLayoutId(): Int = R.layout.activity_main
    private var mTitle: String? = null
    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
    }

    override fun lazyLoad() {

    }


}
