package gruppe19.gui;

import gruppe19.client.ktn.ServerAPI;
import gruppe19.client.ktn.ServerAPI.Status;
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
	
	private final JButton createMeeting = new JButton("Opprett avtale"),
						myMeetings = new JButton("Mine møter"),
						invitations = new JButton("Invitasjoner"),
						importCalendar = new JButton("Importer kalendere");
	
	public MainMenu(MainScreen madeBy) {
		final int spaceBetweenButtons = 5,
					spaceBetweenButtonsAndName = 40;
		this.madeBy = madeBy;
		firstName = new JLabel(MainScreen.getUser().getfirstname());
		lastName = new JLabel(MainScreen.getUser().getLastname());
		
		//First name and last name should be added separately. Combined for now
		lastName.setText(firstName.getText() + " " + lastName.getText());
		
		setLayout(null);
		lastName.setSize(lastName.getPreferredSize());
		createMeeting.setSize(importCalendar.getPreferredSize());
		myMeetings.setSize(importCalendar.getPreferredSize());
		invitations.setSize(importCalendar.getPreferredSize());
		importCalendar.setSize(importCalendar.getPreferredSize());
		
		lastName.setLocation(0, 0);
		createMeeting.setLocation(0, lastName.getY() + spaceBetweenButtonsAndName);
		myMeetings.setLocation(0, createMeeting.getHeight() + createMeeting.getY() + spaceBetweenButtons);
		invitations.setLocation(0, myMeetings.getHeight() + myMeetings.getY() + spaceBetweenButtons);
		importCalendar.setLocation(0, invitations.getHeight() + invitations.getY() + spaceBetweenButtons);
		
		add(lastName);
		add(createMeeting);
		add(myMeetings);
		add(invitations);
		add(importCalendar);
		
		createMeeting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Appointment newApp = new Appointment();
				newApp.setOwner(MainScreen.getUser());
				final AppointmentDialogGUI appGUI = 
						new AppointmentDialogGUI(newApp, MainScreen.getUser(), false);
				
				appGUI.addConfirmButtonListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (appGUI.validateModel()) {
							ServerAPI.createAppointment(newApp);
							appGUI.dispose();
						}
					}
				});
				appGUI.setLocationRelativeTo(null);
				appGUI.setVisible(true);
			}
		});
		
		invitations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Invitations(MainMenu.this.madeBy.getCalendar().getAppointments())
				.setLocationRelativeTo(MainMenu.this.madeBy);
			}
		});
		
		myMeetings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MyAppointments(MainMenu.this.madeBy)
				.setLocationRelativeTo(MainMenu.this.madeBy);
			}
		});
		
		//Height should probably be set to a proper value, but it isn't needed
		setSize(Math.max(lastName.getWidth(), importCalendar.getWidth()), 9999);
		setLocation(XPOS, YPOS);
	}
	
	public void updateInvitationCount() {
		int count = 0;
		
		for (Appointment a : madeBy.getCalendar().getAppointments()) {
			if (a.getUserList().get(MainScreen.getUser()) == Status.PENDING) {
				count++;
			}
		}
		
		if (count == 0) {
			invitations.setText("Invitasjoner");
		}
		else {
			invitations.setText("<html>Invitasjoner " +
									"<font color='red'>(" + count + ")");
		}
	}
	
	public void updateRejectedMeetingsCount() {
		int count = 0;
		
		for (Appointment a : madeBy.getCalendar().getAppointments()) {
			if (a.getOwner().equals(MainScreen.getUser()) ||
					a.getUserList().get(MainScreen.getUser()) != Status.REJECTED) {
				for (Status s : a.getUserList().values()) {
					if (s == Status.REJECTED) {
						count++;
						break;
					}
				}
			}
		}
		
		if (count == 0) {
			myMeetings.setText("Mine møter");
		}
		else {
			myMeetings.setText("<html>Mine møter " +
					"<font color='red'>(" + count + ")");
		}
	}
}
