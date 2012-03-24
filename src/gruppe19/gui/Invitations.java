package gruppe19.gui;

import gruppe19.client.ktn.ServerAPI;
import gruppe19.client.ktn.ServerAPI.Status;
import gruppe19.model.Appointment;
import gruppe19.model.Room;
import gruppe19.model.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Invitations extends JDialog{
	private List<Appointment> appointments;
	private JLabel lblInvitations;
	private GridBagConstraints c;
	
	class InviteButton extends JPanel{
		private JButton btnAccept;
		private JButton btnDecline;
		private JLabel lblDescription;
		
		public InviteButton(final Appointment appointment) {
			setLayout(new BorderLayout());
			lblDescription=new JLabel();
			btnAccept=new JButton();
			btnDecline=new JButton();
						
			lblDescription.setText("<html><u><font size='3' color='blue'>" 
									+ appointment.getTitle());
			lblDescription.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			add(lblDescription,BorderLayout.NORTH);
			
			JPanel wrapper=new JPanel();
			wrapper.setDefaultLocale(lblDescription.getLocale());
			wrapper.setBorder(new EmptyBorder(10,110,5,20));
			add(wrapper,BorderLayout.SOUTH);
			
			c.gridy ++;
			btnAccept.setText("Godta");
			btnAccept.setBackground(Color.GREEN);
			add(btnAccept,BorderLayout.WEST);
			
			btnDecline.setText("Avslå");
			btnDecline.setBackground(Color.RED);
			add(btnDecline,BorderLayout.EAST);

			lblDescription.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					AppointmentDialogGUI appGUI = 
							new AppointmentDialogGUI
									(appointment, MainScreen.getUser(), true);
					appGUI.setLocationRelativeTo(Invitations.this);
					appGUI.setVisible(true);
				}
			});
			
			btnAccept.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					appointment.getUserList().put(
							MainScreen.getUser(), Status.APPROVED);
					ServerAPI.updateAppointment(appointment);

					if (addAppointments() == 0) {
						dispose();
					}
				}
			});
			
			btnDecline.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					appointment.getUserList().put(
							MainScreen.getUser(), Status.REJECTED);
					ServerAPI.updateAppointment(appointment);

					if (addAppointments() == 0) {
						dispose();
					}
				}
			});
		
		}
	}
	
	/**
	 * @return The number of appointments added.
	 */
	private int addAppointments() {
		int appointmentsAdded = 0;
		c=new GridBagConstraints();
		lblInvitations = new JLabel();
		getContentPane().removeAll();
		
		c.gridy=0;
		c.gridx=0;
		lblInvitations.setText("Nye møteinvitasjoner:");
		c.gridx++;
		c.gridy++;
		
		add(lblInvitations,c);
		
		c.gridx++;
		c.gridy++;
		
		//Add all appointments where your status is pending to the list	
		try {
			for (Appointment appointment : appointments) {
				for (Entry<User, Status> entry : appointment.getUserList().entrySet()) {
					if (entry.getKey().equals(MainScreen.getUser())
							&& entry.getValue() == Status.PENDING) {
						add(new InviteButton(appointment),c);
						c.gridy++;
						appointmentsAdded++;
					}
				}
			}
		}
		catch (ConcurrentModificationException e) {
			//This exception is thrown at "random" times. No idea why
			return addAppointments();
		}
		
		if (appointmentsAdded == 0) {
			lblInvitations.setText("Ingen nye møteinvitasjoner");
		}
		pack();
		setVisible(true);
		return appointmentsAdded;
	}
	

	public Invitations(List<Appointment> list){
		appointments=list;
		setLayout(new GridBagLayout());
		addAppointments();
	}
}

