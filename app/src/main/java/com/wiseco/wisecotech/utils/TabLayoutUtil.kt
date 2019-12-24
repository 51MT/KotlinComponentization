package com.wiseco.wisecotech.utils

import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.wiseco.wisecotech.R

/**
 * tablayout工具类
 */
object TabLayoutUtil {
    /**
     * 设置tablayout文字粗体
     * @param tablayoutLimit
     */
    fun setTextBold(tablayoutLimit: TabLayout) {
        tablayoutLimit.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val view = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.tablayout_text)
                }
                val textView = tab.customView?.findViewById(android.R.id.text1)as TextView
                textView.setTextColor(tablayoutLimit.tabTextColors)
                textView.setTypeface(Typeface.DEFAULT_BOLD)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val view = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.tablayout_text)
                }
                val textView = tab.customView?.findViewById(android.R.id.text1) as TextView
                textView.setTypeface(Typeface.DEFAULT)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    /**
     * Reflex.
     * TabLayout长度
     * @param tabLayout the tab layout
     */
    fun reflex(tabLayout: TabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(Runnable {
            try {
                //拿到tabLayout的mTabStrip属性
                val mTabStrip = tabLayout.getChildAt(0) as LinearLayout

                val dp10 = ScreenAdaptationUtil.dp2px(40f)

                for (i in 0 until mTabStrip.childCount) {
                    val tabView = mTabStrip.getChildAt(i)

                    //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                    val mTextViewField = tabView.javaClass.getDeclaredField("mTextView")
                    mTextViewField.isAccessible = true

                    val mTextView = mTextViewField.get(tabView) as TextView

                    tabView.setPadding(0, 0, 0, 0)

                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    var width = 0
                    width = mTextView.width
                    if (width == 0) {
                        mTextView.measure(0, 0)
                        width = mTextView.measuredWidth
                    }

                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    val params = tabView.layoutParams as LinearLayout.LayoutParams
                    params.width = width
                    params.leftMargin = dp10
                    params.rightMargin = dp10
                    tabView.layoutParams = params

                    tabView.invalidate()
                }

            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        })

    }
}
