package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.github.igr.sexyeditor.ui.PluginIcons
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import java.awt.Dimension
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JPanel

fun uiCreateInsertFilesButton(panel: JPanel, dimension: Dimension, actionListener: ActionListener): JButton {
    val insertFilesButton = JButton().apply {
        icon = PluginIcons.IMAGES
        text = PluginBundle.message("label.add-images")
        addActionListener(actionListener)
    }
    panel.add(
        insertFilesButton,
        GridConstraints(
            10, 1, 1, 1,
            ANCHOR_CENTER, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
            null, dimension, null, 0, false
        )
    )
    return insertFilesButton
}