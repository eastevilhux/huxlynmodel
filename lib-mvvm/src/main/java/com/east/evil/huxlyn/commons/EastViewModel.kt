package com.east.evil.huxlyn.commons

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.east.evil.huxlyn.entity.Error
import com.east.evil.huxlyn.entity.Loading
import com.east.evil.huxlyn.entity.VMData
import com.east.evil.huxlyn.ext.isMainThread
import com.east.evil.huxlyn.ext.mainThread

abstract class EastViewModel<D : VMData>(application: Application) : BaseViewModel(application) {
    val vmData = MutableLiveData<D>();
    val loading = MutableLiveData<Loading>();

    companion object{
        private const val TAG = "EastViewModel=>";
    }

    open fun initModel(){
        var data = initData();
        data.code= VMData.Code.CODE_DEFAULT;
        data.requestCode = VMData.REQUEST_CODE_DEFAULT;
        vmData.value = data;
        var load = setLoadingTheme();
        load?.let {
            loading.value = it;
        }
    }

    abstract fun initData() : D;

    private fun postData(data:D){
        vmData.postValue(data);
    }

    private fun setData(data : D){
        if(isMainThread()){
            vmData.value = data;
        }else{
            mainThread {
                vmData.value = data;
            }
        }
    }

    /**
     * 设置系统的加载样式,每次在显示加载的时候都将通过调用该方法获得一个加载样式进行展示
     * create by hux at 2020/10/28 0:12
     * @author hux
     * @return
     *      加载样式
     */
    open fun setLoadingTheme() : Loading?{
        return null;
    }

    fun error(code:Int = VMData.ERROR_CODE_DEFAULT,msg:String = getString(),
                   requestCode:Int = VMData.REQUEST_CODE_DEFAULT){
        var error = Error(code,msg);
        var data = vmData.value!!;
        data.error = error;
        data.code = VMData.Code.CODE_ERROR;
        data.msg = msg;
        data.requestCode = requestCode;
        setData(data);
    }

    fun success(code:Int = VMData.SUCCESS_CODE_DEFAULT,msg:String = getString(),
                     requestCode:Int = VMData.REQUEST_CODE_DEFAULT){
        var data = vmData.value!!;
        data.code = VMData.Code.CODE_SUCCESS;
        data.msg = msg;
        data.requestCode = requestCode;
        setData(data);
    }

    fun error(error : Error,requestCode:Int = VMData.REQUEST_CODE_DEFAULT){
        var data = vmData.value!!;
        data.msg = error.errorMsg;
        data.code = VMData.Code.CODE_ERROR;
        data.requestCode = requestCode;
        setData(data);
    }

    fun loading(){
        var loading = setLoadingTheme();
        loading?:let {
            loading = Loading();
        }
        if(isMainThread()){
            this.loading.value = loading;
        }else{
            mainThread {
                this.loading.value = loading;
            }
        }
    }

}