package gruppe19.client.gui;

import gruppe19.model.Room;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class RoomListRenderer extends JLabel implements ListCellRenderer{

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		setText(((Room) value).getName());
		if (isSelected) {
            setBackground(list.getSelectionBackground());
              setForeground(list.getSelectionForeground());
          }
        else {
              setBackground(list.getBackground());
              setForeground(list.getForeground());
          }
        setEnabled(list.isEnabled());
        setOpaque(true);
		return this;
	}

}
