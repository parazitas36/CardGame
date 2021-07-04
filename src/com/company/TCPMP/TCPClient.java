package com.company.TCPMP;

import com.company.Classes.ID;
import com.company.Engine.Game;

import java.io.*;
import java.net.Socket;

public class TCPClient implements Runnable{
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private final Thread thread;
    public String[] cardLines;
    public String[] cardOppLines;
    public Game game;
    public int playerNr;
    public TCPClient(String hostIP, Game game){
        try {
            socket = new Socket(hostIP, 1331);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            playerNr = input.readInt();
            System.out.println("You are a player #" + this.playerNr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.game = game;
        thread = new Thread(this);
        thread.start();
    }
    public void getUpdate(){
        try {
            int message = input.readInt();
            if(message == 1){
                System.out.println("Got phase update");
                game.phase.attack = true;
                game.phase.enemy = false;
                game.phase.currentPhaseImg = game.phase.phaseStartImg;
                if(this.game.phase.currentPlayer.getID() == ID.Player2){
                    game.phase.currentRound++;
                }
                game.phase.currentPlayer = game.phase.currentPlayer.opponent;
                game.phase.startPhaseActions();
           }else if(message == 2){
                int size = input.readInt();
                cardLines = new String[size];
                for(int i = 0; i < size; i++){
                    cardLines[i] = input.readUTF();
                    System.out.println(cardLines[i]);
                }
            }else if(message == 3){
                int size = input.readInt();
                cardOppLines = new String[size];
                for(int i = 0; i < size; i++){
                    cardOppLines[i] = input.readUTF();
                    System.out.println();
                }
                game.initMP();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendUpdate(int message){
        try{
            if(message == 1){
                System.out.println("Sending phase update...");
                output.writeInt(message);
                output.flush();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while(true){
            getUpdate();
        }
    }
}
