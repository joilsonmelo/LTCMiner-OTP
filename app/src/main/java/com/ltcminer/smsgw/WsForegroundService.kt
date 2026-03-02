
package com.ltcminer.smsgw

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class WsForegroundService : Service() {

    private val client = OkHttpClient.Builder()
        .pingInterval(25, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val wssUrl = "wss://ltcminer.com/sms-wss/"
    private val token = "CHANGE_ME_LONG_RANDOM_TOKEN"
    private val deviceId = "android-device"

    override fun onCreate() {
        super.onCreate()
        startForeground(1, buildNotification())
        connect()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(): Notification {
        val channelId = "sms_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(
                NotificationChannel(channelId, "SMS Gateway", NotificationManager.IMPORTANCE_LOW)
            )
        }
        return Notification.Builder(this, channelId)
            .setContentTitle("LTCMiner SMS Gateway")
            .setContentText("Running...")
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .build()
    }

    private fun connect() {
        val request = Request.Builder().url(wssUrl).build()
        client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                LogBus.add("Connected")
                webSocket.send(JSONObject()
                    .put("type", "auth")
                    .put("device_id", deviceId)
                    .put("token", token)
                    .toString())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                LogBus.add("RECV: $text")
            }
        })
    }
}
