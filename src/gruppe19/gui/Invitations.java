package gruppe19.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Invitations extends JPanel{
	private ArrayList<InviteButton> invites;
	
	
	class InviteButton extends JPanel{
		private JButton btnAccept;
		private JButton btnDecline;
		private JLabel lblDescription;
		
		public InviteButton(String text) {
			lblDescription=new JLabel();
			lblDescription.setText(text);
			add(lblDescription);
			
			btnAccept=new JButton();
			btnAccept.setText("Godta");
			btnAccept.setBackground(Color.GREEN);
			add(btnAccept);
			
			btnDecline=new JButton();
			btnDecline.setText("Avslå");
			btnDecline.setBackground(Color.RED);
			add(btnDecline);
		}
		
	}
	
	
	public Invitations(){
		
		add(new InviteButton("Dette er en testavtale"));
	}
	
	public static void main(String[]args){
		JFrame frame=new JFrame("Det er her det skjer");
		
		frame.getContentPane().add(new Invitations());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
