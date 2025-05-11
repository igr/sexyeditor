package com.github.igr.sexyeditor.config

import com.github.igr.sexyeditor.PluginBundle
import com.github.igr.sexyeditor.ui.MainConfigPanel
import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class SettingsConfigurable: Configurable {

    private var configurationComponent: MainConfigPanel? = null

    @Nls
    override fun getDisplayName(): String {
        return PluginBundle.getMessage("name")
    }

    override fun getHelpTopic(): String? {
        return null
    }

    override fun createComponent(): JComponent {
        if (configurationComponent == null) {
            val state = PluginState.get().state

            configurationComponent = MainConfigPanel().apply {
                load(state.configs)
            }

            if (state.configs.isEmpty()) {
                // initial configuration
                state.configs.add(BackgroundConfiguration())
            }
        }
        return configurationComponent!!.rootComponent
    }

    override fun isModified(): Boolean {
        val state = PluginState.get()
        return configurationComponent?.isModified(state.configs) ?: false
    }

    override fun apply() {
        if (configurationComponent == null) {
            return
        }
        val state = PluginState.get()
        state.configs = configurationComponent!!.save()
    }

    override fun reset() {
        val state = PluginState.get()
        configurationComponent?.load(state.configs)
    }

    override fun disposeUIResources() {
        configurationComponent = null
    }

}