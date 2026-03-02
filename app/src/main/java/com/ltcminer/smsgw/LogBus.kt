
package com.ltcminer.smsgw

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LogBus {
    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    fun add(line: String) {
        val ts = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
        _text.value = (_text.value + "\n[$ts] " + line).takeLast(30000)
    }
}
