package gruppe19.gui;

import gruppe19.client.ktn.ServerAPI.Status;
import gruppe19.model.Appointment;
import gruppe19.model.Room;
import gruppe19.model.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.sound.sampled.ReverbType;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class Invitations extends JDialog{
	private ArrayList<InviteButton> invites;
	private List<Appointment> appointments;
	private JLabel lblInvitations;
	private GridBagConstraints c;
	
	
	class InviteButton extends JPanel implements ActionListener{
		private JButton btnAccept;
		private JButton btnDecline;
		private JLabel lblDescription;
		
		public InviteButton(Appointment appointment) {
			lblDescription=new JLabel();
			lblDescription.setText(appointment.getTitle());
			add(lblDescription,c);
			
			
			c.gridy ++;
			btnAccept=new JButton();
			btnAccept.setText("Godta");
			btnAccept.setBackground(Color.GREEN);
			add(btnAccept,c);
			
			btnDecline=new JButton();
			btnDecline.setText("Avslå");
			btnDecline.setBackground(Color.RED);
			add(btnDecline,c);
		}

		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
	
	
	
	public Invitations(List<Appointment> list){
		setLayout(new GridBagLayout());
		this.appointments = list;
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setLocationRelativeTo(null);
		
	}
	
	@Override
	public void setVisible(boolean b) {
		if (b) {
			removeAll();
			
			c=new GridBagConstraints();
			c.gridy=0;
			c.gridx=0;
			lblInvitations = new JLabel();
			lblInvitations.setText("Nye møteinvitasjoner:");
			c.gridx++;
			c.gridy++;
			add(lblInvitations,c);
			
			//Add all appointments where your status is pending to the list
			for (Appointment appointment : appointments) {
				for (Entry<User, Status> entry : appointment.getUserList().entrySet()) {
					if (entry.getKey().equals(MainScreen.getUser())
							&& entry.getValue() == Status.PENDING) {
						add(new InviteButton(appointment),c);
						c.gridy++;
					}
				}
			}
		}
		pack();
		validate();
		repaint();
		super.setVisible(b);
	}
}
