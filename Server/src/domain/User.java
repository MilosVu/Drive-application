package domain;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

public class User implements Serializable{

	private String username;
	private String password;
	private String status;
	private File file;
	private LinkedList<String> sharePermisions;
	private int numberOfFiles;
	
	public User(String username, String password, String status) {
		super();
		this.username = username;
		this.password = password;
		this.status = status;
		this.numberOfFiles = 0;
		sharePermisions = new LinkedList<>();
		this.file = new File("C:\\Users\\Milos\\eclipse-workspace\\Server\\Drive\\" + username);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public LinkedList<String> getSharePermisions() {
		return sharePermisions;
	}

	public void addSharePermisions(String driveName) {
		sharePermisions.add(driveName);
	}

	public int getNumberOfFiles() {
		return numberOfFiles;
	}

	public void setNumberOfFiles(int numberOfFiles) {
		this.numberOfFiles = numberOfFiles;
	}
	
	
	@Override
	public String toString() {
		String shared = "";
		for (String string : sharePermisions) {
			shared += string + " ";
		}
		return getUsername() + " " + getPassword() + " " + getStatus() + " " + shared;
	}
	
	
}
