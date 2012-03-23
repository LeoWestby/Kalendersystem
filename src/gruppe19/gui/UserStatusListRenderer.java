package gruppe19.gui;

import gruppe19.client.ktn.ServerAPI.Status;
import gruppe19.model.User;

import java.awt.Component;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class UserStatusListRenderer extends JLabel implements ListCellRenderer{

	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		// TODO Auto-generated method stub
		Entry<User,Status> set = (Entry<User,Status>)value;
		
		setText((set.getKey()).getName() + " " + set.getValue());
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
