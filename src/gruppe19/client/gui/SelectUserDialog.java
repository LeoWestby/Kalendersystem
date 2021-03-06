package gruppe19.client.gui;

import gruppe19.client.ktn.ServerAPI;
import gruppe19.client.ktn.ServerAPI.Status;
import gruppe19.model.User;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A dialog for selecting users 
 *
 */
public class SelectUserDialog extends JDialog implements ListSelectionListener, ActionListener{
	
	private JButton btnClose, btnAdd;
	private JList listUsers;
	private DefaultListModel defaultListModel, model;
	private DefaultListSelectionModel defaultListSelectionModel;
	private User owner;

	private class UserListRenderer extends JLabel implements ListCellRenderer{
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setText(((User) value).getName());
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
	
	/**
	 * creates a new SelectUserDialog with a DefaultlistModel and an owner of the appointment
	 * The gui will then hold every other user than the users who are currently selected to join an event   
	 * @param model
	 * @param owner
	 */
	public SelectUserDialog(DefaultListModel model, User owner) {
		this.model = model;
		this.owner = owner;
		setUp();
		addFreeUsers();
	}
	
	private void setUp(){
		setLayout(new FlowLayout());
		//User list
		defaultListModel = new DefaultListModel();
		listUsers = new JList();
		listUsers.setModel(defaultListModel);
		listUsers.setCellRenderer(new UserListRenderer());
		
		defaultListSelectionModel = new DefaultListSelectionModel();
		defaultListSelectionModel.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		listUsers.setSelectionModel(defaultListSelectionModel);
		
		JScrollPane scrollUsers = new JScrollPane(listUsers);
		scrollUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollUsers.setPreferredSize(new Dimension(200,200));
		
		btnClose = new JButton("Lukk");
		btnAdd = new JButton("Legg til brukere");
		
		add(scrollUsers);
		add(btnAdd);
		add(btnClose);
		
		//actionlisteners
		btnAdd.addActionListener(this);
		btnClose.addActionListener(this);
		defaultListSelectionModel.addListSelectionListener(this);
		
		//behaviour
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
	}
	
	private void addFreeUsers(){
		//TODO: Hent alle brukere fra databasen. Sjekk deretter om samme brukeren ikke blir lagt til to ganger
		ArrayList<User> list = (ArrayList<User>) ServerAPI.getUsers();
		ArrayList<User> userinapp = new ArrayList<User>();
		for (int i = 0; i < model.size(); i++) {
			Entry<User,Status> set = (Entry<User,Status>)model.get(i);
			userinapp.add(set.getKey());
		}
		for (User user : list) {
			boolean exsist = false;
			if(user.getUsername().equals(owner.getUsername())){
				continue;
			}
			for (User users : userinapp) {
				if(user.getUsername().equals(users.getUsername())){
					exsist = true;
					break;
				}
				
			}
			if (!exsist) {
				defaultListModel.addElement(user);
			}
		}
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnClose){
			dispose();
		}
		if(e.getSource() == btnAdd){
			if(!defaultListSelectionModel.isSelectionEmpty()){
				int i = defaultListSelectionModel.getAnchorSelectionIndex();
				User a = (User)defaultListModel.get(i);
				HashMap<User, Status> b = new HashMap<User, ServerAPI.Status>();
				b.put(a, Status.PENDING);
				Set<Entry<User,Status>> set = b.entrySet();
				for (Entry<User,Status> user : set) {
					model.addElement(user);
				}
				defaultListModel.remove(i);
			}
			
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

	}
	
}
