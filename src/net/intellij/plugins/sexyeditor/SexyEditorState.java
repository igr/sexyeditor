package net.intellij.plugins.sexyeditor;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@State(
	name = SexyEditor.COMPONENT_NAME,
	storages = {@Storage(id = "sexyeditor", file = "sexyeditor.xml")})
public class SexyEditorState implements PersistentStateComponent<SexyEditorState> {

	public List<BackgroundConfiguration> configs = new ArrayList<>();

	@Nullable
	public static SexyEditorState getInstance() {
		return ServiceManager.getService(SexyEditorState.class);
	}

	/**
	 * Returns plugin state.
	 */
	@Override
	public SexyEditorState getState() {
		return this;
	}

	/**
	 * Loads state from configuration file.
	 */
	@Override
	public void loadState(SexyEditorState state) {
		XmlSerializerUtil.copyBean(state, this);
	}

}
