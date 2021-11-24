package com.ahs.camera.utils

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import kotlin.Throws
import java.lang.InterruptedException
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.awt.Graphics
import java.io.*

/**
 * 图片工具类
 *
 * @author hjn
 * @version 1.0 2013-11-26
 */

/**
 * 图片等比缩放居中剪裁
 * 不管尺寸不等的图片生成的缩略图都是同一尺寸，方便用于页面展示
 *
 * @param imageSrc图片所在路径
 * @param thumbWidth缩略图宽度
 * @param thumbHeight缩略图长度
 * @param outFilePath缩略图存放路径
 * @throws InterruptedException
 * @throws IOException
 */
@Throws(InterruptedException::class, IOException::class)
fun createImgThumbnail(imgSrc: String?, thumbWidth: Int, thumbHeight: Int, outFilePath: String?) {
    val imageFile = File(imgSrc)
    val image = ImageIO.read(imageFile)
    val width = image.width
    val height = image.height
    val i = width.toDouble() / height.toDouble()
    val j = thumbWidth.toDouble() / thumbHeight.toDouble()
    val d = IntArray(2)
    var x = 0
    var y = 0
    if (i > j) {
        d[1] = thumbHeight
        d[0] = (thumbHeight * i).toInt()
        y = 0
        x = (d[0] - thumbWidth) / 2
    } else {
        d[0] = thumbWidth
        d[1] = (thumbWidth / i).toInt()
        x = 0
        y = (d[1] - thumbHeight) / 2
    }
    val outFile = File(outFilePath)
    if (!outFile.parentFile.exists()) {
        outFile.parentFile.mkdirs()
    }
    /*等比例缩放*/
    var newImage = BufferedImage(d[0], d[1], image.type)
    val g = newImage.graphics
    g.drawImage(image, 0, 0, d[0], d[1], null)
    g.dispose()
    /*居中剪裁*/newImage = newImage.getSubimage(x, y, thumbWidth, thumbHeight)
    ImageIO.write(newImage, imageFile.name.substring(imageFile.name.lastIndexOf(".") + 1), outFile)
}

/**
 * 居中裁剪
 */
@Throws(InterruptedException::class, IOException::class)
fun createImgThumbnail(image: BufferedImage, thumbWidth: Int, thumbHeight: Int): BufferedImage {
    val width = image.width
    val height = image.height
    val i = width.toDouble() / height.toDouble()
    val j = thumbWidth.toDouble() / thumbHeight.toDouble()
    val d = IntArray(2)
    val x: Int
    val y: Int
    if (i > j) {
        d[1] = thumbHeight
        d[0] = (thumbHeight * i).toInt()
        y = 0
        x = (d[0] - thumbWidth) / 2
    } else {
        d[0] = thumbWidth
        d[1] = (thumbWidth / i).toInt()
        x = 0
        y = (d[1] - thumbHeight) / 2
    }
    /*等比例缩放*/
    val newImage = BufferedImage(d[0], d[1], image.type)
    val g = newImage.graphics
    g.drawImage(image, 0, 0, d[0], d[1], null)
    g.dispose()
    /*居中剪裁*/
    return newImage.getSubimage(x, y, thumbWidth, thumbHeight)
}

fun saveFile(path: String?, image: BufferedImage?) {
    val out = ByteArrayOutputStream()
    ImageIO.write(image, "jpg", out)
    val bs = out.toByteArray()
    saveFile(path, bs)
}

fun saveFile(path: String?, bs: ByteArray) {
    var os: OutputStream? = null
    try {
        val file = File(path.toString())
        if (file.parentFile != null && !file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        os = FileOutputStream(file)
        os.write(bs, 0, bs.size)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        // Close file stream
        try {
            os?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun byteArray2BufferedImage(data: ByteArray?): BufferedImage = ImageIO.read(ByteArrayInputStream(data))

fun getBufferedImage(frame: Frame?): BufferedImage = Java2DFrameConverter().getBufferedImage(frame)

fun saveImage(path: String?, image: BufferedImage?) {
    saveImage(path, image, 720, 720)
}

fun saveImage(path: String?, bs: ByteArray) {
    saveImage(path, bs, 720, 720)
}

fun saveImage(path: String?, image: BufferedImage?, width: Int, height: Int) =
    Thumbnails.of(image)
        .scale(1.00)
        .sourceRegion(Positions.CENTER, width, height)
        .outputQuality(1f)
        .toFile(path)

fun saveImage(path: String?, bs: ByteArray, width: Int, height: Int) =
    Thumbnails.of(ByteArrayInputStream(bs))
        .scale(1.00)
        .sourceRegion(Positions.CENTER, width, height)
        .outputQuality(1f)
        .toFile(path)

fun createThumbnail(image: BufferedImage, thumbWidth: Int, thumbHeight: Int): BufferedImage =
    Thumbnails.of(image)
        .sourceRegion(Positions.CENTER, image.height, image.height)  //裁剪大小
        .size(thumbWidth, thumbHeight)  //显示大小
        .outputQuality(0.2f)
        .keepAspectRatio(false)
        .asBufferedImage()





