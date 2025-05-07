package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST
import com.intellij.uiDesigner.core.GridConstraints.FILL_NONE
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED
import javax.swing.JCheckBox
import javax.swing.JPanel
import java.awt.Dimension

fun uiCreateRandomCheckBox(panel: JPanel, dimension: Dimension): JCheckBox {
    val randomCheckBox = JCheckBox().apply {
        text = PluginBundle.message("label.random")
        toolTipText = PluginBundle.message("tooltip.random")
    }
    panel.add(
        randomCheckBox,
        GridConstraints(
            7, 0, 1, 1,
            ANCHOR_WEST, FILL_NONE,
            SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
            null, dimension, null, 0, false
        )
    )
    return randomCheckBox
}