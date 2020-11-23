package com.skfo763.qrcheckin.intro.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.skfo763.base.BaseViewModel
import com.skfo763.qrcheckin.intro.usecase.IntroActivityUseCase

class IntroViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
): BaseViewModel<IntroActivityUseCase>() {

}