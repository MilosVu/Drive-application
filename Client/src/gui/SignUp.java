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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class SignUp extends JFrame {

	private JPanel contentPane;
	private JLabel label;
	private JLabel lblPassword1;
	private JLabel lblPassword2;
	private JTextField txtUsername;
	private JLabel txt1;
	private JButton btnNewButton;
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
					SignUp frame = new SignUp(socketForCommunication, streamFromServer, streamToServer);
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
	private JPasswordField txtPassword1;
	private JPasswordField txtPassword2;
	
	
	/**
	 * Create the frame.
	 */
	public SignUp(Socket socketForCommunication, BufferedReader streamFromServer, PrintStream streamToServer) {
		this.socketForCommunication = socketForCommunication;
		this.streamFromServer = streamFromServer;
		this.streamToServer = streamToServer;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLabel());
		contentPane.add(getLblPassword1());
		contentPane.add(getLblPassword2());
		contentPane.add(getTxtUsername());
		contentPane.add(getTxt1());
		contentPane.add(getBtnNewButton());
		contentPane.add(getTxtPassword1());
		contentPane.add(getPasswordField_1());
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Username:");
			label.setBounds(212, 187, 168, 31);
			label.setFont(new Font("Tahoma", Font.PLAIN, 25));
		}
		return label;
	}
	private JLabel getLblPassword1() {
		if (lblPassword1 == null) {
			lblPassword1 = new JLabel("Password:");
			lblPassword1.setFont(new Font("Tahoma", Font.PLAIN, 25));
			lblPassword1.setBounds(212, 231, 168, 31);
		}
		return lblPassword1;
	}
	private JLabel getLblPassword2() {
		if (lblPassword2 == null) {
			lblPassword2 = new JLabel("Password:");
			lblPassword2.setFont(new Font("Tahoma", Font.PLAIN, 25));
			lblPassword2.setBounds(212, 275, 168, 31);
		}
		return lblPassword2;
	}
	private JTextField getTxtUsername() {
		if (txtUsername == null) {
			txtUsername = new JTextField();
			txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 25));
			txtUsername.setColumns(10);
			txtUsername.setBounds(375, 187, 168, 31);
		}
		return txtUsername;
	}
	private JLabel getTxt1() {
		if (txt1 == null) {
			txt1 = new JLabel("Enter username and password");
			txt1.setHorizontalAlignment(SwingConstants.CENTER);
			txt1.setFont(new Font("Tahoma", Font.PLAIN, 25));
			txt1.setBounds(194, 121, 396, 42);
		}
		return txt1;
	}
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Submit");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String username = txtUsername.getText();
					String password1 = txtPassword1.getText();
					String password2 = txtPassword2.getText();
					if(password1.equals(password2)) {
						streamToServer.println(serviceImpl.registration(socketForCommunication, username, password1));
						try {
							response = streamFromServer.readLine();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						if(response.equals("Username already taken")) {
							JOptionPane.showMessageDialog(null, response);
							
						}
						else {
							JOptionPane.showMessageDialog(null, response);
						}
						}
					else {
						JOptionPane.showMessageDialog(null, "Password doesn't match");
						txtUsername.setText(null);
						txtPassword1.setText(null);
						txtPassword2.setText(null);
					}
				}
			});
			btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
			btnNewButton.setBounds(375, 332, 168, 31);
		}
		return btnNewButton;
	}
	private JPasswordField getTxtPassword1() {
		if (txtPassword1 == null) {
			txtPassword1 = new JPasswordField();
			txtPassword1.setBounds(375, 231, 168, 31);
		}
		return txtPassword1;
	}
	private JPasswordField getPasswordField_1() {
		if (txtPassword2 == null) {
			txtPassword2 = new JPasswordField();
			txtPassword2.setBounds(375, 275, 168, 31);
		}
		return txtPassword2;
	}
}
