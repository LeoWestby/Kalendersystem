package gruppe19.gui;

import gruppe19.client.ktn.ServerAPI;
import gruppe19.model.Appointment;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CalendarView extends JScrollPane {
	private final static int 	colHeight = 28,
								oneHourHeight = colHeight * 2,
								//indentWidth = 89,
								lineWidth = 2,
								col1Start = 88,
								col2Start = 214,
								col3Start = 329,
								col4Start = 448,
								col5Start = 572,
								col6Start = 681,
								col7Start = 792,
								col7End = 910;
	private final static Image calendarImage;
	private final JPanel calendarPanel;
	private final List<Appointment> appointments; 
	private Date currentDate;
	
	static {
		calendarImage = new ImageIcon(
				CalendarView.class.getResource("/gruppe19/client/images/CalendarHeadless.png")
							).getImage();
	}
	
	private static class AppointmentWidget extends JPanel {
		public Appointment appointment;
		
		public AppointmentWidget(Appointment a) {
			appointment = a;
					
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			setBackground(Color.GREEN);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(new JLabel(appointment.getTitle()));
			add(Box.createVerticalStrut(30));
			add(new JLabel(appointment.getPlace()));
			
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					Appointment newApp = appointment;
					new AppointmentDialogGUI(newApp, MainScreen.getUser());
					//Check if dialog was cancelled
					if (!newApp.getTitle().equals("")) {
						ServerAPI.updateAppointment(newApp);
					}
				}
			});
		}
	}
	
	public CalendarView(Date currentDate) {
		this.currentDate = currentDate;
		appointments = new ArrayList<Appointment>();
		calendarPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(calendarImage, 0, 0, null);
				super.paintComponent(g);
			}
		};
		
		calendarPanel.setOpaque(false);
		calendarPanel.setLayout(null);
		calendarPanel.setSize(calendarImage.getWidth(null), 
								calendarImage.getHeight(null));
		calendarPanel.setPreferredSize(calendarPanel.getSize());
		setViewportView(calendarPanel);
		//Increase scroll bar speed 16x
		getVerticalScrollBar().setUnitIncrement(16);
		//Set slider position to 07:00
		getVerticalScrollBar().setValue(colHeight * 14);
		//For some reason, it has to be called twice to have any effect at all...
		getVerticalScrollBar().setValue(colHeight * 14);
	}
	
	/**
	 * Adds an appointment to the calendar.
	 * 
	 * @param a The appointment to add.
	 */
	public void addAppointment(Appointment a) {
		appointments.add(a);
		repaintAppointments();
	}
	
	public void removeAppointment(int ID) {
		Appointment[] tmp = new Appointment[appointments.size()];
		appointments.toArray(tmp);
		
		for (Appointment a : tmp) {
			if (a.getID() == ID) {
				appointments.remove(a);
			}
		}
		repaintAppointments();
	}
	
	/**
	 * Sets the calendar to the specified date.
	 * 
	 * @param date The date to which the calendar will be set.
	 * If <code>null</code>, today's date will be used.
	 */
	public void setDate(Date date) {
		currentDate = date;
		repaintAppointments();
	}
	
	/**
	 * @return The date the calendar is currently set to.
	 */
	public Date getDate() {
		return currentDate;
	}
	
	public List<Appointment> getAppointments() {
		return appointments;
	}

	/**
	 * Adds all appointments of the current week to the calendar.
	 */
	public void repaintAppointments() {
		Calendar cal = new GregorianCalendar();
		Calendar current = new GregorianCalendar();
		
		current.setTime(currentDate);
		int currentWeek = current.get(Calendar.WEEK_OF_YEAR);
		int currentYear = current.get(Calendar.YEAR);
		
		//Clear the calendar
		calendarPanel.removeAll();
		
		for (Appointment a : appointments) {
			cal.setTime(a.getDateStart());
			
			if (cal.get(Calendar.WEEK_OF_YEAR) == currentWeek
					&& cal.get(Calendar.YEAR) == currentYear) {
				AppointmentWidget widget = new AppointmentWidget(a);
				
				setWidgetLocAndPos(widget);
				calendarPanel.add(widget);
			}
		}
		calendarPanel.revalidate();
		calendarPanel.repaint();
	}
	
	/**
	 * Gets the current week of the calendar.
	 */
	public int getCurrentWeek() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(currentDate);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	@SuppressWarnings("deprecation")
	private static void setWidgetLocAndPos(AppointmentWidget widget) {
		int x = 0, y = 0, width = 0;
		Date start = widget.appointment.getDateStart();
		Date end = widget.appointment.getDateEnd();
		double startInHours =	start.getHours() +
								start.getMinutes() / 60.0 +
								start.getSeconds() / 3600.0;
		double endInHours =		end.getHours() +
								end.getMinutes() / 60.0 +
								end.getSeconds() / 3600.0;
		double duration = endInHours - startInHours;

		switch (start.getDay()) {
			case Calendar.MONDAY - 1:
				x = col1Start;
				width = col2Start - col1Start - lineWidth;
				break;
			case Calendar.TUESDAY - 1:
				x = col2Start;
				width = col3Start - col2Start - lineWidth;
				break;
			case Calendar.WEDNESDAY - 1:
				x = col3Start;
				width = col4Start - col3Start - lineWidth;
				break;
			case Calendar.THURSDAY - 1:
				x = col4Start;
				width = col5Start - col4Start - lineWidth;
				break;
			case Calendar.FRIDAY - 1:
				x = col5Start;
				width = col6Start - col5Start - lineWidth;
				break;
			case Calendar.SATURDAY - 1:
				x = col6Start;
				width = col7Start - col6Start - lineWidth;
				break;
			case Calendar.SUNDAY - 1:
				x = col7Start;
				width = col7End - col7Start;
				break;
		}
		y = (int)(startInHours * oneHourHeight) - 1;
		
		if (y < 0) {
			y = 0;
		}
		widget.setLocation(x, y);
		widget.setSize(width, (int)(oneHourHeight * duration));
	}
}
