package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import service.impl.ServiceImpl;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Home extends JFrame {

	private JPanel contentPane;
	private JLabel lblWelcome;
	private JLabel lblPleaseLogIn;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JTextField txtUsername;
	private JTextPane txtpnYouDontHave;
	private JButton btnSingUp;
	private JButton btnSingIn;
	private String response = null;
	private static Socket socketForCommunication;
	private static BufferedReader streamFromServer;
	private static PrintStream streamToServer;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home(socketForCommunication, streamFromServer, streamToServer);
					frame.setVisible(true);
					frame.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent windowEvent) {
							if (JOptionPane.showConfirmDialog(frame, 
									"Are you sure you want to close this window?", "Close Window?", 
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
//								try {
//									socketForCommunication.close();
//								} catch (IOException e) {
//									e.printStackTrace();
//								}
								System.exit(0);
							}
						}
					});
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	ServiceImpl serviceImpl = new ServiceImpl();
	private JPasswordField txtPassword;
	
	/**
	 * Create the frame.
	 */
	public Home(Socket socketForCommunication, BufferedReader streamFromServer, PrintStream streamToServer) {
		this.socketForCommunication = socketForCommunication;
		this.streamFromServer = streamFromServer;
		this.streamToServer = streamToServer;
		setTitle("Home");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLblWelcome());
		contentPane.add(getLblPleaseLogIn());
		contentPane.add(getLblUsername());
		contentPane.add(getLblPassword());
		contentPane.add(getTxtUsername());
		contentPane.add(getTxtpnYouDontHave());
		contentPane.add(getBtnSingUp());
		contentPane.add(getBtnSingIn());
		contentPane.add(getTxtPassword());
	}
	private JLabel getLblWelcome() {
		if (lblWelcome == null) {
			lblWelcome = new JLabel("Welcome");
			lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
			lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 34));
			lblWelcome.setBounds(292, 59, 216, 72);
		}
		return lblWelcome;
	}
	private JLabel getLblPleaseLogIn() {
		if (lblPleaseLogIn == null) {
			lblPleaseLogIn = new JLabel("Please log in");
			lblPleaseLogIn.setFont(new Font("Tahoma", Font.PLAIN, 25));
			lblPleaseLogIn.setHorizontalAlignment(SwingConstants.CENTER);
			lblPleaseLogIn.setBounds(292, 159, 216, 31);
		}
		return lblPleaseLogIn;
	}
	private JLabel getLblUsername() {
		if (lblUsername == null) {
			lblUsername = new JLabel("Username:");
			lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 25));
			lblUsername.setBounds(221, 228, 168, 31);
		}
		return lblUsername;
	}
	private JLabel getLblPassword() {
		if (lblPassword == null) {
			lblPassword = new JLabel("Password:");
			lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 25));
			lblPassword.setBounds(221, 287, 168, 31);
		}
		return lblPassword;
	}
	private JTextField getTxtUsername() {
		if (txtUsername == null) {
			txtUsername = new JTextField();
			txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 25));
			txtUsername.setBounds(401, 228, 168, 31);
			txtUsername.setColumns(10);
		}
		return txtUsername;
	}
	private JTextPane getTxtpnYouDontHave() {
		if (txtpnYouDontHave == null) {
			txtpnYouDontHave = new JTextPane();
			txtpnYouDontHave.setText("You don't have account. Sign up now");
			txtpnYouDontHave.setBounds(430, 487, 156, 38);
		}
		return txtpnYouDontHave;
	}
	private JButton getBtnSingUp() {
		if (btnSingUp == null) {
			btnSingUp = new JButton("Sign up");
			btnSingUp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				SignUp newFrame = new SignUp(socketForCommunication, streamFromServer, streamToServer);
				newFrame.setVisible(true);
				//System.exit(0);
				}
			});
			btnSingUp.setFont(new Font("Tahoma", Font.PLAIN, 20));
			btnSingUp.setBounds(644, 487, 122, 38);
		}
		return btnSingUp;
	}
	private JButton getBtnSingIn() {
		if (btnSingIn == null) {
			btnSingIn = new JButton("Sign in");
			btnSingIn.setFont(new Font("Tahoma", Font.PLAIN, 20));
			btnSingIn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				
					String username = txtUsername.getText();
					String password = txtPassword.getText();
					streamToServer.println(serviceImpl.logIn(username, password));
					try {
						response = streamFromServer.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(response.equals("You have signed in")) {
						JOptionPane.showMessageDialog(null, response);
						Drive drive = new Drive(socketForCommunication, streamFromServer, streamToServer);
						drive.setVisible(true);
					}
					else {
						JOptionPane.showMessageDialog(null, response);
					}
				}
			});
			btnSingIn.setBounds(401, 349, 168, 31);
		}
		return btnSingIn;
	}
	private JPasswordField getTxtPassword() {
		if (txtPassword == null) {
			txtPassword = new JPasswordField();
			txtPassword.setBounds(401, 287, 168, 31);
		}
		return txtPassword;
	}
}
