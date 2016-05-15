package net.intellij.plugins.sexyeditor;

import com.intellij.openapi.util.IconLoader;

import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.JList;
import javax.swing.Icon;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Position combo-box renderer.
 */
public class PositionComboBoxRenderer extends JLabel implements ListCellRenderer {

	public static final String[] POSITIONS = new String[] {
			"Top-Left", "Top-Middle", "Top-Right", "Middle-Left", "Center", "Middle-Right", "Bottom-Left", "Bottom-Middle", "Bottom-Right"};

	public static final Icon[] POSITION_ICONS = new Icon[] {
			IconLoader.getIcon("/net/intellij/plugins/sexyeditor/gfx/top-left.png"),
			IconLoader.getIcon("/net/intellij/plugins/sexyeditor/gfx/top-middle.png"),
			IconLoader.getIcon("/net/intellij/plugins/sexyeditor/gfx/top-right.png"),
			IconLoader.getIcon("/net/intellij/plugins/sexyeditor/gfx/middle-left.png"),
			IconLoader.getIcon("/net/intellij/plugins/sexyeditor/gfx/center.png"),
			IconLoader.getIcon("/net/intellij/plugins/sexyeditor/gfx/middle-right.png"),
			IconLoader.getIcon("/net/intellij/plugins/sexyeditor/gfx/bottom-left.png"),
			IconLoader.getIcon("/net/intellij/plugins/sexyeditor/gfx/bottom-middle.png"),
			IconLoader.getIcon("/net/intellij/plugins/sexyeditor/gfx/bottom-right.png")
	};

	public PositionComboBoxRenderer() {
		setOpaque(true);
		setHorizontalAlignment(LEFT);
		setVerticalAlignment(CENTER);
		setPreferredSize(new Dimension(160, 20));
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		int selectedIndex = (Integer) value;
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		setIcon(POSITION_ICONS[selectedIndex]);
		setIconTextGap(6);
		setText(POSITIONS[selectedIndex]);
		return this;
	}
}
