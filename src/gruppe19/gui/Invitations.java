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
			btnAccept=new JButton();
			btnDecline=new JButton();
						
			lblDescription.setText(appointment.getTitle());
			add(lblDescription,BorderLayout.NORTH);
			
			JPanel wrapper=new JPanel();
			wrapper.setDefaultLocale(lblDescription.getLocale());
			wrapper.setBorder(new EmptyBorder(10,110,5,20));
			add(wrapper,BorderLayout.SOUTH);
			
			c.gridy ++;
			btnAccept.setText("Godta");
			btnAccept.setBackground(Color.GREEN);
			add(btnAccept,BorderLayout.WEST);
			
			btnDecline.setText("Avsl�");
			btnDecline.setBackground(Color.RED);
			add(btnDecline,BorderLayout.EAST);

		}

		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
	
	
	public Invitations(ArrayList<Appointment> appointments){
		setLayout(new GridBagLayout());
		c=new GridBagConstraints();
		lblInvitations = new JLabel();

		c.gridy=0;
		c.gridx=0;
		lblInvitations.setText("Nye m�teinvitasjoner:");
		c.gridx++;
		c.gridy++;
		
		
		add(lblInvitations,c);

		for (Appointment appointment : appointments) {
			add(new InviteButton(appointment),c);
			c.gridy++;
		}
	}
	
	public static void main(String[]args){
		JFrame frame=new JFrame("Det er her det skjer");
		ArrayList<Appointment> avtaler= new ArrayList<Appointment>();
		
		
		avtaler.add(new Appointment(2, "Avtale", new Date(), new Date(), "Kiosken p� hj�rnet", new User("Bjarne"), new Room("101"), null, "Kj�pe mat"));
		avtaler.add(new Appointment(3, "Dette er en lengre avtalebeskrivelse som skal vise om dette blir seendes bra ut", new Date(), new Date(), "Kiosken p� hj�rnet", new User("Bjarne"), new Room("101"), null, "Kj�pe mat"));
		
		frame.getContentPane().add(new Invitations(avtaler));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
