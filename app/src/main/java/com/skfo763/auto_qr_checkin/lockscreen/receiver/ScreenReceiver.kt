package com.skfo763.auto_qr_checkin.lockscreen.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.skfo763.auto_qr_checkin.lockscreen.activity.LockScreenActivity

class ScreenReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return
        if(intent.action.equals(Intent.ACTION_SCREEN_ON)) {
            val intent = Intent(context, LockScreenActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            try {
                pendingIntent.send()
            } catch (e: PendingIntent.CanceledException) {
                context.startActivity(intent)
            }
        }
    }

}