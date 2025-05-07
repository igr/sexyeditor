package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST
import com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL
import com.intellij.uiDesigner.core.GridConstraints.FILL_NONE
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import java.awt.Dimension

fun uiCreateNameTextField(panel: JPanel, labelDimension: Dimension, fieldDimension: Dimension): JTextField {
    val nameTextField = JTextField().apply {
        text = ""
    }
    panel.add(
        JLabel().apply {
            text = PluginBundle.message("label.name")
            labelFor = nameTextField
            toolTipText = PluginBundle.message("tooltip.name")
        },
        GridConstraints(
            0, 0, 1, 1,
            ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
            null, labelDimension, null, 0, false
        )
    )
    panel.add(
        nameTextField,
        GridConstraints(
            0, 1, 1, 4,
            ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
            null, fieldDimension, null, 0, false
        )
    )
    return nameTextField
}