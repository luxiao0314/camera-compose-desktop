package com.ahs.camera.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap

/**
 * @Description
 * @Author lux
 * @Date 2021/11/18 10:54 上午
 * @Version
 */
class Message(val message: String = System.currentTimeMillis().toString())

@Stable
class SearchState(image: ImageBitmap) {
    var query by mutableStateOf(image)
}