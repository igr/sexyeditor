package net.intellij.plugins.sexyeditor;

import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.util.StringTokenizer;
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
	private JTextArea fileListTextArea;
	private JTextField positionOffsetTextField;
	private JLabel imageLabel;

	private DefaultComboBoxModel positionComboBoxModel;

	public BorderConfig() {
		init();
		reset();
	}

	// ---------------------------------------------------------------- init

	/**
	 * Initialization.
	 */
	private void init() {
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
					StringBuilder result = new StringBuilder();
					for (File file : selectedFiles) {
						result.append(file.getAbsolutePath()).append('\n');
					}
					fileListTextArea.setText(fileListTextArea.getText() + result.toString());
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
		fileListTextArea.setText(stringArrayToString(config.getFileNames()));
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
		config.setFileNames(stringToStringArray(fileListTextArea.getText()));
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
				|| !fileListTextArea.getText().equals(stringArrayToString(config.getFileNames()))
				;
	}

	// ---------------------------------------------------------------- util

	private String stringArrayToString(String... strarr) {
		if (strarr == null) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		for (String s : strarr) {
			result.append(s).append('\n');
		}
		return result.toString();
	}

	private String[] stringToStringArray(String s) {
		if (s == null) {
			return null;
		}
		s = s.trim();
		if (s.length() == 0) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(s, "\n\r");
		int total = st.countTokens();
		String[] result = new String[total];
		int i = 0;
		while (st.hasMoreTokens()) {
			result[i] = st.nextToken().trim();
			i++;
		}
		return result;
	}

}
