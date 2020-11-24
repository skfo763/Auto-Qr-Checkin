package com.skfo763.component.permissionstateview

import androidx.databinding.BindingAdapter

@BindingAdapter("permissionState")
fun PermissionStateView.setPermissionStateGranted(isGranted: Boolean) {
    this.setPermissionState(isGranted)
}