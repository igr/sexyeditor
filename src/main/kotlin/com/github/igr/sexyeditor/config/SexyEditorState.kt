package com.github.igr.sexyeditor.config

import com.github.igr.sexyeditor.COMPONENT_NAME
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = COMPONENT_NAME,
    storages = [Storage("sexy-editor-3.xml")]
)
class SexyEditorState : PersistentStateComponent<SexyEditorState> {

    var configs: MutableList<BackgroundConfiguration> = mutableListOf()

    companion object {
        fun get(): SexyEditorState {
            return ApplicationManager.getApplication().getService(SexyEditorState::class.java)
        }
    }

    /**
     * Returns plugin state.
     */
    override fun getState(): SexyEditorState {
        return this
    }

    /**
     * Loads state from configuration file.
     */
    override fun loadState(state: SexyEditorState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}