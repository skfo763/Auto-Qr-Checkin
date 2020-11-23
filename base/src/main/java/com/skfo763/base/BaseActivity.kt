package com.skfo763.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.NavHostFragment

abstract class BaseActivity<B : ViewDataBinding, V: BaseViewModel<U>, U: IBaseActivityUseCase> : AppCompatActivity() {

    abstract val viewModel: V

    lateinit var binding: B

    private var navHostFragment: NavHostFragment? = null

    abstract val layoutResId: Int

    abstract val navHostResId: Int?

    abstract var useCase: U

    open fun doAfterBindViewModel() {
        // override if need additional injection
    }

    open fun connectNavHostToController(host: NavHostFragment) {
        // override if activity supports navigation component
    }

    abstract val bindingVariable: (B) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
        viewModel.useCase = useCase
        lifecycle.addObserver(useCase)
        binding.lifecycleOwner = this
        bindingVariable.invoke(binding)
        setNavHost()
        doAfterBindViewModel()
    }

    private fun setNavHost() {
        navHostResId ?: return
        (supportFragmentManager.findFragmentById(navHostResId!!) as? NavHostFragment)?.let {
            navHostFragment = it
            connectNavHostToController(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(!useCase.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}