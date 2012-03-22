package gruppe19.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import gruppe19.client.ktn.ServerAPI;
import gruppe19.model.User;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SelectUserDialog extends JDialog implements ListSelectionListener, ActionListener{
	
	private JButton btnClose, btnAdd;
	private JList listUsers;
	private DefaultListModel defaultListModel, model;
	private DefaultListSelectionModel defaultListSelectionModel;
	
	public SelectUserDialog(DefaultListModel model) {
		this.model = model;
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
			userinapp.add((User)model.get(i));
		}
		System.err.println(userinapp);
		for (User user : list) {
			boolean exsist = false;
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
				if(!model.contains(a)){
					model.addElement(a);
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
