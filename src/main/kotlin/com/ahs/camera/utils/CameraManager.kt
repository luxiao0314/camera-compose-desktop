package com.ahs.camera.utils

import com.ahs.camera.model.Store
import com.ahs.camera.ui.JavacvCamera
import com.ahs.camera.ui.PhotoBlock
import com.ahs.camera.ui.MvCamrera
import com.ahs.camera.ICamera


object CameraManager : ICamera {

    val mvCamrera by lazy { MvCamrera(Store.camera1bufferedImage) }
    val javacvCamera by lazy { JavacvCamera(0, Store.camera2bufferedImage) }
    val javacvCamera2 by lazy { JavacvCamera(1, Store.camera3bufferedImage) }

    /**
     * cameraId : 1:屏幕显示相机; 2:俯拍相机; 3:侧拍相机
     */
    override fun takePhoto(cameraId: Int, block: PhotoBlock?) {
        when (cameraId) {
            1 -> mvCamrera.takePhoto { block?.invoke(it) }
            2 -> javacvCamera.takePhoto { block?.invoke(it) }
            3 -> javacvCamera2.takePhoto { block?.invoke(it) }
            else -> {
            }
        }
    }

    /**
     * 设置曝光
     * -1,-7
     */
    override fun exposure(cameraId: Int, value: Double) {
        when (cameraId) {
            2 -> javacvCamera.exposure(value)
            3 -> javacvCamera2.exposure(value)
            else -> {
            }
        }
    }

    /**
     * 设置焦距
     */
    override fun focus(cameraId: Int, value: Double) {
        when (cameraId) {
            2 -> javacvCamera.focus(value)
            3 -> javacvCamera2.focus(value)
            else -> {
            }
        }
    }

    /**
     * 释放相机
     */
    override fun close(cameraId: Int) {
        when (cameraId) {
            1 -> mvCamrera.stop()
            2 -> javacvCamera.stop()
            3 -> javacvCamera2.stop()
            else -> {
            }
        }
    }
}