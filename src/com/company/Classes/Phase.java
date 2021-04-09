package com.company.Classes;

import com.company.Engine.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Phase {

    public static boolean isMyTurn;
    public static int currentPhase = 1;

    private static int phaseIconWidth = 50, phaseIconHeight = 50;
    private static int endTurnImgWidth = 100, endTurnImgHeight = 100;
    private static int endTurnPosX;
    private static int endTurnPosY;

    private static BufferedImage phaseStartImg, phaseAttackImg, phaseEndImg, phaseEnemyTurnImg, currentPhaseImg, endTurnImg, endPhaseImg, currentEndImg;

    public static void LoadImages(){
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

        endTurnPosX = Game.GetWidth() - (int)(1.5 * endTurnImgWidth);
        endTurnPosY = Game.GetHeight()/2 - (int)(1.5 * endTurnImgHeight);

        currentPhaseImg = phaseStartImg;
        currentEndImg = endPhaseImg;

        currentPhase = 1;
    }

    public static void NextPhase(){
        currentPhase++;
        if(currentPhase == 1){
            // start phase
            // add mana to the player
            currentPhaseImg = phaseStartImg;
            currentEndImg = endPhaseImg;
        }else if(currentPhase == 2){
            // attack phase
            currentPhaseImg = phaseAttackImg;
            currentEndImg = endPhaseImg;
        }else if(currentPhase == 3){
            // end phase
            currentPhaseImg = phaseEndImg;
            currentEndImg = endTurnImg;
        }else{
            // enemy turn
            currentPhaseImg = phaseEnemyTurnImg;
            StarEnemyTurn();
        }
    }

    public static BufferedImage GetCurrentPhaseImage(){
        return currentPhaseImg;
    }

    public static BufferedImage GetEndTurnImage(){
        return currentEndImg;
    }

    public static int GetPhaseIconWidth(){
       return phaseIconWidth;
    }

    public static int GetPhaseIconHeight(){
        return phaseIconHeight;
    }

    public static int GetEndTurnImgWidth(){
        return endTurnImgWidth;
    }

    public static int GetEndTurnImgHeight(){
        return endTurnImgHeight;
    }

    public static int GetEndTurnPosX(){
        return endTurnPosX;
    }

    public static int GetEndTurnPosY(){
        return endTurnPosY;
    }

    public static void StartPlayerTurn(){
        currentPhase = 1;
        isMyTurn = true;
    }
    private static void StarEnemyTurn(){
        isMyTurn = false;
    }
}
