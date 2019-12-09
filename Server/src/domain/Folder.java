package domain;

import java.io.File;

public class Folder {

	private String name;
	private File file;
	
	public Folder(String name, File file) {
		super();
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public File getFile() {
		return file;
	}
	
	
	
}
