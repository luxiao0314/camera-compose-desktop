package com.ahs.camera.utils

import com.ahs.camera.model.Message
import com.ahs.camera.model.Store

inline fun pushLogToUI(noinline log: () -> Message) {
    val logItem = log.invoke()

    Store.logs.value += listOf(logItem)   //必须添加list
}

fun clearLog() {
    Store.logs.value = emptyList()
}