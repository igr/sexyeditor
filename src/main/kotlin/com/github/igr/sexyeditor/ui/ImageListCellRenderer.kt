package com.github.igr.sexyeditor.ui

import javax.swing.DefaultListCellRenderer
import javax.swing.JLabel

private val home = System.getProperty("user.home")

class ImageListCellRenderer : DefaultListCellRenderer {
    constructor() : super() {
        isOpaque = true
    }

    override fun getListCellRendererComponent(
        list: javax.swing.JList<*>,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): java.awt.Component {
        val imageFile = value as ImageFile
        val component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        val jLabel = component as JLabel

        //jLabel.setIcon(imageFile.icon)
        jLabel.setHorizontalTextPosition(RIGHT)

        jLabel.text = if (imageFile.path.startsWith(home)) {
            imageFile.path.replaceFirst("$home/", "~/")
        } else {
            imageFile.path
        }
        return component
    }
}