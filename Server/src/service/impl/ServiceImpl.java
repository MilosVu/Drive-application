package service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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
			"delete","list","open","fileForDownloadExists"};
	
	public String answerGenerator(User user, String command, String parameters, Socket socketForCommunication) throws Exception{
		String[] param = parameters.split(",");;
		int c = 0;
		for(int i = 0; i < commandList.length; i++) {
			if(command.equals(commandList[i]))
				c = i + 1;
		}
		switch (c) {
		case 1:
			return fileRequest(user, param[0],socketForCommunication);
		case 2:
			return makeFolder(user, param[0]);
		case 3:
			return uploadFile(user,param[0],socketForCommunication);
		case 4:
			shareFile(user, param[0]);
			break;
		case 5:
			return shareDrive(user,param[0]);
		case 6:
			getShareableLink();
			break;
		case 7:
			return move(user, param[0],param[1]);
		case 8:
			return rename(user, param[0], param[1]);
		case 9:
			return delete(user, param[0]);
		case 10:
			return list(user, param[0]);
		case 11:
			return open(user, param[0]);
		case 12:
			return fileForDownloadExists(user, param[0]);
		default:
			return "ERROR";
		}
		return null;
	}
	
	public void preview() {
		for(User u: users.getUsers()) {
			System.out.println(u.toString());
		}
		System.out.println(users.getUsers().size());
	}
	
	@Override
	public boolean registration(String username, String password, String status) {
		if(validatorImpl.userNameIsValid(users, username)) {
			users.add(new User(username,password, status));
			File f = new File(s+"\\"+username);
			f.mkdir();
			store();
			preview();
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
		File f = new File("C:\\Users\\Milos\\eclipse-workspace\\Server\\users");
		f.delete();
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
			folderNew = new File(user.getFile() + "\\" + folder);
			files = folderNew.list();
		}
		if(files == null || files.length == 0)
			response =  "";
		else {
			for (String string : files) {
				response += string + ",";
			}
		}
		if(folder.equals("default") && !user.getSharePermisions().isEmpty()) {
			response += "Shared with you:" ;
			for (String s : user.getSharePermisions()) {
				response += ",__________________________," + s + " user shared drive with you: ," + list(
						users.find(s), "default");
			}
		}
		
		return response;
	}
	
	//User commands
	@Override
	public String fileRequest(User user, String filename, Socket socketForCommunication) throws Exception {
		
//		OutputStream streamToClientByte;
//		byte[] bafer = new byte[1024];
//		streamToClientByte = socketForCommunication.getOutputStream();
//		RandomAccessFile randomAccessFile = new RandomAccessFile(
//				user.getFile().getAbsolutePath() + "\\" + filename, "r");
//		int n;
//		while(true) {
//			n = randomAccessFile.read(bafer,0,1024);
//			if(n == -1)
//				break;
//			streamToClientByte.write(bafer,0,n);
//		}
//		randomAccessFile.close();
		boolean own = false;
		for (String u : user.getFile().list()) {
			if(u.equals(filename))
				own = true;
		}
		FileInputStream fr;
		if(own) {
			fr = new FileInputStream(user.getFile().getAbsolutePath() + "\\" + filename);
		}
		else {
			String[] owner = filename.split("-");
			System.out.println("owner " + owner[0] + " file name " + owner[1]);
			User u = users.find(owner[0]);
			fr = new FileInputStream(u.getFile().getAbsolutePath() + "\\" + owner[1]);
			System.out.println(u.getFile().getAbsolutePath() + "\\" + owner[1]);
		}
		byte[] bafer = new byte[10240];
		fr.read(bafer,0,bafer.length);
		OutputStream os = socketForCommunication.getOutputStream();
		os.write(bafer,0,bafer.length);
		return "File sent";
	}

	@Override
	public String makeFolder(User user, String folder) {
		if(user.getStatus().equals("R"))
			return "You don't have permission to create folder";
		String s = user.getFile().getAbsolutePath() + "\\" + folder;
		File file = new File(s);
		file.mkdir();
		return "Folder has been successfully created";
	}

	@Override
	public String uploadFile(User user, String fileName, Socket socketForCommunication) throws Exception{
		if(user.getStatus().equals("R") && user.getFile().list().length >= 5)
			return "You have uploaded maximum number of files";
//		InputStream streamFromClientByte;
//		
//			streamFromClientByte = socketForCommunication.getInputStream();
//			RandomAccessFile randomAccessFile = new RandomAccessFile(s + "\\" + user.getUsername() + "\\" + fileName, "rw");
//			int n;
//			byte[] bafer = new byte[1024];
//			while(true) {
//				System.out.println(n = streamFromClientByte.read(bafer));
//				if (n == -1)
//					break;
//				randomAccessFile.write(bafer,0,n);
//			}
//			randomAccessFile.close();
//		
		byte[] bafer = new byte[10240];
		InputStream is = socketForCommunication.getInputStream();
		FileOutputStream fr = new FileOutputStream(s + "\\" + user.getUsername() + "\\" + fileName);
		is.read(bafer,0,bafer.length);
		fr.write(bafer,0,bafer.length);
		
		
		return "File successfully uploaded";
	}

	@Override
	public void shareFile(User user, String file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String shareDrive(User user, String userWhoGetsAccess) {
		User user2 = users.find(userWhoGetsAccess);
		user2.addSharePermisions(user.getUsername());
		store();
		return "You shared your drive with " + userWhoGetsAccess;
	}

	@Override
	public String getShareableLink() {
		return null;		
	}

	@Override
	public String move(User user,String fileFrom, String folderTo){
		if(user.getStatus().equals("R"))
			return "You don't have permission to do this action";
		File from = new File(user.getFile().getAbsoluteFile() + "\\" + fileFrom);
		File to = new File(user.getFile().getAbsoluteFile() + "\\" + folderTo + "\\" + fileFrom);
		try {
			Files.copy(from.toPath(), to.toPath());
		} catch (IOException e) {
			return "Failed to move";
		}
		from.delete();
		return "Moved succesfully";
	}

	@Override
	public String rename(User user, String fileOld, String fileNew) {
		if(user.getStatus().equals("R"))
			return "You don't have permission to do this action";
		File fOld = new File(user.getFile().getAbsolutePath() + "\\" + fileOld);
		File fNew = new File(user.getFile().getAbsolutePath() + "\\" + fileNew);
		fOld.renameTo(fNew);
		return "File renamed";
	}

	@Override
	public String delete(User user, String file) {
		if(user.getStatus().equals("R"))
			return "You don't have permission to do this action";
		File fOld = new File(user.getFile().getAbsolutePath() + "\\" + file);
		if(fOld.list().length != 0)
			return "Folder is not empty";
		fOld.delete();
		return "Folder deleted";
	}

	@Override
	public String open(User user, String file){
		String response = "";
		File path = new File(user.getFile().getAbsolutePath() + "\\" + file);
		if(path.isDirectory())
			return "Folder";
		try(FileReader fIn = new FileReader(path);
				BufferedReader brIn = new BufferedReader(fIn)){
			
			boolean end = false;
			
			while(!end) {
				String pom = brIn.readLine();
				if(pom == null) end = true;
				else response = response + pom + "|||";
			}
		
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return response;
	}

	@Override
	public String back(User user, String file) {
		file = file.replace("\\", ",");
		String[] previous = file.split(",");
		if(previous.length == 1)
			return "Drive";
		
		return null;
	}

	@Override
	public String fileForDownloadExists(User user, String file) {
		
		for (String string :user.getFile().list()) {
			if(string.equals(file)) {
				return "Exists your drive";
			}
		}
		
		for (String u :user.getSharePermisions()) {
			User user1 = users.find(u);
			for (String string : user1.getFile().list()) {
				if(string.equals(file)) {
					return "Exists in shared drive," + user1.getUsername();
				}
			}
		}
		
		return "Doesn't exist";
	}

}
