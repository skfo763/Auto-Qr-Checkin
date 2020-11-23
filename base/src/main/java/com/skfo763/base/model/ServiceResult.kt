package com.skfo763.base.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceResult <T: Parcelable> (
    val code: Int,
    val message: String,
    val data: T? = null
): Parcelable