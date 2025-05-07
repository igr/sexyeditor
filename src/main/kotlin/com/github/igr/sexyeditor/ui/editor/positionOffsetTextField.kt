package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST
import com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL
import com.intellij.uiDesigner.core.GridConstraints.FILL_NONE
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import java.awt.Dimension

fun uiCreatePositionOffsetTextField(panel: JPanel, dimension: Dimension): JTextField {
    val positionOffsetTextField = JTextField().apply {
        columns = 4
    }
    panel.add(
        JLabel().apply {
            text = PluginBundle.message("label.offset")
            labelFor = positionOffsetTextField
            toolTipText = PluginBundle.message("tooltip.position-offset")
        },
        GridConstraints(
            2, 3, 1, 1,
            ANCHOR_EAST, FILL_NONE, SIZEPOLICY_WANT_GROW, SIZEPOLICY_FIXED,
            null, null, null, 0, false
        )
    )
    panel.add(
        positionOffsetTextField,
        GridConstraints(
            2, 4, 1, 1,
            ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
            null, dimension, null, 0, false
        )
    )
    return positionOffsetTextField
}