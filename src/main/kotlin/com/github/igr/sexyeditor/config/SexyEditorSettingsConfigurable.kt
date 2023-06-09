package com.github.igr.sexyeditor.config

import com.github.igr.sexyeditor.PluginBundle
import com.github.igr.sexyeditor.ui.SexyEditorConfigPanel
import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class SexyEditorSettingsConfigurable: Configurable {

    private var configurationComponent: SexyEditorConfigPanel? = null

    @Nls
    override fun getDisplayName(): String {
        return PluginBundle.getMessage("name")
    }

    override fun getHelpTopic(): String? {
        return null
    }

    override fun createComponent(): JComponent {
        if (configurationComponent == null) {
            val state = SexyEditorState.get().state

            configurationComponent = SexyEditorConfigPanel().apply {
                load(state.configs)
            }

            if (state.configs.isEmpty()) {
                state.configs.add(BackgroundConfiguration())
            }
        }
        return configurationComponent!!.rootComponent
    }

    override fun isModified(): Boolean {
        val state = SexyEditorState.get()
        return configurationComponent?.isModified(state.configs) ?: false
    }

    override fun apply() {
        if (configurationComponent == null) {
            return
        }
        val state = SexyEditorState.get()
        state.configs = configurationComponent!!.save()
    }

    override fun reset() {
        val state = SexyEditorState.get()
        configurationComponent?.load(state.configs)
    }

    override fun disposeUIResources() {
        configurationComponent = null
    }

}