package net.intellij.plugins.sexyeditor;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

/**
 * Accessory for JFileChooser that enables image previews.
 * Do not loads large images automatically, because of memory consumption.
 * Hides itself automatically when selecting non-images.
 */
public class ImagePreviewPanel extends JPanel implements PropertyChangeListener {

	private JLabel label;
	private JButton sure;
	private int maxImgWidth;

	private final int previewSize;
	private int maxFileSize = 512 * 1024;
	private boolean showOnlyOnImages = true;
	private String[] imageExtensions = new String[] {"gif", "jpg", "jpeg", "bmp", "png"};

	public ImagePreviewPanel() {
		this(200);
	}

	public ImagePreviewPanel(int previewSize) {
		this.previewSize = previewSize;
		setLayout(new BorderLayout(5, 5));
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(new JLabel("Preview:"), BorderLayout.NORTH);
		label = new JLabel();
		label.setBackground(Color.WHITE);
		label.setOpaque(true);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(previewSize, previewSize));
		maxImgWidth = previewSize - 5;
		label.setBorder(BorderFactory.createEtchedBorder());
		add(label, BorderLayout.CENTER);

		sure = new JButton("big image - click to preview");
		sure.setVisible(false);
		add(sure, BorderLayout.SOUTH);
		sure.addActionListener(e -> {
			sure.setVisible(false);
			Icon icon = loadImage();
			label.setIcon(icon);
			ImagePreviewPanel.this.repaint();
		});
	}

	/**
	 * Attach this accessory to file chooser and, optionally, add file filter for image files.
	 */
	public void attachToFileChooser(JFileChooser fileChooser, String imagesFilterName) {
		fileChooser.setAccessory(this);
		fileChooser.addPropertyChangeListener(this);
		if (imagesFilterName != null) {
			fileChooser.setFileFilter(new FileNameExtensionFilter(imagesFilterName, imageExtensions));
		}
	}

	private File newFile;

	public void propertyChange(PropertyChangeEvent evt) {
		Icon icon = null;
		if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
			if (showOnlyOnImages) {
				this.setVisible(false);
			}
			newFile = (File) evt.getNewValue();
			if (newFile != null) {
				String path = newFile.getAbsolutePath();
				int i = path.lastIndexOf('.');
				if (i != -1) {
					String extension = path.substring(i + 1).toLowerCase();
					for (String ext : imageExtensions) {
						if (extension.equals(ext)) {
							if (showOnlyOnImages) {
								this.setVisible(true);
							}
							sure.setVisible((maxFileSize != -1) && (newFile.length() > maxFileSize));
							if (!sure.isVisible()) {
								icon = loadImage();
							}
							break;
						}
					}
				}
			}
			label.setIcon(icon);
			this.repaint();
		}
	}

	/**
	 * Loads and scales images for previewing purposes if necessary.
	 */
	private Icon loadImage() {
		if (newFile == null) {
			return null;
		}
		try {
			BufferedImage image = ImageIO.read(newFile);
			float width = image.getWidth();
			float height = image.getHeight();
			if (width <= maxImgWidth) {
				return new ImageIcon(image);
			}
			float scale = height / width;
			width = maxImgWidth;
			height = (width * scale); // height should be scaled from new width
			return new ImageIcon(SwingUtil.getScaledInstance(image, Math.max(1, (int) width), Math.max(1, (int) height), RenderingHints.VALUE_INTERPOLATION_BILINEAR, true));
		}
		catch (IOException ioex) {
			// ignore, couldn't read image
		}
		finally {
			newFile = null;
		}
		return null;
	}

	// ---------------------------------------------------------------- accessories

	/**
	 * Returns preview size.
	 */
	public int getPreviewSize() {
		return previewSize;
	}

	public int getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * Sets maximal file size of image that will be previewed automatically.
	 * By setting this value to <code>-1</code> all images will be previewed.  
	 */
	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public boolean isShowOnlyOnImages() {
		return showOnlyOnImages;
	}

	public void setShowOnlyOnImages(boolean showOnlyOnImages) {
		this.showOnlyOnImages = showOnlyOnImages;
	}
}