package com.skfo763.qrcheckin.lockscreen.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.skfo763.base.BaseActivity
import com.skfo763.component.bixbysetting.BixbyLandingManager
import com.skfo763.component.tracker.FirebaseTracker
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.admob.AdMobManager
import com.skfo763.qrcheckin.databinding.ActivityLockScreenBinding
import com.skfo763.qrcheckin.extension.*
import com.skfo763.qrcheckin.lockscreen.usecase.LockScreenActivityUseCase
import com.skfo763.qrcheckin.lockscreen.viewmodel.LockScreenViewModel
import com.skfo763.storage.gps.isLocationPermissionGranted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LockScreenActivity (
    override val layoutResId: Int = R.layout.activity_lock_screen,
    override val navHostResId: Int? = R.id.lock_screen_nav_host_fragment
): BaseActivity<ActivityLockScreenBinding, LockScreenViewModel, LockScreenActivityUseCase>() {

    override val viewModel: LockScreenViewModel by viewModels()
    override var useCase = LockScreenActivityUseCase(this)
    private lateinit var appBarConfiguration: AppBarConfiguration
    @Inject lateinit var adMobManager: AdMobManager
    @Inject lateinit var firebaseTracker: FirebaseTracker
    @Inject lateinit var bixbyManager: BixbyLandingManager

    override val bindingVariable: (ActivityLockScreenBinding) -> Unit = {
        it.viewModel = viewModel
        useCase.snackBarWindow = binding.lockScreenNavHostFragment
        viewModel.setSwitchToSavedState()
        viewModel.navigationViewModel.useCase = useCase
    }

    override fun connectNavHostToController(host: NavHostFragment) {
        appBarConfiguration = AppBarConfiguration(host.navController.graph, binding.lockScreenDrawerlayout)
        binding.toolbar.setupWithNavController(host.navController, appBarConfiguration)
        binding.lockScreenNavigation.lockScreenNavView.setupWithNavController(host.navController)
        binding.lockScreenBottomNavigation.setupWithNavController(host.navController)
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(adMobManager)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestDismissKeyGuard()
        registerScreenOnLocked()
        setToolbar()
        requestLocationPermissions(viewModel.startTrackingLocationListener)
        adMobManager.putAddToCustomContainer(binding.lockScreenAdViewContainer)
    }

    override fun onDestroy() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        clearDismissKeyGuard()
        clearScreenOnLocked()
        if(this.isLocationPermissionGranted) {
            viewModel.stopTrackingLocation()
        }
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                val navController = navHostResId?.let { findNavController(this, it) } ?: return super.onSupportNavigateUp()
                return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.deleteFloatingButton()
        viewModel.sendCheckStateProperty()
    }

    override fun onStop() {
        super.onStop()
        viewModel.createFloatingButton()
        viewModel.sendCheckStateProperty()
    }

    override fun onBackPressed() {
        when {
            binding.lockScreenDrawerlayout.isDrawerOpen(GravityCompat.START) -> {
                binding.lockScreenDrawerlayout.closeDrawer(GravityCompat.START)
            }
            adMobManager.shouldShowInterstitialAd -> {
                adMobManager.showInterstitialAd()
            }
            viewModel.shouldReviewApp -> {
                viewModel.startReviewFlow { super.onBackPressed() }
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

}