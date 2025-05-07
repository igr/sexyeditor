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
import javax.swing.JSlider
import java.awt.Dimension

fun uiCreateOpacitySlider(panel: JPanel, labelDimension: Dimension): JSlider {
    val opacitySlider = JSlider().apply {
        majorTickSpacing = 10
        minorTickSpacing = 5
        paintLabels = true
        paintTicks = true
    }
    panel.add(
        JLabel().apply {
            text = PluginBundle.message("label.opacity")
            labelFor = opacitySlider
            toolTipText = PluginBundle.message("tooltip.opacity")
        },
        GridConstraints(
            5, 0, 1, 1,
            ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
            null, labelDimension, null, 0, false
        )
    )
    panel.add(
        opacitySlider,
        GridConstraints(
            5, 1, 1, 4,
            ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
            null, null, null, 0, false
        )
    )
    return opacitySlider
}