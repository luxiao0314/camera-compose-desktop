package com.ahs.camera.utils

import MvCameraControlWrapper.MvCameraControl
import MvCameraControlWrapper.MvCameraControlDefines
import MvCameraControlWrapper.MvCameraControlDefines.MV_CC_DEVICE_INFO
import MvCameraControlWrapper.MvCameraControlDefines.MV_FRAME_OUT_INFO
import com.ahs.camera.model.Message
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.FrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import org.bytedeco.javacv.Java2DFrameUtils
import org.jetbrains.skiko.toBitmap
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.*
import java.util.*
import javax.imageio.ImageIO
import javax.swing.BorderFactory
import javax.swing.border.CompoundBorder

/**
 * @Description
 * @Author lux
 * @Date 2021/11/18 4:59 下午
 * @Version
 */
fun chooseCamera(stDeviceList: List<MV_CC_DEVICE_INFO>?): Int {
    if (null == stDeviceList) {
        return -1
    }

    // Choose a device to operate
    var camIndex = -1
    val scanner = Scanner(System.`in`)
    while (true) {
        try {
            print("Please input camera index (-1 to quit):")
            camIndex = scanner.nextInt()
            if (camIndex >= 0 && camIndex < stDeviceList.size || -1 == camIndex) {
                break
            } else {
                println("Input error: $camIndex")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            camIndex = -1
            break
        }
    }
    scanner.close()
    if (-1 == camIndex) {
        println("Bye.")
        return camIndex
    }
    if (0 <= camIndex && stDeviceList.size > camIndex) {
        if (MvCameraControlDefines.MV_GIGE_DEVICE == stDeviceList[camIndex].transportLayerType) {
            println("Connect to camera[" + camIndex + "]: " + stDeviceList[camIndex].gigEInfo.userDefinedName)
        } else if (MvCameraControlDefines.MV_USB_DEVICE == stDeviceList[camIndex].transportLayerType) {
            println("Connect to camera[" + camIndex + "]: " + stDeviceList[camIndex].usb3VInfo.userDefinedName)
        } else {
            println("Device is not supported.")
        }
    } else {
        println("Invalid index $camIndex")
        camIndex = -1
    }
    return camIndex
}

fun printDeviceInfo(stDeviceInfo: MV_CC_DEVICE_INFO?) {
    if (null == stDeviceInfo) {
        println("stDeviceInfo is null")
        return
    }
    when (stDeviceInfo.transportLayerType) {
        MvCameraControlDefines.MV_GIGE_DEVICE -> {
            println("\tCurrentIp:       " + stDeviceInfo.gigEInfo.currentIp)
            println("\tModel:           " + stDeviceInfo.gigEInfo.modelName)
            println("\tUserDefinedName: " + stDeviceInfo.gigEInfo.userDefinedName)
        }
        MvCameraControlDefines.MV_USB_DEVICE -> {
            println("\tUserDefinedName: " + stDeviceInfo.usb3VInfo.userDefinedName)
            println("\tSerial Number:   " + stDeviceInfo.usb3VInfo.serialNumber)
            println("\tDevice Number:   " + stDeviceInfo.usb3VInfo.deviceNumber)
        }
        else -> {
            System.err.print("Device is not supported! \n")
        }
    }
    println(
        "\tAccessible:      "
                + MvCameraControl.MV_CC_IsDeviceAccessible(stDeviceInfo, MvCameraControlDefines.MV_ACCESS_Exclusive)
    )
    println("")
}

fun printFrameInfo(stFrameInfo: MV_FRAME_OUT_INFO?) {
    if (null == stFrameInfo) {
        System.err.println("stFrameInfo is null")
        return
    }
    val frameInfo = StringBuilder("")
    frameInfo.append(("\tFrameNum[" + stFrameInfo.frameNum + "]"))
    frameInfo.append("\tWidth[" + stFrameInfo.width + "]")
    frameInfo.append("\tHeight[" + stFrameInfo.height + "]")
    frameInfo.append(String.format("\tPixelType[%#x]", stFrameInfo.pixelType.getnValue()))
    println(frameInfo.toString())
}

