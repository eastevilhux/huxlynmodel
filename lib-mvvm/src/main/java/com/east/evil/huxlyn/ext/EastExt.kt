package com.east.evil.huxlyn.ext

import android.content.Context
import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 当前是否在主线程
 */
fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper();

/**
 * 通过协程  在主线程上运行
 */
fun mainThread(block: () -> Unit) = GlobalScope.launch(Dispatchers.Main) {
    block()
}

fun createRandomNumber(min: Int = 0, max: Int = 10): Int {
    return ((min + Math.random() * (max)).toInt())
}

fun Int.dip2px(content: Context): Int {
    var scale = content.resources.displayMetrics.density;
    return (this * scale+ 0.5f).toInt();
}

fun Int.px2dip(content: Context): Int {
    var scale = content.resources.displayMetrics.density;
    return (this / scale + 0.5f).toInt();
}

fun getScreenSize(content: Context) : Array<Int> {
    var dm = content.resources.displayMetrics;
    return arrayOf(dm.widthPixels,dm.heightPixels);
}