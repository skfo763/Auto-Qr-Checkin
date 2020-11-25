package com.skfo763.qrcheckin.intro.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.viewpager2.widget.ViewPager2
import com.skfo763.base.BaseActivity
import com.skfo763.base.extension.logMessage
import com.skfo763.component.bixbysetting.BixbyLandingManager
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.admob.AdMobManager
import com.skfo763.qrcheckin.databinding.ActivityIntroBinding
import com.skfo763.qrcheckin.extension.isOverlayPermissionGranted
import com.skfo763.qrcheckin.intro.adapter.IntroPagerAdapter
import com.skfo763.qrcheckin.intro.fragment.IntroPagerFragment
import com.skfo763.qrcheckin.intro.fragment.OtherSettingsFragment
import com.skfo763.qrcheckin.intro.fragment.PermissionFragment
import com.skfo763.qrcheckin.intro.usecase.IntroActivityUseCase
import com.skfo763.qrcheckin.intro.viewmodel.IntroViewModel
import com.skfo763.storage.gps.isLocationPermissionGranted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity(
    override val layoutResId: Int = R.layout.activity_intro,
    override val navHostResId: Int? = null
) : BaseActivity<ActivityIntroBinding, IntroViewModel, IntroActivityUseCase>() {

    companion object {
        @JvmStatic
        fun getIntent(context: Context): Intent {
            return Intent(context, IntroActivity::class.java)
        }
    }

    override val viewModel: IntroViewModel by viewModels()
    override var useCase: IntroActivityUseCase = IntroActivityUseCase(this)
    @Inject lateinit var adMobManager: AdMobManager
    @Inject lateinit var bixbyManager: BixbyLandingManager
    @Inject lateinit var introPagerAdapter: IntroPagerAdapter

    override val bindingVariable: (ActivityIntroBinding) -> Unit = {
        it.viewModel = viewModel
        it.introViewPager.adapter = introPagerAdapter
        it.introViewPager.isUserInputEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar()
        adMobManager.putAddToCustomContainer(binding.introAdViewContainer)
        viewModel.setOverlayPermissionState(isOverlayPermissionGranted)
        viewModel.setLocationPermissionState(isLocationPermissionGranted)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.intro_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.toolbar_menu_skip_initialize -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.skip_initialize_title)
                    .setMessage(R.string.skip_initialize_message)
                    .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                        viewModel.completeInitializeFlow()
                    }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.introToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun goToNextFlow() {
        if(binding.introViewPager.isLastItem) {
            viewModel.completeInitializeFlow()
        } else {
            binding.introViewPager.currentItem = binding.introViewPager.currentItem + 1
        }
    }

    fun goToPrevFlow() {
        binding.introViewPager.adapter ?: return
        if(binding.introViewPager.isFirstItem) {
            this.onBackPressed()
        } else {
            binding.introViewPager.currentItem = binding.introViewPager.currentItem - 1
        }
    }

    private val ViewPager2.isLastItem: Boolean get() {
        return currentItem >= adapter?.itemCount?.minus(1) ?: 0
    }

    private val ViewPager2.isFirstItem: Boolean get() = currentItem <= 0

    private val ViewPager2.currentFragment: IntroPagerFragment<out ViewDataBinding>?
        get() = (adapter as? IntroPagerAdapter)?.getItem(currentItem)
}