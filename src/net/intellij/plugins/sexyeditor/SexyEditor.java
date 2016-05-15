package net.intellij.plugins.sexyeditor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.Random;

/**
 * Sexy editor plugin.
 */
public class SexyEditor implements ApplicationComponent {

	public static final String COMPONENT_NAME = "SexyEditor";
	private static Random rnd = new Random();
	private Icon[] pluginIcons;

	public SexyEditor() {
		System.out.println("11111111");
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
		if (pluginIcons == null) {
			pluginIcons = new Icon[5];
			for (int i = 0; i < pluginIcons.length; i++) {
				pluginIcons[i] = IconLoader.getIcon("/net/intellij/plugins/sexyeditor/gfx/girl" + (i + 1) +".png");
			}
		}
		return pluginIcons[rnd.nextInt(pluginIcons.length)];
	}

}
