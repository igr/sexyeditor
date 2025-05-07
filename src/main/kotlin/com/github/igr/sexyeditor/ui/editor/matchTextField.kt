package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

fun uiCreateMatchTextField(panel: JPanel, labelDimension: Dimension, textFieldDimension: Dimension): JTextField {
    val matchTextField = JTextField()
    panel.add(
        JLabel().apply {
            text = PluginBundle.message("label.match")
            labelFor = matchTextField
            toolTipText = PluginBundle.message("tooltip.match")
        },
        GridConstraints(
            1, 0, 1, 1,
            ANCHOR_WEST, FILL_NONE,
            SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
            null, labelDimension, null, 0, false
        )
    )
    panel.add(
        matchTextField,
        GridConstraints(
            1, 1, 1, 4,
            ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
            null, textFieldDimension, null, 0, false
        )
    )
    return matchTextField
}