package com.east.evil.huxlyn.commons

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<D : ViewDataBinding,V : EastViewModel<*>> : Fragment() {
    lateinit var viewModel : V;
    lateinit var dataBinding : D;

    companion object{
        private const val TAG = "BaseFragment==>";
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater,getLayoutRes(),container,false);
        return dataBinding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d(TAG, "onActivityCreated");

        var vp = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        );
        viewModel = vp.get(getVMClass()!!);
        viewModel.setLifecycleOwner(this);
        lifecycle.addObserver(viewModel);
        dataBinding.lifecycleOwner = this;
        initView()
    }

    abstract fun getLayoutRes():Int;

    abstract fun getVMClass() : Class<V>;

    open fun initView(){

    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume();
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause();
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart();
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop();
    }
}