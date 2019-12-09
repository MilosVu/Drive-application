package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.ws.Response;

import domain.User;
import service.impl.ServiceImpl;
import validator.impl.ValidatorImpl;

public class ServerThreads extends Thread{
	Socket socketForCommunication = null;
	BufferedReader streamFromClient = null;
	PrintStream streamToClient = null;
	String command = null;
	String parameters = null;
	String[] textArray = new String[3];
	String username = null;
	String password = null;
	ValidatorImpl validatorImpl = new ValidatorImpl();
	ServiceImpl serviceImpl;
	String status = null;
	boolean log = false;
	User user = null;
	String response;
	
	public ServerThreads(Socket socketForCommunication,	ServiceImpl serviceImpl) {
		this.socketForCommunication = socketForCommunication;
		this.serviceImpl = serviceImpl;
	}

	public void run() {
		try {
			streamFromClient = new BufferedReader(new InputStreamReader(socketForCommunication.getInputStream()));
			streamToClient = new PrintStream(socketForCommunication.getOutputStream());
			
			
			serviceImpl.load();
			
			while(!log) {
				command = streamFromClient.readLine();
				textArray = command.split(",");
				username = textArray[1];
				password = textArray[2];
				status = textArray[0];
				if(textArray[0].equals("L")) {
					user = serviceImpl.logIn(username, password);
					if(user != null) {
						streamToClient.println("You have signed in");
						log = true;
					}
					else
						streamToClient.println("Wrong username or password");
				}else {
					if(serviceImpl.registration(username, password,status))
						streamToClient.println("You have registered successfully");
					else
						streamToClient.println("Username already taken");
				}
			}	
			
			while(true) {
				command = streamFromClient.readLine();
				parameters = streamFromClient.readLine();
				response =  serviceImpl.answerGenerator(user, command, parameters, socketForCommunication);
				System.out.println("Response: " + response);
				streamToClient.println(response);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
