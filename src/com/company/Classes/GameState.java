package com.company.Classes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameState {
    public BufferedImage menuBackgroundImg, menuButton_StartGame, menuButton_Options, menuButton_Exit, overStart, overOptions, overExit;
    private int w, h; //display width and height
    public int imgW, imgH, imgYButtonOffSet, imgX, imgYOffSet;
    public GameState(int width, int height){
        isMenu = true;
        someoneWon = isGame = isLoading = startGame = isOptions = false;
        w=width;
        h=height;
        imgW = (int)(w*0.2); // width of the button
        imgX = (int)(w/2 - (int)(w*0.20)/2); // button X position
        imgH = (int)((h/6)*0.8); // height of the button
        imgYButtonOffSet = (int)(h*0.05); // space/offset between buttons
        imgYOffSet = (int)(h/5); // first button's Y position
        try {
            menuBackgroundImg = ImageIO.read(new File("src/com/company/Images/MenuBackgroundImg.png"));
            menuButton_StartGame = ImageIO.read(new File("src/com/company/Images/MenuButton_Start.png"));
            overStart = ImageIO.read(new File("src/com/company/Images/StartHover.png"));
            menuButton_Options = ImageIO.read(new File("src/com/company/Images/MenuButton_Options.png"));
            overOptions = ImageIO.read(new File("src/com/company/Images/OptionsHover.png"));
            menuButton_Exit = ImageIO.read(new File("src/com/company/Images/MenuButton_Exit.png"));
            overExit = ImageIO.read(new File("src/com/company/Images/ExitHover.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isMenu, isOptions, isGame, startGame, isLoading, overStartButton, overOptionsButton, overExitButton, someoneWon;

    public void render(Graphics g){
        g.drawImage(menuBackgroundImg, 0, 0, w, h, null);
        if(!overStartButton) {
            g.drawImage(menuButton_StartGame, imgX, imgYOffSet, imgW, imgH, null);
        }else{
            g.drawImage(overStart, imgX, imgYOffSet, imgW, imgH, null);
        }
        if(!overOptionsButton) {
            g.drawImage(menuButton_Options, imgX, imgYOffSet + imgH + imgYButtonOffSet, imgW, imgH, null);
        }else{
            g.drawImage(overOptions, imgX, imgYOffSet + imgH + imgYButtonOffSet, imgW, imgH, null);
        }
        if(!overExitButton) {
            g.drawImage(menuButton_Exit, imgX, imgYOffSet + 2*(imgH + imgYButtonOffSet), imgW, imgH, null );
        }else{
            g.drawImage(overExit, imgX, imgYOffSet + 2*(imgH + imgYButtonOffSet), imgW, imgH, null );
        }

    }
}
