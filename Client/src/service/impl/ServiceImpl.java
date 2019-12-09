package service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import service.Service;

public class ServiceImpl implements Service {

	private BufferedReader streamFromServer = null;
	private PrintStream streamToServer = null;
	private Socket socketForCommunication = null;
	private BufferedReader konzola = null;
	private String forServer = new String();

	@Override
	public void openStreams(Socket socketForCommunication) throws Exception {
		this.socketForCommunication = socketForCommunication;
		streamFromServer = new BufferedReader(new InputStreamReader(socketForCommunication.getInputStream()));
		streamToServer = new PrintStream(socketForCommunication.getOutputStream());
	}
	
	
	public String getForServer() {
		return forServer;
	}

	@Override
	public boolean fileRequest(String Filename) {
		return false;
	}

	@Override
	public String logIn(String username, String password) {
		return forServer =  "L," + username + "," + password;
	}

	@Override
	public String registration(Socket socketForCommunication,String username, String password) {
		return forServer =  "R," + username + "," + password;
	}

}
