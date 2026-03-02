
package com.ltcminer.smsgw

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            context.startForegroundService(Intent(context, WsForegroundService::class.java))
        }
    }
}
