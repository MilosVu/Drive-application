package service;

import java.net.Socket;

public interface Service {

	public void openStreams(Socket socketForCommunication) throws Exception;
	
	public boolean fileRequest(String Filename);

	public String logIn(String username, String password);

	public String registration(String username, String password, String status);
	
}
