package com.github.igr.sexyeditor.listeners

import com.github.igr.sexyeditor.config.SexyEditorState
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileDocumentManager

class SexyEditorListener(
    val state: SexyEditorState
) : EditorFactoryListener {

    /**
     * Editor is created, finds appropriate background border and apply it to the editor.
     */
    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor
        val editorFile = FileDocumentManager.getInstance().getFile(editor.document) ?: return
        val editorComponent = editor.contentComponent
//        val border: Border? = createBorder(editorFile.name, editorComponent)
//        if (border != null) {
//            editorComponent.border = border
//            val jvp = editorComponent.parent as JViewport
//            jvp.scrollMode = JViewport.SIMPLE_SCROLL_MODE
//        }
    }

    /**
     * Editor is closed, removes border for editor component.
     */
    override fun editorReleased(event: EditorFactoryEvent) {
        val editor = event.editor
        val editorComponent = editor.contentComponent
        val border = editorComponent.border
//        if (border is BackgroundBorder) {
//            val bb: BackgroundBorder = border as BackgroundBorder
//            removeBorder(bb)
//            bb.close()
//            editorComponent.border = null
//        }
    }

}