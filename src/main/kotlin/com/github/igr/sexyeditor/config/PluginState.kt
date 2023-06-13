package com.github.igr.sexyeditor.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "AwesomeEditor",
    storages = [Storage("awesome-editor-3.xml")]
)
class PluginState : PersistentStateComponent<PluginState> {

    var configs: MutableList<BackgroundConfiguration> = mutableListOf()

    companion object {
        fun get(): PluginState {
            return ApplicationManager.getApplication().getService(PluginState::class.java)
        }
    }

    /**
     * Returns plugin state.
     */
    override fun getState(): PluginState {
        return this
    }

    /**
     * Loads state from configuration file.
     */
    override fun loadState(state: PluginState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}