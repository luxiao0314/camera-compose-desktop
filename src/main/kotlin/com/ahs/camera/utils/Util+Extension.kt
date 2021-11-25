package com.ahs.camera.utils

import kotlinx.coroutines.Job
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.border.CompoundBorder


fun Job.safeCancel() {
    if (isActive) {
        cancel()
    }
}

fun <T> tryCatchFor(tryCatchBlock: () -> T, errorBlock: ((e: Exception) -> T)): T {
    return try {
        tryCatchBlock()
    } catch (e: Exception) {
        println(e.message)
        errorBlock.invoke(e)
    }
}

fun <T> tryCatchFor(tryCatchBlock: () -> T?): T? {
    return try {
        tryCatchBlock()
    } catch (e: Throwable) {
        println(e.message)
        null
    }
}

fun <T> tryCatchOrDefault(def: T, tryCatchBlock: () -> T): T {
    return tryCatchFor(tryCatchBlock) { def }
}

fun compoundBorder(): CompoundBorder {
    val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
    val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
    return CompoundBorder(empty, dashed)
}

/**
 * MvCamreraPanel
 */
fun JLabel.addmvLine() {
    //边框
    add(JLabel("").apply {
        border = BorderFactory.createLineBorder(Color.BLACK)
        setBounds(0, 0, mvPreviewWidth, mvPreviewHeight)
    }, BorderLayout.CENTER)

    //横线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds(0, mvPreviewHeight / 3, mvPreviewWidth, 1)
    }, BorderLayout.CENTER)

    //横线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds(0, mvPreviewHeight / 2, mvPreviewWidth, 1)
    }, BorderLayout.CENTER)

    //横线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds(0, (mvPreviewHeight / 1.5).toInt(), mvPreviewWidth, 1)
    }, BorderLayout.CENTER)

    //竖线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds(mvPreviewWidth / 3, 0, 1, mvPreviewHeight)
    }, BorderLayout.CENTER)

    //竖线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds(mvPreviewWidth / 2, 0, 1, mvPreviewHeight)
    }, BorderLayout.CENTER)

    //竖线
    add(JLabel("").apply {
        val dashed = BorderFactory.createDashedBorder(Color.BLUE, 5f, 5f)
        val empty = BorderFactory.createEmptyBorder(-1, 0, -1, -1)
        border = CompoundBorder(empty, dashed)
        setBounds((mvPreviewWidth / 1.5).toInt(), 0, 1, mvPreviewHeight)
    }, BorderLayout.CENTER)
}

/**
 * JavacvCameraPanel
 */
fun JLabel.addcvLine() {
    //边框
    add(JLabel("").apply {
        border = BorderFactory.createLineBorder(Color.BLACK)
        setBounds(0, 0, previewWidth, previewHeight)
    }, BorderLayout.CENTER)

    //横线
    add(JLabel("").apply {
        border = compoundBorder()
        setBounds(0, previewHeight / 2, previewWidth, 1)
    }, BorderLayout.CENTER)

    //竖线
    add(JLabel("").apply {
        border = compoundBorder()
        setBounds(previewWidth / 2, 0, 1, previewHeight)
    }, BorderLayout.CENTER)
}

fun filepath() =  "image/${System.currentTimeMillis()}.jpg"