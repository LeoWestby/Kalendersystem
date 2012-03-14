package gruppe19.gui;

import gruppe19.nettverk.ServerAPI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginScreen extends JFrame {
	private JLabel userPrefix = new JLabel("Brukernavn: ");
	private JLabel passPrefix = new JLabel("Passord: ");
	private JLabel error = new JLabel(" ");
	private JTextField userBox = new JTextField(16);
	private JTextField passBox = new JTextField(16);
	private JButton logIn = new JButton("Logg inn");
	
	public LoginScreen() {
		JPanel contentPane = new JPanel();
		contentPane.setLayout(null);
		
		userPrefix.setSize(userPrefix.getPreferredSize());
		userPrefix.setLocation(24, 25);
		
		passPrefix.setSize(passPrefix.getPreferredSize());
		passPrefix.setLocation(userPrefix.getX(), userPrefix.getY() + userPrefix.getHeight() + 10);
		
		userBox.setSize(userBox.getPreferredSize());
		userBox.setLocation(userPrefix.getX() + userPrefix.getWidth() + 5, userPrefix.getY());

		passBox.setSize(passBox.getPreferredSize());
		passBox.setLocation(userBox.getX(), passPrefix.getY());
		
		logIn.setSize(logIn.getPreferredSize());
		logIn.setLocation(passBox.getX() + passBox.getWidth() - logIn.getWidth(), passBox.getY() + 60);
		
		error.setSize(999, error.getPreferredSize().height);
		error.setForeground(Color.red);
		error.setLocation(passBox.getX(), logIn.getY());

		logIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int ret = ServerAPI.login(userBox.getText(), passBox.getText());
				
				if (ret < 0) {
					error.setText("Feil brukernavn");
				}
				else if (ret == 0) {
					error.setText("Feil passord");
				}
				else if (ret > 0) {
					error.setText(" ");
				}
			}
		});
		
		setLayout(null);
		add(userPrefix);
		add(passPrefix);
		add(userBox);
		add(passBox);
		add(logIn);
		add(error);
		
		setSize(327, 201);
		setTitle("Kalendersystem");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new LoginScreen();
	}
}