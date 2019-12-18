package com.wiseco.wisecotech

import android.content.Context
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ViewTarget


@GlideModule
class MyAppGlideModule : AppGlideModule() {

    /**
     * 这里不开启，避免添加相同的modules两次
     * @return
     */

     override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    /**
     * 通过GlideBuilder设置默认的结构(Engine,BitmapPool ,ArrayPool,MemoryCache等等).
     * @param context
     * @param builder
     */
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //Glide加载图片时，ImageView.setTag冲突问题，（ids中声明tag_glide）
        ViewTarget.setTagId(R.id.tag_glide)
        val diskCacheSizeBytes = (1024 * 1024 * 30).toLong()
        val memorySize = Runtime.getRuntime().maxMemory() / 8// 取1/8最大内存作为最大缓存
        //设置磁盘缓存
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes))
        // 自定义内存和图片池大小
        builder.setMemoryCache(LruResourceCache(memorySize))
        builder.setBitmapPool(LruBitmapPool(memorySize))
        //        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888));
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_RGB_565))


    }

    override fun registerComponents(
        @NonNull context: Context, @NonNull glide: Glide,
        @NonNull registry: Registry
    ) {
        super.registerComponents(context, glide, registry)
    }
}
