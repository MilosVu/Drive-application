package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.management.ThreadMXBean;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;

public class Drive extends JFrame {

	private JPanel contentPane;
	private JTextPane txtFiles;
	private JButton btnSignOut;
	private JLabel lblFile;
	private JButton btnShareDriveWith;
	private JButton btnNewButton;
	private JButton btnDownloadFile;
	private JButton btnOpen;
	private JTextField txtOpen;
	private JLabel label;
	private static Socket socketForCommunication;
	private static BufferedReader streamFromServer;
	private static PrintStream streamToServer;
	private JTextField txtUpload;
	private JButton btnUppload;
	private JButton btnNewButton_1;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Drive frame = new Drive(socketForCommunication, streamFromServer, streamToServer);
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

	/**
	 * Create the frame.
	 */
	public Drive(Socket socketForCommunication, BufferedReader streamFromServer, PrintStream streamToServer) {
		this.socketForCommunication = socketForCommunication;
		this.streamFromServer = streamFromServer;
		this.streamToServer = streamToServer;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 791, 515);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getTxtFiles());
		contentPane.add(getBtnSignOut());
		contentPane.add(getLblFile());
		contentPane.add(getBtnShareDriveWith());
		contentPane.add(getBtnNewButton());
		contentPane.add(getBtnDownloadFile());
		contentPane.add(getBtnOpen());
		contentPane.add(getTxtOpen());
		contentPane.add(getLabel());
		contentPane.add(getTxtUpload());
		contentPane.add(getBtnUppload());
		contentPane.add(getBtnNewButton_1());
	}
	private JTextPane getTxtFiles() {
		if (txtFiles == null) {
			txtFiles = new JTextPane();
			txtFiles.setBounds(12, 29, 406, 344);
		}
		try {
			streamToServer.println("list");
			streamToServer.println("default,");
			String files = streamFromServer.readLine();
			files = files.replace(",", "\n");
			txtFiles.setText("YOUR DRIVE:\n_______________________\n" + files);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return txtFiles;
	}
	private JButton getBtnSignOut() {
		if (btnSignOut == null) {
			btnSignOut = new JButton("Sign out");
			btnSignOut.setBounds(676, 13, 97, 25);
		}
		return btnSignOut;
	}
	private JLabel getLblFile() {
		if (lblFile == null) {
			lblFile = new JLabel("File path:");
			lblFile.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblFile.setBounds(290, 416, 84, 24);
		}
		return lblFile;
	}
	private JButton getBtnShareDriveWith() {
		if (btnShareDriveWith == null) {
			btnShareDriveWith = new JButton("Share drive with someone");
			btnShareDriveWith.setBounds(536, 348, 225, 25);
		}
		return btnShareDriveWith;
	}
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Get shareable link");
			btnNewButton.setBounds(536, 296, 225, 25);
		}
		return btnNewButton;
	}
	private JButton getBtnDownloadFile() {
		if (btnDownloadFile == null) {
			btnDownloadFile = new JButton("Download");
			btnDownloadFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						String fileName = JOptionPane.showInputDialog("Enter name of file:");
						System.out.println(fileName);
						streamToServer.println("fileRequest");
						streamToServer.println(fileName + ",");
						InputStream streamFromServerByte = socketForCommunication.getInputStream();
						RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\Milos\\Downloads\\" + fileName, "rw");
						int n;
						byte[] bafer = new byte[1024];
						while(true) {
							n = streamFromServerByte.read(bafer,0,1024);
							if (n == -1)
								break;
							randomAccessFile.write(bafer,0,n);
						}
						
						randomAccessFile.close();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			btnDownloadFile.setBounds(657, 258, 104, 25);
		}
		return btnDownloadFile;
	}
	private JButton getBtnOpen() {
		if (btnOpen == null) {
			btnOpen = new JButton("Open");
			btnOpen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			btnOpen.setBounds(657, 220, 104, 25);
		}
		return btnOpen;
	}
	private JTextField getTxtOpen() {
		if (txtOpen == null) {
			txtOpen = new JTextField();
			txtOpen.setColumns(10);
			txtOpen.setBounds(493, 221, 159, 22);
		}
		return txtOpen;
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("File name:");
			label.setBounds(430, 224, 67, 16);
		}
		return label;
	}
	private JTextField getTxtUpload() {
		if (txtUpload == null) {
			txtUpload = new JTextField();
			txtUpload.setColumns(10);
			txtUpload.setBounds(386, 420, 266, 22);
		}
		return txtUpload;
	}
	private JButton getBtnUppload() {
		if (btnUppload == null) {
			btnUppload = new JButton("Upload");
			btnUppload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						String file = txtUpload.getText();
						OutputStream streamToServerByte;
						byte[] bafer = new byte[1024];
						streamToServer.println("uploadFile");
						String fileNew = file.replace("\\", ",");
						String[] fileName = fileNew.split(",");
						streamToServer.println(fileName[fileName.length - 1]+ ",");
						streamToServerByte = socketForCommunication.getOutputStream();
						RandomAccessFile randomAccessFile = new RandomAccessFile(
								file, "r");
						int n;
						while(true) {
							n = randomAccessFile.read(bafer);
							if(n == -1)
								break;
							streamToServerByte.write(bafer,0,n);
						}
						
						randomAccessFile.close();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			btnUppload.setBounds(657, 418, 104, 25);
		}
		return btnUppload;
	}
	private JButton getBtnNewButton_1() {
		if (btnNewButton_1 == null) {
			btnNewButton_1 = new JButton("Make folder");
			btnNewButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String folderName = JOptionPane.showInputDialog("Enter name of folder:");
					System.out.println(folderName);
					streamToServer.println("makeFolder");
					streamToServer.println(folderName + ",");
					
					try {
						streamToServer.println("list");
						streamToServer.println("default,");
						String files = streamFromServer.readLine();
						System.out.println(files);
						files = files.replace(",", "\n");
						txtFiles.setText("YOUR DRIVE:\n_______________________\n" + files);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			btnNewButton_1.setBounds(657, 182, 104, 25);
		}
		return btnNewButton_1;
	}
}
