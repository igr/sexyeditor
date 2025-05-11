package com.github.igr.sexyeditor.ui.editor

import com.github.igr.sexyeditor.PluginBundle
import com.github.igr.sexyeditor.ui.ImageFile
import com.github.igr.sexyeditor.ui.ImageListCellRenderer
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import java.awt.Dimension
import javax.swing.DefaultListModel
import javax.swing.JPanel
import javax.swing.ListSelectionModel

fun uiCreateFileList(panel: JPanel): JBList<ImageFile> {
    val scrollPane = JBScrollPane()
    panel.add(
        scrollPane,
        GridConstraints(
            9, 1, 1, 4,
            ANCHOR_CENTER, FILL_BOTH,
            SIZEPOLICY_FIXED,
            SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_WANT_GROW,
            Dimension(-1, 160), null, null, 0, false
        )
    )

    val fileList = JBList<ImageFile>().apply {
        model = DefaultListModel()
        selectionMode = ListSelectionModel.SINGLE_INTERVAL_SELECTION
        toolTipText = PluginBundle.message("tooltip.filelist")
        putClientProperty("List.isFileList", java.lang.Boolean.TRUE)
        cellRenderer = ImageListCellRenderer()
        emptyText.text = PluginBundle.message("label.file-list.empty")
    }
    scrollPane.setViewportView(fileList)

    return fileList
}