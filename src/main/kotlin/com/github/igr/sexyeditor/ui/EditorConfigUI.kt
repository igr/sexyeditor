package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.PluginBundle
import com.github.igr.sexyeditor.config.BackgroundPosition
import com.github.igr.sexyeditor.config.FitType
import com.github.igr.sexyeditor.ui.editor.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.ui.components.JBList
import com.intellij.uiDesigner.core.GridLayoutManager
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
    private val fileListLabel: JLabel
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
        nameTextField = uiCreateNameTextField(panel, dimensions.label, dimensions.textField)
        matchTextField = uiCreateMatchTextField(panel, dimensions.label, dimensions.textField)

        // row 1
        positionComboBox = uiCreatePositionComboBox(panel, dimensions.label, dimensions.combobox)
        // row 2
        positionOffsetTextField = uiCreatePositionOffsetTextField(panel, dimensions.smallTextField)

        // row 4
        fixedPositionCheckBox = uiCreateFixedPositionCheckbox(panel, dimensions.label)
        // row 5
        fitTypeComboBox = uiCreateFitTypeComboBox(panel, dimensions.combobox)

        // row 6
        opacitySlider = uiCreateOpacitySlider(panel, dimensions.label)

        // row 7
        resizeSlider = uiCreateResizeSlider(panel)
        resizeCheckBox = uiCreateResizeCheckBox(panel, dimensions.checkbox, resizeSlider)

        // row 8
        randomCheckBox = uiCreateRandomCheckBox(panel, dimensions.checkbox)

        // row 9
        slideShowPause = uiCreateSlideShowPause(panel, dimensions.smallTextField)
        slideshowCheckBox = uiCreateSlideShowCheckBox(panel, dimensions.checkbox, slideShowPause)
        uiCreateSpacer1(panel)

        // row 10
        fileListLabel = uiCreateFileListLabel(panel, dimensions.label)
        fileList = createFileList()

        // row 11
        insertFilesButton = createInsertFilesButton()
        removeFilesButton = createRemoveFileButton()
        uiCreateSpacer3(panel)

        // setup

        fileListModel = fileList.model as DefaultListModel<ImageFile>

        positionComboBoxModel = positionComboBox.model as DefaultComboBoxModel<BackgroundPosition>
        BackgroundPosition.entries.forEach { positionComboBoxModel.addElement(it) }

        fitTypeComboBoxModel = fitTypeComboBox.model as DefaultComboBoxModel<FitType>
        FitType.entries.forEach { fitTypeComboBoxModel.addElement(it) }
    }

    private fun createInsertFilesButton(): JButton {
        return uiCreateInsertFilesButton(panel, dimensions.button) {
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
                addToFileListModelInBackground(chooser.selectedFiles.asList())
            }
        }
    }

    private fun createFileList(): JBList<ImageFile> {
        val fileList = uiCreateFileList(panel)

        try {
            fileList.dropTarget = object : DropTarget() {
                @Synchronized
                override fun drop(evt: DropTargetDropEvent) {
                    try {
                        evt.acceptDrop(DnDConstants.ACTION_COPY)
                        val droppedFiles = evt.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                        addToFileListModelInBackground(droppedFiles)
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

    private fun createRemoveFileButton(): JButton {
        return uiCreateRemoveFileButton(panel, dimensions.button) {
            val min = fileList.minSelectionIndex
            if (min == -1) {
                return@uiCreateRemoveFileButton
            }
            val max = fileList.maxSelectionIndex
            if (max == -1) {
                return@uiCreateRemoveFileButton
            }
            fileListModel.removeRange(min, max)
            uiSetFileListLabelText(fileListLabel, fileListModel.size)
        }
    }

    internal fun addToFileListModel(files: List<File>) {
        fileList.setPaintBusy(true)

        LOG.info("Adding files to list in the background")
        for (file in files) {
            if (!file.exists()) {
                continue
            }
            if (!file.isFile) {
                continue
            }
            fileListModel.addElement(
                ImageFile(
                    file.absolutePath
                )
            )
        }
        uiSetFileListLabelText(fileListLabel, fileListModel.size)
        fileList.setPaintBusy(false)
    }

    private fun addToFileListModelInBackground(files: List<File>) {
        ApplicationManager.getApplication().executeOnPooledThread {
            addToFileListModel(files)
        }
    }

}
