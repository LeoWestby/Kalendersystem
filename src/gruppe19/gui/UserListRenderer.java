package gruppe19.gui;

import java.awt.Component;

import gruppe19.model.User;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class UserListRenderer extends JLabel implements ListCellRenderer<User>{

	@Override
	public Component getListCellRendererComponent(JList<? extends User> list,
			User value, int index, boolean isSelected, boolean cellHasFocus) {
	setText(value.getName());
		
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
