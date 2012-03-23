package gruppe19.gui;

import gruppe19.model.Appointment;
import gruppe19.model.Room;
import gruppe19.model.User;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyAppointments extends JPanel {

//	private JList listAppointments;
//	private DefaultListModel defaultListModel;
//	private DefaultListSelectionModel defaultListSelectionModel;
//	private JButton btnClose;
//	private JButton btnAdd;
//	private User user;
//
//	public MyAppointments(User user) {
//		// TODO Auto-generated constructor stub
//		this.user = user;
//		setUp();
//		getAppointments();
//		
//	}
//	
//	private void setUp(){
//		setLayout(new FlowLayout());
//		//User list
//		defaultListModel = new DefaultListModel();
//		listAppointments = new JList();
//		listAppointments.setModel(defaultListModel);
//		listAppointments.setCellRenderer(new AppointmentListRenderer());
//		defaultListSelectionModel = new DefaultListSelectionModel();
//		listAppointments.setSelectionModel(defaultListSelectionModel);
//		defaultListSelectionModel.addListSelectionListener(this);
//		JScrollPane scrollUsers = new JScrollPane(listAppointments);
//		scrollUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		
//		btnClose = new JButton("Lukk");
//		btnAdd = new JButton("Se på avtale");
//		
//		add(scrollUsers);
//		add(btnAdd);
//		add(btnClose);
//		
//		defaultListSelectionModel.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
//		//actionlisteners
//		btnAdd.addActionListener(this);
//		btnClose.addActionListener(this);
//		
//		//behaviour
//		setModalityType(ModalityType.APPLICATION_MODAL);
//		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		pack();
//	}
//	
//	private void getAppointments(){
//		ArrayList<Appointment> appoints = new ArrayList<Appointment>();
//		User b = new User("lol", "lol");
//		b.setUsername("lol");
//		for (int i = 0; i < 5; i++) {
//			defaultListModel.addElement(new Appointment(i,"Avtale "+ i, new Date(), new Date(), null, user, null, null, null));
//		}
//		for (int i = 5; i < 10; i++) {
//			defaultListModel.addElement(new Appointment(i,"Avtale "+ i, new Date(), new Date(), null, b, null, null, null));
//		}
//	}
//	public static void main(String[] args) {		
//		User a = new User("Vegard", "Harper");
//		a.setUsername("vegahar");
//		MyAppointments b = new MyAppointments(a);
//		b.setVisible(true);
//	}
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		int i = defaultListSelectionModel.getAnchorSelectionIndex();
//		Appointment app = (Appointment)defaultListModel.get(i);
//		AppointmentDialogGUI gui = new AppointmentDialogGUI(app, user);
//		gui.setVisible(true);
//		
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void valueChanged(ListSelectionEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
	
	

	
	
	private ArrayList<AppoinmentsButton> invites;
	private JLabel lblInvitations;
	private GridBagConstraints c;
	
	
	class AppoinmentsButton extends JPanel implements ActionListener{
		private JLabel lblDescription;
		private JButton btnHyperlink;
		
		public AppoinmentsButton(Appointment appointment) {
			btnHyperlink=new JButton();
			btnHyperlink.setText("<html><u>"+appointment.getTitle()+"</u></html>");
			btnHyperlink.setBorderPainted(false);
			btnHyperlink.setForeground(Color.BLUE);
			btnHyperlink.setContentAreaFilled(false);
			btnHyperlink.addActionListener(this);
			add(btnHyperlink);
			
			String []months={"Januar","Februar","Mars","April","Mai","Juni","Juli","August","September","Oktober","November","Desember"};
			lblDescription=new JLabel();
			String txt=String.format("%d. %s, kl. %d:%d, %s", appointment.getDateStart().getDate(),months[appointment.getDateStart().getMonth()],appointment.getDateStart().getHours(),appointment.getDateStart().getMinutes(),appointment.getRoom()!=null?"rom "+appointment.getRoom().getName():appointment.getPlace());
			lblDescription.setText(txt);
			add(lblDescription,c);
			
		}

		public void actionPerformed(ActionEvent e) {
			System.out.println("Klikk");
		}
		
	}
	
	
	
	public MyAppointments(ArrayList<Appointment> appointments){
		setLayout(new GridBagLayout());
		c=new GridBagConstraints();
		c.gridy=0;
		c.gridx=0;
		lblInvitations = new JLabel();
		lblInvitations.setText("Nye møteinvitasjoner:");
		c.gridx++;
		c.gridy++;
		add(lblInvitations,c);
//		add(new JSeparator(JSeparator.HORIZONTAL));

//		add(Box.createHorizontalStrut(50));
		
		int i =1;
		
		for (Appointment appointment : appointments) {
			c.gridx = 0;
			JLabel index = new JLabel();
			c.gridy++;
			index.setText(""+i+".");
			add(index, c);
			c.gridx++;
			add(new AppoinmentsButton(appointment),c);
			c.gridy++;
			i++;
			
			
		}
	}
		
		
		
		public static void main(String[]args){
			JFrame frame=new JFrame("Det er her det skjer");
			ArrayList<Appointment> avtaler= new ArrayList<Appointment>();
			
			
			avtaler.add(new Appointment(2, "Avtale", new Date(), new Date(), "Kiosken på hjørnet", new User("Bjarne"), new Room("101"), null, "Kjøpe mat"));
			avtaler.add(new Appointment(3, "Avtale", new Date(), new Date(), "Kiosken på hjørnet", new User("Bjarne"), new Room("101"), null, "Kjøpe mat"));
			
			frame.getContentPane().add(new MyAppointments(avtaler));
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
		
		
	}