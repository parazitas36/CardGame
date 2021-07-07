package com.company.Classes;

import com.company.Database.DatabaseHandler;
import com.company.Engine.Game;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class GameState  implements Serializable {
    public ImageIcon menuBackgroundImg, menuButton_StartGame, menuButton_Options, menuButton_Exit,
            optionsButton_res1, optionsButton_res2, optionsButton_res3, optionsButton_back, optionsButton_back2, optionsOver1, optionsOver2, optionsOver3, overStart, overOptions,
            overExit,
            mpButton, mpHover, connectButton, connectButtonHover, statusConnected, statusDisconnected, readyBtn, readyGreenBtn;
    private int w, h; //display width and height
    public int imgW, imgH, imgYButtonOffSet, imgX, imgYOffSet;
    private Game game;
    public boolean isMenu, isOptions, isGame, startGame, isLoading, isMP, isConnected, overStartButton, overOptionsButton, overExitButton, overBackButton, overConnectButton, overMP, ready, someoneWon, celebrationWindow, isWaiting;
    private String waiting = "Waiting for opponent...";
    public long lastTime;

    public GameState(int width, int height, Game game){
        this.game = game;
        isMenu = true;
        celebrationWindow = someoneWon = isGame = isLoading  = isOptions = startGame = isMP = isConnected = overStartButton =
                overOptionsButton = overExitButton = overBackButton = overMP = isWaiting = overConnectButton = ready = false;
        isWaiting = true;
        lastTime = System.nanoTime();
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
            mpButton = new ImageIcon("src/com/company/Images/MultiPlayer_Button.png");
            mpHover = new ImageIcon("src/com/company/Images/MultiPlayerHover.png");
            connectButton = new ImageIcon("src/com/company/Images/ConnectToServer.png");
            connectButtonHover = new ImageIcon("src/com/company/Images/ConnectToServer_Hover.png");
            statusConnected = new ImageIcon("src/com/company/Images/connected.png");
            statusDisconnected = new ImageIcon("src/com/company/Images/disconnected.png");
            readyBtn = new ImageIcon("src/com/company/Images/READY.png");
            readyGreenBtn = new ImageIcon("src/com/company/Images/READY_GREEN.png");

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


    public void render(Graphics g){

        if(isMenu) {
            g.drawImage(menuBackgroundImg.getImage(), 0, 0, w, h, null);
            g.drawImage(mpButton.getImage(), 0, 0, imgW, imgH, null);
            if (!overStartButton) {
                g.drawImage(menuButton_StartGame.getImage(), imgX, imgYOffSet, imgW, imgH, null);
            } else {
                g.drawImage(overStart.getImage(), imgX, imgYOffSet, imgW, imgH, null);
            }
            if (!overOptionsButton) {
                g.drawImage(menuButton_Options.getImage(), imgX, imgYOffSet + imgH + imgYButtonOffSet, imgW, imgH, null);
            } else {
                g.drawImage(overOptions.getImage(), imgX, imgYOffSet + imgH + imgYButtonOffSet, imgW, imgH, null);
            }
            if (!overExitButton) {
                g.drawImage(menuButton_Exit.getImage(), imgX, imgYOffSet + 2 * (imgH + imgYButtonOffSet), imgW, imgH, null);
            } else {
                g.drawImage(overExit.getImage(), imgX, imgYOffSet + 2 * (imgH + imgYButtonOffSet), imgW, imgH, null);
            }
            if (!overMP) {
                g.drawImage(mpButton.getImage(), 0, 0, imgW, imgH, null);
            } else {
                g.drawImage(mpHover.getImage(), 0, 0, imgW, imgH, null);
            }
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
        if(isMP){
            g.drawImage(menuBackgroundImg.getImage(), 0, 0, w, h, null);
            if(!isConnected) {
                int yOffSet = h - (int)(h * 0.1);
                String text = "Connection: status: ";
                String status = "disconnected";
                g.drawString(text, 5, yOffSet);
                Color prevColor = g.getColor();
                g.setColor(Color.red);
                g.drawString(status, 5 + text.length() * g.getFont().getSize()/2 + statusDisconnected.getIconWidth()/2, yOffSet);
                g.setColor(prevColor);
                g.drawImage(statusDisconnected.getImage(), 3 + text.length() * g.getFont().getSize()/2, yOffSet-statusDisconnected.getIconHeight()/2 + statusDisconnected.getIconHeight()/10, statusDisconnected.getIconWidth()/2, statusDisconnected.getIconHeight()/2, null);
                if(!overConnectButton){
                    g.drawImage(connectButton.getImage(), w/2 - connectButton.getIconWidth()/2, h/2 - connectButton.getIconHeight()/2, connectButton.getIconWidth(), connectButton.getIconHeight(), null);
                }else{
                    g.drawImage(connectButtonHover.getImage(), w/2 - connectButtonHover.getIconWidth()/2, h/2 - connectButtonHover.getIconHeight()/2, connectButtonHover.getIconWidth(), connectButtonHover.getIconHeight(), null);
                }
            }else {
                if(!game.isGuest) {
                    int yOffSet = h - (int)(h * 0.1);
                    String text = "Connection: status: ";
                    String status = "connected";
                    g.drawString(text, 5, yOffSet);
                    Color prevColor = g.getColor();
                    g.setColor(Color.green);
                    g.drawString(status, 5 + text.length() * g.getFont().getSize()/2 + statusDisconnected.getIconWidth()/2, yOffSet);
                    g.setColor(prevColor);
                    g.drawImage(statusConnected.getImage(), 3 + text.length() * g.getFont().getSize()/2, yOffSet-statusConnected.getIconHeight()/2 + statusConnected.getIconHeight()/10, statusConnected.getIconWidth()/2, statusConnected.getIconHeight()/2, null);
                    DatabaseHandler.User user = game.dbHandler.getUser();
                    String username = "Connected as: " + user.username;
                    String wins = String.format("Wins: %d", user.wins).trim();
                    String losses = String.format("Losses: %d", user.losses).trim();
                    String rating = String.format("Rating: %d", user.rating).trim();
                    Color oldColor = g.getColor();
                    g.setColor(Color.BLACK);
                    g.drawRect(0, 0, this.w, this.h);
                    g.setColor(Color.white);
                    Font oldFont = g.getFont();
                    int fontSize = (h + w) / 60;
                    Font smallerFont = new Font(Font.SANS_SERIF, Font.BOLD, fontSize/2);
                    Font newFont = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
                    g.setFont(newFont);
                    yOffSet = h / 10;
                    int xOffSet = w / 2;
                    g.drawString(username, (xOffSet - fontSize * username.length() / 4), yOffSet);
                    g.drawString("Your stats:", 5, (int)(1.5 * yOffSet));
                    g.setColor(Color.YELLOW);
                    g.setFont(smallerFont);
                    g.drawString(wins, 10, 2 * yOffSet);
                    g.drawString(losses, 10, (int)(2.5 * yOffSet));
                    g.drawString(rating, 10, 3 * yOffSet);
                    if(isWaiting){
                        String waiting = liveWaitingForOpponent();
                        g.setColor(Color.WHITE);
                        g.setFont(newFont);
                        g.drawString(waiting, (xOffSet - fontSize * "Waiting for opponent".length() / 4), 5 * yOffSet);
                    }else{
                        String oppConnected = "Your opponent: " + game.tcpClient.oppUsername + " has connected.";
                        String message = " Press READY when you are ready to play.";
                        g.setColor(Color.WHITE);
                        g.setFont(newFont);
                        g.drawString(oppConnected, (xOffSet - fontSize * oppConnected.length() / 4), 5 * yOffSet);
                        g.drawString(message, (xOffSet - fontSize * message.length() / 4), (int)(5.5 * yOffSet));
                        if(!ready) {
                            g.drawImage(readyBtn.getImage(), xOffSet - readyBtn.getIconWidth() / 2, (int) (5.5 * yOffSet), readyBtn.getIconWidth(), readyBtn.getIconHeight(), null);
                        }else{
                            g.drawImage(readyGreenBtn.getImage(), xOffSet - readyGreenBtn.getIconWidth() / 2, (int) (5.5 * yOffSet), readyGreenBtn.getIconWidth(), readyGreenBtn.getIconHeight(), null);
                        }
                    }
                    g.setFont(oldFont);
                    g.setColor(oldColor);
                }else{
                    int yOffSet = h - (int)(h * 0.1);
                    String text = "Connection: status: ";
                    String status = "connected";
                    g.drawString(text, 5, yOffSet);
                    Color prevColor = g.getColor();
                    g.setColor(Color.green);
                    g.drawString(status, 5 + text.length() * g.getFont().getSize()/2 + statusDisconnected.getIconWidth()/2, yOffSet);
                    g.setColor(prevColor);
                    g.drawImage(statusConnected.getImage(), 3 + text.length() * g.getFont().getSize()/2, yOffSet-statusConnected.getIconHeight()/2 + statusConnected.getIconHeight()/10, statusConnected.getIconWidth()/2, statusConnected.getIconHeight()/2, null);
                    String username = "Connected as: Guest";
                    Color oldColor = g.getColor();
                    g.setColor(Color.white);
                    Font oldFont = g.getFont();
                    int fontSize = (h + w) / 60;
                    Font newFont = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
                    g.setFont(newFont);
                    yOffSet = h / 10;
                    int xOffSet = w / 2;
                    g.drawString(username, (xOffSet - fontSize * username.length() / 4), yOffSet);
                    if(isWaiting){
                        String waiting = liveWaitingForOpponent();
                        g.setColor(Color.WHITE);
                        g.setFont(newFont);
                        g.drawString(waiting, (xOffSet - fontSize * "Waiting for opponent".length() / 4), 5 * yOffSet);
                    }else{
                        String oppConnected = "Your opponent: " + game.tcpClient.oppUsername + " has connected.";
                        String message = " Press READY when you are ready to play.";
                        g.setColor(Color.WHITE);
                        g.setFont(newFont);
                        g.drawString(oppConnected, (xOffSet - fontSize * oppConnected.length() / 4), 5 * yOffSet);
                        g.drawString(message, (xOffSet - fontSize * message.length() / 4), (int)(5.5 * yOffSet));
                        if(!ready) {
                            g.drawImage(readyBtn.getImage(), xOffSet - readyBtn.getIconWidth() / 2, (int) (5.5 * yOffSet), readyBtn.getIconWidth(), readyBtn.getIconHeight(), null);
                        }else{
                            g.drawImage(readyGreenBtn.getImage(), xOffSet - readyGreenBtn.getIconWidth() / 2, (int) (5.5 * yOffSet), readyGreenBtn.getIconWidth(), readyGreenBtn.getIconHeight(), null);
                        }
                    }
                    g.setFont(oldFont);
                    g.setColor(oldColor);
                }
            }
        }

    }

    public String liveWaitingForOpponent(){
        if(this.waiting.equals("Waiting for opponent") && System.nanoTime() - lastTime > 500000000){
            this.waiting = "Waiting for opponent.";
            lastTime = System.nanoTime();
            return this.waiting;
        }else if(this.waiting.equals("Waiting for opponent.") && System.nanoTime() - lastTime > 500000000){
            this.waiting = "Waiting for opponent..";
            lastTime = System.nanoTime();
            return this.waiting;
        }else if(this.waiting.equals("Waiting for opponent..") && System.nanoTime() - lastTime > 500000000){
            this.waiting = "Waiting for opponent...";
            lastTime = System.nanoTime();
            return this.waiting;
        }else if(this.waiting.equals("Waiting for opponent...") && System.nanoTime() - lastTime > 500000000) {
            this.waiting = "Waiting for opponent";
            lastTime = System.nanoTime();
            return this.waiting;
        }else{
            return this.waiting;
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
