package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.PluginBundle
import com.github.igr.sexyeditor.config.BackgroundPosition
import com.github.igr.sexyeditor.config.FitType
import com.github.igr.sexyeditor.ui.editor.uiCreateFileListLabel
import com.github.igr.sexyeditor.ui.editor.uiCreateFitTypeComboBox
import com.github.igr.sexyeditor.ui.editor.uiCreateFixedPositionCheckbox
import com.github.igr.sexyeditor.ui.editor.uiCreateMatchTextField
import com.github.igr.sexyeditor.ui.editor.uiCreateNameTextField
import com.github.igr.sexyeditor.ui.editor.uiCreateOpacitySlider
import com.github.igr.sexyeditor.ui.editor.uiCreatePositionComboBox
import com.github.igr.sexyeditor.ui.editor.uiCreatePositionOffsetTextField
import com.github.igr.sexyeditor.ui.editor.uiCreateRandomCheckBox
import com.github.igr.sexyeditor.ui.editor.uiCreateResizeCheckBox
import com.github.igr.sexyeditor.ui.editor.uiCreateResizeSlider
import com.github.igr.sexyeditor.ui.editor.uiCreateSlideShowCheckBox
import com.github.igr.sexyeditor.ui.editor.uiCreateSlideShowPause
import com.github.igr.sexyeditor.ui.editor.uiCreateSpacer1
import com.github.igr.sexyeditor.ui.editor.uiCreateSpacer3
import com.intellij.openapi.diagnostic.logger
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

private val LOG = logger<EditorConfigUI>()

open class EditorConfigUI {
    private val panel: JPanel = JPanel()
    protected val nameTextField: JTextField
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
    private val fileList: JBList<ImageFile>
    protected val fileListModel: DefaultListModel<ImageFile>
    protected val positionComboBox: JComboBox<BackgroundPosition>
    private val positionComboBoxModel: DefaultComboBoxModel<BackgroundPosition>
    protected val fitTypeComboBox: JComboBox<FitType>
    private val fitTypeComboBoxModel: DefaultComboBoxModel<FitType>

    private val dimensions = object {
        val label = Dimension(60, -1)
        val textField = Dimension(150, -1)
        val smallTextField = Dimension(70, 20)
        val combobox = Dimension(120, 22)
        val checkbox = Dimension(60, 22)
        val button = Dimension(70, 25)
    }


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
        fitTypeComboBox = createFitTypeComboBox()

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

        fileListModel = fileList.model as DefaultListModel<ImageFile>

        positionComboBoxModel = positionComboBox.model as DefaultComboBoxModel<BackgroundPosition>
        BackgroundPosition.entries.forEach { positionComboBoxModel.addElement(it) }

        fitTypeComboBoxModel = fitTypeComboBox.model as DefaultComboBoxModel<FitType>
        FitType.entries.forEach { fitTypeComboBoxModel.addElement(it) }
    }

    private fun createSpacer1() {
        uiCreateSpacer1(panel)
    }

    private fun createNameTextField(): JTextField {
        return uiCreateNameTextField(panel, dimensions.label, dimensions.textField)
    }

    private fun createPositionComboBox(): JComboBox<BackgroundPosition> {
        return uiCreatePositionComboBox(panel,dimensions.label, dimensions.combobox)
    }

    private fun createOpacitySlider(): JSlider {
        return uiCreateOpacitySlider(panel, dimensions.label)
    }

    private fun createResizeCheckBox(shrinkSlider: JSlider): JCheckBox {
        return uiCreateResizeCheckBox(panel, dimensions.checkbox, shrinkSlider)
    }

    private fun createResizeSlider(): JSlider {
        return uiCreateResizeSlider(panel)
    }

    private fun createRandomCheckBox(): JCheckBox {
        return uiCreateRandomCheckBox(panel, dimensions.checkbox)
    }

    private fun createSlideShowCheckBox(slideShowPause: JTextField): JCheckBox {
        return uiCreateSlideShowCheckBox(panel, dimensions.checkbox, slideShowPause)
    }

    private fun createSlideShowPause(): JTextField {
        return uiCreateSlideShowPause(panel, dimensions.smallTextField)
    }

    private fun createInsertFilesButton(): JButton {
        val insertFilesButton = JButton().apply {
            icon = PluginIcons.IMAGES
            text = PluginBundle.message("label.add-images")
            addActionListener {
                val chooser = JFileChooser().apply {
                    isMultiSelectionEnabled = true
                    dialogTitle = "Select images"
                    preferredSize = Dimension(800, 500)
                }
                ImagePreviewPanel().apply {
                    attachToFileChooser(chooser, "Only images")
                }
                val returnVal = chooser.showOpenDialog(panel)
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    for (file in chooser.selectedFiles) {
                        fileListModel.addElement(
                            ImageFile(
                                file.absolutePath
                            )
                        )
                    }
                }
            }
        }
        panel.add(
            insertFilesButton,
            GridConstraints(
                10, 1, 1, 1,
                ANCHOR_CENTER, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                null, dimensions.button, null, 0, false
            )
        )
        return insertFilesButton
    }

    private fun createMatchTextField(): JTextField {
        return uiCreateMatchTextField(panel, dimensions.label, dimensions.textField)
    }

    private fun createFileList(): JBList<ImageFile> {
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
            selectionMode = 1
            toolTipText = PluginBundle.message("tooltip.filelist")
            putClientProperty("List.isFileList", java.lang.Boolean.TRUE)
            cellRenderer = ImageListCellRenderer()
        }
        scrollPane.setViewportView(fileList)

        try {
            fileList.dropTarget = object : DropTarget() {
                @Synchronized
                override fun drop(evt: DropTargetDropEvent) {
                    try {
                        evt.acceptDrop(DnDConstants.ACTION_COPY)
                        val droppedFiles = evt.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                        for (file in droppedFiles) {
                            fileListModel.addElement(
                                ImageFile(
                                    file.absolutePath
                                )
                            )
                        }
                    } catch (ex: Exception) {
                        LOG.warn("Drag and drop failed", ex)
                    }
                }
            }
        } catch (headless: java.awt.HeadlessException) {
            LOG.warn("Drag and drop not supported in Headless mode", headless)
        }
        return fileList
    }

    private fun createPositionOffsetTextField(): JTextField {
        return uiCreatePositionOffsetTextField(panel, dimensions.smallTextField)
    }

    private fun createFileListLabel() {
        uiCreateFileListLabel(panel, dimensions.label)
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
                null, dimensions.button, null, 0, false
            )
        )
        return removeFilesButton
    }

    private fun createSpacer3() {
        uiCreateSpacer3(panel)
    }

    private fun createFixedPositionCheckbox(): JCheckBox {
        return uiCreateFixedPositionCheckbox(panel, dimensions.label)
    }

    private fun createFitTypeComboBox(): ComboBox<FitType> {
        return uiCreateFitTypeComboBox(panel, dimensions.combobox)
    }

}
