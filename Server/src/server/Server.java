package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import service.impl.ServiceImpl;

public class Server {

	public static LinkedList<ServerThreads> clients = new LinkedList<>();
	public static void main(String[] args){
		
		try {
			ServerSocket serverSocket = new ServerSocket(14000);
	
			ServiceImpl serviceImpl = new ServiceImpl();
			serviceImpl.load();
			Socket socketForCommunication = null;
			while(true) {
				System.out.println("Cekam konekciju...");
				socketForCommunication = serverSocket.accept();
				System.out.println("Prihvacena konekcija...");
				
				ServerThreads newClient = new ServerThreads(socketForCommunication,serviceImpl);
				clients.add(newClient);
				
				newClient.start();
			}
			
		} catch (IOException e) {
			System.out.println("Greska prilikom pokretanja servera... " + e.getMessage());
		}
		
	}
	
}
