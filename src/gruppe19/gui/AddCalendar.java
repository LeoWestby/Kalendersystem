package gruppe19.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import gruppe19.client.ktn.ServerAPI.Status;
import gruppe19.model.Appointment;
import gruppe19.model.User;

public class AddCalendar extends JDialog implements ActionListener, ListSelectionListener{

	private Appointment model;

	private GridBagLayout layout;
	private JButton btnConfirm, btnRoom, btnCancel, btnAddUser, btnDeleteUser, btnRemoveRoom,btnDelete ;
	private DefaultListModel defaultModel;
	private DefaultListSelectionModel defaultSelectModel;
	private GridBagConstraints constraints;
	private JLabel labTimeError, labTitleError;
	private JList listUsers;
	private Dimension dim = new Dimension(210, 20);
	private User opener;
	private boolean noButtons;



	public AddCalendar(Appointment model,User opener, boolean noButtons) {
		this.model=model;
		this.opener=opener;
		this.noButtons = noButtons;
		setUp();
		getValues();
	}

	private void setUp(){
		layout = new GridBagLayout();
		this.setLayout(layout);
		constraints = new GridBagConstraints();

		
		
		//legg til deltagere
		constraints.gridx=0;
		constraints.gridy=7;
		add(new JLabel("Deltagere: "),constraints);

		constraints.gridx=1;
		defaultModel = new DefaultListModel();
		listUsers = new JList();
		listUsers.setModel(defaultModel);

		listUsers.setCellRenderer(new UserStatusListRenderer());

		defaultSelectModel = new DefaultListSelectionModel();
		defaultSelectModel.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		listUsers.setSelectionModel(defaultSelectModel);
		defaultSelectModel.addListSelectionListener(this);
		JScrollPane scrollUsers = new JScrollPane(listUsers);
		scrollUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollUsers.setPreferredSize(new Dimension(210, 100));
		add(scrollUsers, constraints);

		if (!noButtons) {
			//legge til knapper
			constraints.gridx=2;
			constraints.gridy=7;
			btnAddUser = new JButton("Legg til deltager");
			add(btnAddUser,constraints);
			constraints.gridx=3;
			btnDeleteUser = new JButton("Slett deltager");
			add(btnDeleteUser,constraints);
			//knapper for godta og slett av avtale
			constraints.gridx=1;
			constraints.gridy=8;
			btnConfirm = new JButton("Legg til/endre avtale");
			add(btnConfirm, constraints);
			constraints.gridx=2;
			btnCancel = new JButton("Avbryt");
			add(btnCancel, constraints);
			constraints.gridx=3;
			btnDelete = new JButton("Slett avtale");

			//An ID of -1 means the appointment is new
			if (model.getID() != -1) {
				add(btnDelete, constraints);
			}

			//add actionlisteners
			btnRoom.addActionListener(this);
			btnAddUser.addActionListener(this);
			btnCancel.addActionListener(this);
			btnDeleteUser.addActionListener(this);
			btnRemoveRoom.addActionListener(this);
		}

		//behaviour
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
	}



	private void setValues(){
		Map<User,Status> userList= new HashMap<User,Status>();

		//adder brukere
		for (int i = 0; i < defaultModel.size(); i++) {
			Entry<User,Status> a = (Entry<User,Status>)defaultModel.get(i);
			userList.put(a.getKey(),a.getValue());
		}

		model.setUserList(userList);
	}

	private void getValues(){
		//adder brukere
		Map<User, Status> users = model.getUserList();
		if(users==null){
			return;
		}
		Set<Entry<User,Status>> set = users.entrySet();
		for (Entry<User,Status> user : set) {

			defaultModel.addElement(user);
		}


	}

	public void setModel(Appointment a){
		if(a==null){
			throw new NullPointerException();
		}
		this.model = a;
		getValues();
	}

	public Appointment getModel(){
		return model;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		labTimeError.setText("");
		labTitleError.setText("");

		if (e.getSource() == btnCancel) {
			dispose();
		}
		

		//button add users
		if (e.getSource() == btnAddUser) {
			SelectUserDialog selectUser = new SelectUserDialog(defaultModel,model.getOwner());
			selectUser.setVisible(true);
		}

		//button delete user
		if(e.getSource()==btnDeleteUser){
			if(!defaultSelectModel.isSelectionEmpty()){
				int i= defaultSelectModel.getAnchorSelectionIndex();
				defaultModel.remove(i);
			}
		}
	}
	

	
	public void addConfirmButtonListener(ActionListener listener) {
		btnConfirm.addActionListener(listener);
	}
	
	public void addDeleteButtonListener(ActionListener listener) {
		btnDelete.addActionListener(listener);
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

}


