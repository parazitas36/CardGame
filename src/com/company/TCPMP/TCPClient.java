package com.company.TCPMP;

import com.company.Classes.Buff;
import com.company.Classes.CardSlot;
import com.company.Classes.Curse;
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
    public String oppUsername = null;
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
            }else if(message.trim().equalsIgnoreCase("battle")){
                int attackerIndex = input.readInt();
                int defenderIndex = input.readInt();
                CardSlot attacker = game.ME.opponent.playerBoardSlots.get(attackerIndex);
                CardSlot defender = null;
                if(defenderIndex == 10){
                    System.out.println("attacked enemy");
                    game.attackOpponent(attacker, game.ME.opponent);
                }else{
                    defender = game.ME.playerBoardSlots.get(defenderIndex);
                }
                if(defender != null){
                    game.ME.opponent.attack(attacker, defender);
                }
            }else if(message.trim().equalsIgnoreCase("curse")){
                int curseIndexInHand = input.readInt();
                int cursedCardIndex = input.readInt();
                CardSlot curse = game.ME.opponent.playerHandSlots.get(curseIndexInHand);
                CardSlot cursedSlot = game.ME.playerBoardSlots.get(cursedCardIndex);
                Curse card = (Curse)curse.getCard();
                card.curseLogicMP(cursedSlot, game.ME.opponent, game.ME);
                curse.removeCard();
            }else if(message.trim().equalsIgnoreCase("curseHP")){
                System.out.println("Got curse hp...");
                int curseIndexInHand = input.readInt();
                CardSlot curse = game.ME.opponent.playerHandSlots.get(curseIndexInHand);
                Curse card = (Curse)curse.getCard();
                card.hpCurseLogicMP(game.ME.opponent, game.ME, curse);
            }else if(message.trim().equalsIgnoreCase("buff")){
                System.out.println("Got buff...");
                int buffInHandIndex = input.readInt();
                int targetedCardIndex = input.readInt();
                CardSlot buffSlot = game.ME.opponent.playerHandSlots.get(buffInHandIndex);
                CardSlot targetedSlot = game.ME.opponent.playerBoardSlots.get(targetedCardIndex);
                Buff buff = (Buff)buffSlot.getCard();
                buff.buffLogic(targetedSlot, game.ME.opponent);
                buffSlot.removeCard();
            }else if(message.trim().equalsIgnoreCase("buffHP")){
                System.out.println("Got HP buff...");
                int buffInHandIndex = input.readInt();
                CardSlot buffSlot = game.ME.opponent.playerHandSlots.get(buffInHandIndex);
                Buff buff = (Buff)buffSlot.getCard();
                buff.hpBuffLogic(game.ME.opponent, buffSlot);
            }else if(message.trim().equalsIgnoreCase("OppUsername")){
                this.oppUsername = input.readUTF();
                System.out.println("Got opponent username: " + this.oppUsername);
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
    public void sendAttack(int attackerIndex, int defenderIndex){
        System.out.println("Send attack: [attacker index: " + attackerIndex + "] [defender index: " + defenderIndex + "]");
        try {
            output.writeUTF("battle");
            output.writeInt(attackerIndex);
            output.writeInt(defenderIndex);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendCurse(int curseIndexInHand, int cursedCardIndex){
        try{
            output.writeUTF("curse");
            output.writeInt(curseIndexInHand);
            output.writeInt(cursedCardIndex);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void sendCurseHP(int curseIndexInHand){
        try{
            System.out.println("Played curse hp [index: " + curseIndexInHand +"]");
            output.writeUTF("curseHP");
            output.writeInt(curseIndexInHand);
            output.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void sendBuff(int buffInHandIndex, int targetedCardIndex){
        try{
            System.out.println("Sending buff...");
            output.writeUTF("buff");
            output.writeInt(buffInHandIndex);
            output.writeInt(targetedCardIndex);
            output.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void sendBuffHP(int buffInHandIndex){
        try{
            System.out.println("Sending HP buff...");
            output.writeUTF("buffHP");
            output.writeInt(buffInHandIndex);
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void sendOppUsername(String username){
        try{
            System.out.println("Sending username: " + username);
            output.writeUTF("OppUsername");
            output.writeUTF(username);
            output.flush();
        }catch (IOException e){
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
