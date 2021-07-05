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
            String message = input.readUTF();
            if(message.trim().equalsIgnoreCase("phase")){
                System.out.println("Got phase update");
//                game.phase.attack = true;
//                game.phase.enemy = false;
//                game.phase.currentPhaseImg = game.phase.phaseStartImg;
////                if(game.ME.getID() == ID.Player1){
////                    game.phase.currentRound++;
////                }
//                game.phase.currentPlayer = game.phase.currentPlayer.opponent;
//                game.phase.startPhaseActions();
                game.phase.nextPhase();
           }else if(message.trim().equalsIgnoreCase("player1Cards")){
                int size = input.readInt();
                cardLines = new String[size];
                for(int i = 0; i < size; i++){
                    cardLines[i] = input.readUTF();
                    System.out.println(cardLines[i]);
                }
            }else if(message.trim().equalsIgnoreCase("player2Cards")){
                int size = input.readInt();
                cardOppLines = new String[size];
                for(int i = 0; i < size; i++){
                    cardOppLines[i] = input.readUTF();
                    System.out.println();
                }
                game.initMP();
            }else if(message.trim().equalsIgnoreCase("placedCard")){
                System.out.println("Atejo placed card");
                int oppHandSlotIndex = input.readInt();
                int boardSlotIndex = input.readInt();
                game.ME.placeCardMP(oppHandSlotIndex, boardSlotIndex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendUpdate(String message){
        try{
            if(message.trim().equalsIgnoreCase("phase")){
                System.out.println("Sending phase update...");
                output.writeUTF(message);
                output.flush();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void sendCardPlaced(int handSlotIndex, int boardSlotIndex){
        try{
            System.out.println(String.format("Sending card [Handslot index: %d] [Board slot index: %d]", handSlotIndex, boardSlotIndex));
            output.writeUTF("placedCard");
            output.writeInt(handSlotIndex);
            output.writeInt(boardSlotIndex);
            output.flush();
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
