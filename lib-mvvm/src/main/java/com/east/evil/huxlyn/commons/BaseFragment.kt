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


/**
 * 需要考虑改造
 *
 * 可根据 https://blog.csdn.net/qq_30993595/article/details/80736814 进行修改
 */

abstract class BaseFragment<D : ViewDataBinding,V : EastViewModel<*>> : Fragment() {
    lateinit var viewModel : V;
    lateinit var dataBinding : D;


    /**
     * 是否执行了lazyLoad方法
     */
    private var isLoaded = false;
    /**
     * 是否创建了View
     */
    private var isCreateView = false;

    /**
     * 当从另一个activity回到fragment所在的activity
     * 当fragment回调onResume方法的时候，可以通过这个变量判断fragment是否可见，来决定是否要刷新数据
     */
    private var isViewVisible = false;


    companion object{
        private const val TAG = "BaseFragment==>";
    }

    /**
     * /*
     * 此方法在viewpager嵌套fragment时会回调
     * 查看FragmentPagerAdapter源码中instantiateItem和setPrimaryItem会调用此方法
     * 在所有生命周期方法前调用
     * 这个基类适用于在viewpager嵌套少量的fragment页面
     * 该方法是第一个回调，可以将数据放在这里处理（viewpager默认会预加载一个页面）
     * 只在fragment可见时加载数据，加快响应速度
     * */
     * create by Administrator at 2021/4/29 11:28
     * @author Administrator
     * @param
     * @return
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d(TAG,"setUserVisibleHint==>")
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(dataBinding == null || dataBinding.root == null){
            dataBinding = DataBindingUtil.inflate(inflater,getLayoutRes(),container,false);
            dataBinding.lifecycleOwner = this;
        }
        onVisible()
        return dataBinding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated");
        initView();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"onViewCreated==>");

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate");
    }

    protected fun onVisible() {
        isViewVisible = true;

        if(isLoaded){
            refreshLoad();
        }
        if (!isLoaded && isCreateView && getUserVisibleHint()) {
            isLoaded = true;
            lazyLoad();
        }
    }

    protected open fun onInvisible() {
        isViewVisible = false
    }


    open fun initView(){
        Log.d(TAG,"initView==>");
    }

    abstract fun getLayoutRes():Int;

    abstract fun getVMClass() : Class<V>;

    /**
     * fragment第一次可见的时候回调此方法
     */
    open protected fun lazyLoad(){
        Log.d(TAG,"lazyLoad==>")
    }

    /**
     * 在Fragment第一次可见加载以后，每次Fragment滑动可见的时候会回调这个方法，
     * 子类可以重写这个方法做数据刷新操作
     * create by Administrator at 2021/4/29 11:29
     * @author Administrator
     * @param
     * @return
     */
    open protected fun refreshLoad(){
        Log.d(TAG,"refreshLoad==>")
    }

    open fun addObserve(){
        viewModel.vmData.observe(this, Observer {
            onVmdataChanged(it);
        })

        viewModel.loading.observe(this, Observer {
            Log.d(TAG,"LOADING==>${it.loadingFlag}")
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
        Log.d(TAG,"dismissLoading==>")
        when(loading.type){
            Loading.LoadingType.TYPE_DIALOG->{
                Log.d(TAG,"dismissLoading==>TYPE_DIALOG")
                val ac = activity;
                ac?.let {
                    Log.d(TAG,"dismissLoading==>make baseactivity dismiss loading");
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
