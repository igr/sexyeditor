package net.intellij.plugins.sexyeditor;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SexyEditorConfigurable implements Configurable {

	private SexyEditorConfigurationPanel configurationComponent;

	@Nls
	@Override
	public String getDisplayName() {
		return SexyEditor.COMPONENT_NAME;
	}

	@Nullable
	@Override
	public String getHelpTopic() {
		return null;
	}

	@Nullable
	@Override
	public JComponent createComponent() {
		if (configurationComponent == null) {
			SexyEditorState state = SexyEditorState.getInstance();

			configurationComponent = new SexyEditorConfigurationPanel();
			configurationComponent.load(state.configs);
		}
		return configurationComponent.getPanel();
	}

	@Override
	public boolean isModified() {
		return configurationComponent.isModified();
	}

	@Override
	public void apply() throws ConfigurationException {
		SexyEditorState state = SexyEditorState.getInstance();

		state.configs = configurationComponent.save();

		state.configs.forEach(BackgroundConfiguration::repaintAllEditors);
	}

	@Override
	public void reset() {
		SexyEditorState state = SexyEditorState.getInstance();

		configurationComponent.load(state.configs);
	}

	@Override
	public void disposeUIResources() {
		configurationComponent = null;
	}


}
