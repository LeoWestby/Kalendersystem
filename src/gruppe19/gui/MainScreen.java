package gruppe19.gui;

import gruppe19.model.Appointment;
import gruppe19.model.User;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.toedter.calendar.JCalendar;

public class MainScreen extends JFrame {
	private final User loggedInUser;
	private final MainMenu menu;
	private final JCalendar miniCalendar;
	private final CalendarView calendar;
	public final JLabel calendarHeader;
	
	private class MainMenu extends JPanel {
		private final int XPOS = 47;
		private final int YPOS = 39;
		private final JLabel firstName = new JLabel(loggedInUser.getfirstname()), 
							lastName = new JLabel(loggedInUser.getLastname());
		
		private final JButton createMeeting = new JButton("Kall inn til møte"),
							createAppointment = new JButton("Opprett avtale"), 
							myMeetings = new JButton("Mine møter"),
							invitations = new JButton("Invitasjoner"),
							importCalendar = new JButton("Importer kalendere");
		
		public MainMenu() {
			final int spaceBetweenButtons = 5,
						spaceBetweenButtonsAndName = 40;
			
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
			
			//TODO: Set height properly
			setSize(Math.max(lastName.getWidth(), importCalendar.getWidth()), 9999);
			setLocation(XPOS, YPOS);
		}
	}
	
	public MainScreen(User user) {
		setLayout(null);
		setSize(1280, 720);
		
		loggedInUser = user;
		calendarHeader = new JLabel(new ImageIcon(getClass().getResource("/gruppe19/client/images/CalendarHead.png")));
		calendarHeader.setSize(calendarHeader.getPreferredSize());
		calendar = new CalendarView(this);
		menu = new MainMenu();
		miniCalendar = new JCalendar();

		calendarHeader.setLocation(calendar.getX() + 1, 0);
		add(calendarHeader);
		add(menu);
		add(calendar);
		
		setTitle("Kalendersystem - Hovedskjerm");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		getContentPane().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				calendar.setPos();
				calendarHeader.setLocation(calendar.getX() + 1, 0);
			}
		});
		
		//Add some example appointments
		Appointment a1 = new Appointment();
		Appointment a2 = new Appointment();
		
		
		a1.setTitle("Forelesing");
		a2.setTitle("Shopping");
		
		a1.setPlace("Skolen");
		a2.setPlace("På butikken");
		
		a1.setDateStart(new Date(112, 2, 21, 12, 00, 00));
		a1.setDateEnd(new Date(112, 2, 21, 14, 00, 00));
		
		a2.setDateStart(new Date(112, 2, 23, 16, 30, 00));
		a2.setDateEnd(new Date(112, 2, 23, 20, 00, 00));
		
		calendar.addAppointment(a1);
		calendar.addAppointment(a2);
	}
	
	
	public static void main(String[] args) {
		new MainScreen(new User("Hans", "Hansen"));
	}
}