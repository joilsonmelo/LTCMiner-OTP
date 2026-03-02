
package com.ltcminer.smsgw

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scroll = ScrollView(this)
        val tv = TextView(this)
        tv.setPadding(24, 24, 24, 24)
        tv.textSize = 14f
        scroll.addView(tv)
        setContentView(scroll)

        requestPermission()

        startForegroundService(Intent(this, WsForegroundService::class.java))

        lifecycleScope.launch {
            LogBus.text.collect {
                tv.text = it
                scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
            }
        }

        LogBus.add("APK iniciado")
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.SEND_SMS), 1001)
        }
    }
}
