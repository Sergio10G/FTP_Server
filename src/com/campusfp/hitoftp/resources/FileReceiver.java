package com.campusfp.hitoftp.resources;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.campusfp.hitoftp.resources.Colors.EColors;

public class FileReceiver extends Thread {
	
	//	Attributes
	private Socket socket;
	private FileManager fileManager;
	//private List<byte[]> fileContent;
	private List<String> fileContent;
	
	//	Constructors
	public FileReceiver(Socket socket, FileManager fileManager) {
		this.socket = socket;
		this.fileManager = fileManager;
		this.fileContent = new ArrayList<>();
	}
	
	//	Methods
	@Override
	public void run() {
		try {
			DataInputStream inStream = new DataInputStream(socket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
	
			int serverMsg = inStream.readInt();
			Colors.printlnString(EColors.BLUE, "Receiver received " + serverMsg);
			if (serverMsg == 1) {
				Colors.printlnString(EColors.BLUE, "Receiver sending 1");
				outStream.writeInt(1);
				String fileName = inStream.readUTF();
				Colors.printlnString(EColors.BLUE, "Receiver received " + fileName);

				while (inStream.readInt() == 1) {
					String msg = inStream.readUTF();
					fileContent.add(msg);
					Colors.printlnString(EColors.BLUE, "Reading file content...");
					//Colors.printlnString(EColors.BLUE, msg);
				}

				if (fileManager.createFile(fileName, fileContent)) {
					Colors.printlnString(EColors.GREEN, "Fichero transmitido con éxito!");
				}
				else {
					Colors.printlnString(EColors.RED, "Ha habido algún error al crear el archivo.");
				}

				Colors.printlnString(EColors.BLUE, "Receiver finished");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//	Getters & Setters
	
	
}
