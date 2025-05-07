package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.config.BackgroundConfiguration
import com.github.igr.sexyeditor.config.BackgroundPosition
import com.github.igr.sexyeditor.config.FitType
import java.io.File

class EditorConfigPanel : EditorConfigUI() {

    private var config: BackgroundConfiguration = BackgroundConfiguration()

    /**
     * Loads configuration to GUI.
     */
    fun load(config: BackgroundConfiguration) {
        this.config = config

        nameTextField.text = config.name
        matchTextField.text = config.editorGroup
        opacitySlider.value = (config.opacity * 100).toInt()

        positionComboBox.selectedIndex = config.position.value
        fitTypeComboBox.selectedIndex = config.fitType.value

        positionOffsetTextField.text = config.positionOffset.toString()
        if (resizeCheckBox.isSelected != config.resize) {
            resizeCheckBox.doClick()
        }
        resizeSlider.value = config.resizeValue
        if (fixedPositionCheckBox.isSelected != config.fixedPosition) {
            fixedPositionCheckBox.doClick()
        }

        if (randomCheckBox.isSelected != config.random) {
            randomCheckBox.doClick()
        }
        if (slideshowCheckBox.isSelected != config.slideshow) {
            slideshowCheckBox.doClick()
        }
        slideShowPause.text = config.slideshowPause.toString()
        fileListModel.clear()
        addToFileListModel(config.fileNames.map { File(it) }.toList())
    }

    /**
     * Saves configuration from GUI.
     */
    fun save(): BackgroundConfiguration {
        config.name = nameTextField.text
        config.editorGroup = matchTextField.text
        config.opacity = (opacitySlider.value / 100.0).toFloat()
        config.position = BackgroundPosition.of(positionComboBox.selectedIndex)
        config.fitType = FitType.of(fitTypeComboBox.selectedIndex)
        config.positionOffset = positionOffsetTextField.text.toInt()
        config.resize = resizeCheckBox.isSelected
        config.resizeValue = resizeSlider.value
        config.random = randomCheckBox.isSelected
        config.fixedPosition = fixedPositionCheckBox.isSelected
        config.slideshow = slideshowCheckBox.isSelected
        config.slideshowPause = slideShowPause.text.toInt()
        config.fileNames = fileListModel.elements().toList().map { it.path }.toTypedArray()
        return config
    }

    /**
     * Returns `true` if configuration is modified.
     */
    fun isModified(): Boolean {
        return ((nameTextField.text != config.name ||
                    matchTextField.text != config.editorGroup ||
                    opacitySlider.value != (config.opacity * 100).toInt()
                ) ||
                positionComboBox.selectedIndex != config.position.value ||
                fitTypeComboBox.selectedIndex != config.fitType.value ||
                positionOffsetTextField.text != config.positionOffset.toString() ||
                resizeCheckBox.isSelected != config.resize ||
                resizeSlider.value != config.resizeValue ||
                randomCheckBox.isSelected != config.random ||
                fixedPositionCheckBox.isSelected != config.fixedPosition ||
                slideshowCheckBox.isSelected != config.slideshow ||
                slideShowPause.text != config.slideshowPause.toString() ||
                !equalFileListModel(config.fileNames))
    }

    private fun equalFileListModel(otherList: Array<String>?): Boolean {
        val size = fileListModel.size
        if (otherList == null) {
            return size == 0
        }
        if (size != otherList.size) {
            return false
        }
        for (i in 0 until size) {
            if (fileListModel[i].path != otherList[i]) {
                return false
            }
        }
        return true
    }
}