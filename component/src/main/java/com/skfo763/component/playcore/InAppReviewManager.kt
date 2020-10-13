package com.skfo763.component.playcore

import android.app.Activity
import android.content.Context
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.skfo763.component.BuildConfig
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import java.lang.IllegalStateException
import javax.inject.Inject

@ActivityScoped
class InAppReviewManager @Inject constructor(private val activity: Activity) {

    private val reviewManager =
        if(BuildConfig.DEBUG) FakeReviewManager(activity)
        else ReviewManagerFactory.create(activity)

    fun launchReviewFlow(onRequestComplete: (Boolean) -> Unit, onRequestFailed: (Exception) -> Unit) {
        requestReviewFlow({
            launchReviewFlow(it, onRequestComplete, onRequestFailed)
        }) {
            onRequestFailed(it)
        }
    }

    private fun requestReviewFlow(onRequestComplete: (ReviewInfo?) -> Unit, onRequestFailed: (Exception) -> Unit) {
        reviewManager.requestReviewFlow().addOnCompleteListener {
            if(it.isSuccessful) {
                onRequestComplete(it.result)
            } else {
                onRequestComplete(null)
            }
        }.addOnFailureListener {
            onRequestFailed(it)
        }
    }

    private fun launchReviewFlow(
        reviewInfo: ReviewInfo?,
        onRequestComplete: (Boolean) -> Unit,
        onRequestFailed: (Exception) -> Unit
    ) {
        reviewInfo?.let {
            reviewManager.launchReviewFlow(activity, it).addOnCompleteListener { task ->
                onRequestComplete(task.isSuccessful)
            }.addOnFailureListener { e ->
                onRequestFailed(e)
            }
        } ?: run {
            onRequestFailed(NullPointerException())
        }
    }
}