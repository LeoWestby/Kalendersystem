package gruppe19.gui;

import gruppe19.model.Appointment;
import gruppe19.model.Room;
import gruppe19.model.User;

import java.awt.FlowLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MyAppointments extends JDialog implements ListSelectionListener, ActionListener{

	private JList listAppointments;
	private DefaultListModel defaultListModel;
	private DefaultListSelectionModel defaultListSelectionModel;
	private JButton btnClose;
	private JButton btnAdd;
	private User user;

	public MyAppointments(User user) {
		// TODO Auto-generated constructor stub
		this.user = user;
		setUp();
		getAppointments();
		
	}
	
	private void setUp(){
		setLayout(new FlowLayout());
		//User list
		defaultListModel = new DefaultListModel();
		listAppointments = new JList();
		listAppointments.setModel(defaultListModel);
		listAppointments.setCellRenderer(new AppointmentListRenderer());
		defaultListSelectionModel = new DefaultListSelectionModel();
		listAppointments.setSelectionModel(defaultListSelectionModel);
		defaultListSelectionModel.addListSelectionListener(this);
		JScrollPane scrollUsers = new JScrollPane(listAppointments);
		scrollUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		btnClose = new JButton("Lukk");
		btnAdd = new JButton("Se på avtale");
		
		add(scrollUsers);
		add(btnAdd);
		add(btnClose);
		
		defaultListSelectionModel.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		//actionlisteners
		btnAdd.addActionListener(this);
		btnClose.addActionListener(this);
		
		//behaviour
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
	}
	
	private void getAppointments(){
		ArrayList<Appointment> appoints = new ArrayList<Appointment>();
		User b = new User("lol", "lol");
		b.setUsername("lol");
		for (int i = 0; i < 5; i++) {
			defaultListModel.addElement(new Appointment(i,"Avtale "+ i, new Date(), new Date(), null, user, null, null, null));
		}
		for (int i = 5; i < 10; i++) {
			defaultListModel.addElement(new Appointment(i,"Avtale "+ i, new Date(), new Date(), null, b, null, null, null));
		}
	}
	public static void main(String[] args) {		
		User a = new User("Vegard", "Harper");
		a.setUsername("vegahar");
		MyAppointments b = new MyAppointments(a);
		b.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int i = defaultListSelectionModel.getAnchorSelectionIndex();
		Appointment app = (Appointment)defaultListModel.get(i);
		AppointmentDialogGUI gui = new AppointmentDialogGUI(app, user);
		gui.setVisible(true);
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
