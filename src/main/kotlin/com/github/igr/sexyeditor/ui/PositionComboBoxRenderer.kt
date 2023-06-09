package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.config.BackgroundPosition
import java.awt.Component
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer
import javax.swing.SwingConstants

class PositionComboBoxRenderer : JLabel(), ListCellRenderer<BackgroundPosition> {

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
        list: JList<out BackgroundPosition>,
        value: BackgroundPosition,
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
        icon = positionIcons[value.value]
        iconTextGap = 6
        text = value.label
        return this
    }

}
