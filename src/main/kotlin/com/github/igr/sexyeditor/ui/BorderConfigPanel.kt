package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.config.BackgroundConfiguration
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.uiDesigner.core.Spacer
import com.intellij.util.ui.JBUI
import java.awt.Color
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.TitledBorder

class BorderConfigPanel {
    private val panel: JPanel = JPanel()
    private val nameTextField: JTextField
    private val positionComboBox: JComboBox<Int>
    private val opacitySlider: JSlider
    private val shrinkCheckBox: JCheckBox
    private val shrinkSlider: JSlider
    private val randomCheckBox: JCheckBox
    private var slideshowCheckBox: JCheckBox? = null
    private var slideShowPause: JTextField? = null
    private var insertFilesButton: JButton? = null
    private var matchTextField: JTextField? = null
    private var positionOffsetTextField: JTextField? = null
    private var imageLabel: JLabel? = null
    private var removeFilesButton: JButton? = null
    private var fixedPositionCheckBox: JCheckBox? = null
    private var shrinkToFitCheckBox: JCheckBox? = null
    private val fileList: JList<String>
    private var fileListModel: DefaultListModel<String>? = null
    private val positionComboBoxModel: DefaultComboBoxModel<Int>


    val rootComponent: JComponent
        get() = panel

    init {
        panel.layout = GridLayoutManager(11, 7, JBUI.insets(10), -1, -1)
        panel.border = BorderFactory.createTitledBorder(
            null,
            "Editor group configuration",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            null, null
        )

        nameTextField = createNameTextField()

        val label3 = JLabel()
        label3.text = "Position:"
        panel.add(
            label3,
            GridConstraints(
                2, 0, 1, 1,
                ANCHOR_WEST,
                FILL_NONE,
                SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED,
                null,
                Dimension(61, 14),
                null,
                0,
                false
            )
        )
        positionComboBox = JComboBox<Int>()
        positionComboBox.isEditable = false
        positionComboBox.maximumRowCount = 9
        positionComboBox.toolTipText = "<html>\nImage position relative to editor window."
        panel.add(
            positionComboBox,
            GridConstraints(
                2, 1, 1, 2,
                ANCHOR_WEST,
                FILL_HORIZONTAL,
                SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null,
                Dimension(117, 22),
                null,
                0,
                false
            )
        )

        opacitySlider = createOpacitySlider()

        shrinkCheckBox = JCheckBox()
        shrinkCheckBox.text = "Resize"
        shrinkCheckBox.toolTipText = "<html>\nShrink large images to fit the editor."
        panel.add(
            shrinkCheckBox,
            GridConstraints(
                6, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null, Dimension(61, 22), null, 0, false
            )
        )
        shrinkSlider = JSlider().apply {
            isEnabled = false
            majorTickSpacing = 10
            minimum = 0
            minorTickSpacing = 5
            paintLabels = true
            paintTicks = true
            toolTipText =  "<html>\nShrink percentage amount relative to OS desktop size.<br>\n100% means image will be shrinked to <b>desktop</b> (and not IDEA) size."
        }

        panel.add(
            shrinkSlider,
            GridConstraints(
                6, 1, 1, 4,
                ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )
        randomCheckBox = JCheckBox().apply {
            text = "Random"
            toolTipText = "<html>\nIf set, next image from the list will be chosen randomly.<br>\nAffects file openings and slideshows."
        }
        panel.add(
            randomCheckBox,
            GridConstraints(
                7, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
                null, Dimension(61, 22), null, 0, false
            )
        )
        slideshowCheckBox = JCheckBox()
        slideshowCheckBox!!.text = "Slideshow:"
        slideshowCheckBox!!.toolTipText = "<html>\nIf set images in editor will change while you work:)"
        panel.add(
            slideshowCheckBox,
            GridConstraints(
                8,
                0,
                1,
                1,
                ANCHOR_WEST,
                FILL_NONE,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null,
                Dimension(61, 22),
                null,
                0,
                false
            )
        )
        slideShowPause = JTextField()
        slideShowPause!!.columns = 10
        slideShowPause!!.isEnabled = false
        slideShowPause!!.toolTipText =
            "<html>\nTime between changing the image<br>\nin slideshow mode (in milliseconds)."
        panel.add(
            slideShowPause,
            GridConstraints(
                8,
                1,
                1,
                1,
                ANCHOR_WEST,
                FILL_HORIZONTAL,
                SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null,
                Dimension(70, 20),
                null,
                0,
                false
            )
        )
        val label4 = JLabel()
        label4.text = "File list:"
        panel.add(
            label4,
            GridConstraints(
                9,
                0,
                1,
                1,
                ANCHOR_NORTHWEST,
                FILL_NONE,
                SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED,
                null,
                Dimension(61, 14),
                null,
                0,
                false
            )
        )
        val spacer1 = Spacer()
        panel.add(
            spacer1,
            GridConstraints(
                8,
                2,
                1,
                2,
                ANCHOR_CENTER,
                FILL_HORIZONTAL,
                SIZEPOLICY_WANT_GROW,
                1,
                null,
                null,
                null,
                0,
                false
            )
        )
        insertFilesButton = JButton()
        insertFilesButton!!.icon = iconOf("images")
        insertFilesButton!!.text = "Add image(s)..."
        panel.add(
            insertFilesButton,
            GridConstraints(
                10,
                1,
                1,
                1,
                ANCHOR_CENTER,
                FILL_HORIZONTAL,
                SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED,
                null,
                Dimension(70, 25),
                null,
                0,
                false
            )
        )
        val label5 = JLabel()
        label5.text = "Match:"
        panel.add(
            label5,
            GridConstraints(
                1,
                0,
                1,
                1,
                ANCHOR_WEST,
                FILL_NONE,
                SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED,
                null,
                Dimension(61, 14),
                null,
                0,
                false
            )
        )
        matchTextField = JTextField()
        matchTextField!!.toolTipText =
            "<html>\nList of <b>wildcard</b> expressions separated by semicolon (<b>;</b>) for matching editor file names.<br>\nFile belongs to the first group that it matches.<br>\n<i>Example</i>: *.java;*.jsp"
        panel.add(
            matchTextField,
            GridConstraints(
                1,
                1,
                1,
                4,
                ANCHOR_WEST,
                FILL_HORIZONTAL,
                SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null,
                Dimension(120, 20),
                null,
                0,
                false
            )
        )
        val scrollPane1 = JScrollPane()
        panel.add(
            scrollPane1,
            GridConstraints(
                9,
                1,
                1,
                6,
                ANCHOR_CENTER,
                FILL_BOTH,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_WANT_GROW,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_WANT_GROW,
                null,
                null,
                null,
                0,
                false
            )
        )

        fileList = JList<String>()
        val defaultListModel1: DefaultListModel<String> = DefaultListModel<String>()
        fileList.model = defaultListModel1
        fileList.selectionMode = 1
        fileList.toolTipText = "File list"
        fileList.putClientProperty("List.isFileList", java.lang.Boolean.TRUE)
        scrollPane1.setViewportView(fileList)
        val label6 = JLabel()
        label6.text = "Offset:"
        panel.add(
            label6,
            GridConstraints(
                2,
                3,
                1,
                1,
                ANCHOR_EAST,
                FILL_NONE,
                SIZEPOLICY_WANT_GROW,
                SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false
            )
        )
        positionOffsetTextField = JTextField()
        positionOffsetTextField!!.columns = 4
        positionOffsetTextField!!.toolTipText = "<html>\nImage offset from nearest editor edges (in pixels)."
        panel.add(
            positionOffsetTextField,
            GridConstraints(
                2,
                4,
                1,
                1,
                ANCHOR_WEST,
                FILL_HORIZONTAL,
                SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null,
                Dimension(78, 20),
                null,
                0,
                false
            )
        )
        imageLabel = JLabel()
        imageLabel!!.background = Color(-10066330)
        imageLabel!!.foreground = Color(-6710887)
        imageLabel!!.horizontalAlignment = 10
        imageLabel!!.iconTextGap = 8
        imageLabel!!.text =
            "<html>\nHINTS:<br>\n+ Read <b>tooltips</b> for more help.<br>\n+ Reopen editors if changes are not visible.<br>\n+ Do not forget to apply changes before<br>\nchanging the editor group. "
        imageLabel!!.verticalAlignment = 0
        imageLabel!!.verticalTextPosition = 0
        panel.add(
            imageLabel,
            GridConstraints(
                0,
                5,
                3,
                1,
                ANCHOR_NORTHWEST,
                FILL_NONE,
                SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED,
                null,
                null,
                null,
                1,
                false
            )
        )
        val spacer2 = Spacer()
        panel.add(
            spacer2,
            GridConstraints(
                1,
                6,
                1,
                1,
                ANCHOR_CENTER,
                FILL_HORIZONTAL,
                SIZEPOLICY_WANT_GROW,
                1,
                null,
                null,
                null,
                0,
                false
            )
        )
        removeFilesButton = JButton()
        removeFilesButton!!.text = "Remove"
        panel.add(
            removeFilesButton,
            GridConstraints(
                10,
                3,
                1,
                1,
                ANCHOR_CENTER,
                FILL_HORIZONTAL,
                SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false
            )
        )
        val label7 = JLabel()
        label7.text = "Fixed Position:"
        panel.add(
            label7,
            GridConstraints(
                3,
                0,
                1,
                1,
                ANCHOR_WEST,
                FILL_NONE,
                SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED,
                null,
                Dimension(61, 14),
                null,
                0,
                false
            )
        )
        fixedPositionCheckBox = JCheckBox()
        fixedPositionCheckBox!!.isSelected = true
        fixedPositionCheckBox!!.text = ""
        panel.add(
            fixedPositionCheckBox,
            GridConstraints(
                3,
                1,
                1,
                1,
                ANCHOR_WEST,
                FILL_NONE,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false
            )
        )
        val label8 = JLabel()
        label8.text = "Shrink To Fit:"
        panel.add(
            label8,
            GridConstraints(
                4,
                0,
                1,
                1,
                ANCHOR_WEST,
                FILL_NONE,
                SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED,
                null,
                Dimension(61, 14),
                null,
                0,
                false
            )
        )
        shrinkToFitCheckBox = JCheckBox()
        shrinkToFitCheckBox!!.text = ""
        panel.add(
            shrinkToFitCheckBox,
            GridConstraints(
                4,
                1,
                1,
                1,
                ANCHOR_WEST,
                FILL_NONE,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false
            )
        )
        label3.labelFor = positionComboBox
        label5.labelFor = matchTextField
        label6.labelFor = positionOffsetTextField
        label7.labelFor = positionComboBox
        label8.labelFor = positionComboBox





        fileListModel = fileList.model as DefaultListModel<String>
        positionComboBoxModel = positionComboBox.model as DefaultComboBoxModel<Int>
        positionComboBox.setRenderer(PositionComboBoxRenderer())
        for (i in 0 until PositionComboBoxRenderer.POSITIONS.size) {
            positionComboBoxModel.addElement(Integer.valueOf(i))
        }
        //imageLabel!!.icon = SexyEditor.getInstance().getIcon()
    }

    private fun createNameTextField(): JTextField {
        val nameTextField = JTextField().apply {
            text = ""
            toolTipText = "User-friendly editor group name."
        }
        panel.add(
            JLabel().apply {
                text = "Name:"
                labelFor = nameTextField
            },
            GridConstraints(
                0, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, Dimension(60, 14), null, 0, false
            )
        )
        panel.add(
            nameTextField,
            GridConstraints(
                0, 1, 1, 4,
                ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
                null, Dimension(150, -1), null, 0, false
            )
        )
        return nameTextField
    }

    private fun createOpacitySlider(): JSlider {
        val opacitySlider = JSlider().apply {
            majorTickSpacing = 10
            minorTickSpacing = 5
            paintLabels = true
            paintTicks = true
            toolTipText = "<html>\nImage opacity percentage value."
        }
        panel.add(
            JLabel().apply {
                text = "Opacity:"
                labelFor = opacitySlider
            },
            GridConstraints(
                5, 0, 1, 1,
                ANCHOR_WEST,
                FILL_NONE,
                SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED,
                null,
                Dimension(61, 14),
                null,
                0,
                false
            )
        )
        panel.add(
            opacitySlider,
            GridConstraints(
                5, 1, 1, 4,
                ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )
        return opacitySlider
    }


    // ---------------------------------------------------------------- LSRM

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
     * Resets configuration to default.
     */
    fun reset() {
        load(BackgroundConfiguration())
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