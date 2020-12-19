package com.skfo763.component.bottomsheetdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skfo763.component.R
import com.skfo763.component.databinding.DialogAutoCheckinDescBinding

class AutoCheckInDescDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAutoCheckinDescBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_auto_checkin_desc, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext()).load(R.raw.auto_checkin_desc_video).into(binding.autoCheckInGifView)
    }
}