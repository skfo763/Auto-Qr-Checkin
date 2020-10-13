package com.skfo763.component.bindingswitch

import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("onCheckStateChanged")
fun SwitchCompat.setOnCheckStateChangedListener(onChanged: (CompoundButton?, Boolean) -> Unit) {
    setOnCheckedChangeListener { buttonView, isChecked ->
        onChanged.invoke(buttonView, isChecked)
    }
}