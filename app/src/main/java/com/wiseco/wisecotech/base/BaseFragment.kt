package com.wiseco.wisecotech.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import com.wiseco.wisecotech.MainApplication
import com.wiseco.wisecotech.views.MultipleStatusView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 * desc:
 */

abstract class BaseFragment : SupportFragment(), EasyPermissions.PermissionCallbacks {



    lateinit var mContext: Context
    lateinit var mActivity: Activity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), null)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
        mContext = context
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)

    }



    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化 ViewI
     */
    open fun initView(view: View?, savedInstanceState: Bundle?){

    }

    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    override fun onDestroy() {
        super.onDestroy()
        val view = activity!!.currentFocus
        if (view != null) {
            val inputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
        activity?.let { MainApplication.getRefWatcher(it)?.watch(activity) }
    }


    /**
     * 重写要申请权限的Activity或者Fragment的onRequestPermissionsResult()方法，
     * 在里面调用EasyPermissions.onRequestPermissionsResult()，实现回调。
     *
     * @param requestCode  权限请求的识别码
     * @param permissions  申请的权限
     * @param grantResults 授权结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 当权限被成功申请的时候执行回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.i("EasyPermissions", "获取成功的权限$perms")
    }

    /**
     * 当权限申请失败的时候执行的回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        //处理权限名字字符串
        val sb = StringBuffer()
        for (str in perms) {
            sb.append(str)
            sb.append("\n")
        }
        sb.replace(sb.length - 2, sb.length, "")
        //用户点击拒绝并不在询问时候调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(activity, "已拒绝权限" + sb + "并不再询问", Toast.LENGTH_SHORT).show()
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("好")
                .setNegativeButton("不行")
                .build()
                .show()
        }
    }

    override fun start(toFragment: ISupportFragment) {
        if (parentFragment != null) {
            (parentFragment as SupportFragment).start(toFragment)
        } else
            super.start(toFragment)
    }

    override fun start(toFragment: ISupportFragment, launchMode: Int) {
        if (parentFragment != null) {
            (parentFragment as SupportFragment).start(toFragment, launchMode)
        } else
            super.start(toFragment, launchMode)
    }

    override fun startForResult(toFragment: ISupportFragment, requestCode: Int) {
        if (parentFragment != null) {
            (parentFragment as SupportFragment).startForResult(toFragment, requestCode)
        } else
            super.startForResult(toFragment, requestCode)
    }

    override fun pop() {
        if (parentFragment != null || preFragment == null) {
            if (activity != null) {
                activity!!.onBackPressed()
            } else {
                super.pop()
            }
        } else {
            super.pop()
        }
    }

    fun setResult(resultCode: Int) {
        setResult(resultCode, null)
    }

    fun setResult(resultCode: Int, bundle: Bundle?) {
        if (preFragment == null) {
            val intent = Intent()
            if (bundle != null)
                intent.putExtras(bundle)
            activity!!.setResult(resultCode, intent)
            activity!!.finish()
        } else {
            setFragmentResult(resultCode, bundle)
            pop()
        }
    }
}
