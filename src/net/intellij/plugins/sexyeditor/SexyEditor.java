package net.intellij.plugins.sexyeditor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

/**
 * Sexy editor plugin.
 */
public class SexyEditor implements ApplicationComponent {

	public static final String COMPONENT_NAME = "SexyEditor";
	private Icon pluginIcon;

	public SexyEditor() {
	}

	public static SexyEditor getInstance() {
		return ApplicationManager.getApplication().getComponent(SexyEditor.class);
	}

	private SexyEditorListener editorListener;

	/**
	 * Registers new editor factory listener.
	 */
	public void initComponent() {
		SexyEditorState state = SexyEditorState.getInstance();

		if (state.configs.isEmpty()) {
			state.configs.add(new BackgroundConfiguration());
		}
		editorListener = new SexyEditorListener(state);
		EditorFactory.getInstance().addEditorFactoryListener(editorListener, () -> editorListener = null);
	}

	/**
	 * Removes editor listener.
	 */
	public void disposeComponent() {
//		EditorFactory.getInstance().removeEditorFactoryListener(editorListener);
		editorListener = null;
	}

	/**
	 * Returns component name.
	 */
	@NotNull
	public String getComponentName() {
		return COMPONENT_NAME;
	}

	/**
	 * Returns random 32x32 icon representing the plugin.
	 */
	@Nullable
	public Icon getIcon() {
		if (pluginIcon == null) {
			pluginIcon = IconLoader.getIcon("/net/intellij/plugins/sexyeditor/gfx/heart.png");
		}
		return pluginIcon;
	}

}
