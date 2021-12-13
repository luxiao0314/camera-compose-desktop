package com.aihuishou.creative.pcs.watcher.photocube.warehouse.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.ahs.camera.model.Store
import com.ahs.camera.ui.*
import com.ahs.camera.utils.closeRequest
import com.ahs.camera.utils.launchType
import com.ahs.camera.utils.padding
import com.ahs.camera.utils.windowWidth
import com.aihuishou.creative.pcs.watcher.photocube.warehouse.ui.com.ahs.camera.utils.CameraManager

fun main() = application {

    val visible = remember { Store.visible }

    Window(
        icon = painterResource("image/ic_icon.jpeg"),
        onCloseRequest = { closeRequest() },
        title = "魔方W(v1.0.0)",
        resizable = false,
        state = rememberWindowState(width = windowWidth, height = 850.dp)
    ) {
        MaterialTheme {
            Column(Modifier.background(MaterialTheme.colors.surface).padding(padding)) {
                if (visible.value) {
                    message()
                    Row {
                        if (launchType == 1) {
                            camera1Preview(CameraManager.mvCamrera)
                        } else {
                            camera2Preview(CameraManager.javacvCamera)
                            camera3Preview(CameraManager.javacvCamera2)
                        }
                    }
                }
                messageList(visible.value) { visible.value = !visible.value }
            }
        }
    }
}
