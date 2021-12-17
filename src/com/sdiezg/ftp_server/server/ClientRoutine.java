package com.sdiezg.ftp_server.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;

import com.sdiezg.ftp_server.resources.Colors;
import com.sdiezg.ftp_server.resources.FileManager;
import com.sdiezg.ftp_server.resources.FileReceiver;
import com.sdiezg.ftp_server.resources.FileSender;
import com.sdiezg.ftp_server.resources.Menu;
import com.sdiezg.ftp_server.resources.Colors.EColors;

public class ClientRoutine extends Thread{
    // ATTRIBUTES
    private Socket socket;
    private FileManager fileManager;
	private boolean activo;
	int clietnNum;
    
    // CONSTRUCTORS
    public ClientRoutine(Socket socket, FileManager fileManager, int clientNum) {
        this.socket = socket;
        this.fileManager = fileManager;
		this.activo = true;
		this.clietnNum = clientNum;
    }
    
    // METHDOS
    @Override
    public void run() {
		Menu menu = new Menu();
		String currentCategory = "main";
		String msgForClient;

		log("Client connected");
		
		try {
			DataInputStream inStream = new DataInputStream(socket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

			while (activo) {
				if (currentCategory.equals("listUpload")) 
					msgForClient = "listUpload/";
				else
					msgForClient = menu.optionListToText(currentCategory);

				outStream.writeUTF(msgForClient);
			
				int clientPick = inStream.readInt();
				log("Client pick: " + clientPick);

				if (currentCategory.equals("main")) {
					switch (clientPick) {
						/*
							Codigos de comunicación con el cliente:
							0 - Terminar comunicación
							1 - Activar el envio de archivos en el server
							2 - Activar la recepción de archivos en el server
						*/
						case 0:
							this.activo = false;
							break;
	
						case 1:
							currentCategory = "listDownload";
							menu.refreshOptionsList(currentCategory, fileManager.getFileNames());
							break;
						
						case 2:
							currentCategory = "listUpload";
							break;

						default:
							currentCategory = "main";
							break;
					}
					
				}
				else {
					if (clientPick == 0) {
						currentCategory = "main";
					}
					else {
						if (currentCategory.equals("listDownload")) {
							if (clientPick - 1 < fileManager.getFiles().size()) {
								// Inicio de la descarga
								outStream.writeUTF("startDownload");
								File file = fileManager.getFiles().get(clientPick - 1);
								FileSender fs = new FileSender(socket, file);
								Thread.sleep(100);
								fs.start();
								fs.join();
								currentCategory = "main";
								continue;
							}
						}
						else if(currentCategory.equals("listUpload")) {
							// Inicio de la subida
							outStream.writeUTF("startUpload");
							FileReceiver fr = new FileReceiver(socket, fileManager);

							fr.start();
							fr.join();
							currentCategory = "main";
							continue;
						}
					}
				}
			}
				
        } catch (Exception e) {
            //e.printStackTrace();
        }

		log("Client disconnected");
    }

	private void log(String msg) {
		System.out.print("[" + clietnNum + "] ");
		Colors.printlnString(EColors.CYAN, msg);
	}
    
    // GETTERS AND SETTERS
    
    
}
