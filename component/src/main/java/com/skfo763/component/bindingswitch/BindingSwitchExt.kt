package com.skfo763.component.bindingswitch

import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("onCheckStateChanged")
inline fun SwitchCompat.setOnCheckStateChangedListener(crossinline onChanged: (CompoundButton?, Boolean) -> Unit) {
    setOnCheckedChangeListener { buttonView, isChecked ->
        onChanged.invoke(buttonView, isChecked)
    }
}