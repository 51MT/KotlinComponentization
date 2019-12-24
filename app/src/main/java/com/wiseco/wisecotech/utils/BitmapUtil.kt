package com.wiseco.wisecotech.utils

import android.content.Intent
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.wiseco.wisecotech.MainApplication

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by dingmingzhong on 2019/11/7.
 */
class BitmapUtil {

    /**
     * Byte 2 image.
     *
     * @param data the data
     * @param path the path
     */
    //byte数组到图片
    fun byte2image(data: ByteArray, path: String) {
        if (data.size < 3 || path == "") return
        try {
            val imageOutput = FileOutputStream(File(path))
            imageOutput.write(data, 0, data.size)
            imageOutput.close()
            println("Make Picture success,Please find image in $path")
        } catch (ex: Exception) {
            println("Exception: $ex")
            ex.printStackTrace()
        }

    }

    companion object {

        /**
         * 获取图片的旋转角度
         *
         * @param filePath
         * @return
         */
        fun getRotateAngle(filePath: String): Int {
            var rotate_angle = 0
            try {
                val exifInterface = ExifInterface(filePath)
                val orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotate_angle = 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotate_angle = 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotate_angle = 270
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return rotate_angle
        }

        /**
         * 旋转图片角度
         *
         * @param angle
         * @param bitmap
         * @return
         */
        fun setRotateAngle(angle: Int, bitmap: Bitmap?): Bitmap? {
            var bitmap = bitmap

            if (bitmap != null) {
                val m = Matrix()
                m.postRotate(angle.toFloat())
                bitmap = Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.width,
                    bitmap.height, m, true
                )
                return bitmap
            }
            return bitmap

        }

        //转换为圆形状的bitmap
        fun createCircleImage(source: Bitmap): Bitmap {
            val length = if (source.width < source.height) source.width else source.height
            val paint = Paint()
            paint.isAntiAlias = true
            val target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(target)
            canvas.drawCircle((length / 2).toFloat(), (length / 2).toFloat(), (length / 2).toFloat(), paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(source, 0f, 0f, paint)
            return target
        }


        /**
         * 图片压缩-质量压缩
         *
         * @param filePath 源图片路径
         * @return 压缩后的路径
         */

        fun compressImage(filePath: String): String {

            //原文件
            val oldFile = File(filePath)


            //压缩文件路径 照片路径/
            val targetPath = oldFile.path
            val quality = 50//压缩比例0-100
            var bm: Bitmap? = getSmallBitmap(filePath)//获取一定尺寸的图片
            val degree = getRotateAngle(filePath)//获取相片拍摄角度

            if (degree != 0) {//旋转照片角度，防止头像横着显示
                bm = setRotateAngle(degree, bm)
            }
            val outputFile = File(targetPath)
            try {
                if (!outputFile.exists()) {
                    outputFile.parentFile!!.mkdirs()
                    //outputFile.createNewFile();
                } else {
                    outputFile.delete()
                }
                val out = FileOutputStream(outputFile)
                bm!!.compress(Bitmap.CompressFormat.JPEG, quality, out)
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
                return filePath
            }

            return outputFile.path
        }

        /**
         * 根据路径获得图片信息并按比例压缩，返回bitmap
         */
        fun getSmallBitmap(filePath: String): Bitmap {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true//只解析图片边沿，获取宽高
            BitmapFactory.decodeFile(filePath, options)
            // 计算缩放比
            options.inSampleSize = calculateInSampleSize(options, 480, 800)
            // 完整解析图片返回bitmap
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(filePath, options)
        }


        fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int, reqHeight: Int
        ): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) {
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            return inSampleSize
        }


        /**
         * 将字符串转换成Bitmap类型
         *
         * @param string the string
         * @return the bitmap
         */
        fun stringtoBitmap(string: String): Bitmap? {
            //将字符串转换成Bitmap类型
            var bitmap: Bitmap? = null
            try {
                val bitmapArray: ByteArray
                bitmapArray = Base64.decode(string, Base64.DEFAULT)
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return bitmap
        }

        /**
         * Byte 2 base 64 string fun string.
         *
         * @param bitmapBytes the bitmap bytes
         * @return the string
         */
        //byte[]转base64
        fun byte2Base64StringFun(bitmapBytes: ByteArray): String {

            return Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
        }

        /**
         * 把字节数组保存为一个文件
         *
         * @param b   the b
         * @param str the str
         * @return the file from bytes
         * @Author HEH
         */
        fun getFileFromBytes(b: ByteArray, str: String): File? {
            var stream: BufferedOutputStream? = null
            var file: File? = null
            try {
                file = File(Environment.getExternalStorageDirectory().absolutePath + "/" + str + ".jpeg")
                val fstream = FileOutputStream(file)
                stream = BufferedOutputStream(fstream)
                stream.write(b)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (stream != null) {
                    try {
                        stream.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }

                }
            }


            return file
        }

        /**
         * 删除文件
         *
         * @param file the file
         */
        fun removeFile(file: File) {
            //如果是文件直接删除
            if (file.isFile) {
                file.delete()
                return
            }
            //如果是目录，递归判断，如果是空目录，直接删除，如果是文件，遍历删除
            if (file.isDirectory) {
                val childFile = file.listFiles()
                if (childFile == null || childFile.size == 0) {
                    file.delete()
                    return
                }
                for (f in childFile) {
                    removeFile(f)
                }
                file.delete()
            }
        }

        /**
         * 保存图片到本地
         * @param bitmap 图片
         * @return 地址
         */
        fun saveBitmap(bitmap: Bitmap): String {
            try {
                // 获取内置SD卡路径
                val sdCardPath = Environment.getExternalStorageDirectory().path
                // 图片文件路径
                var file = File(sdCardPath)
                val files = file.listFiles()
                for (i in files!!.indices) {
                    val file1 = files[i]
                    val name = file1.name
                    if (name.endsWith("twocode.png")) {
                        val flag = file1.delete()
                        Log.d("TAG", "删除 + $flag")
                    }
                }
                val filePath = "$sdCardPath/twocode.png"
                file = File(filePath)
                val os = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                os.flush()
                os.close()

                //把文件插入到系统图库
                //            MediaStore.Images.Media.insertImage( MainApplication.sContext.getContentResolver(),
                //                    file.getAbsolutePath(), "twocode.png", null);

                //保存图片后发送广播通知更新数据库
                val uri = Uri.fromFile(file)
                MainApplication.context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))

                Toast.makeText(MainApplication.context, "二维码保存成功", Toast.LENGTH_SHORT).show()

                return filePath
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

        }
    }

}
