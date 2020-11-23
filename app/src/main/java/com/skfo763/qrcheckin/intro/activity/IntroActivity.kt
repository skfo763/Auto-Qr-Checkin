package com.skfo763.qrcheckin.intro.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.skfo763.base.BaseActivity
import com.skfo763.component.bixbysetting.BixbyLandingManager
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.admob.AdMobManager
import com.skfo763.qrcheckin.databinding.ActivityIntroBinding
import com.skfo763.qrcheckin.intro.usecase.IntroActivityUseCase
import com.skfo763.qrcheckin.intro.viewmodel.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity(
    override val layoutResId: Int = R.layout.activity_intro,
    override val navHostResId: Int? = null
) : BaseActivity<ActivityIntroBinding, IntroViewModel, IntroActivityUseCase>() {

    override val viewModel: IntroViewModel by viewModels()
    override var useCase: IntroActivityUseCase = IntroActivityUseCase(this)
    @Inject lateinit var adMobManager: AdMobManager
    @Inject lateinit var bixbyManager: BixbyLandingManager


    override val bindingVariable: (ActivityIntroBinding) -> Unit = {
        it.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adMobManager.putAddToCustomContainer(binding.introAdViewContainer)
    }

}