package com.ahs.camera.model

/**
 * @Description
 * @Author lux
 * @Date 2021/11/17 3:34 下午
 * @Version
 */
class DeviceStatus(
    var deviceStatus: String = "",    //设备状态
    var cameraStatus: String = "",    //相机状态
    var netStatus: String = ""        //联网状态
)