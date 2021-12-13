package com.ahs.camera.ui

import androidx.compose.runtime.MutableState
import com.ahs.camera.model.Store
import com.ahs.camera.utils.pictureHeight
import com.ahs.camera.utils.pictureWidth
import com.ahs.camera.utils.safeCancel
import com.ahs.camera.utils.tryCatchFor
import com.ahs.camera.utils.bufferedImage2ByteArray
import com.ahs.camera.utils.createThumbnail
import com.ahs.camera.utils.getBufferedImage
import kotlinx.coroutines.*
import org.bytedeco.javacv.OpenCVFrameGrabber
import org.opencv.videoio.Videoio
import java.awt.image.BufferedImage
import javax.swing.JComponent


/**
 * @Description
 * @Author lux
 * @Date 2021/11/18 3:51 下午
 * @Version
 */
class JavacvCamera(
    var index: Int = 0,
    var bufferedImage: MutableState<BufferedImage?>,
    private val picWidth: Int = pictureWidth,
    private val picHeight: Int = pictureHeight,
) : CamreraPanel {

    private var job: Job? = null
    private var grabber: OpenCVFrameGrabber? = null

    @Synchronized
    override fun start() {
        job = CoroutineScope(Dispatchers.IO).launch {
            tryCatchFor({
                grabber = OpenCVFrameGrabber.createDefault(index).apply {
//                    imageWidth = picWidth
//                    imageHeight = picHeight
                    start()
                    changeCameraStatus(1)
                }
                do {
                    getBufferedImage(grabber?.grab()).let {
                        bufferedImage.value = it
                    }
                    // 每40毫秒刷新视频,一秒25帧
                    Thread.sleep(40)
                } while (isActive && grabber != null)
            }, {
                changeCameraStatus(0)
                stop()
            })
        }
    }

    private fun changeCameraStatus(status: Int) {
        if (index == 0) {
            Store.device.camera2Status.value = status
        } else {
            Store.device.camera3Status.value = status
        }
    }

    @Synchronized
    override fun stop() {
        println("stop")

        job?.safeCancel()

        grabber?.let {
            grabber?.stop()
            grabber = null
        }

        bufferedImage.value = null
    }

    @Synchronized
    override fun isStart(): Boolean {
        return grabber != null
    }

    override fun takePhoto(block: PhotoBlock?) {
        CoroutineScope(Dispatchers.IO).launch {
            block?.invoke(bufferedImage2ByteArray(createThumbnail(getBufferedImage(grabber?.grab()))))
        }
    }

    override fun getComponent(): JComponent? = null

    /**
     * 设置曝光
     * -1,-7
     */
    fun exposure(value: Double) {
        grabber?.setOption(Videoio.CAP_PROP_AUTO_EXPOSURE, value) //0.75 作为自动曝光开启和 0.25 作为自动曝光关闭
        grabber?.setOption(Videoio.CAP_PROP_EXPOSURE, 100000.0)
    }

    /**
     * 设置焦距
     */
    fun focus(value: Double) {
        grabber?.setOption(Videoio.CAP_PROP_GAIN, value)
    }
}