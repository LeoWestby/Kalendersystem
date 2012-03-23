package gruppe19.gui;

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

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Invitations extends JPanel{
	private ArrayList<InviteButton> invites;
	private JLabel lblInvitations;
	private GridBagConstraints c;
	
	
	class InviteButton extends JPanel implements ActionListener{
		private JButton btnAccept;
		private JButton btnDecline;
		private JLabel lblDescription;
		
		public InviteButton(Appointment appointment) {
			setLayout(new BorderLayout());
			
			
			lblDescription=new JLabel();
			lblDescription.setText(appointment.getTitle());
			add(lblDescription,BorderLayout.NORTH);
			
			
			c.gridy ++;
			btnAccept=new JButton();
			btnAccept.setText("Godta");
			btnAccept.setBackground(Color.GREEN);
			add(btnAccept,BorderLayout.WEST);
			
			btnDecline=new JButton();
			btnDecline.setText("Avslå");
			btnDecline.setBackground(Color.RED);
			add(btnDecline,BorderLayout.EAST);
			JPanel wrapper=new JPanel();
			wrapper.setDefaultLocale(lblDescription.getLocale());
			wrapper.setBorder(new EmptyBorder(20,150,20,20));
			add(wrapper,BorderLayout.SOUTH);

		}

		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
	
	
	
	public Invitations(ArrayList<Appointment> appointments){
		setLayout(new GridBagLayout());
		c=new GridBagConstraints();
		c.gridy=0;
		c.gridx=0;
		lblInvitations = new JLabel();
		lblInvitations.setText("Nye møteinvitasjoner:");
		c.gridx++;
		c.gridy++;
		
		
		add(lblInvitations,c);
//		add(lblInvitations,BorderLayout.NORTH);

		for (Appointment appointment : appointments) {
			
			add(new InviteButton(appointment),c);
//			add(new InviteButton(appointment),BorderLayout.SOUTH);
			c.gridy++;
			
		}
	}
	
	public static void main(String[]args){
		JFrame frame=new JFrame("Det er her det skjer");
		ArrayList<Appointment> avtaler= new ArrayList<Appointment>();
		
		
		avtaler.add(new Appointment(2, "Avtale", new Date(), new Date(), "Kiosken på hjørnet", new User("Bjarne"), new Room("101"), null, "Kjøpe mat"));
		avtaler.add(new Appointment(3, "Avtale", new Date(), new Date(), "Kiosken på hjørnet", new User("Bjarne"), new Room("101"), null, "Kjøpe mat"));
		
		frame.getContentPane().add(new Invitations(avtaler));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
