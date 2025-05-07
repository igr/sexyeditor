package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.github.igr.sexyeditor.config.FitType
import com.intellij.openapi.ui.ComboBox
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST
import com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL
import com.intellij.uiDesigner.core.GridConstraints.FILL_NONE
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JPanel

fun uiCreateFitTypeComboBox(panel: JPanel, dimension: Dimension): ComboBox<FitType> {
    val combo = ComboBox<FitType>().apply {
        isEditable = false
        maximumRowCount = 4
    }
    panel.add(
        JLabel().apply {
            text = PluginBundle.message("label.fit-to-editor")
            labelFor = combo
            toolTipText = PluginBundle.message("tooltip.fit-to-editor")
        },
        GridConstraints(
            4, 0, 1, 1,
            ANCHOR_WEST, FILL_NONE,
            SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
            null, null, null, 0, false
        )
    )
    panel.add(
        combo,
        GridConstraints(
            4, 1, 1, 2,
            ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
            null, dimension, null, 0, false
        )
    )
    return combo
}