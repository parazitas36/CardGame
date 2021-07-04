package com.company.TCPMP;

import com.company.MultiPlayer.GameServer;
import com.company.Utils.CardReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class TCPServer implements Serializable{
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private int numOfPlayers;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private String[] player1Lines;
    private String[] player2Lines;

    public TCPServer(){
        System.out.println("----- SERVER INITIALIZED -----");
        try {
            player1Lines = CardReader.ReadLines("src/com/company/Assets/Cards_Data.txt");
            player2Lines = CardReader.ReadLines("src/com/company/Assets/Cards_Data.txt");
            serverSocket = new ServerSocket(1331);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void acceptConnections(){
        try{
            System.out.println("Waiting for connections...");
            while(numOfPlayers < 2){
                Socket s = serverSocket.accept();
                numOfPlayers++;
                System.out.println("Player #"+numOfPlayers + " has connected...");
                ServerSideConnection ssc = new ServerSideConnection(s, numOfPlayers);
                if(numOfPlayers == 1){
                    player1 = ssc;
                }else{
                    player2 = ssc;
                }
                Thread t = new Thread(ssc);
                t.start();
            }

            sendLines(player1Lines, 1);
            sendLines(player2Lines, 2);
            sendOppLines(player2Lines, 1);
            sendOppLines(player1Lines, 2);
            System.out.println("Possible to start a game.");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private class ServerSideConnection implements Runnable{
        private Socket ssocket;
        private DataInputStream input;
        private DataOutputStream output;
        private int playerID;
        public ServerSideConnection(Socket s, int id){
            ssocket = s;
            playerID = id;
            try{
                input = new DataInputStream(ssocket.getInputStream());
                output = new DataOutputStream(ssocket.getOutputStream());
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try{
                output.writeInt(playerID);
                output.flush();
                while(true){
                    if(playerID == 1) {
                        int message = input.readInt();
                        System.out.println(" 1 Message in run: " );
                        if(message == 1) {
                            player2.output.writeInt(message);
                            player2.output.flush();
                        }
                    }else{
                        int message = input.readInt(); // 1 = Phase
                        System.out.println(" 2 Message in run: ");
                        if(message == 1) {
                            player1.output.writeInt(message);
                            player1.output.flush();
                        }
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void sendLines(String[] lines, int id){
        try {
            if(id == 1) {
                player1.output.writeInt(2);
                player1.output.writeInt(lines.length);
                for (String line : lines) {
                    player1.output.writeUTF(line);
                }
                player1.output.flush();
            }else {
                player2.output.writeInt(2);
                player2.output.writeInt(lines.length);
                for (String line : lines) {
                    player2.output.writeUTF(line);
                }
                player2.output.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void sendOppLines(String[] lines, int id){
        try {
            if(id == 1) {
                player1.output.writeInt(3);
                player1.output.writeInt(lines.length);
                for (String line : lines) {
                    player1.output.writeUTF(line);
                }
                player1.output.flush();
            }else {
                player2.output.writeInt(3);
                player2.output.writeInt(lines.length);
                for (String line : lines) {
                    player2.output.writeUTF(line);
                }
                player2.output.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        TCPServer server = new TCPServer();
        server.acceptConnections();
    }
}
