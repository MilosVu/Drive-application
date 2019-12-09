package domain;

import java.io.File;
import java.io.Serializable;

public class User implements Serializable{

	private String username;
	private String password;
	private String status;
	private File file;
	private File[] sharePermisions;
	private int numberOfFiles;
	
	public User(String username, String password, String status) {
		super();
		this.username = username;
		this.password = password;
		this.status = status;
		this.numberOfFiles = 0;
		sharePermisions = null;
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

	public File[] getSharePermisions() {
		return sharePermisions;
	}

	public void setSharePermisions(File[] sharePermisions) {
		this.sharePermisions = sharePermisions;
	}

	public int getNumberOfFiles() {
		return numberOfFiles;
	}

	public void setNumberOfFiles(int numberOfFiles) {
		this.numberOfFiles = numberOfFiles;
	}
	
	
	
	
	
}
