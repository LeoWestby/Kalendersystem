package gruppe19.client.gui;

import gruppe19.client.ktn.ServerAPI;
import gruppe19.model.User;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * The login screen acts as the main entry point of the client.
 */
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

		KeyAdapter enterListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					logIn.doClick();
				}
			}
		};
		userBox.addKeyListener(enterListener);
		passBox.addKeyListener(enterListener);
		
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
	
	/**
	 * The main entry point of the client.
	 * 
	 * @param args Ignored.
	 */
	public static void main(String[] args) {
		new LoginScreen();
	}
}
