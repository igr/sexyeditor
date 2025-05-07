package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.github.igr.sexyeditor.ui.EditorConfigUI
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST
import com.intellij.uiDesigner.core.GridConstraints.FILL_NONE
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.Dimension

fun uiCreateFixedPositionCheckbox(panel: JPanel, dimension: Dimension): JCheckBox {
    val fixedPositionCheckBox = JCheckBox().apply {
        isSelected = true
        text = ""
    }
    panel.add(
        JLabel().apply {
            text = PluginBundle.message("label.fixed-position")
            labelFor = fixedPositionCheckBox
            toolTipText = PluginBundle.message("tooltip.fixed-position")
        },
        GridConstraints(
            3, 0, 1, 1,
            ANCHOR_WEST, FILL_NONE,
            SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
            null, dimension, null, 0, false
        )
    )
    panel.add(
        fixedPositionCheckBox,
        GridConstraints(
            3, 1, 1, 1,
            ANCHOR_WEST, FILL_NONE,
            SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
            SIZEPOLICY_FIXED,
            null, null, null, 0, false
        )
    )
    return fixedPositionCheckBox
}