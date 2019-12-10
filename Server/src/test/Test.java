package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;

import domain.User;
import domain.Users;
import service.impl.ServiceImpl;

public class Test {

	public static void main(String[] args) {

//		User u1 = new User("MilosVu", "milos123");
//		User u2 = new User("AnjaDj", "anja456");
//		User u3 = new User("MarkoCAR", "marko789");
		Users users = new Users();
//		users.add(u1);
//		users.add(u2);
//		users.add(u3);
//		
//		users.store();
		ServiceImpl serviceImpl = new ServiceImpl();
		serviceImpl.load();
		
//	
		serviceImpl.preview();
//		if(serviceImpl.logIn("Milos1", "m")==null)
//			System.out.println("ma");
//		else {
//			System.out.println("ne");
//		}
//	}

//		File f = new File("C:\\Users\\Milos\\eclipse-workspace\\Server\\src\\test\\s.txt");
//		if(f.mkdir())
//			System.out.println("kreirano");
//		else
//			System.out.println("jok");
//		
//		try {
//			f.createNewFile();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
//		String s = "C:\\Users\\Milos\\eclipse-workspace\\Server\\src\\test\\proba.txt";
//		File f = new File(s+"\\dir1");
//		f.mkdir();
//		f = new File(s+"\\dir2");
//		f.mkdir();
		
//		File f = new File(s+"\\proba.txt");
//		File dir = new File(s+"\\dir1");
//		dir.mkdir();
//		System.out.println(dir.getName());
//		try {
//			f.createNewFile();
//			try(FileReader fr = new FileReader(f);
//					BufferedReader br = new BufferedReader(fr)){
//				System.out.println(br.readLine());
//				System.out.println(br.readLine());
//			}
//			
//			try(FileWriter fw = new FileWriter(f);
//					BufferedWriter bw = new BufferedWriter(fw);
//					PrintWriter out = new PrintWriter(bw)){
//				out.print("Aj upisi ovo majke ti\npls");
//		s = s.replace("\\", ",");
//		String[] failName = s.split(",");
//		System.out.println(failName[0]);
//		System.out.println(failName[failName.length - 1]+ ",");
	
//		File f = new File("C:\\Users\\Milos\\eclipse-workspace\\Server\\Drive\\premium\\br");
//		File out = new File("C:\\Users\\Milos\\eclipse-workspace\\Server\\src\\test\\dir1\\mmm.txt");
//		try {
//			Files.copy(f.toPath(), out.toPath());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		f.delete();
//		System.out.println(f.list().length);
//		String s = "CA";
//		String[] rs = s.split(",");
//		System.out.println(rs[0]);
//		File f = new File("C:\\Users\\Milos\\eclipse-workspace\\Server\\users");
//		f.delete();
	}
		
	
}

