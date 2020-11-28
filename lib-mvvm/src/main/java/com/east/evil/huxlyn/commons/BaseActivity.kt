package com.east.evil.huxlyn.commons

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.east.evil.huxlyn.entity.Loading
import com.east.evil.huxlyn.entity.Target
import com.east.evil.huxlyn.entity.VMData

abstract class BaseActivity<D : ViewDataBinding, V : EastViewModel<*>> :
    AppCompatActivity() {

    lateinit var dataBinding: D;
    lateinit var viewModel : V;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes())
        var vp = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        );
        viewModel = vp.get(getVMClass()!!);
        viewModel?.setLifecycleOwner(this);
        lifecycle.addObserver(viewModel!!);
        dataBinding?.lifecycleOwner = this;
        viewModel?.initModel();
        initView();
    }

    abstract fun getLayoutRes() : Int;

    abstract fun getVMClass() : Class<V>;

    open fun initView(){
        addObserve();
    }

    open fun addObserve(){
        viewModel.vmData.observe(this, Observer {
            onVmdataChanged(it);
        })

        viewModel.loading.observe(this, Observer {
            if(it.loadingFlag){
                showLoading(it);
            }else{
                dismissLoading(it);
            }
        })

        viewModel.target.observe(this, Observer {
            openTarget(it);
        })
    }

    open fun openTarget(target:Target){
        EastRouter.with(this)
            .target(target.cls!!)
            .isFinish(target.isFinish)
            .bundle(target.bundle)
            .start()
    }

    private fun onVmdataChanged(data:VMData){
        when(data.code){
            VMData.Code.CODE_DEFAULT->vmdataDefault(data);
            VMData.Code.CODE_SHOW_MSG->showVmDataToast(data);
            VMData.Code.CODE_SUCCESS->onVmdataSuccess(data);
            VMData.Code.CODE_ERROR->onVmdataError(data);
        }
    }

    open fun vmdataDefault(data:VMData){

    }

    open fun showVmDataToast(data: VMData){

    }

    open fun onVmdataSuccess(data: VMData){

    }

    open fun onVmdataError(data : VMData){

    }

    open fun showLoading(loading: Loading){

    }

    open fun dismissLoading(loading: Loading){

    }

}