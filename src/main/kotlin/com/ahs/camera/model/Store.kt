package com.ahs.camera.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.awt.image.BufferedImage

object Store {
    var logs = mutableStateOf(emptyList<Message>())    //必须采用emptyList.否则数据可能不更新
    val visible = mutableStateOf(true)
    val device = Device()

    private val bufferedImage: BufferedImage? = null
    val camera1bufferedImage = mutableStateOf(bufferedImage)
    val camera2bufferedImage = mutableStateOf(bufferedImage)
    val camera3bufferedImage = mutableStateOf(bufferedImage)
}

data class Device(
    val deviceStatus: MutableState<Int> = mutableStateOf(-1), //设备状态
    val camera1Status: MutableState<Int> = mutableStateOf(-1),//相机状态
    val camera2Status: MutableState<Int> = mutableStateOf(-1),//相机状态
    val camera3Status: MutableState<Int> = mutableStateOf(-1), //相机状态
    val netStatus: MutableState<Int> = mutableStateOf(-1),//联网状态, 0:离线;1:在线
)