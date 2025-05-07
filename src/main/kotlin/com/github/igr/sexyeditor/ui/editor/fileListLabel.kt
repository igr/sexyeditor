package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JPanel

fun uiCreateFileListLabel(panel: JPanel, dimension: Dimension): JLabel {
    val label = JLabel().apply {
        text = PluginBundle.message("label.file-list")
    }
    panel.add(
        label,
        GridConstraints(
            9, 0, 1, 1,
            ANCHOR_NORTHWEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
            null, dimension, null, 0, false
        )
    )
    return label
}

fun uiSetFileListLabelText(label: JLabel, count: Int) {
    label.text = PluginBundle.message("label.file-list") + "<br><br><b>${count}</b> files"
}