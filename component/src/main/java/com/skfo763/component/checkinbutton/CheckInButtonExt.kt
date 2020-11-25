package com.skfo763.component.checkinbutton

import android.view.View
import androidx.databinding.BindingAdapter
import com.jakewharton.rxbinding4.view.clicks
import com.skfo763.base.extension.logException
import java.util.concurrent.TimeUnit

@BindingAdapter("buttonVisibility")
fun CheckInButton.setButtonVisibility(isVisible: Boolean?) {
    isVisible?.let {
        buttonVisibility = it
    }
}

@BindingAdapter("onButtonClicked")
inline fun CheckInButton.setButtonClickListener(crossinline onButtonClicked: (View) -> Unit) {
    compositeDisposable.add(binding.checkInButtonFab.clicks().throttleFirst(500L, TimeUnit.MILLISECONDS).subscribe({
        onButtonClicked.invoke(this)
    }) {
        logException(Exception(it))
    })
}