package com.github.igr.sexyeditor.ui

import com.intellij.ui.JBColor
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.RenderingHints
import java.awt.event.ActionEvent
import java.awt.image.BufferedImage
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.math.max

class ImagePreviewPanel : JPanel(), PropertyChangeListener {

    private val label: JLabel
    private var sure: JButton? = null
    private var maxImgWidth = 0

    private val previewSize: Int
    private var maxFileSize = 512 * 1024
    private var showOnlyOnImages = false
    private val imageExtensions = arrayOf("gif", "jpg", "jpeg", "bmp", "png")

    init {
        previewSize = 200
        layout = BorderLayout(5, 5)
        border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
        add(JLabel("Preview:"), BorderLayout.NORTH)

        label = JLabel().apply {
            background = JBColor.WHITE
            isOpaque = true
            horizontalAlignment = SwingConstants.CENTER
            preferredSize = Dimension(previewSize, previewSize)
            border = BorderFactory.createEtchedBorder()
        }

        maxImgWidth = previewSize - 5
        add(label, BorderLayout.CENTER)
        sure = JButton("big image - click to preview").apply {
            isVisible = false
        }
        add(sure, BorderLayout.SOUTH)
        sure!!.addActionListener { e: ActionEvent? ->
            sure!!.isVisible = false
            val icon: Icon? = loadImage()
            label.icon = icon
            this@ImagePreviewPanel.repaint()
        }
    }

    /**
     * Attach this accessory to file chooser and, optionally, add file filter for image files.
     */
    fun attachToFileChooser(fileChooser: JFileChooser, imagesFilterName: String?) {
        fileChooser.accessory = this
        fileChooser.addPropertyChangeListener(this)
        if (imagesFilterName != null) {
            fileChooser.fileFilter = FileNameExtensionFilter(imagesFilterName, *imageExtensions)
        }
    }

    private var newFile: File? = null

    override fun propertyChange(evt: PropertyChangeEvent) {
        var icon: Icon? = null
        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY == evt.propertyName) {
            if (showOnlyOnImages) {
                this.isVisible = false
            }
            if (evt.newValue != null) {
                newFile = evt.newValue as File
                val path: String = newFile!!.absolutePath
                val i = path.lastIndexOf('.')
                if (i != -1) {
                    val extension = path.substring(i + 1).lowercase(Locale.getDefault())
                    for (ext in imageExtensions) {
                        if (extension == ext) {
                            if (showOnlyOnImages) {
                                this.isVisible = true
                            }
                            sure!!.isVisible = maxFileSize != -1 && newFile!!.length() > maxFileSize
                            if (!sure!!.isVisible) {
                                icon = loadImage()
                            }
                            break
                        }
                    }
                }
            }
            label.icon = icon
            this.repaint()
        }
    }

    /**
     * Loads and scales images for previewing purposes if necessary.
     */
    private fun loadImage(): Icon? {
        if (newFile == null) {
            return null
        }
        try {
            val image: BufferedImage = ImageIO.read(newFile)
            var width = image.width.toFloat()
            var height = image.height.toFloat()
            if (width <= maxImgWidth) {
                return ImageIcon(image)
            }
            val scale = height / width
            width = maxImgWidth.toFloat()
            height = width * scale // height should be scaled from new width
            return ImageIcon(
                createScaledInstance(
                    image,
                    max(1, width.toInt()),
                    max(1, height.toInt()),
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                    true
                )
            )
        } catch (ioex: IOException) {
            // ignore, couldn't read image
        } finally {
            newFile = null
        }
        return null
    }

    // ---------------------------------------------------------------- accessories

    /**
     * Returns preview size.
     */
    fun getPreviewSize(): Int {
        return previewSize
    }

    fun getMaxFileSize(): Int {
        return maxFileSize
    }

    /**
     * Sets maximal file size of image that will be previewed automatically.
     * By setting this value to `-1` all images will be previewed.
     */
    fun setMaxFileSize(maxFileSize: Int) {
        this.maxFileSize = maxFileSize
    }

    fun isShowOnlyOnImages(): Boolean {
        return showOnlyOnImages
    }

    fun setShowOnlyOnImages(showOnlyOnImages: Boolean) {
        this.showOnlyOnImages = showOnlyOnImages
    }
}