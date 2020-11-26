package com.skfo763.qrcheckin.intro.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.youtube.player.YouTubePlayer
import com.skfo763.component.youtubeplayer.YouTubePlayerView
import com.gun0912.tedpermission.PermissionListener
import com.skfo763.base.BaseFragment
import com.skfo763.component.bixbysetting.NumberTextViewSetter
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.databinding.FragmentIntroOtherSettingBinding
import com.skfo763.qrcheckin.databinding.FragmentIntroPermissionBinding
import com.skfo763.qrcheckin.extension.isOverlayPermissionGranted
import com.skfo763.qrcheckin.extension.requestLocationPermissions
import com.skfo763.qrcheckin.extension.showOverlayPermissionDialog
import com.skfo763.qrcheckin.intro.usecase.IntroActivityUseCase
import com.skfo763.qrcheckin.intro.viewmodel.IntroViewModel
import com.skfo763.qrcheckin.intro.viewmodel.OtherSettingsViewModel
import com.skfo763.qrcheckin.lockscreen.usecase.LockScreenActivityUseCase.Companion.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE

sealed class IntroPagerFragment<T: ViewDataBinding> : BaseFragment<T, IntroViewModel, IntroActivityUseCase>()

class PermissionFragment: IntroPagerFragment<FragmentIntroPermissionBinding>() {

    override val layoutResId: Int = R.layout.fragment_intro_permission

    override val parentViewModel: IntroViewModel by activityViewModels()

    override val useCase: IntroActivityUseCase by lazy { parentViewModel.useCase }

    private val requestOverlayPermission = View.OnClickListener {
        checkOverlayOptions(true)
    }

    private val requestLocationPermission = View.OnClickListener {
        context?.requestLocationPermissions(locationPermissionListener)
    }

    override val bindingVariable: (FragmentIntroPermissionBinding) -> Unit = {
        it.parentViewModel = parentViewModel
        it.requestOverlayPermission = requestOverlayPermission
        it.requestLocationPermission = requestLocationPermission
    }

    private val locationPermissionListener = object: PermissionListener {
        override fun onPermissionGranted() {
            parentViewModel.setLocationPermissionState(true)
        }
        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            parentViewModel.setLocationPermissionState(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.intro_permission_toolbar_menu, menu)
    }

    override fun onStart() {
        super.onStart()
        checkOverlayOptions(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                useCase.goToPreviousInitFlow()
                true
            }
            R.id.toolbar_menu_skip_initialize -> {
                useCase.completePermissionFlow()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE -> {
                checkOverlayOptions(false)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    @SuppressLint("NewApi")
    fun checkOverlayOptions(showDialog: Boolean) {
        if (activity?.isOverlayPermissionGranted == false) {
            parentViewModel.setOverlayPermissionState(false)
            if(showDialog) activity?.showOverlayPermissionDialog()
        } else {
            parentViewModel.setOverlayPermissionState(true)
            context?.requestLocationPermissions(locationPermissionListener)
        }
    }
}

class OtherSettingsFragment : IntroPagerFragment<FragmentIntroOtherSettingBinding>(), YouTubePlayer.PlaybackEventListener  {

    companion object {
        private const val REQUEST_SETTINGS = 4874
    }

    override val layoutResId: Int = R.layout.fragment_intro_other_setting

    override val parentViewModel: IntroViewModel by activityViewModels()

    private val viewModel: OtherSettingsViewModel by viewModels()

    override val useCase: IntroActivityUseCase by lazy { parentViewModel.useCase }

    private val onFabClickListener = View.OnClickListener {
        startActivityForResult(Intent(Settings.ACTION_SETTINGS), REQUEST_SETTINGS)
    }

    private val numberTextViewSetter by lazy {
        NumberTextViewSetter(
            listOf(binding.introBixbyStep1, binding.introBixbyStep2, binding.introBixbyStep3, binding.introBixbyStep4),
            parentViewModel.videoInfo?.checkPointList ?: listOf()
        )
    }

    override val bindingVariable: (FragmentIntroOtherSettingBinding) -> Unit = {
        it.parentViewModel = parentViewModel
        it.introOtherFab.setOnClickListener(onFabClickListener)
        initPlayerView(it.introOtherYoutubePlayerView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.intro_other_toolbar_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                useCase.goToPreviousInitFlow()
                true
            }
            R.id.toolbar_menu_complete_initialize -> {
                parentViewModel.completeOtherSettingFlow()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initPlayerView(playerView: YouTubePlayerView) {
        playerView.onInitializedListener = viewModel
        playerView.playbackEventListener = this
        parentViewModel.videoInfo?.id?.let {
            playerView.play(it)
        }
    }

    private fun observeLiveData() {
        viewModel.videoTimeMillis.observe(viewLifecycleOwner, { videoTimeMillis ->
            numberTextViewSetter.onVideoTimeMillis(videoTimeMillis, viewModel.maxVideoTime)
        })
    }

    override fun onPlaying() {
        viewModel.startTracking()
    }

    override fun onPaused() {
        viewModel.stopTracking()
    }

    override fun onStopped() {
        viewModel.stopTracking()
    }

    override fun onBuffering(isBuffering: Boolean) {
        if(isBuffering) {
            viewModel.startTracking()
        } else {
            viewModel.stopTracking()
        }
    }

    override fun onSeekTo(newPositionMillis: Int) {
        viewModel.setVideoTimeMillis(newPositionMillis)
        viewModel.stopTracking()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_SETTINGS -> {

            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
