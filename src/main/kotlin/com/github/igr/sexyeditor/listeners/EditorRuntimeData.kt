package com.github.igr.sexyeditor.listeners

import com.github.igr.sexyeditor.config.BackgroundConfiguration
import kotlin.random.Random

class EditorRuntimeData(
    private val config: BackgroundConfiguration,
    private val border: BackgroundBorder,
) {

    // the index of current image
    private var imageIndex = -1
    // current thread that changes images
    private var slideshowThread: Thread? = null
    private var slideshowThreadRunning = false

    init {
        if (config.slideshow) {
            slideshowThreadRunning = true
            slideshowThread = createThread(config.slideshowPause)
        }
    }

    /**
     * Returns the file name of the next image. If random mode is not enabled,
     * returns the very next image, otherwise, the random one.
     */
    fun selectNextImage(): String? {
        val total: Int = config.fileNames.size
        if (total == 0) {
            return null
        }
        if (config.random) {
            imageIndex = Random.nextInt(total)
        } else {
            imageIndex++
            if (imageIndex >= total) {
                imageIndex = 0
            }
        }
        return config.fileNames[imageIndex]
    }

    private fun createThread(slideshowPause: Int): Thread {
        return Thread {
            while (slideshowThreadRunning) {
                try {
                    Thread.sleep(slideshowPause * 1000L)
                } catch (_: InterruptedException) {
                    return@Thread
                }
                border.loadImage(selectNextImage())
            }
        }.apply {
            isDaemon = true
            priority = Thread.MIN_PRIORITY
            start()
        }
    }

    private fun stopThread() {
        slideshowThreadRunning = false
        slideshowThread?.interrupt()
        while (slideshowThread!!.isAlive) {
            Thread.sleep(100)
        }
        slideshowThread = null
    }

    fun end() {
        if (slideshowThreadRunning) {
            stopThread()
        }
    }
}