package com.skfo763.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<B: ViewDataBinding, VP: BaseViewModel<U>, U: IBaseActivityUseCase> : Fragment() {

    abstract val layoutResId: Int

    abstract val parentViewModel: VP

    lateinit var binding: B

    abstract val bindingVariable: (B) -> Unit

    abstract val useCase: U

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = this
        bindingVariable(binding)
        setHasOptionsMenu(true)
        return binding.root
    }
}