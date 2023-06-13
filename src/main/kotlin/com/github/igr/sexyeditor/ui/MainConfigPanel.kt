package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.config.BackgroundConfiguration

class MainConfigPanel : MainConfigUI() {

    /**
     * Loads configuration list into the GUI and selects the very first element.
     */
    fun load(configs: List<BackgroundConfiguration>) {
        editorsListModel.removeAllElements()
        for (cfg in configs) {
            editorsListModel.addElement(cfg)
        }
        if (editorsListModel.size >= 1) {
            editorsList.selectedIndex = 0 // loads the very first border config
        }
        editorsList.repaint()
    }

    /**
     * Saves current changes and creates new configuration list instance.
     */
    fun save(): MutableList<BackgroundConfiguration> {
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
        return newConfigs
    }

    private fun saveBackgroundConfig(index: Int) {
        val newConfiguration: BackgroundConfiguration = editorConfigPanel.save()
        editorsListModel.remove(index)
        editorsListModel.add(index, newConfiguration)
    }

    /**
     * Returns `false` if all list elements are the same as in given config list
     * and selected border config is modified.
     */
    fun isModified(configs: List<BackgroundConfiguration>): Boolean {
        if (editorConfigPanel.isModified()) {
            return true
        }
        if (configs.size != editorsListModel.size) {
            return true
        }
        for (i in 0 until editorsListModel.size) {
            if (configs[i] != editorsListModel.getElementAt(i)) {
                return true
            }
        }
        return false
    }

}