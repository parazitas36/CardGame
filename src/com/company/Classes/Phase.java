package com.company.Classes;

import com.company.Engine.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Phase {

    public static boolean isMyTurn;
    public static int currentPhase = 1;
    private int currentRound;
    private static int phaseIconWidth = 50, phaseIconHeight = 50;
    private static int endTurnImgWidth = 100, endTurnImgHeight = 100;
    private static int endTurnPosX;
    private static int endTurnPosY;
    private boolean start, attack, end, enemy;
    private int width, height;
    private BufferedImage phaseStartImg, phaseAttackImg, phaseEndImg, phaseEnemyTurnImg, currentPhaseImg, endTurnImg, endPhaseImg, currentEndImg;
    public Phase(int w, int h){
        width = w;
        height = h;
        this.LoadImages();
        start = true;
        attack = false;
        end = false;
        enemy = false;
        currentRound = 1;
        currentPhaseImg = phaseStartImg;
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

        endTurnPosX = width - (int)(1.5 * endTurnImgWidth);
        endTurnPosY = height/2 - (int)(1.5 * endTurnImgHeight);

       // currentPhaseImg = phaseStartImg;
        //currentEndImg = endPhaseImg;

        currentPhase = 1;
    }
    public void nextPhase(){
        if(this.startPhase()){
            if(currentRound != 1){
                attack = true;
                currentPhaseImg = phaseAttackImg;
            }else{ // Cannot attack during first round
                end = true;
                currentPhaseImg = phaseEndImg;
            }
            start = false;
        }else if(this.attackPhase()){
            end = true;
            attack = false;
            currentPhaseImg = phaseEndImg;
        }else if(this.endPhase()){
            enemy = true;
            end = false;
            currentPhaseImg = phaseEnemyTurnImg;
        }else if(this.enemyTurn()){
            start = true;
            enemy = false;
            currentRound++;
            currentPhaseImg = phaseStartImg;
        }
    }

    // Leave this just in case
//    public void NextPhase(){
//        currentPhase++;
//        if(currentPhase == 1){
//            // start phase
//            // add mana to the player
//            currentPhaseImg = phaseStartImg;
//            currentEndImg = endPhaseImg;
//        }else if(currentPhase == 2){
//            // attack phase
//            currentPhaseImg = phaseAttackImg;
//            currentEndImg = endPhaseImg;
//        }else if(currentPhase == 3){
//            // end phase
//            currentPhaseImg = phaseEndImg;
//            currentEndImg = endTurnImg;
//        }else{
//            // enemy turn
//            currentPhaseImg = phaseEnemyTurnImg;
//            StarEnemyTurn();
//        }
//    }

    public BufferedImage GetCurrentPhaseImage(){
        return currentPhaseImg;
    }

    public BufferedImage GetEndTurnImage(){
        return currentEndImg;
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

    public void StartPlayerTurn(){
        currentPhase = 1;
        isMyTurn = true;
    }
    private void StarEnemyTurn(){
        isMyTurn = false;
    }
    public boolean startPhase(){ return this.start; }
    public boolean attackPhase(){ return this.attack; }
    public boolean endPhase() { return this.end; }
    public boolean enemyTurn() { return this.enemy; }
    public int getCurrentRound(){ return this.currentRound; }
}
