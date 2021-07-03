package com.company.TCPMP;

import com.company.Engine.Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient implements Runnable{
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private final Thread thread;
    public Game game;
    public TCPClient(String hostIP, Game game){
        try {
            socket = new Socket(hostIP, 1331);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            System.out.println("You are a player #" + input.readInt());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.game = game;
        thread = new Thread(this);
        thread.start();
    }
    public void getUpdate(Socket s){
        try {
            System.out.println("Got");
            int message = input.readInt();
            System.out.println("Message: " + message);
            if(message == 1){
                System.out.println("Got phase update");
                game.phase.nextPhase();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendUpdate(int message){
        try{
            if(message == 1){
                System.out.println("Sending phase update...");
                output.writeInt(1);
                output.flush();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while(true){
            getUpdate(socket);
        }
    }
}
