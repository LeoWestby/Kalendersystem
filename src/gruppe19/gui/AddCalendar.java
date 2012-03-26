package gruppe19.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import gruppe19.client.ktn.ServerAPI;
import gruppe19.client.ktn.ServerAPI.Status;
import gruppe19.model.Appointment;
import gruppe19.model.User;
import gruppe19.server.ktn.Server;

public class AddCalendar extends JDialog implements ActionListener, ListSelectionListener{

	
	private static final long serialVersionUID = 1L;

	private Appointment model;

	private GridBagLayout layout;
	private JButton btnCancel, btnConfirm, btnColorChooser, btnAddUser, btnDeleteImportedCalendar, btnDeleteAllImportedCalendars;
	private DefaultListModel defaultModel;
	private DefaultListSelectionModel defaultSelectModel;
	private GridBagConstraints constraints;
	private JLabel labTimeError, labTitleError;
	private JList listUsers;
	private Dimension dim = new Dimension(210, 20);
	private User opener;
	private Color color;



	public AddCalendar() throws SocketTimeoutException, UnknownHostException, IOException {
		setUp();
		getValues();
	}

	private void setUp() throws SocketTimeoutException, UnknownHostException, IOException{
		Server.startServer();
		ServerAPI.open();
		layout = new GridBagLayout();
		this.setLayout(layout);
		constraints = new GridBagConstraints();

		//legg til deltagere
		constraints.gridx=0;
		constraints.gridy=0;
		add(new JLabel("Velg kalendre: "),constraints);

		constraints.gridx=0;
		constraints.gridy=1;
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

		//legge til knapper
		
		constraints.gridy=1;
		constraints.gridx=3;
		
		// Create a button using the action
		btnColorChooser = new JButton("Velg farge");
		add(btnColorChooser,constraints);
		
		//knapper for å slette en kalender og for å slette alle kalendre
		constraints.gridy=2;
		constraints.gridx=0;
		btnDeleteImportedCalendar = new JButton("Slett kalender");
		add(btnDeleteImportedCalendar,constraints);
		
		constraints.gridx=1;
		btnDeleteAllImportedCalendars=new JButton("Slett alle importerte kalendre");
		add(btnDeleteAllImportedCalendars,constraints);
		
		//knapper for godta og slett
		constraints.gridx=3;
		btnConfirm=new JButton("Lagre");
		add(btnConfirm, constraints);
		
		constraints.gridx=4;
		btnCancel = new JButton("Avbryt");
		add(btnCancel, constraints);

		//add actionlisteners
		btnCancel.addActionListener(this);
		btnDeleteImportedCalendar.addActionListener(this);
		btnConfirm.addActionListener(this);
		btnDeleteAllImportedCalendars.addActionListener(this);
		btnColorChooser.addActionListener(this);

		//behaviour
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
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
//		labTimeError.setText("");
//		labTitleError.setText("");

		if (e.getSource() == btnCancel) {
			dispose();
		}
		

		//button add users
		if (e.getSource() == btnAddUser) {
			SelectUserDialog selectUser = new SelectUserDialog(defaultModel,model.getOwner());
			selectUser.setVisible(true);
		}

		//button delete user
		if(e.getSource()==btnDeleteImportedCalendar){
			if(!defaultSelectModel.isSelectionEmpty()){
				int i= defaultSelectModel.getAnchorSelectionIndex();
				defaultModel.remove(i);
			}
		}
		
		if(e.getSource()==btnColorChooser){
			color=JColorChooser.showDialog(this, "Velg farge til kalenderen", Color.WHITE);
			if(color==null)
				color=Color.WHITE;
		}
	}
	

	
	public void addConfirmButtonListener(ActionListener listener) {
		btnConfirm.addActionListener(listener);
	}
	
	

	@Override
	public void valueChanged(ListSelectionEvent arg0) {

	}
	
	public static void main(String[] args) throws SocketTimeoutException, UnknownHostException, IOException {
		
		AddCalendar cal=new AddCalendar();
	}


}


