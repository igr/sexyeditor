package com.github.igr.sexyeditor.listeners

import com.github.igr.sexyeditor.config.BackgroundConfiguration
import com.github.igr.sexyeditor.config.BackgroundPosition
import com.github.igr.sexyeditor.config.BackgroundPosition.*
import com.github.igr.sexyeditor.ui.createScaledInstance
import com.intellij.util.ui.JBUI
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JViewport
import javax.swing.border.Border

/**
 * Editors border that draws background image.
 * Each editor has its own background border instance.
 */
class BackgroundBorder(
    private val config: BackgroundConfiguration,
    private val component: Component
) : Border {

    private var runtime: EditorRuntimeData = EditorRuntimeData(config)
    private var imageFileName: String? = null
    private var image: BufferedImage? = null
    private var imageWidth = 0
    private var imageHeight = 0

    private var active = false

    init {
        active = true
        loadImage(runtime.getNextImage())
    }

    // ---------------------------------------------------------------- border

    /**
     * Paints the border and the background.
     */
    override fun paintBorder(component: Component, graphics: Graphics, x: Int, y: Int, width: Int, height: Int) {
        var x = x
        var y = y
        var width = width
        var height = height

        if (image == null) {
            return
        }
        val g2d = graphics as Graphics2D
        g2d.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, config.opacity)
        val jv = component.parent as JViewport
        width = jv.width
        height = jv.height
        x = jv.viewRect.x
        y = jv.viewRect.y
        val position = config.position
        val positionOffset: Int = config.positionOffset
        val fixedLocation: Boolean = config.fixedPosition
        val shrinkToFit: Boolean = config.shrinkToFit
        // x axis
        if (fixedLocation) {
            // image is not moved on scroll
            if (isOnLeftSide(position)) {
                x += positionOffset
            } else if (isVerticallyCentered(position)) {
                x += width - imageWidth shr 1
            } else if (isOnRightSide(position)) {
                x += width - imageWidth - positionOffset
            }

            // y axis
            if (isOnTop(position)) {
                y += positionOffset
            } else if (isHorizontallyCentered(position)) {
                y += height - imageHeight shr 1
            } else if (isOnBottom(position)) {
                y += height - imageHeight - positionOffset
            }
        } else {
            // image is move with scroll
            if (isOnLeftSide(position)) {
                x = positionOffset
            } else if (isVerticallyCentered(position)) {
                x = width - imageWidth shr 1
            } else if (isOnRightSide(position)) {
                x = width - imageWidth - positionOffset
            }

            // y axis
            if (isOnTop(position)) {
                y = positionOffset
            } else if (isHorizontallyCentered(position)) {
                y = height - imageHeight shr 1
            } else if (isOnBottom(position)) {
                y = height - imageHeight - positionOffset
            }
        }

        // draw
        if (!shrinkToFit) {
            g2d.drawImage(image, x, y, jv)
        } else {
            var newImageWidth = imageWidth
            var newImageHeight = imageHeight
            val visibleWidth = width - 2 * positionOffset
            val visibleHeight = height - 2 * positionOffset
            if (newImageWidth > visibleWidth) {
                // reduce image height
                val scale = visibleWidth / newImageWidth.toDouble()
                newImageWidth = visibleWidth
                newImageHeight = (newImageHeight * scale).toInt()
            }
            if (newImageHeight > visibleHeight) {
                // reduce image width
                val scale = visibleHeight / newImageHeight.toDouble()
                newImageHeight = visibleHeight
                newImageWidth = (newImageWidth * scale).toInt()
            }
            g2d.drawImage(image, x, y, newImageWidth, newImageHeight, jv)
        }
    }

    private fun isOnBottom(position: BackgroundPosition): Boolean {
        return position == POSITION_BOTTOM_LEFT ||
                position == POSITION_BOTTOM_MIDDLE ||
                position == POSITION_BOTTOM_RIGHT
    }

    private fun isHorizontallyCentered(position: BackgroundPosition): Boolean {
        return position == POSITION_MIDDLE_LEFT ||
                position == POSITION_CENTER ||
                position == POSITION_MIDDLE_RIGHT
    }

    private fun isOnTop(position: BackgroundPosition): Boolean {
        return position == POSITION_TOP_LEFT ||
                position == POSITION_TOP_MIDDLE ||
                position == POSITION_TOP_RIGHT
    }

    private fun isOnRightSide(position: BackgroundPosition): Boolean {
        return position == POSITION_TOP_RIGHT ||
                position == POSITION_MIDDLE_RIGHT ||
                position == POSITION_BOTTOM_RIGHT
    }

    private fun isVerticallyCentered(position: BackgroundPosition): Boolean {
        return position == POSITION_TOP_MIDDLE ||
                position == POSITION_CENTER ||
                position == POSITION_BOTTOM_MIDDLE
    }

    private fun isOnLeftSide(position: BackgroundPosition): Boolean {
        return position == POSITION_TOP_LEFT ||
                position == POSITION_MIDDLE_LEFT ||
                position == POSITION_BOTTOM_LEFT
    }

    /**
     * Returns the insets of the border.
     */
    override fun getBorderInsets(c: Component?): Insets {
        return JBUI.emptyInsets()
    }

    /**
     * Returns if the border is opaque.
     */
    override fun isBorderOpaque(): Boolean {
        return true
    }

    // ---------------------------------------------------------------- image
    /**
     * Loads specified image in the background and shrink to fit if required so.
     * Repaints parent component of this border.
     */
    private fun loadImage(fileName: String?) {
        if (fileName == null) {
            return
        }
        Thread { loadImageNow(fileName) }.start()
    }

    /**
     * Loads an image.
     */
    private fun loadImageNow(fileName: String) {
        if (isClosed()) {
            return
        }
        imageFileName = fileName
        var image = readImage(fileName)
        if (image == null) {
            this.image = null
            component.repaint()
            return
        }
        if (config.shrink) {
            val imageWidth = image.width
            val imageHeight = image.height
            val screen = Toolkit.getDefaultToolkit().screenSize
            val screenWidth = (screen.getWidth() * config.shrinkValue / 100.0).toInt()
            val screenHeight = (screen.getHeight() * config.shrinkValue / 100.0).toInt()
            var ratioW = 1.0f
            var ratioH = 1.0f
            var scale = false
            if (imageWidth > screenWidth) {
                scale = true
                ratioW = screenWidth / imageWidth.toFloat()
            }
            if (imageHeight > screenHeight) {
                scale = true
                ratioH = screenHeight / imageHeight.toFloat()
            }

            // image really should be scaled down
            if (scale) {
                val ratio = if (ratioH < ratioW) ratioH else ratioW
                val targetWidth = (imageWidth * ratio).toInt()
                val targetHeight = (imageHeight * ratio).toInt()
                image = createScaledInstance(
                    image,
                    targetWidth,
                    targetHeight,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                    true
                )
            }
        }
        if (isClosed()) {
            return
        }
        imageWidth = image.width
        imageHeight = image.height
        this.image = image
        component.repaint()
    }

    /**
     * Reads the image from the file system.
     */
    private fun readImage(imageFileName: String?): BufferedImage? {
        return if (imageFileName == null) {
            null
        } else try {
            ImageIO.read(File(imageFileName))
        } catch (ignore: IOException) {
            null
        }
    }

    /**
     * Removes image from the background border and repaints component.
     * Caution: due to asynchronous image loading, image still can be loaded
     * after this method returns.
     */
    fun removeImage() {
        image = null
        imageWidth = 0
        imageHeight = 0
        imageFileName = null
        component.repaint()
    }

    /**
     * Closes current border instance: [.readImage] and assures
     * that no further image is going to be loaded after.
     */
    fun close() {
        active = false
        removeImage()
    }


    /**
     * Returns `true` if this instance is closed.
     */
    fun isClosed(): Boolean {
        return !active
    }

    override fun toString(): String {
        return "BackgroundBorder (" + imageFileName + ": " + imageWidth + 'x' + imageHeight + ')'
    }
}