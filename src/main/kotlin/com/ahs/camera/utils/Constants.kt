package com.ahs.camera.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @Description
 * @Author lux
 * @Date 2021/11/19 11:07 上午
 * @Version
 */

val padding = 15.dp
val fontSize = 13.sp
const val launchType = 2 //1:屏幕检测拍照;2:魔方拍照

const val pictureWidth = 3840
const val pictureHeight = 2880
const val previewWidth = (pictureHeight / 5)
const val previewHeight = (pictureHeight / 5)

const val mvPreviewWidth = previewWidth * 2
const val mvPreviewHeight = previewWidth

val windowWidth = if (launchType == 1) {
    Dp(mvPreviewWidth.toFloat()) + padding * 3
} else {
    Dp(previewWidth * 2.toFloat()) + padding * 4
}
