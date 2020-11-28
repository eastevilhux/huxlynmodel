package com.east.evil.huxlyn.commons

import android.app.Application
import android.app.Service
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.east.evil.huxlyn.entity.Error
import com.east.evil.huxlyn.entity.Loading
import com.east.evil.huxlyn.entity.Target
import com.east.evil.huxlyn.entity.VMData
import com.east.evil.huxlyn.ext.isMainThread
import com.east.evil.huxlyn.ext.mainThread
import java.io.Serializable

abstract class EastViewModel<D : VMData>(application: Application) : BaseViewModel(application) {
    val vmData = MutableLiveData<D>();
    val loading = MutableLiveData<Loading>();
    val target = MutableLiveData<Target>();

    companion object{
        private const val TAG = "EastViewModel=>";
    }

    open fun initModel(){
        var data = initData();
        data.code= VMData.Code.CODE_DEFAULT;
        data.requestCode = VMData.REQUEST_CODE_DEFAULT;
        vmData.value = data;
        if(needLoadingOnInit()){
            loadingOnInit();
        }
    }

    /**
     * 加载时的加载动画效果
     * create by hux at 2020-11-28 19:12
     * @author hux
     * @return
     */
    open fun loadingOnInit(){
        val loading = Loading()
        loading.loadingFlag = true;
        loading.type = Loading.LoadingType.TYPE_DIALOG;
        this.loading.value = loading;
    }

    /**
     * 在初始化的时候，是否需要加载动画
     * create by hux at 2020-11-28 19:12
     * @author hux
     * @return
     *      是否需要加载动画，默认不需要
     */
    open fun needLoadingOnInit(): Boolean {
        return false
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

    open fun showLoading(loadingType : Loading.LoadingType = Loading.LoadingType.TYPE_DIALOG){
        var loading = this.loading.value;
        loading?:let {
            loading = Loading();
        }
        loading!!.loadingFlag = true;
        loading!!.type = loadingType;
        if(isMainThread()){
            this.loading.value = loading;
        }else{
            mainThread {
                this.loading.value = loading;
            }
        }
    }

    open fun dismissLoading(){
        var loading = this.loading.value;
        loading?.let {
            it.loadingFlag = false;
            if(isMainThread()){
                this.loading.value = loading;
            }else{
                mainThread {
                    this.loading.value = loading;
                }
            }
        }
    }


    fun open(target: Target){
        if(isMainThread()){
            this.target.value = target;
        }else{
            mainThread {
                this.target.value = target;
            }
        }
    }

    fun open(cls:Class<*>,isFinish : Boolean = false){
        var tag = Target.Builder(cls)
            .isFinish(isFinish)
            .builder();
        if(isMainThread()){
            target.value = tag;
        }else{
            mainThread {
                target.value = tag;
            }
        }
    }

    fun open(cls:Class<*>,isFinish : Boolean = false,key:String,value:Int){
        var tag = Target.Builder(cls)
            .isFinish(isFinish)
            .put(key,value)
            .builder();
        open(tag);
    }

    fun open(cls:Class<*>,isFinish : Boolean = false,key:String,value:String){
        var tag = Target.Builder(cls)
            .isFinish(isFinish)
            .put(key,value)
            .builder();
        open(tag);
    }

    fun open(cls:Class<*>,isFinish : Boolean = false,key:String,value:Serializable){
        var tag = Target.Builder(cls)
            .isFinish(isFinish)
            .put(key,value)
            .builder();
        open(tag);
    }

    fun open(cls:Class<*>,isFinish : Boolean = false,key:String,value:Int,strKey:String,strvalue:String){
        var tag = Target.Builder(cls)
            .isFinish(isFinish)
            .put(key,value)
            .put(strKey,strvalue)
            .builder();
        open(tag);
    }
}