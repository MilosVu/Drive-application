package service;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import domain.User;

public interface Service {
	
	public String answerGenerator(User user, String command, String parameters, Socket socketForCommunication) throws Exception;
	
	public String fileRequest(User user, String filename, Socket socketForCommunication) throws Exception;

	User logIn(String username, String password);

	boolean registration(String username, String password, String status);
	
	public void load();
	
	public void store();
	
	public void makeFolder(User user, String folder);
	
	public String uploadFile(User user, String fileName, Socket socketForCommunication)throws IOException;
	
	public String list(User user, String folder);
	
	//access to specific file
	public void shareFile(User user,String file);
	
	//access to hole drive except upload function
	public void shareDrive(User user);
	
	public String getShareableLink();
	
	public String move(User user, String fileFrom, String fileTo) throws Exception;
	
	public String rename(User user, String fileOld, String fileNew);
	
	public String delete(User user, String file);
	
}
