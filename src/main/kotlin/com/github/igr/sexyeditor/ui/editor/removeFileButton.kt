package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import java.awt.Dimension
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JPanel

fun uiCreateRemoveFileButton(panel: JPanel, dimension: Dimension, actionListener: ActionListener): JButton {
    val removeFilesButton = JButton().apply {
        text = PluginBundle.message("button.remove")
        addActionListener(actionListener)
    }
    panel.add(
        removeFilesButton,
        GridConstraints(
            10, 2, 1, 1,
            ANCHOR_CENTER, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
            null, dimension, null, 0, false
        )
    )
    return removeFilesButton
}