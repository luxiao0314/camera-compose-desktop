package com.ahs.camera.ui

import MvCameraControlWrapper.CameraControlException
import MvCameraControlWrapper.MvCameraControl
import MvCameraControlWrapper.MvCameraControlDefines
import com.ahs.camera.utils.*
import com.ahs.camera.utils.safeCancel
import kotlinx.coroutines.*
import java.util.ArrayList
import javax.swing.*

/**
 * @Description
 * @Author lux
 * @Date 2021/11/18 3:51 下午
 * @Version
 */
class MvCamreraPanel : JLabel(), CamreraPanel {

    private var job: Job? = null
    private var nRet = MvCameraControlDefines.MV_OK
    private var hCamera: MvCameraControlDefines.Handle? = null

    init {
        addmvLine()
        start()
    }

    override fun getPanel() = this

    override fun start() {

        job = CoroutineScope(Dispatchers.IO).launch {

            tryCatchFor({

                if (createCamera()) return@tryCatchFor

                do {
                    byteArray2BufferedImage(getFrame()).let {
                        icon = ImageIcon(createThumbnail(it, previewWidth, previewHeight))
                    }
                    // 每40毫秒刷新视频,一秒25帧
                    Thread.sleep(40)
                } while (isVisible && isActive)
            }, {
                it.printStackTrace()
                stop()
            })
        }
    }

    override fun stop() {
        synchronized(this) {

            job?.safeCancel()

            hCamera?.let {
                // Stop grabbing
                nRet = MvCameraControl.MV_CC_StopGrabbing(hCamera)
                if (MvCameraControlDefines.MV_OK != nRet) {
                    System.err.printf("StopGrabbing fail, errcode: [%#x]\n", nRet)
                }

                // Destroy handle
                nRet = MvCameraControl.MV_CC_DestroyHandle(hCamera)
                if (MvCameraControlDefines.MV_OK != nRet) {
                    System.err.printf("DestroyHandle failed, errcode: [%#x]\n", nRet)
                }
            }
        }
    }

    override fun saveImage(path: String, block: (() -> Unit?)?) {
        CoroutineScope(Dispatchers.IO).launch {
            saveImage(path, getFrame())
            JOptionPane.showMessageDialog(null, "拍照成功")
        }
    }

    private fun createCamera(): Boolean {

        println("SDK Version " + MvCameraControl.MV_CC_GetSDKVersion())

        val stDeviceList: ArrayList<MvCameraControlDefines.MV_CC_DEVICE_INFO>

        // Enuerate GigE and USB devices
        try {
            stDeviceList =
                MvCameraControl.MV_CC_EnumDevices(MvCameraControlDefines.MV_GIGE_DEVICE or MvCameraControlDefines.MV_USB_DEVICE)
            if (0 >= stDeviceList.size) {
                println("No devices found!")
                return true
            }
            var i = 0
            for (stDeviceInfo in stDeviceList) {
                println("[camera " + i++ + "]")
                printDeviceInfo(stDeviceInfo)
            }
        } catch (e: CameraControlException) {
            System.err.println("Enumrate devices failed!$e")
            e.printStackTrace()
            return true
        }

        // Create handle
        try {
            hCamera = MvCameraControl.MV_CC_CreateHandle(stDeviceList[0])
        } catch (e: CameraControlException) {
            System.err.println("Create handle failed!$e")
            e.printStackTrace()
            hCamera = null
            return true
        }

        // Open device
        nRet = MvCameraControl.MV_CC_OpenDevice(hCamera)
        if (MvCameraControlDefines.MV_OK != nRet) {
            System.err.printf("Connect to camera failed, errcode: [%#x]\n", nRet)
            return true
        }

        // Make sure that trigger mode is off
        nRet = MvCameraControl.MV_CC_SetEnumValueByString(hCamera, "TriggerMode", "Off")
        if (MvCameraControlDefines.MV_OK != nRet) {
            System.err.printf("SetTriggerMode failed, errcode: [%#x]\n", nRet)
            return true
        }

        // Start grabbing
        nRet = MvCameraControl.MV_CC_StartGrabbing(hCamera)
        if (MvCameraControlDefines.MV_OK != nRet) {
            System.err.printf("Start Grabbing fail, errcode: [%#x]\n", nRet)
            return true
        }
        return false
    }

    private fun getFrame(): ByteArray {
        // Get payload size
        val stParam = MvCameraControlDefines.MVCC_INTVALUE()
        nRet = MvCameraControl.MV_CC_GetIntValue(hCamera, "PayloadSize", stParam)
        if (MvCameraControlDefines.MV_OK != nRet) {
            System.err.printf("Get PayloadSize fail, errcode: [%#x]\n", nRet)
        }

        // Get one frame
        val stImageInfo = MvCameraControlDefines.MV_FRAME_OUT_INFO()
        val pData = ByteArray(stParam.curValue.toInt())
        nRet = MvCameraControl.MV_CC_GetOneFrameTimeout(hCamera, pData, stImageInfo, 1000)
        if (MvCameraControlDefines.MV_OK != nRet) {
            System.err.printf("GetOneFrameTimeout fail, errcode:[%#x]\n", nRet)
        }

//        println("GetOneFrame: ")
//        printFrameInfo(stImageInfo)

        val imageLen = stImageInfo.width * stImageInfo.height * 3 // Every RGB pixel takes 3 bytes
        val imageBuffer = ByteArray(imageLen)

        // Call MV_CC_SaveImage to save image as JPEG
        val stSaveParam = MvCameraControlDefines.MV_SAVE_IMAGE_PARAM()
        stSaveParam.width = stImageInfo.width // image width
        stSaveParam.height = stImageInfo.height // image height
        stSaveParam.data = pData // image data
        stSaveParam.dataLen = stImageInfo.frameLen // image data length
        stSaveParam.pixelType = stImageInfo.pixelType // image pixel format
        stSaveParam.imageBuffer = imageBuffer // output image buffer
        stSaveParam.imageLen = imageLen // output image buffer size
        stSaveParam.imageType = MvCameraControlDefines.MV_SAVE_IAMGE_TYPE.MV_Image_Jpeg // output image pixel format
        stSaveParam.methodValue =
            0 // Interpolation method that converts Bayer format to RGB24.  0-Neareast 1-double linear 2-Hamilton
        stSaveParam.jpgQuality = 90 // JPG endoding quality(50-99]
        nRet = MvCameraControl.MV_CC_SaveImage(hCamera, stSaveParam)
        if (MvCameraControlDefines.MV_OK != nRet) {
            System.err.printf("SaveImage fail, errcode: [%#x]\n", nRet)
        }
        return imageBuffer
    }
}