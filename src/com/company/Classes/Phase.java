package com.company.Classes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Phase {

    private int currentRound;
    private static int phaseIconWidth = 50, phaseIconHeight = 50;
    private static int endTurnImgWidth = 100, endTurnImgHeight = 100;
    private static int endTurnPosX;
    private static int endTurnPosY;
    private boolean start, attack, end, enemy;
    private int width, height;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    public long startTime;
    public long elapsedTime;
    private BufferedImage phaseStartImg, phaseAttackImg, phaseEndImg, phaseEnemyTurnImg, currentPhaseImg, endTurnImg, endPhaseImg, currentEndImg;
    public Phase(int w, int h, Player p1, Player p2){
        width = w;
        height = h;
        this.LoadImages();
        start = true;
        attack = false;
        end = false;
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
    }

    private void LoadImages(){
        try{
            phaseStartImg = ImageIO.read(new File("src/com/company/Images/phase1.png"));
            phaseAttackImg = ImageIO.read(new File("src/com/company/Images/phase2.png"));
            phaseEndImg = ImageIO.read(new File("src/com/company/Images/phase3.png"));
            phaseEnemyTurnImg = ImageIO.read(new File("src/com/company/Images/phase4.png"));
            endTurnImg = ImageIO.read(new File("src/com/company/Images/endTurn.png"));
            endPhaseImg = ImageIO.read(new File("src/com/company/Images/endPhase.png"));
        }catch (IOException e){
            e.printStackTrace();
        }

        endTurnPosX = (int)(width * 0.87);
        endTurnPosY = (int)(height * 0.375);
        endTurnImgHeight = endTurnImg.getHeight();
        endTurnImgWidth = endTurnImg.getWidth();
        phaseIconWidth = phaseEndImg.getWidth();
        phaseIconHeight = phaseEndImg.getHeight();
    }
    public void nextPhase(){
        if(this.startPhase()){
            if(currentRound != 1){
                attack = true;
                currentPhaseImg = phaseAttackImg;
            }else{ // Cannot attack during the first round
                end = true;
                currentPhaseImg = phaseEndImg;
            }
            start = false;
        }else if(this.attackPhase()){
            end = true;
            attack = false;
            currentPhaseImg = phaseEndImg;
        }else if(this.endPhase()){
            for(CardSlot c : currentPlayer.playerBoardSlots){
                if(c.cardOnBoard() && c.getCard().getID() == ID.Monster && ((Monster)c.getCard()).stunTime > 0){
                    ((Monster)c.getCard()).removeStun();
                }
            }
            enemy = true;
            end = false;
            currentPhaseImg = phaseEnemyTurnImg;
            currentPlayer = currentPlayer == player1 ? player2 : player1;
            enemyTurnSequence();
        }else if(this.enemyTurn()){
            start = true;
            enemy = false;
            currentRound++;
            currentPhaseImg = phaseStartImg;
            currentPlayer = currentPlayer == player1 ? player2 : player1;
            startPhaseActions();
        }
    }
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
    public void startPhaseActions(){
        if(currentRound > 1){
            currentPlayer.addMana();
            currentPlayer.refillMana();

        }
        currentPlayer.drawCard();
        for(int i = 0; i < currentPlayer.playerBoardSlots.size(); i++){
            CardSlot slot = currentPlayer.playerBoardSlots.get(i);
            slot.resetAttackedThisTurn();
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
                ((Monster)slot.getCard()).setWasAttacked(false);
            }
        }
        startTime = System.nanoTime();
    }
    public Player getOpponent(){
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
    public BufferedImage GetCurrentPhaseImage(){
        return currentPhaseImg;
    }

    public BufferedImage GetEndTurnImage(){
        return currentEndImg;
    }
    public Player getCurrentPlayer(){
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
