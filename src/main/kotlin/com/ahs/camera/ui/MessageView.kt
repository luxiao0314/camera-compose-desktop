package com.ahs.camera.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
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
import com.ahs.camera.model.Store
import com.ahs.camera.utils.*
import java.awt.image.BufferedImage

/**
 * @Description
 * @Author lux
 * @Date 2021/11/17 6:28 下午
 * @Version
 */
@Composable
fun message() {

    val deviceStatus = remember { Store.device }

    Row(verticalAlignment = Alignment.CenterVertically) {

        Text("设备状态: ${deviceStatus.deviceStatus()}", modifier = Modifier.padding(end = padding), fontSize = fontSize)

        Text("相机状态: ${deviceStatus.cameraStatus()}", modifier = Modifier.padding(end = padding), fontSize = fontSize)

        Text("联网状态: ${deviceStatus.netStatus()}", fontSize = fontSize)
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
fun messageList(more: Boolean, onClick: () -> Unit) {

    val messages = remember { Store.logs }

    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text("运行日志: ", modifier = Modifier.padding(top = 5.dp, bottom = 5.dp), fontSize = fontSize)
        Row {
            TextButton(
                modifier = Modifier.size(50.dp, 30.dp),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(vertical = 3.dp),
                onClick = onClick,
            ) {
                val text = if (more) "放大" else "缩小"
                Text(text, fontSize = fontSize)
            }
            TextButton(
                modifier = Modifier.size(50.dp, 30.dp),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(vertical = 3.dp),
                onClick = { clearLog() },
            ) {
                Text("清空", fontSize = fontSize)
            }
        }
    }

    val state = rememberLazyListState()

    Box(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.border(1.dp, color = Color.Gray).fillMaxSize().padding(10.dp), state) {
            items(messages.value.size) { x ->
                Text(messages.value[x].message, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = state)
        )
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun alertDialog() {

    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        androidx.compose.material.AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "这是对话框标题")
            },
            text = {
                Text(
                    "这是一段描述对话框提示内容的文本， " +
                            "这个文本有点长，还有点俏皮！"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("取消")
                }
            }
        )
    }
}