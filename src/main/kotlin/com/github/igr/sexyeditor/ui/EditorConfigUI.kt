package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.PluginBundle
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.*
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.uiDesigner.core.Spacer
import com.intellij.util.ui.JBUI
import java.awt.Dimension
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.io.File
import javax.swing.*
import javax.swing.border.TitledBorder


open class EditorConfigUI {
    private val panel: JPanel = JPanel()
    protected val nameTextField: JTextField
    protected val positionComboBox: JComboBox<Int>
    protected val opacitySlider: JSlider
    protected val resizeCheckBox: JCheckBox
    protected val resizeSlider: JSlider
    protected val randomCheckBox: JCheckBox
    protected val slideshowCheckBox: JCheckBox
    protected val slideShowPause: JTextField
    protected val matchTextField: JTextField
    protected val positionOffsetTextField: JTextField
    private val insertFilesButton: JButton
    private val removeFilesButton: JButton
    protected val fixedPositionCheckBox: JCheckBox
    protected val shrinkToFitCheckBox: JCheckBox
    private val fileList: JBList<String>
    protected val fileListModel: DefaultListModel<String>
    private val positionComboBoxModel: DefaultComboBoxModel<Int>

    val rootComponent: JComponent
        get() = panel

    init {
        panel.layout = GridLayoutManager(11, 5, JBUI.insets(10), -1, -1)
        panel.border = BorderFactory.createTitledBorder(
            null,
            PluginBundle.message("label.editor-group"),
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            null, null
        )

        // row 0
        nameTextField = createNameTextField()
        matchTextField = createMatchTextField()

        // row 1
        positionComboBox = createPositionComboBox()
        // row 2
        positionOffsetTextField = createPositionOffsetTextField()

        // row 4
        fixedPositionCheckBox = createFixedPositionCheckbox()
        // row 5
        shrinkToFitCheckBox = createShrinkToFitCheckBox()
        // row 6
        opacitySlider = createOpacitySlider()

        // row 7
        resizeSlider = createResizeSlider()
        resizeCheckBox = createResizeCheckBox(resizeSlider)

        // row 8
        randomCheckBox = createRandomCheckBox()

        // row 9
        slideShowPause = createSlideShowPause()
        slideshowCheckBox = createSlideShowCheckBox(slideShowPause)
        createSpacer1()

        // row 10
        createFileListLabel()
        fileList = createFileList()

        // row 11
        insertFilesButton = createInsertFilesButton()
        removeFilesButton = createRemoveFileButton()
        createSpacer3()

        // setup

        fileListModel = fileList.model as DefaultListModel<String>
        positionComboBoxModel = positionComboBox.model as DefaultComboBoxModel<Int>
        positionComboBox.renderer = PositionComboBoxRenderer()
        for (i in 0 until PositionComboBoxRenderer.POSITIONS.size) {
            positionComboBoxModel.addElement(Integer.valueOf(i))
        }
    }

    private fun createSpacer1() {
        panel.add(
            Spacer(),
            GridConstraints(
                8, 2, 1, 3,
                ANCHOR_CENTER, FILL_HORIZONTAL, SIZEPOLICY_WANT_GROW, SIZEPOLICY_CAN_SHRINK,
                null, null, null, 0, false
            )
        )
    }

    private fun createNameTextField(): JTextField {
        val nameTextField = JTextField().apply {
            text = ""
        }
        panel.add(
            JLabel().apply {
                text = PluginBundle.message("label.name")
                labelFor = nameTextField
                toolTipText = PluginBundle.message("tooltip.name")
            },
            GridConstraints(
                0, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, Dimension(60, -1), null, 0, false
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

    private fun createPositionComboBox(): JComboBox<Int> {
        val positionComboBox = ComboBox<Int>().apply {
            isEditable = false
            maximumRowCount = 9
        }
        panel.add(
            JLabel().apply {
                text = PluginBundle.message("label.position")
                labelFor = positionComboBox
                toolTipText = PluginBundle.message("tooltip.position")
            },
            GridConstraints(
                2, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, Dimension(60, 14), null, 0, false
            )
        )
        panel.add(
            positionComboBox,
            GridConstraints(
                2, 1, 1, 2,
                ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
                null, Dimension(120, 22), null, 0, false
            )
        )
        return positionComboBox
    }

    private fun createOpacitySlider(): JSlider {
        val opacitySlider = JSlider().apply {
            majorTickSpacing = 10
            minorTickSpacing = 5
            paintLabels = true
            paintTicks = true
        }
        panel.add(
            JLabel().apply {
                text = PluginBundle.message("label.opacity")
                labelFor = opacitySlider
                toolTipText = PluginBundle.message("tooltip.opacity")
            },
            GridConstraints(
                5, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, Dimension(60, 14), null, 0, false
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

    private fun createResizeCheckBox(shrinkSlider: JSlider): JCheckBox {
        val resizeCheckBox = JCheckBox().apply {
            text = PluginBundle.message("label.resize-on-load")
            toolTipText = PluginBundle.message("tooltip.resize")
            addActionListener {  shrinkSlider.isEnabled = isSelected }
        }

        panel.add(
            resizeCheckBox,
            GridConstraints(
                6, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null, Dimension(60, 22), null, 0, false
            )
        )
        return resizeCheckBox
    }

    private fun createResizeSlider(): JSlider {
        val resizeSlider = JSlider().apply {
            isEnabled = false
            majorTickSpacing = 10
            minimum = 0
            minorTickSpacing = 5
            paintLabels = true
            paintTicks = true
            toolTipText =  PluginBundle.message("tooltip.resize-slider")
        }
        panel.add(
            resizeSlider,
            GridConstraints(
                6, 1, 1, 4,
                ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )
        return resizeSlider
    }

    private fun createRandomCheckBox(): JCheckBox {
        val randomCheckBox = JCheckBox().apply {
            text = PluginBundle.message("label.random")
            toolTipText = PluginBundle.message("tooltip.random")
        }
        panel.add(
            randomCheckBox,
            GridConstraints(
                7, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
                null, Dimension(60, 22), null, 0, false
            )
        )
        return randomCheckBox
    }

    private fun createSlideShowCheckBox(slideShowPause: JTextField): JCheckBox {
        val slideshowCheckBox = JCheckBox().apply {
            text = PluginBundle.message("label.slideshow")
            toolTipText = PluginBundle.message("tooltip.slideshow")
            addActionListener { slideShowPause.isEnabled = isSelected }
        }
        panel.add(
            slideshowCheckBox,
            GridConstraints(
                8, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null, Dimension(60, 22), null, 0, false
            )
        )
        return slideshowCheckBox
    }

    private fun createSlideShowPause(): JTextField {
        val slideShowPause = JTextField().apply {
            columns = 10
            isEnabled = false
            toolTipText = PluginBundle.message("tooltip.slideshow-pause")
        }
        panel.add(
            slideShowPause,
            GridConstraints(
                8, 1, 1, 1,
                ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
                null, Dimension(70, 20), null, 0, false
            )
        )
        return slideShowPause
    }

    private fun createInsertFilesButton(): JButton {
        val insertFilesButton = JButton().apply {
            icon = PluginIcons.IMAGES
            text = PluginBundle.message("label.add-images")
            addActionListener {
                val chooser = JFileChooser().apply {
                    isMultiSelectionEnabled = true
                    dialogTitle = "Select images to insert..."
                    preferredSize = Dimension(500, 500)
                }
                ImagePreviewPanel().apply {
                    attachToFileChooser(chooser, "Only images")
                }
                val returnVal = chooser.showOpenDialog(panel)
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    for (file in chooser.selectedFiles) {
                        fileListModel.addElement(file.absolutePath)
                    }
                }
            }
        }
        panel.add(
            insertFilesButton,
            GridConstraints(
                10, 1, 1, 1,
                ANCHOR_CENTER, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, Dimension(70, 25), null, 0, false
            )
        )
        return insertFilesButton
    }

    private fun createMatchTextField(): JTextField {
        val matchTextField = JTextField()
        panel.add(
            JLabel().apply {
                text = PluginBundle.message("label.match")
                labelFor = matchTextField
                toolTipText = PluginBundle.message("tooltip.match")
            },
            GridConstraints(
                1, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, Dimension(61, 14), null, 0, false
            )
        )
        panel.add(
            matchTextField,
            GridConstraints(
                1, 1, 1, 4,
                ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
                null, Dimension(120, 20), null, 0, false
            )
        )
        return matchTextField
    }

    private fun createFileList(): JBList<String> {
        val scrollPane = JBScrollPane()
        panel.add(
            scrollPane,
            GridConstraints(
                9, 1, 1, 4,
                ANCHOR_CENTER, FILL_BOTH,
                SIZEPOLICY_FIXED,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_WANT_GROW,
                Dimension(-1, 120), null, null, 0, false
            )
        )

        val fileList = JBList<String>().apply {
            model = DefaultListModel()
            selectionMode = 1
            toolTipText = PluginBundle.message("tooltip.filelist")
            putClientProperty("List.isFileList", java.lang.Boolean.TRUE)
        }
        scrollPane.setViewportView(fileList)

        fileList.dropTarget = object : DropTarget() {
            @Synchronized
            override fun drop(evt: DropTargetDropEvent) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY)
                    val droppedFiles = evt.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                    for (file in droppedFiles) {
                        fileListModel.addElement(file.absolutePath)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        return fileList
    }

    private fun createPositionOffsetTextField(): JTextField {
        val positionOffsetTextField = JTextField().apply {
            columns = 4
        }
        panel.add(
            JLabel().apply {
                text = PluginBundle.message("label.offset")
                labelFor = positionOffsetTextField
                toolTipText = PluginBundle.message("tooltip.position-offset")
            },
            GridConstraints(
                2, 3, 1, 1,
                ANCHOR_EAST, FILL_NONE, SIZEPOLICY_WANT_GROW, SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )
        panel.add(
            positionOffsetTextField,
            GridConstraints(
                2, 4, 1, 1,
                ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED,
                null, Dimension(78, 20), null, 0, false
            )
        )
        return positionOffsetTextField
    }

    private fun createFileListLabel() {
        panel.add(
            JLabel().apply {
                text = PluginBundle.message("label.file-list")
            },
            GridConstraints(
                9, 0, 1, 1,
                ANCHOR_NORTHWEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, Dimension(60, 14), null, 0, false
            )
        )
    }

    private fun createRemoveFileButton(): JButton {
        val removeFilesButton = JButton().apply {
            text = PluginBundle.message("button.remove")
            addActionListener {
                val min = fileList.minSelectionIndex
                if (min == -1) {
                    return@addActionListener
                }
                val max = fileList.maxSelectionIndex
                if (max == -1) {
                    return@addActionListener
                }
                fileListModel.removeRange(min, max)
            }
        }
        panel.add(
            removeFilesButton,
            GridConstraints(
                10, 2, 1, 1,
                ANCHOR_CENTER, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, Dimension(70, 25), null, 0, false
            )
        )
        return removeFilesButton
    }

    private fun createSpacer3() {
        panel.add(
            Spacer(),
            GridConstraints(
                10, 3, 1, 1,
                ANCHOR_CENTER, FILL_HORIZONTAL, SIZEPOLICY_WANT_GROW, 1,
                null, null, null, 0, false
            )
        )
    }

    private fun createFixedPositionCheckbox(): JCheckBox {
        val fixedPositionCheckBox = JCheckBox().apply {
            isSelected = true
            text = ""
        }
        panel.add(
            JLabel().apply {
                text = PluginBundle.message("label.fixed-position")
                labelFor = fixedPositionCheckBox
                toolTipText = PluginBundle.message("tooltip.fixed-position")
            },
            GridConstraints(
                3, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, Dimension(61, 14), null, 0, false
            )
        )
        panel.add(
            fixedPositionCheckBox,
            GridConstraints(
                3, 1, 1, 1,
                ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )
        return fixedPositionCheckBox
    }

    private fun createShrinkToFitCheckBox(): JCheckBox {
        val shrinkToFitCheckBox = JCheckBox().apply {
            text = ""
        }
        panel.add(
            JLabel().apply {
                text = PluginBundle.message("label.shrink-to-fit")
                labelFor = shrinkToFitCheckBox
            },
            GridConstraints(
                4, 0, 1, 1,
                ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, Dimension(61, 14), null, 0, false
            )
        )
        panel.add(
            shrinkToFitCheckBox,
            GridConstraints(
                4, 1, 1, 1,
                ANCHOR_WEST, FILL_NONE,
                SIZEPOLICY_CAN_SHRINK or SIZEPOLICY_CAN_GROW,
                SIZEPOLICY_FIXED,
                null, null, null, 0, false
            )
        )
        return shrinkToFitCheckBox
    }

}