package net.intellij.plugins.sexyeditor;

import javax.swing.*;
import java.awt.Dimension;
import java.io.File;

/**
 * Background configuration UI.
 */
public class BorderConfig {

	private JTextField nameTextField;
	private JSlider opacitySlider;
	private JComboBox positionComboBox;
	private JCheckBox shrinkCheckBox;
	private JSlider shrinkSlider;
	private JCheckBox randomCheckBox;
	private JCheckBox slideshowCheckBox;
	private JTextField slideShowPause;

	private JPanel borderConfigPanel;
	private JTextField matchTextField;
	private JButton insertFilesButton;
	private JTextField positionOffsetTextField;
	private JLabel imageLabel;
	private JList<String> fileList;
	private JButton removeFilesButton;
	private DefaultListModel<String> fileListModel;

	private DefaultComboBoxModel positionComboBoxModel;

	public BorderConfig() {
		init();
		reset();
	}

	// ---------------------------------------------------------------- init

	/**
	 * Initialization.
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		this.fileListModel = (DefaultListModel<String>) fileList.getModel();
		positionComboBoxModel = (DefaultComboBoxModel) positionComboBox.getModel();
		positionComboBox.setRenderer(new PositionComboBoxRenderer());
		for (int i = 0; i < PositionComboBoxRenderer.POSITIONS.length; i++) {
			positionComboBoxModel.addElement(Integer.valueOf(i));
		}
		imageLabel.setIcon(SexyEditor.getInstance().getIcon());
		initActions();
	}


	/**
	 * Actions initialization.
	 */
	private void initActions() {

		// shrinked un/checked
		shrinkCheckBox.addActionListener(
			e -> shrinkSlider.setEnabled(shrinkCheckBox.isSelected()));

		// slideshow un/checked
		slideshowCheckBox.addActionListener(
			e -> slideShowPause.setEnabled(slideshowCheckBox.isSelected()));

		// remove files
		removeFilesButton.addActionListener(e -> {
			int min = fileList.getMinSelectionIndex();
			if (min == -1) {
				return;
			}
			int max = fileList.getMaxSelectionIndex();
			if (max == -1) {
				return;
			}
			fileListModel.removeRange(min, max);
		});

		// insert files
		insertFilesButton.addActionListener(
			e -> {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(true);
				chooser.setDialogTitle("Select images to insert...");
				chooser.setPreferredSize(new Dimension(700, 500));
				ImagePreviewPanel preview = new ImagePreviewPanel();
				preview.attachToFileChooser(chooser, "Only images");
				int returnVal = chooser.showOpenDialog(borderConfigPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File[] selectedFiles = chooser.getSelectedFiles();
					for (File file : selectedFiles) {
						fileListModel.addElement(file.getAbsolutePath());
					}
				}
			}
		);
	}

	// ---------------------------------------------------------------- LSRM

	private BackgroundConfiguration config;

	/**
	 * Loads configuration to GUI.
	 */
	public void load(BackgroundConfiguration config) {
		this.config = config;
		nameTextField.setText(config.getName());
		matchTextField.setText(config.getEditorGroup());
		opacitySlider.setValue((int) (config.getOpacity() * 100));
		positionComboBox.setSelectedIndex(config.getPosition());
		positionOffsetTextField.setText(String.valueOf(config.getPositionOffset()));

		if (shrinkCheckBox.isSelected() != config.isShrink()) {
			shrinkCheckBox.doClick();
		}

		shrinkSlider.setValue(config.getShrinkValue());

		if (randomCheckBox.isSelected() != config.isRandom()) {
			randomCheckBox.doClick();
		}

		if (slideshowCheckBox.isSelected() != config.isSlideshow()) {
			slideshowCheckBox.doClick();
		}

		slideShowPause.setText(String.valueOf(config.getSlideshowPause()));
		fileListModel.clear();
		if (config.getFileNames() != null) {
			for (String fileName : config.getFileNames()) {
				fileListModel.addElement(fileName);
			}
		}
	}

	/**
	 * Saves configuration from GUI.
	 */
	public BackgroundConfiguration save() {
		if (config == null) {
			return null;
		}
		config.setName(nameTextField.getText());
		config.setEditorGroup(matchTextField.getText());
		config.setOpacity((float) (opacitySlider.getValue() / 100.0));
		config.setPosition(positionComboBox.getSelectedIndex());
		config.setPositionOffset(Integer.valueOf(positionOffsetTextField.getText()));
		config.setShrink(shrinkCheckBox.isSelected());
		config.setShrinkValue(shrinkSlider.getValue());
		config.setRandom(randomCheckBox.isSelected());
		config.setSlideshow(slideshowCheckBox.isSelected());
		config.setSlideshowPause(Integer.parseInt(slideShowPause.getText()));

		String[] files = new String[fileListModel.size()];
		fileListModel.copyInto(files);

		config.setFileNames(files);
		return config;
	}

	/**
	 * Resets configuration to default.
	 */
	public void reset() {
		load(new BackgroundConfiguration());
	}

	/**
	 * Returns <code>true</code> if configuration is modified.
	 */
	public boolean isModified() {
		if (config == null) {
			return false;
		}

		return !nameTextField.getText().equals(config.getName())
				|| !matchTextField.getText().equals(config.getEditorGroup())
				|| opacitySlider.getValue() != (int) (config.getOpacity() * 100)
				|| positionComboBox.getSelectedIndex() != config.getPosition()
				|| !positionOffsetTextField.getText().equals(String.valueOf(config.getPositionOffset()))
				|| shrinkCheckBox.isSelected() != config.isShrink()
				|| shrinkSlider.getValue() != config.getShrinkValue()
				|| randomCheckBox.isSelected() != config.isRandom()
				|| slideshowCheckBox.isSelected() != config.isSlideshow()
				|| !slideShowPause.getText().equals(String.valueOf(config.getSlideshowPause()))
				|| !equalFileListModel(config.getFileNames())
				;
	}

	private boolean equalFileListModel(String[] otherList) {
		int size = fileListModel.getSize();
		if (otherList == null) {
			return size == 0;
		}
		if (size != otherList.length) {
			return false;
		}

		for (int i = 0; i < size; i++) {
			if (!fileListModel.get(i).equals(otherList[i])) {
				return false;
			}
		}

		return true;
	}
}
