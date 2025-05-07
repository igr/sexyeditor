package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST
import com.intellij.uiDesigner.core.GridConstraints.FILL_NONE
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.Dimension

fun uiCreateFileListLabel(panel: JPanel, dimension: Dimension) {
    panel.add(
        JLabel().apply {
            text = PluginBundle.message("label.file-list")
        },
        GridConstraints(
            9, 0, 1, 1,
            ANCHOR_NORTHWEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
            null, dimension, null, 0, false
        )
    )
}