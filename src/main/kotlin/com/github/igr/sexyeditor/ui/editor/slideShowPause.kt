package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST
import com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED
import javax.swing.JPanel
import javax.swing.JTextField
import java.awt.Dimension

fun uiCreateSlideShowPause(panel: JPanel, dimension: Dimension): JTextField {
    val slideShowPause = JTextField().apply {
        columns = 10
        isEnabled = false
        toolTipText = PluginBundle.message("tooltip.slideshow-pause")
    }
    panel.add(
        slideShowPause,
        GridConstraints(
            8, 1, 1, 1,
            ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
            null, dimension, null, 0, false
        )
    )
    return slideShowPause
}