package com.company.Classes;

import com.company.Engine.Game;
import com.company.MultiPlayer.PlayerMP;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public final class Phase  implements Serializable {

    public int currentRound;
    private static int phaseIconWidth = 50, phaseIconHeight = 50;
    private static int endTurnImgWidth = 100, endTurnImgHeight = 100;
    private static int endTurnPosX;
    private static int endTurnPosY;
    public boolean start, attack, end, enemy;
    private int width, height;
    private PlayerMP player1;
    private PlayerMP player2;
    public PlayerMP currentPlayer;
    public long startTime;
    public long elapsedTime;
    public Game game;
    public boolean sendToOther = false;
    public ImageIcon phaseStartImg, phaseAttackImg, phaseEndImg, phaseEnemyTurnImg, currentPhaseImg, endTurnImg, endPhaseImg, currentEndImg;
    public Phase(int w, int h, PlayerMP p1, PlayerMP p2, Game game){
        width = w;
        height = h;
        this.LoadImages();
        //start = true;
        attack = true;
        //end = false;
        enemy = false;
        currentRound = 1;
        currentPhaseImg = phaseStartImg;
        player1 = p1;
        player1.setPhase(this);
        player2 = p2;
        player2.setPhase(this);
        currentPlayer = p1;

        startPhaseActions();
        phaseIconWidth = (int)(width * 0.1);
        phaseIconHeight = (int)(height * 0.05);
        endTurnImgWidth = (int)(width * 0.1);
        endTurnImgHeight = (int)(height * 0.05);
        this.game = game;
    }

    private void LoadImages(){

            phaseStartImg = new ImageIcon("src/com/company/Images/phase1.png");
            phaseAttackImg = new ImageIcon("src/com/company/Images/phase2.png");
            phaseEndImg = new ImageIcon("src/com/company/Images/phase3.png");
            phaseEnemyTurnImg = new ImageIcon("src/com/company/Images/phase4.png");
            endTurnImg = new ImageIcon("src/com/company/Images/endTurn.png");
            endPhaseImg = new ImageIcon("src/com/company/Images/endPhase.png");


        endTurnPosX = (int)(width * 0.87);
        endTurnPosY = (int)(height * 0.375);
        endTurnImgHeight = endTurnImg.getIconHeight();
        endTurnImgWidth = endTurnImg.getIconWidth();
        phaseIconWidth = phaseEndImg.getIconWidth();
        phaseIconHeight = phaseEndImg.getIconHeight();
    }
    public void nextPhase(){
        if(this.attackPhase()){
            currentPlayer = currentPlayer.opponent;
            attack = false;
            enemy = true;
            currentPhaseImg = endPhaseImg;
            startPhaseActions();
            sendToOther = true;
        }else if(this.enemyTurn()){
            if(currentPlayer.getID() == ID.Player2){
                currentRound++;
            }
            currentPlayer = currentPlayer.opponent;
            attack = true;

            currentPhaseImg = phaseAttackImg;
            startPhaseActions();
        }
    }

//    public void nextPhase(){
//        if(this.startPhase()){
//            if(currentRound != 1){
//                attack = true;
//                currentPhaseImg = phaseAttackImg;
//            }else{ // Cannot attack during the first round
//                end = true;
//                currentPhaseImg = phaseEndImg;
//            }
//            start = false;
//        }else if(this.attackPhase()){
//            end = true;
//            attack = false;
//            currentPhaseImg = phaseEndImg;
//        }else if(this.endPhase()){
//            for(CardSlot c : currentPlayer.playerBoardSlots){
//                if(c.cardOnBoard() && c.getCard().getID() == ID.Monster && ((Monster)c.getCard()).stunTime > 0){
//                    ((Monster)c.getCard()).removeStun();
//                }
//            }
//            enemy = true;
//            end = false;
//            currentPhaseImg = phaseEnemyTurnImg;
//            sendToOther = true;
//            currentPlayer = currentPlayer.opponent;
//            startPhaseActions();
//            //enemyTurnSequence();
//        }/*else if(this.enemyTurn()){
//            enemy = false;
//            currentRound++;
//            currentPlayer = currentPlayer.opponent;
//            start = true;
//            currentPhaseImg = phaseStartImg;
//
//            startPhaseActions();
//            //startPhaseActions();
//        }*/
//    }

    /*
    public void enemyTurnSequence(){enemyStartPhase();}
    public void enemyAttackPhase(){((AIPlayer)(currentPlayer)).attackAI();}
    public void enemyStartPhase(){
        startPhaseActions();
        ((AIPlayer)(currentPlayer)).startPhaseSequenceAI();
        enemyAttackPhase();
        for(int i = 0; i < currentPlayer.playerBoardSlots.size(); i++){
            CardSlot slot = currentPlayer.playerBoardSlots.get(i);
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster && ((Monster)slot.getCard()).stunTime > 0){
                ((Monster)slot.getCard()).removeStun();
                System.out.println("Removed stun");
            }
        }
    }
    */
    public void startPhaseActions(){
        if(currentRound > 1){
            currentPlayer.addMana();
            currentPlayer.refillMana();
        }
        System.out.println("Current player turn: " + currentPlayer.getID());
        currentPlayer.drawCard();
        for(int i = 0; i < currentPlayer.playerBoardSlots.size(); i++){
            CardSlot slot = currentPlayer.playerBoardSlots.get(i);
            slot.resetAttackedThisTurn();
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
                ((Monster)slot.getCard()).setWasAttacked(false);
                if(((Monster)slot.getCard()).stunTime > 0){
                    ((Monster)slot.getCard()).removeStun();
                }
            }
        }
        startTime = System.nanoTime();
    }
    public PlayerMP getOpponent(){
        return currentPlayer.opponent;
    }
    public boolean weHaveAWinner(){
        if(currentPlayer.getHP() <= 0 || currentPlayer.opponent.getHP() <= 0){
            return true;
        }else{
            return false;
        }
    }
    public void updateTime(){
        elapsedTime = (System.nanoTime() - startTime)/1000000000;
    }
    public void checkTime(){
        if(elapsedTime >= 35){
            while(!this.enemyTurn()){
                nextPhase();
            }
        }
    }
    public ImageIcon GetCurrentPhaseImage(){
        System.out.println(String.format("Current: %s me: %s", currentPlayer.getID().toString(), this.game.ME.getID().toString()));
        if(currentPlayer.getID() != this.game.ME.getID()){
            System.out.println("T");
            return phaseEnemyTurnImg;
        }else{
            System.out.println("F");
            return phaseAttackImg;
        }
    }

    public ImageIcon GetEndTurnImage(){
        return currentEndImg;
    }
    public PlayerMP getCurrentPlayer(){
        return this.currentPlayer;
    }
    public int GetPhaseIconWidth(){
       return phaseIconWidth;
    }

    public int GetPhaseIconHeight(){
        return phaseIconHeight;
    }

    public int GetEndTurnImgWidth(){
        return endTurnImgWidth;
    }

    public int GetEndTurnImgHeight(){
        return endTurnImgHeight;
    }

    public int GetEndTurnPosX(){
        return endTurnPosX;
    }

    public int GetEndTurnPosY(){
        return endTurnPosY;
    }

    public boolean startPhase(){ return this.start; }
    public boolean attackPhase(){ return this.attack; }
    public boolean endPhase() { return this.end; }
    public boolean enemyTurn() { return this.enemy; }
    public int getCurrentRound(){ return this.currentRound; }
}
