package com.east.evil.huxlyn.commons

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.east.evil.huxlyn.entity.Loading
import com.east.evil.huxlyn.entity.Target
import com.east.evil.huxlyn.entity.VMData
import com.east.evil.huxlyn.widget.LoadingDialog

abstract class BaseFragment<D : ViewDataBinding,V : EastViewModel<*>> : Fragment() {
    lateinit var viewModel : V;
    lateinit var dataBinding : D;

    private var loading : LoadingDialog? = null;

    companion object{
        private const val TAG = "BaseFragment==>";
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater,getLayoutRes(),container,false);
        dataBinding.lifecycleOwner = this;
        return dataBinding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated");
        initView();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate");

        var vp = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        );
        viewModel = vp.get(getVMClass()!!);
        viewModel.setLifecycleOwner(this);
        lifecycle.addObserver(viewModel);
        addObserve();
        viewModel.initModel();
    }

    open fun initView(){

    }

    abstract fun getLayoutRes():Int;

    abstract fun getVMClass() : Class<V>;

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

    open fun openTarget(target: Target){
        EastRouter.with(requireContext())
            .target(target.cls!!)
            .isFinish(target.isFinish)
            .bundle(target.bundle)
            .start()
    }

    private fun onVmdataChanged(data: VMData){
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
        when(loading.type){
            Loading.LoadingType.TYPE_DIALOG->{
                val ac = activity;
                ac?.let {
                    (it as BaseActivity<*, *>).showLoading(loading);
                }
            }
            Loading.LoadingType.TYPE_TOAST->{
                loadingToast(loading)
            }
            Loading.LoadingType.TYPE_VIEW->{
                loadingView(loading);
            }
        }
    }

    open fun dismissLoading(loading: Loading){
        when(loading.type){
            Loading.LoadingType.TYPE_DIALOG->{
                val ac = activity;
                ac?.let {
                    (it as BaseActivity<*, *>).dismissLoading(loading);
                }
            }
            Loading.LoadingType.TYPE_TOAST->{
                disLoadingToast(loading);
            }
            Loading.LoadingType.TYPE_VIEW->{
                disLadingView(loading)
            }
        }
    }

    /**
     * 使用View方式进行Loading加载
     * create by hux at 2020-11-28 19:30
     * @author hux
     * @param loading
     *      Loading
     * @return
     *      void
     */
    open fun loadingView(loading: Loading){

    }

    /**
     * 加载完毕，并切加载的方式为View，进行加载完毕后的操作
     * create by hux at 2020-11-28 19:31
     * @author hux
     * @param loading
     *      Loading
     * @return
     *      void
     */
    open fun disLadingView(loading : Loading){

    }

    /**
     * 使用Toast方式进行加载
     * create by hux at 2020-11-28 19:33
     * @author hux
     * @param loading
     *      Loading
     * @return
     *      void
     */
    open fun loadingToast(loading: Loading){

    }

    /**
     * 使用Toast进行加载完毕，进行加载完毕后的操作
     * create by hux at 2020-11-28 19:34
     * @author hux
     * @param loading
     *      Loading
     * @return
     *      void
     */
    open fun disLoadingToast(loading: Loading){

    }

}
