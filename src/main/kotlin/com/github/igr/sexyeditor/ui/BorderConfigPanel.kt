package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.config.BackgroundConfiguration

// todo rename to EditorConfigPanel
class BorderConfigPanel : EditorConfigUI() {

    private var config: BackgroundConfiguration = BackgroundConfiguration()

    /**
     * Loads configuration to GUI.
     */
    fun load(config: BackgroundConfiguration) {
        this.config = config

        nameTextField.text = config.name
        matchTextField!!.text = config.editorGroup
        opacitySlider.value = (config.opacity * 100).toInt()

        positionComboBox.selectedIndex = config.position
        positionOffsetTextField!!.text = config.positionOffset.toString()
        if (shrinkCheckBox.isSelected != config.shrink) {
            shrinkCheckBox.doClick()
        }
        shrinkSlider.value = config.shrinkValue
        if (fixedPositionCheckBox!!.isSelected != config.fixedPosition) {
            fixedPositionCheckBox!!.doClick()
        }
        if (shrinkToFitCheckBox!!.isSelected != config.shrinkToFit) {
            shrinkToFitCheckBox!!.doClick()
        }
        if (randomCheckBox.isSelected != config.random) {
            randomCheckBox.doClick()
        }
        if (slideshowCheckBox!!.isSelected != config.slideshow) {
            slideshowCheckBox!!.doClick()
        }
        slideShowPause!!.text = config.slideshowPause.toString()
        fileListModel!!.clear()
        if (config.fileNames != null) {
            for (fileName in config.fileNames) {
                fileListModel!!.addElement(fileName)
            }
        }
    }

    /**
     * Saves configuration from GUI.
     */
    fun save(): BackgroundConfiguration {
        config.name = nameTextField.text
        config.editorGroup = matchTextField!!.text
        config.opacity = ((opacitySlider.value / 100.0).toFloat())
        config.position = (positionComboBox.selectedIndex)
        config.positionOffset = (Integer.valueOf(positionOffsetTextField!!.text))
        config.shrink = (shrinkCheckBox.isSelected)
        config.shrinkValue = (shrinkSlider.value)
        config.random = (randomCheckBox.isSelected)
        config.fixedPosition = (fixedPositionCheckBox!!.isSelected)
        config.shrinkToFit = (shrinkToFitCheckBox!!.isSelected)
        config.slideshow = (slideshowCheckBox!!.isSelected)
        config.slideshowPause = (slideShowPause!!.text.toInt())
        val files = arrayOf<String>()
        fileListModel!!.copyInto(files)
        config.fileNames = files
        return config
    }

    /**
     * Returns `true` if configuration is modified.
     */
    fun isModified(): Boolean {
        return ((nameTextField.text != config.name
                || matchTextField!!.text != config.editorGroup || opacitySlider.value != (config.opacity * 100).toInt()) || positionComboBox.selectedIndex != config.position || positionOffsetTextField!!.text != java.lang.String.valueOf(
            config.positionOffset
        ) || shrinkCheckBox.isSelected != config.shrink || shrinkSlider.value != config.shrinkValue || randomCheckBox.isSelected != config.random || fixedPositionCheckBox!!.isSelected != config.fixedPosition || shrinkToFitCheckBox!!.isSelected != config.shrinkToFit || slideshowCheckBox!!.isSelected != config.slideshow || slideShowPause!!.text != java.lang.String.valueOf(
            config.slideshowPause
        )
                || !equalFileListModel(config.fileNames))
    }

    private fun equalFileListModel(otherList: Array<String>?): Boolean {
        val size = fileListModel!!.size
        if (otherList == null) {
            return size == 0
        }
        if (size != otherList.size) {
            return false
        }
        for (i in 0 until size) {
            if (fileListModel!![i] != otherList[i]) {
                return false
            }
        }
        return true
    }
}