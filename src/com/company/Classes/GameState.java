package com.company.Classes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.Serializable;

public class GameState  implements Serializable {
    public ImageIcon menuBackgroundImg, menuButton_StartGame, menuButton_Options, menuButton_Exit,
            optionsButton_res1, optionsButton_res2, optionsButton_res3, optionsButton_back, optionsButton_back2, optionsOver1, optionsOver2, optionsOver3, overStart, overOptions,
            overExit,
            multiplayerButtion;
    private int w, h; //display width and height
    public int imgW, imgH, imgYButtonOffSet, imgX, imgYOffSet;
    public GameState(int width, int height){
        isMenu = true;
        celebrationWindow = someoneWon = isGame = isLoading = startGame = isOptions = false;
        w=width;
        h=height;
        imgW = (int)(w*0.2); // width of the button
        imgX = (int)(w/2 - (int)(w*0.20)/2); // button X position
        imgH = (int)((h/6)*0.8); // height of the button
        imgYButtonOffSet = (int)(h*0.05); // space/offset between buttons
        imgYOffSet = (int)(h/5); // first button's Y position
        try {
            menuBackgroundImg = new ImageIcon("src/com/company/Images/MenuBackgroundImg.png");
            menuButton_StartGame = new ImageIcon("src/com/company/Images/MenuButton_Start.png");
            multiplayerButtion = new ImageIcon("src/com/company/Images/MenuButton_Start.png");
            optionsButton_res1 = new ImageIcon("src/com/company/Images/options_1366x768.png");
            optionsButton_res2 = new ImageIcon("src/com/company/Images/options_1600x900.png");
            optionsButton_res3 = new ImageIcon("src/com/company/Images/options_1920x1080.png");
            optionsButton_back = new ImageIcon("src/com/company/Images/options_back.png");
            optionsButton_back2 = new ImageIcon("src/com/company/Images/options_back_white.png");
            optionsOver1 = new ImageIcon("src/com/company/Images/options_1366x768_white.png");
            optionsOver2 = new ImageIcon("src/com/company/Images/options_1600x900_white.png");
            optionsOver3 = new ImageIcon("src/com/company/Images/options_1920x1080_white.png");
            overStart = new ImageIcon("src/com/company/Images/StartHover.png");
            menuButton_Options = new ImageIcon("src/com/company/Images/MenuButton_Options.png");
            overOptions = new ImageIcon("src/com/company/Images/OptionsHover.png");
            menuButton_Exit = new ImageIcon("src/com/company/Images/MenuButton_Exit.png");
            overExit = new ImageIcon("src/com/company/Images/ExitHover.png");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isMenu, isOptions, isGame, startGame, isLoading, overStartButton, overOptionsButton, overExitButton, overBackButton, someoneWon, celebrationWindow, isWaiting;

    public void render(Graphics g){

        g.drawImage(menuBackgroundImg.getImage(), 0, 0, w, h, null);
        g.drawImage(multiplayerButtion.getImage(), 0, 0, imgW, imgH, null);
        if(!overStartButton) {
            g.drawImage(menuButton_StartGame.getImage(), imgX, imgYOffSet, imgW, imgH, null);
        }else{
            g.drawImage(overStart.getImage(), imgX, imgYOffSet, imgW, imgH, null);
        }
        if(!overOptionsButton) {
            g.drawImage(menuButton_Options.getImage(), imgX, imgYOffSet + imgH + imgYButtonOffSet, imgW, imgH, null);
        }else{
            g.drawImage(overOptions.getImage(), imgX, imgYOffSet + imgH + imgYButtonOffSet, imgW, imgH, null);
        }
        if(!overExitButton) {
            g.drawImage(menuButton_Exit.getImage(), imgX, imgYOffSet + 2*(imgH + imgYButtonOffSet), imgW, imgH, null );
        }else{
            g.drawImage(overExit.getImage(), imgX, imgYOffSet + 2*(imgH + imgYButtonOffSet), imgW, imgH, null );
        }

        if(isOptions){
            g.drawImage(menuBackgroundImg.getImage(), 0, 0, w, h, null);
            if(!overStartButton) {
                g.drawImage(optionsButton_res1.getImage(), imgX, imgYOffSet, imgW, imgH, null);
            }else{
                g.drawImage(optionsOver1.getImage(), imgX, imgYOffSet, imgW, imgH, null);
            }
            if(!overOptionsButton) {
                g.drawImage(optionsButton_res2.getImage(), imgX, imgYOffSet + imgH + imgYButtonOffSet, imgW, imgH, null);
            }else{
                g.drawImage(optionsOver2.getImage(), imgX, imgYOffSet + imgH + imgYButtonOffSet, imgW, imgH, null);
            }
            if(!overExitButton) {
                g.drawImage(optionsButton_res3.getImage(), imgX, imgYOffSet + 2*(imgH + imgYButtonOffSet), imgW, imgH, null );
            }else{
                g.drawImage(optionsOver3.getImage(), imgX, imgYOffSet + 2*(imgH + imgYButtonOffSet), imgW, imgH, null );
            }
            if(!overBackButton) {
                g.drawImage(optionsButton_back.getImage(), imgX, imgYOffSet + 3*(imgH + imgYButtonOffSet), imgW, imgH, null );
            }else{
                g.drawImage(optionsButton_back2.getImage(), imgX, imgYOffSet + 3*(imgH + imgYButtonOffSet), imgW, imgH, null );
            }
            return;
        }

    }

    public void setDisplaySize(int width, int height){
        w = width;
        h = height;
        imgW = (int)(w*0.2); // width of the button
        imgX = (int)(w/2 - (int)(w*0.20)/2); // button X position
        imgH = (int)((h/6)*0.8); // height of the button
        imgYButtonOffSet = (int)(h*0.05); // space/offset between buttons
        imgYOffSet = (int)(h/5); // first button's Y position
    }
}
