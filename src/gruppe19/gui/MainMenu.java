package gruppe19.gui;

import gruppe19.client.ktn.ServerAPI;
import gruppe19.model.Appointment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenu extends JPanel {
	private final int XPOS = 47;
	private final int YPOS = 39;
	private final MainScreen madeBy;
	private final JLabel firstName, lastName;
	
	private final JButton createMeeting = new JButton("Kall inn til møte"),
						createAppointment = new JButton("Opprett avtale"), 
						myMeetings = new JButton("Mine møter"),
						invitations = new JButton("Invitasjoner"),
						importCalendar = new JButton("Importer kalendere");
	
	public MainMenu(MainScreen madeBy) {
		final int spaceBetweenButtons = 5,
					spaceBetweenButtonsAndName = 40;
		this.madeBy = madeBy;
		firstName = new JLabel(madeBy.getUser().getfirstname());
		lastName = new JLabel(madeBy.getUser().getLastname());
		
		//First name and last name should be added separately. Combined for now
		lastName.setText(firstName.getText() + " " + lastName.getText());
		
		setLayout(null);
		lastName.setSize(lastName.getPreferredSize());
		createMeeting.setSize(importCalendar.getPreferredSize());
		createAppointment.setSize(importCalendar.getPreferredSize());
		myMeetings.setSize(importCalendar.getPreferredSize());
		invitations.setSize(importCalendar.getPreferredSize());
		importCalendar.setSize(importCalendar.getPreferredSize());
		
		lastName.setLocation(0, 0);
		createMeeting.setLocation(0, lastName.getY() + spaceBetweenButtonsAndName);
		createAppointment.setLocation(0, createMeeting.getHeight() + createMeeting.getY() + spaceBetweenButtons);
		myMeetings.setLocation(0, createAppointment.getHeight() + createAppointment.getY() + spaceBetweenButtons);
		invitations.setLocation(0, myMeetings.getHeight() + myMeetings.getY() + spaceBetweenButtons);
		importCalendar.setLocation(0, invitations.getHeight() + invitations.getY() + spaceBetweenButtons);
		
		add(lastName);
		add(createMeeting);
		add(createAppointment);
		add(myMeetings);
		add(invitations);
		add(importCalendar);
		
		createMeeting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Appointment newApp = new Appointment();
				newApp.setOwner(MainMenu.this.madeBy.getUser());
				new AppointmentDialogGUI(newApp, MainMenu.this.madeBy.getUser());
				
				//Check if dialog was cancelled
				if (newApp.getTitle() != null) {
					MainMenu.this.madeBy.getCalendar()
					.addAppointment(ServerAPI.createAppointment(newApp));
				}
			}
		});
		
		//Height should probably be set to a proper value, but it isn't needed
		setSize(Math.max(lastName.getWidth(), importCalendar.getWidth()), 9999);
		setLocation(XPOS, YPOS);
	}
}