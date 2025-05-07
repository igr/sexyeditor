package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.github.igr.sexyeditor.config.BackgroundPosition
import com.github.igr.sexyeditor.ui.PositionComboBoxRenderer
import com.intellij.openapi.ui.ComboBox
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST
import com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL
import com.intellij.uiDesigner.core.GridConstraints.FILL_NONE
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.Dimension

fun uiCreatePositionComboBox(panel: JPanel, labelDimension: Dimension, comboboxDimension: Dimension): JComboBox<BackgroundPosition> {
    val positionComboBox = ComboBox<BackgroundPosition>().apply {
        isEditable = false
        maximumRowCount = 9
        renderer = PositionComboBoxRenderer()
    }
    panel.add(
        JLabel().apply {
            text = PluginBundle.message("label.position")
            labelFor = positionComboBox
            toolTipText = PluginBundle.message("tooltip.position")
        },
        GridConstraints(
            2, 0, 1, 1,
            ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
            null, labelDimension, null, 0, false
        )
    )
    panel.add(
        positionComboBox,
        GridConstraints(
            2, 1, 1, 2,
            ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
            null, comboboxDimension, null, 0, false
        )
    )
    return positionComboBox
}
