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
import javax.swing.JTextField
import java.awt.Dimension

fun uiCreateSlideShowCheckBox(panel: JPanel, dimension: Dimension, slideShowPause: JTextField): JCheckBox {
    val slideshowCheckBox = JCheckBox().apply {
        text = PluginBundle.message("label.slideshow")
        toolTipText = PluginBundle.message("tooltip.slideshow")
        addActionListener { slideShowPause.isEnabled = isSelected }
    }
    panel.add(
        slideshowCheckBox,
        GridConstraints(
            8, 0, 1, 1,
            ANCHOR_WEST, FILL_NONE,
            SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
            SIZEPOLICY_FIXED,
            null, dimension, null, 0, false
        )
    )
    return slideshowCheckBox
}