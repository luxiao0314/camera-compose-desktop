package com.ahs.camera.model

import java.util.concurrent.TimeUnit

object MessagesRepository {

    fun getMessages(): MutableList<Message> {
        val messageList = mutableListOf<Message>()
        for (i in 0..99999) {
            messageList.add(Message())
        }
        return messageList
    }

    fun getDeviceStatus(): DeviceStatus {
        val deviceStatus = DeviceStatus()
        deviceStatus.deviceStatus = "运行中"
        deviceStatus.cameraStatus = "在线"
        deviceStatus.netStatus = "在线"
        return deviceStatus
    }

}