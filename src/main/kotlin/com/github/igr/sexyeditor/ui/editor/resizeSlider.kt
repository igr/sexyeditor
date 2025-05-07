package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST
import com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED
import javax.swing.JPanel
import javax.swing.JSlider

fun uiCreateResizeSlider(panel: JPanel): JSlider {
    val resizeSlider = JSlider().apply {
        isEnabled = false
        majorTickSpacing = 10
        minimum = 0
        minorTickSpacing = 5
        paintLabels = true
        paintTicks = true
        toolTipText =  PluginBundle.message("tooltip.resize-slider")
    }
    panel.add(
        resizeSlider,
        GridConstraints(
            6, 1, 1, 4,
            ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
            null, null, null, 0, false
        )
    )
    return resizeSlider
}