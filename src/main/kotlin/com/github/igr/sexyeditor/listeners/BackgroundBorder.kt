package com.github.igr.sexyeditor.listeners

import com.github.igr.sexyeditor.config.BackgroundConfiguration
import com.github.igr.sexyeditor.config.BackgroundPosition
import com.github.igr.sexyeditor.config.BackgroundPosition.*
import com.github.igr.sexyeditor.ui.createScaledInstance
import com.intellij.openapi.diagnostic.thisLogger
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

    private var runtime: EditorRuntimeData = EditorRuntimeData(config, this)
    private var image: BufferedImage? = null
    private var imageWidth = 0
    private var imageHeight = 0
    private var active = false

    init {
        active = true
        loadImage(runtime.selectNextImage())
    }

    // ---------------------------------------------------------------- border

    /**
     * Paints the border and the background.
     */
    override fun paintBorder(component: Component, graphics: Graphics, naX: Int, naY: Int, naWidth: Int, naHeight: Int) {
        if (image == null) {
            return
        }
        val g2d = graphics as Graphics2D
        g2d.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, config.opacity)
        val jv = component.parent as JViewport
        val width = jv.width
        val height = jv.height
        var x = jv.viewRect.x
        var y = jv.viewRect.y
        val position = config.position
        val positionOffset: Int = config.positionOffset
        val fixedLocation: Boolean = config.fixedPosition
        val shrinkToFit: Boolean = config.shrinkToFit

        var newImageWidth = imageWidth
        var newImageHeight = imageHeight

        // draw
        if (shrinkToFit) {
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
        }

        // x axis
        if (fixedLocation) {
            // image is not moved on scroll
            if (position.isOnLeftSide()) {
                x += positionOffset
            } else if (position.isVerticallyCentered()) {
                x += (width - newImageWidth) / 2
            } else if (position.isOnRightSide()) {
                x += width - newImageWidth - positionOffset
            }

            // y axis
            if (position.isOnTop()) {
                y += positionOffset
            } else if (position.isHorizontallyCentered()) {
                y += (height - newImageHeight) / 2
            } else if (position.isOnBottom()) {
                y += height - newImageHeight - positionOffset
            }
        } else {
            // image is move with scroll
            if (position.isOnLeftSide()) {
                x = positionOffset
            } else if (position.isVerticallyCentered()) {
                x = (width - newImageWidth) / 2
            } else if (position.isOnRightSide()) {
                x = width - newImageWidth - positionOffset
            }

            // y axis
            if (position.isOnTop()) {
                y = positionOffset
            } else if (position.isHorizontallyCentered()) {
                y = (height - newImageHeight) / 2
            } else if (position.isOnBottom()) {
                y = height - newImageHeight - positionOffset
            }
        }

        // draw
        if (!shrinkToFit) {
            g2d.drawImage(image, x, y, jv)
        } else {
            g2d.drawImage(image, x, y, newImageWidth, newImageHeight, jv)
        }
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
    fun loadImage(fileName: String?) {
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
        thisLogger().debug("Load image: {}", fileName)

        var image = readImage(fileName)
        if (image == null) {
            this.image = null
            component.repaint()
            return
        }
        if (config.resize) {
            val imageWidth = image.width
            val imageHeight = image.height
            val screen = Toolkit.getDefaultToolkit().screenSize
            val screenWidth = (screen.getWidth() * config.resizeValue / 100.0).toInt()
            val screenHeight = (screen.getHeight() * config.resizeValue / 100.0).toInt()
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
        runtime.end()
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
    private fun isClosed(): Boolean {
        return !active
    }

}