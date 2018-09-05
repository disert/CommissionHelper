package com.meishai.commission.helper.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.ColorInt
import android.support.design.widget.TabLayout
import android.text.Spannable
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.meishai.commission.helper.GlobalContext
import com.meishai.commission.helper.R
import com.meishai.commission.helper.cons.Constants.REQUEST_CODE_CONTACT
import com.meishai.commission.helper.view.dialog.PublicDialog
import java.io.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * 功 能：一些简单的小方法 <br></br>
 * 时 间：2016/6/8 17:37 <br></br>
 */
object AndriodUtils {


    fun <T : Any> ctlData(res: MutableList<T>?, temp: MutableList<T>?, page: Int): MutableList<T>? {

        if (page == 1) {
            return temp
        }
        if (res == null) {
            return temp
        }
        if (temp == null || temp.isEmpty()) {
            return res
        }
        //使用equals去重
        for (i in temp.indices) {
            val t = temp[i]
            var equal = false
            for (j in res.indices) {
                val t1 = res[j]
                if (t == t1) {
                    equal = true
                    break
                }
            }
            if (!equal) res.add(t)
        }
        return res
    }

    /**
     * 修改TabLayout的指导线与字体长度一致
     *
     * @param tabLayout
     */
    fun reflex(tabLayout: TabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post {
            try {
                //拿到tabLayout的mTabStrip属性
                val mTabStrip = tabLayout.getChildAt(0) as LinearLayout

                val dp10 = dip2px(tabLayout.context, 10f)

                for (i in 0 until mTabStrip.childCount) {
                    val tabView = mTabStrip.getChildAt(i)

                    //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                    val mTextViewField = tabView.javaClass.getDeclaredField("mTextView")
                    mTextViewField.isAccessible = true

                    val mTextView = mTextViewField.get(tabView) as TextView

                    tabView.setPadding(0, 5, 0, 5)

                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    var width = 0
                    width = mTextView.width + 20
                    if (width == 0) {
                        mTextView.measure(0, 0)
                        width = mTextView.measuredWidth
                    }

                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据
                    //tabView的宽度来设置的
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
        }

    }

    fun getColor(colorId: Int): Int {

        return GlobalContext.context!!.resources.getColor(colorId)
    }

    /**
     * 根据long类型时间获取时分秒格式的字符串
     *
     * @param time 时间 单位秒
     * @return 类似00:10:15的时间字符串
     */
    fun getTimeHmsString(time: Long): String {

        if (time <= 0) return "00:00:00"
        val hours = time / (60 * 60)
        val minutes = (time - hours * (60 * 60)) / 60
        val seconds = time - hours * (60 * 60) - minutes * 60
        val sb = StringBuilder()
        sb.append(if (hours < 10) "0$hours" else hours).append(":")
                .append(if (minutes < 10) "0$minutes" else minutes).append(":")
                .append(if (seconds < 10) "0$seconds" else seconds)
        return sb.toString()
    }

    fun formatTime(time: Long): String {

        return if (time > 9) time.toString() + "" else "0$time"
    }

    fun isEmpty(list: List<*>?): Boolean {

        return list == null || list.isEmpty()
    }


    /**
     * 根据图片的宽高来设置imageview的大小
     *
     * @param context
     * @param widht
     * @param height
     */
    fun fullImageSize(context: Activity, widht: Int, height: Int): Point {

        val dm = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)
        val bl = dm.widthPixels / widht
        val imgHeight = height * bl

        val p = Point()
        p.x = dm.widthPixels
        p.y = imgHeight
        return p
    }

    @JvmOverloads
    fun append(builder: StringBuilder, key: String, value: Any, needStr: Boolean = true): StringBuilder {

        builder.append("\"").append(key).append("\":")
        if (needStr)
            builder.append("\"").append(value).append("\",")
        else
            builder.append(value).append(",")
        return builder
    }


    /**
     * 产生单个随机数
     */
    fun randomNum(total: Int): Int {

        val random = Random()
        return random.nextInt(total)
    }


    fun getAnimFloat(start: Float, end: Float, listener: ValueAnimator.AnimatorUpdateListener): Animator {

        val animator = ValueAnimator.ofFloat(start, end)
        animator.addUpdateListener(listener)
        return animator
    }

    fun getAnimInt(start: Int, end: Int, listener: ValueAnimator.AnimatorUpdateListener): Animator {

        val animator = ValueAnimator.ofInt(start, end)
        animator.addUpdateListener(listener)
        return animator
    }

    @JvmOverloads
    fun splitString2Array(url: String, regex: String = "\\|"): ArrayList<String> {

        val mSlideImgs = ArrayList<String>()
        if (!TextUtils.isEmpty(url)) {
            val split = url.split(regex.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (split != null && split.size > 0) {
                for (u in split) {
                    if (!TextUtils.isEmpty(u)) mSlideImgs.add(u)
                }
            } else {
                mSlideImgs.add(url)
            }
        }
        return mSlideImgs
    }

    fun formatText(strId: Int, vararg replace: Any): String {

        if (strId == -1) return ""
        val sInfoFormat = GlobalContext.context!!.resources.getString(strId)
        return String.format(sInfoFormat, *replace)
    }

    /**
     * 是否在该时间区间
     *
     * @param current
     * @param start
     * @param end
     * @return
     */
    fun isTimeOut(current: Long, start: Long, end: Long): Boolean {

        return current >= start && current <= end
    }

    /**
     * 获取屏幕宽高
     *
     * @param context
     */
    fun getScreenHW(context: Activity): Point {

        val dm = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)

        val p = Point()
        p.x = dm.widthPixels
        p.y = dm.heightPixels
        return p
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {

        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {

        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    fun sp2px(context: Context, spValue: Float): Int {

        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {

        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 功 能：改变span指定位置的文字大小 <br></br>
     * 时 间：2016/6/8 18:13 <br></br>
     *
     * @param start 开始位置,从0开始
     * @param end   截止位置
     * @param sp    文字大小
     */
    fun onChangTextSize(context: Context, span: Spannable, start: Int, end: Int, sp: Int) {

        span.setSpan(AbsoluteSizeSpan(AndriodUtils.dip2px(context, sp.toFloat())), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun onChangTextBold(span: Spannable, start: Int, end: Int) {

        span.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    /**
     * 功 能：改变span指定位置的文本颜色 <br></br>
     * 时 间：2016/6/8 18:13 <br></br>
     *
     * @param start 开始位置,从0开始
     * @param end   截止位置
     * @param color 改变的颜色
     */
    fun onChangTextColor(span: Spannable, start: Int, end: Int, @ColorInt color: Int) {

        span.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }


    /**
     * 功 能：创建一个确认的的dialog,内部简单的显示提示文字 <br></br>
     * 时 间：2016/6/16 12:59 <br></br>
     *
     * @param left     左侧按钮的提示文字
     * @param right    右侧按钮的提示文字
     * @param msg      提示信息
     * @param negative 中间点击事件
     * @param positive 右侧点击事件
     */
    fun showSubmitDialog(context: Context, left: String, right: String, msg: String, negative: View.OnClickListener?, positive: View.OnClickListener): PublicDialog {

        val dialog = PublicDialog.Builder(context).setContent(msg)
                .setCancel(left)
                .setConform(right)
                .setCancelListenr(negative)
                .setConformListenr(positive).build()
        dialog.show()
        return dialog
    }

    fun showSubmitDialog(context: Context, title: String, left: String, right: String, msg: String, negative: View.OnClickListener, positive: View.OnClickListener) {

        PublicDialog.Builder(context).setContent(msg)
                .setTitle(title)
                .setCancel(left)
                .setConform(right)
                //                .setRightBg(R.color.color_ffe527)
                .setCancelListenr(negative)
                .setConformListenr(positive)
                .show()
    }


    /**
     * 功 能：获取屏幕的宽度 <br></br>
     * 时 间：2016/6/22 16:14 <br></br>
     */
    fun getScreenWidth(activity: Activity): Int {

        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    /**
     * 功 能：获取屏幕的高度 <br></br>
     * 时 间：2016/6/22 16:14 <br></br>
     */
    fun getScreenHeight(activity: Activity): Int {

        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    /**
     * 功 能：获得当前版本号1.0.1 <br></br>
     * 时 间：2016/6/23 16:25 <br></br>
     */
    fun getVersionName(context: Context): String {

        try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionName
        } catch (e: Exception) {
            return ""
        }

    }

    /**
     * 功 能：获得当前版本号2 <br></br>
     * 时 间：2016/6/23 16:25 <br></br>
     */
    fun getVersion(context: Context): Int {

        try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionCode
        } catch (e: Exception) {
            return 0
        }

    }


    /**
     * 功 能：判断是否有指定的权限 <br></br>
     * 时 间：2016/6/24 16:26 <br></br>
     */
    fun hasPermission(context: Context, permission: String): Boolean {

        val pm = context.packageManager
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission(permission, context.packageName)
    }

    @JvmOverloads
    fun permission(context: Activity, permissions: Array<String>, requestCode: Int = REQUEST_CODE_CONTACT): Boolean {

        if (Build.VERSION.SDK_INT >= 23) {
            //            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (str in permissions) {
                if (context.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    context.requestPermissions(permissions, requestCode)
                    return false
                }
            }
            return true
        }
        return true
    }

    /**
     * 功 能：获取调用相册的Intent  <br></br>
     * 时 间：2016/6/24 16:26 <br></br>
     */
    val album: Intent
        get() = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    /**
     * 功 能：返回调用系统裁剪的Intent,并将文件保存在指定文件中 <br></br>
     * 时 间：2016/6/24 16:50 <br></br>
     */
    fun getCrop(uri: Uri, file: File): Intent {

        //        Intent intent = new Intent("com.android.camera.action.CROP");
        ////        intent.setDataAndType(uri, "icon/*");
        //        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        //        intent.putExtra("crop", "true");
        //
        //
        //
        //        //sdk>=24
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //
        //            Uri outPutUri = Uri.fromFile(file);
        //            intent.setDataAndType(uri, "image/*");
        //            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        //            intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
        //            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //
        //        } else {
        //            Uri outPutUri = Uri.fromFile(file);
        //            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
        //                String url = GetImagePath.getPath(GlobalContext.getContext(), uri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
        //                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        //            } else {
        //                intent.setDataAndType(uri, "image/*");
        //            }
        //            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        //        }
        //        // aspectX aspectY 是宽高的比例
        //        intent.putExtra("aspectX", 1);
        //        intent.putExtra("aspectY", 1);
        //
        //        // outputX,outputY 是剪裁图片的宽高
        //        intent.putExtra("outputX", 300);
        //        intent.putExtra("outputY", 300);
        //        intent.putExtra("return-data", false);// 不返回data数据
        //        intent.putExtra("noFaceDetection", true); // 不启用人脸识别
        //        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        ////        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        //        return intent;
        val intent1 = Intent("com.android.camera.action.CROP")
        intent1.setDataAndType(uri, "image/*")
        intent1.putExtra("crop", "true")
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))//
        intent1.putExtra("aspectX", 1)
        intent1.putExtra("aspectY", 1)
        intent1.putExtra("outputFormat", Bitmap.CompressFormat.JPEG)
        intent1.putExtra("outputX", 300)
        intent1.putExtra("outputY", 300)
        intent1.putExtra("scale", true)
        intent1.putExtra("scaleUpIfNeeded", true)
        intent1.putExtra("noFaceDetection", true) // 不启用人脸识别
        intent1.putExtra("return-data", false)
        return intent1
    }

    /**
     * 功 能：根据屏幕宽度,margin,计算出剩余宽度等分(待间隔)的宽度 <br></br>
     * 时 间：2016/7/4 17:24 <br></br>
     *
     * @param number 将宽度分为几分
     * @param margin 布局外侧的margin需要去除. dp值
     */
    fun getScreenImageViewWidth(context: Activity, number: Int, margin: Int): Int {

        var width = getScreenWidth(context)
        width = ((width - dip2px(context, margin.toFloat())) / (number + number.toFloat() / 10)).toInt()
        return width
    }

    /**
     * 功 能：返回相册里选中文件的实际地址 <br></br>
     * 时 间：2016/7/5 11:37 <br></br>
     */
    fun getAlbumImagePath(context: Context, uri: Uri): String {

        val strings = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, strings, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val picturePath = cursor.getString(cursor.getColumnIndex(strings[0]))
            cursor.close()
            return picturePath
        }
        return ""
    }

    /**
     * 功 能：加载本地图片 <br></br>
     * 时 间：2016/7/5 15:13 <br></br>
     */
    fun onLoadImage(context: Context, file: File, view: ImageView) {

        Glide.with(context).load(file).into(view)
    }


    @JvmOverloads
    fun onLoadHttpImage(context: Context, url: String, view: ImageView, defResId: Int = R.mipmap.default_img) {

        if (context is Activity && context.isDestroyed) return
        //        Glide.with(context).applyDefaultRequestOptions(options).load(url).into(view);
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.errorOf(defResId))
                .into(view)
    }

    fun replaceAll(content: String, old: String, newText: String): String {

        return content.replace(old.toRegex(), newText)
    }

    /**
     * 功 能：返回默认格式的时间类型 yyyy-MM-dd HH:mm<br></br>
     * 时 间：2016/7/20 16:32 <br></br>
     */
    fun getFormatTime(time: Long): String {

        return SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(time))
    }

    @JvmOverloads
    fun getDecimal(decimal: Double, pattern: String = "0.00"): String {

        return DecimalFormat(pattern).format(decimal)
    }

    /**
     * 功 能：返回指定格式的时间类型 yyyy-MM-dd HH:mm<br></br>
     * 时 间：2016/7/20 16:32 <br></br>
     */
    fun getFormatTime(format: String, time: Long): String {

        return SimpleDateFormat(format).format(Date(time))
    }

    /**
     * 功 能：将图片按照分辨率大小来压缩,目前指定为1280*720 <br></br>
     * 时 间：2016/7/22 19:11 <br></br>
     *
     * @param srcFile 要压缩的图片
     * @param file    压缩之后的图片
     * @param maxKb
     */
    @Throws(IOException::class)
    fun getImage(srcFile: String, file: File, maxKb: Int) {

        var degree = 0
        // 从指定路径下读取图片，并获取其EXIF信息
        val exifInterface = ExifInterface(srcFile)
        // 获取图片的旋转信息
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
        }
        val newOpts = BitmapFactory.Options()
        //开始读入图片，此时把options.inJustDecodeBounds 设true了
        newOpts.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeFile(srcFile, newOpts)//此时返回bm为空

        val w = newOpts.outWidth
        val h = newOpts.outHeight
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        val hh = 1280f//这里设置高度为1280f
        val ww = 720f//这里设置宽度为720f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        var be = 1//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (newOpts.outWidth / ww).toInt()
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (newOpts.outHeight / hh).toInt()
        }
        if (be <= 0) be = 1
        newOpts.inSampleSize = be//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false
        bitmap = BitmapFactory.decodeFile(srcFile, newOpts)
        onCompressImage(bitmap, file, degree, maxKb)//压缩好比例大小后再进行质量压缩
    }

    @Throws(FileNotFoundException::class)
    fun onCompressImage(image: Bitmap, file: File, rote: Int, maxKb: Int) {

        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (outputStream.toByteArray().size / 1024 > maxKb || options == 20) {    //循环判断如果压缩后图片是否大于500kB,大于继续压缩
            outputStream.reset()//重置outputStream即清空outputStream
            options -= 10//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, outputStream)//这里压缩options%，把压缩后的数据存放到baos中
        }
        val isBm = ByteArrayInputStream(outputStream.toByteArray())//把压缩后的数据baos存放到ByteArrayInputStream中
        val bitmap = BitmapFactory.decodeStream(isBm, null, null)//把ByteArrayInputStream数据生成图片
        var returnBm: Bitmap? = null

        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(rote.toFloat())
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        if (returnBm == null) {
            returnBm = bitmap
        }
        if (bitmap != returnBm) {
            bitmap.recycle()
        }
        val bos = BufferedOutputStream(FileOutputStream(file))
        returnBm!!.compress(Bitmap.CompressFormat.JPEG, options, bos)
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    fun getImageAbsolutePath(context: Activity?, imageUri: Uri?): String? {

        if (context == null || imageUri == null)
            return null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(imageUri)) {
                val id = DocumentsContract.getDocumentId(imageUri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("icon" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } // MediaStore (and general)
        else if ("content".equals(imageUri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(imageUri)) imageUri.lastPathSegment else getDataColumn(context, imageUri, null, null)
        } else if ("file".equals(imageUri.scheme, ignoreCase = true)) {
            return imageUri.path
        }// File
        return null
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {

        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {

        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {

        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {

        return "com.google.android.apps.photos.content" == uri.authority
    }


    /**
     * 功 能：判断手机是否联网 <br></br>
     * 时 间：2016/8/6 11:16 <br></br>
     */

    fun isNetworkAvailable(context: Context): Boolean {

        val cm = context.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo ?: return false
        return netInfo.isAvailable
    }

    /**
     * 功 能：转化弧度 <br></br>
     * 时 间：2016-8-13 15:48 <br></br>
     */
    private fun rad(d: Double): Double {

        return d * Math.PI / 180.0
    }

    /**
     * 修改文件夹权限为公开分组模式
     *
     * @param dir
     */
    fun changeDirPerm(dir: String) {

        val p: Process
        try {
            p = Runtime.getRuntime().exec("chmod 777 $dir")
            p.waitFor()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun getString(resId: Int): String {

        return GlobalContext.context!!.getString(resId)
    }

    private fun emojiTranc(emoji: String): String {

        val H: Char
        val L: Char
        val code: Int
        if (emoji.length == 2) {
            H = emoji[0]
            // 取出高位
            L = emoji[1]
            // 取出低位
            code = (H.toInt() - 0xD800) * 0x400 + 0x10000 + L.toInt() - 0xDC00
            // 转换算法
            return "&#$code;"
        }
        return emoji
    }

    private fun emojiRecode(c: Int): String {

        val h: Int
        val l: Int
        h = (Math.floor(((c - 0x10000) / 0x400).toDouble()) + 0xD800).toInt() // 高位

        l = (c - 0x10000) % 0x400 + 0xDC00 // 低位
        val ch = h.toChar()
        val cl = l.toChar()
        return StringBuilder().append(ch).append(cl).toString()
    }

    /**
     * 将输入的内容编码
     *
     * @param content
     * @return
     */
    fun emojiEncode(content: String): String {

        val reg = "[\\ud800\\udc00-\\udbff\\udfff]"
        val pattern = Pattern.compile(reg)
        val matcher = pattern.matcher(content)
        var retValue = content
        while (matcher.find()) {
            val group = matcher.group()
            val ch = emojiTranc(group)
            retValue = retValue.replace(group, ch)
        }
        return retValue
    }

    /**
     * 将取出内容解码
     *
     * @param content
     * @return
     */
    fun emojiDecode(content: String): String {

        val reg = "&#(\\d+);"
        val pattern = Pattern.compile(reg)
        val matcher = pattern.matcher(content)
        var retValue = content
        while (matcher.find()) {
            val group = matcher.group(1)
            val ch = emojiRecode(Integer.parseInt(group))
            retValue = retValue.replace(matcher.group(), ch)
        }
        return retValue
    }

}