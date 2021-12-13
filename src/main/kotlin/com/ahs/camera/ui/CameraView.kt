package com.ahs.camera.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahs.camera.model.Store
import com.ahs.camera.utils.*
import javax.swing.JComponent


/**
 * @Description
 * @Author lux
 * @Date 2021/11/17 6:26 下午
 * @Version
 */
typealias PhotoBlock = (bytes: ByteArray) -> Unit

interface CamreraPanel {
    fun start()
    fun stop()
    fun isStart(): Boolean
    fun takePhoto(block: PhotoBlock? = null)
    fun getComponent(): JComponent?
}

@Composable
fun camera1Preview(camreraPanel: CamreraPanel) {
    if (!camreraPanel.isStart()) {
        camreraPanel.start()
    }
    val bufferedImage = remember { Store.camera1bufferedImage }

    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("俯视机位预览") { camreraPanel.takePhoto { saveImage(it) } }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 5.dp)
                .size(Dp(mvPreviewWidth.toFloat()), Dp(mvPreviewHeight.toFloat()))
                .border(1.dp, color = Color.Gray),
        ) {
            bufferedImage.value?.let {
                Image(
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    bitmap = it.toComposeImageBitmap(),
                    modifier = Modifier.fillMaxSize()
                )
            }
            drawline(true)
        }
    }
}

@Composable
fun camera2Preview(camreraPanel: CamreraPanel) {
    if (!camreraPanel.isStart()) {
        camreraPanel.start()
    }
    val bufferedImage = remember { Store.camera2bufferedImage }

    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("俯视机位预览") { camreraPanel.takePhoto { saveImage(it) } }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 5.dp, end = padding)
                .size(Dp(previewWidth.toFloat()), Dp(previewHeight.toFloat()))
                .border(1.dp, color = Color.Gray)
        ) {
            bufferedImage.value?.let {
                Image(
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    bitmap = it.toComposeImageBitmap(),
                    modifier = Modifier.fillMaxSize()
                )
            }
            drawline(true)
        }
    }
}

@Composable
fun camera3Preview(camreraPanel: CamreraPanel) {
    if (!camreraPanel.isStart()) {
        camreraPanel.start()
    }
    val bufferedImage = remember { Store.camera3bufferedImage }

    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("俯视机位预览") { camreraPanel.takePhoto { saveImage(it) } }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 5.dp)
                .size(Dp(previewWidth.toFloat()), Dp(previewHeight.toFloat()))
                .border(1.dp, color = Color.Gray)
        ) {
            bufferedImage.value?.let {
                Image(
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    bitmap = it.toComposeImageBitmap(),
                    modifier = Modifier.fillMaxSize()
                )
            }
            drawline(false)
        }
    }
}

@Composable
private fun drawline(show: Boolean) {
    //横线
    drawline(
        offsetStart = { Offset(x = 0f, y = it.height / 2) },
        offsetEnd = { Offset(x = it.width, y = it.height / 2) }
    )
    //竖线
    drawline(
        offsetStart = { Offset(x = it.width / 2, y = 0f) },
        offsetEnd = { Offset(x = it.width / 2, y = it.height) }
    )

    if (!show) return

    //横线
    drawline(
        offsetStart = { Offset(x = 0f, y = it.height / 3) },
        offsetEnd = { Offset(x = it.width, y = it.height / 3) }
    )

    drawline(
        offsetStart = { Offset(x = 0f, y = it.height * 2 / 3) },
        offsetEnd = { Offset(x = it.width, y = it.height * 2 / 3) }
    )

    //竖线
    drawline(
        offsetStart = { Offset(x = it.width / 3, y = 0f) },
        offsetEnd = { Offset(x = it.width / 3, y = it.height) }
    )

    drawline(
        offsetStart = { Offset(x = it.width * 2 / 3, y = 0f) },
        offsetEnd = { Offset(x = it.width * 2 / 3, y = it.height) }
    )
}

@Composable
fun drawline(offsetStart: (Size) -> Offset, offsetEnd: (Size) -> Offset) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        //绘制直线
        drawLine(
            start = offsetStart.invoke(size),
            end = offsetEnd.invoke(size),
            color = Color.Black,
            strokeWidth = 1F, //设置直线宽度
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)  //设置为虚线
        )
    }
}

@Composable
fun cameraPreview(camreraPanel: CamreraPanel, endPadding: Dp) {
    //防止反复打开相机
    if (!camreraPanel.isStart()) {
        camreraPanel.start()
    }
    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("俯视机位预览") { camreraPanel.takePhoto { saveImage(it) } }
        SwingPanel(
            modifier = Modifier.padding(top = 5.dp, end = endPadding)
                .size(Dp(previewWidth.toFloat()), Dp(previewHeight.toFloat())),
            factory = { camreraPanel.getComponent()!! })
    }
}