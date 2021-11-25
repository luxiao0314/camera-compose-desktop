package com.ahs.camera.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import com.ahs.camera.utils.*
import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamPanel
import com.github.sarxos.webcam.WebcamResolution
import com.github.sarxos.webcam.WebcamUtils
import com.github.sarxos.webcam.util.ImageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.bytedeco.javacv.OpenCVFrameGrabber
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import javax.swing.JComponent
import javax.swing.JOptionPane


/**
 * @Description
 * @Author lux
 * @Date 2021/11/17 6:26 下午
 * @Version
 */

interface CamreraPanel {
    fun start()
    fun stop()
    fun saveImage(path: String = filepath(), block: (() -> Unit?)? = null)
    fun getPanel(): JComponent
}

@Composable
fun cameraPreview(frameWindowScope: FrameWindowScope, camreraPanel: CamreraPanel, endPadding: Dp) {
    frameWindowScope.window.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            camreraPanel.stop()
        }
    })

    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("俯视机位预览") { camreraPanel.saveImage() }
        SwingPanel(
            modifier = Modifier.padding(top = 5.dp, end = endPadding)
                .size(Dp(previewWidth.toFloat()), Dp(previewHeight.toFloat())),
            factory = { camreraPanel.getPanel() })
    }
}

@Composable
fun cameraPreview(frameWindowScope: FrameWindowScope, camreraPanel: CamreraPanel) {
    frameWindowScope.window.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            camreraPanel.stop()
        }
    })

    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("俯视机位预览") { camreraPanel.saveImage() }
        SwingPanel(
            modifier = Modifier.padding(top = 5.dp).size(Dp(mvPreviewWidth.toFloat()), Dp(mvPreviewHeight.toFloat())),
            factory = {
                camreraPanel.getPanel()
            })
    }
}

@Composable
fun mvCameraPreview(frameWindowScope: FrameWindowScope) {
    val mvCamreraPanel = MvCamreraPanel()
    frameWindowScope.window.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            mvCamreraPanel.stop()
        }
    })

    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("俯视机位预览") { mvCamreraPanel.saveImage() }
        SwingPanel(
            modifier = Modifier.padding(top = 5.dp, end = padding)
                .size(Dp(previewWidth.toFloat()), Dp(previewHeight.toFloat())),
            factory = {
                mvCamreraPanel
            })
    }
}

@Composable
fun cvCameraPreview(frameWindowScope: FrameWindowScope) {
    val javacvPanel = JavacvCameraPanel()
    frameWindowScope.window.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            javacvPanel.stop()
        }
    })

    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("侧视机位预览") { javacvPanel.saveImage() }
        SwingPanel(
            modifier = Modifier.padding(top = 5.dp)
                .size(Dp(previewWidth.toFloat()), Dp(previewHeight.toFloat())),
            factory = {
                javacvPanel
            })
    }
}


@Composable
fun camera3Preview(frameWindowScope: FrameWindowScope) {
    val bufferedImage: BufferedImage? = null
    val image = remember { mutableStateOf(bufferedImage) }

    image.value?.let {
        Image(
            bitmap = it.toComposeImageBitmap(),
            "",
            modifier = Modifier.requiredSize(Dp(previewWidth.toFloat())).border(1.dp, color = Color.Gray)
        )
    }

    val grabber = OpenCVFrameGrabber.createDefault(0).apply {
        imageWidth = pictureWidth
        imageHeight = pictureHeight
    }

    frameWindowScope.window.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            grabber.stop()
        }
    })

    CoroutineScope(Dispatchers.IO).launch {
        tryCatchFor {
            grabber?.start()

            // 操作状态
            while (isActive) {
                //居中裁剪
                image.value = createThumbnail(getBufferedImage(grabber?.grab()), previewWidth, previewHeight)
                // 每40毫秒刷新视频,一秒25帧
                Thread.sleep(40)
            }
        }
    }
}

@Composable
fun camera2Preview(frameWindowScope: FrameWindowScope) {
    val webcam = Webcam.getDefault()?.apply {
        setCustomViewSizes(Dimension(4000, 3000), Dimension(1920, 1080), Dimension(1280, 720))
        viewSize = WebcamResolution.FHD.size
    }

    frameWindowScope.window.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            webcam?.close()
        }
    })

    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("侧视机位预览") {
            CoroutineScope(Dispatchers.IO).launch {
                WebcamUtils.capture(webcam, System.currentTimeMillis().toString(), ImageUtils.FORMAT_PNG)
                JOptionPane.showMessageDialog(null, "拍照成功")
            }
        }
        SwingPanel(
            modifier = Modifier.padding(top = 5.dp, end = 15.dp).border(1.dp, color = Color.Gray)
                .size(Dp(previewWidth.toFloat()), Dp(previewHeight.toFloat())),
            factory = {
                WebcamPanel(webcam, true).apply {
                    isFPSDisplayed = true
                    isImageSizeDisplayed = true
                    isFPSDisplayed = true
                    drawMode = WebcamPanel.DrawMode.FILL
                }
            })
    }
}