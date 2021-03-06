package gruppe19.client.gui;

import gruppe19.client.gui.CalendarView.AppointmentWidget;
import gruppe19.client.ktn.ServerAPI;
import gruppe19.client.ktn.ServerAPI.Status;
import gruppe19.model.Appointment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The main menu displayed on a main screen. 
 */
public class MainMenu extends JPanel {
	/**
	 * The main screen containing the main menu.
	 */
	private final MainScreen madeBy;
	private final int XPOS = 47;
	private final int YPOS = 39;
	private final JLabel firstName, lastName;
	
	private final JButton createMeeting = new JButton("Opprett avtale"),
							myMeetings = new JButton("Mine m�ter"),
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
				GregorianCalendar timeNow = new GregorianCalendar();
				final Appointment newApp = new Appointment();
				
				timeNow.setTime(newApp.getDateStart());
				timeNow.add(Calendar.HOUR_OF_DAY, 1);
				newApp.setDateEnd(timeNow.getTime());
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
		
		importCalendar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<AppointmentWidget> imported =
						new ArrayList<AppointmentWidget>();
				new CalendarImportDialog(imported);
				
				if (imported.isEmpty()) {
					return;
				}
				
				if (imported.get(0) == null) {
					//Reset imported calendars
					MainMenu.this.madeBy.getCalendar()
					.getImportedAppointments()
					.clear();
				}
				else {
					MainMenu.this.madeBy.getCalendar()
					.getImportedAppointments()
					.addAll(imported);
				}
				MainMenu.this.madeBy.getCalendar()
				.repaintAppointments();
			}
		});
		
		//Height should probably be set to a proper value, but it isn't needed
		setSize(Math.max(lastName.getWidth(), importCalendar.getWidth()), 9999);
		setLocation(XPOS, YPOS);
	}
	
	/**
	 * Updates the invitation count on the invitation button.
	 */
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
	
	/**
	 * Updates the rejected meetings count on the my meetings button.
	 */
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
			myMeetings.setText("Mine m�ter");
		}
		else {
			myMeetings.setText("<html>Mine m�ter " +
					"<font color='red'>(" + count + ")");
		}
	}
}