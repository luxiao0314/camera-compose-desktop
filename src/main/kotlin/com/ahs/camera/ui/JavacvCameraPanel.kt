package com.ahs.camera.ui

import com.ahs.camera.utils.*
import com.ahs.camera.utils.safeCancel
import kotlinx.coroutines.*
import org.bytedeco.javacv.OpenCVFrameGrabber
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JOptionPane


/**
 * @Description
 * @Author lux
 * @Date 2021/11/18 3:51 下午
 * @Version
 */
class JavacvCameraPanel(var index: Int = 0): JLabel(), CamreraPanel {

    private var job: Job? = null
    private var grabber: OpenCVFrameGrabber? = null

    init {
        addcvLine()
        start()
    }

    override fun getPanel() = this

    override fun start() {

        job = CoroutineScope(Dispatchers.IO).launch {

            tryCatchFor {

                grabber = OpenCVFrameGrabber.createDefault(index).apply {
                    imageWidth = pictureWidth
                    imageHeight = pictureHeight
                    start()
                }

                do {
                    getBufferedImage(grabber?.grab()).let {
                        icon = ImageIcon(createImgThumbnail(it, previewWidth, previewHeight))
                    }
                    // 每40毫秒刷新视频,一秒25帧
                    Thread.sleep(40)
                } while (isVisible && isActive && grabber != null)
            }
        }
    }

    override fun stop() {
        job?.safeCancel()
        grabber?.stop()
        grabber = null
    }

    override fun saveImage(path: String, block: (() -> Unit?)?) {
        CoroutineScope(Dispatchers.IO).launch {
            saveImage(path, getBufferedImage(grabber?.grab()))
            block?.invoke()
            JOptionPane.showMessageDialog(null, "拍照成功")
        }
    }
}