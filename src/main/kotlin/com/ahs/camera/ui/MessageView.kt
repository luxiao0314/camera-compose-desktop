package com.ahs.camera.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahs.camera.model.DeviceStatus
import com.ahs.camera.model.Message
import com.ahs.camera.utils.fontSize
import com.ahs.camera.utils.padding
import java.awt.image.BufferedImage

/**
 * @Description
 * @Author lux
 * @Date 2021/11/17 6:28 下午
 * @Version
 */
@Composable
fun message(deviceStatus: DeviceStatus) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Text("设备状态: ${deviceStatus.deviceStatus}", modifier = Modifier.padding(end = padding), fontSize = fontSize)

        Text("相机状态: ${deviceStatus.cameraStatus}", modifier = Modifier.padding(end = padding), fontSize = fontSize)

        Text("联网状态: ${deviceStatus.netStatus}", fontSize = fontSize)
    }
}

@Composable
fun cameraMessage(text: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text, fontSize = fontSize)
        TextButton(
            modifier = Modifier.align(Alignment.CenterVertically).size(50.dp, 30.dp),
            onClick = onClick,
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(vertical = 3.dp),
        ) {
            Text("拍照", fontSize = fontSize)
        }
    }
}

@Composable
fun messageList(more: Boolean, messages: List<Message>, onClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text("运行日志: ", modifier = Modifier.padding(top = 5.dp, bottom = 5.dp), fontSize = fontSize)
        TextButton(
            modifier = Modifier.size(50.dp, 30.dp),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(vertical = 3.dp),
            onClick = onClick,
        ) {
            val text = if (more) "放大" else "缩小"
            Text(text, fontSize = fontSize)
        }
    }
    LazyColumn(
        modifier = Modifier.border(1.dp, color = Color.Gray)
            .fillMaxSize()
            .padding(10.dp)
    ) {
        items(
            count = messages.size,
            key = { index -> messages[index] }
        ) { index ->
            Text(messages[index].message, fontSize = 12.sp)
        }
    }
}

@Composable
fun image() {
    Image(
        bitmap = BufferedImage(0, 0, 0).toComposeImageBitmap(),
        "",
        modifier = Modifier.requiredSize(50.dp).border(1.dp, color = Color.Gray)
    )
}