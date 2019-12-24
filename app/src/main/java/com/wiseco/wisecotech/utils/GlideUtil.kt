package com.wiseco.wisecotech.utils

import android.app.Application
import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.wiseco.wisecotech.GlideApp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class GlideUtil {
    companion object {
        object ImageLoaderUtil {

            /**
             * 显示图片
             */
            fun loadImage(context: Context, url: Any, imageView: ImageView) {
                GlideApp.with(context).asDrawable().load(url)
                    .apply(
                        RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .transition(DrawableTransitionOptions().crossFade(800))//交叉渐入渐出(Cross fade from the placeholder)
                    .into(imageView)
            }

            /**
             *
             * 显示图片 圆角显示  ImageView
             *
             * @param context  上下文
             * @param errorimg 错误的资源图片
             * @param url      图片链接
             * @param imgeview 组件
             */

            fun loadCircleImage(
                context: Application,
                url: String, imgeview: ImageView, radius: Int
            ) {
                //设置图片圆角角度
                val roundedCorners = RoundedCorners(radius)
                val options = RequestOptions.bitmapTransform(roundedCorners).override(0, 0)
                Glide.with(context).asDrawable().load(url).apply(options).into(imgeview)

            }


        }
    }
}