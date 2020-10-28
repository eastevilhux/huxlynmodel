package com.east.evil.huxlyn.commons

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.auction.framework.utils.AutoDisposeUtil
import com.east.evil.huxlyn.R
import com.uber.autodispose.AutoDisposeConverter


abstract class BaseViewModel(application: Application) : AndroidViewModel(application),ILifecycleObserver{
    lateinit var mLifecycleOwner : LifecycleOwner;

    companion object{
        private const val TAG = "BaseViewModel==>";
    }

    override fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        this.mLifecycleOwner = lifecycleOwner;
    }

    override fun onCreate(owner: LifecycleOwner?) {
        Log.d(TAG,"onCreate")
    }

    override fun onDestroy(owner: LifecycleOwner?) {
        Log.d(TAG,"onDestroy")
    }

    override fun onLifecycleChange(owner: LifecycleOwner?) {
    }

    protected fun <T> bindLifecycle() : AutoDisposeConverter<T> {
        return AutoDisposeUtil.bindLifecycle(mLifecycleOwner!!)
    }

    fun getString(resourceId:Int = R.string.error_default): String {
        return getApplication<Application>().getString(resourceId);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onCreate() {
        Log.d(TAG,"onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onResume() {
        Log.d(TAG,"onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {
        Log.d(TAG,"onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected open fun onPause() {
        Log.d(TAG,"onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onStop() {
        Log.d(TAG,"onStop")
    }

}