package com.skfo763.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<U: IBaseActivityUseCase>: ViewModel() {

    lateinit var useCase: U

}