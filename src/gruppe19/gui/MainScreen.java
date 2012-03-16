package gruppe19.gui;

import gruppe19.model.User;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDayChooser;
import com.toedter.plaf.JCalendarTheme;

public class MainScreen extends JFrame {
	private final User loggedInUser;
	private final MainMenu menu;
	private final JCalendar miniCalendar;
	private final JPanel calendar;
	
	private class MainMenu extends JPanel {
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
			
			//First name and last name should be added seperately. Combined for now
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
		}
	}
	
	public MainScreen(User user) {
		loggedInUser = user;
		menu = new MainMenu();
		miniCalendar = new JCalendar();
		calendar = new JPanel();
		calendar.setLayout(new BorderLayout());
		calendar.add(new JLabel(new ImageIcon(getClass().getResource("/gruppe19/client/images/Calendar.png"))), BorderLayout.CENTER);
		JScrollPane p = new JScrollPane(calendar);
		//Build the calendar
		setSize(1280, 720);
		calendar.setSize(900, 900);
		calendar.setLocation(50, 0);
		p.setSize(p.getPreferredSize().width + 15, 720);
		p.setLocation(getWidth() - p.getPreferredSize().width - 31, 0);
		add(p);
		p.setLayout(null);
		JLabel l = new JLabel("TEST");
		l.setBounds(0, 600, 100, 20);
		p.add(l);
		
		menu.setLocation(47, 39);
		add(menu);
		
		setLayout(null);		
		
		setTitle("Kalendersystem - Hovedskjerm");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args) {
		new MainScreen(new User("Hans", "Hansen"));
	}
}