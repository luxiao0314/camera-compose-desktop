package com.aihuishou.creative.pcs.watcher.photocube.warehouse.ui.com.ahs.camera

import com.ahs.camera.ui.PhotoBlock

/**
 *
 * @FileName:
 *          com.aihuishou.creative.pcs.watcher.photocube.warehouse.ui.camera.ICamera
 * @author: Tony Shen
 * @date: 2021/12/6 2:32 PM
 * @version: V1.0 <描述当前版本功能>
 */
interface ICamera {

    fun takePhoto(cameraId: Int, block: PhotoBlock? = null)

    fun exposure(cameraId: Int, value: Double)

    fun focus(cameraId: Int, value: Double)

    fun close(cameraId: Int)
}