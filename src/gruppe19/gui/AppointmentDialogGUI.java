package gruppe19.gui;

import gruppe19.client.ktn.ServerAPI.Status;
import gruppe19.model.Appointment;
import gruppe19.model.Room;
import gruppe19.model.User;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.toedter.calendar.JDateChooser;

public class AppointmentDialogGUI extends JDialog implements ActionListener, ListSelectionListener{

	private Appointment model;

	private GridBagLayout layout;
	private JTextField txtTitle, txtPlace, txtRoom;
	private JButton btnConfirm, btnRoom, btnCancel, btnAddUser, btnDeleteUser, btnRemoveRoom,btnDelete ;
	private DefaultListModel defaultModel;
	private DefaultListSelectionModel defaultSelectModel;
	private JTextArea txtDescription;
	private GridBagConstraints constraints;
	private DecimalFormat format = new DecimalFormat("00");
	private JSpinner spinnerEnd, spinnerStart;
	private JDateChooser dateChooser;
	private JLabel labTimeError, labTitleError, labOwner;
	private JList listUsers;
	private Dimension dim = new Dimension(210, 20);
	private User opener;
	private boolean noButtons;
	private Room roomtemp;


//	/**
//	 * Create new AppointmentDialogGUI with a {@link Appointment} model set to null
//	 */
//	public AppointmentDialogGUI() {
//		setUp();
//		
//	}
//	/**
//	 * Create new AppointmentDialogGUI with a {@link Appointment} model set to null
//	 */
//	public AppointmentDialogGUI(Appointment model) {
//		this.model=model;
//		setUp();
//		getValues();
//
//	}
	public AppointmentDialogGUI(Appointment model,User opener, boolean noButtons) {
		this.model=model;
		this.opener=opener;
		this.noButtons = noButtons;
		setUp();
		getValues();
		if(!model.getOwner().getUsername().equals(opener.getUsername())){
			setDisabled();
		}
	}

	private void setUp(){
		layout = new GridBagLayout();
		this.setLayout(layout);
		constraints = new GridBagConstraints();

		//tittel på avtale;
		constraints.gridx =0;
		constraints.gridy =0;
		add(new JLabel("Tittel: "),constraints);
		constraints.gridx=1;
		txtTitle = new JTextField();
		txtTitle.setPreferredSize(dim);
		add(txtTitle, constraints);

		constraints.gridx=2;
		labTitleError = new JLabel();
		add(labTitleError, constraints);

		//dato
		constraints.gridx=0;
		constraints.gridy=1;
		add(new JLabel("Dato: "),constraints);
		constraints.gridx=1;
		dateChooser = new JDateChooser();
		dateChooser.setPreferredSize(dim);
		add(dateChooser,constraints);


		//Starttidspunkt
		constraints.gridx=0;
		constraints.gridy=2;
		add(new JLabel("Start: "),constraints);
		constraints.gridx=1;
		SpinnerListModel spinnerListModel = new SpinnerListModel(getTimeInterval());

		spinnerStart = new JSpinner(spinnerListModel);
		spinnerStart.setPreferredSize(dim);
		add(spinnerStart,constraints);

		//sluttidspunkt
		constraints.gridx=0;
		constraints.gridy=3;
		add(new JLabel("Slutt: "),constraints);
		constraints.gridx=1;
		SpinnerListModel spinnerListModel1 = new SpinnerListModel(getTimeInterval());

		spinnerEnd = new JSpinner(spinnerListModel1);
		spinnerEnd.setPreferredSize(dim);
		add(spinnerEnd,constraints);

		constraints.gridx=2;
		labTimeError = new JLabel();
		add(labTimeError,constraints);
		//inputverifiers

		//Beksrivelse
		constraints.gridx=0;
		constraints.gridy=4;
		add(new JLabel("Beskrivelse: "),constraints);
		constraints.gridx=1;
		txtDescription = new JTextArea();
		txtDescription.setLineWrap(true);
		txtDescription.setName("TextDescription");


		JScrollPane pane = new JScrollPane(txtDescription);
		pane.setPreferredSize(new Dimension(210, 100));
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(pane,constraints);

		//sted
		constraints.gridx=0;
		constraints.gridy=5;
		add(new JLabel("Sted: "),constraints);
		constraints.gridx=1;
		txtPlace = new JTextField();
		txtPlace.setPreferredSize(dim);
		txtPlace.setName("DateEnd");
		add(txtPlace,constraints);

		//rom
		constraints.gridx=0;
		constraints.gridy=6;
		add(new JLabel("Rom: "),constraints);
		constraints.gridx=1;
		txtRoom = new JTextField();
		txtRoom.setPreferredSize(dim);
		txtRoom.setEditable(false);
		add(txtRoom,constraints);
		
		if (!noButtons) {
			constraints.gridx=2;
			btnRoom = new JButton("Finn rom");
			add(btnRoom,constraints);
			constraints.gridx=3;
			btnRemoveRoom = new JButton("Fjern Rom");
			add(btnRemoveRoom,constraints);
		}


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

		constraints.gridx=1;
		constraints.gridy=8;
		labOwner = new JLabel("Eier: ");
		add(labOwner,constraints);
		
		if (!noButtons) {
			//legge til knapper
			constraints.gridx=2;
			constraints.gridy=7;
			btnAddUser = new JButton("Legg til deltager");
			add(btnAddUser,constraints);
			constraints.gridx=3;
			btnDeleteUser = new JButton("Slett deltager");
			add(btnDeleteUser,constraints);
			
			//
			
			//knapper for godta og slett av avtale
			constraints.gridx=1;
			constraints.gridy=9;
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
	private boolean setTime(){
		String start= spinnerStart.getValue()+"";
		String end = spinnerEnd.getValue()+"";
		Date dateStart = dateChooser.getDate();
		Date dateEnd = dateChooser.getDate();
		if(end.compareTo(start)> 0){
			String[] startSplit = start.split(":");
			String[] endSplit = end.split(":");
			dateStart.setHours(Integer.parseInt(startSplit[0]));
			dateStart.setMinutes(Integer.parseInt(startSplit[1]));

			dateEnd.setHours(Integer.parseInt(endSplit[0]));
			dateEnd.setMinutes(Integer.parseInt(endSplit[1]));

			model.setDateEnd(dateEnd);
			model.setDateStart(dateStart);
			return true;
		}
		else{
//			spinnerEnd.setValue(spinnerStart.getNextValue());
			return false;
		}
	}

	private ArrayList<String> getTimeInterval(){
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j <60; j++) {
				list.add(format.format(i)+":"+format.format(j));
			}
		}

		return list;
	}

	private void setValues(){
		model.setTitle(txtTitle.getText());
		model.setPlace(txtPlace.getText());
		model.setDescription(txtDescription.getText());
		Map<User,Status> userList= new HashMap<User,Status>();

		//adder brukere
		for (int i = 0; i < defaultModel.size(); i++) {
			Entry<User,Status> a = (Entry<User,Status>)defaultModel.get(i);
			userList.put(a.getKey(),a.getValue());
		}

		model.setUserList(userList);
	}

	private void getValues(){
		txtTitle.setText(model.getTitle());
		dateChooser.setDate(model.getDateStart());
		if(model.getRoom()!=null){			
			txtRoom.setText(model.getRoom().getName());
		}

		//tid
		Date start = model.getDateStart();
		String startS = format.format(start.getHours()) + ":" +format.format(start.getMinutes());
		Date end = model.getDateEnd();
		String endS = format.format(end.getHours()) + ":" +format.format(end.getMinutes());
		spinnerStart.setValue(startS);
		spinnerEnd.setValue(endS);

		//sted
		if (model.getPlace()!=null) {			
			txtPlace.setText(model.getPlace());
		}
		//beskrivelse
		txtDescription.setText(model.getDescription());
		//rom
		if(model.getRoom()!=null){
			txtRoom.setText(model.getRoom().getName());
		}
		
		//sett eier
		labOwner.setText("Eier: " + model.getOwner().getName());
		
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
			if(roomtemp != null){
				model.setRoom(roomtemp);
			}
			dispose();
		}
		
		//Button room 
		if (e.getSource() == btnRoom) {
			if(setTime()){
				SelectRoomDialog selectRoom = new SelectRoomDialog(model);
				selectRoom.setLocationRelativeTo(this);
				selectRoom.setVisible(true);
				txtRoom.setText(model.getRoom().getName());
			}
			else{
				labTimeError.setText("Feil i tid");
				labTimeError.setForeground(Color.RED);
			}
		}

		//btnremoveroom
		if (e.getSource()==btnRemoveRoom) {
			if (model.getRoom()!=null) {
				roomtemp = model.getRoom();
				model.setRoom(new Room(""));
				txtRoom.setText("");
			}
		}

		//button add users
		if (e.getSource() == btnAddUser) {
			SelectUserDialog selectUser = new SelectUserDialog(defaultModel,model.getOwner());
			selectUser.setLocationRelativeTo(this);
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
	
	public boolean validateModel() {
		if (txtTitle.getText().equals("")) {
			labTitleError.setText("Mangler tittel");
			labTitleError.setForeground(Color.red);
			return false;
		}
		if(!setTime()){
			labTimeError.setText("Tid er feil");
			labTimeError.setForeground(Color.red);
			return false;
		}
		setValues();
		return true;
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
	public void setDisabled(){
		txtTitle.setEditable(false);
		dateChooser.getCalendarButton().setVisible(false);
		((JTextField)dateChooser.getDateEditor().getUiComponent()).setEditable(false);
		((DefaultEditor)spinnerStart.getEditor()).getTextField().setEditable(false);
		((DefaultEditor)spinnerEnd.getEditor()).getTextField().setEditable(false);
		txtDescription.setEditable(false);
		txtPlace.setEditable(false);
		
		if (!noButtons) {
			btnAddUser.setVisible(false);
			btnDeleteUser.setVisible(false);
			btnRoom.setVisible(false);
			btnConfirm.setVisible(false);
			btnRemoveRoom.setVisible(false);
		}
	}

}



