package com.github.igr.sexyeditor.ui

import java.awt.Component
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer
import javax.swing.SwingConstants

class PositionComboBoxRenderer : JLabel(), ListCellRenderer<Int> {

    companion object {
        val POSITIONS = arrayOf(
            "Top-Left",
            "Top-Middle",
            "Top-Right",
            "Middle-Left",
            "Center",
            "Middle-Right",
            "Bottom-Left",
            "Bottom-Middle",
            "Bottom-Right"
        )
    }

    private val positionIcons = PluginIcons.POSITIONS

    init {
        isOpaque = true
        horizontalAlignment = SwingConstants.LEFT
        verticalAlignment = SwingConstants.CENTER
        preferredSize = Dimension(160, 20)
    }

    override fun getListCellRendererComponent(
        list: JList<out Int>,
        value: Int,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        if (isSelected) {
            background = list.selectionBackground
            foreground = list.selectionForeground
        } else {
            background = list.background
            foreground = list.foreground
        }
        icon = positionIcons[value]
        iconTextGap = 6
        text = POSITIONS[value]
        return this
    }

}
