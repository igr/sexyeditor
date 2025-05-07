package com.github.igr.sexyeditor.ui


import com.intellij.openapi.diagnostic.logger
import com.intellij.util.ui.ImageUtil
import com.intellij.util.ui.UIUtil
import java.awt.Color
import java.awt.RenderingHints
import java.awt.Transparency
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.ImageIcon

private val LOG = logger<UIUtil>()

/**
 * Convenience method that returns a scaled instance of the provided `BufferedImage`.
 *
 * @param img the original image to be scaled
 * @param targetWidth the desired width of the scaled instance, in pixels
 * @param targetHeight the desired height of the scaled instance, in pixels
 * @param hint one of the rendering hints that corresponds to `RenderingHints.KEY_INTERPOLATION` (e.g.
 * `RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR`,
 * `RenderingHints.VALUE_INTERPOLATION_BILINEAR`,
 * `RenderingHints.VALUE_INTERPOLATION_BICUBIC`)
 * @param higherQuality if true, this method will use a multi-step
 * scaling technique that provides higher quality than the usual
 * one-step technique (only useful in downscaling cases, where
 * `targetWidth` or `targetHeight` is
 * smaller than the original dimensions, and generally only when
 * the `BILINEAR` hint is specified)
 * @return a scaled version of the original `BufferedImage`
 */
fun createScaledInstance(
    img: BufferedImage,
    targetWidth: Int,
    targetHeight: Int,
    hint: Any?,
    higherQuality: Boolean
): BufferedImage {
    val type = if (img.transparency == Transparency.OPAQUE) BufferedImage.TYPE_INT_RGB
        else BufferedImage.TYPE_INT_ARGB
    var ret: BufferedImage = img
    var w: Int
    var h: Int
    if (higherQuality) {
        // Use multi-step technique: start with original size, then
        // scale down in multiple passes with drawImage()
        // until the target size is reached
        w = img.width
        h = img.height
    } else {
        // Use one-step technique: scale directly from original
        // size to target size with a single drawImage() call
        w = targetWidth
        h = targetHeight
    }
    do {
        if (higherQuality && w > targetWidth) {
            w /= 2
            if (w < targetWidth) {
                w = targetWidth
            }
        }
        if (higherQuality && h > targetHeight) {
            h /= 2
            if (h < targetHeight) {
                h = targetHeight
            }
        }
        val tmp: BufferedImage = ImageUtil.createImage(w, h, type)
        tmp.createGraphics().apply {
            setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint)
            drawImage(ret, 0, 0, w, h, null)
            dispose()
        }
        ret = tmp
    } while (w != targetWidth || h != targetHeight)

    return ret
}

fun loadImageAsIcon(path: String): ImageIcon? {
    val image = try {
        ImageIO.read(File(path))
    } catch (e: IOException) {
        LOG.warn("Could not open image '$path'", e)
        null
    }
    if (image == null) {
        return null
    }

    val maxWidth = 32f
    val maxHeight = 32f

    val priorHeight = image.height.toFloat()
    val priorWidth = image.width.toFloat()

    // Calculate the correct new height and width
    var newHeight: Float
    var newWidth: Float
    if (priorHeight / priorWidth > maxHeight / maxWidth) {
        newHeight = maxHeight
        newWidth = (priorWidth / priorHeight) * newHeight
    } else {
        newWidth = maxWidth
        newHeight = (priorHeight / priorWidth) * newWidth
    }

    // Resize the image

    // 1. Create a new Buffered Image and Graphic2D object
    val resizedImg = ImageUtil.createImage(maxWidth.toInt(), maxHeight.toInt(), BufferedImage.TYPE_INT_ARGB)
    val g2 = resizedImg.createGraphics()

    // 1.5 clear
    g2.color = Color(0, 0, 0, 0)
    g2.fillRect(0, 0, maxWidth.toInt(), maxHeight.toInt())

    // 2. Use the Graphic object to draw a new image to the image in the buffer
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    g2.drawImage(
        image,
        ((maxWidth - newWidth) / 2).toInt(),
        ((maxHeight - newHeight) / 2).toInt(),
        newWidth.toInt(),
        newHeight.toInt(),
        null
    )
    g2.dispose()

    return ImageIcon(resizedImg)
}
