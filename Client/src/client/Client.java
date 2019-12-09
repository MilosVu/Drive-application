package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;

import gui.Home;
import service.impl.ServiceImpl;

public class Client implements Runnable{

	static Socket socketForCommunication = null;
	ServiceImpl serviceimpl = new ServiceImpl();
	static BufferedReader streamFromServer = null;
	static PrintStream streamToServer = null;
	boolean end = false;
	
	public static void main(String[] args) {
		try {
			socketForCommunication = new Socket("localhost", 14000);
			streamToServer = new PrintStream(socketForCommunication.getOutputStream());
			streamFromServer = new BufferedReader(new InputStreamReader(socketForCommunication.getInputStream()));
			
//			byte[] bafer = new byte[1024];
//			OutputStream streamToServerByte = socketForCommunication.getOutputStream();
//			streamToServerByte.write(1);
//			RandomAccessFile randomAccessFile = new RandomAccessFile(
//					"C:\\Users\\Milos\\eclipse-workspace\\Client\\wtf.txt", "r");
//			int n;
//			while(true) {
//				n = randomAccessFile.read(bafer);
//				if(n == -1)
//					break;
//				streamToServerByte.write(bafer,0,n);
//			}
//			
//			randomAccessFile.close();
//			
			
			
			new Thread(new Client()).start();
			System.out.println("Zatvaram konekciju");
//			socketForCommunication.close();
		} catch (UnknownHostException e) {
			System.out.println("Server nije podignut...");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Greska prilikom pokretanja klijenta");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		try {
			Home frame = new Home(socketForCommunication, streamFromServer, streamToServer);
			frame.setVisible(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
