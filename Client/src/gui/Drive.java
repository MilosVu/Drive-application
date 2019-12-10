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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.management.ThreadMXBean;
import java.net.Socket;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.JobAttributes;

public class Drive extends JFrame {

	private JPanel contentPane;
	private JTextPane txtFiles;
	private JButton btnSignOut;
	private JButton btnShareDriveWith;
	private JButton btnGetSharableLink;
	private JButton btnDownloadFile;
	private JButton btnOpen;
	private static Socket socketForCommunication;
	private static BufferedReader streamFromServer;
	private static PrintStream streamToServer;
	private JButton btnUppload;
	private JButton btnMakeFolder;
	private JButton btnRenameFolder;
	private JButton btnDeleteFolder;
	private JLabel lblPremiumOptions;
	private JButton btnMove;
	private JButton btnBack;
	private LinkedList<String> currentFolder = new LinkedList<>();
	
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
		contentPane.add(getBtnShareDriveWith());
		contentPane.add(getBtnGetSharableLink());
		contentPane.add(getBtnDownloadFile());
		contentPane.add(getBtnOpen());
		contentPane.add(getBtnUppload());
		contentPane.add(getBtnMakeFolder());
		contentPane.add(getBtnRenameFolder());
		contentPane.add(getBtnDeleteFolder());
		contentPane.add(getLabel_1());
		contentPane.add(getBtnMove());
		contentPane.add(getBtnBack());
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
			btnSignOut.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					int dialogButton = JOptionPane.YES_NO_OPTION;
				    JOptionPane.showConfirmDialog (null, "Would You like to sign out?","Confirm",dialogButton);

				    if (dialogButton == JOptionPane.YES_OPTION) {

				    	System.exit(0);

				    }
					
				}
			});
			btnSignOut.setBounds(676, 13, 97, 25);
		}
		return btnSignOut;
	}
	private JButton getBtnShareDriveWith() {
		if (btnShareDriveWith == null) {
			btnShareDriveWith = new JButton("Share drive with someone");
			btnShareDriveWith.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String userForShare = JOptionPane.showInputDialog("Enter username of user you want to share your drive:");
					if(userForShare == null) return;
					streamToServer.println("shareDrive");
					streamToServer.println(userForShare + ",");
					String response = "";
					
					try {
						response = streamFromServer.readLine();
						JOptionPane.showMessageDialog(null, response);
					} catch (IOException e4) {
						e4.printStackTrace();
					}
					
				}
			});
			btnShareDriveWith.setBounds(536, 348, 225, 25);
		}
		return btnShareDriveWith;
	}
	private JButton getBtnGetSharableLink() {
		if (btnGetSharableLink == null) {
			btnGetSharableLink = new JButton("Get shareable link");
			btnGetSharableLink.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				}
			});
			btnGetSharableLink.setBounds(536, 296, 225, 25);
		}
		return btnGetSharableLink;
	}
	private JButton getBtnDownloadFile() {
		if (btnDownloadFile == null) {
			btnDownloadFile = new JButton("Download");
			btnDownloadFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String fileName = JOptionPane.showInputDialog("Enter name of file:");
					if (fileName == null) return;
					try {
						streamToServer.println("fileForDownloadExists");
						String response = "";
						streamToServer.println(fileName + ",");
						response = streamFromServer.readLine();
						if(response.equals("Exists your drive")) {
							streamToServer.println("fileRequest");
							streamToServer.println(fileName);
//							InputStream streamFromServerByte = socketForCommunication.getInputStream();
//							RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\Milos\\Downloads\\" + fileName, "rw");
//							int n;
//							byte[] bafer = new byte[1024];
//							while(true) {
//								n = streamFromServerByte.read(bafer,0,1024);
//								if (n == -1) {
//									break;
//								}
//								randomAccessFile.write(bafer,0,n);
//							}
//							randomAccessFile.close();
//							response = streamFromServer.readLine();
//							JOptionPane.showMessageDialog(null, response);
						}
						else if(response.equals("Doesn't exist")){
							JOptionPane.showMessageDialog(null, response);
							return;
						}
						else{
							streamToServer.println("fileRequest");
							String[] owner = response.split(",");
							streamToServer.println(owner[1] + "-" + fileName);
						}
						byte[] bafer = new byte[10240];
						InputStream is = socketForCommunication.getInputStream();
						FileOutputStream fr = new FileOutputStream("C:\\Users\\Milos\\Downloads\\" + fileName);
						is.read(bafer,0,bafer.length);
						fr.write(bafer,0,bafer.length);
						response = streamFromServer.readLine();
						if(response.equals("File sent"));
						JOptionPane.showMessageDialog(null, "File saved in Downloads");
						
						} catch (IOException e) {
						}
				}				
			});
			btnDownloadFile.setBounds(390, 418, 140, 25);
		}
		return btnDownloadFile;
	}
	private JButton getBtnOpen() {
		if (btnOpen == null) {
			btnOpen = new JButton("Open");
			btnOpen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						String fileName = JOptionPane.showInputDialog("Enter name of file:");
						System.out.println(fileName);
						streamToServer.println("open");
						streamToServer.println(fileName + ",");
						String s = fileName + ":\n_____________________________\n";
						String response = streamFromServer.readLine();
						
						if(response.equals("Folder")) {
							currentFolder.add(fileName);
							streamToServer.println("list");
							streamToServer.println(fileName + ",");
							String files = streamFromServer.readLine();
							System.out.println(files);
							files = files.replace(",", "\n");
							txtFiles.setText("YOUR DRIVE:\n_______________________\n" + files);
						}
						else {
							s += response;
							s = s.replace("|||", "\n");
							JOptionPane.showMessageDialog(null, s);
							System.out.println(s);
						}
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
				
			});
			btnOpen.setBounds(12, 418, 140, 25);
		}
		return btnOpen;
	}
	private JButton getBtnUppload() {
		if (btnUppload == null) {
			btnUppload = new JButton("Upload");
			btnUppload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						String file = JOptionPane.showInputDialog("Enter file path:");
//						OutputStream streamToServerByte;
						byte[] bafer = new byte[10240];
						String fileNew = file.replace("\\", ",");
						String[] fileName = fileNew.split(",");
						if(file.equals("") || fileName.length == 1)
							throw new IOException();
						streamToServer.println("uploadFile");
						streamToServer.println(fileName[fileName.length - 1]+ ",");
//						
						
						FileInputStream fr = new FileInputStream(file);
						fr.read(bafer,0,bafer.length);
						OutputStream os = socketForCommunication.getOutputStream();
						os.write(bafer,0,bafer.length);
						
//						streamToServerByte = socketForCommunication.getOutputStream();
//						
//						RandomAccessFile randomAccessFile = new RandomAccessFile(
//								file, "r");
//						int n;
//						while(true) {
//							n = randomAccessFile.read(bafer);
//							if(n == -1)
//								break;
//							streamToServerByte.write(bafer,0,n);
//						}
//						
//						randomAccessFile.close();
						String response = streamFromServer.readLine();
						JOptionPane.showMessageDialog(null, response);
						streamToServer.println("list");
						streamToServer.println("default,");
						String files = streamFromServer.readLine();
						System.out.println(files);
						files = files.replace(",", "\n");
						txtFiles.setText("YOUR DRIVE:\n_______________________\n" + files);
						
						
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "File not found");
					}catch (Exception e) {
					}
				}
			});
			btnUppload.setBounds(202, 418, 140, 25);
		}
		return btnUppload;
	}
	private JButton getBtnMakeFolder() {
		if (btnMakeFolder == null) {
			btnMakeFolder = new JButton("Make folder");
			btnMakeFolder.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String folderName = JOptionPane.showInputDialog("Enter name of folder:");
					if (folderName == null) return;
					streamToServer.println("makeFolder");
					streamToServer.println(folderName + ",");
					String response = "";
					try {
						response = streamFromServer.readLine();
						JOptionPane.showMessageDialog(null, response);
						
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
			btnMakeFolder.setBounds(634, 112, 127, 25);
		}
		return btnMakeFolder;
	}
	private JButton getBtnRenameFolder() {
		if (btnRenameFolder == null) {
			btnRenameFolder = new JButton("Rename folder");
			btnRenameFolder.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					String OldfolderName = JOptionPane.showInputDialog("Enter name of folder you want to rename:");
					String NewfolderName = JOptionPane.showInputDialog("Enter new name of folder:");
					if (OldfolderName == null || NewfolderName == null) return;
					streamToServer.println("rename");
					streamToServer.println(OldfolderName + "," + NewfolderName + ",");
					String response = "";
					try {
						response = streamFromServer.readLine();
						JOptionPane.showMessageDialog(null, response);
						
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
			btnRenameFolder.setBounds(634, 150, 127, 25);
		}
		return btnRenameFolder;
	}
	private JButton getBtnDeleteFolder() {
		if (btnDeleteFolder == null) {
			btnDeleteFolder = new JButton("Delete folder");
			btnDeleteFolder.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						
						String folderName = JOptionPane.showInputDialog("Enter name of folder you want to delete:");
						if (folderName == null) return;
						streamToServer.println("delete");
						streamToServer.println(folderName + ",");
						String response = "";
						response = streamFromServer.readLine();
						JOptionPane.showMessageDialog(null, response);
						
						streamToServer.println("list");
						streamToServer.println("default,");
						String files = streamFromServer.readLine();
						System.out.println(files);
						files = files.replace(",", "\n");
						txtFiles.setText("YOUR DRIVE:\n_______________________\n" + files);
						
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
			});
			btnDeleteFolder.setBounds(634, 188, 127, 25);
		}
		return btnDeleteFolder;
	}
	private JLabel getLabel_1() {
		if (lblPremiumOptions == null) {
			lblPremiumOptions = new JLabel("Premium options:");
			lblPremiumOptions.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblPremiumOptions.setBounds(634, 82, 127, 16);
		}
		return lblPremiumOptions;
	}
	private JButton getBtnMove() {
		if (btnMove == null) {
			btnMove = new JButton("Move ");
			btnMove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String file = JOptionPane.showInputDialog("Enter file you want to move:");
					String destinationFolder = JOptionPane.showInputDialog("Enter destination folder:");
					if(file == null || destinationFolder == null) return;
					
					try {
						streamToServer.println("move");
						streamToServer.println(file + "," + destinationFolder + ",");
						String response = "";
						
						response = streamFromServer.readLine();
						JOptionPane.showMessageDialog(null, response);
						streamToServer.println("list");
						streamToServer.println("default,");
						String files = streamFromServer.readLine();
						System.out.println(files);
						files = files.replace(",", "\n");
						txtFiles.setText("YOUR DRIVE:\n_______________________\n" + files);
					} catch (IOException e3) {
						e3.printStackTrace();
					}
					
				}
			});
			btnMove.setBounds(634, 226, 127, 25);
		}
		return btnMove;
	}
	private JButton getBtnBack() {
		if (btnBack == null) {
			btnBack = new JButton("Back");
			btnBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					try {
						currentFolder.removeLast();
						streamToServer.println("list");
						
						if(currentFolder.isEmpty())
							streamToServer.println("default,");
						else
							streamToServer.println(currentFolder.getLast()+ ",");
						
						String files = streamFromServer.readLine();
						System.out.println(files);
						files = files.replace(",", "\n");
						txtFiles.setText("YOUR DRIVE:\n_______________________\n" + files);
					} catch (IOException e3) {
						e3.printStackTrace();
					}
					
				}
			});
			btnBack.setBounds(12, 380, 140, 25);
		}
		return btnBack;
	}
}
