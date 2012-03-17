package gruppe19.gui;

import gruppe19.model.Appointment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Calendar;
import java.util.Date;

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
	private final JPanel calendarPanel;
	private final MainScreen outerFrame;
	private final Image calendarImage;
	
	private class AppointmentWidget extends JPanel {
		public final Appointment appointment;
		
		public AppointmentWidget(Appointment a) {
			appointment = a;
			setBackground(Color.GREEN);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(new JLabel(appointment.getTitle()));
			add(Box.createVerticalStrut(30));
			add(new JLabel(appointment.getPlace()));
		}
	}
	
	public CalendarView(MainScreen outerFrame) {
		this.outerFrame = outerFrame;
		calendarImage = new ImageIcon(getClass().getResource("/gruppe19/client/images/CalendarHeadless.png")).getImage();
		calendarPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(calendarImage, 0, 0, null);
				super.paintComponent(g);
			}
		};
		
		calendarPanel.setOpaque(false);
		calendarPanel.setLayout(null);
		calendarPanel.setSize(outerFrame.calendarHeader.getWidth(), 
				calendarImage.getHeight(null));
		calendarPanel.setPreferredSize(calendarPanel.getSize());
		setViewportView(calendarPanel);
		setPos();
		//Increase scroll bar speed 16x
		getVerticalScrollBar().setUnitIncrement(16);
		//Set slider position to 07:00
		getVerticalScrollBar().setValue(colHeight * 14);
	}
	
	/**
	 * Sets size and location according to the position of the outer frame.
	 */
	public void setPos() {
		getVerticalScrollBar().setValue(colHeight * 14);
		setSize(outerFrame.calendarHeader.getWidth() + 18, 
				outerFrame.getHeight() - 65);
		setLocation(outerFrame.getWidth() - getWidth() - 20, 
				outerFrame.calendarHeader.getHeight());
	}
	
	public void addAppointment(Appointment a) {
		AppointmentWidget widget = new AppointmentWidget(a);
		
		setWidgetLocAndPos(widget);
		calendarPanel.add(widget);
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