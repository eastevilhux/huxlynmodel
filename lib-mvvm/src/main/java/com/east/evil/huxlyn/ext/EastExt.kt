package com.east.evil.huxlyn.ext

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