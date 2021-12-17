package com.sdiezg.ftp_server.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.sdiezg.ftp_server.resources.Colors;
import com.sdiezg.ftp_server.resources.FileManager;
import com.sdiezg.ftp_server.resources.FileReceiver;
import com.sdiezg.ftp_server.resources.FileSender;
import com.sdiezg.ftp_server.resources.Menu;
import com.sdiezg.ftp_server.resources.Colors.EColors;

public class Main {
	
	public static void main(String[] args) {
		Socket socket;
		Scanner sc = new Scanner(System.in);
		DataInputStream inStream;
		DataOutputStream outStream;
		FileManager fileManager;
		String ip;
		int port;

		Colors.printlnString(EColors.PURPLE, "Bienvenido al servicio de transferencia de archivos FTP");
		Colors.printlnString(EColors.YELLOW, "Introduce la ip del servidor (default 127.0.0.1):");
		ip = sc.nextLine();
		if (ip.equals(""))
			ip = "127.0.0.1";
			while (true) {
			Colors.printlnString(EColors.YELLOW, "Introduce el puerto (default 5111):");
			String portInput = sc.nextLine();
			try {
				if (portInput.equals(""))
					port = 5111;
				else
					port = Integer.parseInt(portInput);
				if (port < 1024 || port > 65535) {
					throw new Exception();
				}
			} catch (Exception e) {
				Colors.printlnString(EColors.RED, "El puerto introducido es inválido. Rango válido [1024 - 65535]");
				continue;
			}
			break;
		}

		

		try {
			socket = new Socket(ip, port);

			inStream = new DataInputStream(socket.getInputStream());
			outStream = new DataOutputStream(socket.getOutputStream());

			fileManager = new FileManager("./clientfiles");
			Menu menu = new Menu();
			String category = "main";
			int pick = -1;
			int pickedFileIndex = 0;

			while (true) {
				String serverMsg = inStream.readUTF();
				//System.out.println(serverMsg);

				category = serverMsg.split("/")[0];
				//System.out.println(category);

				if (category.equals("startDownload")) {
					FileReceiver fr = new FileReceiver(socket, fileManager);
					
					fr.start();
					fr.join();
					continue;
				}
				else if (category.equals("startUpload")) {
					FileSender fs = new FileSender(socket, fileManager.getFiles().get(pickedFileIndex));

					Thread.sleep(100);
					fs.start();
					fs.join();
					continue;
				}
				else if (category.equals("listUpload")) {
					serverMsg = menu.refreshOptionsList(category, fileManager.getFileNames());
				}
				
				Menu.clearTerminal();
				while (true) {
					try {
						menu.printMenuFromText(serverMsg);
						pick = Integer.parseInt(sc.nextLine());
						if (pick < 0) {
							throw new Exception();
						}
						if (category.equals("listUpload")) {
							if (pick - 1 >= fileManager.getFiles().size())
								throw new Exception();
							else
								pickedFileIndex = pick - 1;
						}
					} catch (Exception e) {
						Colors.printlnString(EColors.RED, "La opción introducida es incorrecta.");
						continue;
					}
					break;
				}
				
				outStream.writeInt(pick);

				if (category.equals("main") && pick == 0) {
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

		sc.close();
	}
}
