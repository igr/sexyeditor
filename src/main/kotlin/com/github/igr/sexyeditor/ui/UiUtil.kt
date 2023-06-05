package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.SexyEditor
import com.intellij.util.ui.ImageUtil
import java.awt.RenderingHints
import java.awt.Transparency
import java.awt.image.BufferedImage
import javax.swing.ImageIcon


fun iconOf(name: String): ImageIcon {
    val path = "/icons/$name.png"
    val url = SexyEditor::class.java.getResource(path)
    return ImageIcon(url)
}

fun generalIconOf(name: String): ImageIcon {
    val path = "/general/$name.png"
    val url = SexyEditor::class.java.getResource(path)
    return ImageIcon(url)
}

fun actionIconOf(name: String): ImageIcon {
    val path = "/actions/$name.png"
    val url = SexyEditor::class.java.getResource(path)
    return ImageIcon(url)
}


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