package com.campusfp.hitoftp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.campusfp.hitoftp.resources.Colors;
import com.campusfp.hitoftp.resources.FileManager;
import com.campusfp.hitoftp.resources.Colors.EColors;

public class Main {

    public static void main(String[] args) {
        try {
            int clientNum = 0;
            ServerSocket serverSocket = new ServerSocket(5111);
            FileManager fileManager = new FileManager("./serverfiles");

			Colors.printlnString(EColors.GREEN, "SERVER RUNNING");
            while (true) {
                Socket socket = serverSocket.accept();

				ClientRoutine cr = new ClientRoutine(socket, fileManager, ++clientNum);
				cr.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
