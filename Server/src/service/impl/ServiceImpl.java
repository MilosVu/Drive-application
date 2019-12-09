package service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.nio.file.Files;

import domain.User;
import domain.Users;
import service.Service;
import validator.impl.ValidatorImpl;

public class ServiceImpl implements Service{

	final String s = "C:\\Users\\Milos\\eclipse-workspace\\Server\\Drive";
	private ValidatorImpl validatorImpl = new ValidatorImpl();
	private Users users = new Users();
	private String[] commandList = {"fileRequest","makeFolder","uploadFile",
			"shareFile", "shareDrive", "getShareableLink", "move", "rename",
			"delete","list"};
	
	public String answerGenerator(User user, String command, String parameters, Socket socketForCommunication) throws Exception{
		String[] param = parameters.split(",");;
		
		System.out.println("User: " + user.getUsername());
		System.out.println("Param: " + param[0]);
		int c = 0;
		for(int i = 0; i < commandList.length; i++) {
			if(command.equals(commandList[i]))
				c = i + 1;
		}
		switch (c) {
		case 1:
			fileRequest(user, param[0],socketForCommunication);
			break;
		case 2:
			makeFolder(user, param[0]);
			break;
		case 3:
			System.out.println(user.getFile().getAbsolutePath());
			uploadFile(user,param[0],socketForCommunication);
			break;
		case 4:
			shareFile(user, param[0]);
			break;
		case 5:
			shareDrive(user);
			break;
		case 6:
			getShareableLink();
			break;
		case 7:
			move(user, param[0],param[1]);
			break;
		case 8:
			rename(user, param[0], param[1]);
			break;
		case 9:
			delete(user, param[0]);
			break;
		case 10:
			return list(user, param[0]);
		default:
			break;
		}
		return null;
	}
	
	public void preview() {
		for(User u: users.getUsers()) {
			System.out.println(u.getUsername() + " " + u.getPassword());
		}
	}
	
	@Override
	public boolean registration(String username, String password, String status) {
		if(validatorImpl.userNameIsValid(users, username)) {
			users.add(new User(username,password, status));
			File f = new File(s+"\\"+username);
			f.mkdir();
			store();
			return true;
		}
		else
			return false;
	}

	@Override
	public User logIn(String username, String password) {
		if(users.isEmpty())
			return null;
		
		if(users.verifyUser(username, password))
			return users.find(username);
		return null;
	}

	@Override
	public void load() {
		try(FileInputStream fIn = new FileInputStream("users");
				BufferedInputStream bIn = new BufferedInputStream(fIn);
				ObjectInputStream in = new ObjectInputStream(bIn)){
			
			
			try {
				while(true) {
					User user = (User)(in.readObject());
					users.add(user);
				}
			} catch (Exception e) {
			}
			
		} catch (FileNotFoundException e1) {
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void store() {
		try(FileOutputStream fOut = new FileOutputStream("users");
				BufferedOutputStream bOut = new BufferedOutputStream(fOut);
				ObjectOutputStream out = new ObjectOutputStream(bOut)){
			
			for(int i = 0; i < users.getUsers().size(); i++)
					out.writeObject(users.getUsers().get(i));
					
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public String list(User user, String folder) {
		File folderNew;
		String[] files;
		String response = "";
		
		if(folder.equals("default")) {
			files = user.getFile().list();
		}
		else {
			folderNew = new File(user.getFile() + "\\" + "folder");
			files = folderNew.list();
		}
		
		for (String string : files) {
			response += string + ",";
		}
		return response;
	}

	//User commands
	@Override
	public String fileRequest(User user, String filename, Socket socketForCommunication) throws Exception {
		OutputStream streamToClientByte;
		byte[] bafer = new byte[1024];
		streamToClientByte = socketForCommunication.getOutputStream();
		RandomAccessFile randomAccessFile = new RandomAccessFile(
				user.getFile().getAbsolutePath() + "\\" + filename, "r");
		int n;
		while(true) {
			n = randomAccessFile.read(bafer);
			if(n == -1)
				break;
			streamToClientByte.write(bafer,0,n);
		}
		randomAccessFile.close();
		return "File downloaded in Downloads";
	}

	@Override
	public void makeFolder(User user, String folder) {
		String s = user.getFile().getAbsolutePath() + "\\" + folder;
		File file = new File(s);
		file.mkdir();
	}

	@Override
	public String uploadFile(User user, String fileName, Socket socketForCommunication) throws IOException {
		InputStream streamFromClientByte = socketForCommunication.getInputStream();
		RandomAccessFile randomAccessFile = new RandomAccessFile(s + "\\" + user.getUsername() + "\\" + fileName, "rw");
		int n;
		byte[] bafer = new byte[1024];
		while(true) {
			n = streamFromClientByte.read(bafer,0,1024);
			if (n == -1)
				break;
			randomAccessFile.write(bafer,0,n);
		}
		randomAccessFile.close();
		return "File successfully uploaded";
	}

	@Override
	public void shareFile(User user, String file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shareDrive(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getShareableLink() {
		return null;		
	}

	@Override
	public String move(User user,String fileFrom, String folderTo) throws Exception {
		File from = new File(user.getFile().getAbsoluteFile() + "\\" + fileFrom);
		File to = new File(user.getFile().getAbsoluteFile() + "\\" + folderTo + "\\" + fileFrom);
		Files.copy(from.toPath(), to.toPath());
		delete(user, fileFrom);
		return "Moved succesfully";
	}

	@Override
	public String rename(User user, String fileOld, String fileNew) {
		File fOld = new File(user.getFile().getAbsolutePath() + "\\" + fileOld);
		File fNew = new File(user.getFile().getAbsolutePath() + "\\" + fileNew);
		fOld.renameTo(fNew);
		return "File renamed";
	}

	@Override
	public String delete(User user, String file) {
		File fOld = new File(user.getFile().getAbsolutePath() + "\\" + file);
		fOld.delete();
		return "File deleted";
	}

	
	
}
