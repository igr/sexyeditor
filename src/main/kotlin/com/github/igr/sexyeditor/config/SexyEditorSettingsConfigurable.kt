package com.github.igr.sexyeditor.config

import com.github.igr.sexyeditor.SexyEditorBundle
import com.github.igr.sexyeditor.ui.SexyEditorConfigPanel
import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class SexyEditorSettingsConfigurable: Configurable {

    private var configurationComponent: SexyEditorConfigPanel? = null

    @Nls
    override fun getDisplayName(): String {
        return SexyEditorBundle.getMessage("name")
    }

    override fun getHelpTopic(): String? {
        return null
    }

    override fun createComponent(): JComponent {
        if (configurationComponent == null) {
            val state = SexyEditorState.get().state

            configurationComponent = SexyEditorConfigPanel()
            configurationComponent!!.load(state.configs)
        }
        return configurationComponent?.rootComponent!!
    }

    override fun isModified(): Boolean {
        return configurationComponent?.isModified() ?: false
    }

    override fun apply() {
        val state = SexyEditorState.get()
        state.configs = configurationComponent!!.save()!!
    }

    override fun reset() {
        val state = SexyEditorState.get()
        configurationComponent!!.load(state.configs)
    }

    override fun disposeUIResources() {
        configurationComponent = null
    }

}