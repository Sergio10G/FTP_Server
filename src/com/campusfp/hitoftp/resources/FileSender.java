package com.campusfp.hitoftp.resources;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.net.Socket;

import com.campusfp.hitoftp.resources.Colors.EColors;

public class FileSender extends Thread {
	private final int BUF_SIZE = 16000;
	
	//	Attributes
	private Socket socket;
	private File file;
	
	//	Constructors
	public FileSender(Socket socket, File file) {
		this.socket = socket;
		this.file = file;
	}
	
	//	Methods
	@Override
	public void run() {
		try {
			DataInputStream inStream = new DataInputStream(socket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
	
			Colors.printlnString(EColors.BLUE, "Sender sending 1");
			outStream.writeInt(1);
			int response = inStream.readInt();
			Colors.printlnString(EColors.BLUE, "Sender received " + response);
			if (response == 1) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				outStream.writeUTF(file.getName());
				Colors.printlnString(EColors.BLUE, "Sender sending " + file.getName());

				int off = 0;
				String fileContent = "";
				String line = "";
				while ((line = br.readLine()) != null) {
					fileContent += line + "\n";
				}

				boolean finished = false;
				while (!finished) {

					String msg = "";
					int passedChars = 0;
					while (passedChars < BUF_SIZE) {
						if (passedChars + (off * BUF_SIZE) >= fileContent.length()) {
							finished = true;
							break;
						}
						msg += fileContent.charAt(passedChars + (off * BUF_SIZE));
						passedChars++;
					}
					off++;
					
					if (!msg.equals("")) {
						outStream.writeInt(1);
						outStream.writeUTF(msg);
					}
				}
				Colors.printlnString(EColors.BLUE, "Sender finished");
				outStream.writeInt(0);

				br.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//	Getters & Setters
	
	
}
