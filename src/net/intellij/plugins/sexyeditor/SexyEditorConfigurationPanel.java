package net.intellij.plugins.sexyeditor;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Configuration panel.
 */
public class SexyEditorConfigurationPanel {

	private JPanel panel;
	private JList editorsList;
	private JButton addNewButton;
	private JButton removeButton;
	private JButton moveUpButton;
	private JButton moveDownButton;
	private BorderConfig borderConfig;

	private DefaultListModel editorsListModel;

	public SexyEditorConfigurationPanel() {
		init();
	}

	public SexyEditorConfigurationPanel(List<BackgroundConfiguration> configs) {
		init();
		load(configs);
	}

	public JPanel getPanel() {
		return panel;
	}

	// ---------------------------------------------------------------- init

	/**
	 * Initialization.
	 */
	private void init() {
		this.editorsListModel = (DefaultListModel) editorsList.getModel();
		initActions();
	}

	/**
	 * Initialization of all actions.
	 */
	private void initActions() {

		// add new config and selects it
		addNewButton.addActionListener(ae -> {
			int selected = editorsList.getSelectedIndex();
			if (selected != -1) {
				editorsListModel.add(selected, new BackgroundConfiguration());
				editorsList.setSelectedIndex(selected);
			} else {
				editorsListModel.add(editorsListModel.size(), new BackgroundConfiguration());
				editorsList.setSelectedIndex(editorsListModel.size() - 1);
			}
		});

		// remove config and selects the previous.
		removeButton.addActionListener(e -> {
			int selected = editorsList.getSelectedIndex();
			if (selected != -1) {
				editorsListModel.remove(selected);
				if (selected >= editorsListModel.getSize()) {
					selected--;
				}
				if (selected >= 0) {
					editorsList.setSelectedIndex(selected);
				}
			}
		});

		// up
		moveUpButton.addActionListener(e -> {
			int selected = editorsList.getSelectedIndex();
			if (selected  <= 0) {
				return;
			}
			Object removed = editorsListModel.remove(selected);
			selected--;
			editorsListModel.add(selected, removed);
			editorsList.setSelectedIndex(selected);
		});

		// down
		moveDownButton.addActionListener(e -> {
			int selected = editorsList.getSelectedIndex();
			if ((selected  == -1) || (selected == editorsListModel.size() - 1)) {
				return;
			}
			Object removed = editorsListModel.remove(selected);
			selected++;
			editorsListModel.add(selected, removed);
			editorsList.setSelectedIndex(selected);
		});

		// select a config
		editorsList.addListSelectionListener(lse -> {
			int selected = editorsList.getSelectedIndex();
			if (selected != -1) {
				borderConfig.load((BackgroundConfiguration) editorsListModel.getElementAt(selected));
			}
		});
	}

	private void saveBackgroundConfig(int index) {
		if (borderConfig == null) {
			return;
		}
		BackgroundConfiguration newConfiguration = borderConfig.save();
		editorsListModel.remove(index);
		editorsListModel.add(index, newConfiguration);
	}


	// ---------------------------------------------------------------- LSRM

	private List<BackgroundConfiguration> configs;

	/**
	 * Loads configuration list into the gui and selects the very first element.
	 */
	public void load(List<BackgroundConfiguration> configs) {
		this.configs = configs;

		editorsListModel.removeAllElements();
		for (BackgroundConfiguration cfg : configs) {
			editorsListModel.addElement(cfg);
		}
		if (editorsListModel.getSize() >= 1) {
			editorsList.setSelectedIndex(0);    // loads border config
		}
		editorsList.repaint();
	}

	/**
	 * Saves current changes and creates new configuration list instance.
	 */
	public List<BackgroundConfiguration> save() {
		if (configs == null) {
			return null;
		}

		// replace current border config
		int selected = editorsList.getSelectedIndex();
		if (selected != -1) {
			saveBackgroundConfig(selected);
			editorsList.setSelectedIndex(selected);
		}

		// creates new list
		List<BackgroundConfiguration> newConfigs = new ArrayList<BackgroundConfiguration>(editorsListModel.getSize());
		for (int i = 0; i < editorsListModel.getSize(); i++) {
			newConfigs.add((BackgroundConfiguration) editorsListModel.getElementAt(i));
		}
		configs = newConfigs;
		return newConfigs;
	}

	/**
	 * Returns <code>false</code> if all list elements are the same as in given config list
	 * and selected border config is modified.
	 */
	public boolean isModified() {
		if (configs == null) {
			return false;
		}
		if (borderConfig.isModified()) {
			return true;
		}
		if (configs.size() != editorsListModel.getSize()) {
			return true;
		}
		for (int i = 0; i < editorsListModel.getSize(); i++) {
			if (configs.get(i) != editorsListModel.getElementAt(i)) {
				return true;
			}
		}
		return false;
	}


	// ---------------------------------------------------------------- main

	public static void main(String[] args) {
		JFrame frame = new JFrame("SexyEditorConfigurationPanel");
		frame.setContentPane(new SexyEditorConfigurationPanel(new ArrayList<>()).panel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
