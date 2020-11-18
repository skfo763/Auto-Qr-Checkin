package com.skfo763.qrcheckin.lockscreen.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.admob.AdMobManager
import com.skfo763.qrcheckin.databinding.ActivityLockScreenBinding
import com.skfo763.qrcheckin.extension.*
import com.skfo763.qrcheckin.lockscreen.usecase.LockScreenActivityUseCase
import com.skfo763.qrcheckin.lockscreen.viewmodel.LockScreenViewModel
import com.skfo763.base.BaseActivity
import com.skfo763.component.tracker.FirebaseTracker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LockScreenActivity (
    override val layoutResId: Int = R.layout.activity_lock_screen,
    override val navHostResId: Int? = R.id.lock_screen_nav_host_fragment
): BaseActivity<ActivityLockScreenBinding, LockScreenViewModel, LockScreenActivityUseCase>() {

    companion object {
        const val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1000
    }

    override val viewModel: LockScreenViewModel by viewModels()
    override var useCase = LockScreenActivityUseCase(this)
    private lateinit var appBarConfiguration: AppBarConfiguration
    @Inject lateinit var adMobManager: AdMobManager
    @Inject lateinit var firebaseTracker: FirebaseTracker

    override val bindingVariable: (ActivityLockScreenBinding) -> Unit = {
        it.viewModel = viewModel
    }

    override fun connectNavHostToController(host: NavHostFragment) {
        appBarConfiguration = AppBarConfiguration(host.navController.graph, binding.lockScreenDrawerlayout)
        binding.toolbar.setupWithNavController(host.navController, appBarConfiguration)
        binding.lockScreenNavigation.lockScreenNavView.setupWithNavController(host.navController)
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
        checkOverlayOption()
        adMobManager.putAddToCustomContainer(binding.lockScreenAdViewContainer)
        viewModel.inAppReview()
    }

    override fun onDestroy() {
        super.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        clearDismissKeyGuard()
        clearScreenOnLocked()
    }

    private fun showPermissionDialog(doOnPositiveClicked: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(R.string.permission_title)
            .setMessage(R.string.permission_message)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                doOnPositiveClicked.invoke()
            }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                finish()
            }.show()
    }

    private fun checkOverlayOption() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            showPermissionDialog {
                requestOverlayOptions()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE -> {
                checkOverlayOption()
            }
        }
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

    override fun onBackPressed() {
        if(binding.lockScreenDrawerlayout.isDrawerOpen(GravityCompat.START)) {
            binding.lockScreenDrawerlayout.closeDrawer(GravityCompat.START)
        } else {
            adMobManager.showInterstitialAd {
                super.onBackPressed()
            }
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

}