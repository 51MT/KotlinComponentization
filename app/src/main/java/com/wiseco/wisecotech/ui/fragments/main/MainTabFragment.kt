package com.wiseco.wisecotech.ui.fragments.main

import android.os.Bundle
import android.view.View
import com.leifu.mvpkotlin.base.BaseMvpFragment
import com.wiseco.wisecotech.R

class MainTabFragment : BaseMvpFragment<MainTabFragment,MainTabPresenter>(){

    override fun startNetwork() {

    }

    override fun createPresenter(): MainTabPresenter {
        return MainTabPresenter()
    }

    override fun initData() {
    }


    override fun getLayoutId(): Int = R.layout.fragment_main_tab


    override fun initView(view: View?, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
    }



}