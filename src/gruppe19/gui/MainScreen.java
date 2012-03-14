package gruppe19.gui;

import java.awt.BorderLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDayChooser;
import com.toedter.plaf.JCalendarTheme;

public class MainScreen extends JFrame {
	public MainScreen() {
		setLayout(new BorderLayout());
		
		add(new JDateChooser(), BorderLayout.NORTH);
		add(new JLabel("J_CALENDAR: "), BorderLayout.CENTER);
		add(new JCalendar(), BorderLayout.SOUTH);
		
		setSize(327, 600);
		setTitle("Kalendersystem");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args) {
		new MainScreen();
	}
}
