package com.east.evil.huxlyn.commons

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import com.auction.framework.utils.AutoDisposeUtil
import com.east.evil.huxlyn.R
import com.uber.autodispose.AutoDisposeConverter


abstract class BaseViewModel(application: Application) : AndroidViewModel(application),ILifecycleObserver{
    lateinit var mLifecycleOwner : LifecycleOwner;

    override fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        this.mLifecycleOwner = lifecycleOwner;
    }

    override fun onCreate(owner: LifecycleOwner?) {

    }

    override fun onDestroy(owner: LifecycleOwner?) {

    }

    override fun onLifecycleChange(owner: LifecycleOwner?) {
    }

    protected fun <T> bindLifecycle() : AutoDisposeConverter<T> {
        return AutoDisposeUtil.bindLifecycle(mLifecycleOwner!!)
    }

    fun getString(resourceId:Int = R.string.error_default): String {
        return getApplication<Application>().getString(resourceId);
    }

    open fun onStart(){

    }

    open fun onResume(){

    }

    open fun onStop(){

    }

    open fun onPause(){

    }

    open fun onRestart(){

    }
}