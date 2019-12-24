package com.wiseco.wisecotech.ui.activitys.web

import com.wiseco.wisecotech.R
import com.wiseco.wisecotech.base.BaseActivity

class WebActivity : BaseActivity() {
    override fun isTransparentBar(): Boolean {
        return false
    }

    override fun layoutId(): Int {
        return  R.layout.activity_webview
    }

    override fun initData() {
    }

}