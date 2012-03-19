package gruppe19.gui;

import gruppe19.client.ktn.ServerAPI;
import gruppe19.model.User;
import gruppe19.server.ktn.ServerMessage;
import gruppe19.server.ktn.ServerMessage.Type;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import no.ntnu.fp.model.Person;

public class LoginScreen extends JFrame {
	private JLabel userPrefix = new JLabel("Brukernavn: ");
	private JLabel passPrefix = new JLabel("Passord: ");
	private JLabel error = new JLabel(" ");
	private JTextField userBox = new JTextField(16);
	private JTextField passBox = new JPasswordField(16);
	private JButton logIn = new JButton("Logg inn");
	
	public LoginScreen() {
		//Open a connection to the server
		try {
			ServerAPI.open();
		} catch (Exception e) {
			System.err.println("[Error] Failed to establish connection with server...");
			System.exit(1);
		}
		//catch (SocketTimeoutException e1) {} catch (UnknownHostException e1) {} catch (IOException e1) {}
		
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
		
		error.setForeground(Color.red);
		error.setSize(999, error.getPreferredSize().height);
		error.setLocation(passBox.getX(), passBox.getY() + passBox.getHeight());

		logIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				User ret = ServerAPI.login(userBox.getText(), passBox.getText());
				
				if (ret == null) {
					error.setForeground(Color.red);
					error.setText("Feil brukernavn eller passord");
				}
				else {
					MainScreen frame = new MainScreen(ret);
					
					setVisible(false);
					userBox.setText("");
					passBox.setText("");
					error.setText("");
					
					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent e) {
							setVisible(true);
							userBox.requestFocus();
						}
					});
					
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
