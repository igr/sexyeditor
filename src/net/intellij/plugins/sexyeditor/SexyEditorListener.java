package net.intellij.plugins.sexyeditor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.border.Border;
import java.awt.Component;

/**
 * Listens for editor creation and release, adds and removes {@link net.intellij.plugins.sexyeditor.BackgroundBorder}
 * to editor component.
 */
public class SexyEditorListener implements EditorFactoryListener {

	private final SexyEditorState state;

	public SexyEditorListener(SexyEditorState state) {
		this.state = state;
	}

	/**
	 * Editor is created, finds appropriate background border and apply it to the editor. 
	 */
	public void editorCreated(@NotNull EditorFactoryEvent event) {
		Editor editor = event.getEditor();
		VirtualFile editorFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
		if (editorFile == null) {
			return;
		}
		JComponent editorComponent = editor.getContentComponent();
		BackgroundBorder border = createBorder(editorFile.getName(), editorComponent);
		if (border != null) {
			editorComponent.setBorder(border);
			JViewport jvp = (JViewport) editorComponent.getParent();
			jvp.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		}
	}

	/**
	 * Editor is closed, removes border for editor component.
	 */
	public void editorReleased(@NotNull EditorFactoryEvent event) {
		Editor editor = event.getEditor();
		JComponent editorComponent = editor.getContentComponent();
		Border border = editorComponent.getBorder();
		if (border instanceof BackgroundBorder) {
			BackgroundBorder bb = (BackgroundBorder) border;
			removeBorder(bb);
			bb.close();
			editorComponent.setBorder(null);
		}
	}

	// ---------------------------------------------------------------- border


	/**
	 * Returns new border instance for given editor file name.
	 * Each border is registered into the configuration.
	 * Image is loaded during border creation.
	 */
	private BackgroundBorder createBorder(String fileName, Component component) {
		for (BackgroundConfiguration config : state.configs) {
			if (config.matchFileName(fileName)) {
				BackgroundBorder bb = new BackgroundBorder(config, component);
				config.registerBorder(bb);
				return bb;
			}
		}
		return null;
	}

	/**
	 * Unregisters border in all configurations.
	 */
	private void removeBorder(BackgroundBorder border) {
		for (BackgroundConfiguration config : state.configs) {
			config.unregisterBorder(border);
		}
	}
}
