package com.github.igr.sexyeditor.ui

import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon


data class ImageFile(
    val path: String
) {
    val icon: ImageIcon by lazy {
        val image = ImageIO.read(File(path))
        if (image == null) {
            return@lazy ImageIcon(ImageFile::class.java.getResource("/icons/warning.png"))
        }

        val maxWidth = 32f
        val maxHeight = 32f

        val priorHeight = image.height.toFloat()
        val priorWidth = image.width.toFloat()

        // Calculate the correct new height and width
        var newHeight = maxHeight
        var newWidth = maxWidth
        if (priorHeight / priorWidth > maxHeight / maxWidth) {
            newHeight = maxHeight
            newWidth = (priorWidth / priorHeight) * newHeight
        } else {
            newWidth = maxWidth
            newHeight = (priorHeight / priorWidth) * newWidth
        }

        // Resize the image

        // 1. Create a new Buffered Image and Graphic2D object
        val resizedImg = BufferedImage(maxWidth.toInt(), maxHeight.toInt(), BufferedImage.TYPE_INT_RGB)
        val g2 = resizedImg.createGraphics()

        // 2. Use the Graphic object to draw a new image to the image in the buffer
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g2.drawImage(image, ((maxWidth - newWidth) / 2).toInt(), ((maxHeight - newHeight) / 2).toInt(), newWidth.toInt(), newHeight.toInt(), null)
        g2.dispose()

        ImageIcon(resizedImg)
    }
}
