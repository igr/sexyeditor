package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.config.BackgroundConfiguration
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.util.ui.JBUI
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.TitledBorder

class SexyEditorConfigurationPanel {
    private val panel: JPanel = JPanel()
    private val editorsList: JList<BackgroundConfiguration>
    private val addNewButton: JButton
    private val removeButton: JButton
    private val moveUpButton: JButton
    private val moveDownButton: JButton
    private val editorsListModel: DefaultListModel<BackgroundConfiguration>
    private val borderConfigPanel: BorderConfigPanel

    val rootComponent: JComponent
        get() = panel

    init {
        panel.apply {
            layout = GridLayoutManager(5, 2, JBUI.insets(0, 10), -1, -1)
            isEnabled = true
        }

        borderConfigPanel = BorderConfigPanel()
        panel.add(
            borderConfigPanel.rootComponent,
            GridConstraints(
                4, 0, 1, 2,
                ANCHOR_CENTER,
                FILL_BOTH,
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
                ANCHOR_CENTER,
                FILL_HORIZONTAL,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )

        removeButton = createRemoveButton()
        panel.add(
            removeButton,
            GridConstraints(
                1, 1, 1, 1,
                ANCHOR_CENTER,
                FILL_HORIZONTAL,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )

        moveUpButton = createMoveUpButton()
        panel.add(
            moveUpButton,
            GridConstraints(
                2, 1, 1, 1,
                ANCHOR_CENTER,
                FILL_HORIZONTAL,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )

        moveDownButton = createMoveDownButton()
        panel.add(
            moveDownButton,
            GridConstraints(
                3, 1, 1, 1,
                ANCHOR_CENTER,
                FILL_HORIZONTAL,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )

        val innerPanel = JPanel().apply {
            layout = GridLayoutManager(1, 1, JBUI.emptyInsets(), -1, -1)
            border = BorderFactory.createTitledBorder(
                null,
                "Editors",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                null, null
            )
        }
        panel.add(
            innerPanel,
            GridConstraints(
                0, 0, 4, 1,
                ANCHOR_CENTER,
                FILL_BOTH,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_CAN_GROW,
                null,
                Dimension(300, 160),
                null, 0, false
            )
        )
        val scrollPane = JScrollPane()
        innerPanel.add(
            scrollPane,
            GridConstraints(
                0, 0, 1, 1,
                ANCHOR_CENTER,
                FILL_BOTH,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_WANT_GROW,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_WANT_GROW,
                null,
                Dimension(256, 88),
                null, 0, false
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
                    borderConfigPanel.load(editorsListModel.getElementAt(selected) as BackgroundConfiguration)
                }
            }
        }
        scrollPane.setViewportView(editorsList)
    }


    private fun createAddNewButton() =
        JButton().apply {
            hideActionText = false
            horizontalAlignment = SwingConstants.LEFT
            icon = generalIconOf("add")
            text = "Add Editor"
            addActionListener {
                val selected = editorsList.selectedIndex
                if (selected != -1) {
                    editorsListModel.add(selected, BackgroundConfiguration())
                    editorsList.setSelectedIndex(selected)
                } else {
                    editorsListModel.add(editorsListModel.size(), BackgroundConfiguration())
                    editorsList.setSelectedIndex(editorsListModel.size() - 1)
                }
            }
        }

    private fun createRemoveButton() =
        JButton().apply {
            horizontalAlignment = SwingConstants.LEFT
            icon = generalIconOf("remove")
            text = "Remove"
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
            }
        }

    private fun createMoveUpButton() =
        JButton().apply {
            horizontalAlignment = SwingConstants.LEFT
            icon = actionIconOf("moveUp")
            text = "Move Up"

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
            horizontalAlignment = SwingConstants.LEFT
            icon = actionIconOf("moveDown")
            text = "Move Down"

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

    // ---------------------------------------------------------------- LSRM

    private var configs: List<BackgroundConfiguration>? = null

    /**
     * Loads configuration list into the GUI and selects the very first element.
     */
    fun load(configs: List<BackgroundConfiguration>) {
        this.configs = configs
        editorsListModel.removeAllElements()
        for (cfg in configs) {
            editorsListModel.addElement(cfg)
        }
        if (editorsListModel.size >= 1) {
            editorsList.selectedIndex = 0 // loads border config
        }
        editorsList.repaint()
    }

    /**
     * Saves current changes and creates new configuration list instance.
     */
    fun save(): List<BackgroundConfiguration>? {
        if (configs == null) {
            return null
        }

        // replace current border config
        val selected = editorsList.selectedIndex
        if (selected != -1) {
            saveBackgroundConfig(selected)
            editorsList.selectedIndex = selected
        }

        // creates new list
        val newConfigs: MutableList<BackgroundConfiguration> = ArrayList(editorsListModel.size)
        for (i in 0 until editorsListModel.size) {
            newConfigs.add(editorsListModel.getElementAt(i) as BackgroundConfiguration)
        }
        configs = newConfigs
        return newConfigs
    }

    private fun saveBackgroundConfig(index: Int) {
        val newConfiguration: BackgroundConfiguration = borderConfigPanel.save()
        editorsListModel.remove(index)
        editorsListModel.add(index, newConfiguration)
    }

    /**
     * Returns `false` if all list elements are the same as in given config list
     * and selected border config is modified.
     */
    fun isModified(): Boolean {
        if (configs == null) {
            return false
        }
        if (borderConfigPanel.isModified()) {
            return true
        }
        if (configs!!.size != editorsListModel.size) {
            return true
        }
        for (i in 0 until editorsListModel.size) {
            if (configs!![i] != editorsListModel.getElementAt(i)) {
                return true
            }
        }
        return false
    }

}