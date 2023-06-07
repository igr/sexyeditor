package com.github.igr.sexyeditor.listeners

import com.github.igr.sexyeditor.config.BackgroundConfiguration
import kotlin.random.Random

class EditorRuntimeData(
    val config: BackgroundConfiguration,
) {

    // the index of current image
    private var imageIndex = -1

    /**
     * Returns the file name of the next image. If random mode is not enabled,
     * returns the very next image, otherwise, the random one.
     */
    fun getNextImage(): String? {
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
}