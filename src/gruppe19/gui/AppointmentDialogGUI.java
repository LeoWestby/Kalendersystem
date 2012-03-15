package gruppe19.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import com.toedter.calendar.JDateChooser;

import gruppe19.model.Appointment;
import gruppe19.model.Room;
import gruppe19.model.User;

public class AppointmentDialogGUI extends JDialog implements ActionListener, ListSelectionListener{

	private Appointment model;

	private GridBagLayout layout;
	private JTextField txtTitle, txtPlace, txtRoom;
	private JButton btnConfirm, btnRoom, btnCancel, btnAddUser, btnDeleteUser;
	private DefaultListModel<User> defaultModel;
	private DefaultListSelectionModel defaultSelectModel;
	private JTextArea txtDescription;
	private GridBagConstraints constraints;
	private DecimalFormat format = new DecimalFormat("00");
	private JSpinner spinnerEnd, spinnerStart;
	private JDateChooser dateChooser;
	private JLabel labTimeError, labTitleError;
	private JList<User> listUsers;
	private Dimension dim = new Dimension(210, 20);


	/**
	 * Create new AppointmentDialogGUI with a {@link Appointment} model set to null
	 */
	public AppointmentDialogGUI() {
		setUp();
	}
	/**
	 * Create new AppointmentDialogGUI with a {@link Appointment} model set to null
	 */
	public AppointmentDialogGUI(Appointment model) {
		this.model=model;
		setUp();
		getValues();
	}

	private void setUp(){
		layout = new GridBagLayout();
		this.setLayout(layout);
		constraints = new GridBagConstraints();
		
		//tittel p� avtale;
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
		constraints.gridx=2;
		btnRoom = new JButton("Finn rom");
		add(btnRoom,constraints);

		
		//legg til deltagere
		constraints.gridx=0;
		constraints.gridy=7;
		add(new JLabel("Deltagere: "),constraints);
		
		constraints.gridx=1;
		defaultModel = new DefaultListModel<User>();
		listUsers = new JList<User>();
		listUsers.setModel(defaultModel);
		listUsers.setCellRenderer(new UserListRenderer());
		
		defaultSelectModel = new DefaultListSelectionModel();
		listUsers.setSelectionModel(defaultSelectModel);
		defaultSelectModel.addListSelectionListener(this);
		JScrollPane scrollUsers = new JScrollPane(listUsers);
		scrollUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollUsers.setPreferredSize(new Dimension(210, 100));
		add(scrollUsers, constraints);

		//legge til knapper
		constraints.gridx=2;
		constraints.gridy=7;
		btnAddUser = new JButton("Legg til deltager");
		add(btnAddUser,constraints);
		constraints.gridx=2;
		constraints.gridy=8;
		btnDeleteUser = new JButton("Slett deltager");
		add(btnDeleteUser,constraints);
		

		
		
		//knapper for godta og slett av avtale
		constraints.gridx=1;
		constraints.gridy=9;
		btnConfirm = new JButton("Legg til/endre avtale");
		add(btnConfirm, constraints);
		constraints.gridx=2;
		constraints.gridy=9;
		btnCancel = new JButton("Avbryt");
		add(btnCancel, constraints);
		
		//setname
		
		//add actionlisteners
		btnRoom.addActionListener(this);
		btnAddUser.addActionListener(this);
		btnConfirm.addActionListener(this);
		btnCancel.addActionListener(this);
		btnDeleteUser.addActionListener(this);
		
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
			String[] endSplit = start.split(":");
			dateStart.setHours(Integer.parseInt(startSplit[0]));
			dateStart.setMinutes(Integer.parseInt(startSplit[1]));

			dateEnd.setHours(Integer.parseInt(endSplit[0]));
			dateEnd.setMinutes(Integer.parseInt(endSplit[1]));

			model.setDateEnd(dateEnd);
			model.setDateStart(dateStart);
			return true;
		}
		else{
			spinnerEnd.setValue(spinnerStart.getNextValue());
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
	}

	private void getValues(){
		txtTitle.setText(model.getTitle());
		dateChooser.setDate(model.getDateStart());
		txtRoom.setText(model.getRoom().getName());


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
		
		
		//Button room 
		if (e.getSource() == btnRoom) {
			if(setTime()){
				SelectRoomDialog selectRoom = new SelectRoomDialog(model);
				selectRoom.setVisible(true);
				txtRoom.setText(model.getRoom().getName());
			}
			else{
				labTimeError.setText("Feil i tid");
				labTimeError.setForeground(Color.RED);
			}
		}
		
		//button add users
		if (e.getSource() == btnAddUser) {
			SelectUserDialog selectUser = new SelectUserDialog(defaultModel);
			selectUser.setVisible(true);
		}
		
		//button delete user
		if(e.getSource()==btnDeleteUser){
			if(!defaultSelectModel.isSelectionEmpty()){
				int i= defaultSelectModel.getAnchorSelectionIndex();
				defaultModel.remove(i);
			}
		}
		
		//button confirm
		if(e.getSource()==btnConfirm){
			if (txtTitle.getText().equals("")) {
				labTitleError.setText("En avtale m� ha en tittel");
				labTitleError.setForeground(Color.red);
				return;
			}
			
			getValues();	
		}
		
		//button cancel
		if (e.getSource() == btnCancel) {
			dispose();
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	
	
	public static void main(String[] args) {
		Appointment app = new Appointment();
		app.setTitle("En avtale");
		app.setDateStart(new Date(1000000000));
		app.setDateEnd(new Date(10000000));
		Room rom = new Room("Rommet");
		app.setRoom(rom);
		AppointmentDialogGUI gui = new AppointmentDialogGUI(app);
		gui.setVisible(true);
	}
	

}



