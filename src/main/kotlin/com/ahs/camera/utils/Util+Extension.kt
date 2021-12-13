package com.ahs.camera.utils

import androidx.compose.ui.window.ApplicationScope
import com.ahs.camera.model.Device
import com.aihuishou.creative.pcs.watcher.photocube.warehouse.ui.com.ahs.camera.utils.CameraManager
import kotlinx.coroutines.Job
import java.awt.BorderLayout
import java.awt.Color
import java.io.File
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.border.CompoundBorder


fun Job.safeCancel() {
    if (isActive) {
        cancel()
    }
}

fun <T> tryCatchFor(tryCatchBlock: () -> T, errorBlock: ((e: Exception) -> T)): T {
    return try {
        tryCatchBlock()
    } catch (e: Exception) {
        println(e.message)
        errorBlock.invoke(e)
    }
}

fun <T> tryCatchFor(tryCatchBlock: () -> T?): T? {
    return try {
        tryCatchBlock()
    } catch (e: Throwable) {
        println(e.message)
        null
    }
}

fun <T> tryCatchOrDefault(def: T, tryCatchBlock: () -> T): T {
    return tryCatchFor(tryCatchBlock) { def }
}

fun compoundBorder(): CompoundBorder {
    val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
    val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
    return CompoundBorder(empty, dashed)
}

/**
 * MvCamreraPanel
 */
fun JLabel.addmvLine() {
    //边框
    add(JLabel("").apply {
        border = BorderFactory.createLineBorder(Color.BLACK)
        setBounds(0, 0, mvPreviewWidth, mvPreviewHeight)
    }, BorderLayout.CENTER)

    //横线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds(0, mvPreviewHeight / 3, mvPreviewWidth, 1)
    }, BorderLayout.CENTER)

    //横线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds(0, mvPreviewHeight / 2, mvPreviewWidth, 1)
    }, BorderLayout.CENTER)

    //横线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds(0, (mvPreviewHeight / 1.5).toInt(), mvPreviewWidth, 1)
    }, BorderLayout.CENTER)

    //竖线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds(mvPreviewWidth / 3, 0, 1, mvPreviewHeight)
    }, BorderLayout.CENTER)

    //竖线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds(mvPreviewWidth / 2, 0, 1, mvPreviewHeight)
    }, BorderLayout.CENTER)

    //竖线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds((mvPreviewWidth / 1.5).toInt(), 0, 1, mvPreviewHeight)
    }, BorderLayout.CENTER)
}

/**
 * JavacvCameraPanel
 */
fun JLabel.addcvLine() {
    //边框
    add(JLabel("").apply {
        border = BorderFactory.createLineBorder(Color.BLACK)
        setBounds(0, 0, previewWidth, previewHeight)
    }, BorderLayout.CENTER)

    //横线
    add(JLabel("").apply {
        border = compoundBorder()
        setBounds(0, previewHeight / 2, previewWidth, 1)
    }, BorderLayout.CENTER)

    //竖线
    add(JLabel("").apply {
        border = compoundBorder()
        setBounds(previewWidth / 2, 0, 1, previewHeight)
    }, BorderLayout.CENTER)
}

fun Device.netStatus(): String = if (netStatus.value == 1) "在线" else "离线"

fun Device.cameraStatus(): String {
    val camera1Status = if (camera1Status.value == 1) "在线(1)" else "离线(1)"
    val camera2Status = if (camera2Status.value == 1) "在线(2)" else "离线(2)"
    val camera3Status = if (camera3Status.value == 1) "在线(3)" else "离线(3)"
    if (launchType == 1) return camera1Status
    return "$camera2Status/$camera3Status"
}

fun Device.deviceStatus(): String = when (deviceStatus.value) {
    0 -> "初始化"
    1 -> "运行中"
    2 -> "已结束"
    else -> "未执行"
}

fun filepath() = "images/${System.currentTimeMillis()}.jpg"

fun String.mkdirs(): String {
    val file = File(toString())
    if (file.parentFile != null && !file.parentFile.exists()) {
        file.parentFile.mkdirs()
    }
    return file.path
}

fun ApplicationScope.closeRequest() {
    CameraManager.javacvCamera.stop()
    CameraManager.javacvCamera2.stop()
    CameraManager.mvCamrera.stop()
    exitApplication()
}