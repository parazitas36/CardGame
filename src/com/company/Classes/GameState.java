package com.company.Classes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameState {
    public BufferedImage menuBackgroundImg, menuButton_StartGame, menuButton_Options, menuButton_Exit;
    private int w, h;
    public int imgW, imgH, imgYButtonOffSet, imgX, imgYOffSet;
    public GameState(int width, int height){
        isMenu = true;
        isGame = false;
        isOptions = false;
        startGame = false;
        isLoading = false;
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
            menuButton_Options = ImageIO.read(new File("src/com/company/Images/MenuButton_Options.png"));
            menuButton_Exit = ImageIO.read(new File("src/com/company/Images/MenuButton_Exit.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isMenu, isOptions, isGame, startGame, isLoading;

    public void render(Graphics g){
        g.drawImage(menuButton_StartGame, imgX, imgYOffSet, imgW, imgH, null );
        g.drawImage(menuButton_Options, imgX, imgYOffSet + imgH + imgYButtonOffSet, imgW, imgH, null );
        g.drawImage(menuButton_Exit, imgX, imgYOffSet + 2*(imgH + imgYButtonOffSet), imgW, imgH, null );
    }
}
