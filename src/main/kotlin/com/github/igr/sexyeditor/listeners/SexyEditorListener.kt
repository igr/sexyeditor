package com.github.igr.sexyeditor.listeners

import com.github.igr.sexyeditor.config.SexyEditorState
import com.github.igr.sexyeditor.services.SexyEditorService
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import java.awt.Component
import javax.swing.JViewport
import javax.swing.border.Border

internal class SexyEditorListener: EditorFactoryListener {

    /**
     * Editor is created, finds appropriate background border and apply it to the editor.
     */
    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor
        val editorFile = FileDocumentManager.getInstance().getFile(editor.document) ?: return
        val editorComponent = editor.contentComponent
        val border: Border? = createBorder(editorFile.name, editorComponent)
        if (border != null) {
            editorComponent.border = border
            val jvp = editorComponent.parent as JViewport
            jvp.scrollMode = JViewport.SIMPLE_SCROLL_MODE
        }
    }

    /**
     * Editor is closed, removes border for editor component.
     */
    override fun editorReleased(event: EditorFactoryEvent) {
        val editor = event.editor
        val editorComponent = editor.contentComponent
        val border = editorComponent.border
        if (border is BackgroundBorder) {
            val bb: BackgroundBorder = border
            removeBorder(bb)
            bb.close()
            editorComponent.border = null
        }
    }

    // ---------------------------------------------------------------- border

    /**
     * Returns new border instance for given editor file name.
     * Each border is registered into the configuration.
     * Image is loaded during border creation.
     */
    private fun createBorder(fileName: String, component: Component): BackgroundBorder? {
        val state = SexyEditorState.get()
        val service = SexyEditorService.get()

        for (config in state.configs) {
            if (service.matchFileName(config, fileName)) {
                val backgroundBorder = BackgroundBorder(config, component)
                service.registerBorder(backgroundBorder)
                return backgroundBorder
            }
        }
        return null
    }

    /**
     * Unregisters border.
     */
    private fun removeBorder(border: BackgroundBorder) {
        val service = SexyEditorService.get()
        service.unregisterBorder(border)
    }

}