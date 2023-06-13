package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.PluginBundle
import com.github.igr.sexyeditor.config.BackgroundConfiguration
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.util.ui.JBUI
import java.awt.event.MouseAdapter
import javax.swing.*
import javax.swing.border.TitledBorder


open class MainConfigUI {

    private val panel: JPanel = JPanel()
    protected val editorsList: JList<BackgroundConfiguration>
    private val addNewButton: JButton
    private val removeButton: JButton
    private val moveUpButton: JButton
    private val moveDownButton: JButton
    protected val editorsListModel: DefaultListModel<BackgroundConfiguration>
    protected val editorConfigPanel: EditorConfigPanel
    private val hintLabel: JLabel

    val rootComponent: JComponent
        get() = panel

    init {
        panel.apply {
            layout = GridLayoutManager(6, 2, JBUI.insets(0, 10), -1, -1)
            isEnabled = true
        }

        editorConfigPanel = EditorConfigPanel()
        panel.add(
            editorConfigPanel.rootComponent,
            GridConstraints(
                4, 0, 1, 2,
                ANCHOR_CENTER, FILL_BOTH,
                SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )
        hintLabel = JLabel().apply {
            horizontalAlignment = SwingConstants.CENTER
            text = randomTip()
            icon = PluginIcons.TIP
            verticalAlignment = SwingConstants.BOTTOM
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: java.awt.event.MouseEvent) {
                    text = randomTip()
                }
            })
        }
        panel.add(
            hintLabel,
            GridConstraints(
                5, 0, 1, 2,
                ANCHOR_CENTER, FILL_BOTH,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                null, null, null, 0, false
            )
        )

        addNewButton = createAddNewButton()
        panel.add(
            addNewButton,
            GridConstraints(
                0, 1, 1, 1,
                ANCHOR_CENTER, FILL_HORIZONTAL,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )

        removeButton = createRemoveButton()
        panel.add(
            removeButton,
            GridConstraints(
                1, 1, 1, 1,
                ANCHOR_CENTER, FILL_HORIZONTAL,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )

        moveUpButton = createMoveUpButton()
        panel.add(
            moveUpButton,
            GridConstraints(
                2, 1, 1, 1,
                ANCHOR_CENTER, FILL_HORIZONTAL,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )

        moveDownButton = createMoveDownButton()
        panel.add(
            moveDownButton,
            GridConstraints(
                3, 1, 1, 1,
                ANCHOR_CENTER, FILL_HORIZONTAL,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )

        val scrollPane = JScrollPane().apply {
            border = BorderFactory.createEmptyBorder()
        }
        val innerPanel = JPanel().apply {
            layout = GridLayoutManager(1, 1, JBUI.emptyInsets(), -1, -1)
            border = BorderFactory.createTitledBorder(
                null, PluginBundle.message("label.editors"),
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                null, null
            )
            add(
                scrollPane,
                GridConstraints(
                    0, 0, 1, 1,
                    ANCHOR_CENTER, FILL_BOTH,
                    SIZEPOLICY_CAN_SHRINK,
                    SIZEPOLICY_CAN_SHRINK,
                    null, null, null, 0, false
                )
            )
        }
        panel.add(
            innerPanel,
            GridConstraints(
                0, 0, 4, 1,
                ANCHOR_CENTER, FILL_BOTH,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_CAN_GROW,
                null, null, null, 0, false
            )
        )

        editorsListModel = DefaultListModel()
        editorsList = JList<BackgroundConfiguration>().apply {
            model = editorsListModel
            selectionMode = 0
            addListSelectionListener {
                // select a config
                val selected = selectedIndex
                if (selected != -1) {
                    editorConfigPanel.load(editorsListModel.getElementAt(selected) as BackgroundConfiguration)
                } else {
                    // when no config is selected, load a default one
                    editorConfigPanel.load(BackgroundConfiguration())
                }
            }
        }
        scrollPane.setViewportView(editorsList)

        showEditorConfigPanel()
    }

    private fun createAddNewButton() =
        JButton().apply {
            hideActionText = false
            horizontalAlignment = SwingConstants.CENTER
            text = PluginBundle.message("button.add-editor")
            addActionListener {
                if (editorsListModel.size() == 0) {
                    showEditorConfigPanel()
                }
                val selected = editorsList.selectedIndex
                if (selected != -1) {
                    editorsListModel.add(selected, BackgroundConfiguration())
                    editorsList.selectedIndex = selected
                } else {
                    editorsListModel.add(editorsListModel.size(), BackgroundConfiguration())
                    editorsList.setSelectedIndex(editorsListModel.size() - 1)
                }
            }
        }

    private fun createRemoveButton() =
        JButton().apply {
            horizontalAlignment = SwingConstants.CENTER
            text = PluginBundle.message("button.remove")
            addActionListener {
                var selected = editorsList.selectedIndex
                if (selected != -1) {
                    editorsListModel.remove(selected)
                    if (selected >= editorsListModel.size) {
                        selected--
                    }
                    if (selected >= 0) {
                        editorsList.selectedIndex = selected
                    }
                }
                if (editorsListModel.isEmpty) {
                    hideEditorConfigPanel()
                }
            }
        }

    private fun createMoveUpButton() =
        JButton().apply {
            horizontalAlignment = SwingConstants.CENTER
            text = PluginBundle.message("button.move-up")

            addActionListener {
                var selected = editorsList.selectedIndex
                if (selected <= 0) {
                    return@addActionListener
                }
                val removed = editorsListModel.remove(selected)
                selected--
                editorsListModel.add(selected, removed)
                editorsList.setSelectedIndex(selected)
            }
        }

    private fun createMoveDownButton() =
        JButton().apply {
            horizontalAlignment = SwingConstants.CENTER
            text = PluginBundle.message("button.move-down")

            addActionListener {
                var selected = editorsList.selectedIndex
                if (selected == -1 || selected == editorsListModel.size() - 1) {
                    return@addActionListener
                }
                val removed = editorsListModel.remove(selected)
                selected++
                editorsListModel.add(selected, removed)
                editorsList.setSelectedIndex(selected)
            }
        }


    private fun hideEditorConfigPanel() {
        if (!editorConfigPanel.rootComponent.isVisible) {
            return
        }
        editorConfigPanel.rootComponent.isVisible = false
        hintLabel.apply {
            text = PluginBundle.message("no-editors")
            icon = PluginIcons.LOGO
            verticalAlignment = SwingConstants.CENTER
        }
    }

    private fun showEditorConfigPanel() {
        if (editorConfigPanel.rootComponent.isVisible) {
            return
        }
        editorConfigPanel.rootComponent.isVisible = true
        hintLabel.apply {
            text = randomTip()
            icon = PluginIcons.TIP
            verticalAlignment = SwingConstants.BOTTOM
        }
    }

    private fun randomTip(): String = arrayOf(
        PluginBundle.message("tip.1"),
        PluginBundle.message("tip.2"),
        PluginBundle.message("tip.3"),
    ).random()

}